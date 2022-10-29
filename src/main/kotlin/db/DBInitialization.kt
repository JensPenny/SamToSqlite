package db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction

fun createTables() {
    println("creating tables")

    //AMP Tables
    transaction {
        drop(ActualMedicineSamTableModel.AMP_FAMHP)
        create(ActualMedicineSamTableModel.AMP_FAMHP)
        drop(ActualMedicineSamTableModel.AMP_BCPI)
        create(ActualMedicineSamTableModel.AMP_BCPI)
    }

    //Reference tables
    transaction {
        drop(ReferenceTableModel.ATC)
        create(ReferenceTableModel.ATC)
        drop(ReferenceTableModel.DLVM)
        create(ReferenceTableModel.DLVM)
        drop(ReferenceTableModel.DLVMS)
        create(ReferenceTableModel.DLVMS)
        drop(ReferenceTableModel.DVCTP)
        create(ReferenceTableModel.DVCTP)
        drop(ReferenceTableModel.PCKCL)
        create(ReferenceTableModel.PCKCL)
        drop(ReferenceTableModel.PCKMT)
        create(ReferenceTableModel.PCKMT)
        drop(ReferenceTableModel.PCKTP)
        create(ReferenceTableModel.PCKTP)
        drop(ReferenceTableModel.PHFRM)
        create(ReferenceTableModel.PHFRM)
        drop(ReferenceTableModel.ROA)
        create(ReferenceTableModel.ROA)
        drop(ReferenceTableModel.SBST)
        create(ReferenceTableModel.SBST)
        drop(ReferenceTableModel.NOSWR)
        create(ReferenceTableModel.NOSWR)
        drop(ReferenceTableModel.VTFRM)
        create(ReferenceTableModel.VTFRM)
        drop(ReferenceTableModel.WADA)
        create(ReferenceTableModel.WADA)
        drop(ReferenceTableModel.NOGNPR)
        create(ReferenceTableModel.NOGNPR)
        drop(ReferenceTableModel.STDFRM)
        create(ReferenceTableModel.STDFRM)
        drop(ReferenceTableModel.STDROA)
        create(ReferenceTableModel.STDROA)
        drop(ReferenceTableModel.STDSBST)
        create(ReferenceTableModel.STDSBST)
        drop(ReferenceTableModel.STDUNT)
        create(ReferenceTableModel.STDUNT)
        drop(ReferenceTableModel.APPENDIX)
        create(ReferenceTableModel.APPENDIX)
        drop(ReferenceTableModel.FORMCAT)
        create(ReferenceTableModel.FORMCAT)
        //Parameter should come here, if it's a bit less shit
        drop(ReferenceTableModel.RMBCRIT)
        create(ReferenceTableModel.RMBCRIT)
    }
}

fun createDB() {
    //Database.connect()
    // In file
    Database.connect("jdbc:sqlite:/home/jens/Workspace/SamToSqlite/data/data.db", "org.sqlite.JDBC")
    // In memory
    //Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC";
}