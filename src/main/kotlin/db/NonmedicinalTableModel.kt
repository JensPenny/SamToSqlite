package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

/**
 * Nonmedicinal table models
 */
class NonmedicinalTableModel {

    //Nonmedicinal
    object NONMEDICINAL : IntIdTable("NONMEDICINAL") {
        val productId = varchar("productId", 100) //Not in docs, but is in export
        val cnk = varchar("cnk", 7)

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()

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

}