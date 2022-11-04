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
    inputFactory: XMLInputFactory,
    xmlMapper: ObjectMapper,
    path: String
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
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.code] = amp.code
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.vmpCode] = amp.vmpCode?.toInt()
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.companyActorNumber] =
                                        ampData.company.actorNr.toInt()
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.status] = ampData.status
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.blackTriangle] = ampData.blackTriangle
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.officialName] = ampData.officialName
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.nameNl] = ampData.name.nl!!
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.nameFr] = ampData.name.fr!!
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.nameEnglish] = ampData.name.en
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.nameGerman] = ampData.name.de
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.medicineType] = ampData.medicineType
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.prescriptionNameNl] =
                                        ampData.prescriptionNameFamhp?.nl
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.prescriptionNameFr] =
                                        ampData.prescriptionNameFamhp?.fr
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.prescriptionNameEng] =
                                        ampData.prescriptionNameFamhp?.en
                                    it[ActualMedicineSamTableModel.AMP_FAMHP.prescriptionNameGer] =
                                        ampData.prescriptionNameFamhp?.de

                                    it[ActualMedicineSamTableModel.AMP_FAMHP.validFrom] = LocalDate.parse(ampData.from)
                                    if (ampData.to != null) {
                                        it[ActualMedicineSamTableModel.AMP_FAMHP.validTo] = LocalDate.parse(ampData.to)
                                    }
                                }

                                ActualMedicineSamTableModel.AMP_BCPI.insert {
                                    it[ActualMedicineSamTableModel.AMP_BCPI.code] = amp.code
                                    it[ActualMedicineSamTableModel.AMP_BCPI.abbreviatedNameNl] =
                                        ampData.abbreviatedName?.nl
                                    it[ActualMedicineSamTableModel.AMP_BCPI.abbreviatedNameFr] =
                                        ampData.abbreviatedName?.fr
                                    it[ActualMedicineSamTableModel.AMP_BCPI.abbreviatedNameEng] =
                                        ampData.abbreviatedName?.en
                                    it[ActualMedicineSamTableModel.AMP_BCPI.abbreviatedNameGer] =
                                        ampData.abbreviatedName?.de
                                    it[ActualMedicineSamTableModel.AMP_BCPI.proprietarySuffixNl] =
                                        ampData.proprietarySuffix?.nl
                                    it[ActualMedicineSamTableModel.AMP_BCPI.proprietarySuffixFr] =
                                        ampData.proprietarySuffix?.fr
                                    it[ActualMedicineSamTableModel.AMP_BCPI.proprietarySuffixEng] =
                                        ampData.proprietarySuffix?.en
                                    it[ActualMedicineSamTableModel.AMP_BCPI.proprietarySuffixGer] =
                                        ampData.proprietarySuffix?.de
                                    it[ActualMedicineSamTableModel.AMP_BCPI.prescriptionNameNl] =
                                        ampData.prescriptionName?.nl
                                    it[ActualMedicineSamTableModel.AMP_BCPI.prescriptionNameFr] =
                                        ampData.prescriptionName?.fr
                                    it[ActualMedicineSamTableModel.AMP_BCPI.prescriptionNameEng] =
                                        ampData.prescriptionName?.en
                                    it[ActualMedicineSamTableModel.AMP_BCPI.prescriptionNameGer] =
                                        ampData.prescriptionName?.de

                                    it[ActualMedicineSamTableModel.AMP_BCPI.validFrom] = LocalDate.parse(ampData.from)
                                    if (ampData.to != null) {
                                        it[ActualMedicineSamTableModel.AMP_BCPI.validTo] = LocalDate.parse(ampData.to)
                                    }
                                    //logger.info { "Persisted amp ${amp}" }
                                }
                            }

                            for (ampComponent in amp.ampComponents) {
                                for (ampComponentData in ampComponent.dataBlocks) {
                                    ActualMedicineSamTableModel.AMPC_FAMHP.insert {
                                        it[ActualMedicineSamTableModel.AMPC_FAMHP.ampCode] = amp.code
                                        it[ActualMedicineSamTableModel.AMPC_FAMHP.sequenceNumber] =
                                            ampComponent.sequenceNumber.toInt()

                                        it[ActualMedicineSamTableModel.AMPC_FAMHP.validFrom] =
                                            LocalDate.parse(ampComponentData.from)
                                        if (ampComponentData.to != null) {
                                            it[ActualMedicineSamTableModel.AMPC_FAMHP.validTo] =
                                                LocalDate.parse(ampComponentData.to)
                                        }
                                    }

                                    ActualMedicineSamTableModel.AMPC_BCPI.insert {
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.ampCode] = amp.code
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.sequenceNumber] =
                                            ampComponent.sequenceNumber.toInt()
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.vmpcCode] =
                                            ampComponent.vmpComponentCode
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.dividable] = ampComponentData.dividable
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.scored] = ampComponentData.scored
                                        //it[crushable] = ampComponentData.crushable
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.containsAlcohol] =
                                            ampComponentData.containsAlcohol
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.sugarFree] = ampComponentData.sugarFree
                                        //it[modifiedReleaseType] = ampComponentData.modifiedReleaseType
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.specificDrugDevice] =
                                            ampComponentData.specificDrugDevice
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.dimensions] =
                                            ampComponentData.dimensions
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.nameNl] = ampComponentData.name.nl
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.nameFr] = ampComponentData.name.fr
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.nameEng] = ampComponentData.name.en
                                        it[ActualMedicineSamTableModel.AMPC_BCPI.nameGer] = ampComponentData.name.de
                                        //it[noteNl] = ampComponentData.note
                                        //it[concentration] = ampComponentData.concentration
                                        //it[osmoticConcentration] = ampComponentData.osmoticConcentration
                                        //it[caloricValue] = ampComponentData.caloricValue

                                        it[ActualMedicineSamTableModel.AMPC_BCPI.validFrom] =
                                            LocalDate.parse(ampComponentData.from)
                                        if (ampComponentData.to != null) {
                                            it[ActualMedicineSamTableModel.AMPC_BCPI.validTo] =
                                                LocalDate.parse(ampComponentData.to)
                                        }
                                    }

                                    for (pharmaceuticalFormReference in ampComponentData.pharmaceuticalFormReferences) {
                                        ActualMedicineSamTableModel.AMPC_TO_PHARMFORM.insert {
                                            it[ActualMedicineSamTableModel.AMPC_TO_PHARMFORM.ampCode] = amp.code
                                            it[ActualMedicineSamTableModel.AMPC_TO_PHARMFORM.sequenceNumber] =
                                                ampComponent.sequenceNumber.toInt()
                                            it[ActualMedicineSamTableModel.AMPC_TO_PHARMFORM.pharmaFormCode] =
                                                pharmaceuticalFormReference.codeReference

                                            it[ActualMedicineSamTableModel.AMPC_TO_PHARMFORM.validFrom] =
                                                LocalDate.parse(ampComponentData.from)
                                            if (ampComponentData.to != null) {
                                                it[ActualMedicineSamTableModel.AMPC_TO_PHARMFORM.validTo] =
                                                    LocalDate.parse(ampComponentData.to)
                                            }
                                        }
                                    }

                                    for (roaReference in ampComponentData.routesOfAdministration) {
                                        ActualMedicineSamTableModel.AMPC_TO_ROA.insert {
                                            it[ActualMedicineSamTableModel.AMPC_TO_ROA.ampCode] = amp.code
                                            it[ActualMedicineSamTableModel.AMPC_TO_ROA.sequenceNumber] =
                                                ampComponent.sequenceNumber.toInt()
                                            it[ActualMedicineSamTableModel.AMPC_TO_ROA.roaCode] =
                                                roaReference.codeReference

                                            it[ActualMedicineSamTableModel.AMPC_TO_ROA.validFrom] =
                                                LocalDate.parse(ampComponentData.from)
                                            if (ampComponentData.to != null) {
                                                it[ActualMedicineSamTableModel.AMPC_TO_ROA.validTo] =
                                                    LocalDate.parse(ampComponentData.to)
                                            }
                                        }
                                    }
                                }

                                for (ingredient in ampComponent.ingredients) {
                                    for (ingredientData in ingredient.dataBlocks) {
                                        ActualMedicineSamTableModel.RACTING.insert {
                                            it[ActualMedicineSamTableModel.RACTING.ampCode] = amp.code
                                            it[ActualMedicineSamTableModel.RACTING.sequenceNumber] =
                                                ampComponent.sequenceNumber.toInt()
                                            it[ActualMedicineSamTableModel.RACTING.rank] = ingredient.rank.toInt()
                                            it[ActualMedicineSamTableModel.RACTING.type] = ingredientData.type
                                            it[ActualMedicineSamTableModel.RACTING.substanceCode] =
                                                ingredientData.substanceCode.codeReference
                                            //it[knownEffect] = ingredientData
                                            it[ActualMedicineSamTableModel.RACTING.strengthUnit] =
                                                ingredientData.strength?.unit
                                            it[ActualMedicineSamTableModel.RACTING.strengthQuantity] =
                                                ingredientData.strength?.Strength
                                            it[ActualMedicineSamTableModel.RACTING.strengthDescription] =
                                                ingredientData.strengthDescription
                                            //it[additionalInformation] =

                                            it[ActualMedicineSamTableModel.RACTING.validFrom] =
                                                LocalDate.parse(ingredientData.from)
                                            if (ingredientData.to != null) {
                                                it[ActualMedicineSamTableModel.RACTING.validTo] =
                                                    LocalDate.parse(ingredientData.to)
                                            }
                                        }
                                    }
                                    for (actualIngredientEquivalent in ingredient.actualIngredientEquivalents) {
                                        for (dataBlock in actualIngredientEquivalent.dataBlocks) {
                                            ActualMedicineSamTableModel.RACTIEQ.insert {
                                                it[ActualMedicineSamTableModel.RACTIEQ.ampCode] = amp.code
                                                it[ActualMedicineSamTableModel.RACTIEQ.ampcSequenceNumber] =
                                                    ampComponent.sequenceNumber.toInt()
                                                it[ActualMedicineSamTableModel.RACTIEQ.rank] = ingredient.rank.toInt()
                                                it[ActualMedicineSamTableModel.RACTIEQ.sequenceNumber] =
                                                    actualIngredientEquivalent.sequenceNumber.toInt()
                                                it[ActualMedicineSamTableModel.RACTIEQ.type] = dataBlock.type
                                                it[ActualMedicineSamTableModel.RACTIEQ.substanceCode] =
                                                    dataBlock.substanceCode?.codeReference
                                                //it[knownEffect] = dataBlock
                                                it[ActualMedicineSamTableModel.RACTIEQ.strengthQuantity] =
                                                    dataBlock.strength?.Strength
                                                it[ActualMedicineSamTableModel.RACTIEQ.strengthUnit] =
                                                    dataBlock.strength?.unit
                                                it[ActualMedicineSamTableModel.RACTIEQ.strengthDescription] =
                                                    dataBlock.strengthDescription
                                                //it[additionalInformation] = dataBlock.a

                                                it[ActualMedicineSamTableModel.RACTIEQ.validFrom] =
                                                    LocalDate.parse(dataBlock.from)
                                                if (dataBlock.to != null) {
                                                    it[ActualMedicineSamTableModel.RACTIEQ.validTo] =
                                                        LocalDate.parse(dataBlock.to)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            for (amppElement in amp.amppElements) {
                                for (amppDataBlock in amppElement.amppDataBlocks) {
                                    ActualMedicineSamTableModel.AMPP_FAMHP.insert {
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.ctiExtended] = amppElement.ctiExtended
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.ampCode] = amp.code
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.deliveryModusCode] =
                                            amppDataBlock.deliveryModusReference.codeReference
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.deliveryModusSpecificationCode] =
                                            amppDataBlock.deliveryModusSpecReference?.codeReference
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.authorizationNumber] =
                                            amppDataBlock.authorisationNr
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.orphan] = amppDataBlock.orphan
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.leafletLinkNl] =
                                            amppDataBlock.leafletLink?.nl
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.leafletLinkFr] =
                                            amppDataBlock.leafletLink?.fr
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.leafletLinkEng] =
                                            amppDataBlock.leafletLink?.en
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.leafletLinkGer] =
                                            amppDataBlock.leafletLink?.de
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.spcLinkNl] = amppDataBlock.spcLink?.nl
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.spcLinkFr] = amppDataBlock.spcLink?.fr
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.spcLinkEng] =
                                            amppDataBlock.spcLink?.en
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.spcLinkGer] =
                                            amppDataBlock.spcLink?.de
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.rmaPatientLinkNl] =
                                            amppDataBlock.rmaPatientLink?.nl
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.rmaPatientLinkFr] =
                                            amppDataBlock.rmaPatientLink?.fr
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.rmaPatientLinkEng] =
                                            amppDataBlock.rmaPatientLink?.en
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.rmaPatientLinkGer] =
                                            amppDataBlock.rmaPatientLink?.de
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.rmaProfessionalLinkNl] =
                                            amppDataBlock.rmaProfessionalLink?.nl
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.rmaProfessionalLinkFr] =
                                            amppDataBlock.rmaProfessionalLink?.fr
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.rmaProfessionalLinkEng] =
                                            amppDataBlock.rmaProfessionalLink?.en
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.rmaProfessionalLinkGer] =
                                            amppDataBlock.rmaProfessionalLink?.de
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.parallelCircuit] =
                                            amppDataBlock.parallelCircuit
                                        //it[parallelDistributor] = amppDataBlock.parallelDistributor
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.packMultiplier] =
                                            amppDataBlock.packMultiplier?.toInt()
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.packAmount] =
                                            amppDataBlock.packAmount?.PackAmount
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.packAmountUnit] =
                                            amppDataBlock.packAmount?.unit
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.packDisplayValue] =
                                            amppDataBlock.packDisplayValue
                                        //it[gtin] = amppDataBlock.gtin
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.status] = amppDataBlock.status
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.fmdProductCode] =
                                            amppDataBlock.fmdProductCode
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.fmdInScope] = amppDataBlock.fmdInScope
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.antiTamperingDevicePresent] =
                                            amppDataBlock.antiTamperingDevicePresent
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.prescriptionNameNl] =
                                            amppDataBlock.prescriptionNameFamhp?.nl
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.prescriptionNameFr] =
                                            amppDataBlock.prescriptionNameFamhp?.fr
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.prescriptionNameEng] =
                                            amppDataBlock.prescriptionNameFamhp?.en
                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.prescriptionNameGer] =
                                            amppDataBlock.prescriptionNameFamhp?.de

                                        it[ActualMedicineSamTableModel.AMPP_FAMHP.validFrom] =
                                            LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[ActualMedicineSamTableModel.AMPP_FAMHP.validTo] =
                                                LocalDate.parse(amppDataBlock.to)
                                        }
                                    }

                                    ActualMedicineSamTableModel.AMPP_BCFI.insert {
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.ctiExtended] = amppElement.ctiExtended
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.singleUse] = amppDataBlock.singleUse
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.speciallyRegulated] =
                                            amppDataBlock.speciallyRegulated
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.abbreviatedNameNl] =
                                            amppDataBlock.abbreviatedName?.nl
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.abbreviatedNameFr] =
                                            amppDataBlock.abbreviatedName?.fr
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.abbreviatedNameEng] =
                                            amppDataBlock.abbreviatedName?.en
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.abbreviatedNameGer] =
                                            amppDataBlock.abbreviatedName?.de
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.noteNl] = amppDataBlock.note?.nl
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.noteFr] = amppDataBlock.note?.fr
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.noteEng] = amppDataBlock.note?.en
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.noteGer] = amppDataBlock.note?.de
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.posologyNoteNl] =
                                            amppDataBlock.posologyNote?.nl
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.posologyNoteFr] =
                                            amppDataBlock.posologyNote?.fr
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.posologyNoteEng] =
                                            amppDataBlock.posologyNote?.en
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.posologyNoteGer] =
                                            amppDataBlock.posologyNote?.de
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.crmLinkNl] = amppDataBlock.crmLink?.nl
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.crmLinkFr] = amppDataBlock.crmLink?.fr
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.crmLinkEng] = amppDataBlock.crmLink?.en
                                        it[ActualMedicineSamTableModel.AMPP_BCFI.crmLinkGer] = amppDataBlock.crmLink?.de

                                        it[ActualMedicineSamTableModel.AMPP_BCFI.validFrom] =
                                            LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[ActualMedicineSamTableModel.AMPP_BCFI.validTo] =
                                                LocalDate.parse(amppDataBlock.to)
                                        }
                                    }

                                    ActualMedicineSamTableModel.AMPP_NIHDI.insert {
                                        it[ActualMedicineSamTableModel.AMPP_NIHDI.ctiExtended] = amppElement.ctiExtended
                                        it[ActualMedicineSamTableModel.AMPP_NIHDI.exfactory_price] =
                                            amppDataBlock.exFactoryPrice
                                        it[ActualMedicineSamTableModel.AMPP_NIHDI.reimbursementCode] =
                                            amppDataBlock.reimbursementCode
                                        //it[cheap] = amppDataBlock.cheap
                                        //it[cheapest] = amppDataBlock.cheapest
                                        it[ActualMedicineSamTableModel.AMPP_NIHDI.index] = amppDataBlock.index
                                        it[ActualMedicineSamTableModel.AMPP_NIHDI.bigPackage] = amppDataBlock.bigPackage

                                        it[ActualMedicineSamTableModel.AMPP_NIHDI.validFrom] =
                                            LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[ActualMedicineSamTableModel.AMPP_NIHDI.validTo] =
                                                LocalDate.parse(amppDataBlock.to)
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
                                        it[ActualMedicineSamTableModel.AMPP_ECON.ctiExtended] = amppElement.ctiExtended
                                        it[ActualMedicineSamTableModel.AMPP_ECON.officialExFactoryPrice] =
                                            amppDataBlock.officialExFactoryPrice
                                        it[ActualMedicineSamTableModel.AMPP_ECON.realExFactoryPrice] =
                                            amppDataBlock.realExFactoryPrice

                                        if (amppDataBlock.pricingDecisionDate != null) {
                                            it[ActualMedicineSamTableModel.AMPP_ECON.decisionDate] =
                                                LocalDate.parse(amppDataBlock.pricingDecisionDate)
                                        }

                                        it[ActualMedicineSamTableModel.AMPP_ECON.validFrom] =
                                            LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[ActualMedicineSamTableModel.AMPP_ECON.validTo] =
                                                LocalDate.parse(amppDataBlock.to)
                                        }
                                    }
                                }

                                for (amppComponent in amppElement.amppComponents) {
                                    for (amppComponentDataBlock in amppComponent.amppComponentDataBlocks) {
                                        ActualMedicineSamTableModel.AMPPC.insert {
                                            it[ActualMedicineSamTableModel.AMPPC.ctiExtended] = amppElement.ctiExtended
                                            it[ActualMedicineSamTableModel.AMPPC.sequenceNumber] =
                                                amppComponent.sequenceNumber.toInt()
                                            it[ActualMedicineSamTableModel.AMPPC.ampcSequenceNumber] =
                                                amppComponentDataBlock.ampcSequenceNr?.toInt()
                                            it[ActualMedicineSamTableModel.AMPPC.deviceTypeCode] =
                                                amppComponentDataBlock.deviceType?.codeReference
                                            it[ActualMedicineSamTableModel.AMPPC.packagingTypeCode] =
                                                amppComponentDataBlock.packagingType?.codeReference
                                            it[ActualMedicineSamTableModel.AMPPC.contentType] =
                                                amppComponentDataBlock.contentType
                                            it[ActualMedicineSamTableModel.AMPPC.contentMultiplier] =
                                                amppComponentDataBlock.contentMultiplier?.toInt()
                                            it[ActualMedicineSamTableModel.AMPPC.packSpecification] =
                                                amppComponentDataBlock.packSpecification

                                            it[ActualMedicineSamTableModel.AMPPC.validFrom] =
                                                LocalDate.parse(amppComponentDataBlock.from)
                                            if (amppComponentDataBlock.to != null) {
                                                it[ActualMedicineSamTableModel.AMPPC.validTo] =
                                                    LocalDate.parse(amppComponentDataBlock.to)
                                            }
                                        }
                                    }
                                    for (ampComponentEquivalent in amppComponent.ampComponentEquivalents) {
                                        for (amppCompEquiData in ampComponentEquivalent.amppComponentEquivalentDataBlocks) {
                                            ActualMedicineSamTableModel.AMPPCES.insert {
                                                it[ActualMedicineSamTableModel.AMPPCES.ctiExtended] =
                                                    amppElement.ctiExtended
                                                it[ActualMedicineSamTableModel.AMPPCES.amppcSequenceNumber] =
                                                    amppComponent.sequenceNumber.toInt()
                                                it[ActualMedicineSamTableModel.AMPPCES.sequenceNumber] =
                                                    ampComponentEquivalent.sequenceNumber.toInt()
                                                it[ActualMedicineSamTableModel.AMPPCES.contentQuantity] =
                                                    amppCompEquiData.content?.Content
                                                it[ActualMedicineSamTableModel.AMPPCES.contentUnit] =
                                                    amppCompEquiData.content?.unit

                                                it[ActualMedicineSamTableModel.AMPPCES.validFrom] =
                                                    LocalDate.parse(amppCompEquiData.from)
                                                if (amppCompEquiData.to != null) {
                                                    it[ActualMedicineSamTableModel.AMPPCES.validTo] =
                                                        LocalDate.parse(amppCompEquiData.to)
                                                }
                                            }
                                        }
                                    }
                                }

                                for (dmpp in amppElement.dmpps) {
                                    for (dmppDataBlock in dmpp.dmppDataBlocks) {
                                        ActualMedicineSamTableModel.DMPP.insert {
                                            it[ActualMedicineSamTableModel.DMPP.code] = dmpp.code
                                            it[ActualMedicineSamTableModel.DMPP.codeType] = dmpp.codeSystem
                                            it[ActualMedicineSamTableModel.DMPP.productId] = dmpp.productId
                                            it[ActualMedicineSamTableModel.DMPP.deliveryEnvironment] =
                                                dmpp.deliveryEnvironment
                                            it[ActualMedicineSamTableModel.DMPP.price] = dmppDataBlock.price
                                            it[ActualMedicineSamTableModel.DMPP.reimbursable] =
                                                dmppDataBlock.reimbursable
                                            //it[reimbursementRequiresPriorAgreement] = dmppDataBlock.
                                            //it[cheapestCeilingPricesStatus5] = dmppDataBlock.ch

                                            it[ActualMedicineSamTableModel.DMPP.validFrom] =
                                                LocalDate.parse(dmppDataBlock.from)
                                            if (dmppDataBlock.to != null) {
                                                it[ActualMedicineSamTableModel.DMPP.validTo] =
                                                    LocalDate.parse(dmppDataBlock.to)
                                            }
                                        }
                                    }
                                }

                                for (commercialization in amppElement.commercialization) {
                                    for (commercializationDataBlock in commercialization.commercializationDataBlocks) {
                                        ActualMedicineSamTableModel.CMRCL.insert {
                                            it[ActualMedicineSamTableModel.CMRCL.ctiExtended] = amppElement.ctiExtended
                                            it[ActualMedicineSamTableModel.CMRCL.endOfCommercializationNl] =
                                                commercializationDataBlock.endOfCommercialization?.nl
                                            it[ActualMedicineSamTableModel.CMRCL.endOfCommercializationFr] =
                                                commercializationDataBlock.endOfCommercialization?.fr
                                            it[ActualMedicineSamTableModel.CMRCL.endOfCommercializationEng] =
                                                commercializationDataBlock.endOfCommercialization?.en
                                            it[ActualMedicineSamTableModel.CMRCL.endOfCommercializationGer] =
                                                commercializationDataBlock.endOfCommercialization?.de

                                            it[ActualMedicineSamTableModel.CMRCL.reasonEndOfCommercializationNl] =
                                                commercializationDataBlock.reason?.nl
                                            it[ActualMedicineSamTableModel.CMRCL.reasonEndOfCommercializationFr] =
                                                commercializationDataBlock.reason?.fr
                                            it[ActualMedicineSamTableModel.CMRCL.reasonEndOfCommercializationEng] =
                                                commercializationDataBlock.reason?.en
                                            it[ActualMedicineSamTableModel.CMRCL.reasonEndOfCommercializationGer] =
                                                commercializationDataBlock.reason?.de

                                            it[ActualMedicineSamTableModel.CMRCL.additionalInformationNl] =
                                                commercializationDataBlock.impact?.nl
                                            it[ActualMedicineSamTableModel.CMRCL.additionalInformationFr] =
                                                commercializationDataBlock.impact?.fr
                                            it[ActualMedicineSamTableModel.CMRCL.additionalInformationEng] =
                                                commercializationDataBlock.impact?.en
                                            it[ActualMedicineSamTableModel.CMRCL.additionalInformationGer] =
                                                commercializationDataBlock.impact?.de

                                            it[ActualMedicineSamTableModel.CMRCL.validFrom] =
                                                LocalDate.parse(commercializationDataBlock.from)
                                            if (commercializationDataBlock.to != null) {
                                                it[ActualMedicineSamTableModel.CMRCL.validTo] =
                                                    LocalDate.parse(commercializationDataBlock.to)
                                            }
                                        }
                                    }
                                }

                                for (supplyProblem in amppElement.supplyProblems) {
                                    for (supplyDataBlock in supplyProblem.supplyDataBlocks) {
                                        ActualMedicineSamTableModel.SPPROB.insert {
                                            it[ActualMedicineSamTableModel.SPPROB.ctiExtended] = amppElement.ctiExtended
                                            if (supplyDataBlock.expectedEnd != null) {
                                                it[ActualMedicineSamTableModel.SPPROB.expectedEndDate] =
                                                    LocalDate.parse(supplyDataBlock.expectedEnd)
                                            }
                                            it[ActualMedicineSamTableModel.SPPROB.reportedBy] =
                                                supplyDataBlock.reportedBy
                                            if (supplyDataBlock.reportedOn != null) {
                                                it[ActualMedicineSamTableModel.SPPROB.reportedOn] =
                                                    LocalDate.parse(supplyDataBlock.reportedOn)
                                            }

                                            it[ActualMedicineSamTableModel.SPPROB.contactName] =
                                                supplyDataBlock.contactName
                                            it[ActualMedicineSamTableModel.SPPROB.contactCompany] =
                                                supplyDataBlock.contactCompany
                                            it[ActualMedicineSamTableModel.SPPROB.contactMail] =
                                                supplyDataBlock.contactMail
                                            it[ActualMedicineSamTableModel.SPPROB.contactPhone] = supplyDataBlock.phone

                                            it[ActualMedicineSamTableModel.SPPROB.reasonNl] = supplyDataBlock.reason?.nl
                                            it[ActualMedicineSamTableModel.SPPROB.reasonFr] = supplyDataBlock.reason?.fr
                                            it[ActualMedicineSamTableModel.SPPROB.reasonEng] =
                                                supplyDataBlock.reason?.en
                                            it[ActualMedicineSamTableModel.SPPROB.reasonGer] =
                                                supplyDataBlock.reason?.de

                                            it[ActualMedicineSamTableModel.SPPROB.additionalInformationNl] =
                                                supplyDataBlock.additionalInformation?.nl
                                            it[ActualMedicineSamTableModel.SPPROB.additionalInformationFr] =
                                                supplyDataBlock.additionalInformation?.fr
                                            it[ActualMedicineSamTableModel.SPPROB.additionalInformationEng] =
                                                supplyDataBlock.additionalInformation?.en
                                            it[ActualMedicineSamTableModel.SPPROB.additionalInformationGer] =
                                                supplyDataBlock.additionalInformation?.de

                                            it[ActualMedicineSamTableModel.SPPROB.impactNl] = supplyDataBlock.impact?.nl
                                            it[ActualMedicineSamTableModel.SPPROB.impactFr] = supplyDataBlock.impact?.fr
                                            it[ActualMedicineSamTableModel.SPPROB.impactEng] =
                                                supplyDataBlock.impact?.en
                                            it[ActualMedicineSamTableModel.SPPROB.impactGer] =
                                                supplyDataBlock.impact?.de

                                            //it[limitedAvailability] = supplyDataBlock.

                                            it[ActualMedicineSamTableModel.SPPROB.validFrom] =
                                                LocalDate.parse(supplyDataBlock.from)
                                            if (supplyDataBlock.to != null) {
                                                it[ActualMedicineSamTableModel.SPPROB.validTo] =
                                                    LocalDate.parse(supplyDataBlock.to)
                                            }
                                        }
                                    }
                                }

                                for (derogationImport in amppElement.derogationImport) {
                                    for (derogationDatablock in derogationImport.derogationData) {
                                        ActualMedicineSamTableModel.DRGIMP.insert {
                                            it[ActualMedicineSamTableModel.DRGIMP.ctiExtended] = amppElement.ctiExtended
                                            it[ActualMedicineSamTableModel.DRGIMP.sequenceNumber] =
                                                derogationImport.sequenceNumber.toInt()
                                            it[ActualMedicineSamTableModel.DRGIMP.noteNl] = derogationDatablock.note?.nl
                                            it[ActualMedicineSamTableModel.DRGIMP.noteFr] = derogationDatablock.note?.fr
                                            it[ActualMedicineSamTableModel.DRGIMP.noteEng] =
                                                derogationDatablock.note?.en
                                            it[ActualMedicineSamTableModel.DRGIMP.noteGer] =
                                                derogationDatablock.note?.de

                                            it[ActualMedicineSamTableModel.DRGIMP.validFrom] =
                                                LocalDate.parse(derogationDatablock.from)
                                            if (derogationDatablock.to != null) {
                                                it[ActualMedicineSamTableModel.DRGIMP.validTo] =
                                                    LocalDate.parse(derogationDatablock.to)
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