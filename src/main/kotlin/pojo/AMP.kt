package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

/**
 * Temp object to hold the fields that can persist into object ActualMedicineSamTableModel.AMP_FAMHP
 */
@JsonRootName("ns4:Amp")
data class AmpElement(
    @set:JsonProperty("vmpCode")
    var vmpCode: String?, //Optional link to vmp - foreign key

    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("AmpData", "ns4:Data")
    var dataBlocks: ArrayList<AmpData> = ArrayList(),

    @set:JsonAlias("AMPComponentElement", "ns4:AmpComponent")
    var ampComponents: List<AMPComponentElement> = ArrayList(),

    @set:JsonAlias("AMPPElement", "ns4:Ampp")
    var amppElements: List<AMPPElement> = ArrayList(),
)

@JsonRootName("ns4:Data")
data class AmpData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("OfficialName")
    var officialName: String,

    @set:JsonProperty("Status")
    var status: String,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData = TranslatedData(),

    @set:JsonProperty("BlackTriangle")
    var blackTriangle: Boolean = false,

    @set:JsonProperty("MedicineType")
    var medicineType: String,

    @set:JsonAlias("AmpCompany", "ns4:Company") //Required
    var company: AmpCompany,

    @set:JsonAlias("TranslatedData", "PrescriptionNameFamhp")
    var prescriptionNameFamhp: TranslatedData?,

    @set:JsonAlias("TranslatedData", "AbbreviatedName")
    var abbreviatedName: TranslatedData?,

    @set:JsonAlias("TranslatedData", "ProprietarySuffix")
    var proprietarySuffix: TranslatedData?,

    @set:JsonAlias("TranslatedData", "PrescriptionName")
    var prescriptionName: TranslatedData?,
)

@JsonRootName("Ampp")
data class AMPPElement(
    @set:JsonProperty("ctiExtended")
    var ctiExtended: String,

    @set:JsonAlias("AMPPData", "ns4:Data")
    var amppDataBlocks: List<AMPPData> = ArrayList(),

    @set:JsonAlias("AmppComponent", "ns4:AmppComponent")
    var amppComponents: List<AmppComponent> = ArrayList(),

    @set:JsonAlias("Commercialization", "ns4:Commercialization")
    var commercialization: List<Commercialization> = ArrayList(),

    @set:JsonAlias("SupplyProblem", "ns4:SupplyProblem")
    var supplyProblems: List<SupplyProblem> = ArrayList(),

    @set:JsonAlias("Dmpp", "ns4:Dmpp")
    var dmpps: List<Dmpp> = ArrayList(),
)

@JsonRootName("ns4:SupplyProblem")
data class SupplyProblem(
    @set:JsonAlias("SupplyProblemData", "ns4:Data")
    var supplyDataBlocks: List<SupplyProblemData> = ArrayList(),
)

@JsonRootName("ns4:Data")
data class SupplyProblemData(

    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("ExpectedEndOn")
    var expectedEnd: String?,

    @set:JsonProperty("ReportedBy")
    var reportedBy: String?,

    @set:JsonAlias("TranslatedData", "Reason")
    var reason: TranslatedData?,

    @set:JsonAlias("TranslatedData", "AdditionalInformation")
    var additionalInformation: TranslatedData?,
)

@JsonRootName("ns4:Dmpp")
data class Dmpp(

    @set:JsonProperty("ProductId")
    var productId: String,

    @set:JsonProperty("deliveryEnvironment")
    var deliveryEnvironment: String,

    @set:JsonProperty("code")
    var code: String?,

    @set:JsonProperty("codeType")
    var codeSystem: String?,

    @set:JsonAlias("DmppData", "ns4:Data")
    var dmppDataBlocks: List<DmppData> = ArrayList()
)

@JsonRootName("ns4:Data")
data class DmppData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("Price")
    var price: String?,

    @set:JsonProperty("Reimbursable")
    var reimbursable: Boolean = false,  //todo check if might be string

    @set:JsonProperty("Cheap")
    var cheap: Boolean = false,

    @set:JsonProperty("Cheapest")
    var cheapest: Boolean = false,
)

