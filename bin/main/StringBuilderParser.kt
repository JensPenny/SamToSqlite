import com.fasterxml.jackson.module.kotlin.readValue
import db.ReferenceTableModel
import db.createDB
import db.createTables
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import parser.Mapper
import pojo.ATC
import java.io.FileInputStream
import java.io.StringWriter
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.StartElement

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
                "AtcClassification" -> {
                    val atcClassificationString = fullElement(startElement, reader)
                    val atc = Mapper.xmlMapper.readValue<ATC>(atcClassificationString)
                    println("ATC OBJ: ${atc.atcCode} - ${atc.description}")
                    tryPersist{
                        transaction {
                            val insertStatement = ReferenceTableModel.ATC.insert{
                                it[code] = atc.atcCode
                                it[description] = atc.description
                            }
                        }
                    }
                }

                else -> {
                    println("no handler for " + startElement.name.localPart)
                }
            }
        }
    }
}

inline fun tryPersist(call: () -> Any){
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
