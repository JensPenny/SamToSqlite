package db

import db.ActualMedicineSamTableModel.RACTIEQ.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

/**
 * Collection of Samv2 tables in their pure form.
 */
class ActualMedicineSamTableModel {


    //Actual Medicinal Product
    //Sem. key = code
    //Foreign keys:
    //CPN: Company Actor Number
    //VMP: VMP Code
    object AMP_FAMHP : IntIdTable("AMP_FAMHP") {
        val code = varchar("code", 12)
        val vmpCode = integer("vmpCode").nullable()
        val companyActorNumber = integer("companyActorNumber")
        val status = varchar("status", 10)
        val blackTriangle = bool("blackTriangle")
        val officialName = varchar("officialName", 255)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGerman = varchar("nameGer", 255).nullable()
        val nameEnglish = varchar("nameEng", 255).nullable()
        val medicineType = varchar("medicineType", 30)
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()

        //rare shit in de docs - prescription en prescriptiontranslations? Lijken 'prescriptionnamefamhp' te zijn?
        val prescriptionNameNl = text("prescriptionNameNl").nullable()
        val prescriptionNameFr = text("prescriptionNameFr").nullable()
        val prescriptionNameGer = text("prescriptionNameGer").nullable()
        val prescriptionNameEng = text("prescriptionNameEng").nullable()

    }

    object AMP_BCPI : IntIdTable("AMP_BCPI") {
        val code = varchar("code", 12)
        val abbreviatedNameNl = varchar("abbreviatedNameNl", 255).nullable()
        val abbreviatedNameFr = varchar("abbreviatedNameFr", 255).nullable()
        val abbreviatedNameGer = varchar("abbreviatedNameGer", 255).nullable()
        val abbreviatedNameEng = varchar("abbreviatedNameEng", 255).nullable()
        val proprietarySuffixNl = varchar("proprietarySuffNl", 255).nullable()
        val proprietarySuffixFr = varchar("proprietarySuffFr", 255).nullable()
        val proprietarySuffixGer = varchar("proprietarySuffGer", 255).nullable()
        val proprietarySuffixEng = varchar("proprietarySuffEng", 255).nullable()
        val prescriptionNameNl = text("prescriptionNameNl").nullable()
        val prescriptionNameFr = text("prescriptionNameFr").nullable()
        val prescriptionNameGer = text("prescriptionNameGer").nullable()
        val prescriptionNameEng = text("prescriptionNameEng").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //AMP Component
    //Sem. Key: AMP Code + sequence number
    //For. keys:
    //AMP: AMP code
    //VMPC: VMPC code

