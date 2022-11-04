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
                                    it[productId] = nonmedicinal.ProductId
                                    it[cnk] = nonmedicinal.code

                                    it[nameNl] = datablock.name.nl!!
                                    it[nameFr] = datablock.name.fr!!
                                    it[nameEng] = datablock.name.en
                                    it[nameGer] = datablock.name.de

                                    it[category] = datablock.category
                                    it[commercialStatus] =
                                        datablock.commercialStatus

                                    it[distributorNl] = datablock.distributor.nl
                                    it[distributorFr] = datablock.distributor.fr
                                    it[distributorEng] = datablock.distributor.en
                                    it[distributorGer] = datablock.distributor.de

                                    it[producerNl] = datablock.producer.nl!!
                                    it[producerFr] = datablock.producer.fr!!
                                    it[producerEng] = datablock.producer.en
                                    it[producerGer] = datablock.producer.de

                                    it[validFrom] = LocalDate.parse(datablock.from)
                                    if (datablock.to != null) {
                                        it[validTo] = LocalDate.parse(datablock.to)
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