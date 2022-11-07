package pojo

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

//Top level element
@JsonRootName("ns2:Paragraph")
data class Paragraph(
    @set:JsonProperty("ChapterName")
    var chapterName: String,

    @set:JsonProperty("ParagraphName")
    var paragraphName: String,

    @set:JsonAlias("ParagraphData", "ns2:Data")
    var paragraphData: ArrayList<ParagraphData> = ArrayList(),

    @set:JsonAlias("Verse", "ns2:Verse")
    var verses: ArrayList<Verse> = ArrayList(),

    @set:JsonAlias("Exclusion", "ns2:Exclusion")
    var exclusion: ArrayList<Exclusion> = ArrayList()
)

@JsonRootName("ns2:Exclusion")
data class Exclusion(
    @set:JsonProperty("ExclusionType")
    var exclusionType: String?,

    @set:JsonProperty("IdentifierNum")
    var identifierNum: String?,

    @set:JsonAlias("ExclusionData", "ns2:Data")
    var exlusionData: ArrayList<ExclusionData> = ArrayList(),
)

@JsonRootName("ns2:Data")
data class ExclusionData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("CreatedTimestamp")
    var createdTimestamp: String?,

    @set:JsonProperty("CreatedUserId")
    var createdUserId: String?,

    @set:JsonProperty("ModificationStatus")
    var modificationStatus: String?,
)

@JsonRootName("ns2:Verse")
data class Verse(
    @set:JsonProperty("VerseSeq")
    var verseSeq: String,

    @set:JsonAlias("VerseData", "ns2:Data")
    var verseData: ArrayList<VerseData> = ArrayList(),

    @set:JsonAlias("AddedDocument", "ns2:AddedDocument")
    var addedDocuments: ArrayList<AddedDocument> = ArrayList()
)

@JsonRootName("ns2:AddedDocument")
data class AddedDocument(
    @JsonProperty("DocumentSeq")
    var documentSequence: String,

    @set:JsonAlias("VerseData", "ns2:Data")
    var documentData: ArrayList<AddedDocumentData> = ArrayList(),
)

@JsonRootName("ns2:Data")
data class AddedDocumentData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("CreatedTimestamp")
    var createdTimestamp: String?,

    @set:JsonProperty("CreatedUserId")
    var createdUserId: String?,

    @set:JsonProperty("NameId")
    var nameId: String,

    @set:JsonProperty("FormTypeId")
    var formTypeId: String,

    @set:JsonProperty("AppendixTypeId")
    var appendixTypeId: String,

    @set:JsonProperty("AddressUrl")
    var addressUrl: String?,

    @set:JsonProperty("ModificationStatus")
    var modificationStatus: String,
)

@JsonRootName("ns2:Data")
data class VerseData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("CreatedTimestamp")
    var createdTimestamp: String?,

    @set:JsonProperty("CreatedUserId")
    var createdUserId: String?,

    @set:JsonProperty("VerseNum")
    var verseNum: String?,

    @set:JsonProperty("VerseSeqParent")
    var verseSeqParent: String,

    @set:JsonProperty("VerseLevel")
    var verseLevel: String,

    @set:JsonProperty("VerseType")
    var verseType: String?,

    @set:JsonProperty("CheckBoxInd")
    var checkBoxInd: Boolean = false,

    @set:JsonProperty("MinCheckNum")
    var minCheckNumber: String?,

    @set:JsonProperty("AndClauseNum")
    var andClauseNum: String?,

    @set:JsonProperty("TextFr")
    var textFr: String,

    @set:JsonProperty("TextNl")
    var textNl: String,

    @set:JsonProperty("RequestType")
    var requestType: String?,

    @set:JsonProperty("AgreementTerm")
    var agreementTerm: String?,

    @set:JsonProperty("AgreementTermUnit")
    var agreementTermUnit: String?,

    @set:JsonProperty("MaxPackageNumber")
    var maxPackageNumber: String?,

    @set:JsonProperty("PurchasingAdvisorQualList")
    var purchasingAdvisorQualList: String?,

    @set:JsonProperty("ModificationDate")
    var modificationDate: String?,

    @set:JsonProperty("MaximumContentQuantity")
    var maximumContentQuantity: String?,

    @set:JsonProperty("AgreementYearMax")
    var agreementYearMax: String?,

    @set:JsonProperty("AgreementRenewalMax")
    var agreementRenewalMax: String?,

    @set:JsonProperty("SexRestricted")
    var sexRestricted: String?,

    @set:JsonProperty("MinimumAgeAuthorized")
    var minimumAgeAuthorized: String?,

    @set:JsonProperty("MinimumAgeAuthorizedUnit")
    var minimumAgeAuthorizedUnit: String?,

    @set:JsonProperty("MaximumAgeAuthorized")
    var maximumAgeAuthorized: String?,

    @set:JsonProperty("MaximumAgeAuthorizedUnit")
    var maximumAgeAuthorizedUnit: String?,

    @set:JsonProperty("MaximumContentUnit")
    var maximumContentUnit: String?,

    @set:JsonProperty("MaximumStrengthQuantity")
    var maximumStrengthQuantity: String?,

    @set:JsonProperty("MaximumStrengthUnit")
    var maximumStrengthUnit: String?,

    @set:JsonProperty("MaximumDurationQuantity")
    var maximumDurationQuantity: String?,

    @set:JsonProperty("MaximumDurationUnit")
    var maximumDurationUnit: String?,

    @set:JsonProperty("OtherAddedDocumentInd")
    var otherAddedDocument: Boolean?,

    @set:JsonProperty("ModificationStatus")
    var modificationStatus: String,
)

