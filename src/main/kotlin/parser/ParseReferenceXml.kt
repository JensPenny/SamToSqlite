package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import db.ReferenceTableModel
import fullElement
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.*
import tryPersist
import java.io.FileInputStream
import javax.xml.stream.XMLInputFactory

fun parseReferenceXml(
    inputFactory: XMLInputFactory, xmlMapper: ObjectMapper, path: String
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

                    if (currentCounter == commitAfterAmount) {
                        commit()
                        currentCounter = 1
                    } else {
                        currentCounter++
                    }
                    when (startElement.name.localPart) {
                        "AtcClassification" -> {
                            val atcClassificationString = fullElement(startElement, reader)
                            val atc = xmlMapper.readValue<ATC>(atcClassificationString)
                            ReferenceTableModel.ATC.insert {
                                it[code] = atc.atcCode
                                it[description] = atc.description
                            }
                        }

                        "DeliveryModus" -> {
                            val deliveryModusString = fullElement(startElement, reader)
                            val deliveryModus = xmlMapper.readValue<DeliveryModus>(deliveryModusString)
                            ReferenceTableModel.DLVM.insert {
                                it[code] = deliveryModus.code
                                it[descriptionNameNl] = deliveryModus.description.nl!!
                                it[descriptionNameFr] = deliveryModus.description.fr!!
                                it[descriptionNameEng] = deliveryModus.description.en
                                it[descriptionNameGer] = deliveryModus.description.de
                            }
                        }

                        "DeliveryModusSpecification" -> {
                            val deliveryModeSpecString = fullElement(startElement, reader)
                            val deliveryModeSpec =
                                xmlMapper.readValue<DeliveryModusSpecification>(deliveryModeSpecString)
                            ReferenceTableModel.DLVMS.insert {
                                it[code] = deliveryModeSpec.code
                                it[descriptionNameNl] = deliveryModeSpec.description.nl!!
                                it[descriptionNameFr] = deliveryModeSpec.description.fr!!
                                it[descriptionNameEng] = deliveryModeSpec.description.en
                                it[descriptionNameGer] = deliveryModeSpec.description.de
                            }
                        }

                        "DeviceType" -> {
                            val deviceTypeString = fullElement(startElement, reader)
                            val deviceType = xmlMapper.readValue<DeviceType>(deviceTypeString)
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

                        "PackagingClosure" -> {
                            val packagingString = fullElement(startElement, reader)
                            val packaging = xmlMapper.readValue<PackagingClosure>(packagingString)
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

                        "PackagingMaterial" -> {
                            val materialString = fullElement(startElement, reader)
                            val material = xmlMapper.readValue<PackagingMaterial>(materialString)
                            ReferenceTableModel.PCKMT.insert {
                                it[code] = material.code
                                it[nameNl] = material.name.nl!!
                                it[nameFr] = material.name.fr!!
                                it[nameEng] = material.name.en
                                it[nameGer] = material.name.de
                            }
                        }

                        "PackagingType" -> {
                            val packagingTypeString = fullElement(startElement, reader)
                            val packagingType = xmlMapper.readValue<PackagingType>(packagingTypeString)

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

                        "PharmaceuticalForm" -> {
                            val pharmaceuticalFormString = fullElement(startElement, reader)
                            val pharmaceuticalForm = xmlMapper.readValue<PharmaceuticalForm>(pharmaceuticalFormString)

                            ReferenceTableModel.PHFRM.insert {
                                it[code] = pharmaceuticalForm.code
                                it[nameNl] = pharmaceuticalForm.name.nl!!
                                it[nameFr] = pharmaceuticalForm.name.fr!!
                                it[nameEnglish] = pharmaceuticalForm.name.en
                                it[nameGerman] = pharmaceuticalForm.name.de
                            }
                        }

                        "RouteOfAdministration" -> {
                            val routeOfAdmString = fullElement(startElement, reader)
                            val routeOfAdministration = xmlMapper.readValue<RouteOfAdministration>(routeOfAdmString)
                            ReferenceTableModel.ROA.insert {
                                it[code] = routeOfAdministration.code
                                it[nameNl] = routeOfAdministration.name.nl!!
                                it[nameFr] = routeOfAdministration.name.fr!!
                                it[nameEng] = routeOfAdministration.name.en
                                it[nameGer] = routeOfAdministration.name.de
                            }
                        }

                        "Substance" -> {
                            val substanceString = fullElement(startElement, reader)
                            val substance = xmlMapper.readValue<Substance>(substanceString)
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

                        "NoSwitchReason" -> {
                            val noSwitchReasonString = fullElement(startElement, reader)
                            val noSwitchReason = xmlMapper.readValue<NoSwitchReason>(noSwitchReasonString)
                            ReferenceTableModel.NOSWR.insert {
                                it[code] = noSwitchReason.code
                                it[descriptionNl] = noSwitchReason.description.nl
                                it[descriptionFr] = noSwitchReason.description.fr
                                it[descriptionEng] = noSwitchReason.description.en
                                it[descriptionGer] = noSwitchReason.description.de
                            }
                        }

                        "VirtualForm" -> {
                            val virtualFormString = fullElement(startElement, reader)
                            val virtualForm = xmlMapper.readValue<VirtualForm>(virtualFormString)
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

                        "Wada" -> {
                            val wadaString = fullElement(startElement, reader)
                            val wada = xmlMapper.readValue<Wada>(wadaString)
                            ReferenceTableModel.WADA.insert {
                                it[code] = wada.code

                                it[nameNl] = wada.name.nl!!
                                it[nameFr] = wada.name.fr!!
                                it[nameEng] = wada.name.en
                                it[nameGer] = wada.name.de

                                it[descriptionNl] = wada.description?.nl
                                it[descriptionFr] = wada.description?.fr
                                it[descriptionEng] = wada.description?.en
                                it[descriptionGer] = wada.description?.de
                            }
                        }

                        "NoGenericPrescriptionReason" -> {
                            val noGenPrescrReasonStr = fullElement(startElement, reader)
                            val noGenPrescrReason =
                                xmlMapper.readValue<NoGenericPrescriptionReason>(noGenPrescrReasonStr)

                            ReferenceTableModel.NOGNPR.insert {
                                it[code] = noGenPrescrReason.code
                                it[descriptionNl] = noGenPrescrReason.description.nl
                                it[descriptionFr] = noGenPrescrReason.description.fr
                                it[descriptionEng] = noGenPrescrReason.description.en
                                it[descriptionGer] = noGenPrescrReason.description.de
                            }
                        }

                        "StandardForm" -> {
                            val standardFormString = fullElement(startElement, reader)
                            val standardForm = xmlMapper.readValue<StandardForm>(standardFormString)

                            ReferenceTableModel.STDFRM.insert {
                                it[standard] = standardForm.standard
                                it[code] = standardForm.code
                                it[virtualFormCode] = standardForm.virtualFormReference.codeReference
                            }
                        }

                        "StandardRoute" -> {
                            val standardRouteString = fullElement(startElement, reader)
                            val standardRoute = xmlMapper.readValue<StandardRoute>(standardRouteString)

                            ReferenceTableModel.STDROA.insert {
                                it[standard] = standardRoute.standard
                                it[code] = standardRoute.code
                                it[routeOfAdministrationCode] = standardRoute.routeOfAdminReference.codeReference
                            }
                        }

                        "StandardSubstance" -> {
                            val standardSubstanceString = fullElement(startElement, reader)
                            val standardSubstance = xmlMapper.readValue<StandardSubstance>(standardSubstanceString)

                            for (singleReference in standardSubstance.substanceReference) {
                                ReferenceTableModel.STDSBST.insert {
                                    it[standard] = standardSubstance.standard
                                    it[code] = standardSubstance.code
                                    it[substanceCode] = singleReference.codeReference
                                }
                            }
                        }

                        "StandardUnit" -> {
                            val standardUnitString = fullElement(startElement, reader)
                            val standardUnit = xmlMapper.readValue<StandardUnit>(standardUnitString)

                            ReferenceTableModel.STDUNT.insert {
                                it[name] = standardUnit.name
                                it[descriptionNl] = standardUnit.description?.nl
                                it[descriptionFr] = standardUnit.description?.fr
                                it[descriptionEng] = standardUnit.description?.en
                                it[descriptionGer] = standardUnit.description?.de
                            }
                        }

                        "Appendix" -> {
                            val appendixString = fullElement(startElement, reader)
                            val appendix = xmlMapper.readValue<Appendix>(appendixString)

                            ReferenceTableModel.APPENDIX.insert {
                                it[code] = appendix.code
                                it[descriptionNl] = appendix.description.nl
                                it[descriptionFr] = appendix.description.fr
                                it[descriptionEng] = appendix.description.en
                                it[descriptionGer] = appendix.description.de
                            }
                        }

                        "FormCategory" -> {
                            val formCatString = fullElement(startElement, reader)
                            val formCategory = xmlMapper.readValue<FormCategory>(formCatString)

                            ReferenceTableModel.FORMCAT.insert {
                                it[code] = formCategory.code
                                it[descriptionNl] = formCategory.description.nl
                                it[descriptionFr] = formCategory.description.fr
                                it[descriptionEng] = formCategory.description.en
                                it[descriptionGer] = formCategory.description.de
                            }
                        }

                        "Parameter" -> {
                            val parameterString = fullElement(startElement, reader)
                            //logger.debug("Did absolutely nothing with a parsed 'parameter'")
                        }

                        "ProfessionalCode" -> {
                            val professionalCodeString = fullElement(startElement, reader)
                            val professionalCode = xmlMapper.readValue<ProfessionalCode>(professionalCodeString)

                            ReferenceTableModel.PROFESSIONALCODE.insert {
                                it[professionalCv] = professionalCode.professionalCv
                                it[nameId] = professionalCode.nameId.toInt()
                                //it[professionalName] = professionalCode.
                            }
                        }

                        "AppendixType" -> {
                            val appendixString = fullElement(startElement, reader)
                            val appendix = xmlMapper.readValue<AppendixType>(appendixString)

                            ReferenceTableModel.APPENDIX_TYPE.insert {
                                it[appendixTypeId] = appendix.appendixTypeId.toInt()
                                it[nameId] = appendix.nameId.toInt()
                            }
                        }

                        "FormType" -> {
                            val formTypeString = fullElement(startElement, reader)
                            val formType = xmlMapper.readValue<FormType>(formTypeString)

                            ReferenceTableModel.FORM_TYPE.insert {
                                it[formTypeId] = formType.formTypeId.toInt()
                                it[nameId] = formType.nameId.toInt()
                            }
                        }

                        "NameType" -> {
                            val nameTypeString = fullElement(startElement, reader)
                            val nameType = xmlMapper.readValue<NameType>(nameTypeString)

                            ReferenceTableModel.NAME_TYPE.insert {
                                it[nameTypeCV] = nameType.nameTypeCv
                                it[nameId] = nameType.nameId.toInt()
                                it[ReferenceTableModel.NAME_TYPE.nameType] = nameType.nameType
                                it[nameTypeSequence] = nameType.nameTypeSeq?.toInt()
                            }
                        }

                        "LegalReferencePathToParagraph" -> {
                            val legalReferenceString = fullElement(startElement, reader)
                            val legalReference =
                                xmlMapper.readValue<LegalReferencePathToParagraph>(legalReferenceString)

                            ReferenceTableModel.LEGAL_REF_TO_PARAGRAPH.insert {
                                it[legalReferencePath] = legalReference.legalReferencePath
                                it[chapterName] = legalReference.chapterName
                                it[paragraphName] = legalReference.paragraphName
                            }
                        }

                        "ReimbursementCriterion" -> {
                            val reimbCritString = fullElement(startElement, reader)
                            val reimbursementCriterion = xmlMapper.readValue<ReimbursementCriterion>(reimbCritString)

                            ReferenceTableModel.RMBCRIT.insert {
                                it[code] = reimbursementCriterion.code
                                it[category] = reimbursementCriterion.category
                                it[descriptionNl] = reimbursementCriterion.description.nl
                                it[descriptionFr] = reimbursementCriterion.description.fr
                                it[descriptionEng] = reimbursementCriterion.description.en
                                it[descriptionGer] = reimbursementCriterion.description.de
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