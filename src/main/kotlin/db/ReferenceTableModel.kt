package db

import org.jetbrains.exposed.dao.id.IntIdTable

class ReferenceTableModel {
    //ATC Code
    object ATC : IntIdTable("ATC") {
        val code = varchar("code", 7)
        val description = text("description")
    }

}