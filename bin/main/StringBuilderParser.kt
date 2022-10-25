import com.fasterxml.jackson.module.kotlin.readValue
import db.ReferenceTableModel
import db.createDB
import db.createTables
import mu.KotlinLogging
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import parser.Mapper
import pojo.ATC
import pojo.DeliveryModus
import java.io.FileInputStream
import java.io.StringWriter
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.StartElement

val logger = KotlinLogging.logger {}

fun main() {
    createDB()
    createTables()

    val inputFactory = XMLInputFactory.newInstance()

    parseReferenceXml(inputFactory)
}

fun parseReferenceXml(inputFactory: XMLInputFactory) {
    val path = "res/latest/REF-1657801178464.xml" //todo make changable
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    while (reader.hasNext()) {
        val event = reader.nextEvent()

        if (event.isStartElement) {
            val startElement = event.asStartElement()

            when (startElement.name.localPart) {
                "_AtcClassification" -> { //todo _ niet committen - voor nu te negeren
                    val atcClassificationString = fullElement(startElement, reader)
                    val atc = Mapper.xmlMapper.readValue<ATC>(atcClassificationString)
                    tryPersist{
                        transaction {
                            ReferenceTableModel.ATC.insert{
                                it[code] = atc.atcCode
                                it[description] = atc.description
                            }
                        }
                        logger.info { "Persisted ATC: : ${atc.atcCode} - ${atc.description}" }
                    }
                }

                "DeliveryModus" -> {
                    val deliveryModusString = fullElement(startElement, reader)
                    val deliveryModus = Mapper.xmlMapper.readValue<DeliveryModus>(deliveryModusString)
                    logger.info { deliveryModus.toString() }

                }

                else -> {
                    println("no handler for " + startElement.name.localPart)
                }
            }
        }
    }
}

inline fun tryPersist(call: () -> Unit){
    try {
        call()
    } catch (e: ExposedSQLException) {
        println("Message: ${e.message}")
        println("Trace: ${e.stackTrace}")
    }}

fun fullElement(startElement: StartElement, reader: XMLEventReader): String {
    val writer = StringWriter()
    startElement.writeAsEncodedUnicode(writer)

    var event = reader.nextEvent()
    while (!(event.isEndElement && event.asEndElement().name.localPart == startElement.name.localPart)) {
        event.writeAsEncodedUnicode(writer)
        event = reader.nextEvent()
    }

    event.writeAsEncodedUnicode(writer) //Write the last element as well
    return writer.toString()
}
