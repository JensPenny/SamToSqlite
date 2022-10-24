package pojo

import java.time.LocalDate

/**
 * Temp object to hold the fields that can persist into object ActualMedicineSamTableModel.AMP_FAMHP
 */
data class AmpElement(
    var from: LocalDate? = null,
    var to: LocalDate? = null,
    var officialName: String? = null,
    var status: String? = null,
    var name: TranslatedData = TranslatedData(),
    var blackTriangle: Boolean = false,
    var medicineType: String? = null,
    var companyActorNr: Int? = null,
)

data class AMPComponentElement(
    var from: LocalDate? = null,
    var to: LocalDate? = null,
    var ampCode: String? = null,
    var sequenceNumber: String? = null,
    var vmpcCode: String? = null,
    var dividable: String? = null,
    var scored: String? = null,
    var crushable: String? = null,
    var containsAlcohol: String? = null,
    var sugarFree: String? = null,
    var modifiedReleasedType: String? = null,
    var specificDrugDevice: String? = null,
    var dimensions: String? = null,
    var name: TranslatedData = TranslatedData(),
    var note: TranslatedData = TranslatedData(),
    var concentration: String? = null,
    var osmoticConcentration: String? = null,
    var caloricValue: String? = null,
)

data class RealActualIngredientElement(
    var from: LocalDate? = null,
    var to: LocalDate? = null,
    var ampCode: String? = null,
    var sequenceNumber: String? = null,
    var rank: String? = null,
    var type: String? = null,
    var substanceCode: String? = null,
    var knownEffect: String? = null,
    var strengthQuantity: String? = null,
    var strengthUnit: String? = null,
    var strengthDescription: String? = null,
    var additionalInformation: String? = null,
)

