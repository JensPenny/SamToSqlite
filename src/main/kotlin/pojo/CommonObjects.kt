package pojo

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Holder object for common xml parts that can have translations
 */
data class TranslatedData(
    @set:JsonProperty("ns2:Nl")
    var nl: String? = null,
    @set:JsonProperty("ns2:Fr")
    var fr: String? = null,
    @set:JsonProperty("ns2:En")
    var en: String? = null,
    @set:JsonProperty("ns2:De")
    var de: String? = null,
)

//The same holder, but for no namespace
data class TranslatedDataNoNS(
    @set:JsonProperty("Nl")
    var nl: String? = null,
    @set:JsonProperty("Fr")
    var fr: String? = null,
    @set:JsonProperty("En")
    var en: String? = null,
    @set:JsonProperty("De")
    var de: String? = null,
)

/**
 * Holder object for references to other objects by code-field
 */
data class CodeReference(
    @set:JsonProperty("code")
    var codeReference: String
)