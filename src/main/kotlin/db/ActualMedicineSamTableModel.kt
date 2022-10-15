package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

/**
 * Collection of Samv2 tables in their pure form.
 * Don't blame me for their content, because it's really really dumb the way
 * this stuff is subdivided
 */
class ActualMedicineSamTableModel {

    //Company Actor Table
    //Sem. key = Actor Number

    //Q: waarom de shit zit dit ook mee in de AMP-tabel? dit zit al in de CPN-tabel..
    object CPN : IntIdTable("CPN") {
        val actorNumber = varchar("actorNumber", 5) //Number(5) - varchar
        val authorisationNumber = varchar("authorisationNumber", 50).nullable()
        val vatCountryCode = varchar("vatCountryCode", 2).nullable()
        val vatNumber = varchar("vatNumber", 15).nullable()
        val europeanNumber = varchar("europeanNumber", 40).nullable()
        val denomination = varchar("denomination", 255)
        val legalForm = varchar("legalForm", 30).nullable()
        val building = varchar("building", 50).nullable()
        val streetName = varchar("streetName", 50).nullable()
        val streetNum = varchar("streetNum", 10).nullable()
        val postbox = varchar("postbox", 50).nullable()
        val postcode = varchar("postcode", 10).nullable()
        val city = varchar("city", 50).nullable()
        val countryCode = varchar("countryCode", 2).nullable()
        val phone = varchar("phone", 30).nullable()
        val language = varchar("language", 5)
        val website = varchar("website", 255).nullable()
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

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

/*      rare shit in de docs - prescription en prescriptiontranslations?
        val prescriptionNameNl = text("prescriptionNameNl")
        val prescriptionNameFr = text("prescriptionNameFr")
        val prescriptionNameGer = text("prescriptionNameGer").nullable()
        val prescriptionNameEng = text("prescriptionNameEng").nullable()
*/
    }

    object AMP_BCPI : IntIdTable("AMP_BCPI") {
        val code = varchar("code", 12)
        val abbreviatedNameNl = varchar("abbreviatedNameNl", 255)
        val abbreviatedNameFr = varchar("abbreviatedNameFr", 255)
        val abbreviatedNameGer = varchar("abbreviatedNameGer", 255).nullable()
        val abbreviatedNameEng = varchar("abbreviatedNameEng", 255).nullable()
        val proprietarySuffixNl = varchar("proprietarySuffNl", 255)
        val proprietarySuffixFr = varchar("proprietarySuffFr", 255)
        val proprietarySuffixGer = varchar("proprietarySuffGer", 255).nullable()
        val proprietarySuffixEng = varchar("proprietarySuffEng", 255).nullable()
        val prescriptionNameNl = text("prescriptionNameNl")
        val prescriptionNameFr = text("prescriptionNameFr")
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
        val sugarFree = varchar("sugarFree", 1).nullable()
        val modifiedReleaseType = varchar("modifiedReleaseType", 2).nullable()
        val specificDrugDevice = varchar("specificDrugDevice", 2).nullable()
        val dimensions = varchar("dimensions", 50).nullable()
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
        val noteNl = text("noteNl")
        val noteFr = text("noteFr")
        val noteGer = text("noteGer").nullable()
        val noteEng = text("noteEng").nullable()
        val concentration = text("concentration") //Is this actually text?
        val osmoticConcentration = integer("osmoticConcentration")
        val caloricValue = integer("caloricValue")

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Pharmaceutical Form
    object PHFRM : IntIdTable("PHFRM") {
        val code = varchar("code", 10)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGerman = varchar("nameGer", 255).nullable()
        val nameEnglish = varchar("nameEng", 255).nullable()
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
        val packAmount = integer("packAmount").nullable()
        val packAmountUnit = varchar("packAmountUnit", 20).nullable()
        val packDisplayValue = varchar("packDisplayValue", 255).nullable()
        val gtin = varchar("gtin", 20).nullable()
        val status = varchar("status", 10).nullable()
        val fmdProductCode = text("fmdProductCode").nullable() //Is an array
        val fmdInScope = bool("fmdInScope").nullable()
        val antiTamperingDevicePresent =
            bool("antiTamperingDevicePresent").nullable() //anti tempering in docs, but is wrong

        val prescriptionNameNl = text("prescriptionNameNl")
        val prescriptionNameFr = text("prescriptionNameFr")
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

        val abbreviatedNameNl = varchar("abbreviatedNameNl", 255)
        val abbreviatedNameFr = varchar("abbreviatedNameFr", 255)
        val abbreviatedNameGer = varchar("abbreviatedNameGer", 255).nullable()
        val abbreviatedNameEng = varchar("abbreviatedNameEng", 255).nullable()

        val noteNl = varchar("noteNl", 255).nullable()
        val noteFr = varchar("noteFr", 255).nullable()
        val noteGer = varchar("noteGer", 255).nullable()
        val noteEng = varchar("noteEng", 255).nullable()

        val posologyNoteNl = varchar("posologyNoteNl", 255).nullable()
        val posologyNoteFr = varchar("posologyNoteFr", 255).nullable()
        val posologyNoteGer = varchar("posologyNoteGer", 255).nullable()
        val posologyNoteEng = varchar("posologyNoteEng", 255).nullable()

