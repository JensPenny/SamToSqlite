package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

//Top level
@JsonRootName("ns3:LegalBasis")
data class LegalBasis(
    @set:JsonProperty("key")
    var key: String,

    @set:JsonAlias("LegalBasisData", "ns3:Data")
    var dataBlocks: ArrayList<LegalBasisData> = ArrayList(),

    @set:JsonAlias("LegalReference", "ns3:LegalReference")
    var legalReferences: ArrayList<LegalReference> = ArrayList(),
)

@JsonRootName("ns3:Data")
data class LegalBasisData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("ns2:Type")
    var type: String,

    @set:JsonProperty("ns2:EffectiveOn")
    var effectiveOn: String?,

    @JsonAlias("TranslatedDataNoNS", "ns2:Title")
    var title: TranslatedDataNoNS?,
)

@JsonRootName("ns3:LegalReference")
data class LegalReference(
    @set:JsonProperty("key")
    var key: String,

    //LegalReferences can attach to other legal references
    @set:JsonAlias("LegalReference", "ns3:LegalReference")
    var legalReferences: ArrayList<LegalReference> = ArrayList(),

    @set:JsonAlias("LegalReferenceData", "ns3:Data")
    var dataBlocks: ArrayList<LegalReferenceData> = ArrayList(),

    @set:JsonAlias("LegalText", "ns3:LegalText")
    var legalTexts: ArrayList<LegalText> = ArrayList(),
)

@JsonRootName("ns3:LegalText")
data class LegalText(
    @set:JsonProperty("key")
    var key: String,

    @set:JsonAlias("LegalTextData", "ns3:Data")
    var dataBlocks: ArrayList<LegalTextData> = ArrayList(),

    @set:JsonAlias("LegalText", "ns3:LegalText")
    var legalTexts: ArrayList<LegalText> = ArrayList(),
)

@JsonRootName("ns3:Data")
data class LegalTextData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @JsonAlias("TranslatedDataNoNS", "ns2:Content")
    var content: TranslatedDataNoNS?,

    @set:JsonProperty("ns2:Type")
    var type: String,

    @set:JsonProperty("ns2:SequenceNr")
    var sequenceNr: String,
)

@JsonRootName("ns3:Data")
data class LegalReferenceData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @JsonAlias("TranslatedDataNoNS", "ns2:Title")
    var title: TranslatedDataNoNS?,

    @set:JsonProperty("ns2:Type")
    var type: String,

    @set:JsonProperty("ns2:FirstPublishedOn")
    var firstPublishedOn: String?,
)
