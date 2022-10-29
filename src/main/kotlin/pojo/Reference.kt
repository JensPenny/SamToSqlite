package pojo

import com.fasterxml.jackson.annotation.*

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
    var description: TranslatedData,
)

/**
 * @See db.ReferenceTableModel.DLVMS
 */
@JsonRootName("DeliveryModusSpecification")
data class DeliveryModusSpecification(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Description")
    //var description: List<TranslatedData> = ArrayList(),
    var description: TranslatedData,
)

/**
 * @see db.ReferenceTableModel.DVCTP
 */
@JsonRootName("DeviceType")
data class DeviceType(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,

    @set:JsonProperty("edqmCode")
    var edqmCode: String?,

    @set:JsonProperty("edqmDefinition")
    var edqmDefinition: String?,
)

/**
 * @see db.ReferenceTableModel.PCKCL
 */
@JsonRootName("PackagingClosure")
data class PackagingClosure(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,

    @set:JsonProperty("edqmCode")
    var edqmCode: String?,

    @set:JsonProperty("edqmDefinition")
    var edqmDefinition: String?,
)

/**
 * @See db.ReferenceTableModel.PCKMT
 */
@JsonRootName("PackagingMaterial")
data class PackagingMaterial(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,
)

/**
 * @see db.ReferenceTableModel.PCKTP
 */
@JsonRootName("PackagingType")
data class PackagingType(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,

    @set:JsonProperty("edqmCode")
    var edqmCode: String?,

    @set:JsonProperty("edqmDefinition")
    var edqmDefinition: String?,
)

/**
 * @See db.ReferenceTableModel.PHFRM
 */
@JsonRootName("PharmaceuticalForm")
data class PharmaceuticalForm(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,
)

/**
 * @See db.ReferenceTableModel.ROA
 */
@JsonRootName("RouteOfAdministration")
data class RouteOfAdministration(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,
)

/**
 * @See db.ReferenceTableModel.SBST
 */
@JsonRootName("Substance")
data class Substance(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonProperty("chemicalForm")
    var chemicalForm: String?,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,

    @set:JsonAlias("TranslatedData", "Note")
    var note: TranslatedData?,
)

/**
 * @See db.ReferenceTableModel.NOSWR
 */
@JsonRootName("NoSwitchReason")
data class NoSwitchReason(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Description")
    var description: TranslatedData,
)

/**
 * @See db.ReferenceTableModel.VTFRM
 */
@JsonRootName("VirtualForm")
data class VirtualForm(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Abbreviation")
    var abbreviation: TranslatedData,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,

    @set:JsonAlias("TranslatedData", "Description")
    var description: TranslatedData?,
)

/**
 * @See db.ReferenceTableModel.VTFRM
 */
@JsonRootName("Wada")
data class Wada(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Name")
    var name: TranslatedData,

    @set:JsonAlias("TranslatedData", "Description")
    var description: TranslatedData?,
)

/**
 * @see db.ReferenceTableModel.NOGNPR
 */
@JsonRootName("NoGenericPrescriptionReason")
data class NoGenericPrescriptionReason(
    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("TranslatedData", "Description")
    var description: TranslatedData,
)

@JsonRootName("StandardForm")
data class StandardForm(
    @set:JsonProperty("standard")
    var standard: String,

    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("CodeReference", "VirtualForm")
    var virtualFormReference: CodeReference,
)

@JsonRootName("StandardRoute")
data class StandardRoute(
    @set:JsonProperty("standard")
    var standard: String,

    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("CodeReference", "RouteOfAdministration")
    var routeOfAdminReference: CodeReference,
)

@JsonRootName("StandardSubstance")
data class StandardSubstance(
    @set:JsonProperty("standard")
    var standard: String,

    @set:JsonProperty("code")
    var code: String,

    @set:JsonAlias("CodeReference", "Substance")
    var substanceReference: List<CodeReference> = ArrayList(),
)