package db

import db.Chapter4SamTableModel.PARAGRAPH.nullable
import db.Chapter4SamTableModel.VERSE.nullable
import db.VirtualMedicineSamTableModel.VTM.nullable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

/**
 * Chapter IV statements - reimbursement contexts
 */
class Chapter4SamTableModel {

    //Paragraph
    object PARAGRAPH : Table("PARAGRAPH") {
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)

        val keyStringNls = varchar("keyStringNls", 500).nullable()
        val keyStringFr = varchar("keyStringFr", 500).nullable()
        val agreementType = varchar("agreementType", 1).nullable()
        val processType = varchar("processType", 1).nullable()
        val legalReference = varchar("legalReference", 100)
        val publicationDate = date("publicationDate")
        val modificationDate = date("modificationDate")
        val processTypeOverrule = varchar("processTypeOverrule", 10).nullable()
        val agreementTypePro = varchar("agreementTypePro", 1).nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Paragraph Trace
    object PARAGR_TRACE : Table("PARAGR_TRACE") {
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)
        val parentChapterName = varchar("parentChapterName", 10)
        val parentParagraphName = varchar("parentParagraphName", 10)
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Verse
    object VERSE : Table("VERSE") {
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)

        val verseSequence = integer("verseSequence")
        val verseSequenceParent = integer("verseSequenceParent")
        val verseLevel = integer("verseLevel")
        val verseType = varchar("verseType", 2).nullable()
        val checkBoxIndicator = bool("checkBoxIndicator")
        val minCheckNumber = integer("minCheckNumber").nullable()
        val andClauseNumber = integer("andClauseNumber").nullable()
        val textNl = text("textNl")
        val textFr = text("textFr")
        val requestType = varchar("requestType", 1)
        val agreementTerm = integer("agreementTerm").nullable()
        val agreementTermUnit = varchar("agreementTermUnit", 1).nullable()
        val maxPackageNumber = integer("maxPackageNumber").nullable()
        val purchasingAdvisorQual = varchar("purchasingAdvisorQual", 10).nullable()
        val legalReference = varchar("legalReference", 100).nullable()
        val modificationDate = date("modificationDate").nullable()
        val agreementYearMax = integer("agreementYearMax").nullable()
        val agreementRenewalMax = integer("agreementRenewalMax").nullable()
        val sexRestricted = varchar("sexRestricted", 1).nullable()
        val minimumAgeAuthorized = integer("minimumAgeAuthorized").nullable()
        val minimumAgeAuthorizedUnit = varchar("minimumAgeAuthorizedUnit", 5).nullable()
        val maximumAgeAuthorized = integer("maximumAgeAuthorized").nullable()
        val maximumAgeAuthorizedUnit = varchar("maximumAgeAuthorizedUnit", 5).nullable()
        val maximumContentQuantity = integer("maximumContentQuantity").nullable()
        val maximumContentUnit = varchar("maximumContentUnit", 5).nullable()
        val maximumStrengthQuantity = integer("maximumStrengthQuantity").nullable()
        val maximumStrengthUnit = varchar("maximumStrengthUnit", 5).nullable()
        val maximumDurationQuantity = integer("maximumDurationQuantity").nullable()
        val maximumDurationUnit = varchar("maximumDurationUnit", 5).nullable()
        val otherAddedDocument = bool("oherAddedDocument").nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Qualification List
    object QUALLIST : Table("QUALLIST") {
        val qualificationList = varchar("qualificationList", 10)
        val nameId = integer("nameId")
        val exclusiveInd = varchar("exclusiveInd", 1).nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Professional code
    object PROFFESSIONALCODE : Table("PROFESSIONALCODE") {
        val professionalCv = varchar("professionalCv", 10)
        val nameId = integer("nameId")
        val professionalName = varchar("professionalName", 50).nullable()
    }


}