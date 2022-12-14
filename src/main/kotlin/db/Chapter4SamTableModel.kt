package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp

/**
 * Chapter IV statements - reimbursement contexts
 */
class Chapter4SamTableModel {

    //Paragraph
    object PARAGRAPH : IntIdTable("PARAGRAPH") {
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)

        val keyStringNls = varchar("keyStringNls", 500).nullable()
        val keyStringFr = varchar("keyStringFr", 500).nullable()
        val agreementType = varchar("agreementType", 1).nullable()
        val processType = varchar("processType", 1).nullable()
        val legalReference = varchar("legalReference", 100).nullable()
        val publicationDate = date("publicationDate").nullable()
        val modificationDate = date("modificationDate").nullable()
        val processTypeOverrule = varchar("processTypeOverrule", 10).nullable()
        val agreementTypePro = varchar("agreementTypePro", 1).nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()

        //Undocumented
        val createdDate = timestamp("createdDate").nullable()
        val createdByUser = varchar("createdByUser", 30).nullable()
    }

    //Verse
    object VERSE : IntIdTable("VERSE") {
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
        val requestType = varchar("requestType", 1).nullable()
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
        val maximumContentQuantity = varchar("maximumContentQuantity", 20).nullable()
        val maximumContentUnit = varchar("maximumContentUnit", 5).nullable()
        val maximumStrengthQuantity = varchar("maximumStrengthQuantity", 20).nullable()
        val maximumStrengthUnit = varchar("maximumStrengthUnit", 5).nullable()
        val maximumDurationQuantity = varchar("maximumDurationQuantity", 20).nullable()
        val maximumDurationUnit = varchar("maximumDurationUnit", 5).nullable()
        val otherAddedDocument = bool("oherAddedDocument").nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()

        //Undocumented
        val createdDate = timestamp("createdDate").nullable()
        val createdByUser = varchar("createdByUser", 30).nullable()
    }

    //Qualification List
    object QUALLIST : IntIdTable("QUALLIST") {
        val qualificationList = varchar("qualificationList", 10)
        val nameId = integer("nameId")
        val exclusiveInd = varchar("exclusiveInd", 1).nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()

        //Undocumented
        val createdDate = timestamp("createdDate").nullable()
        val createdByUser = varchar("createdByUser", 30).nullable()
    }

    //Professional Authorisation
    object PROF_AUTHORISATION : IntIdTable("PROF_AUTHORISATION") {
        val professionalAuthorisationId = integer("professionalAuthorisationId")
        val qualificationList = varchar("qualificationList", 10)
        val professionalCv = varchar("professionalCv", 10).nullable()
        val purchasingAdvisorName = varchar("purchasingAdvisorName", 50).nullable()
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()

        //Undocumented
        val createdDate = timestamp("createdDate").nullable()
        val createdByUser = varchar("createdByUser", 30).nullable()
    }

    //Added document
    object ADDED_DOCUMENT : IntIdTable("ADDED_DOCUMENT") {
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

        //Undocumented
        val createdDate = timestamp("createdDate").nullable()
        val createdByUser = varchar("createdByUser", 30).nullable()
    }

    object EXCLUSION : IntIdTable("EXCLUSION") {
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)
        val exclusionType = varchar("exclusionType", 1).nullable()
        val identifierNum = varchar("identifierNum", 10).nullable()
        val modificationStatus = varchar("modificationStatus", 1).nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()

        //Undocumented
        val createdDate = timestamp("createdDate").nullable()
        val createdByUser = varchar("createdByUser", 30).nullable()
    }

    object NAME_EXPLANATION : IntIdTable("NAME_EXPLANATION") {
        val nameId = integer("nameId")
        val sourceTableId = integer("sourceTableId")
        val modificationStatus = varchar("modificationStatus", 1)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()

        //Undocumented
        val createdDate = timestamp("createdDate").nullable()
        val createdByUser = varchar("createdByUser", 30).nullable()
    }

    object NAME_TRANSLATION : IntIdTable("NAME_TRANSLATION") {
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

        //Undocumented
        val createdDate = timestamp("createdDate").nullable()
        val createdByUser = varchar("createdByUser", 30).nullable()
    }
}