package db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

class Tuple {

    //Company Actor Table
    //Sem. key = Actor Number

    //Q: waarom de shit zit dit ook mee in de AMP-tabel? dit zit al in de CPN-tabel..
    object CPN : Table("CPN") {
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
    object AMP : Table("AMP") {
        val code = varchar("code", 12)
        val companyActorNumber = varchar("companyActorNumber", 5)
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
        val abbreviatedNameNl = varchar("abbreviatedNameNl", 255)
        val abbreviatedNameFr = varchar("abbreviatedNameFr", 255)
        val abbreviatedNameGer = varchar("abbreviatedNameGer", 255).nullable()
        val abbreviatedNameEng = varchar("abbreviatedNameEng", 255).nullable()
        val proprietarySuffixNl = varchar("proprietarySuffNl", 255)
        val proprietarySuffixFr = varchar("proprietarySuffFr", 255)
        val proprietarySuffixGer = varchar("proprietarySuffGer", 255).nullable()
        val proprietarySuffixEng = varchar("proprietarySuffEng", 255).nullable()
    }

    //AMP Component
    //Sem. Key: AMP Code + sequence number
    //For. keys:
    //AMP: AMP code
    //VMPC: VMPC code
    object AMPComponent : Table("AMPC") {
        val ampCode = varchar("ampCode", 12)
        val sequenceNumber = integer("sequenceNumber")
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
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
        //Again validfrom and to in docs - why
        val concentration = text("concentration") //Is this actually text?
        val osmoticConcentration = integer("osmoticConcentration")
        val caloricValue = integer("caloricValue")
    }

    object AMPPackage : Table("AMPP") {

    }
}