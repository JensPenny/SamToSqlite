package pojo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xml.createXmlInputFactory
import xml.createXmlMapper
import javax.xml.stream.XMLInputFactory

/**
 * An AMPP is an element of an AMP, but the object is complex enough to warrant its own tests
 */
class AmppElementTest {

    private lateinit var inputFactory: XMLInputFactory
    private lateinit var xmlMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        inputFactory = createXmlInputFactory()
        xmlMapper = createXmlMapper(inputFactory)
    }

    @Test
    fun testAmppElementAndData() {
        val frenchLeafLink = "https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7"
        val spcNlLink = "https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7"

        val ampp = xmlMapper.readValue<AMPPElement>(amppXml)

        assertEquals("478284-01", ampp.ctiExtended)
        assertEquals(8, ampp.amppDataBlocks.size)
        assertEquals(1, ampp.amppComponents.size)
        assertEquals(3, ampp.dmpps.size)
        assertEquals(2, ampp.commercialization[0].commercializationDataBlocks.size) //todo Bit weird - should check if always one with multiple data
        assertEquals(1, ampp.supplyProblems.size)

        //Test a single (the active) data block as well
        val amppData = ampp.amppDataBlocks[5]
        assertEquals("2022-06-01", amppData.from)
        assertNull(amppData.to)
        assertEquals("BE478284", amppData.authorisationNr)
        assertEquals(false, amppData.orphan)
        assertEquals(frenchLeafLink, amppData.leafletLink!!.fr)
        assertEquals(spcNlLink, amppData.spcLink!!.nl)
        assertEquals("0", amppData.parallelCircuit)
        assertEquals("100 ml", amppData.packDisplayValue)
        assertEquals("AUTHORIZED", amppData.status)
        assertEquals("Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml", amppData.prescriptionNameFamhp!!.de)
        assertEquals("03700386800283", amppData.fmdProductCode)
        assertEquals(true, amppData.fmdInScope)
        assertEquals(true, amppData.antiTamperingDevicePresent)
        assertEquals("J06BA02", amppData.atcCodeReference[0].codeReference)
        assertEquals("M44", amppData.deliveryModusReference.codeReference)
        assertEquals("H", amppData.deliveryModusSpecReference!!.codeReference)
        assertEquals("Iqymune sol. perf. i.v. [flac.] 1 x 10 g / 100 ml", amppData.abbreviatedName!!.fr)
        assertEquals("Iqymune sol. perf. i.v. [flac.] 1x 10g/100ml", amppData.prescriptionName!!.fr) //Pretty funny that the abbreviated name is longer than the prescriptionname
        assertEquals("http://www.cbip.be/fr/contents/jump?amppid=160267", amppData.crmLink!!.fr)
        assertEquals("566.3200", amppData.exFactoryPrice)
    }

    @Test
    fun testSingleAmppComponent() {
        val ampp = xmlMapper.readValue<AMPPElement>(amppXml)
        val amppComponent = ampp.amppComponents[0]

        assertEquals("1", amppComponent.sequenceNumber)
        assertEquals(1, amppComponent.amppComponentDataBlocks.size)
        assertEquals(1, amppComponent.ampComponentEquivalents.size)

        val amppComponentData = amppComponent.amppComponentDataBlocks[0]
        assertEquals("2015-09-25", amppComponentData.from)
        assertNull(amppComponentData.to)
        assertEquals("1", amppComponentData.ampcSequenceNr)
        assertEquals("ACTIVE_COMPONENT", amppComponentData.contentType)
        assertEquals("26", amppComponentData.packagingType.codeReference)

        val equivalent = amppComponent.ampComponentEquivalents[0]
        assertEquals("1", equivalent.sequenceNumber)
        val equivalentData = equivalent.amppComponentEquivalentDataBlocks[0]
        assertEquals("2015-09-25", equivalentData.from)
        assertNull(equivalentData.to)
        assertEquals("mL", equivalentData.content!!.unit)
        assertEquals("100.0000", equivalentData.content!!.Content)
    }

    @Test
    fun testSingleDmpp() {
        val ampp = xmlMapper.readValue<AMPPElement>(amppXml)
        val dmpp = ampp.dmpps[2]

        assertEquals("drfGOPWJcLLfNA5X1jH+fJn2QmNorrRV2JsSPZ9dTbQ=", dmpp.productId)
        assertEquals("A", dmpp.deliveryEnvironment)
        assertEquals("7722168", dmpp.code)
        assertEquals("CNK", dmpp.codeSystem)
        assertEquals(4, dmpp.dmppDataBlocks.size)

        val dmppData = dmpp.dmppDataBlocks[0]
        assertEquals("2020-11-01", dmppData.from)
        assertEquals("2021-05-31", dmppData.to)
        assertEquals("435.4600", dmppData.price)
        assertEquals(false, dmppData.cheap) //Weird that cheap was false and cheapest was true..
        assertEquals(true, dmppData.cheapest)
        assertEquals(true, dmppData.reimbursable)
    }

