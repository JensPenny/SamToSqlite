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

}