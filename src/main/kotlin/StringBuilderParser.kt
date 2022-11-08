import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.module.kotlin.readValue
import db.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import parser.parseCompanyXml
import parser.parseCompoundingXml
import parser.parseNonMedicinalXml
import parser.parseReferenceXml
import pojo.*
import xml.createXmlInputFactory
import xml.createXmlMapper
import java.io.FileInputStream
import java.io.StringWriter
import java.security.Timestamp
import java.time.*
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
        val ampParseTime = measureTime { parseAmpXml(inputFactory, xmlMapper, "res/latest/AMP-1667395273070.xml") }

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

        val chapter4Time = measureTime { parseChapter4Xml(inputFactory, xmlMapper, "res/latest/CHAPTERIV-1667395606032.xml") }

        //Pooling logresults to make this stuff more readable
        //logger.info("AMP file parsed in ${ampParseTime.inWholeMinutes}:${ampParseTime.inWholeSeconds - (ampParseTime.inWholeMinutes * 60)}")
        logger.info("CHAPTERIV file parsed in ${chapter4Time.inWholeMinutes}:${chapter4Time.inWholeSeconds - (chapter4Time.inWholeMinutes * 60)}")
        //logger.info("CMP file parsed in ${compoundingTime.inWholeMinutes}:${compoundingTime.inWholeSeconds - (compoundingTime.inWholeMinutes * 60)}")
        //logger.info("CPN file parsed in ${cpnParseTime.inWholeMinutes}:${cpnParseTime.inWholeSeconds - (cpnParseTime.inWholeMinutes * 60)}")
        //logger.info("NONMEDICINAL file parsed in ${nonmedicinalTime.inWholeMinutes}:${nonmedicinalTime.inWholeSeconds - (nonmedicinalTime.inWholeMinutes * 60)}")
        //logger.info("REF file parsed in ${refParseTime.inWholeMinutes}:${refParseTime.inWholeSeconds - (refParseTime.inWholeMinutes * 60)}")
    }
    logger.info("Full export parsed in ${fullTime.inWholeMinutes}:${fullTime.inWholeSeconds - (fullTime.inWholeMinutes * 60)}")

}

