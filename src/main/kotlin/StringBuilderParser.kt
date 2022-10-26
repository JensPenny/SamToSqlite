import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import db.ReferenceTableModel
import db.createDB
import db.createTables
import mu.KotlinLogging
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.*
import java.io.FileInputStream
import java.io.StringWriter
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.StartElement

val logger = KotlinLogging.logger {}

fun main() {
    createDB()
    createTables()

    val inputFactory = XmlFactory.builder().xmlInputFactory()
    inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)

    val xmlMapper = XmlMapper.builder(XmlFactory(inputFactory, WstxOutputFactory()))
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .addModule(JacksonXmlModule())
        .defaultUseWrapper(false)
        .build()
        .registerKotlinModule()

    parseReferenceXml(inputFactory, xmlMapper)
}

fun parseReferenceXml(
    inputFactory: XMLInputFactory,
    xmlMapper: ObjectMapper
) {
    val path = "res/latest/REF-1657801178464.xml" //todo make changable
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    while (reader.hasNext()) {
        val event = reader.nextEvent()

        if (event.isStartElement) {
            val startElement = event.asStartElement()

            when (startElement.name.localPart) {
                "_AtcClassification" -> {
                    val atcClassificationString = fullElement(startElement, reader)
                    val atc = xmlMapper.readValue<ATC>(atcClassificationString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.ATC.insert {
                                it[code] = atc.atcCode
                                it[description] = atc.description
                            }
                        }
                        logger.info { "Persisted ATC: : ${atc.atcCode} - ${atc.description}" }
                    }
                }

                "DeliveryModus" -> {
                    val deliveryModusString = fullElement(startElement, reader)
                    val deliveryModus = xmlMapper.readValue<DeliveryModus>(deliveryModusString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.DLVM.insert {
                                it[code] = deliveryModus.code
                                it[descriptionNameNl] = deliveryModus.description.nl!!
                                it[descriptionNameFr] = deliveryModus.description.fr!!
                                it[descriptionNameEng] = deliveryModus.description.en
                                it[descriptionNameGer] = deliveryModus.description.de
                            }
                        }
                        logger.info { "Persisted Deliverymodus $deliveryModus" }
                    }
                }

                "DeliveryModusSpecification" -> {
                    val deliveryModeSpecString = fullElement(startElement, reader)
                    val deliveryModeSpec = xmlMapper.readValue<DeliveryModusSpecification>(deliveryModeSpecString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.DLVMS.insert {
                                it[code] = deliveryModeSpec.code
                                it[descriptionNameNl] = deliveryModeSpec.description.nl!!
                                it[descriptionNameFr] = deliveryModeSpec.description.fr!!
                                it[descriptionNameEng] = deliveryModeSpec.description.en
                                it[descriptionNameGer] = deliveryModeSpec.description.de
                            }
                        }
                        logger.info { "Persisted DeliverymodusSpec $deliveryModeSpec" }
                    }
                }

                "DeviceType" -> {
                    val deviceTypeString = fullElement(startElement, reader)
                    val deviceType = xmlMapper.readValue<DeviceType>(deviceTypeString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.DVCTP.insert {
                                it[code] = deviceType.code
                                it[nameNl] = deviceType.name.nl!!
                                it[nameFr] = deviceType.name.fr!!
                                it[nameEng] = deviceType.name.en
                                it[nameGer] = deviceType.name.de
                                it[edqmCode] = deviceType.edqmCode
                                it[edqmDefinition] = deviceType.edqmDefinition
                            }
                        }
                        logger.info { "Persisted deviceType $deviceType" }
                    }
                }

                "PackagingClosure" -> {
                    val packagingString = fullElement(startElement, reader)
                    val packaging = xmlMapper.readValue<PackagingClosure>(packagingString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.PCKCL.insert {
                                it[code] = packaging.code
                                it[nameNl] = packaging.name.nl!!
                                it[nameFr] = packaging.name.fr!!
                                it[nameEng] = packaging.name.en
                                it[nameGer] = packaging.name.de
                                it[edqmCode] = packaging.edqmCode
                                it[edqmDefinition] = packaging.edqmDefinition
                            }
                        }
                        logger.info { "Persisted package $packaging" }
                    }
                }

                "PackagingMaterial" -> {
                    val materialString = fullElement(startElement, reader)
                    val material = xmlMapper.readValue<PackagingMaterial>(materialString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.PCKMT.insert {
                                it[code] = material.code
                                it[nameNl] = material.name.nl!!
                                it[nameFr] = material.name.fr!!
                                it[nameEng] = material.name.en
                                it[nameGer] = material.name.de
                            }
                        }
                        logger.info { "Persisted Packaging Material $material" }
                    }
                }

                "PackagingType" -> {
                    val packagingTypeString = fullElement(startElement, reader)
                    val packagingType = xmlMapper.readValue<PackagingType>(packagingTypeString)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.PCKTP.insert {
                                it[code] = packagingType.code
                                it[nameNl] = packagingType.name.nl!!
                                it[nameFr] = packagingType.name.fr!!
                                it[nameEng] = packagingType.name.en
                                it[nameGer] = packagingType.name.de
                                it[edqmCode] = packagingType.edqmCode
                                it[edqmDefinition] = packagingType.edqmDefinition
                            }
                        }
                        logger.info { "Persisted Package Type $packagingType" }
                    }
                }

                "PharmaceuticalForm" -> {
                    val pharmaceuticalFormString = fullElement(startElement, reader)
                    val pharmaceuticalForm = xmlMapper.readValue<PharmaceuticalForm>(pharmaceuticalFormString)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.PHFRM.insert {
                                it[code] = pharmaceuticalForm.code
                                it[nameNl] = pharmaceuticalForm.name.nl!!
                                it[nameFr] = pharmaceuticalForm.name.fr!!
                                it[nameEnglish] = pharmaceuticalForm.name.en
                                it[nameGerman] = pharmaceuticalForm.name.de
                            }
                        }
                        logger.info { "Persisted Pharmaceutical Form $pharmaceuticalForm" }
                    }
                }

                "RouteOfAdministration" -> {
                    val routeOfAdmString = fullElement(startElement, reader)
                    val routeOfAdministration = xmlMapper.readValue<RouteOfAdministration>(routeOfAdmString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.ROA.insert {
                                it[code] = routeOfAdministration.code
                                it[nameNl] = routeOfAdministration.name.nl!!
                                it[nameFr] = routeOfAdministration.name.fr!!
                                it[nameEng] = routeOfAdministration.name.en
                                it[nameGer] = routeOfAdministration.name.de
                            }
                        }
                        logger.info { "Persisted Route of Administration $routeOfAdministration" }
                    }
                }

                "Substance" -> {
                    val substanceString = fullElement(startElement, reader)
                    val substance = xmlMapper.readValue<Substance>(substanceString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.SBST.insert {
                                it[code] = substance.code
                                it[chemicalForm] = substance.chemicalForm
                                it[nameNl] = substance.name.nl!!
                                it[nameFr] = substance.name.fr!!
                                it[nameEng] = substance.name.en
                                it[nameGer] = substance.name.de
                                it[noteNl] = substance.note?.nl
                                it[noteFr] = substance.note?.fr
                                it[noteEng] = substance.note?.en
                                it[noteGer] = substance.note?.de
                            }
                        }
                        logger.info { "Persisted Substance $substance" }
                    }
                }

                "NoSwitchReason" -> {
                    val noSwitchReasonString = fullElement(startElement, reader)
                    val noSwitchReason = xmlMapper.readValue<NoSwitchReason>(noSwitchReasonString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.NOSWR.insert {
                                it[code] = noSwitchReason.code
                                it[descriptionNl] = noSwitchReason.description.nl
                                it[descriptionFr] = noSwitchReason.description.fr
                                it[descriptionEng] = noSwitchReason.description.en
                                it[descriptionGer] = noSwitchReason.description.de
                            }
                        }
                        logger.info { "Persisted Route of Administration $noSwitchReason" }
                    }
                }

                "VirtualForm" -> {
                    val virtualFormString = fullElement(startElement, reader)
                    val virtualForm = xmlMapper.readValue<VirtualForm>(virtualFormString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.VTFRM.insert {
                                it[code] = virtualForm.code
                                it[abbreviatedNl] = virtualForm.abbreviation.nl!!
                                it[abbreviatedFr] = virtualForm.abbreviation.fr!!
                                it[abbreviatedEng] = virtualForm.abbreviation.en
                                it[abbreviatedGer] = virtualForm.abbreviation.de

                                it[nameNl] = virtualForm.name.nl!!
                                it[nameFr] = virtualForm.name.fr!!
                                it[nameEng] = virtualForm.name.en
                                it[nameGer] = virtualForm.name.de

                                it[descriptionNl] = virtualForm.description?.nl
                                it[descriptionFr] = virtualForm.description?.fr
                                it[descriptionEng] = virtualForm.description?.en
                                it[descriptionGer] = virtualForm.description?.de
                            }
                        }
                    }
                }

                "Wada" -> {

                }

                else -> {
                    println("no handler for " + startElement.name.localPart)
                }
            }
        }
    }
}

inline fun tryPersist(call: () -> Unit) {
    try {
        call()
    } catch (e: ExposedSQLException) {
        println("Message: ${e.message}")
        println("Trace: ${e.stackTrace}")
    }
}

fun fullElement(startElement: StartElement, reader: XMLEventReader): String {

    val writer = StringWriter()
    val xmlWriter = XmlFactory.builder().xmlOutputFactory().createXMLEventWriter(writer)
    xmlWriter.namespaceContext = startElement.namespaceContext
    xmlWriter.add(startElement)

    var event = reader.nextEvent()
    while (!(event.isEndElement && event.asEndElement().name.localPart == startElement.name.localPart)) {
        xmlWriter.add(event)
        event = reader.nextEvent()
    }
    xmlWriter.add(event)
    xmlWriter.close()
    return writer.toString()
}
