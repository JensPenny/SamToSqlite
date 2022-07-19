package db

import db.ActualMedicineSamTableModel.AMPC_BCPI.nullable
import db.Chapter4SamTableModel.ADDED_DOCUMENT.nullable
import db.Chapter4SamTableModel.EXCLUSION.nullable
import db.Chapter4SamTableModel.NAME_EXPLANATION.nullable
import db.Chapter4SamTableModel.PARAGRAPH.nullable
import db.Chapter4SamTableModel.PROF_AUTHORISATION.nullable
import db.Chapter4SamTableModel.QUALLIST.nullable
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

    //Professional Authorisation
    object PROF_AUTHORISATION : Table("PROF_AUTHIRISATION") {
        val professionalAuthorisationId = integer("professionalAuthorisationId")
        val qualificationList = varchar("qualificationList", 10)
        val professionalCv = varchar("professionalCv", 10).nullable()
        val purchasingAdvisorName = varchar("purchasingAdvisorName", 50).nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Form Type
    object FORM_TYPE : Table("FORM_TABLE") {
        val formTypeId = integer("formTypeId")
        val nameId = integer("nameId")
    }

    //Appendix Type
    object APPENDIX_TYPE : Table("APPENDIX_TYPE") {
        val appendixTypeId = integer("appendixTypeId")
        val nameId = integer("nameId")
    }

    //Added document
    object ADDED_DOCUMENT : Table("ADDED_DOCUMENT") {
        val chapterName = varchar("chapterName", 10)
        val paragrapName = varchar("paragrapName", 10)
        val verseSequence = integer("verseSequence")
        val documentSequence = integer("documentSequence")
        val nameId = integer("nameId")
        val formTypeId = integer("formTypeId")
        val appendixTypeId = integer("appendixTypeId")
        val mimeType = varchar("mimeType", 10).nullable()
        val documentContent = blob("documentContent").nullable()
        val addressURL = varchar("addressURL", 255).nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object EXCLUSION : Table("EXCLUSION") {
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)
        val exclusionType = varchar("exclusionType", 1)
        val identifierNum = varchar("identifierNum", 10)
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object NAME_EXPLANATION : Table("NAME_EXPLANATION") {
        val nameId = integer("nameId")
        val sourceTableId = integer("sourceTableId")
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object NAME_TYPE : Table("NAME_TYPE") {
        val nameTypeCV = varchar("nameTypeCV", 6)
        val nameId = integer("nameId")
        val nameType = varchar("nameType", 50).nullable()
        val nameTypeSequence = integer("nameTypeSequence").nullable()
    }

    object NAME_TRANSLATION : Table("NAME_TRANSLATION") {
        val nameId = integer("nameId")
        val nameTypeCV = varchar("nameTypeCV", 6)
        val languageCv = varchar("languageCv", 2)
        val shortText = varchar("shortText", 300).nullable()
        val longText = text("longText").nullable()
        val longBinaryText = blob("longBinaryText").nullable()
        val addressURL = varchar("addressURL", 255).nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    object LEGAL_REF_TO_PARAGRAPH : Table("LEGAL_REF_TO_PARAGRAPH") {
        val legalReferencePath = varchar("legalReferencePath", 79)
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)
    }
}