import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.module.kotlin.readValue
import db.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import parser.*
import pojo.*
import xml.createXmlInputFactory
import xml.createXmlMapper
import java.io.FileInputStream
import java.io.StringWriter
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

        //done
        val reimbursementTime =
            measureTime { parseReimbursementContextXml(inputFactory, xmlMapper, "res/latest/RMB-1667395543490.xml") }

        //done
        val reimbursementLawTime = measureTime { parseReimbursementLawXml(inputFactory, xmlMapper, "res/latest/RML-1667395532545.xml") }
 */
        val vmpParseTime = measureTime { parseVmpXml(inputFactory, xmlMapper, "res/latest/VMP-1667395497382.xml") }

        //Pooling logresults to make this stuff more readable
        //logger.info("AMP file parsed in ${ampParseTime.inWholeMinutes}:${ampParseTime.inWholeSeconds - (ampParseTime.inWholeMinutes * 60)}")
        //logger.info("CHAPTERIV file parsed in ${chapter4Time.inWholeMinutes}:${chapter4Time.inWholeSeconds - (chapter4Time.inWholeMinutes * 60)}")
        //logger.info("CMP file parsed in ${compoundingTime.inWholeMinutes}:${compoundingTime.inWholeSeconds - (compoundingTime.inWholeMinutes * 60)}")
        //logger.info("CPN file parsed in ${cpnParseTime.inWholeMinutes}:${cpnParseTime.inWholeSeconds - (cpnParseTime.inWholeMinutes * 60)}")
        //logger.info("NONMEDICINAL file parsed in ${nonmedicinalTime.inWholeMinutes}:${nonmedicinalTime.inWholeSeconds - (nonmedicinalTime.inWholeMinutes * 60)}")
        //logger.info("REF file parsed in ${refParseTime.inWholeMinutes}:${refParseTime.inWholeSeconds - (refParseTime.inWholeMinutes * 60)}")
        //logger.info("RMB file parsed in ${reimbursementTime.inWholeMinutes}:${reimbursementTime.inWholeSeconds - (reimbursementTime.inWholeMinutes * 60)}")
        //logger.info("RML file parsed in ${reimbursementLawTime.inWholeMinutes}:${reimbursementLawTime.inWholeSeconds - (reimbursementLawTime.inWholeMinutes * 60)}")
        logger.info("VMP file parsed in ${vmpParseTime.inWholeMinutes}:${vmpParseTime.inWholeSeconds - (vmpParseTime.inWholeMinutes * 60)}")
    }
    logger.info("Full export parsed in ${fullTime.inWholeMinutes}:${fullTime.inWholeSeconds - (fullTime.inWholeMinutes * 60)}")
}

