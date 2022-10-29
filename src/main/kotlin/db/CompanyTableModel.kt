package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

class CompanyTableModel {

    //Company Actor Table
    //Sem. key = Actor Number
    object CPN : IntIdTable("CPN") {
        val actorNumber = varchar("actorNumber", 5) //Number(5) - varchar
        val authorisationNumber = varchar("authorisationNumber", 50).nullable()
        val vatCountryCode = varchar("vatCountryCode", 2).nullable()
        val vatNumber = varchar("vatNumber", 15).nullable()
        val europeanNumber = varchar("europeanNumber", 40).nullable()
        val denomination = varchar("denomination", 255)
        val legalForm = varchar("legalForm", 30).nullable()
        val building = varchar("building", 50).nullable()
        val streetName = varchar("streetName", 50).nullable()
        val streetNum = varchar("streetNum", 10).nullable()
        val postbox = varchar("postbox", 50).nullable()
        val postcode = varchar("postcode", 10).nullable()
        val city = varchar("city", 50).nullable()
        val countryCode = varchar("countryCode", 2).nullable()
        val phone = varchar("phone", 30).nullable()
        val language = varchar("language", 5)
        val website = varchar("website", 255).nullable()
        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }
}