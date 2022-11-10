package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

class VirtualMedicineSamTableModel {

    object VTM : IntIdTable("VTM") {
        val code = integer("code")
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGerman = varchar("nameGer", 255).nullable()
        val nameEnglish = varchar("nameEng", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Virtual Medicinal Product
    object VMP : IntIdTable("VMP") {
        val code = integer("code")
        val vmpGroupCode = integer("vmpGroupCode")
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGerman = varchar("nameGer", 255).nullable()
        val nameEnglish = varchar("nameEng", 255).nullable()

        val abbreviatedNameNl = varchar("abbreviatedNameNl", 255)
        val abbreviatedNameFr = varchar("abbreviatedNameFr", 255)
        val abbreviatedNameGer = varchar("abbreviatedNameGer", 255).nullable()
        val abbreviatedNameEng = varchar("abbreviatedNameEng", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //VMP Component
    object VMPC : IntIdTable("VMPC") {
        val code = integer("code")
        val vmpCode = integer("vmpCode")
        val virtualFormCode = varchar("virtualFormCode", 10)
        val phaseNumber = integer("phaseNumber").nullable()

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGerman = varchar("nameGer", 255).nullable()
        val nameEnglish = varchar("nameEng", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //VMP Group
    object VMPG : IntIdTable("VMPG") {
        val code = integer("code")
        val noswrCode = varchar("noswrCode", 10).nullable()
        val nognprCode = varchar("nognprCode", 10).nullable()

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGerman = varchar("nameGer", 255).nullable()
        val nameEnglish = varchar("nameEng", 255).nullable()

        val patientFrailtyIndicator = bool("patientFrailtyIndicator").nullable()
        val singleAdministrationDose = integer("singleAdministrationDose").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Commented Classification
    object COMCLS : IntIdTable("COMCLS") {
        val code = varchar("code", 10)
        val parent = varchar("parent", 10).nullable()

        val titleNl = varchar("titleNl", 255).nullable()
        val titleFr = varchar("titleFr", 255).nullable()
        val titleGerman = varchar("titleGer", 255).nullable()
        val titleEnglish = varchar("titleEng", 255).nullable()

        val contentNl = text("contentNl").nullable()
        val contentFr = text("contentFr").nullable()
        val contentGerman = text("contentGer").nullable()
        val contentEnglish = text("contentEng").nullable()

        val posologyNoteNl = text("posologyNoteNl").nullable()
        val posologyNoteFr = text("posologyNoteFr").nullable()
        val posologyNoteGerman = text("posologyNoteGer").nullable()
        val posologyNoteEnglish = text("posologyNoteEng").nullable()

        val urlNl = varchar("urlNl", 255).nullable()
        val urlFr = varchar("urlFr", 255).nullable()
        val urlEnglish = varchar("urlEnglish", 255).nullable()
        val urlGerman = varchar("urlGerman", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Virtual Ingredient
    object VTLING : IntIdTable("VTLING") {
        val vmpcCode = integer("vmpcCode")
        val rank = integer("rank")
        val substanceCode = varchar("substanceCode", 10)
        val type = varchar("type", 20)
        val strengthNumeratorMinimum = varchar("strengthNumeratorMinimum", 20).nullable()
        val strengthNumeratorMaximum = varchar("strengthNumeratorMaximum", 20).nullable()
        val strengthNumeratorUnit = varchar("strengthNumeratorUnit", 20).nullable()
        val strengthDenominator = varchar("strengthDenominator", 20).nullable()
        val strengthDenominatorUnit = varchar("strengthDenominatorUnit", 20).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Real Virtual Ingredient
    object RVTLING : IntIdTable("RVTLING") {
        val vmpcCode = integer("vmpcCode")
        val rank = integer("rank")
        val sequenceNumber = integer("sequenceNumber")
        val substanceCode = varchar("substanceCode", 10)
        val type = varchar("type", 20)
        val strengthNumeratorMinimum = varchar("strengthNumeratorMinimum", 20).nullable()
        val strengthNumeratorMaximum = varchar("strengthNumeratorMaximum", 20).nullable()
        val strengthNumeratorUnit = varchar("strengthNumeratorUnit", 20).nullable()
        val strengthDenominator = varchar("strengthDenominator", 20).nullable()
        val strengthDenominatorUnit = varchar("strengthDenominatorUnit", 20).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }
}