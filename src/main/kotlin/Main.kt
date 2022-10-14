import db.ActualMedicineSamTableModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.FileInputStream
import java.time.LocalDate
import java.util.*
import javax.xml.namespace.QName
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.XMLEvent

fun main(args: Array<String>) {
    createDB()
    createTables()

    val inputFactory = XMLInputFactory.newInstance()
    parseAmpXml(inputFactory)
}

data class ampElement(
    val from: LocalDate,
    val to: LocalDate?,
    var officialName: String? = null,
    var status: String? = null,
    var authorized: String? = null,
    var name: translatedData? = null,
    var blackTriangle: Boolean = false,
    var medicineType: String? = null,
    var companyActorNr: String? = null,
)

data class translatedData(
    var nl: String? = null,
    var fr: String? = null,
    var en: String? = null,
    var de: String? = null,
)

fun ampDataElement(
    event: XMLEvent,
    reader: XMLEventReader,
    from: LocalDate,
    to: LocalDate?
) {
    val amp = ampElement(from, to)
    while (!(event.isEndElement && event.asEndElement().name.localPart.equals("Data"))) {
        val nextElement = reader.nextEvent()
        if (nextElement.isStartElement) {
            val startElement = nextElement.asStartElement()

            when (startElement.name.localPart) {
                "OfficialName" -> amp.officialName = reader.elementText
                "Status" -> amp.status = reader.elementText
                "Name" -> {
                    //Itereer over de verschillende translates en voeg die toe
                    //Todo: generischer maken in toolsklasse
                    var nextInName = reader.nextTag()
                    val nameTranslations = translatedData()
                    while (!(nextInName.isEndElement && nextElement.asEndElement().name.localPart.equals("Name"))) {
                        if (nextInName.isStartElement) {
                            when (nextInName.asStartElement().name.localPart) {
                                "Nl" -> nameTranslations.nl = reader.elementText
                                "Fr" -> nameTranslations.fr = reader.elementText
                                "En" -> nameTranslations.en = reader.elementText
                                "De" -> nameTranslations.de = reader.elementText
                            }
                        }
                        nextInName = reader.nextTag()
                    }
                    amp.name = nameTranslations
                }
            }
        }

    }
}

fun amp(
    ampCode: String,
    vmpCode: Int,
    event: XMLEvent,
    reader: XMLEventReader
) {
    while (!(event.isEndElement && event.asEndElement().name.localPart.equals("Amp"))) {
        val nextElement = reader.nextEvent()
        if (nextElement.isStartElement) {
            val startElement = nextElement.asStartElement()

            when (startElement.name.localPart) {

                "Data" -> {
                    val endDateAsString = startElement.getAttributeByName(QName("to"))?.value
                    val endDate: LocalDate? = if (endDateAsString != null) {
                        LocalDate.parse(endDateAsString)
                    } else {
                        null
                    }

                    ampDataElement(
                        nextElement,
                        reader,
                        LocalDate.parse(startElement.getAttributeByName(QName("from")).value),
                        endDate
                    )
                }
            }

        } else if (nextElement.isEndElement) {

        }
    }
}

fun singleAmpElement() {

}

fun parseAmpXml(inputFactory: XMLInputFactory) {
    //Parse the AMP part
    val path = "res/latest/AMP-1657800909670.xml" //todo make changable
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    var amp: String = ""
    var vmp: Int? = 0
    var ampFrom: Date?
    var ampTo: Date?
    var officialName: String?
    var status: String?
    var nameFr: String?
    var nameNl: String?
    var nameDe: String?
    var nameEn: String?
    var blackTriangle = false
    var medicineType: String?
    var companyId: Int?

    while (reader.hasNext()) {
        val nextEvent = reader.nextEvent()

        if (nextEvent.isStartElement) {
            val startElement = nextEvent.asStartElement()

            when (startElement.name.localPart) {
                "Amp" -> {
                    val codeAttr = startElement.getAttributeByName(QName("code"))
                    val vmpCodeAttr = startElement.getAttributeByName(QName("vmpCode"))

                    amp = codeAttr.value
                    vmp = vmpCodeAttr.value?.toInt()
                }
                "Data" -> {

                }
                else -> {
                    println("no handler for " + startElement.name.localPart)
                }
            }
        }

        if (nextEvent.isEndElement) {
            val endElement = nextEvent.asEndElement()
            if (endElement.name.localPart.equals("Amp")) {
                //Persist the rows and create new ones
                transaction {
                    ActualMedicineSamTableModel.AMP_FAMHP.insert {
                        it[code] = amp
                        if (vmp != null){
                            it[vmpCode] = vmp
                        }
                    }

                    ActualMedicineSamTableModel.AMP_BCPI.insert {
                        it[code] = amp
                    }
                }
                println("inserted amp")
            }
        }
    }
}
fun createTables() {
    println("creating tables")
    transaction {
        SchemaUtils.create(ActualMedicineSamTableModel.AMP_FAMHP)
        SchemaUtils.create(ActualMedicineSamTableModel.AMP_BCPI)
    }
}
fun createDB() {
    //Database.connect()
    // In file
    Database.connect("jdbc:sqlite:/home/jens/Workspace/SamToSqlite/data/data.db", "org.sqlite.JDBC")
    // In memory
    //Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
}