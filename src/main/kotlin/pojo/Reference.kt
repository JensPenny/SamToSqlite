package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.annotation.JsonUnwrapped

/**
 * @see db.ReferenceTableModel.ATC
 */
@JsonRootName("AtcClassification")
data class ATC(

    @set:JsonProperty("code")
    var atcCode: String,

    @set:JsonProperty("Description")
    var description: String,
)

/**
 * @See db.ReferenceTableModel.DLVM
 */
@JsonRootName("DeliveryModus")
data class DeliveryModus(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Description")
    var description: List<TranslatedData> = ArrayList()

)
