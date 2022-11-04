package db

import db.ReferenceTableModel.STDUNT.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

/**
 * Collection of the reference tables.
 * Most of these are pretty straightforward and reasonable.
 * They get persisted once, we refer to these elements in other elements, even if they're fully expanded in the xml-files
 */
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

    //Delivery Mode Specification
    object DLVMS : IntIdTable("DLVMS") {
        val code = varchar("code", 7)

        val descriptionNameNl = text("descriptionNameNl")
        val descriptionNameFr = text("descriptionNameFr")
        val descriptionNameGer = text("descriptionNameGer").nullable()
        val descriptionNameEng = text("descriptionNameEng").nullable()
    }

    //Device Type
    object DVCTP : IntIdTable("DVCTP") {
        val code = varchar("code", 8)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
        val edqmCode = varchar("edqmCode", 20).nullable()
        val edqmDefinition = text("edqmDefinition").nullable()
    }

    //Packaging Closure
    object PCKCL : IntIdTable("PCKCL") {
        val code = varchar("code", 8)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
        val edqmCode = varchar("edqmCode", 20).nullable()
        val edqmDefinition = text("edqmDefinition").nullable()
    }

    //Packaging Material
    object PCKMT : IntIdTable("PCKMT") {
        val code = varchar("code", 8)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
    }

    //Packaging type
    object PCKTP : IntIdTable("PCKTP") {
        val code = varchar("code", 8)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
        val edqmCode = varchar("edqmCode", 20).nullable()
        val edqmDefinition = text("edqmDefinition").nullable()
    }

    //Pharmaceutical Form
    object PHFRM : IntIdTable("PHFRM") {
        val code = varchar("code", 10)
        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGerman = varchar("nameGer", 255).nullable()
        val nameEnglish = varchar("nameEng", 255).nullable()
    }

    //Route of administration
    object ROA : IntIdTable("ROA") {
        val code = varchar("code", 10).uniqueIndex()

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()
    }

    //Substance
    object SBST : IntIdTable("SBST") {
        val code = varchar("code", 10)
        val chemicalForm = varchar("chemical", 10).nullable()

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val noteNl = text("noteNl").nullable()
        val noteFr = text("noteFr").nullable()
        val noteGer = text("noteGer").nullable()
        val noteEng = text("noteEng").nullable()
    }

    //No switch Reason
    object NOSWR : IntIdTable("NOSWR") {
        val code = varchar("code", 10)

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //Virtual Form
    object VTFRM : IntIdTable("VTFRM") {
        val code = varchar("code", 10)
        val abbreviatedNl = varchar("abbreviatedNl", 255)
        val abbreviatedFr = varchar("abbreviatedFr", 255)
        val abbreviatedGer = varchar("abbreviatedGer", 255).nullable()
        val abbreviatedEng = varchar("abbreviatedEng", 255).nullable()

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //World Anti-doping Agency
    object WADA : IntIdTable("WADA") {
        val code = varchar("code", 10)

        val nameNl = varchar("nameNl", 255)
        val nameFr = varchar("nameFr", 255)
        val nameGer = varchar("nameGer", 255).nullable()
        val nameEng = varchar("nameEng", 255).nullable()

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //No generic prescription reason
    object NOGNPR : IntIdTable("NOGNPR") {
        val code = varchar("code", 10)

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //Standard Form
    object STDFRM : IntIdTable("STDFRM") {
        val standard = varchar("standard", 20)
        val code = varchar("code", 20)

        val virtualFormCode = varchar("virtualFormCode", 10)
    }

    //Standard Route Of Administration
    object STDROA : IntIdTable("STDROA") {
        val standard = varchar("standard", 20)
        val code = varchar("code", 20)

        val routeOfAdministrationCode = varchar("roaCode", 10)
    }

    //Standard Substance
    object STDSBST : IntIdTable("STDSBST") {
        val standard = varchar("standard", 20)
        val code = varchar("code", 20)

        val substanceCode = varchar("substanceCode", 10)

    }

    //Standard Unit
    object STDUNT : IntIdTable("STDUNT") {
        val name = varchar("name", 20)

        val descriptionNl = varchar("descriptionNl", 255).nullable()
        val descriptionFr = varchar("descriptionFr", 255).nullable()
        val descriptionGer = varchar("descriptionGer", 255).nullable()
        val descriptionEng = varchar("descriptionEng", 255).nullable()
    }

    //Appendix - unknown in the documentation
    object APPENDIX: IntIdTable("APPENDIX") {
        val code = varchar("code", 20)

        val descriptionNl = varchar("descriptionNl", 255).nullable()
        val descriptionFr = varchar("descriptionFr", 255).nullable()
        val descriptionGer = varchar("descriptionGer", 255).nullable()
        val descriptionEng = varchar("descriptionEng", 255).nullable()
    }

    //Formcategory - unknown in the documentation
    object FORMCAT: IntIdTable("FORMCAT") {
        val code = varchar("code", 20)

        val descriptionNl = varchar("descriptionNl", 255).nullable()
        val descriptionFr = varchar("descriptionFr", 255).nullable()
        val descriptionGer = varchar("descriptionGer", 255).nullable()
        val descriptionEng = varchar("descriptionEng", 255).nullable()
    }

    //Reimbursement Criterion
    object RMBCRIT : IntIdTable("RMBCRIT") {
        val category = varchar("category", 10)
        val code = varchar("code", 10)

        val descriptionNl = text("descriptionNl").nullable()
        val descriptionFr = text("descriptionFr").nullable()
        val descriptionGer = text("descriptionGer").nullable()
        val descriptionEng = text("descriptionEng").nullable()
    }

    //Professional code
    object PROFESSIONALCODE : IntIdTable("PROFESSIONALCODE") {
        val professionalCv = varchar("professionalCv", 10)
        val nameId = integer("nameId")
        val professionalName = varchar("professionalName", 50).nullable()
    }

    //Appendix Type
    object APPENDIX_TYPE : IntIdTable("APPENDIX_TYPE") {
        val appendixTypeId = integer("appendixTypeId")
        val nameId = integer("nameId")
    }

    //Form Type
    object FORM_TYPE : IntIdTable("FORM_TABLE") {
        val formTypeId = integer("formTypeId")
        val nameId = integer("nameId")
    }

    //Nametype
    object NAME_TYPE : IntIdTable("NAME_TYPE") {
        val nameTypeCV = varchar("nameTypeCV", 6)
        val nameId = integer("nameId")
        val nameType = varchar("nameType", 50).nullable()
        val nameTypeSequence = integer("nameTypeSequence").nullable()
    }

    object LEGAL_REF_TO_PARAGRAPH : IntIdTable("LEGAL_REF_TO_PARAGRAPH") {
        val legalReferencePath = varchar("legalReferencePath", 79)
        val chapterName = varchar("chapterName", 10)
        val paragraphName = varchar("paragraphName", 10)
    }
}