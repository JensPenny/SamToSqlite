package pojo

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Holder object for common xml parts that can have translations
 */
data class TranslatedData(
    @set:JsonProperty("Nl")
    var nl: String? = null,
    @set:JsonProperty("Fr")
    var fr: String? = null,
    @set:JsonProperty("En")
    var en: String? = null,
    @set:JsonProperty("De")
    var de: String? = null,
)