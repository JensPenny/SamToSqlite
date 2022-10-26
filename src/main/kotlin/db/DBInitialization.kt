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
        SchemaUtils.drop(ReferenceTableModel.DLVM)
        SchemaUtils.create(ReferenceTableModel.DLVM)
        SchemaUtils.drop(ReferenceTableModel.DLVMS)
        SchemaUtils.create(ReferenceTableModel.DLVMS)
        SchemaUtils.drop(ReferenceTableModel.DVCTP)
        SchemaUtils.create(ReferenceTableModel.DVCTP)
        SchemaUtils.drop(ReferenceTableModel.PCKCL)
        SchemaUtils.create(ReferenceTableModel.PCKCL)
        SchemaUtils.drop(ReferenceTableModel.PCKMT)
        SchemaUtils.create(ReferenceTableModel.PCKMT)
        SchemaUtils.drop(ReferenceTableModel.PCKTP)
        SchemaUtils.create(ReferenceTableModel.PCKTP)
        SchemaUtils.drop(ReferenceTableModel.PHFRM)
        SchemaUtils.create(ReferenceTableModel.PHFRM)
        SchemaUtils.drop(ReferenceTableModel.ROA)
        SchemaUtils.create(ReferenceTableModel.ROA)
        SchemaUtils.drop(ReferenceTableModel.SBST)
        SchemaUtils.create(ReferenceTableModel.SBST)
        SchemaUtils.drop(ReferenceTableModel.NOSWR)
        SchemaUtils.create(ReferenceTableModel.NOSWR)
    }
}

fun createDB() {
    //Database.connect()
    // In file
    Database.connect("jdbc:sqlite:/home/jens/Workspace/SamToSqlite/data/data.db", "org.sqlite.JDBC")
    // In memory
    //Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC";
}