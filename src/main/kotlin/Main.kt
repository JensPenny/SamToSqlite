import db.ActualMedicineSamTableModel
import db.createDB
import db.createTables
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import pojo.AMPComponentElement
import pojo.AmpElement
import pojo.TranslatedData
import java.io.FileInputStream
import java.time.LocalDate
import javax.xml.namespace.QName
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.XMLEvent

fun main() {
    createDB()
    createTables()

    val inputFactory = XMLInputFactory.newInstance()
    parseAmpXml(inputFactory)
}

/**
 * Function to parse the AMP data - element
 */
fun amp(
    ampCode: String,
    vmpCode: Int?,
    reader: XMLEventReader
) {
    var event = reader.nextTag() //We peeked to get here, so get the data element as well
    val amp = AmpElement()

    //Each data object is 1 transactional row
    while (!(event.isEndElement && event.asEndElement().name.localPart.equals("Data"))) {
        if (event.isStartElement) {
            val startElement = event.asStartElement()

            when (startElement.name.localPart) {
                "Data" -> {
                    val toAttr = startElement.getAttributeByName(QName("to"))
                    if (toAttr?.value != null) {
                        amp.to = LocalDate.parse(toAttr.value)
                    }

                    val fromAttr = startElement.getAttributeByName(QName("from"))
                    if (fromAttr?.value != null) {
                        amp.from = LocalDate.parse(fromAttr.value)
                    }
                }

                "OfficialName" -> {
                    amp.officialName = reader.elementText
                }

                "Status" -> {
                    amp.status = reader.elementText
                }

                "Name" -> {
                    amp.name = parseTranslation(reader)
                }

                "BlackTriangle" -> {
                    amp.blackTriangle = reader.elementText.toBoolean()
                }

                "MedicineType" -> {
                    amp.medicineType = reader.elementText
                }

                "Company" -> {
                    val actorNrAttr = startElement.getAttributeByName(QName("actorNr"))
                    amp.companyActorNr = actorNrAttr?.value?.toInt()
                }

                else -> {
                    println("AMP: No AMP-handler found! " + startElement.name.localPart)
                }
            }
        }
        //Go to the next event
        event = reader.nextEvent()
    }

    //While loop stops after we encounter a 'data' end element
    //Let's check that and persist the tuple
    if (event.isEndElement && event.asEndElement().name.localPart.equals("Data")) {
        persistAmpFAMHP(ampCode, vmpCode, amp)
    }
}

fun parseTranslation(reader: XMLEventReader): TranslatedData {
    var nextInName = reader.nextTag()
    val nameTranslations = TranslatedData()
    while (!(nextInName.isEndElement && nextInName.asEndElement().name.localPart.equals("Name"))) {
        if (nextInName.isStartElement) {
            when (nextInName.asStartElement().name.localPart) {
                "Nl" -> nameTranslations.nl = reader.elementText
                "Fr" -> nameTranslations.fr = reader.elementText
                "En" -> nameTranslations.en = reader.elementText
                "De" -> nameTranslations.de = reader.elementText
            }
        }
        nextInName = reader.nextTag()
    }
    return nameTranslations
}

fun persistAmpFAMHP(ampCode: String, vmp: Int?, amp: AmpElement) {
    try {
        transaction {
            ActualMedicineSamTableModel.AMP_FAMHP.insert {
                it[code] = ampCode
                if (vmp != null) {
                    it[vmpCode] = vmp
                }
                it[validTo] = amp.to
                it[validFrom] = amp.from!!
                it[officialName] = amp.officialName!!
                it[status] = amp.status!!
                it[nameNl] = amp.name.nl!!
                it[nameFr] = amp.name.fr!!
                it[nameEnglish] = amp.name.en
                it[nameGerman] = amp.name.de
                it[blackTriangle] = amp.blackTriangle
                it[medicineType] = amp.medicineType!!
                it[companyActorNumber] = amp.companyActorNr!!
            }
        }
        println("inserted amp")
    } catch (e: ExposedSQLException) {
        println("Error persisting tuple: " + e.message)
        println("Trace: " + e.stackTrace.toString())
    }

    /*
            ActualMedicineSamTableModel.AMP_BCPI.insert {
                it[code] = amp
            }
    */
}

