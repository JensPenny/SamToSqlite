package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JsonRootName("ns2:CompoundingIngredient")
data class CompoundingIngredient(
    @set:JsonProperty("ProductId")
    var ProductId: String,

    @set:JsonProperty("code")
    var code: String,

    @set:JsonProperty("codeType")
    var codeType: String,

    @set:JsonAlias("CompoundingData", "ns2:Data")
    var data: List<CompoundingData> = ArrayList()
)

@JsonRootName("ns2:Data")
data class CompoundingData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String?,

    @set:JsonAlias("IngredientSynonym", "Synonym")
    var synonyms: List<CompoundingSynonym> = ArrayList()
)

@JsonRootName("Synonym")
data class CompoundingSynonym(
    @set:JsonProperty("xml:lang")
    var language: String?,

    @set:JsonProperty("rank")
    var rank: String?,

    @set:JacksonXmlText
    var Synonym: String?,
)

@JsonRootName("ns2:CompoundingFormula")
data class CompoundingFormula(
    @set:JsonProperty("ProductId")
    var ProductId: String,

    @set:JsonProperty("code")
    var code: String,

    @set:JsonProperty("codeType")
    var codeType: String,

    @set:JsonAlias("CompoundingData", "ns2:Data")
    var data: List<CompoundingData> = ArrayList()
)
