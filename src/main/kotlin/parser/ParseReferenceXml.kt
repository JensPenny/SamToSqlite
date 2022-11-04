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
                                it[ReferenceTableModel.ATC.code] = atc.atcCode
                                it[ReferenceTableModel.ATC.description] = atc.description
                            }
                        }

                        "DeliveryModus" -> {
                            val deliveryModusString = fullElement(startElement, reader)
                            val deliveryModus = xmlMapper.readValue<DeliveryModus>(deliveryModusString)
                            ReferenceTableModel.DLVM.insert {
                                it[ReferenceTableModel.DLVM.code] = deliveryModus.code
                                it[ReferenceTableModel.DLVM.descriptionNameNl] = deliveryModus.description.nl!!
                                it[ReferenceTableModel.DLVM.descriptionNameFr] = deliveryModus.description.fr!!
                                it[ReferenceTableModel.DLVM.descriptionNameEng] = deliveryModus.description.en
                                it[ReferenceTableModel.DLVM.descriptionNameGer] = deliveryModus.description.de
                            }
                        }

                        "DeliveryModusSpecification" -> {
                            val deliveryModeSpecString = fullElement(startElement, reader)
                            val deliveryModeSpec =
                                xmlMapper.readValue<DeliveryModusSpecification>(deliveryModeSpecString)
                            ReferenceTableModel.DLVMS.insert {
                                it[ReferenceTableModel.DLVMS.code] = deliveryModeSpec.code
                                it[ReferenceTableModel.DLVMS.descriptionNameNl] = deliveryModeSpec.description.nl!!
                                it[ReferenceTableModel.DLVMS.descriptionNameFr] = deliveryModeSpec.description.fr!!
                                it[ReferenceTableModel.DLVMS.descriptionNameEng] = deliveryModeSpec.description.en
                                it[ReferenceTableModel.DLVMS.descriptionNameGer] = deliveryModeSpec.description.de
                            }
                        }

                        "DeviceType" -> {
                            val deviceTypeString = fullElement(startElement, reader)
                            val deviceType = xmlMapper.readValue<DeviceType>(deviceTypeString)
                            ReferenceTableModel.DVCTP.insert {
                                it[ReferenceTableModel.DVCTP.code] = deviceType.code
                                it[ReferenceTableModel.DVCTP.nameNl] = deviceType.name.nl!!
                                it[ReferenceTableModel.DVCTP.nameFr] = deviceType.name.fr!!
                                it[ReferenceTableModel.DVCTP.nameEng] = deviceType.name.en
                                it[ReferenceTableModel.DVCTP.nameGer] = deviceType.name.de
                                it[ReferenceTableModel.DVCTP.edqmCode] = deviceType.edqmCode
                                it[ReferenceTableModel.DVCTP.edqmDefinition] = deviceType.edqmDefinition
                            }
                        }

                        "PackagingClosure" -> {
                            val packagingString = fullElement(startElement, reader)
                            val packaging = xmlMapper.readValue<PackagingClosure>(packagingString)
                            ReferenceTableModel.PCKCL.insert {
                                it[ReferenceTableModel.PCKCL.code] = packaging.code
                                it[ReferenceTableModel.PCKCL.nameNl] = packaging.name.nl!!
                                it[ReferenceTableModel.PCKCL.nameFr] = packaging.name.fr!!
                                it[ReferenceTableModel.PCKCL.nameEng] = packaging.name.en
                                it[ReferenceTableModel.PCKCL.nameGer] = packaging.name.de
                                it[ReferenceTableModel.PCKCL.edqmCode] = packaging.edqmCode
                                it[ReferenceTableModel.PCKCL.edqmDefinition] = packaging.edqmDefinition
                            }
                        }

                        "PackagingMaterial" -> {
                            val materialString = fullElement(startElement, reader)
                            val material = xmlMapper.readValue<PackagingMaterial>(materialString)
                            ReferenceTableModel.PCKMT.insert {
                                it[ReferenceTableModel.PCKMT.code] = material.code
                                it[ReferenceTableModel.PCKMT.nameNl] = material.name.nl!!
                                it[ReferenceTableModel.PCKMT.nameFr] = material.name.fr!!
                                it[ReferenceTableModel.PCKMT.nameEng] = material.name.en
                                it[ReferenceTableModel.PCKMT.nameGer] = material.name.de
                            }
                        }

                        "PackagingType" -> {
                            val packagingTypeString = fullElement(startElement, reader)
                            val packagingType = xmlMapper.readValue<PackagingType>(packagingTypeString)

                            ReferenceTableModel.PCKTP.insert {
                                it[ReferenceTableModel.PCKTP.code] = packagingType.code
                                it[ReferenceTableModel.PCKTP.nameNl] = packagingType.name.nl!!
                                it[ReferenceTableModel.PCKTP.nameFr] = packagingType.name.fr!!
                                it[ReferenceTableModel.PCKTP.nameEng] = packagingType.name.en
                                it[ReferenceTableModel.PCKTP.nameGer] = packagingType.name.de
                                it[ReferenceTableModel.PCKTP.edqmCode] = packagingType.edqmCode
                                it[ReferenceTableModel.PCKTP.edqmDefinition] = packagingType.edqmDefinition
                            }
                        }

                        "PharmaceuticalForm" -> {
                            val pharmaceuticalFormString = fullElement(startElement, reader)
                            val pharmaceuticalForm = xmlMapper.readValue<PharmaceuticalForm>(pharmaceuticalFormString)

                            ReferenceTableModel.PHFRM.insert {
                                it[ReferenceTableModel.PHFRM.code] = pharmaceuticalForm.code
                                it[ReferenceTableModel.PHFRM.nameNl] = pharmaceuticalForm.name.nl!!
                                it[ReferenceTableModel.PHFRM.nameFr] = pharmaceuticalForm.name.fr!!
                                it[ReferenceTableModel.PHFRM.nameEnglish] = pharmaceuticalForm.name.en
                                it[ReferenceTableModel.PHFRM.nameGerman] = pharmaceuticalForm.name.de
                            }
                        }

                        "RouteOfAdministration" -> {
                            val routeOfAdmString = fullElement(startElement, reader)
                            val routeOfAdministration = xmlMapper.readValue<RouteOfAdministration>(routeOfAdmString)
                            ReferenceTableModel.ROA.insert {
                                it[ReferenceTableModel.ROA.code] = routeOfAdministration.code
                                it[ReferenceTableModel.ROA.nameNl] = routeOfAdministration.name.nl!!
                                it[ReferenceTableModel.ROA.nameFr] = routeOfAdministration.name.fr!!
                                it[ReferenceTableModel.ROA.nameEng] = routeOfAdministration.name.en
                                it[ReferenceTableModel.ROA.nameGer] = routeOfAdministration.name.de
                            }
                        }

                        "Substance" -> {
                            val substanceString = fullElement(startElement, reader)
                            val substance = xmlMapper.readValue<Substance>(substanceString)
                            ReferenceTableModel.SBST.insert {
                                it[ReferenceTableModel.SBST.code] = substance.code
                                it[ReferenceTableModel.SBST.chemicalForm] = substance.chemicalForm
                                it[ReferenceTableModel.SBST.nameNl] = substance.name.nl!!
                                it[ReferenceTableModel.SBST.nameFr] = substance.name.fr!!
                                it[ReferenceTableModel.SBST.nameEng] = substance.name.en
                                it[ReferenceTableModel.SBST.nameGer] = substance.name.de
                                it[ReferenceTableModel.SBST.noteNl] = substance.note?.nl
                                it[ReferenceTableModel.SBST.noteFr] = substance.note?.fr
                                it[ReferenceTableModel.SBST.noteEng] = substance.note?.en
                                it[ReferenceTableModel.SBST.noteGer] = substance.note?.de
                            }
                        }

                        "NoSwitchReason" -> {
                            val noSwitchReasonString = fullElement(startElement, reader)
                            val noSwitchReason = xmlMapper.readValue<NoSwitchReason>(noSwitchReasonString)
                            ReferenceTableModel.NOSWR.insert {
                                it[ReferenceTableModel.NOSWR.code] = noSwitchReason.code
                                it[ReferenceTableModel.NOSWR.descriptionNl] = noSwitchReason.description.nl
                                it[ReferenceTableModel.NOSWR.descriptionFr] = noSwitchReason.description.fr
                                it[ReferenceTableModel.NOSWR.descriptionEng] = noSwitchReason.description.en
                                it[ReferenceTableModel.NOSWR.descriptionGer] = noSwitchReason.description.de
                            }
                        }

                        "VirtualForm" -> {
                            val virtualFormString = fullElement(startElement, reader)
                            val virtualForm = xmlMapper.readValue<VirtualForm>(virtualFormString)
                            ReferenceTableModel.VTFRM.insert {
                                it[ReferenceTableModel.VTFRM.code] = virtualForm.code
                                it[ReferenceTableModel.VTFRM.abbreviatedNl] = virtualForm.abbreviation.nl!!
                                it[ReferenceTableModel.VTFRM.abbreviatedFr] = virtualForm.abbreviation.fr!!
                                it[ReferenceTableModel.VTFRM.abbreviatedEng] = virtualForm.abbreviation.en
                                it[ReferenceTableModel.VTFRM.abbreviatedGer] = virtualForm.abbreviation.de

                                it[ReferenceTableModel.VTFRM.nameNl] = virtualForm.name.nl!!
                                it[ReferenceTableModel.VTFRM.nameFr] = virtualForm.name.fr!!
                                it[ReferenceTableModel.VTFRM.nameEng] = virtualForm.name.en
                                it[ReferenceTableModel.VTFRM.nameGer] = virtualForm.name.de

                                it[ReferenceTableModel.VTFRM.descriptionNl] = virtualForm.description?.nl
                                it[ReferenceTableModel.VTFRM.descriptionFr] = virtualForm.description?.fr
                                it[ReferenceTableModel.VTFRM.descriptionEng] = virtualForm.description?.en
                                it[ReferenceTableModel.VTFRM.descriptionGer] = virtualForm.description?.de
                            }
                        }

                        "Wada" -> {
                            val wadaString = fullElement(startElement, reader)
                            val wada = xmlMapper.readValue<Wada>(wadaString)
                            ReferenceTableModel.WADA.insert {
                                it[ReferenceTableModel.WADA.code] = wada.code

                                it[ReferenceTableModel.WADA.nameNl] = wada.name.nl!!
                                it[ReferenceTableModel.WADA.nameFr] = wada.name.fr!!
                                it[ReferenceTableModel.WADA.nameEng] = wada.name.en
                                it[ReferenceTableModel.WADA.nameGer] = wada.name.de

                                it[ReferenceTableModel.WADA.descriptionNl] = wada.description?.nl
                                it[ReferenceTableModel.WADA.descriptionFr] = wada.description?.fr
                                it[ReferenceTableModel.WADA.descriptionEng] = wada.description?.en
                                it[ReferenceTableModel.WADA.descriptionGer] = wada.description?.de
                            }
                        }

                        "NoGenericPrescriptionReason" -> {
                            val noGenPrescrReasonStr = fullElement(startElement, reader)
                            val noGenPrescrReason =
                                xmlMapper.readValue<NoGenericPrescriptionReason>(noGenPrescrReasonStr)

                            ReferenceTableModel.NOGNPR.insert {
                                it[ReferenceTableModel.NOGNPR.code] = noGenPrescrReason.code
                                it[ReferenceTableModel.NOGNPR.descriptionNl] = noGenPrescrReason.description.nl
                                it[ReferenceTableModel.NOGNPR.descriptionFr] = noGenPrescrReason.description.fr
                                it[ReferenceTableModel.NOGNPR.descriptionEng] = noGenPrescrReason.description.en
                                it[ReferenceTableModel.NOGNPR.descriptionGer] = noGenPrescrReason.description.de
                            }
                        }

                        "StandardForm" -> {
                            val standardFormString = fullElement(startElement, reader)
                            val standardForm = xmlMapper.readValue<StandardForm>(standardFormString)

                            ReferenceTableModel.STDFRM.insert {
                                it[ReferenceTableModel.STDFRM.standard] = standardForm.standard
                                it[ReferenceTableModel.STDFRM.code] = standardForm.code
                                it[ReferenceTableModel.STDFRM.virtualFormCode] =
                                    standardForm.virtualFormReference.codeReference
                            }
                        }

                        "StandardRoute" -> {
                            val standardRouteString = fullElement(startElement, reader)
                            val standardRoute = xmlMapper.readValue<StandardRoute>(standardRouteString)

                            ReferenceTableModel.STDROA.insert {
                                it[ReferenceTableModel.STDROA.standard] = standardRoute.standard
                                it[ReferenceTableModel.STDROA.code] = standardRoute.code
                                it[ReferenceTableModel.STDROA.routeOfAdministrationCode] =
                                    standardRoute.routeOfAdminReference.codeReference
                            }
                        }

                        "StandardSubstance" -> {
                            val standardSubstanceString = fullElement(startElement, reader)
                            val standardSubstance = xmlMapper.readValue<StandardSubstance>(standardSubstanceString)

                            for (singleReference in standardSubstance.substanceReference) {
                                ReferenceTableModel.STDSBST.insert {
                                    it[ReferenceTableModel.STDSBST.standard] = standardSubstance.standard
                                    it[ReferenceTableModel.STDSBST.code] = standardSubstance.code
                                    it[ReferenceTableModel.STDSBST.substanceCode] = singleReference.codeReference
                                }
                            }
                        }

                        "StandardUnit" -> {
                            val standardUnitString = fullElement(startElement, reader)
                            val standardUnit = xmlMapper.readValue<StandardUnit>(standardUnitString)

                            ReferenceTableModel.STDUNT.insert {
                                it[ReferenceTableModel.STDUNT.name] = standardUnit.name
                                it[ReferenceTableModel.STDUNT.descriptionNl] = standardUnit.description?.nl
                                it[ReferenceTableModel.STDUNT.descriptionFr] = standardUnit.description?.fr
                                it[ReferenceTableModel.STDUNT.descriptionEng] = standardUnit.description?.en
                                it[ReferenceTableModel.STDUNT.descriptionGer] = standardUnit.description?.de
                            }
                        }

                        "Appendix" -> {
                            val appendixString = fullElement(startElement, reader)
                            val appendix = xmlMapper.readValue<Appendix>(appendixString)

                            ReferenceTableModel.APPENDIX.insert {
                                it[ReferenceTableModel.APPENDIX.code] = appendix.code
                                it[ReferenceTableModel.APPENDIX.descriptionNl] = appendix.description.nl
                                it[ReferenceTableModel.APPENDIX.descriptionFr] = appendix.description.fr
                                it[ReferenceTableModel.APPENDIX.descriptionEng] = appendix.description.en
                                it[ReferenceTableModel.APPENDIX.descriptionGer] = appendix.description.de
                            }
                        }

                        "FormCategory" -> {
                            val formCatString = fullElement(startElement, reader)
                            val formCategory = xmlMapper.readValue<FormCategory>(formCatString)

                            ReferenceTableModel.FORMCAT.insert {
                                it[ReferenceTableModel.FORMCAT.code] = formCategory.code
                                it[ReferenceTableModel.FORMCAT.descriptionNl] = formCategory.description.nl
                                it[ReferenceTableModel.FORMCAT.descriptionFr] = formCategory.description.fr
                                it[ReferenceTableModel.FORMCAT.descriptionEng] = formCategory.description.en
                                it[ReferenceTableModel.FORMCAT.descriptionGer] = formCategory.description.de
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
                                it[ReferenceTableModel.PROFESSIONALCODE.professionalCv] =
                                    professionalCode.professionalCv
                                it[ReferenceTableModel.PROFESSIONALCODE.professionalName] = professionalCode.nameId
                            }
                        }

                        "AppendixType" -> {
                            val appendixString = fullElement(startElement, reader)
                            val appendix = xmlMapper.readValue<AppendixType>(appendixString)

                            ReferenceTableModel.APPENDIX_TYPE.insert {
                                it[ReferenceTableModel.APPENDIX_TYPE.appendixTypeId] = appendix.appendixTypeId.toInt()
                                it[ReferenceTableModel.APPENDIX_TYPE.nameId] = appendix.nameId.toInt()
                            }
                        }

                        "FormType" -> {
                            val formTypeString = fullElement(startElement, reader)
                            val formType = xmlMapper.readValue<FormType>(formTypeString)

                            ReferenceTableModel.FORM_TYPE.insert {
                                it[ReferenceTableModel.FORM_TYPE.formTypeId] = formType.formTypeId.toInt()
                                it[ReferenceTableModel.FORM_TYPE.nameId] = formType.nameId.toInt()
                            }
                        }

                        "NameType" -> {
                            val nameTypeString = fullElement(startElement, reader)
                            val nameType = xmlMapper.readValue<NameType>(nameTypeString)

                            ReferenceTableModel.NAME_TYPE.insert {
                                it[ReferenceTableModel.NAME_TYPE.nameTypeCV] = nameType.nameTypeCv
                                it[ReferenceTableModel.NAME_TYPE.nameId] = nameType.nameId.toInt()
                                it[ReferenceTableModel.NAME_TYPE.nameType] = nameType.nameType
                                it[ReferenceTableModel.NAME_TYPE.nameTypeSequence] = nameType.nameTypeSeq?.toInt()
                            }
                        }

                        "LegalReferencePathToParagraph" -> {
                            val legalReferenceString = fullElement(startElement, reader)
                            val legalReference =
                                xmlMapper.readValue<LegalReferencePathToParagraph>(legalReferenceString)

                            ReferenceTableModel.LEGAL_REF_TO_PARAGRAPH.insert {
                                it[ReferenceTableModel.LEGAL_REF_TO_PARAGRAPH.legalReferencePath] =
                                    legalReference.legalReferencePath
                                it[ReferenceTableModel.LEGAL_REF_TO_PARAGRAPH.chapterName] = legalReference.chapterName
                                it[ReferenceTableModel.LEGAL_REF_TO_PARAGRAPH.paragraphName] =
                                    legalReference.paragraphName
                            }
                        }

                        "ReimbursementCriterion" -> {
                            val reimbCritString = fullElement(startElement, reader)
                            val reimbursementCriterion = xmlMapper.readValue<ReimbursementCriterion>(reimbCritString)

                            ReferenceTableModel.RMBCRIT.insert {
                                it[ReferenceTableModel.RMBCRIT.code] = reimbursementCriterion.code
                                it[ReferenceTableModel.RMBCRIT.category] = reimbursementCriterion.category
                                it[ReferenceTableModel.RMBCRIT.descriptionNl] = reimbursementCriterion.description.nl
                                it[ReferenceTableModel.RMBCRIT.descriptionFr] = reimbursementCriterion.description.fr
                                it[ReferenceTableModel.RMBCRIT.descriptionEng] = reimbursementCriterion.description.en
                                it[ReferenceTableModel.RMBCRIT.descriptionGer] = reimbursementCriterion.description.de
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