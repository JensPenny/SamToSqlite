import com.fasterxml.jackson.dataformat.xml.XmlFactory
import db.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.slf4j.LoggerFactory
import parser.*
import pojo.*
import xml.createXmlInputFactory
import xml.createXmlMapper
import java.io.File
import java.io.StringWriter
import java.nio.file.Files
import java.time.*
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.StartElement
import kotlin.io.path.name
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

val mainLogger = LoggerFactory.getLogger("SamV2Exporter")

@OptIn(ExperimentalTime::class)
fun main() {
    val dbInit = DBInitialization()
    dbInit.createDB()
    dbInit.createTables()

    val inputFactory = createXmlInputFactory()
    val xmlMapper = createXmlMapper(inputFactory)

    var ampFile: File? = null
    var chapter4File: File? = null
    var compoundingFile: File? = null
    var companyFile: File? = null
    var nonmedicinalFile: File? = null
    var referenceFile: File? = null
    var reimbursementFile: File? = null
    var reimbursementLawFile: File? = null
    var vmpFile: File? = null
    // using extension function walk

    val fileDirectory = File("res/latest/")
    if (!fileDirectory.isDirectory) {
        throw IllegalArgumentException("File-object at ${fileDirectory.path} is not a directory.")
    }

    Files.newDirectoryStream(fileDirectory.toPath()).forEach {
        when {
            it.name.matches(Regex("AMP.*")) -> ampFile = it.toFile()
            it.name.matches(Regex("CHAPTERIV.*")) -> chapter4File = it.toFile()
            it.name.matches(Regex("CMP.*")) -> compoundingFile = it.toFile()
            it.name.matches(Regex("CPN.*")) -> companyFile = it.toFile()
            it.name.matches(Regex("NONMEDICINAL.*")) -> nonmedicinalFile = it.toFile()
            it.name.matches(Regex("REF.*")) -> referenceFile = it.toFile()
            it.name.matches(Regex("RMB.*")) -> reimbursementFile = it.toFile()
            it.name.matches(Regex("RML.*")) -> reimbursementLawFile = it.toFile()
            it.name.matches(Regex("VMP.*")) -> vmpFile = it.toFile()
            else -> mainLogger.error("Could not find a use for path $it")
        }
    }

    mainLogger.info("Starting export")
    val fullTime = measureTime {

        //logMemory()
        val ampParseTime = measureTime { parseAmpXml(inputFactory, xmlMapper, ampFile!!) }
        //logMemory()
        val chapter4Time = measureTime { parseChapter4Xml(inputFactory, xmlMapper, chapter4File!!) }
        //logMemory()
        val compoundingTime = measureTime { parseCompoundingXml(inputFactory, xmlMapper, compoundingFile!!) }
        //logMemory()
        val cpnParseTime = measureTime { parseCompanyXml(inputFactory, xmlMapper, companyFile!!) }
        //logMemory()
        val nonmedicinalTime = measureTime { parseNonMedicinalXml(inputFactory, xmlMapper, nonmedicinalFile!!) }
        //logMemory()
        val refParseTime = measureTime { parseReferenceXml(inputFactory, xmlMapper, referenceFile!!) }
        //logMemory()
        val reimbursementTime =
            measureTime { parseReimbursementContextXml(inputFactory, xmlMapper, reimbursementFile!!) }
        //logMemory()
        //val reimbursementLawTime =
            //measureTime { parseReimbursementLawXml(inputFactory, xmlMapper, reimbursementLawFile!!) }
        //logMemory()
        val vmpParseTime = measureTime { parseVmpXml(inputFactory, xmlMapper, vmpFile!!) }
        //logMemory()

        //Pooling logresults to make this stuff more readable
        mainLogger.info("AMP file parsed in ${ampParseTime.inWholeMinutes}:${ampParseTime.inWholeSeconds - (ampParseTime.inWholeMinutes * 60)}")
        mainLogger.info("CHAPTERIV file parsed in ${chapter4Time.inWholeMinutes}:${chapter4Time.inWholeSeconds - (chapter4Time.inWholeMinutes * 60)}")
        mainLogger.info("CMP file parsed in ${compoundingTime.inWholeMinutes}:${compoundingTime.inWholeSeconds - (compoundingTime.inWholeMinutes * 60)}")
        mainLogger.info("CPN file parsed in ${cpnParseTime.inWholeMinutes}:${cpnParseTime.inWholeSeconds - (cpnParseTime.inWholeMinutes * 60)}")
        mainLogger.info("NONMEDICINAL file parsed in ${nonmedicinalTime.inWholeMinutes}:${nonmedicinalTime.inWholeSeconds - (nonmedicinalTime.inWholeMinutes * 60)}")
        mainLogger.info("REF file parsed in ${refParseTime.inWholeMinutes}:${refParseTime.inWholeSeconds - (refParseTime.inWholeMinutes * 60)}")
        mainLogger.info("RMB file parsed in ${reimbursementTime.inWholeMinutes}:${reimbursementTime.inWholeSeconds - (reimbursementTime.inWholeMinutes * 60)}")
        //mainLogger.info("RML file parsed in ${reimbursementLawTime.inWholeMinutes}:${reimbursementLawTime.inWholeSeconds - (reimbursementLawTime.inWholeMinutes * 60)}")
        mainLogger.info("RML (reimbursement law) is deprecated and will not be exported")
        mainLogger.info("VMP file parsed in ${vmpParseTime.inWholeMinutes}:${vmpParseTime.inWholeSeconds - (vmpParseTime.inWholeMinutes * 60)}")
    }
    mainLogger.info("Full export parsed in ${fullTime.inWholeMinutes}:${fullTime.inWholeSeconds - (fullTime.inWholeMinutes * 60)}")
}

inline fun tryPersist(call: () -> Unit) {
    try {
        call()
    } catch (e: ExposedSQLException) {
        mainLogger.error("Message: ${e.message}")
        mainLogger.error("Trace: ${e.stackTrace}")
    }
}

fun fullElement(startElement: StartElement, reader: XMLEventReader): String {
    val writer = StringWriter()
    val xmlWriter = XmlFactory.builder().xmlOutputFactory().createXMLEventWriter(writer)
    xmlWriter.namespaceContext = startElement.namespaceContext
    xmlWriter.add(startElement)

    var openedTags = 1

    var event = reader.nextEvent()
    while (!(event.isEndElement && event.asEndElement().name.localPart == startElement.name.localPart && openedTags <= 1)) {
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

private fun logMemory() {
    val mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    mainLogger.info("total: ${Runtime.getRuntime().totalMemory() / 1024 / 1024}\tfree: ${Runtime.getRuntime().freeMemory() / 1024 / 1024}\tused: ${mem / 1024 / 1024}")
}