@JsonRootName("ns4:Commercialization")
data class Commercialization(
    @set:JsonAlias("CommercializationData", "ns4:Data")
    var commercializationDataBlocks: List<CommercializationData> = ArrayList(),
)

@JsonRootName("ns4:Data")
data class CommercializationData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonAlias("TranslatedData", "EndOfCommercialization")
    var endOfCommercialization: TranslatedData?,

    @set:JsonAlias("TranslatedData", "Reason")
    var reason: TranslatedData?,

    @set:JsonAlias("TranslatedData", "Impact")
    var impact: TranslatedData?,
)

@JsonRootName("ns4:AmppComponent")
data class AmppComponent(
    @set:JsonProperty("sequenceNr")
    var sequenceNumber: String,

    @set:JsonAlias("AmppComponentData", "ns4:Data")
    var amppComponentDataBlocks: List<AmppComponentData> = ArrayList(),

    @set:JsonAlias("AmppComponentEquivalent", "ns4:AmppComponentEquivalent")
    var ampComponentEquivalents: List<AmppComponentEquivalent> = ArrayList()
)

@JsonRootName("ns4:AmppComponentEquivalent")
data class AmppComponentEquivalent(
    @set:JsonProperty("sequenceNr")
    var sequenceNumber: String,

    @set:JsonAlias("AmppComponentEquivalentData", "ns4:Data")
    var amppComponentEquivalentDataBlocks: List<AmppComponentEquivalentData> = ArrayList(),
)

@JsonRootName("ns4:Data")
data class AmppComponentEquivalentData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonAlias("ContentElement", "Content")
        var content: ContentElement?
)

@JsonRootName("ns4:Data")
data class AmppComponentData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("AmpcSequenceNr")
    var ampcSequenceNr: String?,

    @set:JsonProperty("ContentType")
    var contentType: String?,

    @set:JsonAlias("CodeReference", "ns4:PackagingType")
    var packagingType: CodeReference,
)

@JsonRootName("ns4:Data")
data class AMPPData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("AuthorisationNr")
    var authorisationNr: String,

    @set:JsonProperty("Orphan")
    var orphan: Boolean,

    @set:JsonAlias("TranslatedData", "LeafletLink")
    var leafletLink: TranslatedData?,

    @set:JsonAlias("TranslatedData", "SpcLink")
    var spcLink: TranslatedData?,

    @set:JsonAlias("TranslatedData", "RmaPatientLink")
    var rmaPatientLink: TranslatedData?,

    @set:JsonAlias("TranslatedData", "RmaProfessionalLink")
    var rmaProfessionalLink: TranslatedData?,

    @set:JsonProperty("ParallelCircuit")
    var parallelCircuit: String?,

    @set:JsonProperty("PackMultiplier")
    var packMultiplier: String?,

    @set:JsonAlias("PackAmount", "PackAmount")
    var packAmount: PackAmount?,

    @set:JsonProperty("PackDisplayValue")
    var packDisplayValue: String?,

    @set:JsonProperty("Status")
    var status: String?,

    @set:JsonAlias("TranslatedData", "PrescriptionNameFamhp")
    var prescriptionNameFamhp: TranslatedData?,

    @set:JsonProperty("FMDProductCode")
    var fmdProductCode: String?,

    @set:JsonProperty("FMDInScope")
    var fmdInScope: Boolean? = false,

    @set:JsonProperty("SingleUse")
    var singleUse: Boolean?,

    @set:JsonProperty("SpeciallyRegulated")
    var speciallyRegulated:String?,

    @set:JsonProperty("AntiTemperingDevicePresent")
    var antiTamperingDevicePresent: Boolean? = false,

    @set:JsonAlias("CodeReference", "ns4:Atc")
    var atcCodeReference: List<CodeReference> = ArrayList(),

    @set:JsonAlias("CodeReference", "ns4:DeliveryModus")
    var deliveryModusReference: CodeReference,

    @set:JsonAlias("CodeReference", "ns4:DeliveryModusSpecification")
    var deliveryModusSpecReference: CodeReference?,

    @set:JsonAlias("TranslatedData", "AbbreviatedName")
    var abbreviatedName: TranslatedData?,

    @set:JsonAlias("TranslatedData", "PrescriptionName")
    var prescriptionName: TranslatedData?,

    @set:JsonAlias("TranslatedData", "CrmLink")
    var crmLink: TranslatedData?,

    @set:JsonAlias("TranslatedData", "Note")
    var note: TranslatedData?,

    @set:JsonAlias("TranslatedData", "PosologyNote")
    var posologyNote: TranslatedData?,

    @set:JsonProperty("ExFactoryPrice")
    var exFactoryPrice: String?,

    @set:JsonProperty("ReimbursementCode")
    var reimbursementCode: String?,

    @set:JsonProperty("BigPackage")
    var bigPackage: Boolean?,

    @set:JsonProperty("Index")
    var index: String?,

    @set:JsonProperty("OfficialExFactoryPrice")
    var officialExFactoryPrice: String?,

    @set:JsonProperty("RealExFactoryPrice")
    var realExFactoryPrice: String?,

    @set:JsonProperty("PricingInformationDecisionDate")
    var pricingDecisionDate: String?,
)

