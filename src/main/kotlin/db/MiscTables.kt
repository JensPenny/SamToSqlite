package db

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Adds a couple of miscelanious tables, like the compounding parts, the nonmedicinal part and the units
 */
class MiscTables {
    //Compounding part

    object COMP_INGREDIENT : IntIdTable("COMP_INGREDIENT") {
        val cnk = varchar("cnk", 7)
        val synonym = text("synonym").nullable()
    }

    object COMP_FORMULA : IntIdTable("COMP_FORMULA") {
        val cnk = varchar("cnk", 7)
        val synonym = text("synonym").nullable()
        val formulary = text("formulary").nullable()
    }

    //Nonmedicinal
    object NONMEDICINAL : IntIdTable("NONMEDICINAL") {
        val cnk = varchar("cnk", 7)

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val category = varchar("category", 5)
        val commercialStatus = varchar("commercialStatus", 5)

        val producerNl = varchar("producerNl", 255)
        val producerFr = varchar("producerFr", 255)
        val producerGer = varchar("producerGer", 255).nullable()
        val producerEng = varchar("producerEng", 255).nullable()

        val distributorNl = varchar("distributorNl", 255).nullable()
        val distributorFr = varchar("distributorFr", 255).nullable()
        val distributorGer = varchar("distributorGer", 255).nullable()
        val distributorEng = varchar("distributorEng", 255).nullable()

        val exFactoryPrice = integer("exFactoryPrice").nullable()
        val publicPrice = integer("publicPrice").nullable()
        val contraceptive = bool("contraceptive").nullable()
    }

    //Standard Unit
    object STDUNT : IntIdTable("STDUNT") {
        val name = varchar("name", 20)

        val descriptionNl = varchar("descriptionNl", 255).nullable()
        val descriptionFr = varchar("descriptionFr", 255).nullable()
        val descriptionGer = varchar("descriptionGer", 255).nullable()
        val descriptionEng = varchar("descriptionEng", 255).nullable()
    }
}