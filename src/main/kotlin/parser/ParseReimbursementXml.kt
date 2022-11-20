package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import db.ReimbursementContextTableModel
import fullElement
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.ReimbursementContext
import tryPersist
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import javax.xml.stream.XMLInputFactory

fun parseReimbursementContextXml(
    inputFactory: XMLInputFactory, xmlMapper: ObjectMapper, file: File
) {

    val reader = inputFactory.createXMLEventReader(FileInputStream(file))
    val commitAfterAmount = 100
    var currentCounter = 0

    tryPersist {
        transaction {
            while (reader.hasNext()) {
                val event = reader.nextEvent()

                if (event.isStartElement) {
                    val startElement = event.asStartElement()

                    if (currentCounter >= commitAfterAmount) {
                        commit()
                        currentCounter = 0
                    }

                    when (startElement.name.localPart) {
                        "ns4:ReimbursementContext" -> {
                            val reimbursementString = fullElement(startElement, reader)
                            val reimbursementContext = xmlMapper.readValue<ReimbursementContext>(reimbursementString)

                            for (contextData in reimbursementContext.contextDatas) {
                                currentCounter++
                                ReimbursementContextTableModel.RMBCTX.insert {
                                    it[code] = reimbursementContext.code
                                    it[codeType] = reimbursementContext.codeType
                                    it[deliveryEnvironment] = reimbursementContext.deliveryEnvironment
                                    it[legalReferencePath] = reimbursementContext.legalReferencePath

                                    it[reimbursementCriterionCategory] = contextData.reimbursementCriterion.category
                                    it[reimbursementCriterionCode] = contextData.reimbursementCriterion.code
                                    it[multiple] = contextData.multiple
                                    it[temporary] = contextData.temporary
                                    it[reference] = contextData.reference
                                    it[flatRateSystem] = contextData.flatRateSystem
                                    it[reimbursementBasePrice] = contextData.reimbursementBasePrice
                                    it[referenceBasePrice] = contextData.referenceBasePrice
                                    it[copaymentSupplement] = contextData.copaymentSupplement
                                    it[pricingUnitQuantity] = contextData.pricingUnit.quantity
                                    it[pricingUnitLabelNl] = contextData.pricingUnit.label?.nl
                                    it[pricingUnitLabelFr] = contextData.pricingUnit.label?.fr
                                    it[pricingUnitLabelGerman] = contextData.pricingUnit.label?.de
                                    it[pricingUnitLabelEnglish] = contextData.pricingUnit.label?.en

                                    it[pricingSliceQuantity] = contextData.pricingSlice?.quantity
                                    it[pricingSliceLabelNl] = contextData.pricingSlice?.label?.nl
                                    it[pricingSliceLabelFr] = contextData.pricingSlice?.label?.fr
                                    it[pricingSliceLabelEnglish] = contextData.pricingSlice?.label?.en
                                    it[pricingSliceLabelGerman] = contextData.pricingSlice?.label?.de

                                    it[collegeForOrphanDrugs] = contextData.collegeForOrphanDrugs
                                    it[facturation85Perc] = contextData.chargedAt85Perc
                                    //it[exceptional] = contextData.

                                    it[validFrom] = LocalDate.parse(contextData.from)
                                    if (contextData.to != null) {
                                        it[validTo] = LocalDate.parse(contextData.to)
                                    }
                                }
                            }

                            for (copayment in reimbursementContext.copayments) {
                                for (copaymentData in copayment.copaymentData) {
                                    currentCounter++
                                    ReimbursementContextTableModel.COPAY.insert {
                                        it[code] = reimbursementContext.code
                                        it[codeType] = reimbursementContext.codeType
                                        it[deliveryEnvironment] = reimbursementContext.deliveryEnvironment
                                        it[legalReferencePath] = reimbursementContext.legalReferencePath
                                        it[regimeType] = copayment.regimeType
                                        it[feeAmount] = copaymentData.feeAmount

                                        it[validFrom] = LocalDate.parse(copaymentData.from)
                                        if (copaymentData.to != null) {
                                            it[validTo] = LocalDate.parse(copaymentData.to)
                                        }
                                    }
                                }
                            }
                        }

                        else -> {
                            println("no handler for " + startElement.name.localPart)
                        }
                    }
                }
            }
        }
    }
}