fun ampComponent(reader: XMLEventReader, event: XMLEvent, ampCode: String) {

    val vmpComponentCode = event.asStartElement().getAttributeByName(QName("vmpComponentCode"))?.value
    val sequenceNumber = event.asStartElement().getAttributeByName(QName("sequenceNr")).value

    var nextEvent = reader.nextEvent()

    var loopOverData = true;
    while (loopOverData) {
        val ampComponent = AMPComponentElement()
        ampComponent.ampCode = ampCode
        ampComponent.vmpcCode = vmpComponentCode
        ampComponent.sequenceNumber = sequenceNumber

        while (!(nextEvent.isEndElement && nextEvent.asEndElement().name.localPart.equals("Data"))) {
            if (nextEvent.isStartElement) {
                val startElement = nextEvent.asStartElement()

                when (startElement.name.localPart) {
                    "Data" -> {
                        val toAttr = startElement.getAttributeByName(QName("to"))
                        if (toAttr?.value != null) {
                            ampComponent.to = LocalDate.parse(toAttr.value)
                        }

                        val fromAttr = startElement.getAttributeByName(QName("from"))
                        if (fromAttr?.value != null) {
                            ampComponent.from = LocalDate.parse(fromAttr.value)
                        }
                    }

                    "PharmaceuticalForm" -> {
                        val codeAttr = startElement.getAttributeByName(QName("code")).value
                        persistAMPCToPharmFormLink(ampComponent, codeAttr)
                    }

                    "RouteOfAdministration" -> {
                        val roaCode = startElement.getAttributeByName(QName("code")).value
                        persistAMPCToRoaLink(ampComponent, roaCode)
                    }
                    "Scored" -> {
                        ampComponent.scored = reader.elementText
                    }
                }
            }
            nextEvent = reader.nextEvent()
        }

        if (nextEvent.isEndElement && nextEvent.asEndElement().name.localPart.equals("Data")) {
            //PersistAmpComponent

            //Check if we need to keep looping for the data elements here
            val peekEvent = reader.peek()
            loopOverData = peekEvent.isStartElement && peekEvent.asStartElement().name.localPart.equals("Data")
        }
    }

    nextEvent = reader.nextEvent()
    while (!(nextEvent.isEndElement && nextEvent.asEndElement().name.localPart.equals("AmpComponent"))) {
        if (nextEvent.isStartElement) {
            val startElement = nextEvent.asStartElement()

            when (startElement.name.localPart) {
                "RealActualIngredient" -> {
                    realActualIngredient(reader, event, ampCode, sequenceNumber)
                }
            }

        }

        nextEvent = reader.nextEvent()
    }

}

fun persistAMPCToRoaLink(ampComponent: AMPComponentElement, codeAttr: String?) {
    try {
        transaction {
            ActualMedicineSamTableModel.AMPC_TO_ROA.insert {
                it[ampCode] = ampComponent.ampCode!!
                it[sequenceNumber] = ampComponent.sequenceNumber!!.toInt()
                it[roaCode] = codeAttr!!
            }
        }
    } catch (e: ExposedSQLException) {
        println("Error persisting tuple: " + e.message)
        println("Trace: " + e.stackTrace.toString())
    }

}


fun persistAMPCToPharmFormLink(ampComponent: AMPComponentElement, codeAttr: String?) {
    try {
        transaction {
            ActualMedicineSamTableModel.AMPC_TO_PHARMFORM.insert {
                it[ampCode] = ampComponent.ampCode!!
                it[sequenceNumber] = ampComponent.sequenceNumber!!.toInt()
                it[pharmaFormCode] = codeAttr!!
            }
        }
    } catch (e: ExposedSQLException) {
        println("Error persisting tuple: " + e.message)
        println("Trace: " + e.stackTrace.toString())
    }

}

fun realActualIngredient(reader: XMLEventReader, event: XMLEvent, ampCode: String, sequenceNumber: String) {
    //todo
}


fun parseAmpXml(inputFactory: XMLInputFactory) {
    //Parse the AMP part
    val path = "res/latest/AMP-1657800909670.xml" //todo make changable
    val reader = inputFactory.createXMLEventReader(FileInputStream(path))

    var amp: String = ""
    var vmp: Int?

    while (reader.hasNext()) {
        val event = reader.nextEvent()

        if (event.isStartElement) {
            val startElement = event.asStartElement()

            when (startElement.name.localPart) {
                "Amp" -> {
                    val codeAttr = startElement.getAttributeByName(QName("code"))
                    val vmpCodeAttr = startElement.getAttributeByName(QName("vmpCode"))

                    amp = codeAttr.value
                    vmp = vmpCodeAttr?.value?.toInt()

                    //The xml can have these random ass \n chars
                    if (reader.peek().isCharacters) {
                        reader.nextEvent()
                    }
                    //if the next tag is a data-element we pass it to the amp data persistor.
                    //We keep checking if there are following data elements to see if we need to persist more
                    while (reader.peek().isStartElement && reader.peek()
                            .asStartElement().name.localPart.equals("Data")
                    ) {
                        amp(amp, vmp, reader)
                    }
                    //Otherwise we continue
                }

                "AmpComponent" -> {
                    if (amp.equals("")) {
                        throw Exception("Invalid! AMP is empty while parsing component")
                    }
                    ampComponent(reader, event, amp)
                }

                else -> {
                    println("no handler for " + startElement.name.localPart)
                }
            }
        }

        //if (nextEvent.isEndElement) {
        //val endElement = nextEvent.asEndElement()
        //if (endElement.name.localPart.equals("Amp")) {
        //Persist the rows and create new ones
        //}
        //}
    }
}

