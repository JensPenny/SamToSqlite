package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import db.VirtualMedicineSamTableModel
import fullElement
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.CommentedClassificationElement
import pojo.VmpElement
import pojo.VmpGroupElement
import pojo.VtmElement
import tryPersist
import java.io.FileInputStream
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger
import javax.xml.stream.XMLInputFactory

fun parseVmpXml(
    inputFactory: XMLInputFactory, xmlMapper: ObjectMapper, path: String
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
    toPersist: CommentedClassificationElement, parentCode: String?, counter: AtomicInteger
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