//We just grab the actornr from this file. The rest is encompassed in the company-file
@JsonRootName("ns4:Company")
data class AmpCompany(
    @set:JsonProperty("actorNr")
    var actorNr: String
)

@JsonRootName("ns4:AmpComponent")
data class AMPComponentElement(

    @set:JsonProperty("sequenceNr")
    var sequenceNumber: String,

    @set:JsonProperty("vmpComponentCode")
    var vmpComponentCode: String? = null,

    @set:JsonAlias("AmpComponentData", "ns4:Data")
    var dataBlocks: ArrayList<AmpComponentData> = ArrayList(),

    @set:JsonAlias("RealActualIngredient", "ns4:RealActualIngredient")
    var ingredients: ArrayList<RealActualIngredient> = ArrayList(),
)

@JsonRootName("ns4:Data")
data class AmpComponentData(

    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonAlias("CodeReference", "ns4:PharmaceuticalForm")
    var pharmaceuticalFormReferences: List<CodeReference>,

    @set:JsonAlias("CodeReference", "ns4:RouteOfAdministration")
    var routesOfAdministration: List<CodeReference>,

    @set:JsonProperty("Dividable")
    var dividable: String?,

    @set:JsonProperty("ContainsAlcohol")
    var containsAlcohol: String?,

    @set:JsonProperty("SugarFree")
    var sugarFree: Boolean,

    @set:JsonProperty("Scored")
    var scored: String?,

    @set:JsonProperty("SpecificDrugDevice")
    var specificDrugDevice: String?,

    @set:JsonProperty("Dimensions")
    var dimensions: String?,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData = TranslatedData(),
)

@JsonRootName("ns4:RealActualIngredient")
data class RealActualIngredient(
    @set:JsonProperty("rank")
    var rank: String,

    @set:JsonAlias("AmpComponentData", "ns4:Data")
    var dataBlocks: ArrayList<RealActualIngredientData> = ArrayList(),
)

@JsonRootName("ns4:Data")
data class RealActualIngredientData(
    @set:JsonProperty("from")
    var from: String? = null,
    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("Type")
    var type: String? = null,

    @set:JsonAlias("IngredientStrength", "Strength")
    var strength: IngredientStrength?,

    @set:JsonAlias("CodeReference", "ns4:Substance")
    var substanceCode: CodeReference,

    /*    var ampCode: String? = null,
        var sequenceNumber: String? = null,
        var rank: String? = null,
        var knownEffect: String? = null,
        var strengthDescription: String? = null,
        var additionalInformation: String? = null,*/
)

@JsonRootName("Strength")
data class IngredientStrength(
    @set:JsonProperty("unit")
    var unit: String?,

    @set:JacksonXmlText
    var Strength: String?,  //I don't really get why the name must be exact here, but worthy of a post later
)

@JsonRootName("Content")
data class ContentElement(
    @set:JsonProperty("unit")
    var unit: String?,

    @set:JacksonXmlText
    var Content: String?,
)

@JsonRootName("PackAmount")
data class PackAmount(
    @set:JsonProperty("unit")
    var unit: String?,

    @set:JacksonXmlText
    var PackAmount: String?,
)

