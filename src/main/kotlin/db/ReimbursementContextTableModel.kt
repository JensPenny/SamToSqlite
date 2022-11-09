package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

class ReimbursementContextTableModel {
    //Reimbursement Context
    object RMBCTX : IntIdTable("RMBCTX") {
        val code = varchar("code", 7)
        val codeType = varchar("codeType", 10)
        val deliveryEnvironment = varchar("deliveryEnvironment", 1)
        val legalReferencePath = varchar("legalReferencePath", 79)
        val reimbursementCriterionCategory = varchar("reimbursementCriterionCategory", 10)
        val reimbursementCriterionCode = varchar("reimbursementCriterionCode", 10)
        val multiple = varchar("multiple", 1).nullable()
        val temporary = bool("temporary")
        val reference = bool("reference")
        val flatRateSystem = bool("flatRateSystem")
        val reimbursementBasePrice = varchar("reimbursementBasePrice", 20)
        val referenceBasePrice = varchar("referenceBasePrice", 20)
        val copaymentSupplement = varchar("copaymentSupplement", 20).nullable()
        val pricingUnitQuantity = varchar("pricingUnitQuantity", 20)

        val pricingUnitLabelNl = varchar("pricingUnitLabelNl", 255).nullable()
        val pricingUnitLabelFr = varchar("pricingUnitLabelFr", 255).nullable()
        val pricingUnitLabelGerman = varchar("pricingUnitLabelGer", 255).nullable()
        val pricingUnitLabelEnglish = varchar("pricingUnitLabelEng", 255).nullable()

        val pricingSliceQuantity = varchar("pricingSliceQuantity", 20).nullable()

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
        val feeAmount = varchar("feeAmount", 20)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }
}