fun parseChapter4Xml(inputFactory: XMLInputFactory,
                     xmlMapper: ObjectMapper,
                     path: String) {

    val reader = inputFactory.createXMLEventReader(FileInputStream(path))
    val commitAfterAmount = 100
    var currentCounter = 0

    tryPersist {
        transaction {
            while (reader.hasNext()) {
                val event = reader.nextEvent()

                if (event.isStartElement) {
                    val startElement = event.asStartElement()

                    if (currentCounter >= commitAfterAmount) {
                        commit()
                        currentCounter = 0
                    } else {
                        currentCounter++
                    }

                    when(startElement.name.localPart) {
                        "ns2:Paragraph" -> {
                            val paragraphString = fullElement(startElement, reader)
                            val paragraph = xmlMapper.readValue<Paragraph>(paragraphString)

                            for (paragraphData in paragraph.paragraphData) {
                                currentCounter++
                                Chapter4SamTableModel.PARAGRAPH.insert {
                                    it[chapterName] = paragraph.chapterName
                                    it[paragraphName] = paragraph.paragraphName

                                    it[keyStringNls] = paragraphData.keystringNl
                                    it[keyStringFr] = paragraphData.keystringFr
                                    it[agreementType] = paragraphData.agreementType
                                    it[processType] = paragraphData.processType
                                    //it[legalReference] = paragraphData.l
                                    it[publicationDate] = paragraphData.publicationDate?.let { d -> LocalDate.parse(d) }
                                    it[modificationDate] = paragraphData.modificationDate?.let { d -> LocalDate.parse(d) }
                                    it[processTypeOverrule] = paragraphData.processTypeOverrule
                                    it[agreementTypePro] = paragraphData.agreementTypePro
                                    it[modificationStatus] = paragraphData.modificationStatus

                                    it[validFrom] = LocalDate.parse(paragraphData.from)
                                    if (paragraphData.to != null) {
                                        it[validTo] = LocalDate.parse(paragraphData.to)
                                    }

                                    //Extra createdby-properties
                                    if (paragraphData.createdTimestamp != null) {
                                        it[createdDate] = Instant.parse(paragraphData.createdTimestamp + "Z") //Add zulu time for easy parsing
                                    }
                                    it[createdByUser] = paragraphData.createdUserId
                                }
                            }

                            for (exclusion in paragraph.exclusion) {
                                for (exclusionData in exclusion.exlusionData) {
                                    currentCounter++
                                    Chapter4SamTableModel.EXCLUSION.insert {
                                        it[chapterName] = paragraph.chapterName
                                        it[paragraphName] = paragraph.paragraphName

                                        it[exclusionType] = exclusion.exclusionType
                                        it[identifierNum] = exclusion.identifierNum
                                        it[modificationStatus] = exclusionData.modificationStatus

                                        it[validFrom] = LocalDate.parse(exclusionData.from)
                                        if (exclusionData.to != null) {
                                            it[validTo] = LocalDate.parse(exclusionData.to)
                                        }

                                        //Extra createdby-properties
                                        if (exclusionData.createdTimestamp != null) {
                                            it[createdDate] = Instant.parse(exclusionData.createdTimestamp + "Z") //Add zulu time for easy parsing
                                        }
                                        it[createdByUser] = exclusionData.createdUserId
                                    }
                                }
                            }

                            for (verse in paragraph.verses) {
                                for (verseData in verse.verseData) {
                                    currentCounter++
                                    Chapter4SamTableModel.VERSE.insert {
                                        it[chapterName] = paragraph.chapterName
                                        it[paragraphName] = paragraph.paragraphName

                                        it[verseSequence] = verse.verseSeq.toInt()
                                        it[verseSequenceParent] = verseData.verseSeqParent.toInt()
                                        it[verseLevel] = verseData.verseLevel.toInt()
                                        it[verseType] = verseData.verseType
                                        it[checkBoxIndicator] = verseData.checkBoxInd
                                        it[minCheckNumber] = verseData.minCheckNumber?.toInt()
                                        it[andClauseNumber] = verseData.andClauseNum?.toInt()
                                        it[textNl] = verseData.textNl
                                        it[textFr] = verseData.textFr
                                        it[requestType] = verseData.requestType
                                        it[agreementTerm] = verseData.agreementTerm?.toInt()
                                        it[agreementTermUnit] = verseData.agreementTermUnit
                                        it[maxPackageNumber] = verseData.maxPackageNumber?.toInt()
                                        it[purchasingAdvisorQual] = verseData.purchasingAdvisorQualList
                                        //it[legalReference] = verseData.leg
                                        it[modificationDate] = verseData.modificationDate?.let { d -> LocalDate.parse(d) }
                                        it[agreementYearMax] = verseData.agreementYearMax?.toInt()
                                        it[agreementRenewalMax] = verseData.agreementRenewalMax?.toInt()
                                        it[sexRestricted] = verseData.sexRestricted
                                        it[minimumAgeAuthorized] = verseData.minimumAgeAuthorized?.toInt()
                                        it[minimumAgeAuthorizedUnit] = verseData.minimumAgeAuthorizedUnit
                                        it[maximumAgeAuthorized] = verseData.maximumAgeAuthorized?.toInt()
                                        it[maximumAgeAuthorizedUnit] = verseData.maximumAgeAuthorizedUnit
                                        it[maximumContentQuantity] = verseData.maximumContentQuantity
                                        it[maximumContentUnit] = verseData.maximumContentUnit
                                        it[maximumStrengthQuantity] = verseData.maximumStrengthQuantity
                                        it[maximumStrengthUnit] = verseData.maximumStrengthUnit
                                        it[maximumDurationQuantity] = verseData.maximumDurationQuantity
                                        it[maximumDurationUnit] = verseData.maximumDurationUnit
                                        it[otherAddedDocument] = verseData.otherAddedDocument
                                        it[modificationStatus] = verseData.modificationStatus

                                        it[validFrom] = LocalDate.parse(verseData.from)
                                        if (verseData.to != null) {
                                            it[validTo] = LocalDate.parse(verseData.to)
                                        }

                                        //Extra createdby-properties
                                        if (verseData.createdTimestamp != null) {
                                            it[createdDate] = Instant.parse(verseData.createdTimestamp + "Z") //Add zulu time for easy parsing
                                        }
                                        it[createdByUser] = verseData.createdUserId

                                    }

                                    for (addedDocument in verse.addedDocuments) {
                                        for (documentData in addedDocument.documentData) {
                                            currentCounter++
                                            Chapter4SamTableModel.ADDED_DOCUMENT.insert {
                                                it[chapterName] = paragraph.chapterName
                                                it[paragrapName] = paragraph.paragraphName
                                                it[verseSequence] = verse.verseSeq.toInt()
                                                it[documentSequence] = addedDocument.documentSequence.toInt()
                                                it[nameId] = documentData.nameId.toInt()
                                                it[formTypeId] = documentData.formTypeId.toInt()
                                                it[appendixTypeId] = documentData.appendixTypeId.toInt()
                                                //it[mimeType] = documentData.mim
                                                //it[documentContent] = documentData.doc
                                                it[addressURL] = documentData.addressUrl
                                                it[modificationStatus] = documentData.modificationStatus
                                                it[validFrom] = LocalDate.parse(documentData.from)
                                                if (documentData.to != null) {
                                                    it[validTo] = LocalDate.parse(documentData.to)
                                                }

                                                //Extra createdby-properties
                                                if (documentData.createdTimestamp != null) {
                                                    it[createdDate] = Instant.parse(documentData.createdTimestamp + "Z") //Add zulu time for easy parsing
                                                }
                                                it[createdByUser] = documentData.createdUserId
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        "ns2:QualificationList" -> {
                            val qualificationListString = fullElement(startElement, reader)
                            val qualificationList = xmlMapper.readValue<QualificationList>(qualificationListString)

                            for (qualificationListData in qualificationList.qualificationListData) {
                                currentCounter++
                                Chapter4SamTableModel.QUALLIST.insert {
                                    it[Chapter4SamTableModel.QUALLIST.qualificationList] = qualificationList.qualificationListId
                                    it[nameId] = qualificationListData.nameId.toInt()
                                    it[exclusiveInd] = qualificationListData.exclusiveInd
                                    it[modificationStatus] = qualificationListData.modificationStatus

                                    it[validFrom] = LocalDate.parse(qualificationListData.from)
                                    if (qualificationListData.to != null) {
                                        it[validTo] = LocalDate.parse(qualificationListData.to)
                                    }

                                    //Extra createdby-properties
                                    if (qualificationListData.createdTimestamp != null) {
                                        it[createdDate] = Instant.parse(qualificationListData.createdTimestamp + "Z") //Add zulu time for easy parsing
                                    }
                                    it[createdByUser] = qualificationListData.createdUserId
                                }
                            }

                            for (professionalAuthorisation in qualificationList.professionalAuthorisations) {
                                for (professionalAuthorisationData in professionalAuthorisation.professionalAuthorisationData) {
                                    currentCounter++
                                    Chapter4SamTableModel.PROF_AUTHORISATION.insert {
                                        it[professionalAuthorisationId] = professionalAuthorisation.professionalAuthorisationId.toInt()
                                        it[Chapter4SamTableModel.PROF_AUTHORISATION.qualificationList] = qualificationList.qualificationListId
                                        it[professionalCv] = professionalAuthorisationData.professionalCv
                                        //it[purchasingAdvisorName] = professionalAuthorisationData.purchasingAdvisorName
                                        it[modificationStatus] = professionalAuthorisationData.modificationStatus

                                        it[validFrom] = LocalDate.parse(professionalAuthorisationData.from)
                                        if (professionalAuthorisationData.to != null) {
                                            it[validTo] = LocalDate.parse(professionalAuthorisationData.to)
                                        }

                                        //Extra createdby-properties
                                        if (professionalAuthorisationData.createdTimestamp != null) {
                                            it[createdDate] = Instant.parse(professionalAuthorisationData.createdTimestamp + "Z") //Add zulu time for easy parsing
                                        }
                                        it[createdByUser] = professionalAuthorisationData.createdUserId
                                    }
                                }
                            }
                        }
                        "ns2:NameExplanation" -> {
                            val nameExplanationString = fullElement(startElement, reader)
                            val nameExplanation = xmlMapper.readValue<NameExplanation>(nameExplanationString)


                            currentCounter++
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
