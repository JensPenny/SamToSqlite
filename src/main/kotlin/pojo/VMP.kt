package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

//Top level element
@JsonRootName("ns3:Vtm")
data class VtmElement (
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("VtmData", "ns3:Data")
    var dataBlocks: ArrayList<VtmData> = ArrayList()
)

@JsonRootName("ns3:Data")
data class VtmData (
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Name")
    var name: TranslatedDataNoNS,
)

//Top level element
@JsonRootName("ns3:VmpGroup")
data class VmpGroupElement(
    @set:JsonProperty("ProductId")
    var productId: String,

    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("VmpGroupData", "ns3:Data")
    var dataBlocks: ArrayList<VmpGroupData> = ArrayList()
)

@JsonRootName("ns3:Data")
data class VmpGroupData (
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Name")
    var name: TranslatedDataNoNS,

    @set:JsonAlias("CodeReference", "ns3:NoGenericPrescriptionReason")
    var noGenericPrescriptionReason: CodeReference?,

    @set:JsonAlias("CodeReference", "ns3:NoSwitchReason")
    var noSwitchReason: CodeReference?,
)

//Top level element, can reference itself
@JsonRootName("ns3:CommentedClassification")
data class CommentedClassificationElement(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("CommentedClassificationData", "ns3:Data")
    var dataBlocks: ArrayList<CommentedClassificationData> = ArrayList(),

    @set:JsonAlias("CommentedClassificationElement", "ns3:CommentedClassification")
    var childClassificationElements: ArrayList<CommentedClassificationElement> = ArrayList(),
)

@JsonRootName("ns3:Data")
data class CommentedClassificationData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Title")
    var title: TranslatedDataNoNS?,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Content")
    var content: TranslatedDataNoNS?,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:PosologyNote")
    var posologyNote: TranslatedDataNoNS?,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Url")
    var url: TranslatedDataNoNS?,
)

//Top level element
@JsonRootName("ns3:Vmp")
data class VmpElement(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("VmpData", "ns3:Data")
    var dataBlocks: ArrayList<VmpData> = ArrayList(),

    @set:JsonAlias("VmpComponent", "ns3:VmpComponent")
    var vmpComponents: ArrayList<VmpComponent> = ArrayList()
)

@JsonRootName("ns3:Data")
data class VmpData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Name")
    var name: TranslatedDataNoNS,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Abbreviation")
    var abbreviation: TranslatedDataNoNS,

    @set:JsonAlias("CodeReference", "ns3:CommentedClassification")
    var commentedClassificationReference: CodeReference,

    @set:JsonAlias("CodeReference", "ns3:VmpGroup")
    var vmpGroupReference: CodeReference,
)

@JsonRootName("ns3:VmpComponent")
data class VmpComponent(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("VmpComponentData", "ns3:Data")
    var dataBlocks: ArrayList<VmpComponentData> = ArrayList(),

    @set:JsonAlias("VirtualIngredient", "ns3:VirtualIngredient")
    var virtualIngredients: ArrayList<VirtualIngredient> = ArrayList()
)

@JsonRootName("ns3:Data")
data class VmpComponentData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("ns2:PhaseNumber")
    var phaseNumber: String,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Name")
    var name: TranslatedDataNoNS,

    @set:JsonAlias("CodeReference", "ns3:VirtualForm")
    var virtualForm: CodeReference,

    @set:JsonAlias("CodeReference", "ns3:RouteOfAdministration")
    var routeOfAdministration: CodeReference,
)

@JsonRootName("ns3:VirtualIngredient")
data class VirtualIngredient(
    @set:JsonProperty("rank")
    var rank: String,

    @set:JsonAlias("VirtualIngredientData", "ns3:Data")
    var dataBlocks: ArrayList<VirtualIngredientData> = ArrayList()
)

@JsonRootName("ns3:Data")
data class VirtualIngredientData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("ns2:Type")
    var type: String,

    @set:JsonAlias("VirtualIngredientStrength", "ns2:Strength")
    var strength: VirtualIngredientStrength,

    @set:JsonAlias("CodeReference", "ns3:Substance")
    var substanceReference: CodeReference,
)

@JsonRootName("ns2:Strength")
data class VirtualIngredientStrength(
    @set:JsonAlias("NumeratorRange", "NumeratorRange")
    var numeratorRange: NumeratorRange?,

    @set:JsonAlias("Denominator", "Denominator")
    var denominator: Denominator?,
)

@JsonRootName("NumeratorRange")
data class NumeratorRange (
    @set:JsonProperty("unit")
    var unit: String?,

    @set:JsonProperty("Min")
    var min: String?,

    @set:JsonProperty("Max")
    var max: String?,
)

@JsonRootName("Denominator")
data class Denominator(
    @set:JsonProperty("unit")
    var unit: String?,

    @set:JacksonXmlText
    var Denominator: String?,
)

