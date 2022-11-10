package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

class UnusedModels {

    //Paragraph Trace
    object PARAGR_TRACE : IntIdTable("PARAGR_TRACE") {
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)
        val parentChapterName = varchar("parentChapterName", 10)
        val parentParagraphName = varchar("parentParagraphName", 10)
        val modificationStatus = varchar("modificationStatus", 1)

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
        val kidneyFailureClass = varchar("kidneyFailureClass", 2).nullable()
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
}