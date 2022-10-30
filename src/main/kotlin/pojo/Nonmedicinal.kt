package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("ns3:NonMedicinalProduct")
data class Nonmedicinal(

    @set:JsonProperty("ProductId")
    var ProductId: String,

    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("NonmedData", "ns3:Data")
    var datablocks: List<NonmedData> = ArrayList()

)

@JsonRootName("ns3:Data")
data class NonmedData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String?,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Name")
    var name: TranslatedDataNoNS,

    @set:JsonProperty("ns2:Category")
    var category: String,

    @set:JsonProperty("ns2:CommercialStatus")
    var commercialStatus: String,

    @set:JsonAlias("TranslatedDataNoNS","ns2:Producer")
    var producer: TranslatedDataNoNS,

    @set:JsonAlias("TranslatedDataNoNS", "ns2:Distributor")
    var distributor: TranslatedDataNoNS,
)