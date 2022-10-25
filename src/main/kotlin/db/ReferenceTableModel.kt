package db

import org.jetbrains.exposed.dao.id.IntIdTable

class ReferenceTableModel {
    //ATC Code
    object ATC : IntIdTable("ATC") {
        val code = varchar("code", 7)
        val description = text("description")
    }

    //Delivery mode
    object DLVM : IntIdTable("DLVM") {
        val code = varchar("code", 7)

        val descriptionNameNl = text("descriptionNameNl")
        val descriptionNameFr = text("descriptionNameFr")
        val descriptionNameGer = text("descriptionNameGer").nullable()
        val descriptionNameEng = text("descriptionNameEng").nullable()
    }

}