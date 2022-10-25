package pojo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("AtcClassification")
data class ATC(

    @set:JsonProperty("code")
    var atcCode: String,

    @set:JsonProperty("Description")
    var description: String,
)
/**
 * data class LedgerActivityDetail(

@set:JsonProperty("TransactionType")
var loanType: String? = null,

@set:JsonProperty("Amount")
var amount: String? = null
)


@JsonRootName("LedgerActivityObject")
data class LedgerActivity(

@set:JsonProperty("LedgerTransactionDate")
var LedgerTransactionDate: String? = null,

@set:JsonProperty("Amount")
var amount: String? = null,

// HERE IS WHERE THE MAGIC HAPPENS!!!
@set:JsonAlias("LedgerTransactionDetails", "LedgerActivityDetailObject")
var LedgerActivityDetails: List<LedgerActivityDetail> = ArrayList()
)


@JsonRootName("ArrayOfLedgerActivityObject")
data class LedgerActivities(

@set:JsonProperty("LedgerActivityObject")
var ledgerActivities: List<LedgerActivity> = ArrayList()
)
 */