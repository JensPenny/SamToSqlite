import com.fasterxml.jackson.dataformat.xml.XmlFactory
import db.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.slf4j.LoggerFactory
import parser.*
import pojo.*
import xml.createXmlInputFactory
import xml.createXmlMapper
import java.io.StringWriter
import java.time.*
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.StartElement
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private val logger = LoggerFactory.getLogger("SamV2Exporter")

@OptIn(ExperimentalTime::class)
fun main() {
    val dbInit = DBInitialization()
    dbInit.createDB()
    dbInit.createTables()

    val inputFactory = createXmlInputFactory()
    val xmlMapper = createXmlMapper(inputFactory)

    logger.info("Starting export")
    //Todo: make paths also variable

    val fullTime = measureTime {

/*
        //done
        val ampParseTime = measureTime { parseAmpXml(inputFactory, xmlMapper, "res/latest/AMP-1667395273070.xml") }

        //done
        val chapter4Time = measureTime { parseChapter4Xml(inputFactory, xmlMapper, "res/latest/CHAPTERIV-1667395606032.xml") }

        //done
        val compoundingTime =
            measureTime { parseCompoundingXml(inputFactory, xmlMapper, "res/latest/CMP-1667395564754.xml") }

        //done
        val cpnParseTime = measureTime { parseCompanyXml(inputFactory, xmlMapper, "res/latest/CPN-1667395271162.xml") }

        //done
        val nonmedicinalTime =
            measureTime { parseNonMedicinalXml(inputFactory, xmlMapper, "res/latest/NONMEDICINAL-1667395565095.xml") }

        //done
        val refParseTime =
            measureTime { parseReferenceXml(inputFactory, xmlMapper, "res/latest/REF-1667395561910.xml") }
*/
        val reimbursementTime =
            measureTime { parseReimbursementXml(inputFactory, xmlMapper, "res/latest/RMB-1667395543490.xml") }

        //Pooling logresults to make this stuff more readable
        //logger.info("AMP file parsed in ${ampParseTime.inWholeMinutes}:${ampParseTime.inWholeSeconds - (ampParseTime.inWholeMinutes * 60)}")
        //logger.info("CHAPTERIV file parsed in ${chapter4Time.inWholeMinutes}:${chapter4Time.inWholeSeconds - (chapter4Time.inWholeMinutes * 60)}")
        //logger.info("CMP file parsed in ${compoundingTime.inWholeMinutes}:${compoundingTime.inWholeSeconds - (compoundingTime.inWholeMinutes * 60)}")
        //logger.info("CPN file parsed in ${cpnParseTime.inWholeMinutes}:${cpnParseTime.inWholeSeconds - (cpnParseTime.inWholeMinutes * 60)}")
        //logger.info("NONMEDICINAL file parsed in ${nonmedicinalTime.inWholeMinutes}:${nonmedicinalTime.inWholeSeconds - (nonmedicinalTime.inWholeMinutes * 60)}")
        //logger.info("REF file parsed in ${refParseTime.inWholeMinutes}:${refParseTime.inWholeSeconds - (refParseTime.inWholeMinutes * 60)}")
        logger.info("RMB file parsed in ${reimbursementTime.inWholeMinutes}:${reimbursementTime.inWholeSeconds - (reimbursementTime.inWholeMinutes * 60)}")
    }
    logger.info("Full export parsed in ${fullTime.inWholeMinutes}:${fullTime.inWholeSeconds - (fullTime.inWholeMinutes * 60)}")

}

inline fun tryPersist(call: () -> Unit) {
    try {
        call()
    } catch (e: ExposedSQLException) {
        println("Message: ${e.message}")
        println("Trace: ${e.stackTrace}")
    }
}

fun fullElement(startElement: StartElement, reader: XMLEventReader): String {

    val writer = StringWriter()
    val xmlWriter = XmlFactory.builder().xmlOutputFactory().createXMLEventWriter(writer)
    xmlWriter.namespaceContext = startElement.namespaceContext
    xmlWriter.add(startElement)

    var event = reader.nextEvent()
    while (!(event.isEndElement && event.asEndElement().name.localPart == startElement.name.localPart)) {
        xmlWriter.add(event)
        event = reader.nextEvent()
    }
    xmlWriter.add(event)
    xmlWriter.close()
    return writer.toString()
}
