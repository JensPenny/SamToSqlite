package db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

/**
 * Deprecated tables. See Chapter IV statements
 */
class ReimbursementLawSamTableModel {

    //Legal basis 
    object LGLBAS : IntIdTable("LGLBAS") {
        val key = varchar("key", 15)

        val titleNl = varchar("titleNl", 255).nullable()
        val titleFr = varchar("titleFr", 255).nullable()
        val titleGerman = varchar("titleGer", 255).nullable()
        val titleEnglish = varchar("titleEng", 255).nullable()

        val type = varchar("type", 30)
        val effectiveOn = date("effectiveOn").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Legal Reference
    object LGLREF : IntIdTable("LGLREF") {
        //Own identifiers
        val key = varchar("key", 70) //Own field to persist the key as well, since legalReferencePath is a combo field
        val basisKey = varchar("LGLBASkey", 15) //Parent reference
        val parentRefKey = varchar("LGLREFkey", 70).nullable() //Possible parent reference

        val legalReferencePath = varchar("legalReferencePath", 79)
        val type = varchar("type", 30)

        val titleNl = varchar("titleNl", 255).nullable()
        val titleFr = varchar("titleFr", 255).nullable()
        val titleGerman = varchar("titleGer", 255).nullable()
        val titleEnglish = varchar("titleEng", 255).nullable()

        val firstPublishedOn = date("firstPublishedOn").nullable()
        //val lastModifiedOn = date("lastModifiedOn").nullable()
        //val legalReferenceTrace = text("legalReferenceTrace").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

    //Legal Text
    object LGLTXT : IntIdTable("LGLTXT") {
        //Own identifiers
        val key = varchar("key", 70) //Own field to persist the key as well, since legalReferencePath is a combo field
        val basisKey = varchar("LGLBASkey", 15) //Parent legal basis
        val referenceKey = varchar("LGLREFkey", 70) //Parent reference 1
        val reference2Key = varchar("LGLREF2Key", 70).nullable() //Second reference

        //Combined identifiers
        val legalTextPath = varchar("legalTextPath", 175)
        val legalReferencePath = varchar("legalReferencePath", 79).nullable()

        val type = varchar("type", 30)
        val contentNl = text("contentNl").nullable()
        val contentFr = text("contentFr").nullable()
        val contentGerman = text("contentGer").nullable()
        val contentEnglish = text("contentEng").nullable()

        val sequenceNumber = integer("sequenceNumber")
        //val lastModifiedOn = date("lastModifiedOn").nullable()

        val validFrom = date("validFrom")
        val validTo = date("validTo").nullable()
    }

}