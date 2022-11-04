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
                                    it[actorNumber] = company.actorNr
                                    it[authorisationNumber] = datablock.authorisationNr
                                    it[vatCountryCode] = datablock.vat?.countryCode
                                    it[vatNumber] = datablock.vat?.VatNr
                                    it[europeanNumber] = null
                                    it[denomination] = datablock.denomination
                                    it[legalForm] = datablock.legalForm
                                    it[building] = datablock.building
                                    it[streetName] = datablock.streetName
                                    it[streetNum] = datablock.streetNum
                                    it[postbox] = datablock.postbox
                                    it[postcode] = datablock.postcode
                                    it[city] = datablock.city
                                    it[countryCode] = datablock.countryCode
                                    it[phone] = datablock.phone
                                    it[language] = datablock.language
                                    it[website] = datablock.website
                                    it[validFrom] = LocalDate.parse(datablock.from)
                                    if (datablock.to != null) {
                                        it[validTo] = LocalDate.parse(datablock.to)
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