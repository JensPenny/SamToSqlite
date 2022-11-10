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

        val ampParseTime = measureTime { parseAmpXml(inputFactory, xmlMapper, "res/latest/AMP-1667395273070.xml") }
        val chapter4Time = measureTime { parseChapter4Xml(inputFactory, xmlMapper, "res/latest/CHAPTERIV-1667395606032.xml") }
        val compoundingTime = measureTime { parseCompoundingXml(inputFactory, xmlMapper, "res/latest/CMP-1667395564754.xml") }
        val cpnParseTime = measureTime { parseCompanyXml(inputFactory, xmlMapper, "res/latest/CPN-1667395271162.xml") }
        val nonmedicinalTime = measureTime { parseNonMedicinalXml(inputFactory, xmlMapper, "res/latest/NONMEDICINAL-1667395565095.xml") }
        val refParseTime = measureTime { parseReferenceXml(inputFactory, xmlMapper, "res/latest/REF-1667395561910.xml") }
        val reimbursementTime = measureTime { parseReimbursementContextXml(inputFactory, xmlMapper, "res/latest/RMB-1667395543490.xml") }
        val reimbursementLawTime = measureTime { parseReimbursementLawXml(inputFactory, xmlMapper, "res/latest/RML-1667395532545.xml") }
        val vmpParseTime = measureTime { parseVmpXml(inputFactory, xmlMapper, "res/latest/VMP-1667395497382.xml") }

        //Pooling logresults to make this stuff more readable
        logger.info("AMP file parsed in ${ampParseTime.inWholeMinutes}:${ampParseTime.inWholeSeconds - (ampParseTime.inWholeMinutes * 60)}")
        logger.info("CHAPTERIV file parsed in ${chapter4Time.inWholeMinutes}:${chapter4Time.inWholeSeconds - (chapter4Time.inWholeMinutes * 60)}")
        logger.info("CMP file parsed in ${compoundingTime.inWholeMinutes}:${compoundingTime.inWholeSeconds - (compoundingTime.inWholeMinutes * 60)}")
        logger.info("CPN file parsed in ${cpnParseTime.inWholeMinutes}:${cpnParseTime.inWholeSeconds - (cpnParseTime.inWholeMinutes * 60)}")
        logger.info("NONMEDICINAL file parsed in ${nonmedicinalTime.inWholeMinutes}:${nonmedicinalTime.inWholeSeconds - (nonmedicinalTime.inWholeMinutes * 60)}")
        logger.info("REF file parsed in ${refParseTime.inWholeMinutes}:${refParseTime.inWholeSeconds - (refParseTime.inWholeMinutes * 60)}")
        logger.info("RMB file parsed in ${reimbursementTime.inWholeMinutes}:${reimbursementTime.inWholeSeconds - (reimbursementTime.inWholeMinutes * 60)}")
        logger.info("RML file parsed in ${reimbursementLawTime.inWholeMinutes}:${reimbursementLawTime.inWholeSeconds - (reimbursementLawTime.inWholeMinutes * 60)}")
        logger.info("VMP file parsed in ${vmpParseTime.inWholeMinutes}:${vmpParseTime.inWholeSeconds - (vmpParseTime.inWholeMinutes * 60)}")
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

    var openedTags = 1

    var event = reader.nextEvent()
    while (!(event.isEndElement
                && event.asEndElement().name.localPart == startElement.name.localPart
                && openedTags <= 1
                )
    ) {
        xmlWriter.add(event)
        event = reader.nextEvent()

        //Open an extra tag if we're a startelement from the original element
        if (event.isStartElement && event.asStartElement().name.localPart == startElement.name.localPart) {
            openedTags++
        }

        //Close a tag if we're an extra ending element from the original element
        if (event.isEndElement && event.asEndElement().name.localPart == startElement.name.localPart) {
            openedTags--
        }
    }
    xmlWriter.add(event)
    xmlWriter.close()
    return writer.toString()
}
