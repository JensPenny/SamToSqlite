import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.module.kotlin.readValue
import db.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import parser.*
import pojo.*
import xml.createXmlInputFactory
import xml.createXmlMapper
import java.io.FileInputStream
import java.io.StringWriter
import java.lang.RuntimeException
import java.time.*
import java.util.concurrent.atomic.AtomicInteger
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
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

        val reimbursementTime =
            measureTime { parseReimbursementContextXml(inputFactory, xmlMapper, "res/latest/RMB-1667395543490.xml") }
*/

        val reimbursementLawTime = measureTime { parseReimbursementLawXml(inputFactory, xmlMapper, "res/latest/RML-1667395532545.xml") }

        //Pooling logresults to make this stuff more readable
        //logger.info("AMP file parsed in ${ampParseTime.inWholeMinutes}:${ampParseTime.inWholeSeconds - (ampParseTime.inWholeMinutes * 60)}")
        //logger.info("CHAPTERIV file parsed in ${chapter4Time.inWholeMinutes}:${chapter4Time.inWholeSeconds - (chapter4Time.inWholeMinutes * 60)}")
        //logger.info("CMP file parsed in ${compoundingTime.inWholeMinutes}:${compoundingTime.inWholeSeconds - (compoundingTime.inWholeMinutes * 60)}")
        //logger.info("CPN file parsed in ${cpnParseTime.inWholeMinutes}:${cpnParseTime.inWholeSeconds - (cpnParseTime.inWholeMinutes * 60)}")
        //logger.info("NONMEDICINAL file parsed in ${nonmedicinalTime.inWholeMinutes}:${nonmedicinalTime.inWholeSeconds - (nonmedicinalTime.inWholeMinutes * 60)}")
        //logger.info("REF file parsed in ${refParseTime.inWholeMinutes}:${refParseTime.inWholeSeconds - (refParseTime.inWholeMinutes * 60)}")
        //logger.info("RMB file parsed in ${reimbursementTime.inWholeMinutes}:${reimbursementTime.inWholeSeconds - (reimbursementTime.inWholeMinutes * 60)}")
        logger.info("RML file parsed in ${reimbursementLawTime.inWholeMinutes}:${reimbursementLawTime.inWholeSeconds - (reimbursementLawTime.inWholeMinutes * 60)}")
    }
    logger.info("Full export parsed in ${fullTime.inWholeMinutes}:${fullTime.inWholeSeconds - (fullTime.inWholeMinutes * 60)}")

}

