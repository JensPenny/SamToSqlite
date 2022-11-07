import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import db.ActualMedicineSamTableModel
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.AmpElement
import java.io.FileInputStream
import java.time.LocalDate
import javax.xml.stream.XMLInputFactory

fun parseAmpXml(
    inputFactory: XMLInputFactory, xmlMapper: ObjectMapper, path: String
) {
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    while (reader.hasNext()) {
        val event = reader.nextEvent()

        if (event.isStartElement) {
            val startElement = event.asStartElement()

            when (startElement.name.localPart) {
                "ns4:Amp" -> {
                    val ampString = fullElement(startElement, reader)
                    val amp = xmlMapper.readValue<AmpElement>(ampString)

                    tryPersist {
                        transaction {
                            for (ampData in amp.dataBlocks) {
                                ActualMedicineSamTableModel.AMP_FAMHP.insert {
                                    it[code] = amp.code
                                    it[vmpCode] = amp.vmpCode?.toInt()
                                    it[companyActorNumber] = ampData.company.actorNr.toInt()
                                    it[status] = ampData.status
                                    it[blackTriangle] = ampData.blackTriangle
                                    it[officialName] = ampData.officialName
                                    it[nameNl] = ampData.name.nl!!
                                    it[nameFr] = ampData.name.fr!!
                                    it[nameEnglish] = ampData.name.en
                                    it[nameGerman] = ampData.name.de
                                    it[medicineType] = ampData.medicineType
                                    it[prescriptionNameNl] = ampData.prescriptionNameFamhp?.nl
                                    it[prescriptionNameFr] = ampData.prescriptionNameFamhp?.fr
                                    it[prescriptionNameEng] = ampData.prescriptionNameFamhp?.en
                                    it[prescriptionNameGer] = ampData.prescriptionNameFamhp?.de

                                    it[validFrom] = LocalDate.parse(ampData.from)
                                    if (ampData.to != null) {
                                        it[validTo] = LocalDate.parse(ampData.to)
                                    }
                                }

                                ActualMedicineSamTableModel.AMP_BCPI.insert {
                                    it[code] = amp.code
                                    it[abbreviatedNameNl] = ampData.abbreviatedName?.nl
                                    it[abbreviatedNameFr] = ampData.abbreviatedName?.fr
                                    it[abbreviatedNameEng] = ampData.abbreviatedName?.en
                                    it[abbreviatedNameGer] = ampData.abbreviatedName?.de
                                    it[proprietarySuffixNl] = ampData.proprietarySuffix?.nl
                                    it[proprietarySuffixFr] = ampData.proprietarySuffix?.fr
                                    it[proprietarySuffixEng] = ampData.proprietarySuffix?.en
                                    it[proprietarySuffixGer] = ampData.proprietarySuffix?.de
                                    it[prescriptionNameNl] = ampData.prescriptionName?.nl
                                    it[prescriptionNameFr] = ampData.prescriptionName?.fr
                                    it[prescriptionNameEng] = ampData.prescriptionName?.en
                                    it[prescriptionNameGer] = ampData.prescriptionName?.de

                                    it[validFrom] = LocalDate.parse(ampData.from)
                                    if (ampData.to != null) {
                                        it[validTo] = LocalDate.parse(ampData.to)
                                    }
                                    //logger.info { "Persisted amp ${amp}" }
                                }
                            }

                            for (ampComponent in amp.ampComponents) {
                                for (ampComponentData in ampComponent.dataBlocks) {
                                    ActualMedicineSamTableModel.AMPC_FAMHP.insert {
                                        it[ampCode] = amp.code
                                        it[sequenceNumber] = ampComponent.sequenceNumber.toInt()

                                        it[validFrom] = LocalDate.parse(ampComponentData.from)
                                        if (ampComponentData.to != null) {
                                            it[validTo] = LocalDate.parse(ampComponentData.to)
                                        }
                                    }

                                    ActualMedicineSamTableModel.AMPC_BCPI.insert {
                                        it[ampCode] = amp.code
                                        it[sequenceNumber] = ampComponent.sequenceNumber.toInt()
                                        it[vmpcCode] = ampComponent.vmpComponentCode
                                        it[dividable] = ampComponentData.dividable
                                        it[scored] = ampComponentData.scored
                                        //it[crushable] = ampComponentData.crushable
                                        it[containsAlcohol] = ampComponentData.containsAlcohol
                                        it[sugarFree] = ampComponentData.sugarFree
                                        //it[modifiedReleaseType] = ampComponentData.modifiedReleaseType
                                        it[specificDrugDevice] = ampComponentData.specificDrugDevice
                                        it[dimensions] = ampComponentData.dimensions
                                        it[nameNl] = ampComponentData.name.nl
                                        it[nameFr] = ampComponentData.name.fr
                                        it[nameEng] = ampComponentData.name.en
                                        it[nameGer] = ampComponentData.name.de
                                        //it[noteNl] = ampComponentData.note
                                        //it[concentration] = ampComponentData.concentration
                                        //it[osmoticConcentration] = ampComponentData.osmoticConcentration
                                        //it[caloricValue] = ampComponentData.caloricValue

                                        it[validFrom] = LocalDate.parse(ampComponentData.from)
                                        if (ampComponentData.to != null) {
                                            it[validTo] = LocalDate.parse(ampComponentData.to)
                                        }
                                    }

                                    for (pharmaceuticalFormReference in ampComponentData.pharmaceuticalFormReferences) {
                                        ActualMedicineSamTableModel.AMPC_TO_PHARMFORM.insert {
                                            it[ampCode] = amp.code
                                            it[sequenceNumber] = ampComponent.sequenceNumber.toInt()
                                            it[pharmaFormCode] = pharmaceuticalFormReference.codeReference

                                            it[validFrom] = LocalDate.parse(ampComponentData.from)
                                            if (ampComponentData.to != null) {
                                                it[validTo] = LocalDate.parse(ampComponentData.to)
                                            }
                                        }
                                    }

                                    for (roaReference in ampComponentData.routesOfAdministration) {
                                        ActualMedicineSamTableModel.AMPC_TO_ROA.insert {
                                            it[ampCode] = amp.code
                                            it[sequenceNumber] = ampComponent.sequenceNumber.toInt()
                                            it[roaCode] = roaReference.codeReference

                                            it[validFrom] = LocalDate.parse(ampComponentData.from)
                                            if (ampComponentData.to != null) {
                                                it[validTo] = LocalDate.parse(ampComponentData.to)
                                            }
                                        }
                                    }
                                }

                                for (ingredient in ampComponent.ingredients) {
                                    for (ingredientData in ingredient.dataBlocks) {
                                        ActualMedicineSamTableModel.RACTING.insert {
                                            it[ampCode] = amp.code
                                            it[sequenceNumber] = ampComponent.sequenceNumber.toInt()
                                            it[rank] = ingredient.rank.toInt()
                                            it[type] = ingredientData.type
                                            it[substanceCode] = ingredientData.substanceCode.codeReference
                                            //it[knownEffect] = ingredientData
                                            it[strengthUnit] = ingredientData.strength?.unit
                                            it[strengthQuantity] = ingredientData.strength?.Strength
                                            it[strengthDescription] = ingredientData.strengthDescription
                                            //it[additionalInformation] =

                                            it[validFrom] = LocalDate.parse(ingredientData.from)
                                            if (ingredientData.to != null) {
                                                it[validTo] = LocalDate.parse(ingredientData.to)
                                            }
                                        }
                                    }
                                    for (actualIngredientEquivalent in ingredient.actualIngredientEquivalents) {
                                        for (dataBlock in actualIngredientEquivalent.dataBlocks) {
                                            ActualMedicineSamTableModel.RACTIEQ.insert {
                                                it[ampCode] = amp.code
                                                it[ampcSequenceNumber] = ampComponent.sequenceNumber.toInt()
                                                it[rank] = ingredient.rank.toInt()
                                                it[sequenceNumber] = actualIngredientEquivalent.sequenceNumber.toInt()
                                                it[type] = dataBlock.type
                                                it[substanceCode] = dataBlock.substanceCode?.codeReference
                                                //it[knownEffect] = dataBlock
                                                it[strengthQuantity] = dataBlock.strength?.Strength
                                                it[strengthUnit] = dataBlock.strength?.unit
                                                it[strengthDescription] = dataBlock.strengthDescription
                                                //it[additionalInformation] = dataBlock.a

                                                it[validFrom] = LocalDate.parse(dataBlock.from)
                                                if (dataBlock.to != null) {
                                                    it[validTo] = LocalDate.parse(dataBlock.to)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            for (amppElement in amp.amppElements) {
                                for (amppDataBlock in amppElement.amppDataBlocks) {
                                    ActualMedicineSamTableModel.AMPP_FAMHP.insert {
                                        it[ctiExtended] = amppElement.ctiExtended
                                        it[ampCode] = amp.code
                                        it[deliveryModusCode] = amppDataBlock.deliveryModusReference.codeReference
                                        it[deliveryModusSpecificationCode] =
                                            amppDataBlock.deliveryModusSpecReference?.codeReference
                                        it[authorizationNumber] = amppDataBlock.authorisationNr
                                        it[orphan] = amppDataBlock.orphan
                                        it[leafletLinkNl] = amppDataBlock.leafletLink?.nl
                                        it[leafletLinkFr] = amppDataBlock.leafletLink?.fr
                                        it[leafletLinkEng] = amppDataBlock.leafletLink?.en
                                        it[leafletLinkGer] = amppDataBlock.leafletLink?.de
                                        it[spcLinkNl] = amppDataBlock.spcLink?.nl
                                        it[spcLinkFr] = amppDataBlock.spcLink?.fr
                                        it[spcLinkEng] = amppDataBlock.spcLink?.en
                                        it[spcLinkGer] = amppDataBlock.spcLink?.de
                                        it[rmaPatientLinkNl] = amppDataBlock.rmaPatientLink?.nl
                                        it[rmaPatientLinkFr] = amppDataBlock.rmaPatientLink?.fr
                                        it[rmaPatientLinkEng] = amppDataBlock.rmaPatientLink?.en
                                        it[rmaPatientLinkGer] = amppDataBlock.rmaPatientLink?.de
                                        it[rmaProfessionalLinkNl] = amppDataBlock.rmaProfessionalLink?.nl
                                        it[rmaProfessionalLinkFr] = amppDataBlock.rmaProfessionalLink?.fr
                                        it[rmaProfessionalLinkEng] = amppDataBlock.rmaProfessionalLink?.en
                                        it[rmaProfessionalLinkGer] = amppDataBlock.rmaProfessionalLink?.de
                                        it[parallelCircuit] = amppDataBlock.parallelCircuit
                                        //it[parallelDistributor] = amppDataBlock.parallelDistributor
                                        it[packMultiplier] = amppDataBlock.packMultiplier?.toInt()
                                        it[packAmount] = amppDataBlock.packAmount?.PackAmount
                                        it[packAmountUnit] = amppDataBlock.packAmount?.unit
                                        it[packDisplayValue] = amppDataBlock.packDisplayValue
                                        //it[gtin] = amppDataBlock.gtin
                                        it[status] = amppDataBlock.status
                                        it[fmdProductCode] = amppDataBlock.fmdProductCode
                                        it[fmdInScope] = amppDataBlock.fmdInScope
                                        it[antiTamperingDevicePresent] = amppDataBlock.antiTamperingDevicePresent
                                        it[prescriptionNameNl] = amppDataBlock.prescriptionNameFamhp?.nl
                                        it[prescriptionNameFr] = amppDataBlock.prescriptionNameFamhp?.fr
                                        it[prescriptionNameEng] = amppDataBlock.prescriptionNameFamhp?.en
                                        it[prescriptionNameGer] = amppDataBlock.prescriptionNameFamhp?.de

                                        it[validFrom] = LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[validTo] = LocalDate.parse(amppDataBlock.to)
                                        }
                                    }

                                    ActualMedicineSamTableModel.AMPP_BCFI.insert {
                                        it[ctiExtended] = amppElement.ctiExtended
                                        it[singleUse] = amppDataBlock.singleUse
                                        it[speciallyRegulated] = amppDataBlock.speciallyRegulated
                                        it[abbreviatedNameNl] = amppDataBlock.abbreviatedName?.nl
                                        it[abbreviatedNameFr] = amppDataBlock.abbreviatedName?.fr
                                        it[abbreviatedNameEng] = amppDataBlock.abbreviatedName?.en
                                        it[abbreviatedNameGer] = amppDataBlock.abbreviatedName?.de
                                        it[noteNl] = amppDataBlock.note?.nl
                                        it[noteFr] = amppDataBlock.note?.fr
                                        it[noteEng] = amppDataBlock.note?.en
                                        it[noteGer] = amppDataBlock.note?.de
                                        it[posologyNoteNl] = amppDataBlock.posologyNote?.nl
                                        it[posologyNoteFr] = amppDataBlock.posologyNote?.fr
                                        it[posologyNoteEng] = amppDataBlock.posologyNote?.en
                                        it[posologyNoteGer] = amppDataBlock.posologyNote?.de
                                        it[crmLinkNl] = amppDataBlock.crmLink?.nl
                                        it[crmLinkFr] = amppDataBlock.crmLink?.fr
                                        it[crmLinkEng] = amppDataBlock.crmLink?.en
                                        it[crmLinkGer] = amppDataBlock.crmLink?.de

                                        it[validFrom] = LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[validTo] = LocalDate.parse(amppDataBlock.to)
                                        }
                                    }

                                    ActualMedicineSamTableModel.AMPP_NIHDI.insert {
                                        it[ctiExtended] = amppElement.ctiExtended
                                        it[exfactory_price] = amppDataBlock.exFactoryPrice
                                        it[reimbursementCode] = amppDataBlock.reimbursementCode
                                        //it[cheap] = amppDataBlock.cheap
                                        //it[cheapest] = amppDataBlock.cheapest
                                        it[index] = amppDataBlock.index
                                        it[bigPackage] = amppDataBlock.bigPackage

                                        it[validFrom] = LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[validTo] = LocalDate.parse(amppDataBlock.to)
                                        }
                                    }

                                    /** lol not used atm
                                    ActualMedicineSamTableModel.AMPP_NIHDI_BIS.insert {

                                    }
                                     */

                                    /** lol not used atm
                                    ActualMedicineSamTableModel.AMPP_NIHDI_BIS.insert {

                                    }
                                     */

                                    ActualMedicineSamTableModel.AMPP_ECON.insert {
                                        it[ctiExtended] = amppElement.ctiExtended
                                        it[officialExFactoryPrice] = amppDataBlock.officialExFactoryPrice
                                        it[realExFactoryPrice] = amppDataBlock.realExFactoryPrice

                                        if (amppDataBlock.pricingDecisionDate != null) {
                                            it[decisionDate] = LocalDate.parse(amppDataBlock.pricingDecisionDate)
                                        }

                                        it[validFrom] = LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[validTo] = LocalDate.parse(amppDataBlock.to)
                                        }
                                    }
                                }

                                for (amppComponent in amppElement.amppComponents) {
                                    for (amppComponentDataBlock in amppComponent.amppComponentDataBlocks) {
                                        ActualMedicineSamTableModel.AMPPC.insert {
                                            it[ctiExtended] = amppElement.ctiExtended
                                            it[sequenceNumber] = amppComponent.sequenceNumber.toInt()
                                            it[ampcSequenceNumber] = amppComponentDataBlock.ampcSequenceNr?.toInt()
                                            it[deviceTypeCode] = amppComponentDataBlock.deviceType?.codeReference
                                            it[packagingTypeCode] = amppComponentDataBlock.packagingType?.codeReference
                                            it[contentType] = amppComponentDataBlock.contentType
                                            it[contentMultiplier] = amppComponentDataBlock.contentMultiplier?.toInt()
                                            it[packSpecification] = amppComponentDataBlock.packSpecification

                                            it[validFrom] = LocalDate.parse(amppComponentDataBlock.from)
                                            if (amppComponentDataBlock.to != null) {
                                                it[validTo] = LocalDate.parse(amppComponentDataBlock.to)
                                            }
                                        }
                                    }
                                    for (ampComponentEquivalent in amppComponent.ampComponentEquivalents) {
                                        for (amppCompEquiData in ampComponentEquivalent.amppComponentEquivalentDataBlocks) {
                                            ActualMedicineSamTableModel.AMPPCES.insert {
                                                it[ctiExtended] = amppElement.ctiExtended
                                                it[amppcSequenceNumber] = amppComponent.sequenceNumber.toInt()
                                                it[sequenceNumber] = ampComponentEquivalent.sequenceNumber.toInt()
                                                it[contentQuantity] = amppCompEquiData.content?.Content
                                                it[contentUnit] = amppCompEquiData.content?.unit

                                                it[validFrom] = LocalDate.parse(amppCompEquiData.from)
                                                if (amppCompEquiData.to != null) {
                                                    it[validTo] = LocalDate.parse(amppCompEquiData.to)
                                                }
                                            }
                                        }
                                    }
                                }

                                for (dmpp in amppElement.dmpps) {
                                    for (dmppDataBlock in dmpp.dmppDataBlocks) {
                                        ActualMedicineSamTableModel.DMPP.insert {
                                            it[code] = dmpp.code
                                            it[codeType] = dmpp.codeSystem
                                            it[productId] = dmpp.productId
                                            it[deliveryEnvironment] = dmpp.deliveryEnvironment
                                            it[price] = dmppDataBlock.price
                                            it[reimbursable] = dmppDataBlock.reimbursable
                                            //it[reimbursementRequiresPriorAgreement] = dmppDataBlock.
                                            //it[cheapestCeilingPricesStatus5] = dmppDataBlock.ch

                                            it[validFrom] = LocalDate.parse(dmppDataBlock.from)
                                            if (dmppDataBlock.to != null) {
                                                it[validTo] = LocalDate.parse(dmppDataBlock.to)
                                            }
                                        }
                                    }
                                }

                                for (commercialization in amppElement.commercialization) {
                                    for (commercializationDataBlock in commercialization.commercializationDataBlocks) {
                                        ActualMedicineSamTableModel.CMRCL.insert {
                                            it[ctiExtended] = amppElement.ctiExtended
                                            it[endOfCommercializationNl] =
                                                commercializationDataBlock.endOfCommercialization?.nl
                                            it[endOfCommercializationFr] =
                                                commercializationDataBlock.endOfCommercialization?.fr
                                            it[endOfCommercializationEng] =
                                                commercializationDataBlock.endOfCommercialization?.en
                                            it[endOfCommercializationGer] =
                                                commercializationDataBlock.endOfCommercialization?.de

                                            it[reasonEndOfCommercializationNl] = commercializationDataBlock.reason?.nl
                                            it[reasonEndOfCommercializationFr] = commercializationDataBlock.reason?.fr
                                            it[reasonEndOfCommercializationEng] = commercializationDataBlock.reason?.en
                                            it[reasonEndOfCommercializationGer] = commercializationDataBlock.reason?.de

                                            it[additionalInformationNl] = commercializationDataBlock.impact?.nl
                                            it[additionalInformationFr] = commercializationDataBlock.impact?.fr
                                            it[additionalInformationEng] = commercializationDataBlock.impact?.en
                                            it[additionalInformationGer] = commercializationDataBlock.impact?.de

                                            it[validFrom] = LocalDate.parse(commercializationDataBlock.from)
                                            if (commercializationDataBlock.to != null) {
                                                it[validTo] = LocalDate.parse(commercializationDataBlock.to)
                                            }
                                        }
                                    }
                                }

                                for (supplyProblem in amppElement.supplyProblems) {
                                    for (supplyDataBlock in supplyProblem.supplyDataBlocks) {
                                        ActualMedicineSamTableModel.SPPROB.insert {
                                            it[ctiExtended] = amppElement.ctiExtended
                                            if (supplyDataBlock.expectedEnd != null) {
                                                it[expectedEndDate] = LocalDate.parse(supplyDataBlock.expectedEnd)
                                            }
                                            it[reportedBy] = supplyDataBlock.reportedBy
                                            if (supplyDataBlock.reportedOn != null) {
                                                it[reportedOn] = LocalDate.parse(supplyDataBlock.reportedOn)
                                            }

                                            it[contactName] = supplyDataBlock.contactName
                                            it[contactCompany] = supplyDataBlock.contactCompany
                                            it[contactMail] = supplyDataBlock.contactMail
                                            it[contactPhone] = supplyDataBlock.phone

                                            it[reasonNl] = supplyDataBlock.reason?.nl
                                            it[reasonFr] = supplyDataBlock.reason?.fr
                                            it[reasonEng] = supplyDataBlock.reason?.en
                                            it[reasonGer] = supplyDataBlock.reason?.de

                                            it[additionalInformationNl] = supplyDataBlock.additionalInformation?.nl
                                            it[additionalInformationFr] = supplyDataBlock.additionalInformation?.fr
                                            it[additionalInformationEng] = supplyDataBlock.additionalInformation?.en
                                            it[additionalInformationGer] = supplyDataBlock.additionalInformation?.de

                                            it[impactNl] = supplyDataBlock.impact?.nl
                                            it[impactFr] = supplyDataBlock.impact?.fr
                                            it[impactEng] = supplyDataBlock.impact?.en
                                            it[impactGer] = supplyDataBlock.impact?.de

                                            //it[limitedAvailability] = supplyDataBlock.

                                            it[validFrom] = LocalDate.parse(supplyDataBlock.from)
                                            if (supplyDataBlock.to != null) {
                                                it[validTo] = LocalDate.parse(supplyDataBlock.to)
                                            }
                                        }
                                    }
                                }

                                for (derogationImport in amppElement.derogationImport) {
                                    for (derogationDatablock in derogationImport.derogationData) {
                                        ActualMedicineSamTableModel.DRGIMP.insert {
                                            it[ctiExtended] = amppElement.ctiExtended
                                            it[sequenceNumber] = derogationImport.sequenceNumber.toInt()
                                            it[noteNl] = derogationDatablock.note?.nl
                                            it[noteFr] = derogationDatablock.note?.fr
                                            it[noteEng] = derogationDatablock.note?.en
                                            it[noteGer] = derogationDatablock.note?.de

                                            it[validFrom] = LocalDate.parse(derogationDatablock.from)
                                            if (derogationDatablock.to != null) {
                                                it[validTo] = LocalDate.parse(derogationDatablock.to)
                                            }
                                        }
                                    }
                                }
                            }
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