    //Wauw nuttig object
    object AMPC_FAMHP : IntIdTable("AMPC_FAMHP") {
        val ampCode = varchar("ampCode", 12)
        val sequenceNumber = integer("sequenceNumber")
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object AMPC_BCPI : IntIdTable("AMPC_BCPI") {
        val ampCode = varchar("ampCode", 12)
        val sequenceNumber = integer("sequenceNumber")

        val vmpcCode = varchar("vmpcCode", 9).nullable()
        val dividable = varchar("dividable", 1).nullable()
        val scored = varchar("scored", 1).nullable()
        val crushable = varchar("crushable", 1).nullable()
        val containsAlcohol = varchar("containsAlcohol", 1).nullable()
        val sugarFree = bool("sugarFree").nullable()
        val modifiedReleaseType = varchar("modifiedReleaseType", 2).nullable()
        val specificDrugDevice = varchar("specificDrugDevice", 2).nullable()
        val dimensions = varchar("dimensions", 50).nullable()
        val nameNl = varchar("nameNl", 255).nullable()
        val nameFr = varchar("nameFr", 255).nullable()
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
        val noteNl = text("noteNl").nullable()
        val noteFr = text("noteFr").nullable()
        val noteGer = text("noteGer").nullable()
        val noteEng = text("noteEng").nullable()
        val concentration = text("concentration").nullable() //Is this actually text?
        val osmoticConcentration = integer("osmoticConcentration").nullable()
        val caloricValue = integer("caloricValue").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Linking tables
    //Linking table amp component to form
    object AMPC_TO_PHARMFORM : IntIdTable("AMPC_TO_PHARMFORM") {
        val ampCode = varchar("ampCode", 12)
        val sequenceNumber = integer("sequenceNumber")
        val pharmaFormCode = varchar("pharmaCode", 10)
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Linking table amp component to route of administration
    object AMPC_TO_ROA : IntIdTable("AMPC_TO_ROA") {
        val ampCode = varchar("ampCode", 12)
        val sequenceNumber = integer("sequenceNumber")
        val roaCode = varchar("roaCode", 10)
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Real Actual Ingredient
    object RACTING : IntIdTable("RACTING") {
        val ampCode = varchar("ampCode", 12)
        val sequenceNumber = integer("sequenceNumber")
        val rank = integer("rank")
        val type = varchar("type", 20)
        val substanceCode = varchar("substanceCode", 10)
        val knownEffect = bool("knownEffect").nullable()
        val strengthQuantity = varchar("strengthQuantity", 20).nullable()
        val strengthUnit = varchar("strengthUnit", 20).nullable()
        val strengthDescription = varchar("strengthDescription", 50).nullable()
        val additionalInformation = varchar("additionalInformation", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Real Actual Ingredient Equivalent
    object RACTIEQ : IntIdTable("RACTIEQ") {
        val ampCode = varchar("ampCode", 12)
        val ampcSequenceNumber = integer("ampcSequenceNumber")
        val rank = integer("rank")
        val sequenceNumber = integer("sequenceNumber")
        val type = varchar("type", 20).nullable()
        val substanceCode = varchar("substanceCode", 10).nullable()
        val knownEffect = bool("knownEffect").nullable()
        val strengthQuantity = varchar("strengthQuantity", 20).nullable()
        val strengthUnit = varchar("strengthUnit", 20).nullable()
        val strengthDescription = varchar("strengthDescription", 50).nullable()
        val additionalInformation = varchar("additionalInformation", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //AMP Package
    //Sem. Key: CTI-Extended
    //Foreign keys:
    //AMP: AMP Code
    //DLVM: Delivery Modus Code
    //DLVMS: Delivery Modus Specification Code
    object AMPP_FAMHP : IntIdTable("AMPP_FAMHP") {
        val ctiExtended = varchar("ctiExtended", 9)
        val ampCode = varchar("ampCode", 12)
        val deliveryModusCode = varchar("deliveryModusCode", 7)
        val deliveryModusSpecificationCode = varchar("deliveryModusSpecificationCode", 7).nullable()
        val authorizationNumber = varchar("authorizationNumber", 50)
        val orphan = bool("orphan")
        val leafletLinkNl = varchar("leafletLinkNl", 255).nullable()
        val leafletLinkFr = varchar("leafletLinkFr", 255).nullable()
        val leafletLinkEng = varchar("leafletLinkEng", 255).nullable()
        val leafletLinkGer = varchar("leafletLinkGer", 255).nullable()
        val spcLinkNl = varchar("spcLinkNl", 255).nullable()
        val spcLinkFr = varchar("spcLinkFr", 255).nullable()
        val spcLinkEng = varchar("spcLinkEng", 255).nullable()
        val spcLinkGer = varchar("spcLinkGer", 255).nullable()
        val rmaPatientLinkNl = varchar("rmaPatientLinkNl", 255).nullable()
        val rmaPatientLinkFr = varchar("rmaPatientLinkFr", 255).nullable()
        val rmaPatientLinkEng = varchar("rmaPatientLinkEng", 255).nullable()
        val rmaPatientLinkGer = varchar("rmaPatientLinkGer", 255).nullable()
        val rmaProfessionalLinkNl = varchar("rmaProfessionalLinkNl", 255).nullable()
        val rmaProfessionalLinkFr = varchar("rmaProfessionalLinkFr", 255).nullable()
        val rmaProfessionalLinkEng = varchar("rmaProfessionalLinkEng", 255).nullable()
        val rmaProfessionalLinkGer = varchar("rmaProfessionalLinkGer", 255).nullable()
        val parallelCircuit = varchar("parallelCircuit", 1).nullable()
        val parallelDistributor = varchar("parallelDistributor", 255).nullable()
        val packMultiplier = integer("packMultiplier").nullable()
        val packAmount = varchar("packAmount", 20).nullable()
        val packAmountUnit = varchar("packAmountUnit", 20).nullable()
        val packDisplayValue = varchar("packDisplayValue", 255).nullable()
        val gtin = varchar("gtin", 20).nullable()
        val status = varchar("status", 10).nullable()
        val fmdProductCode = text("fmdProductCode").nullable() //Is an array
        val fmdInScope = bool("fmdInScope").nullable()
        val antiTamperingDevicePresent = bool("antiTamperingDevicePresent").nullable() //anti tempering in docs, but is wrong

        val prescriptionNameNl = text("prescriptionNameNl").nullable()
        val prescriptionNameFr = text("prescriptionNameFr").nullable()
        val prescriptionNameGer = text("prescriptionNameGer").nullable()
        val prescriptionNameEng = text("prescriptionNameEng").nullable()

        val rmaKeyMessagesNl = text("rmaKeyMessagesNl").nullable()
        val rmaKeyMessagesFr = text("rmaKeyMessagesFr").nullable()
        val rmaKeyMessagesGer = text("rmaKeyMessagesGer").nullable()
        val rmaKeyMessagesEng = text("rmaKeyMessagesEng").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object AMPP_BCFI : IntIdTable("AMPP_BCFI") {
        val ctiExtended = varchar("ctiExtended", 9)
        val singleUse = bool("singleUse").nullable()
        val speciallyRegulated = varchar("speciallyRegulated", 1).nullable()

        val abbreviatedNameNl = varchar("abbreviatedNameNl", 255).nullable()
        val abbreviatedNameFr = varchar("abbreviatedNameFr", 255).nullable()
        val abbreviatedNameGer = varchar("abbreviatedNameGer", 255).nullable()
        val abbreviatedNameEng = varchar("abbreviatedNameEng", 255).nullable()

        val noteNl = varchar("noteNl", 255).nullable()
        val noteFr = varchar("noteFr", 255).nullable()
        val noteGer = varchar("noteGer", 255).nullable()
        val noteEng = varchar("noteEng", 255).nullable()

        val posologyNoteNl = text("posologyNoteNl").nullable()
        val posologyNoteFr = text("posologyNoteFr").nullable()
        val posologyNoteGer = text("posologyNoteGer").nullable()
        val posologyNoteEng = text("posologyNoteEng").nullable()

        val crmLinkNl = text("crmLinkNl").nullable()
        val crmLinkFr = text("crmLinkFr").nullable()
        val crmLinkGer = text("crmLinkGer").nullable()
        val crmLinkEng = text("crmLinkEng").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object AMPP_NIHDI : IntIdTable("AMPP_NIHDI") {
        val ctiExtended = varchar("ctiExtended", 9)
        val exfactory_price = varchar("exfactory_price", 20).nullable()
        val reimbursementCode = varchar("reimbursementCode", 10).nullable()
        val cheap = bool("cheap").nullable()
        val cheapest = bool("cheapest").nullable()
        val index = varchar("index", 20).nullable()
        val bigPackage = bool("bigPackage").nullable()
        val contraceptive = bool("contraceptive").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object AMPP_NIHDI_BIS : IntIdTable("AMPP_NIHDI_BIS") {
        val ctiExtended = varchar("ctiExtended", 9)
        val definedDailyDose = varchar("definedDailyDose", 20).nullable()
        val definedDailyDoseUnit = varchar("definedDailyDoseUnit", 20).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object AMPP_ECON : IntIdTable("AMPP_ECON") {
        val ctiExtended = varchar("ctiExtended", 9)

        val officialExFactoryPrice = varchar("officialExFactoryPrice", 20).nullable()
        val realExFactoryPrice = varchar("realExFactoryPrice", 20).nullable()
        val decisionDate = date("decisionDate").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //AMPP Component
    object AMPPC : IntIdTable("AMPPC") {
        val ctiExtended = varchar("ctiExtended", 9)
        val sequenceNumber = integer("sequenceNumber")
        val ampcSequenceNumber = integer("ampcSequenceNumber").nullable()
        val deviceTypeCode = varchar("deviceTypeCode", 8).nullable()
        val packagingTypeCode = varchar("packagingTypeCode", 8).nullable()
        val contentType = varchar("contentType", 50).nullable()
        val contentMultiplier = integer("contentMultiplier").nullable()
        val packSpecification = varchar("packSpecification", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //AMPPC-Equivalent - don't judge me. The actual explanation also doesn't really make sense. Who uses this stuff?
    object AMPPCES : IntIdTable("AMPPCES") {
        val ctiExtended = varchar("ctiExtended", 9)
        val amppcSequenceNumber = integer("amppcSequenceNumber")
        val sequenceNumber = integer("sequenceNumber")
        val contentQuantity = varchar("contentQuantity", 20).nullable()
        val contentUnit = varchar("contentUnit", 20).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Delivered Medicinal Product Package
    object DMPP : IntIdTable("DMPP") {
        val code = varchar("code", 7)
        val codeType = varchar("codeType", 10)
        val productId = varchar("productId", 100)
        val deliveryEnvironment = varchar("deliveryEnvironment", 1)
        val price = varchar("price", 20).nullable()
        val reimbursable = bool("reimbursable")
        val reimbursementRequiresPriorAgreement = bool("reimbursementRequiresPriorAgreement").nullable()
        val cheapestCeilingPricesStatus5 = bool("cheapestCeilingPricesStatus5").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Supply Problem
    object SPPROB : IntIdTable("SPPROB") {
        val ctiExtended = varchar("ctiExtended", 9)

        val expectedEndDate = date("expectedEndDate").nullable()
        val reportedBy = varchar("reportedBy", 50).nullable()
        val reportedOn = date("reportedOn").nullable()
        val contactName = varchar("contactName", 50).nullable()
        val contactMail = varchar("contactMail", 50).nullable()
        val contactCompany = varchar("contactCompany", 255).nullable()
        val contactPhone = varchar("contactPhone", 50).nullable()
        val reasonNl = varchar("reasonNl", 50).nullable()
        val reasonFr = varchar("reasonFr", 50).nullable()
        val reasonGer = varchar("reasonGer", 50).nullable()
        val reasonEng = varchar("reasonEng", 50).nullable()
        val additionalInformationNl = text("additionalInformationNl").nullable()
        val additionalInformationFr = text("additionalInformationFr").nullable()
        val additionalInformationGer = text("additionalInformationGer").nullable()
        val additionalInformationEng = text("additionalInformationEng").nullable()
        val impactNl = text("impactNl").nullable()
        val impactFr = text("impactFr").nullable()
        val impactGer = text("impactGer").nullable()
        val impactEng = text("impactEng").nullable()
        val limitedAvailability = bool("limitedAvailability").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Commercialization
    object CMRCL : IntIdTable("CMRCL") {
        val ctiExtended = varchar("ctiExtended", 9)
        val endOfCommercializationNl = varchar("endOfCommercializationNl", 50).nullable()
        val endOfCommercializationFr = varchar("endOfCommercializationFr", 50).nullable()
        val endOfCommercializationGer = varchar("endOfCommercializationGer", 50).nullable()
        val endOfCommercializationEng = varchar("endOfCommercializationEng", 50).nullable()
        val reasonEndOfCommercializationNl = varchar("reasonEndOfCommercializationNl", 60).nullable()
        val reasonEndOfCommercializationFr = varchar("reasonEndOfCommercializationFr", 60).nullable()
        val reasonEndOfCommercializationGer = varchar("reasonEndOfCommercializationGer", 60).nullable()
        val reasonEndOfCommercializationEng = varchar("reasonEndOfCommercializationEng", 60).nullable()
        val additionalInformationNl = text("additionalInformationNl").nullable()
        val additionalInformationFr = text("additionalInformationFr").nullable()
        val additionalInformationGer = text("additionalInformationGer").nullable()
        val additionalInformationEng = text("additionalInformationEng").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Derogation Import
    object DRGIMP : IntIdTable("DRGIMP") {
        val ctiExtended = varchar("ctiExtended", 9)
        val sequenceNumber = integer("sequenceNumber")

        val noteNl = text("noteNl").nullable()
        val noteFr = text("noteFr").nullable()
        val noteGer = text("noteGer").nullable()
        val noteEng = text("noteEng").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

}