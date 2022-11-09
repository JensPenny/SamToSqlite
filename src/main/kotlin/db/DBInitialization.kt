package db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction

class DBInitialization {
    fun createTables() {
        println("creating tables")

        createAmpTables() //done
        createChapter4Tables() //done
        createCompoundingTables() //done
        createCompanyTables() //done
        createNonmedicinalTables() //done
        createReferenceTables() //done
        createReimbursementLawTables() //done
    }

    private fun createReimbursementLawTables() {
        transaction {
            drop(
                ReimbursementLawSamTableModel.RMBCTX,
                ReimbursementLawSamTableModel.COPAY,
                inBatch = true,
            )

            create(
                ReimbursementLawSamTableModel.RMBCTX,
                ReimbursementLawSamTableModel.COPAY,
                inBatch = true,
            )
        }
    }

    private fun createChapter4Tables() {
        transaction {
            drop(
                Chapter4SamTableModel.PARAGRAPH,
                Chapter4SamTableModel.VERSE,
                Chapter4SamTableModel.ADDED_DOCUMENT,
                Chapter4SamTableModel.EXCLUSION,
                Chapter4SamTableModel.QUALLIST,
                Chapter4SamTableModel.PROF_AUTHORISATION,
                Chapter4SamTableModel.NAME_EXPLANATION,
                Chapter4SamTableModel.NAME_TRANSLATION,
                inBatch = true
            )

            create(
                Chapter4SamTableModel.PARAGRAPH,
                Chapter4SamTableModel.VERSE,
                Chapter4SamTableModel.ADDED_DOCUMENT,
                Chapter4SamTableModel.EXCLUSION,
                Chapter4SamTableModel.QUALLIST,
                Chapter4SamTableModel.PROF_AUTHORISATION,
                Chapter4SamTableModel.NAME_EXPLANATION,
                Chapter4SamTableModel.NAME_TRANSLATION,
                inBatch = true
            )
        }
    }

    private fun createNonmedicinalTables() {
        transaction {
            drop(NonmedicinalTableModel.NONMEDICINAL)
            create(NonmedicinalTableModel.NONMEDICINAL)
        }
    }

    private fun createCompoundingTables() {
        transaction {
            drop(CompoundingTableModel.COMP_INGREDIENT)
            create(CompoundingTableModel.COMP_INGREDIENT)
            drop(CompoundingTableModel.COMP_INGREDIENT_SYNONYM)
            create(CompoundingTableModel.COMP_INGREDIENT_SYNONYM)
            drop(CompoundingTableModel.COMP_FORMULA)
            create(CompoundingTableModel.COMP_FORMULA)
            drop(CompoundingTableModel.COMP_FORMULA_SYNONYM)
            create(CompoundingTableModel.COMP_FORMULA_SYNONYM)
        }
    }

    private fun createReferenceTables() {
        transaction {
            drop(
                ReferenceTableModel.ATC,
                ReferenceTableModel.DLVM,
                ReferenceTableModel.DLVMS,
                ReferenceTableModel.DVCTP,
                ReferenceTableModel.PCKCL,
                ReferenceTableModel.PCKMT,
                ReferenceTableModel.PCKTP,
                ReferenceTableModel.PHFRM,
                ReferenceTableModel.ROA,
                ReferenceTableModel.SBST,
                ReferenceTableModel.NOSWR,
                ReferenceTableModel.VTFRM,
                ReferenceTableModel.WADA,
                ReferenceTableModel.NOGNPR,
                ReferenceTableModel.STDFRM,
                ReferenceTableModel.STDROA,
                ReferenceTableModel.STDSBST,
                ReferenceTableModel.STDUNT,
                ReferenceTableModel.APPENDIX,
                ReferenceTableModel.FORMCAT,
                ReferenceTableModel.RMBCRIT,
                ReferenceTableModel.PROFESSIONALCODE,
                ReferenceTableModel.APPENDIX_TYPE,
                ReferenceTableModel.FORM_TYPE,
                ReferenceTableModel.NAME_TYPE,
                ReferenceTableModel.LEGAL_REF_TO_PARAGRAPH,
                inBatch = true
            )

            create(
                ReferenceTableModel.ATC,
                ReferenceTableModel.DLVM,
                ReferenceTableModel.DLVMS,
                ReferenceTableModel.DVCTP,
                ReferenceTableModel.PCKCL,
                ReferenceTableModel.PCKMT,
                ReferenceTableModel.PCKTP,
                ReferenceTableModel.PHFRM,
                ReferenceTableModel.ROA,
                ReferenceTableModel.SBST,
                ReferenceTableModel.NOSWR,
                ReferenceTableModel.VTFRM,
                ReferenceTableModel.WADA,
                ReferenceTableModel.NOGNPR,
                ReferenceTableModel.STDFRM,
                ReferenceTableModel.STDROA,
                ReferenceTableModel.STDSBST,
                ReferenceTableModel.STDUNT,
                ReferenceTableModel.APPENDIX,
                ReferenceTableModel.FORMCAT,
                ReferenceTableModel.RMBCRIT,
                ReferenceTableModel.PROFESSIONALCODE,
                ReferenceTableModel.APPENDIX_TYPE,
                ReferenceTableModel.FORM_TYPE,
                ReferenceTableModel.NAME_TYPE,
                ReferenceTableModel.LEGAL_REF_TO_PARAGRAPH,
                inBatch = true
            )
        }
    }

