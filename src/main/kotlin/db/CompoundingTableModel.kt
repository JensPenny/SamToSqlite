package db

import db.CompanyTableModel.CPN.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

//Compounding part
class CompoundingTableModel {

    //Compounding ingredient
    object COMP_INGREDIENT : IntIdTable("COMP_INGREDIENT") {
        val productId = varchar("productId", 100)
        val code = varchar("code", 20)
        val codeSystem = varchar("codeType", 10)
    }

    //Compounding ingredient synonyms
    object COMP_INGREDIENT_SYNONYM : IntIdTable("COMP_INGREDIENT_SYN") {
        val ingredientProductId = varchar("productId", 100)
        val language = varchar("language", 10).nullable()
        val rank = varchar("rank",10).nullable()
        val synonym = varchar("synonym", 50).nullable()
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Compounding formula
    object COMP_FORMULA : IntIdTable("COMP_FORMULA") {
        val productId = varchar("productId", 100)
        val code = varchar("code", 20)
        val codeSystem = varchar("codeType", 10)
    }

    //Compounding formula synonyms
    object COMP_FORMULA_SYNONYM : IntIdTable("COMP_FORMULA_SYN") {
        val formulaProductId = varchar("formulaProductId", 100)
        val language = varchar("language", 10).nullable()
        val rank = varchar("rank",10).nullable()
        val synonym = varchar("synonym", 50).nullable()
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }
}