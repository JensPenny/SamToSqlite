package db

import db.ReimbursementLawSamTableModel.LGLBAS.nullable
import db.ReimbursementLawSamTableModel.LGLTXT.nullable
import db.ReimbursementLawSamTableModel.RMBCTX.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

/**
 * Deprecated tables. See Chapter IV statements
 */
class ReimbursementLawSamTableModel {

    //Legal basis 
    object LGLBAS : IntIdTable("LGLBAS") {
        val key = varchar("key", 15)

        val titleNl = varchar("titleNl", 255).nullable()
        val titleFr = varchar("titleFr", 255).nullable()
        val titleGerman = varchar("titleGer", 255).nullable()
        val titleEnglish = varchar("titleEng", 255).nullable()

        val type = varchar("type", 30)
        val effectiveOn = date("effectiveOn").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Legal Text
    object LGLTXT : IntIdTable("LGLTXT") {
        val legalTextPath = varchar("legalTextPath", 175)
        val legalReferencePath = varchar("legalReferencePath", 79).nullable()
        val type = varchar("type", 30)

        val contentNl = text("contentNl").nullable()
        val contentFr = text("contentFr").nullable()
        val contentGerman = text("contentGer").nullable()
        val contentEnglish = text("contentEng").nullable()

        val sequenceNumber = integer("sequenceNumber")
        val lastModifiedOn = date("lastModifiedOn").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Reimbursement Context
    object RMBCTX : IntIdTable("RMBCTX") {
        val code = varchar("code", 7)
        val codeType = varchar("codeType", 10)
        val deliveryEnvironment = varchar("deliveryEnvironment", 1)
        val legalReferencePath = varchar("legalReferencePath", 79)
        val reimbursementCriterionCategory = varchar("reimbursementCriterionCategory", 10)
        val multiple = varchar("multiple", 1).nullable()
        val temporary = bool("temporary")
        val reference = bool("reference")
        val flatRateSystem = bool("flatRateSystem")
        val reimbursementBasePrice = integer("reimbursementBasePrice")
        val referenceBasePrice = integer("referenceBasePrice")
        val copaymentSupplement = integer("copaymentSupplement").nullable()
        val pricingUnitQuantity = integer("pricingUnitQuantity")

        val pricingUnitLabelNl = varchar("pricingUnitLabelNl", 255).nullable()
        val pricingUnitLabelFr = varchar("pricingUnitLabelFr", 255).nullable()
        val pricingUnitLabelGerman = varchar("pricingUnitLabelGer", 255).nullable()
        val pricingUnitLabelEnglish = varchar("pricingUnitLabelEng", 255).nullable()

        val pricingSliceQuantity = integer("pricingSliceQuantity").nullable()

        val pricingSliceLabelNl = varchar("pricingSliceLabelNl", 255).nullable()
        val pricingSliceLabelFr = varchar("pricingSliceLabelFr", 255).nullable()
        val pricingSliceLabelGerman = varchar("pricingSliceLabelGer", 255).nullable()
        val pricingSliceLabelEnglish = varchar("pricingSliceLabelEng", 255).nullable()

        val collegeForOrphanDrugs = bool("collegeForOrphanDrugs").nullable()
        val facturation85Perc = bool("85PercFacturation").nullable()
        val exceptional = bool("exceptional").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Copayment
    object COPAY : IntIdTable("COPAY") {
        val code = varchar("code", 7)
        val codeType = varchar("codeType", 10)
        val deliveryEnvironment = varchar("deliveryEnvironment", 1)
        val legalReferencePath = varchar("legalReferencePath", 79)
        val regimeType = varchar("regimeType", 1)
        val feeAmount = integer("feeAmount")

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }
}