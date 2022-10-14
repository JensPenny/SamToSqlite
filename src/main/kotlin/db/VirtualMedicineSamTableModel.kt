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

        val titleNl = varchar("titleNl", 255).nullable()
        val titleFr = varchar("titleFr", 255).nullable()
        val titleGerman = varchar("titleGer", 255).nullable()
        val titleEnglish = varchar("titleEng", 255).nullable()

        val contentNl = varchar("contentNl", 255).nullable()
        val contentFr = varchar("contentFr", 255).nullable()
        val contentGerman = varchar("contentGer", 255).nullable()
        val contentEnglish = varchar("contentEng", 255).nullable()

        val posologyNoteNl = varchar("posologyNoteNl", 255).nullable()
        val posologyNoteFr = varchar("posologyNoteFr", 255).nullable()
        val posologyNoteGerman = varchar("posologyNoteGer", 255).nullable()
        val posologyNoteEnglish = varchar("posologyNoteEng", 255).nullable()

        val url = varchar("url", 255).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Virtual Ingredient
    object VTLING : IntIdTable("VTLING") {
        val vmpcCode = integer("vmpcCode")
        val rank = integer("rank")
        val substanceCode = varchar("substanceCode", 10)
        val type = varchar("type", 20)
        val strengthNumeratorMinimum = integer("strengthNumeratorMinimum").nullable()
        val strengthNumeratorUnit = varchar("strengthNumeratorUnit", 20).nullable()
        val strengthDenominator = integer("strengthDenominator").nullable()
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
        val strengthNumeratorMinimum = integer("strengthNumeratorMinimum").nullable()
        val strengthNumeratorMaximum = integer("strengthNumeratorMaximum").nullable()
        val strengthNumeratorUnit = varchar("strengthNumeratorUnit", 20).nullable()
        val strengthDenominator = integer("strengthDenominator").nullable()
        val strengthDenominatorUnit = varchar("strengthDenominatorUnit", 20).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Standard Dosage
    object STDDOS : IntIdTable("STDDOS") {
        val vmpGroupCode = integer("vmpGroupCode")
        val code = integer("code")
        val indicationCode = text("indicationCode")
        val standardDosageParameterBounds = text("standardDosageParameterBounds")
        val targetGroup = varchar("targetGroup", 255)
        val kidnieyFailureClass = varchar("kidnieyFailureClass", 2).nullable()
        val liverFailureClass = varchar("liverFailureClass", 2).nullable()
        val routeOfAdministrationCode = text("routeOfAdministrationCode")
        val routeSpecification = text("routeSpecification").nullable()
        val treatmentDurationType = varchar("treatmentDurationType", 40)
        val temporaryDurationDetailsDurationValue = integer("temporaryDurationDetailsDurationValue").nullable()
        val temporaryDurationDetailsDurationUnit = varchar("temporaryDurationDetailsDurationUnit", 20).nullable()
        val temporaryDurationDetailsUserProvided = bool("temporaryDurationDetailsUserProvided").nullable()

        val temporaryDurationDetailsNoteNl = varchar("temporaryDurationDetailsNoteNl", 255).nullable()
        val temporaryDurationDetailsNoteFr = varchar("temporaryDurationDetailsNoteFr", 255).nullable()
        val temporaryDurationDetailsNoteGerman = varchar("temporaryDurationDetailsNoteGer", 255).nullable()
        val temporaryDurationDetailsNoteEnglish = varchar("temporaryDurationDetailsNoteEng", 255).nullable()

        val dosageUnitQuantityValue = integer("dosageUnitQuantityValue").nullable()
        val dosageUnitQuantityUnit = varchar("dosageUnitQuantityUnit", 20).nullable()
        val dosageUnitMultiplier = integer("dosageUnitMultiplier").nullable()
        val dosageParameterName = varchar("dosageParameterName", 255).nullable()
        val administrationFrequencyQuantity = integer("administrationFrequencyQuantity").nullable()
        val administrationFrequencyTimeframeValue = integer("administrationFrequencyTimeframeValue").nullable()
        val administrationFrequencyTimeframeUnit = varchar("administrationFrequencyTimeframeUnit", 20).nullable()
        val maximumDailyQuantityValue = integer("maximumDailyQuantityValue").nullable()
        val maximumDailyQuantityUnit = varchar("maximumDailyQuantityUnit", 20).nullable()
        val maximumDailyQuantityMultiplier = integer("maximumDailyQuantityMultiplier").nullable()
        val maximumDailyQuantityParameter = varchar("maximumDailyQuantityParameter", 255).nullable()

        val textualDosageNl = varchar("textualDosageNl", 255).nullable()
        val textualDosageFr = varchar("textualDosageFr", 255).nullable()
        val textualDosageGerman = varchar("textualDosageGer", 255).nullable()
        val textualDosageEnglish = varchar("textualDosageEng", 255).nullable()

        val supplementaryInfoNl = varchar("supplementaryInfoNl", 255).nullable()
        val supplementaryInfoFr = varchar("supplementaryInfoFr", 255).nullable()
        val supplementaryInfoGerman = varchar("supplementaryInfoGer", 255).nullable()
        val supplementaryInfoEnglish = varchar("supplementaryInfoEng", 255).nullable()
    }

    //Virtual Form
    object VTFRM : IntIdTable("VTFRM") {
        val code = varchar("code", 10)
        val abbreviatedNl = varchar("abbreviatedNl", 255)
        val abbreviatedFr = varchar("abbreviatedFr", 255)
        val abbreviatedGer = varchar("abbreviatedGer", 255).nullable()
        val abbreviatedEng = varchar("abbreviatedEng", 255).nullable()

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //World Anti-doping Agency
    object WADA : IntIdTable("WADA") {
        val code = varchar("code", 10)

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //No switch Reason
    object NOSWR : IntIdTable("NOSWR") {
        val code = varchar("code", 10)

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //No generic prescription reason
    object NOGNPR : IntIdTable("NOGNPR") {
        val code = varchar("code", 10)

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //Standard Form
    object STDFRM : IntIdTable("STDFRM") {
        val standard = varchar("standard", 20)
        val code = varchar("code", 20)

        val nameNl = varchar("nameNl", 255).nullable()
        val nameFr = varchar("nameFr", 255).nullable()
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val definitionNl = varchar("definitionNl", 255).nullable()
        val definitionFr = varchar("definitionFr", 255).nullable()
        val definitionGer = varchar("definitionGer", 255).nullable()
        val definitionEng = varchar("definitionEng", 255).nullable()

        val url = varchar("url", 255).nullable()
    }

    //Route of administration
    object ROA : IntIdTable("ROA") {
        val code = varchar("code", 10)

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
    }

    //Standard Route Of Administration
    object STDROA : IntIdTable("STDROA") {
        val standard = varchar("standard", 20)
        val code = varchar("code", 20)

        val nameNl = varchar("nameNl", 255).nullable()
        val nameFr = varchar("nameFr", 255).nullable()
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val definitionNl = varchar("definitionNl", 255).nullable()
        val definitionFr = varchar("definitionFr", 255).nullable()
        val definitionGer = varchar("definitionGer", 255).nullable()
        val definitionEng = varchar("definitionEng", 255).nullable()

        val url = varchar("url", 255).nullable()
    }

    //Substance
    object SBST : IntIdTable("SBST") {
        val code = varchar("code", 10)
        val chemicalForm = varchar("chemical", 10)

        val nameNl = varchar("nameNl", 255).nullable()
        val nameFr = varchar("nameFr", 255).nullable()
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val noteNl = text("noteNl").nullable()
        val noteFr = text("noteFr").nullable()
        val noteGer = text("noteGer").nullable()
        val noteEng = text("noteEng").nullable()
    }

    //Standard Substance
    object STDSBST : IntIdTable("STDSBST") {
        val standard = varchar("standard", 20)
        val code = varchar("code", 20)

        val nameNl = varchar("nameNl", 255).nullable()
        val nameFr = varchar("nameFr", 255).nullable()
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val definitionNl = varchar("definitionNl", 255).nullable()
        val definitionFr = varchar("definitionFr", 255).nullable()
        val definitionGer = varchar("definitionGer", 255).nullable()
        val definitionEng = varchar("definitionEng", 255).nullable()

        val url = varchar("url", 255).nullable()
    }
}