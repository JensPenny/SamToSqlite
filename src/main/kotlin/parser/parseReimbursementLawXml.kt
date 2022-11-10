package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import db.ReimbursementLawSamTableModel
import fullElement
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.LegalBasis
import pojo.LegalText
import tryPersist
import java.io.FileInputStream
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger
import javax.xml.stream.XMLInputFactory

fun parseReimbursementLawXml(
    inputFactory: XMLInputFactory, xmlMapper: ObjectMapper, path: String
) {
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))
    val commitAfterAmount =
        100 //Doesn't really matter - shitloads of recursive elements, so placing this here isn't really useful
    var currentCounter = AtomicInteger(0)

    tryPersist {
        transaction {
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
                                    val secondReferencePath =
                                        "${legalBasis.key}-${legalReference.key}-${secondReference.key}"
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
                                            it[firstPublishedOn] =
                                                data.firstPublishedOn?.let { d -> LocalDate.parse(d) }
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
                                        insertRecursiveLegalText(
                                            legalText,
                                            legalText.key,
                                            secondReferencePath,
                                            legalBasis.key,
                                            legalReference.key,
                                            secondReference.key,
                                            currentCounter
                                        )
                                    }
                                }

                                for (legalText in legalReference.legalTexts) {
                                    insertRecursiveLegalText(
                                        legalText,
                                        legalText.key,
                                        firstReferencePath,
                                        legalBasis.key,
                                        legalReference.key,
                                        null,
                                        currentCounter
                                    )
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

/**
 * Legal texts are recursive, what makes this...challenging to implement
 */
private fun insertRecursiveLegalText(
    currentText: LegalText,
    legalTxtPath: String,
    legalRefPath: String,
    legalBasisKey: String,
    legalRef1Key: String,
    legalRef2Key: String?,
    counter: AtomicInteger
) {
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
        insertRecursiveLegalText(
            legalText,
            "$legalTxtPath ${legalText.key}",
            legalRefPath,
            legalBasisKey,
            legalRef1Key,
            legalRef2Key,
            counter
        )
    }
}