@JsonRootName("ns2:Data")
data class ParagraphData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("CreatedTimestamp")
    var createdTimestamp: String?,

    @set:JsonProperty("CreatedUserId")
    var createdUserId: String?,

    @set:JsonProperty("KeyStringNl")
    var keystringNl: String?,

    @set:JsonProperty("KeyStringFr")
    var keystringFr: String?,

    @set:JsonProperty("ProcessType")
    var processType: String?,

    @set:JsonProperty("PublicationDate")
    var publicationDate: String?,

    @set:JsonProperty("ModificationDate")
    var modificationDate: String?,

    @set:JsonProperty("ProcessTypeOverrule")
    var processTypeOverrule: String?,

    @set:JsonProperty("ParagraphVersion")
    var paragraphVersion: String?,

    @set:JsonProperty("ModificationStatus")
    var modificationStatus: String,

    @set:JsonProperty("AgreementType")
    var agreementType: String?,

    @set:JsonProperty("AgreementTypePro")
    var agreementTypePro: String?,
)

//Top level element
@JsonRootName("ns2:QualificationList")
data class QualificationList(
    @set:JsonProperty("QualificationList")
    var qualificationListId: String,

    @set:JsonAlias("QualificationListData", "ns2:Data")
    var qualificationListData: ArrayList<QualificationListData> = ArrayList(),

    @set:JsonAlias("ProfessionAuthorisation", "ns2:ProfessionalAuthorisation")
    var professionalAuthorisations: ArrayList<ProfessionalAuthorisation> = ArrayList()
)

@JsonRootName("ns2:ProfessionalAuthorisation")
data class ProfessionalAuthorisation(
    @set:JsonProperty("ProfessionalAuthorisationId")
    var professionalAuthorisationId: String?,

    @set:JsonAlias("ProfessionalAuthorisationData", "ns2:Data")
    var professionalAuthorisationData: ArrayList<ProfessionalAuthorisationData> = ArrayList(),
)

@JsonRootName("ns2:Data")
data class ProfessionalAuthorisationData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("CreatedTimestamp")
    var createdTimestamp: String?,
    @set:JsonProperty("CreatedUserId")
    var createdUserId: String?,
    @set:JsonProperty("ProfessionalCV")
    var professionalCv: String?,
    @set:JsonProperty("ModificationStatus")
    var modificationStatus: String?,
)

@JsonRootName("ns2:Data")
data class QualificationListData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("CreatedTimestamp")
    var createdTimestamp: String?,

    @set:JsonProperty("CreatedUserId")
    var createdUserId: String?,

    @set:JsonProperty("NameId")
    var nameId: String?,

    @set:JsonProperty("ModificationStatus")
    var modificationStatus: String?,
)

//Top level element
@JsonRootName("ns2:NameExplanation")
data class NameExplanation(
    @set:JsonProperty("nameId")
    var nameId: String?,

    @set:JsonAlias("NameExplanationData", "ns2:Data")
    var nameExplanationData: ArrayList<NameExplanationData> = ArrayList(),

    @set:JsonAlias("NameTranslation", "ns2:NameTranslation")
    var nameTranslations: ArrayList<NameTranslation> = ArrayList()
)

@JsonRootName("ns2:NameTranslation")
data class NameTranslation(
    @set:JsonProperty("NameTypeCV")
    var nametypeCv: String,

    @set:JsonProperty("LanguageCV")
    var languageCv: String,

    @set:JsonAlias("NameTranslationData", "ns2:Data")
    var nameTranslationData: ArrayList<NameTranslationData> = ArrayList(),
)

@JsonRootName("ns2:Data")
data class NameTranslationData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("CreatedTimestamp")
    var createdTimestamp: String?,
    @set:JsonProperty("CreatedUserId")
    var createdUserId: String?,
    @set:JsonProperty("ShortText")
    var shortText: String?,
    @set:JsonProperty("LongText")
    var longText: String?,
    @set:JsonProperty("ModificationStatus")
    var modificationStatus: String?,
)

@JsonRootName("ns2:Data")
data class NameExplanationData(
    @set:JsonProperty("from")
    var from: String,

    @set:JsonProperty("to")
    var to: String? = null,

    @set:JsonProperty("CreatedTimestamp")
    var createdTimestamp: String?,
    @set:JsonProperty("CreatedUserId")
    var createdUserId: String?,
    @set:JsonProperty("SourceTableId")
    var sourceTableId: String?,
    @set:JsonProperty("ModificationStatus")
    var modificationStatus: String?,
)