    private fun createCompanyTables() {
        transaction {
            drop(CompanyTableModel.CPN)
            create(CompanyTableModel.CPN)
        }
    }

    private fun createAmpTables() {
        transaction {
            drop(
                ActualMedicineSamTableModel.AMP_FAMHP,
                ActualMedicineSamTableModel.AMP_BCPI,
                ActualMedicineSamTableModel.AMPC_FAMHP,
                ActualMedicineSamTableModel.AMPC_BCPI,
                ActualMedicineSamTableModel.AMPC_TO_ROA,
                ActualMedicineSamTableModel.AMPC_TO_PHARMFORM,
                ActualMedicineSamTableModel.AMPP_FAMHP,
                ActualMedicineSamTableModel.AMPP_BCFI,
                ActualMedicineSamTableModel.AMPP_NIHDI,
                ActualMedicineSamTableModel.AMPP_NIHDI_BIS,
                ActualMedicineSamTableModel.AMPP_ECON,
                ActualMedicineSamTableModel.AMPPC,
                ActualMedicineSamTableModel.AMPPCES,
                ActualMedicineSamTableModel.DMPP,
                ActualMedicineSamTableModel.RACTING,
                ActualMedicineSamTableModel.RACTIEQ,
                ActualMedicineSamTableModel.SPPROB,
                ActualMedicineSamTableModel.CMRCL,
                ActualMedicineSamTableModel.DRGIMP,
                inBatch = true
            )

            create(
                ActualMedicineSamTableModel.AMP_FAMHP,
                ActualMedicineSamTableModel.AMP_BCPI,
                ActualMedicineSamTableModel.AMPC_FAMHP,
                ActualMedicineSamTableModel.AMPC_BCPI,
                ActualMedicineSamTableModel.AMPC_TO_ROA,
                ActualMedicineSamTableModel.AMPC_TO_PHARMFORM,
                ActualMedicineSamTableModel.AMPP_FAMHP,
                ActualMedicineSamTableModel.AMPP_BCFI,
                ActualMedicineSamTableModel.AMPP_NIHDI,
                ActualMedicineSamTableModel.AMPP_NIHDI_BIS,
                ActualMedicineSamTableModel.AMPP_ECON,
                ActualMedicineSamTableModel.AMPPC,
                ActualMedicineSamTableModel.AMPPCES,
                ActualMedicineSamTableModel.DMPP,
                ActualMedicineSamTableModel.RACTING,
                ActualMedicineSamTableModel.RACTIEQ,
                ActualMedicineSamTableModel.SPPROB,
                ActualMedicineSamTableModel.CMRCL,
                ActualMedicineSamTableModel.DRGIMP,
                inBatch = true
            )
        }
    }

    fun createDB() {
        //Database.connect()
        // In file
        Database.connect("jdbc:sqlite:/home/jens/Workspace/SamToSqlite/data/data.db", "org.sqlite.JDBC")
        // In memory
        //Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC";
    }
}