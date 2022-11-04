package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import db.CompanyTableModel
import fullElement
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.Company
import tryPersist
import java.io.FileInputStream
import java.time.LocalDate
import javax.xml.stream.XMLInputFactory

fun parseCompanyXml(
    inputFactory: XMLInputFactory,
    xmlMapper: ObjectMapper,
    path: String
) {

    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    val commitAfterAmount = 100
    var currentCounter = 0

    tryPersist {
        transaction {

            while (reader.hasNext()) {
                val event = reader.nextEvent()

                if (event.isStartElement) {
                    val startElement = event.asStartElement()

                    when (startElement.name.localPart) {
                        "ns2:Company" -> {
                            val companyString = fullElement(startElement, reader)
                            val company = xmlMapper.readValue<Company>(companyString)

                            currentCounter++

                            for (datablock in company.data) {
                                CompanyTableModel.CPN.insert {
                                    it[CompanyTableModel.CPN.actorNumber] = company.actorNr
                                    it[CompanyTableModel.CPN.authorisationNumber] = datablock.authorisationNr
                                    it[CompanyTableModel.CPN.vatCountryCode] = datablock.vat?.countryCode
                                    it[CompanyTableModel.CPN.vatNumber] = datablock.vat?.VatNr
                                    it[CompanyTableModel.CPN.europeanNumber] = null
                                    it[CompanyTableModel.CPN.denomination] = datablock.denomination
                                    it[CompanyTableModel.CPN.legalForm] = datablock.legalForm
                                    it[CompanyTableModel.CPN.building] = datablock.building
                                    it[CompanyTableModel.CPN.streetName] = datablock.streetName
                                    it[CompanyTableModel.CPN.streetNum] = datablock.streetNum
                                    it[CompanyTableModel.CPN.postbox] = datablock.postbox
                                    it[CompanyTableModel.CPN.postcode] = datablock.postcode
                                    it[CompanyTableModel.CPN.city] = datablock.city
                                    it[CompanyTableModel.CPN.countryCode] = datablock.countryCode
                                    it[CompanyTableModel.CPN.phone] = datablock.phone
                                    it[CompanyTableModel.CPN.language] = datablock.language
                                    it[CompanyTableModel.CPN.website] = datablock.website
                                    it[CompanyTableModel.CPN.validFrom] = LocalDate.parse(datablock.from)
                                    if (datablock.to != null) {
                                        it[CompanyTableModel.CPN.validTo] = LocalDate.parse(datablock.to)
                                    }
                                }
                            }

                            if (currentCounter == commitAfterAmount) {
                                commit()
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