import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import db.ReferenceTableModel
import db.createDB
import db.createTables
import mu.KotlinLogging
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
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

    val inputFactory = XmlFactory.builder().xmlInputFactory()
    inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)

    val xmlMapper = XmlMapper.builder(XmlFactory(inputFactory, WstxOutputFactory()))
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
        .addModule(JacksonXmlModule())
        .defaultUseWrapper(false)
        .build()
        .registerKotlinModule()

    parseReferenceXml(inputFactory, xmlMapper)
}

fun parseReferenceXml(inputFactory: XMLInputFactory,
                      xmlMapper: ObjectMapper) {
    val path = "res/latest/REF-1657801178464.xml" //todo make changable
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    while (reader.hasNext()) {
        val event = reader.nextEvent()

        if (event.isStartElement) {
            val startElement = event.asStartElement()

            when (startElement.name.localPart) {
                "AtcClassification" -> {
                    val atcClassificationString = fullElement(startElement, reader)
                    val atc = xmlMapper.readValue<ATC>(atcClassificationString)
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
                    val deliveryModus = xmlMapper.readValue<DeliveryModus>(deliveryModusString)
                    tryPersist {
                        transaction {
                            ReferenceTableModel.DLVM.insert {
                                it[code] = deliveryModus.code
                                it[descriptionNameNl] = deliveryModus.description[0].nl!!
                                it[descriptionNameFr] = deliveryModus.description[0].fr!!
                                it[descriptionNameEng] = deliveryModus.description[0].en
                                it[descriptionNameGer] = deliveryModus.description[0].de
                            }
                        }
                    }
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
    val xmlWriter = XmlFactory.builder().xmlOutputFactory().createXMLEventWriter(writer)
    xmlWriter.namespaceContext = startElement.namespaceContext
    xmlWriter.add(startElement)
    //startElement.writeAsEncodedUnicode(writer)

    var event = reader.nextEvent()
    while (!(event.isEndElement && event.asEndElement().name.localPart == startElement.name.localPart)) {
        //event.writeAsEncodedUnicode(writer)
        xmlWriter.add(event)
        event = reader.nextEvent()
    }
    //event.writeAsEncodedUnicode(writer) //Write the last element as well
    xmlWriter.add(event)

    //xmlWriter.createXMLEventWriter(writer)
/*
    val xmlWriter = outputFactory.createXMLStreamWriter(StringWriter())

*/
   // return writer.toString()
    xmlWriter.close()
    return writer.toString()
}