/*  We don't test commercialization, because this looks to be empty.
    This is explained in the documentation
    @Test
    fun testCommercialization(){

    }
*/

    @Test
    fun testSupplyProblem() {
        val ampp = xmlMapper.readValue<AMPPElement>(amppXml)
        val supplyProblem = ampp.supplyProblems[0].supplyDataBlocks[0]

        //What does this mean? Is there still a supply problem?
        assertEquals("2021-07-01", supplyProblem.from)
        assertEquals("2022-06-30", supplyProblem.expectedEnd)
        assertEquals("Company", supplyProblem.reportedBy)
        assertEquals("Andere reden", supplyProblem.reason!!.nl)
        assertEquals(5134, supplyProblem.additionalInformation!!.fr!!.length)
    }

    //The test-ampp contains a supplyproblem and other fun stuff to check the boundaries of the mapping
    var amppXml = """
        <ns4:Ampp ctiExtended="478284-01">
            <ns4:Data from="2015-09-25" to="2018-03-31">
                <AuthorisationNr>BE478284</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537b1e5c015ab3b33f13?version=7</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc53781e5c015ab3b33805?version=7</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc537c1e5c015ab3b34368?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>100 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Iqymune 100 mg/ml sol. perf. i.v. flac. 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune 100 mg/ml inf. opl. i.v. flac. 100 ml</ns2:Nl>
                    <ns2:De>Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml</ns2:De>
                    <ns2:En>Iqymune 100 mg/ml inf. sol. i.v. vial 100 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>03700386800283</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="J06BA02">
                    <ns5:Description>Immunoglobulins, Normal Human, for Intravascular Adm.</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M44">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale restreinte H - Art 6, §1 bis, troisième alinéa, quatrième tiret and neuvième alinéa, 4) de la Loi du 25/03/1964 + Art 63 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Beperkt medisch voorschrift H - Art 6, §1 bis, derde alinea, vierde streepje en negende alinea, 4) van de Wet van 25/03/1964 + Art 63 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Beschränkt verschreibungspflichtige H - Art. 6, §1 bis, Absatz 3, vierter Gedankenstrich und Absatz 9, 4) des Gesetzes vom 25.03.1964 + Art. 63 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Restricted medical prescription H - Art 6, §1 bis, 3rd paragraph, 4th indent and 9th paragraph, 4) Law 25/03/1964 + Art 63 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <ns4:DeliveryModusSpecification code="H">
                    <ns5:Description>
                        <ns2:Fr>Délivrance en pharmacie hospitalière</ns2:Fr>
                        <ns2:Nl>Aflevering in ziekenhuisapotheek</ns2:Nl>
                        <ns2:De>Abgabe in der Krankenhausapotheke</ns2:De>
                        <ns2:En>Delivery in hospital pharmacy</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModusSpecification>
            </ns4:Data>
            <ns4:Data from="2021-06-01" to="2021-06-21">
                <AuthorisationNr>BE478284</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537b1e5c015ab3b33f13?version=7</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc53781e5c015ab3b33805?version=7</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc537c1e5c015ab3b34368?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>100 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Iqymune 100 mg/ml sol. perf. i.v. flac. 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune 100 mg/ml inf. opl. i.v. flac. 100 ml</ns2:Nl>
                    <ns2:De>Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml</ns2:De>
                    <ns2:En>Iqymune 100 mg/ml inf. sol. i.v. vial 100 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>03700386800283</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="J06BA02">
                    <ns5:Description>Immunoglobulins, Normal Human, for Intravascular Adm.</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M44">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale restreinte H - Art 6, §1 bis, troisième alinéa, quatrième tiret and neuvième alinéa, 4) de la Loi du 25/03/1964 + Art 63 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Beperkt medisch voorschrift H - Art 6, §1 bis, derde alinea, vierde streepje en negende alinea, 4) van de Wet van 25/03/1964 + Art 63 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Beschränkt verschreibungspflichtige H - Art. 6, §1 bis, Absatz 3, vierter Gedankenstrich und Absatz 9, 4) des Gesetzes vom 25.03.1964 + Art. 63 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Restricted medical prescription H - Art 6, §1 bis, 3rd paragraph, 4th indent and 9th paragraph, 4) Law 25/03/1964 + Art 63 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <ns4:DeliveryModusSpecification code="H">
                    <ns5:Description>
                        <ns2:Fr>Délivrance en pharmacie hospitalière</ns2:Fr>
                        <ns2:Nl>Aflevering in ziekenhuisapotheek</ns2:Nl>
                        <ns2:De>Abgabe in der Krankenhausapotheke</ns2:De>
                        <ns2:En>Delivery in hospital pharmacy</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModusSpecification>
                <ExFactoryPrice>457.4800</ExFactoryPrice>
            </ns4:Data>
            <ns4:Data from="2018-11-12" to="2020-09-30">
                <AuthorisationNr>BE478284</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537b1e5c015ab3b33f13?version=7</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc53781e5c015ab3b33805?version=7</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc537c1e5c015ab3b34368?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>100 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Iqymune 100 mg/ml sol. perf. i.v. flac. 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune 100 mg/ml inf. opl. i.v. flac. 100 ml</ns2:Nl>
                    <ns2:De>Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml</ns2:De>
                    <ns2:En>Iqymune 100 mg/ml inf. sol. i.v. vial 100 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>03700386800283</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="J06BA02">
                    <ns5:Description>Immunoglobulins, Normal Human, for Intravascular Adm.</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M44">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale restreinte H - Art 6, §1 bis, troisième alinéa, quatrième tiret and neuvième alinéa, 4) de la Loi du 25/03/1964 + Art 63 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Beperkt medisch voorschrift H - Art 6, §1 bis, derde alinea, vierde streepje en negende alinea, 4) van de Wet van 25/03/1964 + Art 63 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Beschränkt verschreibungspflichtige H - Art. 6, §1 bis, Absatz 3, vierter Gedankenstrich und Absatz 9, 4) des Gesetzes vom 25.03.1964 + Art. 63 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Restricted medical prescription H - Art 6, §1 bis, 3rd paragraph, 4th indent and 9th paragraph, 4) Law 25/03/1964 + Art 63 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <ns4:DeliveryModusSpecification code="H">
                    <ns5:Description>
                        <ns2:Fr>Délivrance en pharmacie hospitalière</ns2:Fr>
                        <ns2:Nl>Aflevering in ziekenhuisapotheek</ns2:Nl>
                        <ns2:De>Abgabe in der Krankenhausapotheke</ns2:De>
                        <ns2:En>Delivery in hospital pharmacy</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModusSpecification>
                <AbbreviatedName>
                    <ns2:Fr>Iqymune sol. perf. i.v. [flac.] 1 x 10 g / 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune inf. oploss. i.v. [flac.] 1 x 10 g / 100 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Iqymune sol. perf. i.v. [flac.] 1x 10g/100ml</ns2:Fr>
                    <ns2:Nl>Iqymune inf. oploss. i.v. [flac.] 1x 10g/100ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=160267</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=160267</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>404.1000</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
            </ns4:Data>
            <ns4:Data from="2020-10-01" to="2021-05-30">
                <AuthorisationNr>BE478284</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537b1e5c015ab3b33f13?version=7</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc53781e5c015ab3b33805?version=7</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc537c1e5c015ab3b34368?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>100 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Iqymune 100 mg/ml sol. perf. i.v. flac. 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune 100 mg/ml inf. opl. i.v. flac. 100 ml</ns2:Nl>
                    <ns2:De>Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml</ns2:De>
                    <ns2:En>Iqymune 100 mg/ml inf. sol. i.v. vial 100 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>03700386800283</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="J06BA02">
                    <ns5:Description>Immunoglobulins, Normal Human, for Intravascular Adm.</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M44">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale restreinte H - Art 6, §1 bis, troisième alinéa, quatrième tiret and neuvième alinéa, 4) de la Loi du 25/03/1964 + Art 63 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Beperkt medisch voorschrift H - Art 6, §1 bis, derde alinea, vierde streepje en negende alinea, 4) van de Wet van 25/03/1964 + Art 63 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Beschränkt verschreibungspflichtige H - Art. 6, §1 bis, Absatz 3, vierter Gedankenstrich und Absatz 9, 4) des Gesetzes vom 25.03.1964 + Art. 63 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Restricted medical prescription H - Art 6, §1 bis, 3rd paragraph, 4th indent and 9th paragraph, 4) Law 25/03/1964 + Art 63 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <ns4:DeliveryModusSpecification code="H">
                    <ns5:Description>
                        <ns2:Fr>Délivrance en pharmacie hospitalière</ns2:Fr>
                        <ns2:Nl>Aflevering in ziekenhuisapotheek</ns2:Nl>
                        <ns2:De>Abgabe in der Krankenhausapotheke</ns2:De>
                        <ns2:En>Delivery in hospital pharmacy</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModusSpecification>
                <AbbreviatedName>
                    <ns2:Fr>Iqymune sol. perf. i.v. [flac.] 1 x 10 g / 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune inf. oploss. i.v. [flac.] 1 x 10 g / 100 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Iqymune sol. perf. i.v. [flac.] 1x 10g/100ml</ns2:Fr>
                    <ns2:Nl>Iqymune inf. oploss. i.v. [flac.] 1x 10g/100ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=160267</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=160267</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>404.1000</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
            </ns4:Data>
            <ns4:Data from="2018-04-01" to="2018-11-11">
                <AuthorisationNr>BE478284</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537b1e5c015ab3b33f13?version=7</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc53781e5c015ab3b33805?version=7</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc537c1e5c015ab3b34368?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>100 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Iqymune 100 mg/ml sol. perf. i.v. flac. 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune 100 mg/ml inf. opl. i.v. flac. 100 ml</ns2:Nl>
                    <ns2:De>Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml</ns2:De>
                    <ns2:En>Iqymune 100 mg/ml inf. sol. i.v. vial 100 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>03700386800283</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="J06BA02">
                    <ns5:Description>Immunoglobulins, Normal Human, for Intravascular Adm.</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M44">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale restreinte H - Art 6, §1 bis, troisième alinéa, quatrième tiret and neuvième alinéa, 4) de la Loi du 25/03/1964 + Art 63 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Beperkt medisch voorschrift H - Art 6, §1 bis, derde alinea, vierde streepje en negende alinea, 4) van de Wet van 25/03/1964 + Art 63 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Beschränkt verschreibungspflichtige H - Art. 6, §1 bis, Absatz 3, vierter Gedankenstrich und Absatz 9, 4) des Gesetzes vom 25.03.1964 + Art. 63 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Restricted medical prescription H - Art 6, §1 bis, 3rd paragraph, 4th indent and 9th paragraph, 4) Law 25/03/1964 + Art 63 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <ns4:DeliveryModusSpecification code="H">
                    <ns5:Description>
                        <ns2:Fr>Délivrance en pharmacie hospitalière</ns2:Fr>
                        <ns2:Nl>Aflevering in ziekenhuisapotheek</ns2:Nl>
                        <ns2:De>Abgabe in der Krankenhausapotheke</ns2:De>
                        <ns2:En>Delivery in hospital pharmacy</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModusSpecification>
                <ExFactoryPrice>404.1000</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
            </ns4:Data>
            <ns4:Data from="2022-06-01">
                <AuthorisationNr>BE478284</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537b1e5c015ab3b33f13?version=7</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc53781e5c015ab3b33805?version=7</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc537c1e5c015ab3b34368?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>100 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Iqymune 100 mg/ml sol. perf. i.v. flac. 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune 100 mg/ml inf. opl. i.v. flac. 100 ml</ns2:Nl>
                    <ns2:De>Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml</ns2:De>
                    <ns2:En>Iqymune 100 mg/ml inf. sol. i.v. vial 100 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>03700386800283</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="J06BA02">
                    <ns5:Description>Immunoglobulins, Normal Human, for Intravascular Adm.</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M44">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale restreinte H - Art 6, §1 bis, troisième alinéa, quatrième tiret and neuvième alinéa, 4) de la Loi du 25/03/1964 + Art 63 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Beperkt medisch voorschrift H - Art 6, §1 bis, derde alinea, vierde streepje en negende alinea, 4) van de Wet van 25/03/1964 + Art 63 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Beschränkt verschreibungspflichtige H - Art. 6, §1 bis, Absatz 3, vierter Gedankenstrich und Absatz 9, 4) des Gesetzes vom 25.03.1964 + Art. 63 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Restricted medical prescription H - Art 6, §1 bis, 3rd paragraph, 4th indent and 9th paragraph, 4) Law 25/03/1964 + Art 63 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <ns4:DeliveryModusSpecification code="H">
                    <ns5:Description>
                        <ns2:Fr>Délivrance en pharmacie hospitalière</ns2:Fr>
                        <ns2:Nl>Aflevering in ziekenhuisapotheek</ns2:Nl>
                        <ns2:De>Abgabe in der Krankenhausapotheke</ns2:De>
                        <ns2:En>Delivery in hospital pharmacy</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModusSpecification>
                <AbbreviatedName>
                    <ns2:Fr>Iqymune sol. perf. i.v. [flac.] 1 x 10 g / 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune inf. oploss. i.v. [flac.] 1 x 10 g / 100 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Iqymune sol. perf. i.v. [flac.] 1x 10g/100ml</ns2:Fr>
                    <ns2:Nl>Iqymune inf. oploss. i.v. [flac.] 1x 10g/100ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=160267</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=160267</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>566.3200</ExFactoryPrice>
            </ns4:Data>
            <ns4:Data from="2021-05-31" to="2021-05-31">
                <AuthorisationNr>BE478284</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537b1e5c015ab3b33f13?version=7</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc53781e5c015ab3b33805?version=7</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc537c1e5c015ab3b34368?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>100 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Iqymune 100 mg/ml sol. perf. i.v. flac. 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune 100 mg/ml inf. opl. i.v. flac. 100 ml</ns2:Nl>
                    <ns2:De>Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml</ns2:De>
                    <ns2:En>Iqymune 100 mg/ml inf. sol. i.v. vial 100 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>03700386800283</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="J06BA02">
                    <ns5:Description>Immunoglobulins, Normal Human, for Intravascular Adm.</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M44">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale restreinte H - Art 6, §1 bis, troisième alinéa, quatrième tiret and neuvième alinéa, 4) de la Loi du 25/03/1964 + Art 63 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Beperkt medisch voorschrift H - Art 6, §1 bis, derde alinea, vierde streepje en negende alinea, 4) van de Wet van 25/03/1964 + Art 63 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Beschränkt verschreibungspflichtige H - Art. 6, §1 bis, Absatz 3, vierter Gedankenstrich und Absatz 9, 4) des Gesetzes vom 25.03.1964 + Art. 63 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Restricted medical prescription H - Art 6, §1 bis, 3rd paragraph, 4th indent and 9th paragraph, 4) Law 25/03/1964 + Art 63 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <ns4:DeliveryModusSpecification code="H">
                    <ns5:Description>
                        <ns2:Fr>Délivrance en pharmacie hospitalière</ns2:Fr>
                        <ns2:Nl>Aflevering in ziekenhuisapotheek</ns2:Nl>
                        <ns2:De>Abgabe in der Krankenhausapotheke</ns2:De>
                        <ns2:En>Delivery in hospital pharmacy</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModusSpecification>
                <ExFactoryPrice>404.1000</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
            </ns4:Data>
            <ns4:Data from="2021-06-22" to="2022-05-31">
                <AuthorisationNr>BE478284</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc53791e5c015ab3b33a24?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537b1e5c015ab3b33f13?version=7</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc53781e5c015ab3b33805?version=7</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc537c1e5c015ab3b34368?version=7</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc537d1e5c015ab3b34707?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>100 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Iqymune 100 mg/ml sol. perf. i.v. flac. 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune 100 mg/ml inf. opl. i.v. flac. 100 ml</ns2:Nl>
                    <ns2:De>Iqymune 100 mg/ml Inf-Lös. i.v. Dstfl. 100 ml</ns2:De>
                    <ns2:En>Iqymune 100 mg/ml inf. sol. i.v. vial 100 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>03700386800283</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="J06BA02">
                    <ns5:Description>Immunoglobulins, Normal Human, for Intravascular Adm.</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M44">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale restreinte H - Art 6, §1 bis, troisième alinéa, quatrième tiret and neuvième alinéa, 4) de la Loi du 25/03/1964 + Art 63 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Beperkt medisch voorschrift H - Art 6, §1 bis, derde alinea, vierde streepje en negende alinea, 4) van de Wet van 25/03/1964 + Art 63 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Beschränkt verschreibungspflichtige H - Art. 6, §1 bis, Absatz 3, vierter Gedankenstrich und Absatz 9, 4) des Gesetzes vom 25.03.1964 + Art. 63 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Restricted medical prescription H - Art 6, §1 bis, 3rd paragraph, 4th indent and 9th paragraph, 4) Law 25/03/1964 + Art 63 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <ns4:DeliveryModusSpecification code="H">
                    <ns5:Description>
                        <ns2:Fr>Délivrance en pharmacie hospitalière</ns2:Fr>
                        <ns2:Nl>Aflevering in ziekenhuisapotheek</ns2:Nl>
                        <ns2:De>Abgabe in der Krankenhausapotheke</ns2:De>
                        <ns2:En>Delivery in hospital pharmacy</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModusSpecification>
                <AbbreviatedName>
                    <ns2:Fr>Iqymune sol. perf. i.v. [flac.] 1 x 10 g / 100 ml</ns2:Fr>
                    <ns2:Nl>Iqymune inf. oploss. i.v. [flac.] 1 x 10 g / 100 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Iqymune sol. perf. i.v. [flac.] 1x 10g/100ml</ns2:Fr>
                    <ns2:Nl>Iqymune inf. oploss. i.v. [flac.] 1x 10g/100ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=160267</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=160267</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>457.4800</ExFactoryPrice>
            </ns4:Data>
            <ns4:AmppComponent sequenceNr="1">
                <ns4:Data from="2015-09-25">
                    <AmpcSequenceNr>1</AmpcSequenceNr>
                    <ContentType>ACTIVE_COMPONENT</ContentType>
                    <ns4:PackagingType code="26">
                        <ns5:Name>
                            <ns2:Fr>Flacon</ns2:Fr>
                            <ns2:Nl>Injectieflacon</ns2:Nl>
                            <ns2:De>Durchstechflasche</ns2:De>
                            <ns2:En>Vial</ns2:En>
                        </ns5:Name>
                        <ns5:EdqmCode>30069000</ns5:EdqmCode>
                        <ns5:EdqmDefinition>Small container for parenteral medicinal products, with a stopper and overseal; the contents are removed after piercing the stopper. Single-dose and multidose uses are included.</ns5:EdqmDefinition>
                    </ns4:PackagingType>
                </ns4:Data>
                <ns4:AmppComponentEquivalent sequenceNr="1">
                    <ns4:Data from="2015-09-25">
                        <Content unit="mL">100.0000</Content>
                    </ns4:Data>
                </ns4:AmppComponentEquivalent>
            </ns4:AmppComponent>
            <ns4:Commercialization>
                <ns4:Data from="2018-11-01" to="2021-05-17"/>
                <ns4:Data from="2021-06-07"/>
            </ns4:Commercialization>
            <ns4:SupplyProblem>
                <ns4:Data from="2021-07-01">
                    <ExpectedEndOn>2022-06-30</ExpectedEndOn>
                    <ReportedBy>Company</ReportedBy>
                    <Reason>
                        <ns2:Fr>Autres motifs</ns2:Fr>
                        <ns2:Nl>Andere reden</ns2:Nl>
                        <ns2:De>Sonstiger Grund</ns2:De>
                        <ns2:En>Other reason</ns2:En>
                    </Reason>
                    <AdditionalInformation>
                        <ns2:Fr>21.12.2021
Les immunoglobulines sous-cutanées étant disponibles en quantité réduite dans le monde, la situation est critique en Belgique également vu la disponibilité réduite. L’AFMPS émet des recommandations destinées aux pharmaciens (hospitaliers), aux médecins (spécialistes) et aux patients.
Depuis juillet 2021, la quantité disponible d’immunoglobulines sous-cutanées (IgSC) est réduite. Ce phénomène s’explique par la baisse mondiale de collecte de plasma, le besoin sans cesse croissant d’immunoglobulines et la dépendance au plasma provenant de l’étranger. Cette pénurie est ressentie en permanence mais la crise de la COVID-19 l’a encore aggravée.

Une task force rassemblant des médecins (spécialistes), des pharmaciens (hospitaliers), des associations de patients et les autorités compétentes a été créée pour trouver une solution à très court terme.

À court terme, la solution consiste à importer des conditionnements d’IgSC pour les patients atteints de déficit immunitaire primaire (DIP). Ces conditionnements seront disponibles dans le courant du mois de janvier 2022 et ils seront remboursés pour les patients en question. En outre, depuis octobre 2021, une quantité supplémentaire d’immunoglobulines intraveineuses (IgIV) à base de plasma belge a été mise à disposition pour une durée de douze mois. L’AFMPS espère que grâce à ces quantités supplémentaires et aux recommandations ci-dessous, chaque patient pourra être aidé au mieux.

Recommandations à l’intention des pharmaciens (hospitaliers) et des médecins (spécialistes):

Pour garantir la disponibilité de ces médicaments essentiels pour les patients les plus vulnérables, la task force a fixé les priorités ci-dessous et formulé les recommandations suivantes :

	· les IgSC doivent être réservées aux patients qui ne peuvent pas passer aux formes intraveineuses, à savoir :
			- les enfants atteints de DIP,
			- les patients utilisant actuellement des IgSC qui ne peuvent pas passer à des IgIV.
			
Quand c’est possible, les patients utilisant actuellement des Ig administrées par voie sous-cutanées qui sont actuellement en pénurie critique doivent être orientés vers les formes intraveineuses. L’objectif visé est une réduction de 14 % environ de la consommation totale d’IgSC. Le médecin traitant décide au cas par cas, en fonction du risque estimé pour le patient en question ainsi que de l’efficacité et des effets indésirables attendus.

	· Les IgIV doivent être réservées en priorité aux indications suivantes : 
			- DIP chez l’adulte et chez l’enfant,
			- hypogammaglobulinémie secondaire chez des patients atteints de leucémie lymphoïde chronique (LLC), de myélome multiple (MM) ou ayant subi un traitement pour tumeur maligne du tissu lymphoïde, qui contractent des infections à répétition, ou ayant subi une allogreffe de cellules souches ou une thérapie par cellules CAR-T,
			- purpura thrombopénique idiopathique (PTI) de l’enfant et de l’adulte requérant un traitement urgent, par exemple en cas d’hémorragies aiguës, avec contre-indication relatives aux galéniques, propriétés pharmacologiques  aux corticostéroïdes ou maladie réfractaire aux corticostéroïdes,
			- syndrome de Guillain-Barré,
			- maladie de Kawasaki,
			- neuropathie motrice multifocale (NMM).
			- les patients utilisant des IgSC actuellement en pénurie critique doivent être orientés vers les IgIV.
			
	· Les indications suivantes sont considérées comme moins prioritaires et des alternatives doivent être envisagées :
			- Purpura thrombocytopénique idiopathique (PTI) de l’adulte. Des traitements alternatifs doivent être envisagés,  par exemple des agonistes du récepteur de la thrombopoïétine (TPO) à partir du 1er janvier 2022*,
			- Polyneuropathie inflammatoire démyélinisante chronique (PIDC). Des traitements alternatifs doivent être envisagés,  par exemple la plasmaphérèse, des corticostéroïdes à forte dose et l’immunosuppression*.			
* Le médecin traitant décide pour chaque patient, en fonction du risque estimé pour le patient en question ainsi que de l’efficacité et des effets indésirables attendus.

	· Il est recommandé aux médecins (spécialistes) d’évaluer la dose utilisée avec un regard critique afin d’utiliser pour chaque patient la dose efficace la plus faible possible.
	· En outre, l’AFMPS demande instamment aux pharmaciens (hospitaliers) de ne pas conserver de stock d’IgSC dans la pharmacie et de délivrer aux patients le médicament pour une durée de quatre semaines maximum. L’objectif est que la majeure partie du stock actuel d’IgSC reste chez les grossistes-répartiteurs afin qu’en cette période de pénurie aiguë, chaque patient puisse obtenir le médicament dont il a besoin.
	· La task force rappelle également que l’utilisation hors indication (« off label ») doit être limitée au maximum.
	 
Entretemps, l’AFMPS suit la situation de près. Elle collabore avec l’INAMI, le SPF santé publique, Sécurité de la chaîne alimentaire et Environnement, le SPF Économie et la cellule stratégique du ministre compétent pour trouver des solutions supplémentaires à très court terme ainsi que des solutions structurelles à moyen et long terme.</ns2:Fr>
                        <ns2:Nl>21.12.2021
Door een wereldwijd verminderde beschikbaarheid van subcutane immunoglobulines is er ook in België een kritieke beperkte beschikbaarheid. Het FAGG geeft aanbevelingen voor (ziekenhuis)apothekers, artsen(-specialisten) en patiënten.
Sinds juli 2021 zijn er minder subcutane immunoglobulines (SCIg’s) ter beschikking. De reden hiervoor is de wereldwijde verminderde plasmacollectie, de altijd groeiende nood aan immunoglobulines en de afhankelijkheid van buitenlands plasma. Het tekort is aanhoudend voelbaar maar is versterkt door de COVID-19-problematiek.

Om op zeer korte termijn een oplossing te zoeken, werd een taskforce georganiseerd met artsen(-specialisten), (ziekenhuis)apothekers, patiëntenverenigingen en bevoegde overheden.

Een oplossing op korte termijn is de invoer van buitenlandse verpakkingen van SCIg’s voor patiënten met primaire immuundeficiënties (PID’s). Deze verpakkingen zullen in de loop van de maand januari 2022 ter beschikking zijn en worden terugbetaald voor deze patiënten. Verder wordt er sinds oktober 2021 een extra hoeveelheid intraveneuze immunoglobulines (IVIg’s) op basis van Belgisch plasma beschikbaar gesteld en dit gedurende twaalf maanden. Met deze extra hoeveelheden en onderstaande aanbevelingen, hoopt het FAGG dat elke patiënt zo goed mogelijk kan worden geholpen.

Aanbevelingen ter attentie van (ziekenhuis)apothekers en artsen(-specialisten):

Om de beschikbaarheid van deze essentiële geneesmiddelen zo goed mogelijk voor de meest kwetsbare patiënten te vrijwaren, heeft de taskforce volgende prioritisatie en aanbevelingen opgesteld:

	• subcutane immunoglobulines moeten worden voorbehouden voor patiënten die niet kunnen overschakelen naar intraveneuze toediening:
		- pediatrische PID-patiënten,
		- patiënten die momenteel SCIg’s gebruiken en niet kunnen overschakelen naar IVIg’s.

Wanneer mogelijk worden patiënten, die momenteel SCIg’s gebruiken, in de huidige periode van een kritiek tekort aan subcutane vormen overgeschakeld naar intraveneuze toediening. Er wordt gestreefd naar een vermindering van ongeveer 14 % van het totale SCIg-verbruik. De behandelende arts beslist voor elke patiënt op basis van het ingeschatte risico voor de individuele patiënt en de te verwachten werkzaamheid en bijwerkingen van de behandeling.

	• IVIg’s moeten prioritair worden voorbehouden voor de volgende indicaties:
		- PID’s bij volwassenen en kinderen;
		- secundaire hypogammaglobulinemie bij patiënten met chronische lymfatische leukemie (CLL), multipel myeloom (MM) of na behandeling voor lymfoïde maligniteiten, die recidiverende infecties doen, net als na allogene stamceltransplantatie of CAR-T-celtherapie;
		- idiopathische trombocytopenische purpura (ITP) bij kinderen en volwassenen in geval van urgente nood tot behandeling, zoals acute bloedingen in combinatie met relatieve contra-indicatie voor of refractair zijn aan corticosteroïden;
		- syndroom van Guillain-Barré;
		- ziekte van Kawasaki;
		- multifocale motor neuropathie (MMN);
		- patiënten die in de huidige periode van een kritiek tekort aan SCIg’s worden overgeschakeld naar IVIg’s;

	• Volgende indicaties werden als minder prioritair beschouwd en hiervoor moeten alternatieven worden overwogen:
		- idiopathische trombocytopenische purpura  (ITP) bij volwassenen: alternatieve behandelingen moeten worden overwogen. Bijvoorbeeld trombopoëtine-receptor-agonisten (TPO-receptor-agonisten) vanaf 1 januari 2022*;
		- chronisch inflammatoire demyeliniserende polyneuropathie (CIDP): alternatieve behandelingen moeten worden overwogen. Bijvoorbeeld plasmaferese, hoog gedoseerde corticosteroïden en immuunsuppressie*.
* de behandelende arts beslist voor elke patiënt op basis van het ingeschatte risico voor de individuele patiënt en de te verwachten werkzaamheid en bijwerkingen van de behandeling.

	• Artsen(-specialisten) worden aanbevolen de gebruikte dosis kritisch te evalueren, zodat voor elke patiënt de laagst effectieve dosis wordt gebruikt.
	• Verder vraagt het FAGG met aandrang aan de (ziekenhuis)apothekers aan om geen voorraad SCIg’s in de apotheek te bewaren en slechts voor een periode van maximum vier weken geneesmiddel af te leveren aan de patiënten. Dit moet ervoor zorgen dat de huidige voorraad aan SCIg’s maximaal aanwezig is bij de groothandelaar-verdelers, zodat elke patiënt in deze periode van acuut tekort zijn/haar geneesmiddel kan bekomen.
	• De taskforce herinnert er ook aan om off-label gebruik zoveel mogelijk te beperken.
	
Het FAGG volgt de situatie ondertussen nauw op en zoekt samen met het RIZIV, de FOD Volksgezondheid, Veiligheid van de Voedselketen en Leefmilieu, de FOD economie en de beleidscel van de bevoegde minister verder naar bijkomende oplossingen op zeer korte termijn, net als naar structurele oplossingen op middellange en lange termijn.</ns2:Nl>
                        <ns2:En>21.12.2021
Due to a decrease in the availability of subcutaneous immunoglobulins around the world, there is also a critical level of limited availability in Belgium. The FAMHP issues several recommendations for hospital and retail pharmacists, medical doctors (specialists), and patients.
Fewer subcutaneous immunoglobulins (SCIgs) have been available since July 2021, and the reasons for this are the reduced collection of plasma around the world, the ever increasing need for immunoglobulins, and the dependence on plasma from other countries. The shortage is persistently noticeable but has been exacerbated by the COVID-19 situation.

In order to find a solution in the very short term, a task force has been organised with medical doctors (specialists), hospital and retail pharmacists, patient associations, and competent authorities.

A short-term solution is to import foreign packages of SCIgs for patients with primary immune deficiencies (PIDs). These packages will be available during the course of January 2022 and will be reimbursed for these patients. Furthermore, since October 2021, an additional quantity of intravenous immunoglobulins (IVIgs) based on plasma from Belgium has been made available for twelve months. The FAMHP hopes that every patient can be helped as much as possible with these additional quantities and the recommendations below.

Recommendations for the attention of hospital and retail pharmacists, medical doctors (specialists):

In order to safeguard the availability of these essential medicinal products for the most vulnerable patients as much as possible, the task force has developed the following prioritisation system and recommendations:

	· subcutaneous immunoglobulins must be reserved for patients who cannot switch to intravenous administration:
			- paediatric PID patients,
			- patients who are currently on SCIgs and cannot switch to IVIgs.

Whenever possible, patients currently on SCIgs are being switched to intravenous administration during the current critical shortage of subcutaneous forms. The aim is to reduce total consumption of SCIgs by about 14 %. The medical doctor treating a patient decides for him or her based on the assessed risk for the individual patient and the expected efficacy and adverse effects of the treatment. 

	· IVIgs must be reserved as a priority for the following indications:
			- PIDs in adults and children;
			- secondary hypogammaglobulinemia in patients with chronic lymphocytic leukaemia (CLL), multiple myeloma (MM) or after treatment for lymphoid malignancies, who have recurrent infections, as well as after allogeneic stem cell transplantation or CAR-T cell therapy;
			- immune thrombocytopenia (ITP) in children and adults in case of urgent need for treatment, such as acute bleeding in combination with relative contraindication for or being refractory to corticosteroids;
			- Guillain-Barré syndrome;
			- Kawasaki disease;
			- multifocal motor neuropathy (MMN);
			- patients who are being switched to IVIgs during the current critical shortage of SCIgs;

	· The following indications have been considered as having a lower priority and alternatives should be considered for them:
			- immune thrombocytopenia (ITP) in adults: alternative treatments should be considered. For example, thrombopoietin receptor agonists (TPO receptor agonists) from 1 January 2022*;
			- chronic inflammatory demyelinating polyneuropathy (CIDP): alternative treatments should be considered. For example, plasmapheresis, high-dose corticosteroids and immunosuppression*.
* The medical doctor treating a patient decides for him or her based on the assessed risk for the individual patient and the expected efficacy and adverse effects of the treatment.

	· It is recommended that medical specialists critically evaluate the dose used so that the lowest effective dose is used for each patient.
	· The FAMHP also urges (hospital) pharmacists to avoid keeping a stock of SCIgs in the pharmacy and to only supply medicinal products to patients for a maximum period of four weeks. This is intended to ensure that the current stock levels of SCIgs are available as much as possible at the wholesaler-distributors, so that every patient can receive his/her drug during this period of acute shortage.
	· The task force also reminds practitioners to limit off-label use as much as possible.

In the meantime, the FAMHP is closely monitoring the situation and it is working together with the Belgian National Institute for Health and Disability Insurance (RIZIV/INAMI), the FPS Public Health, Food Chain Safety and Environment, the FPS Economy, and the competent minister's policy unit, to continue to seek additional solutions in the very short term, as well as systemic solutions for the medium and long term.</ns2:En>
                    </AdditionalInformation>
                </ns4:Data>
            </ns4:SupplyProblem>
            <ns4:Dmpp ProductId="tleSNHFcj3jFExdyXtlo+20WoPYeSIWZHP/WumnGok0=" deliveryEnvironment="P" code="3722766" codeType="CNK">
                <ns4:Data from="2015-09-25" to="2018-11-11">
                    <Reimbursable>false</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2018-11-12">
                    <Reimbursable>false</Reimbursable>
                </ns4:Data>
            </ns4:Dmpp>
            <ns4:Dmpp ProductId="drfGOPWJcLLfNA5X1jH+fJn2QmNorrRV2JsSPZ9dTbQ=" deliveryEnvironment="H" code="7722168" codeType="CNK">
                <ns4:Data from="2021-06-01" to="2021-09-30">
                    <Price>484.9300</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2022-05-01" to="2022-05-31">
                    <Price>484.9300</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>false</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2022-04-01" to="2022-04-30">
                    <Price>484.9300</Price>
                    <Cheap>true</Cheap>
                    <Cheapest>false</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2019-04-01" to="2021-05-31">
                    <Price>428.3500</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2018-04-01" to="2019-03-31">
                    <Price>428.3500</Price>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2022-06-01">
                    <Price>600.3000</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>false</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2021-10-01" to="2022-03-31">
                    <Price>484.9300</Price>
                    <Cheap>true</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
            </ns4:Dmpp>
            <ns4:Dmpp ProductId="drfGOPWJcLLfNA5X1jH+fJn2QmNorrRV2JsSPZ9dTbQ=" deliveryEnvironment="A" code="7722168" codeType="CNK">
                <ns4:Data from="2020-11-01" to="2021-05-31">
                    <Price>435.4600</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2018-04-01" to="2020-10-31">
                    <Price>435.4600</Price>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2021-06-01" to="2022-05-31">
                    <Price>492.0400</Price>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2022-06-01">
                    <Price>607.4100</Price>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
            </ns4:Dmpp>
        </ns4:Ampp>
        """
}