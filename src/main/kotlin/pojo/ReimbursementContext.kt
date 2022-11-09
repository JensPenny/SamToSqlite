package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

//Top level
@JsonRootName("ns4:ReimbursementContext")
data class ReimbursementContext (
    @set:JsonProperty("deliveryEnvironment")
    var deliveryEnvironment: String,

    @set:JsonProperty("code")
    var code: String,

    @set:JsonProperty("codeType")
    var codeType: String,

    @set:JsonProperty("legalReferencePath")
    var legalReferencePath: String,

    @set:JsonAlias("ContextData", "ns4:Data")
    var contextDatas: ArrayList<ContextData> = ArrayList(),

    @set:JsonAlias("Copayment", "ns4:Copayment")
    var copayments: ArrayList<Copayment> = ArrayList()
)

@JsonRootName("ns4:Copayment")
data class Copayment(
    @set:JsonProperty("regimeType")
    var regimeType: String,

    @set:JsonAlias("CopaymentData", "ns4:Data")
    var copaymentData: ArrayList<CopaymentData> = ArrayList(),
)

data class CopaymentData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("FeeAmount")
    var feeAmount: String,
)

@JsonRootName("ns4:Data")
data class ContextData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("Multiple")
    var multiple: String?,

    @set:JsonProperty("Temporary")
    var temporary: Boolean,

    @set:JsonProperty("Reference")
    var reference: Boolean,

    @set:JsonProperty("FlatRateSystem")
    var flatRateSystem: Boolean,

    @set:JsonProperty("ReimbursementBasePrice")
    var reimbursementBasePrice: String,

    @set:JsonProperty("ReferenceBasePrice")
    var referenceBasePrice: String,

    @set:JsonAlias("Pricing", "PricingUnit")
    var pricingUnit: Pricing,

    @set:JsonAlias("Pricing", "PricingSlice")
    var pricingSlice: Pricing?,

    @set:JsonProperty("CopaymentSupplement")
    var copaymentSupplement: String?,

    @set:JsonProperty("CollegeForOrphanDrugs")
    var collegeForOrphanDrugs: Boolean?,

    @set:JsonProperty("ChargedAt85Percent")
    var chargedAt85Perc: Boolean?,

    @set:JsonAlias("ReimbursementCriterion", "ns4:ReimbursementCriterion")
    var reimbursementCriterion: ReimbursementCriterionReference,
)

data class Pricing(
    @set:JsonProperty("Quantity")
    var quantity: String,

    @set:JsonAlias("TranslatedData", "Label")
    var label: TranslatedData?,
)

@JsonRootName("ns4:ReimbursementCriterion")
data class ReimbursementCriterionReference(
    @set:JsonProperty("category")
    var category: String,

    @set:JsonProperty("code")
    var code: String,
)