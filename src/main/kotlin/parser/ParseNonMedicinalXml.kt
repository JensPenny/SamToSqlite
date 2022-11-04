package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import db.NonmedicinalTableModel
import fullElement
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.Nonmedicinal
import tryPersist
import java.io.FileInputStream
import java.time.LocalDate
import javax.xml.stream.XMLInputFactory

fun parseNonMedicinalXml(
    inputFactory: XMLInputFactory,
    xmlMapper: ObjectMapper,
    path: String
) {
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    var currentCounter = 0
    val commitAfterAmount = 100

    tryPersist {
        transaction {
            while (reader.hasNext()) {
                val event = reader.nextEvent()

                if (event.isStartElement) {
                    val startElement = event.asStartElement()

                    when (startElement.name.localPart) {
                        "ns3:NonMedicinalProduct" -> {
                            currentCounter++ //Add one to the count of elements
                            val nonmedicinalString = fullElement(startElement, reader)
                            val nonmedicinal = xmlMapper.readValue<Nonmedicinal>(nonmedicinalString)

                            for (datablock in nonmedicinal.datablocks) {
                                NonmedicinalTableModel.NONMEDICINAL.insert {
                                    it[NonmedicinalTableModel.NONMEDICINAL.productId] = nonmedicinal.ProductId
                                    it[NonmedicinalTableModel.NONMEDICINAL.cnk] = nonmedicinal.code

                                    it[NonmedicinalTableModel.NONMEDICINAL.nameNl] = datablock.name.nl!!
                                    it[NonmedicinalTableModel.NONMEDICINAL.nameFr] = datablock.name.fr!!
                                    it[NonmedicinalTableModel.NONMEDICINAL.nameEng] = datablock.name.en
                                    it[NonmedicinalTableModel.NONMEDICINAL.nameGer] = datablock.name.de

                                    it[NonmedicinalTableModel.NONMEDICINAL.category] = datablock.category
                                    it[NonmedicinalTableModel.NONMEDICINAL.commercialStatus] =
                                        datablock.commercialStatus

                                    it[NonmedicinalTableModel.NONMEDICINAL.distributorNl] = datablock.distributor.nl
                                    it[NonmedicinalTableModel.NONMEDICINAL.distributorFr] = datablock.distributor.fr
                                    it[NonmedicinalTableModel.NONMEDICINAL.distributorEng] = datablock.distributor.en
                                    it[NonmedicinalTableModel.NONMEDICINAL.distributorGer] = datablock.distributor.de

                                    it[NonmedicinalTableModel.NONMEDICINAL.producerNl] = datablock.producer.nl!!
                                    it[NonmedicinalTableModel.NONMEDICINAL.producerFr] = datablock.producer.fr!!
                                    it[NonmedicinalTableModel.NONMEDICINAL.producerEng] = datablock.producer.en
                                    it[NonmedicinalTableModel.NONMEDICINAL.producerGer] = datablock.producer.de

                                    it[NonmedicinalTableModel.NONMEDICINAL.validFrom] = LocalDate.parse(datablock.from)
                                    if (datablock.to != null) {
                                        it[NonmedicinalTableModel.NONMEDICINAL.validTo] = LocalDate.parse(datablock.to)
                                    }
                                }

                                if (currentCounter == commitAfterAmount) {
                                    commit()
                                    currentCounter = 0
                                }

                            }
                        }

                        else -> {
                            println("no handler for " + startElement.name.localPart)
                        }
                    }
                }
            }
        }
    }
}