        val crmLinkNl = varchar("crmLinkNl", 255).nullable()
        val crmLinkFr = varchar("crmLinkFr", 255).nullable()
        val crmLinkGer = varchar("crmLinkGer", 255).nullable()
        val crmLinkEng = varchar("crmLinkEng", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object AMPP_NIHDI : IntIdTable("AMPP_NIHDI") {
        val ctiExtended = varchar("ctiExtended", 9)
        val exfactory_price = integer("exfactory_price")
        val reimbursementCode = varchar("reimbursementCode", 10).nullable()
        val cheap = bool("cheap").nullable()
        val cheapest = bool("cheapest").nullable()
        val index = integer("index").nullable()
        val bigPackage = bool("bigPackage").nullable()
        val contraceptive = bool("contraceptive").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object AMPP_NIHDI_BIS : IntIdTable("AMPP_NIHDI_BIS") {
        val ctiExtended = varchar("ctiExtended", 9)
        val definedDailyDose = integer("definedDailyDose").nullable()
        val definedDailyDoseUnit = varchar("definedDailyDoseUnit", 20).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object AMPP_ECON : IntIdTable("AMPP_ECON") {
        val ctiExtended = varchar("ctiExtended", 9)

        val officialExFactoryPrice = integer("officialExFactoryPrice")
        val realExFactoryPrice = integer("realExFactoryPrice").nullable()
        val decisionDate = date("decisionDate")

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
        val reasonEndOfCommercializationNl = varchar("reasonEndOfCommercializationNl", 50).nullable()
        val reasonEndOfCommercializationFr = varchar("reasonEndOfCommercializationFr", 50).nullable()
        val reasonEndOfCommercializationGer = varchar("reasonEndOfCommercializationGer", 50).nullable()
        val reasonEndOfCommercializationEng = varchar("reasonEndOfCommercializationEng", 50).nullable()
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

        val noteNl = text("noteNl")
        val noteFr = text("noteFr")
        val noteGer = text("noteGer").nullable()
        val noteEng = text("noteEng").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //ATC Code
    object ATC : IntIdTable("ATC") {
        val code = varchar("code", 7)
        val description = text("description")
    }

    //Delivery mode
    object DLVM : IntIdTable("DLVM") {
        val code = varchar("code", 7)

        val descriptionNameNl = text("descriptionNameNl")
        val descriptionNameFr = text("descriptionNameFr")
        val descriptionNameGer = text("descriptionNameGer").nullable()
        val descriptionNameEng = text("descriptionNameEng").nullable()
    }

    //Delivery Mode Specification
    object DLVMS : IntIdTable("DLVMS") {
        val code = varchar("code", 7)

        val descriptionNameNl = text("descriptionNameNl")
        val descriptionNameFr = text("descriptionNameFr")
        val descriptionNameGer = text("descriptionNameGer").nullable()
        val descriptionNameEng = text("descriptionNameEng").nullable()
    }

    //AMPP Component
    object AMPPC : IntIdTable("AMPPC") {
        val ctiExtended = varchar("ctiExtended", 9)
        val sequenceNumber = integer("sequenceNumber")
        val ampcSequenceNumber = integer("ampcSequenceNumber").nullable()
        val deviceTypeCode = varchar("deviceTypeCode", 8).nullable()
        val packagingTypeCode = varchar("packagingTypeCode", 8).nullable()
        val contentType = varchar("contentType", 50)
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
        val contentQuantity = integer("contentQuantity")
        val contentUnit = varchar("contentUnit", 20)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Device Type
    object DVCTP : IntIdTable("DVCTP") {
        val code = varchar("code", 8)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
        val edqmCode = varchar("edqmCode", 20).nullable()
        val edqmDefinition = text("edqmDefinition").nullable()
    }

    //Packaging Closure
    object PCKCL : IntIdTable("PCKCL") {
        val code = varchar("code", 8)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
        val edqmCode = varchar("edqmCode", 20).nullable()
        val edqmDefinition = text("edqmDefinition").nullable()
    }

    //Packaging Material
    object PCKMT : IntIdTable("PCKMT") {
        val code = varchar("code", 8)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
    }

    //Packaging type
    object PCKTP : IntIdTable("PCKTP") {
        val code = varchar("code", 8)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
        val edqmCode = varchar("edqmCode", 20).nullable()
        val edqmDefinition = text("edqmDefinition").nullable()
    }

    //Delivered Medicinal Product Package
    object DMPP : IntIdTable("DMPP") {
        val code = varchar("code", 7)
        val codeType = varchar("codeType", 10)
        val deliveryEnvironment = varchar("deliveryEnvironment", 1)
        val price = integer("price").nullable()
        val reimbursable = bool("reimbursable")
        val reimbursementRequiresPriorAgreement = bool("reimbursementRequiresPriorAgreement").nullable()
        val cheapestCeilingPricesStatus5 = bool("cheapestCeilingPricesStatus5").nullable()

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
        val strengthQuantity = integer("strengthQuantity").nullable()
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
        val type = varchar("type", 20)
        val substanceCode = varchar("substanceCode", 10)
        val knownEffect = bool("knownEffect").nullable()
        val strengthQuantity = integer("strengthQuantity").nullable()
        val strengthUnit = varchar("strengthUnit", 20).nullable()
        val strengthDescription = varchar("strengthDescription", 50).nullable()
        val additionalInformation = varchar("additionalInformation", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }
}