package pojo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

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

    @set:JsonProperty("description")
    var description: TranslatedData

)
