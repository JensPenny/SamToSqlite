package db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun createTables() {
    println("creating tables")

    //AMP Tables
    transaction {
        SchemaUtils.drop(ActualMedicineSamTableModel.AMP_FAMHP)
        SchemaUtils.create(ActualMedicineSamTableModel.AMP_FAMHP)
        SchemaUtils.drop(ActualMedicineSamTableModel.AMP_BCPI)
        SchemaUtils.create(ActualMedicineSamTableModel.AMP_BCPI)
    }

    //Reference tables
    transaction {
        SchemaUtils.drop(ReferenceTableModel.ATC)
        SchemaUtils.create(ReferenceTableModel.ATC)
    }

}

fun createDB() {
    //Database.connect()
    // In file
    Database.connect("jdbc:sqlite:/home/jens/Workspace/SamToSqlite/data/data.db", "org.sqlite.JDBC")
    // In memory
    //Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC";
}