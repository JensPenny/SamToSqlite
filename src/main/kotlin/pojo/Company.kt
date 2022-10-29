package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JsonRootName("ns2:Company")
data class Company(

    @set:JsonProperty("actorNr")
    var actorNr: String,

    @set:JsonAlias("CompanyData", "ns2:Data")
    var data: List<CompanyData> = ArrayList()

)

@JsonRootName("ns2:Data")
data class CompanyData(

    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String?,

    @set:JsonProperty("AuthorisationNr")
    var authorisationNr: String?,

    @set:JsonAlias("Vat", "VatNr")
    var vat: Vat?,

    //@set:JsonProperty("VatNr")
    //var vatNr: String?,

    @set:JsonProperty("Denomination")
    var denomination: String,

    @set:JsonProperty("LegalForm")
    var legalForm: String?,

    @set:JsonProperty("StreetName")
    var streetName: String?,

    @set:JsonProperty("StreetNum")
    var streetNum: String?,

    @set:JsonProperty("Postcode")
    var postcode: String?,

    @set:JsonProperty("City")
    var city: String?,

    @set:JsonProperty("CountryCode")
    var countryCode: String?,

    @set:JsonProperty("Phone")
    var phone: String?,

    @set:JsonProperty("Language")
    var language: String,

    @set:JsonProperty("Building")
    var building: String?,

    @set:JsonProperty("Postbox")
    var postbox: String?,

    @set:JsonProperty("Website")
    var website: String?,
)

@JsonRootName("VatNr")
data class Vat(
    @set:JsonProperty("countryCode")
    var countryCode: String?,

    @set:JacksonXmlText
    var VatNr: String?,
)