fun parseVmpXml(
    inputFactory: XMLInputFactory,
    xmlMapper: ObjectMapper,
    path: String
) {
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    val commitAfterAmount = 100
    val counter = AtomicInteger(0)

    tryPersist {
        transaction {
            while (reader.hasNext()) {
                val event = reader.nextEvent()
                if (event.isStartElement) {
                    val startElement = event.asStartElement()

                    if (counter.get() > commitAfterAmount) {
                        TransactionManager.currentOrNull()?.commit()
                        counter.set(0)
                    }

                    when (startElement.name.localPart) {
                        "ns3:Vtm" -> {
                            val vtmString = fullElement(startElement, reader)
                            val vtm = xmlMapper.readValue<VtmElement>(vtmString)

                            for (data in vtm.dataBlocks) {
                                counter.incrementAndGet()
                                VirtualMedicineSamTableModel.VTM.insert {
                                    it[code] = vtm.code.toInt()
                                    it[nameNl] = data.name.nl!!
                                    it[nameFr] = data.name.fr!!
                                    it[nameEnglish] = data.name.en
                                    it[nameGerman] = data.name.de

                                    it[validFrom] = LocalDate.parse(data.from)
                                    it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
                                }
                            }
                        }

                        "ns3:VmpGroup" -> {
                            val vmpGroupString = fullElement(startElement, reader)
                            val vmpGroup = xmlMapper.readValue<VmpGroupElement>(vmpGroupString)

                            for (data in vmpGroup.dataBlocks) {
                                counter.incrementAndGet()

                                VirtualMedicineSamTableModel.VMPG.insert {
                                    it[code] = vmpGroup.code.toInt()
                                    it[noswrCode] = data.noSwitchReason?.codeReference
                                    it[nognprCode] = data.noGenericPrescriptionReason?.codeReference

                                    it[nameNl] = data.name.nl!!
                                    it[nameFr] = data.name.fr!!
                                    it[nameGerman] = data.name.de
                                    it[nameEnglish] = data.name.en

                                    //it[patientFrailtyIndicator] = data
                                    //it[singleAdministrationDose] =

                                    it[validFrom] = LocalDate.parse(data.from)
                                    it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
                                }
                            }
                        }

                        "ns3:CommentedClassification" -> {
                            val commentClassificationString = fullElement(startElement, reader)
                            val commentedClassification =
                                xmlMapper.readValue<CommentedClassificationElement>(commentClassificationString)

                            //This is recursive, so we start our own counter here.
                            persistCommentedClassification(commentedClassification, null, counter)
                        }

                        "ns3:Vmp" -> {
                            val vmpString = fullElement(startElement, reader)
                            val vmp = xmlMapper.readValue<VmpElement>(vmpString)

                            for (data in vmp.dataBlocks) {
                                counter.incrementAndGet()
                                VirtualMedicineSamTableModel.VMP.insert {
                                    it[code] = vmp.code.toInt()
                                    it[vmpGroupCode] = data.vmpGroupReference.codeReference.toInt()
                                    it[nameNl] = data.name.nl!!
                                    it[nameFr] = data.name.fr!!
                                    it[nameEnglish] = data.name.en
                                    it[nameGerman] = data.name.de

                                    it[abbreviatedNameNl] = data.abbreviation.nl!!
                                    it[abbreviatedNameFr] = data.abbreviation.fr!!
                                    it[abbreviatedNameEng] = data.abbreviation.en
                                    it[abbreviatedNameGer] = data.abbreviation.de

                                    it[validFrom] = LocalDate.parse(data.from)
                                    it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
                                }
                            }

                            for (vmpComponent in vmp.vmpComponents) {
                                for (data in vmpComponent.dataBlocks) {
                                    counter.incrementAndGet()
                                    VirtualMedicineSamTableModel.VMPC.insert {
                                        it[code] = vmpComponent.code.toInt()
                                        it[vmpCode] = vmp.code.toInt()
                                        it[virtualFormCode] = data.virtualForm.codeReference
                                        it[phaseNumber] = data.phaseNumber.toInt()

                                        it[nameNl] = data.name.nl!!
                                        it[nameFr] = data.name.fr!!
                                        it[nameEnglish] = data.name.en
                                        it[nameGerman] = data.name.de

                                        it[validFrom] = LocalDate.parse(data.from)
                                        it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
                                    }
                                }

                                for (virtualIngredient in vmpComponent.virtualIngredients) {
                                    for (data in virtualIngredient.dataBlocks) {
                                        counter.incrementAndGet()
                                        VirtualMedicineSamTableModel.VTLING.insert {
                                            it[vmpcCode] = vmpComponent.code.toInt()
                                            it[rank] = virtualIngredient.rank.toInt()
                                            it[substanceCode] = data.substanceReference.codeReference
                                            it[type] = data.type
                                            it[strengthNumeratorMinimum] = data.strength?.numeratorRange?.min
                                            it[strengthNumeratorMaximum] = data.strength?.numeratorRange?.max
                                            it[strengthNumeratorUnit] = data.strength?.numeratorRange?.unit
                                            it[strengthDenominator] = data.strength?.denominator?.Denominator
                                            it[strengthDenominatorUnit] = data.strength?.denominator?.unit

                                            it[validFrom] = LocalDate.parse(data.from)
                                            it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
                                        }
                                    }

                                    for (realVirtualIngredient in virtualIngredient.realVirtualIngredients) {
                                        for (data in realVirtualIngredient.dataBlocks) {
                                            counter.incrementAndGet()
                                            VirtualMedicineSamTableModel.RVTLING.insert {
                                                it[vmpcCode] = vmpComponent.code.toInt()
                                                it[rank] = virtualIngredient.rank.toInt()
                                                it[sequenceNumber] = realVirtualIngredient.sequenceNr.toInt()
                                                it[substanceCode] = data.substanceReference.codeReference
                                                it[type] = data.type
                                                it[strengthNumeratorMinimum] = data.strength?.numeratorRange?.min
                                                it[strengthNumeratorMaximum] = data.strength?.numeratorRange?.max
                                                it[strengthNumeratorUnit] = data.strength?.numeratorRange?.unit
                                                it[strengthDenominatorUnit] = data.strength?.denominator?.unit
                                                it[strengthDenominator] = data.strength?.denominator?.Denominator

                                                it[validFrom] = LocalDate.parse(data.from)
                                                it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
                                            }
                                        }
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
    }
}

private fun persistCommentedClassification(
    toPersist: CommentedClassificationElement,
    parentCode: String?,
    counter: AtomicInteger
) {
    for (data in toPersist.dataBlocks) {
        if (counter.get() > 100) {
            TransactionManager.currentOrNull()?.commit()
            counter.set(0)
        }

        VirtualMedicineSamTableModel.COMCLS.insert {
            it[code] = toPersist.code
            it[parent] = parentCode

            it[titleNl] = data.title?.nl
            it[titleFr] = data.title?.fr
            it[titleEnglish] = data.title?.en
            it[titleGerman] = data.title?.de

            it[contentNl] = data.content?.nl
            it[contentFr] = data.content?.fr
            it[contentEnglish] = data.content?.en
            it[contentGerman] = data.content?.de

            it[posologyNoteNl] = data.posologyNote?.nl
            it[posologyNoteFr] = data.posologyNote?.fr
            it[posologyNoteEnglish] = data.posologyNote?.en
            it[posologyNoteGerman] = data.posologyNote?.de

            it[urlNl] = data.url?.nl
            it[urlFr] = data.url?.fr
            it[urlEnglish] = data.url?.en
            it[urlGerman] = data.url?.de

            it[validFrom] = LocalDate.parse(data.from)
            it[validTo] = data.to?.let { d -> LocalDate.parse(d) }
        }
    }
    for (childClassificationElement in toPersist.childClassificationElements) {
        persistCommentedClassification(childClassificationElement, toPersist.code, counter)
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