fun parseReimbursementLawXml(inputFactory: XMLInputFactory,
                             xmlMapper: ObjectMapper,
                             path: String) {
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))
    val commitAfterAmount = 100 //Doesn't really matter - shitloads of recursive elements, so placing this here isn't really useful
    var currentCounter = AtomicInteger(0)

    tryPersist { transaction {
        while (reader.hasNext()) {
            val event = reader.nextEvent()

            if (event.isStartElement) {
                val startElement = event.asStartElement()

                if (currentCounter.get() >= commitAfterAmount) {
                    commit()
                    currentCounter = AtomicInteger(0)
                }

                when (startElement.name.localPart) {
                    "ns3:LegalBasis" -> {
                        val legalBasisString = fullElement(startElement, reader)
                        val legalBasis = xmlMapper.readValue<LegalBasis>(legalBasisString)

                        for (data in legalBasis.dataBlocks) {
                            currentCounter.incrementAndGet()

                            ReimbursementLawSamTableModel.LGLBAS.insert {
                                it[key] = legalBasis.key
                                it[titleNl] = data.title?.nl
                                it[titleFr] = data.title?.fr
                                it[titleEnglish] = data.title?.en
                                it[titleGerman] = data.title?.de

                                it[type] = data.type
                                it[effectiveOn] = data.effectiveOn?.let { d -> LocalDate.parse(d) }

                                it[validFrom] = LocalDate.parse(data.from)
                                it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
                            }
                        }

                        for (legalReference in legalBasis.legalReferences) {
                            val firstReferencePath = "${legalBasis.key}-${legalReference.key}"

                            for (data in legalReference.dataBlocks) {
                                currentCounter.incrementAndGet()

                                ReimbursementLawSamTableModel.LGLREF.insert {
                                    it[key] = legalReference.key
                                    it[basisKey] = legalBasis.key
                                    it[parentRefKey] = null
                                    it[legalReferencePath] = firstReferencePath
                                    it[type] = data.type
                                    it[titleNl] = data.title?.nl
                                    it[titleFr] = data.title?.fr
                                    it[titleEnglish] = data.title?.en
                                    it[titleGerman] = data.title?.de
                                    it[firstPublishedOn] = data.firstPublishedOn?.let { d -> LocalDate.parse(d) }
                                    //it[lastModifiedOn] = data.la
                                    //it[legalReferenceTrace]

                                    it[validFrom] = LocalDate.parse(data.from)
                                    it[validTo] = data.to?.let { d -> LocalDate.parse(d) }

                                }

                            }

                            for (secondReference in legalReference.legalReferences) {
                                val secondReferencePath = "${legalBasis.key}-${legalReference.key}-${secondReference.key}"
                                for (data in secondReference.dataBlocks) {
                                    currentCounter.incrementAndGet()

                                    ReimbursementLawSamTableModel.LGLREF.insert {
                                        it[key] = legalReference.key
                                        it[basisKey] = legalBasis.key
                                        it[parentRefKey] = legalReference.key
                                        it[legalReferencePath] = secondReferencePath
                                        it[type] = data.type
                                        it[titleNl] = data.title?.nl
                                        it[titleFr] = data.title?.fr
                                        it[titleEnglish] = data.title?.en
                                        it[titleGerman] = data.title?.de
                                        it[firstPublishedOn] = data.firstPublishedOn?.let { d -> LocalDate.parse(d) }
                                        //it[lastModifiedOn] = data.la
                                        //it[legalReferenceTrace]

                                        it[validFrom] = LocalDate.parse(data.from)
                                        it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
                                    }
                                }

                                if (secondReference.legalReferences.isNotEmpty()) {
                                    throw RuntimeException("We assumed that legal references were max. 2 levels deep. We were wrong")
                                }

                                for (legalText in secondReference.legalTexts) {
                                    insertRecursiveLegalText(legalText, legalText.key, secondReferencePath, legalBasis.key, legalReference.key, secondReference.key, currentCounter)
                                }
                            }

                            for (legalText in legalReference.legalTexts) {
                                insertRecursiveLegalText(legalText, legalText.key, firstReferencePath, legalBasis.key, legalReference.key, null, currentCounter)
                            }
                        }
                    }
                    else -> {
                        println("no handler for " + startElement.name.localPart)
                    }
                }
            }
        }
    } }
}

/**
 * Legal texts are recursive, what makes this...challenging to implement
 */
private fun insertRecursiveLegalText(currentText: LegalText,
                                     legalTxtPath: String,
                                     legalRefPath: String,
                                     legalBasisKey: String,
                                     legalRef1Key: String,
                                     legalRef2Key: String?,
                                     counter: AtomicInteger) {
    for (data in currentText.dataBlocks) {
        counter.incrementAndGet()
        ReimbursementLawSamTableModel.LGLTXT.insert {
            it[key] = currentText.key
            it[basisKey] = legalBasisKey
            it[referenceKey] = legalRef1Key
            it[reference2Key] = legalRef2Key

            it[legalTextPath] = legalTxtPath
            it[legalReferencePath] = legalRefPath
            it[type] = data.type
            it[contentNl] = data.content?.nl
            it[contentFr] = data.content?.fr
            it[contentEnglish] = data.content?.en
            it[contentGerman] = data.content?.de

            it[sequenceNumber] = data.sequenceNr.toInt()
            //it[lastModifiedOn] = data.

            it[validFrom] = LocalDate.parse(data.from)
            it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
        }
    }

    for (legalText in currentText.legalTexts) {
        insertRecursiveLegalText(legalText, "$legalTxtPath ${legalText.key}", legalRefPath, legalBasisKey, legalRef1Key, legalRef2Key, counter)
    }
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
