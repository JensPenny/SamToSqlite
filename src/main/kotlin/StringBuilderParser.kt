import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.module.kotlin.readValue
import db.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import pojo.*
import xml.createXmlInputFactory
import xml.createXmlMapper
import java.io.FileInputStream
import java.io.StringWriter
import java.time.LocalDate
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.StartElement
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private val logger = LoggerFactory.getLogger("SamV2Exporter")

@OptIn(ExperimentalTime::class)
fun main() {
    val dbInit = DBInitialization()
    dbInit.createDB()
    dbInit.createTables()

    val inputFactory = createXmlInputFactory()
    val xmlMapper = createXmlMapper(inputFactory)

    logger.info("Starting export")
    //Todo: make paths also variable

    val fullTime = measureTime {


        val ampParseTime = measureTime { parseAmpXml(inputFactory, xmlMapper, "res/latest/AMP-1667395273070.xml") }

        //done
        val compoundingTime =
            measureTime { parseCompoundingXml(inputFactory, xmlMapper, "res/latest/CMP-1667395564754.xml") }

        //done
        val cpnParseTime = measureTime { parseCompanyXml(inputFactory, xmlMapper, "res/latest/CPN-1667395271162.xml") }

        //done
        val nonmedicinalTime =
            measureTime { parseNonMedicinalXml(inputFactory, xmlMapper, "res/latest/NONMEDICINAL-1667395565095.xml") }

        //done
        val refParseTime =
            measureTime { parseReferenceXml(inputFactory, xmlMapper, "res/latest/REF-1667395561910.xml") }

        //Pooling logresults to make this stuff more readable
        logger.info("AMP file parsed in ${ampParseTime.inWholeMinutes}:${ampParseTime.inWholeSeconds - (ampParseTime.inWholeMinutes * 60)}")
        logger.info("CMP file parsed in ${compoundingTime.inWholeMinutes}:${compoundingTime.inWholeSeconds - (compoundingTime.inWholeMinutes * 60)}")
        logger.info("CPN file parsed in ${cpnParseTime.inWholeMinutes}:${cpnParseTime.inWholeSeconds - (cpnParseTime.inWholeMinutes * 60)}")
        logger.info("NONMEDICINAL file parsed in ${nonmedicinalTime.inWholeMinutes}:${nonmedicinalTime.inWholeSeconds - (nonmedicinalTime.inWholeMinutes * 60)}")
        logger.info("REF file parsed in ${refParseTime.inWholeMinutes}:${refParseTime.inWholeSeconds - (refParseTime.inWholeMinutes * 60)}")
    }
    logger.info("Full export parsed in ${fullTime.inWholeMinutes}:${fullTime.inWholeSeconds - (fullTime.inWholeMinutes * 60)}")

}

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
                            }

                            for (amppElement in amp.amppElements) {
                                for (amppDataBlock in amppElement.amppDataBlocks) {
                                    ActualMedicineSamTableModel.AMPP_FAMHP.insert {
                                        it[ctiExtended] = amppElement.ctiExtended
                                        it[ampCode] = amp.code
                                        it[deliveryModusCode] =
                                            amppDataBlock.deliveryModusReference.codeReference
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
                                        it[antiTamperingDevicePresent] =
                                            amppDataBlock.antiTamperingDevicePresent
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

                                    ActualMedicineSamTableModel.AMPP_ECON.insert {
                                        it[ctiExtended] = amppElement.ctiExtended
                                        it[officialExFactoryPrice] = amppDataBlock.officialExFactoryPrice
                                        it[realExFactoryPrice] = amppDataBlock.realExFactoryPrice

                                        if (amppDataBlock.pricingDecisionDate != null) {
                                            it[decisionDate] =
                                                LocalDate.parse(amppDataBlock.pricingDecisionDate)
                                        }

                                        it[validFrom] = LocalDate.parse(amppDataBlock.from)
                                        if (amppDataBlock.to != null) {
                                            it[validTo] = LocalDate.parse(amppDataBlock.to)
                                        }
                                    }

                                    //Log persisted ampp
                                }

                                for (amppComponent in amppElement.amppComponents) {

                                }

                                for (dmpp in amppElement.dmpps) {

                                }

                                for (commercialization in amppElement.commercialization) {

                                }

                                for (supplyProblem in amppElement.supplyProblems) {

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

fun parseNonMedicinalXml(
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
                "ns3:NonMedicinalProduct" -> {
                    val nonmedicinalString = fullElement(startElement, reader)
                    val nonmedicinal = xmlMapper.readValue<Nonmedicinal>(nonmedicinalString)

                    tryPersist {
                        transaction {

                            for (datablock in nonmedicinal.datablocks) {
                                NonmedicinalTableModel.NONMEDICINAL.insert {
                                    it[productId] = nonmedicinal.ProductId
                                    it[cnk] = nonmedicinal.code

                                    it[nameNl] = datablock.name.nl!!
                                    it[nameFr] = datablock.name.fr!!
                                    it[nameEng] = datablock.name.en
                                    it[nameGer] = datablock.name.de

                                    it[category] = datablock.category
                                    it[commercialStatus] = datablock.commercialStatus

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

fun parseCompoundingXml(
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
                "ns2:CompoundingIngredient" -> {
                    val compoundingIngredientString = fullElement(startElement, reader)
                    val compoundingIngredient =
                        xmlMapper.readValue<CompoundingIngredient>(compoundingIngredientString)

                    tryPersist {
                        transaction {
                            CompoundingTableModel.COMP_INGREDIENT.insert {
                                it[productId] = compoundingIngredient.ProductId
                                it[codeSystem] = compoundingIngredient.codeType
                                it[code] = compoundingIngredient.code
                            }

                            for (data in compoundingIngredient.data) {
                                for (synonymObject in data.synonyms) {
                                    CompoundingTableModel.COMP_INGREDIENT_SYNONYM.insert {
                                        it[ingredientProductId] = compoundingIngredient.ProductId
                                        it[language] = synonymObject.language
                                        it[rank] = synonymObject.rank
                                        it[synonym] = synonymObject.Synonym

                                        it[validFrom] = LocalDate.parse(data.from)
                                        if (data.to != null) {
                                            it[validTo] = LocalDate.parse(data.to)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "ns2:CompoundingFormula" -> {
                    val compoundingFormulaString = fullElement(startElement, reader)
                    val compoundingFormula = xmlMapper.readValue<CompoundingFormula>(compoundingFormulaString)

                    tryPersist {
                        transaction {
                            CompoundingTableModel.COMP_FORMULA.insert {
                                it[productId] = compoundingFormula.ProductId
                                it[codeSystem] = compoundingFormula.codeType
                                it[code] = compoundingFormula.code
                            }

                            for (data in compoundingFormula.data) {
                                for (synonymObject in data.synonyms) {
                                    CompoundingTableModel.COMP_FORMULA_SYNONYM.insert {
                                        it[formulaProductId] = compoundingFormula.ProductId
                                        it[language] = synonymObject.language
                                        it[rank] = synonymObject.rank
                                        it[synonym] = synonymObject.Synonym

                                        it[validFrom] = LocalDate.parse(data.from)
                                        if (data.to != null) {
                                            it[validTo] = LocalDate.parse(data.to)
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

fun parseCompanyXml(
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
                "ns2:Company" -> {
                    val companyString = fullElement(startElement, reader)
                    val company = xmlMapper.readValue<Company>(companyString)

                    tryPersist {
                        transaction {
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


fun parseReferenceXml(
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
                "AtcClassification" -> {
                    val atcClassificationString = fullElement(startElement, reader)
                    val atc = xmlMapper.readValue<ATC>(atcClassificationString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.ATC.insert {
                                it[code] = atc.atcCode
                                it[description] = atc.description
                            }
                        }
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
                    }
                }

                "DeliveryModusSpecification" -> {
                    val deliveryModeSpecString = fullElement(startElement, reader)
                    val deliveryModeSpec =
                        xmlMapper.readValue<DeliveryModusSpecification>(deliveryModeSpecString)
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
                    val wadaString = fullElement(startElement, reader)
                    val wada = xmlMapper.readValue<Wada>(wadaString)
                    tryPersist {
                        transaction {
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
                    }
                }

                "NoGenericPrescriptionReason" -> {
                    val noGenPrescrReasonStr = fullElement(startElement, reader)
                    val noGenPrescrReason =
                        xmlMapper.readValue<NoGenericPrescriptionReason>(noGenPrescrReasonStr)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.NOGNPR.insert {
                                it[code] = noGenPrescrReason.code
                                it[descriptionNl] = noGenPrescrReason.description.nl
                                it[descriptionFr] = noGenPrescrReason.description.fr
                                it[descriptionEng] = noGenPrescrReason.description.en
                                it[descriptionGer] = noGenPrescrReason.description.de
                            }
                        }
                    }
                }

                "StandardForm" -> {
                    val standardFormString = fullElement(startElement, reader)
                    val standardForm = xmlMapper.readValue<StandardForm>(standardFormString)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.STDFRM.insert {
                                it[standard] = standardForm.standard
                                it[code] = standardForm.code
                                it[virtualFormCode] = standardForm.virtualFormReference.codeReference
                            }
                        }
                    }
                }

                "StandardRoute" -> {
                    val standardRouteString = fullElement(startElement, reader)
                    val standardRoute = xmlMapper.readValue<StandardRoute>(standardRouteString)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.STDROA.insert {
                                it[standard] = standardRoute.standard
                                it[code] = standardRoute.code
                                it[routeOfAdministrationCode] =
                                    standardRoute.routeOfAdminReference.codeReference
                            }
                        }
                    }
                }

                "StandardSubstance" -> {
                    val standardSubstanceString = fullElement(startElement, reader)
                    val standardSubstance = xmlMapper.readValue<StandardSubstance>(standardSubstanceString)

                    tryPersist {
                        transaction {
                            for (singleReference in standardSubstance.substanceReference) {
                                ReferenceTableModel.STDSBST.insert {
                                    it[standard] = standardSubstance.standard
                                    it[code] = standardSubstance.code
                                    it[substanceCode] = singleReference.codeReference
                                }
                            }
                        }
                    }
                }

                "StandardUnit" -> {
                    val standardUnitString = fullElement(startElement, reader)
                    val standardUnit = xmlMapper.readValue<StandardUnit>(standardUnitString)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.STDUNT.insert {
                                it[name] = standardUnit.name
                                it[descriptionNl] = standardUnit.description?.nl
                                it[descriptionFr] = standardUnit.description?.fr
                                it[descriptionEng] = standardUnit.description?.en
                                it[descriptionGer] = standardUnit.description?.de
                            }
                        }
                    }
                }

                "Appendix" -> {
                    val appendixString = fullElement(startElement, reader)
                    val appendix = xmlMapper.readValue<Appendix>(appendixString)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.APPENDIX.insert {
                                it[code] = appendix.code
                                it[descriptionNl] = appendix.description.nl
                                it[descriptionFr] = appendix.description.fr
                                it[descriptionEng] = appendix.description.en
                                it[descriptionGer] = appendix.description.de
                            }
                        }
                    }
                }

                "FormCategory" -> {
                    val formCatString = fullElement(startElement, reader)
                    val formCategory = xmlMapper.readValue<FormCategory>(formCatString)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.FORMCAT.insert {
                                it[code] = formCategory.code
                                it[descriptionNl] = formCategory.description.nl
                                it[descriptionFr] = formCategory.description.fr
                                it[descriptionEng] = formCategory.description.en
                                it[descriptionGer] = formCategory.description.de
                            }
                        }
                    }
                }

                "Parameter" -> {
                    val parameterString = fullElement(startElement, reader)

                    logger.debug("Did absolutely nothing with a parsed 'parameter'")
                }

                "ReimbursementCriterion" -> {
                    val reimbCritString = fullElement(startElement, reader)
                    val reimbursementCriterion = xmlMapper.readValue<ReimbursementCriterion>(reimbCritString)

                    tryPersist {
                        transaction {
                            ReferenceTableModel.RMBCRIT.insert {
                                it[code] = reimbursementCriterion.code
                                it[category] = reimbursementCriterion.category
                                it[descriptionNl] = reimbursementCriterion.description.nl
                                it[descriptionFr] = reimbursementCriterion.description.fr
                                it[descriptionEng] = reimbursementCriterion.description.en
                                it[descriptionGer] = reimbursementCriterion.description.de
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
