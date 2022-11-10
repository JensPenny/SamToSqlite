package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import db.CompoundingTableModel
import fullElement
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.CompoundingFormula
import pojo.CompoundingIngredient
import tryPersist
import java.io.FileInputStream
import java.time.LocalDate
import javax.xml.stream.XMLInputFactory

fun parseCompoundingXml(
    inputFactory: XMLInputFactory, xmlMapper: ObjectMapper, path: String
) {
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    while (reader.hasNext()) {
        val event = reader.nextEvent()

        if (event.isStartElement) {
            val startElement = event.asStartElement()

            when (startElement.name.localPart) {
                "ns2:CompoundingIngredient" -> {
                    val compoundingIngredientString = fullElement(startElement, reader)
                    val compoundingIngredient = xmlMapper.readValue<CompoundingIngredient>(compoundingIngredientString)

                    tryPersist {
                        transaction {
                            CompoundingTableModel.COMP_INGREDIENT.insert {
                                it[productId] = compoundingIngredient.ProductId
                                it[codeSystem] = compoundingIngredient.codeType
                                it[code] = compoundingIngredient.code
                            }

                            for (data in compoundingIngredient.data) {
                                for (synonymObject in data.synonyms) {
                                    CompoundingTableModel.COMP_INGREDIENT_SYNONYM.insert {
                                        it[ingredientProductId] = compoundingIngredient.ProductId
                                        it[language] = synonymObject.language
                                        it[rank] = synonymObject.rank
                                        it[synonym] = synonymObject.Synonym

                                        it[validFrom] = LocalDate.parse(data.from)
                                        if (data.to != null) {
                                            it[validTo] = LocalDate.parse(data.to)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "ns2:CompoundingFormula" -> {
                    val compoundingFormulaString = fullElement(startElement, reader)
                    val compoundingFormula = xmlMapper.readValue<CompoundingFormula>(compoundingFormulaString)

                    tryPersist {
                        transaction {
                            CompoundingTableModel.COMP_FORMULA.insert {
                                it[productId] = compoundingFormula.ProductId
                                it[codeSystem] = compoundingFormula.codeType
                                it[code] = compoundingFormula.code
                            }

                            for (data in compoundingFormula.data) {
                                for (synonymObject in data.synonyms) {
                                    CompoundingTableModel.COMP_FORMULA_SYNONYM.insert {
                                        it[formulaProductId] = compoundingFormula.ProductId
                                        it[language] = synonymObject.language
                                        it[rank] = synonymObject.rank
                                        it[synonym] = synonymObject.Synonym

                                        it[validFrom] = LocalDate.parse(data.from)
                                        if (data.to != null) {
                                            it[validTo] = LocalDate.parse(data.to)
                                        }
                                    }
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