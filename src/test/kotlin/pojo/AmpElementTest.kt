package pojo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xml.createXmlInputFactory
import xml.createXmlMapper
import javax.xml.stream.XMLInputFactory

class AmpElementTest {

    private lateinit var inputFactory: XMLInputFactory
    private lateinit var xmlMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        inputFactory = createXmlInputFactory()
        xmlMapper = createXmlMapper(inputFactory)
    }

    @Test
    fun testAmpMapper() {
        val amp = xmlMapper.readValue<AmpElement>(ampAsXml)
        println("$amp")

        assertEquals(amp.code, "SAM000025-00")
        assertEquals(amp.vmpCode, "16469")

        assertEquals(amp.dataBlocks.size, 4) //There are 4 data blocks. We'll test the contents in another test
        assertEquals(amp.ampComponents.size, 1) //1 AMPComponent
        assertEquals(amp.amppElements.size, 4) //4 AMPPs
    }

    @Test
    fun testAmpDataBlock() {
        val amp = xmlMapper.readValue<AmpElement>(ampAsXml)
        val ampData = amp.dataBlocks[1]

        //val ampData = xmlMapper.readValue<AmpData>(dataBlock)
        println("$ampData")

        assertEquals("Haldol 5 mg/ml", ampData.officialName)
        assertEquals("2017-09-19", ampData.from)
        assertNull(ampData.to)
        assertEquals("AUTHORIZED", ampData.status)
        assertEquals(false, ampData.blackTriangle)
        assertEquals("ALLOPATHIC", ampData.medicineType)

        assertEquals("01028", ampData.company!!.actorNr)
    }

    @Test
    fun testAmpComponentElementAndData() {
        val amp = xmlMapper.readValue<AmpElement>(ampAsXml)
        val component = amp.ampComponents[0]

        //Main checks
        assertEquals("16469", component.vmpComponentCode)
        assertEquals("1", component.sequenceNumber)
        assertEquals(component.dataBlocks.size, 4)
        assertEquals(component.ingredients.size, 1)

        //data check
        val data = component.dataBlocks[1]
        assertEquals("2017-09-19", data.from)
        assertNull(data.to)
        assertEquals(1, data.pharmaceuticalFormReferences.size)
        assertEquals("306", data.pharmaceuticalFormReferences[0].codeReference)
        assertEquals(1, data.routesOfAdministration.size)
        assertEquals("38", data.routesOfAdministration[0].codeReference)
        assertEquals("X", data.scored)
        assertEquals("2", data.specificDrugDevice)
        assertEquals("Haldol inj. oploss. i.m. [amp.] 5 mg / 1 ml", data.name.nl)

        //Ingredient check
        val ingredient = component.ingredients[0]
        assertEquals("1", ingredient.rank)
        assertEquals(2, ingredient.dataBlocks.size)
        val ingredientData = ingredient.dataBlocks[1]
        assertEquals("1961-07-02", ingredientData.from)
        assertNull(ingredientData.to)
        assertEquals("ACTIVE_SUBSTANCE", ingredientData.type)
        assertEquals("mg/mL", ingredientData.strength.unit)
        assertEquals("5.0000", ingredientData.strength.Strength)
        assertEquals("359", ingredientData.substanceCode.codeReference)
    }

    //region amp xml testing data
    private val ampAsXml = """
        <ns4:Amp vmpCode="16469" code="SAM000025-00">
        <ns4:Data from="1961-07-01" to="1961-07-01">
            <OfficialName>Haldol 5 mg/ml</OfficialName>
            <Status>AUTHORIZED</Status>
            <Name>
                <ns2:Fr>Haldol 5 mg/ml</ns2:Fr>
                <ns2:Nl>Haldol 5 mg/ml</ns2:Nl>
                <ns2:De>Haldol 5 mg/ml</ns2:De>
                <ns2:En>Haldol 5 mg/ml</ns2:En>
            </Name>
            <BlackTriangle>false</BlackTriangle>
            <MedicineType>ALLOPATHIC</MedicineType>
            <ns4:Company actorNr="01028">
                <ns4:Data from="1960-01-01" to="2022-06-29">
                    <ns3:AuthorisationNr>1028H</ns3:AuthorisationNr>
                    <ns3:Denomination>Janssen-Cilag</ns3:Denomination>
                    <ns3:LegalForm>NV</ns3:LegalForm>
                    <ns3:StreetName>Antwerpseweg</ns3:StreetName>
                    <ns3:StreetNum>15-17</ns3:StreetNum>
                    <ns3:Postcode>2340</ns3:Postcode>
                    <ns3:City>Beerse</ns3:City>
                    <ns3:CountryCode>BE</ns3:CountryCode>
                    <ns3:Phone>03/2805411</ns3:Phone>
                    <ns3:Language>NL</ns3:Language>
                </ns4:Data>
                <ns4:Data from="2022-06-30">
                    <ns3:VatNr countryCode="BE">415283427</ns3:VatNr>
                    <ns3:Denomination>Janssen-Cilag</ns3:Denomination>
                    <ns3:LegalForm>S.A.-N.V.</ns3:LegalForm>
                    <ns3:StreetName>Antwerpseweg</ns3:StreetName>
                    <ns3:StreetNum>15-17</ns3:StreetNum>
                    <ns3:Postcode>2340</ns3:Postcode>
                    <ns3:City>Beerse</ns3:City>
                    <ns3:CountryCode>BE</ns3:CountryCode>
                    <ns3:Language>NL</ns3:Language>
                </ns4:Data>
            </ns4:Company>
        </ns4:Data>
    <ns4:Data from="2017-09-19">
            <OfficialName>Haldol 5 mg/ml</OfficialName>
            <Status>AUTHORIZED</Status>
            <Name>
                <ns2:Fr>Haldol 5 mg/ml</ns2:Fr>
                <ns2:Nl>Haldol 5 mg/ml</ns2:Nl>
                <ns2:De>Haldol 5 mg/ml</ns2:De>
                <ns2:En>Haldol 5 mg/ml</ns2:En>
            </Name>
            <BlackTriangle>false</BlackTriangle>
            <MedicineType>ALLOPATHIC</MedicineType>
            <PrescriptionNameFamhp>
                <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp.</ns2:Fr>
                <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp.</ns2:Nl>
                <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp.</ns2:De>
                <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp.</ns2:En>
            </PrescriptionNameFamhp>
            <ns4:Company actorNr="01028">
                <ns4:Data from="1960-01-01" to="2022-06-29">
                    <ns3:AuthorisationNr>1028H</ns3:AuthorisationNr>
                    <ns3:Denomination>Janssen-Cilag</ns3:Denomination>
                    <ns3:LegalForm>NV</ns3:LegalForm>
                    <ns3:StreetName>Antwerpseweg</ns3:StreetName>
                    <ns3:StreetNum>15-17</ns3:StreetNum>
                    <ns3:Postcode>2340</ns3:Postcode>
                    <ns3:City>Beerse</ns3:City>
                    <ns3:CountryCode>BE</ns3:CountryCode>
                    <ns3:Phone>03/2805411</ns3:Phone>
                    <ns3:Language>NL</ns3:Language>
                </ns4:Data>
                <ns4:Data from="2022-06-30">
                    <ns3:VatNr countryCode="BE">415283427</ns3:VatNr>
                    <ns3:Denomination>Janssen-Cilag</ns3:Denomination>
                    <ns3:LegalForm>S.A.-N.V.</ns3:LegalForm>
                    <ns3:StreetName>Antwerpseweg</ns3:StreetName>
                    <ns3:StreetNum>15-17</ns3:StreetNum>
                    <ns3:Postcode>2340</ns3:Postcode>
                    <ns3:City>Beerse</ns3:City>
                    <ns3:CountryCode>BE</ns3:CountryCode>
                    <ns3:Language>NL</ns3:Language>
                </ns4:Data>
            </ns4:Company>
            <AbbreviatedName>
                <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 mg / 1 ml</ns2:Fr>
                <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 mg / 1 ml</ns2:Nl>
            </AbbreviatedName>
            <ProprietarySuffix>
                <ns2:Fr>-</ns2:Fr>
                <ns2:Nl>-</ns2:Nl>
            </ProprietarySuffix>
            <PrescriptionName>
                <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 mg / 1 ml</ns2:Fr>
                <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 mg / 1 ml</ns2:Nl>
            </PrescriptionName>
        </ns4:Data>        
         <ns4:Data from="1961-07-02" to="2016-09-30">
            <OfficialName>Haldol 5 mg/ml</OfficialName>
            <Status>AUTHORIZED</Status>
            <Name>
                <ns2:Fr>Haldol 5 mg/ml</ns2:Fr>
                <ns2:Nl>Haldol 5 mg/ml</ns2:Nl>
                <ns2:De>Haldol 5 mg/ml</ns2:De>
                <ns2:En>Haldol 5 mg/ml</ns2:En>
            </Name>
            <BlackTriangle>false</BlackTriangle>
            <MedicineType>ALLOPATHIC</MedicineType>
            <PrescriptionNameFamhp>
                <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp.</ns2:Fr>
                <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp.</ns2:Nl>
                <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp.</ns2:De>
                <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp.</ns2:En>
            </PrescriptionNameFamhp>
            <ns4:Company actorNr="01028">
                <ns4:Data from="1960-01-01" to="2022-06-29">
                    <ns3:AuthorisationNr>1028H</ns3:AuthorisationNr>
                    <ns3:Denomination>Janssen-Cilag</ns3:Denomination>
                    <ns3:LegalForm>NV</ns3:LegalForm>
                    <ns3:StreetName>Antwerpseweg</ns3:StreetName>
                    <ns3:StreetNum>15-17</ns3:StreetNum>
                    <ns3:Postcode>2340</ns3:Postcode>
                    <ns3:City>Beerse</ns3:City>
                    <ns3:CountryCode>BE</ns3:CountryCode>
                    <ns3:Phone>03/2805411</ns3:Phone>
                    <ns3:Language>NL</ns3:Language>
                </ns4:Data>
                <ns4:Data from="2022-06-30">
                    <ns3:VatNr countryCode="BE">415283427</ns3:VatNr>
                    <ns3:Denomination>Janssen-Cilag</ns3:Denomination>
                    <ns3:LegalForm>S.A.-N.V.</ns3:LegalForm>
                    <ns3:StreetName>Antwerpseweg</ns3:StreetName>
                    <ns3:StreetNum>15-17</ns3:StreetNum>
                    <ns3:Postcode>2340</ns3:Postcode>
                    <ns3:City>Beerse</ns3:City>
                    <ns3:CountryCode>BE</ns3:CountryCode>
                    <ns3:Language>NL</ns3:Language>
                </ns4:Data>
            </ns4:Company>
        </ns4:Data>
        <ns4:Data from="2016-10-01" to="2017-09-18">
            <OfficialName>Haldol 5 mg/ml</OfficialName>
            <Status>AUTHORIZED</Status>
            <Name>
                <ns2:Fr>Haldol 5 mg/ml</ns2:Fr>
                <ns2:Nl>Haldol 5 mg/ml</ns2:Nl>
                <ns2:De>Haldol 5 mg/ml</ns2:De>
                <ns2:En>Haldol 5 mg/ml</ns2:En>
            </Name>
            <BlackTriangle>false</BlackTriangle>
            <MedicineType>ALLOPATHIC</MedicineType>
            <PrescriptionNameFamhp>
                <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp.</ns2:Fr>
                <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp.</ns2:Nl>
                <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp.</ns2:De>
                <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp.</ns2:En>
            </PrescriptionNameFamhp>
            <ns4:Company actorNr="01028">
                <ns4:Data from="1960-01-01" to="2022-06-29">
                    <ns3:AuthorisationNr>1028H</ns3:AuthorisationNr>
                    <ns3:Denomination>Janssen-Cilag</ns3:Denomination>
                    <ns3:LegalForm>NV</ns3:LegalForm>
                    <ns3:StreetName>Antwerpseweg</ns3:StreetName>
                    <ns3:StreetNum>15-17</ns3:StreetNum>
                    <ns3:Postcode>2340</ns3:Postcode>
                    <ns3:City>Beerse</ns3:City>
                    <ns3:CountryCode>BE</ns3:CountryCode>
                    <ns3:Phone>03/2805411</ns3:Phone>
                    <ns3:Language>NL</ns3:Language>
                </ns4:Data>
                <ns4:Data from="2022-06-30">
                    <ns3:VatNr countryCode="BE">415283427</ns3:VatNr>
                    <ns3:Denomination>Janssen-Cilag</ns3:Denomination>
                    <ns3:LegalForm>S.A.-N.V.</ns3:LegalForm>
                    <ns3:StreetName>Antwerpseweg</ns3:StreetName>
                    <ns3:StreetNum>15-17</ns3:StreetNum>
                    <ns3:Postcode>2340</ns3:Postcode>
                    <ns3:City>Beerse</ns3:City>
                    <ns3:CountryCode>BE</ns3:CountryCode>
                    <ns3:Language>NL</ns3:Language>
                </ns4:Data>
            </ns4:Company>
            <AbbreviatedName>
                <ns2:Fr>Haldol sol. inj. i.m./i.v. [amp.] 5 mg / 1 ml</ns2:Fr>
                <ns2:Nl>Haldol inj. oploss. i.m./i.v. [amp.] 5 mg / 1 ml</ns2:Nl>
            </AbbreviatedName>
            <ProprietarySuffix>
                <ns2:Fr>-</ns2:Fr>
                <ns2:Nl>-</ns2:Nl>
            </ProprietarySuffix>
            <PrescriptionName>
                <ns2:Fr>Haldol sol. inj. i.m./i.v. [amp.] 5 mg / 1 ml</ns2:Fr>
                <ns2:Nl>Haldol inj. oploss. i.m./i.v. [amp.] 5 mg / 1 ml</ns2:Nl>
            </PrescriptionName>
        </ns4:Data>
        <ns4:AmpComponent vmpComponentCode="16469" sequenceNr="1">
            <ns4:Data from="1961-07-01" to="1961-07-01">
                <ns4:PharmaceuticalForm code="306">
                    <ns5:Name>
                        <ns2:Fr>Solution injectable</ns2:Fr>
                        <ns2:Nl>Oplossing voor injectie</ns2:Nl>
                        <ns2:De>Injektionslösung</ns2:De>
                        <ns2:En>Solution for injection</ns2:En>
                    </ns5:Name>
                </ns4:PharmaceuticalForm>
                <ns4:RouteOfAdministration code="38">
                    <ns5:Name>
                        <ns2:Fr>Voie intramusculaire</ns2:Fr>
                        <ns2:Nl>Intramusculair gebruik</ns2:Nl>
                        <ns2:De>intramuskuläre Anwendung</ns2:De>
                        <ns2:En>Intramuscular use</ns2:En>
                    </ns5:Name>
                    <ns4:StandardRoute standard="SNOMED_CT" code="78421000"/>
                </ns4:RouteOfAdministration>
                <ns4:RouteOfAdministration code="49">
                    <ns5:Name>
                        <ns2:Fr>Voie intraveineuse</ns2:Fr>
                        <ns2:Nl>Intraveneus gebruik</ns2:Nl>
                        <ns2:De>intravenöse Anwendung</ns2:De>
                        <ns2:En>Intravenous use</ns2:En>
                    </ns5:Name>
                    <ns4:StandardRoute standard="SNOMED_CT" code="47625008"/>
                </ns4:RouteOfAdministration>
            </ns4:Data>
            <ns4:Data from="2017-09-19">
                <ns4:PharmaceuticalForm code="306">
                    <ns5:Name>
                        <ns2:Fr>Solution injectable</ns2:Fr>
                        <ns2:Nl>Oplossing voor injectie</ns2:Nl>
                        <ns2:De>Injektionslösung</ns2:De>
                        <ns2:En>Solution for injection</ns2:En>
                    </ns5:Name>
                </ns4:PharmaceuticalForm>
                <ns4:RouteOfAdministration code="38">
                    <ns5:Name>
                        <ns2:Fr>Voie intramusculaire</ns2:Fr>
                        <ns2:Nl>Intramusculair gebruik</ns2:Nl>
                        <ns2:De>intramuskuläre Anwendung</ns2:De>
                        <ns2:En>Intramuscular use</ns2:En>
                    </ns5:Name>
                    <ns4:StandardRoute standard="SNOMED_CT" code="78421000"/>
                </ns4:RouteOfAdministration>
                <Scored>X</Scored>
                <SpecificDrugDevice>2</SpecificDrugDevice>
                <Name>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 mg / 1 ml</ns2:Nl>
                </Name>
            </ns4:Data>
            <ns4:Data from="1961-07-02" to="2016-09-30">
                <ns4:PharmaceuticalForm code="306">
                    <ns5:Name>
                        <ns2:Fr>Solution injectable</ns2:Fr>
                        <ns2:Nl>Oplossing voor injectie</ns2:Nl>
                        <ns2:De>Injektionslösung</ns2:De>
                        <ns2:En>Solution for injection</ns2:En>
                    </ns5:Name>
                </ns4:PharmaceuticalForm>
                <ns4:RouteOfAdministration code="38">
                    <ns5:Name>
                        <ns2:Fr>Voie intramusculaire</ns2:Fr>
                        <ns2:Nl>Intramusculair gebruik</ns2:Nl>
                        <ns2:De>intramuskuläre Anwendung</ns2:De>
                        <ns2:En>Intramuscular use</ns2:En>
                    </ns5:Name>
                    <ns4:StandardRoute standard="SNOMED_CT" code="78421000"/>
                </ns4:RouteOfAdministration>
            </ns4:Data>
            <ns4:Data from="2016-10-01" to="2017-09-18">
                <ns4:PharmaceuticalForm code="306">
                    <ns5:Name>
                        <ns2:Fr>Solution injectable</ns2:Fr>
                        <ns2:Nl>Oplossing voor injectie</ns2:Nl>
                        <ns2:De>Injektionslösung</ns2:De>
                        <ns2:En>Solution for injection</ns2:En>
                    </ns5:Name>
                </ns4:PharmaceuticalForm>
                <ns4:RouteOfAdministration code="38">
                    <ns5:Name>
                        <ns2:Fr>Voie intramusculaire</ns2:Fr>
                        <ns2:Nl>Intramusculair gebruik</ns2:Nl>
                        <ns2:De>intramuskuläre Anwendung</ns2:De>
                        <ns2:En>Intramuscular use</ns2:En>
                    </ns5:Name>
                    <ns4:StandardRoute standard="SNOMED_CT" code="78421000"/>
                </ns4:RouteOfAdministration>
                <Scored>X</Scored>
                <SpecificDrugDevice>2</SpecificDrugDevice>
                <Name>
                    <ns2:Fr>Haldol sol. inj. i.m./i.v. [amp.] 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m./i.v. [amp.] 5 mg / 1 ml</ns2:Nl>
                </Name>
            </ns4:Data>
            <ns4:RealActualIngredient rank="1">
                <ns4:Data from="1961-07-01" to="1961-07-01">
                    <Type>ACTIVE_SUBSTANCE</Type>
                    <Strength unit="mg">5.0000</Strength>
                    <ns4:Substance code="359">
                        <ns5:Name>
                            <ns2:Fr>Halopéridol</ns2:Fr>
                            <ns2:Nl>Haloperidol</ns2:Nl>
                            <ns2:En>Haloperidol</ns2:En>
                        </ns5:Name>
                        <ns4:StandardSubstance standard="SNOMED_CT" code="386837002"/>
                        <ns4:StandardSubstance standard="CAS" code="52-86-8"/>
                    </ns4:Substance>
                </ns4:Data>
                <ns4:Data from="1961-07-02">
                    <Type>ACTIVE_SUBSTANCE</Type>
                    <Strength unit="mg/mL">5.0000</Strength>
                    <ns4:Substance code="359">
                        <ns5:Name>
                            <ns2:Fr>Halopéridol</ns2:Fr>
                            <ns2:Nl>Haloperidol</ns2:Nl>
                            <ns2:En>Haloperidol</ns2:En>
                        </ns5:Name>
                        <ns4:StandardSubstance standard="SNOMED_CT" code="386837002"/>
                        <ns4:StandardSubstance standard="CAS" code="52-86-8"/>
                    </ns4:Substance>
                </ns4:Data>
            </ns4:RealActualIngredient>
        </ns4:AmpComponent>
        <ns4:Ampp ctiExtended="000025-01">
            <ns4:Data from="1961-07-01" to="1961-07-01">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletFR</ns2:Fr>
                    <ns2:Nl>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletNL</ns2:Nl>
                    <ns2:De>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletDE</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=rcp</ns2:Fr>
                    <ns2:Nl>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=skp</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <FMDInScope>false</FMDInScope>
                <AntiTemperingDevicePresent>false</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
            </ns4:Data>
            <ns4:Data from="1961-07-02">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
            </ns4:Data>
            <ns4:AmppComponent sequenceNr="1">
                <ns4:Data from="1961-07-02">
                    <AmpcSequenceNr>1</AmpcSequenceNr>
                    <ContentType>ACTIVE_COMPONENT</ContentType>
                    <ns4:PackagingType code="1">
                        <ns5:Name>
                            <ns2:Fr>Ampoule</ns2:Fr>
                            <ns2:Nl>Ampul</ns2:Nl>
                            <ns2:De>Ampulle</ns2:De>
                            <ns2:En>Ampoule</ns2:En>
                        </ns5:Name>
                        <ns5:EdqmCode>30001000</ns5:EdqmCode>
                        <ns5:EdqmDefinition>Container sealed by fusion and to be opened exclusively by breaking. The contents are intended for use on one occasion only.</ns5:EdqmDefinition>
                    </ns4:PackagingType>
                </ns4:Data>
                <ns4:AmppComponentEquivalent sequenceNr="1">
                    <ns4:Data from="1961-07-02">
                        <Content unit="mL">1.0000</Content>
                    </ns4:Data>
                </ns4:AmppComponentEquivalent>
            </ns4:AmppComponent>
        </ns4:Ampp>
        <ns4:Ampp ctiExtended="000025-02">
            <ns4:Data from="2020-07-02" to="2020-09-30">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5x 5mg/1ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5x 5mg/1ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>2.4700</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
                <OfficialExFactoryPrice>2.4700</OfficialExFactoryPrice>
                <RealExFactoryPrice>2.4700</RealExFactoryPrice>
                <PricingInformationDecisionDate>2020-07-01</PricingInformationDecisionDate>
            </ns4:Data>
            <ns4:Data from="2022-01-01" to="2022-05-31">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5x 5mg/1ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5x 5mg/1ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>2.4700</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
                <OfficialExFactoryPrice>2.4700</OfficialExFactoryPrice>
                <RealExFactoryPrice>2.4700</RealExFactoryPrice>
                <PricingInformationDecisionDate>2021-12-29</PricingInformationDecisionDate>
            </ns4:Data>
            <ns4:Data from="1961-07-01" to="1961-07-01">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletFR</ns2:Fr>
                    <ns2:Nl>https://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletNL</ns2:Nl>
                    <ns2:De>https://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletDE</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=rcp</ns2:Fr>
                    <ns2:Nl>https://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=skp</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <DHPCLink>
                    <ns2:Fr>https://www.afmps.be/sites/default/files/content/dhpc_haldol_fr_-_website.pdf</ns2:Fr>
                    <ns2:Nl>https://www.afmps.be/sites/default/files/content/dhpc_haldol_nl_-_website.pdf</ns2:Nl>
                    <ns2:De>https://www.afmps.be/sites/default/files/content/dhpc_haldol_fr_-_website.pdf</ns2:De>
                </DHPCLink>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
            </ns4:Data>
            <ns4:Data from="2016-10-01" to="2017-06-12">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m./i.v. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m./i.v. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. [amp.] 5x 5 mg/1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. [amp.] 5x 5 mg/1 ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
            </ns4:Data>
            <ns4:Data from="2020-10-01" to="2021-12-31">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5x 5mg/1ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5x 5mg/1ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>2.4700</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
                <OfficialExFactoryPrice>2.4700</OfficialExFactoryPrice>
                <RealExFactoryPrice>2.4700</RealExFactoryPrice>
                <PricingInformationDecisionDate>2020-07-01</PricingInformationDecisionDate>
            </ns4:Data>
            <ns4:Data from="2017-07-01" to="2017-09-18">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m./i.v. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m./i.v. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. [amp.] 5x 5mg/1ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. [amp.] 5x 5mg/1ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>2.5500</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
            </ns4:Data>
            <ns4:Data from="1961-07-02" to="2016-09-30">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
            </ns4:Data>
            <ns4:Data from="2017-09-19" to="2020-06-30">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5x 5mg/1ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5x 5mg/1ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>2.5500</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
            </ns4:Data>
            <ns4:Data from="2020-07-01" to="2020-07-01">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5x 5mg/1ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5x 5mg/1ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>2.4700</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
            </ns4:Data>
            <ns4:Data from="2022-06-01">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. i.m. [amp.] 5x 5mg/1ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m. [amp.] 5x 5mg/1ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
                <ExFactoryPrice>2.4700</ExFactoryPrice>
                <ReimbursementCode>0</ReimbursementCode>
                <OfficialExFactoryPrice>2.4700</OfficialExFactoryPrice>
                <RealExFactoryPrice>2.4700</RealExFactoryPrice>
                <PricingInformationDecisionDate>2022-06-01</PricingInformationDecisionDate>
            </ns4:Data>
            <ns4:Data from="2017-06-13" to="2017-06-30">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>5 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 5 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 5 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 5 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 5 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDProductCode>05413868105568</FMDProductCode>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
                <AbbreviatedName>
                    <ns2:Fr>Haldol sol. inj. i.m./i.v. [amp.] 5 x 5 mg / 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. i.m./i.v. [amp.] 5 x 5 mg / 1 ml</ns2:Nl>
                </AbbreviatedName>
                <PrescriptionName>
                    <ns2:Fr>Haldol sol. inj. [amp.] 5x 5mg/1ml</ns2:Fr>
                    <ns2:Nl>Haldol inj. oploss. [amp.] 5x 5mg/1ml</ns2:Nl>
                </PrescriptionName>
                <CrmLink>
                    <ns2:Fr>http://www.cbip.be/fr/contents/jump?amppid=1834</ns2:Fr>
                    <ns2:Nl>http://www.bcfi.be/nl/contents/jump?amppid=1834</ns2:Nl>
                </CrmLink>
            </ns4:Data>
            <ns4:AmppComponent sequenceNr="1">
                <ns4:Data from="1961-07-02">
                    <AmpcSequenceNr>1</AmpcSequenceNr>
                    <ContentType>ACTIVE_COMPONENT</ContentType>
                    <ContentMultiplier>5</ContentMultiplier>
                    <ns4:PackagingType code="1">
                        <ns5:Name>
                            <ns2:Fr>Ampoule</ns2:Fr>
                            <ns2:Nl>Ampul</ns2:Nl>
                            <ns2:De>Ampulle</ns2:De>
                            <ns2:En>Ampoule</ns2:En>
                        </ns5:Name>
                        <ns5:EdqmCode>30001000</ns5:EdqmCode>
                        <ns5:EdqmDefinition>Container sealed by fusion and to be opened exclusively by breaking. The contents are intended for use on one occasion only.</ns5:EdqmDefinition>
                    </ns4:PackagingType>
                </ns4:Data>
                <ns4:Data from="1961-07-01" to="1961-07-01">
                    <AmpcSequenceNr>1</AmpcSequenceNr>
                    <ContentType>ACTIVE_COMPONENT</ContentType>
                    <ContentMultiplier>5</ContentMultiplier>
                    <ns4:PackagingType code="1">
                        <ns5:Name>
                            <ns2:Fr>Ampoule</ns2:Fr>
                            <ns2:Nl>Ampul</ns2:Nl>
                            <ns2:De>Ampulle</ns2:De>
                            <ns2:En>Ampoule</ns2:En>
                        </ns5:Name>
                        <ns5:EdqmCode>30001000</ns5:EdqmCode>
                        <ns5:EdqmDefinition>Container sealed by fusion and to be opened exclusively by breaking. The contents are intended for use on one occasion only.</ns5:EdqmDefinition>
                    </ns4:PackagingType>
                </ns4:Data>
                <ns4:AmppComponentEquivalent sequenceNr="1">
                    <ns4:Data from="1961-07-02">
                        <Content unit="mL">1.0000</Content>
                    </ns4:Data>
                    <ns4:Data from="1961-07-01" to="1961-07-01">
                        <Content unit="mL">1.0000</Content>
                    </ns4:Data>
                </ns4:AmppComponentEquivalent>
            </ns4:AmppComponent>
            <ns4:Commercialization>
                <ns4:Data from="2011-09-21"/>
            </ns4:Commercialization>
            <ns4:Dmpp ProductId="T5xvr080ePIBjphyXd6d9yWrNzK20yNolf7W7eAwvj8=" deliveryEnvironment="A" code="0708289" codeType="CNK">
                <ns4:Data from="2017-07-01" to="2020-06-30">
                    <Price>0.5400</Price>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2020-07-01">
                    <Price>0.6380</Price>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
            </ns4:Dmpp>
            <ns4:Dmpp ProductId="T5xvr080ePIBjphyXd6d9yWrNzK20yNolf7W7eAwvj8=" deliveryEnvironment="H" code="0708289" codeType="CNK">
                <ns4:Data from="2017-07-01" to="2020-06-30">
                    <Price>0.5400</Price>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2020-07-01">
                    <Price>0.5240</Price>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
            </ns4:Dmpp>
            <ns4:Dmpp ProductId="MH8qfqeIDRverW/DRXU4OccsIIzA4HQGyM1hUWiFq14=" deliveryEnvironment="P" code="0046128" codeType="CNK">
                <ns4:Data from="2022-01-01" to="2022-05-31">
                    <Price>7.8700</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="1961-07-02" to="2016-07-11">
                    <Reimbursable>false</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2021-01-01" to="2021-12-31">
                    <Price>7.8200</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2017-07-01" to="2020-04-30">
                    <Price>7.7200</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2022-06-01">
                    <Price>7.9700</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2020-05-01" to="2020-06-30">
                    <Price>7.8800</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2016-07-12" to="2017-06-30">
                    <Reimbursable>false</Reimbursable>
                </ns4:Data>
                <ns4:Data from="2020-07-01" to="2020-12-31">
                    <Price>7.7700</Price>
                    <Cheap>false</Cheap>
                    <Cheapest>true</Cheapest>
                    <Reimbursable>true</Reimbursable>
                </ns4:Data>
            </ns4:Dmpp>
        </ns4:Ampp>
        <ns4:Ampp ctiExtended="000025-03">
            <ns4:Data from="1961-07-01" to="1961-07-01">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletFR</ns2:Fr>
                    <ns2:Nl>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletNL</ns2:Nl>
                    <ns2:De>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=leafletDE</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=rcp</ns2:Fr>
                    <ns2:Nl>http://bijsluiters.fagg-afmps.be/registrationSearchServlet?key=BE000025&amp;leafletType=skp</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>30 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <FMDInScope>false</FMDInScope>
                <AntiTemperingDevicePresent>false</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
            </ns4:Data>
            <ns4:Data from="1961-07-02">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>30 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 30 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 30 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 30 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 30 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
            </ns4:Data>
            <ns4:AmppComponent sequenceNr="1">
                <ns4:Data from="1961-07-02">
                    <AmpcSequenceNr>1</AmpcSequenceNr>
                    <ContentType>ACTIVE_COMPONENT</ContentType>
                    <ContentMultiplier>30</ContentMultiplier>
                    <ns4:PackagingType code="1">
                        <ns5:Name>
                            <ns2:Fr>Ampoule</ns2:Fr>
                            <ns2:Nl>Ampul</ns2:Nl>
                            <ns2:De>Ampulle</ns2:De>
                            <ns2:En>Ampoule</ns2:En>
                        </ns5:Name>
                        <ns5:EdqmCode>30001000</ns5:EdqmCode>
                        <ns5:EdqmDefinition>Container sealed by fusion and to be opened exclusively by breaking. The contents are intended for use on one occasion only.</ns5:EdqmDefinition>
                    </ns4:PackagingType>
                </ns4:Data>
                <ns4:AmppComponentEquivalent sequenceNr="1">
                    <ns4:Data from="1961-07-02">
                        <Content unit="mL">1.0000</Content>
                    </ns4:Data>
                </ns4:AmppComponentEquivalent>
            </ns4:AmppComponent>
        </ns4:Ampp>
        <ns4:Ampp ctiExtended="000025-04">
            <ns4:Data from="2019-01-15">
                <AuthorisationNr>BE000025</AuthorisationNr>
                <Orphan>false</Orphan>
                <LeafletLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30b5f?version=5</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30c05?version=6</ns2:Nl>
                    <ns2:De>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ec1e5c015ab3d30a26?version=6</ns2:De>
                </LeafletLink>
                <SpcLink>
                    <ns2:Fr>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ed1e5c015ab3d30cbb?version=6</ns2:Fr>
                    <ns2:Nl>https://app.fagg-afmps.be/pharma-status/api/files/62bc61ee1e5c015ab3d30d5d?version=7</ns2:Nl>
                </SpcLink>
                <ParallelCircuit>0</ParallelCircuit>
                <PackDisplayValue>50 x 1 ml</PackDisplayValue>
                <Status>AUTHORIZED</Status>
                <PrescriptionNameFamhp>
                    <ns2:Fr>Haldol 5 mg/ml sol. inj. i.m. amp. 50 x 1 ml</ns2:Fr>
                    <ns2:Nl>Haldol 5 mg/ml inj. opl. i.m. amp. 50 x 1 ml</ns2:Nl>
                    <ns2:De>Haldol 5 mg/ml Inj-Lös. i.m. Amp. 50 x 1 ml</ns2:De>
                    <ns2:En>Haldol 5 mg/ml inj. sol. i.m. amp. 50 x 1 ml</ns2:En>
                </PrescriptionNameFamhp>
                <FMDInScope>true</FMDInScope>
                <AntiTemperingDevicePresent>true</AntiTemperingDevicePresent>
                <ns4:Atc code="N05AD01">
                    <ns5:Description>Haloperidol</ns5:Description>
                </ns4:Atc>
                <ns4:DeliveryModus code="M1">
                    <ns5:Description>
                        <ns2:Fr>Prescription médicale - Art 6, §1 bis, troisième alinéa, premier tiret de la Loi du 25/03/1964 + Art 61 de l'AR 14/12/2006</ns2:Fr>
                        <ns2:Nl>Medisch voorschrift - Art 6, §1 bis, derde alinea, eerste streepje van de Wet van 25/03/1964 + Art 61 van het KB van 14/12/2006</ns2:Nl>
                        <ns2:De>Arzneimittel auf Verschreibung - Art. 6, §1 bis, Absatz 3, erster Gedankenstrich des Gesetzes vom 25.03.1964 + Art. 61 des KE vom 14.12.2006</ns2:De>
                        <ns2:En>Medical prescription - Art 6, §1 bis, 3rd paragraph, 1st indent Law 25/03/1964 + Art 61 Royal Decree 14/12/2006</ns2:En>
                    </ns5:Description>
                </ns4:DeliveryModus>
            </ns4:Data>
            <ns4:AmppComponent sequenceNr="1">
                <ns4:Data from="2019-01-15">
                    <AmpcSequenceNr>1</AmpcSequenceNr>
                    <ContentType>ACTIVE_COMPONENT</ContentType>
                    <ContentMultiplier>50</ContentMultiplier>
                    <ns4:PackagingType code="1">
                        <ns5:Name>
                            <ns2:Fr>Ampoule</ns2:Fr>
                            <ns2:Nl>Ampul</ns2:Nl>
                            <ns2:De>Ampulle</ns2:De>
                            <ns2:En>Ampoule</ns2:En>
                        </ns5:Name>
                        <ns5:EdqmCode>30001000</ns5:EdqmCode>
                        <ns5:EdqmDefinition>Container sealed by fusion and to be opened exclusively by breaking. The contents are intended for use on one occasion only.</ns5:EdqmDefinition>
                    </ns4:PackagingType>
                </ns4:Data>
                <ns4:AmppComponentEquivalent sequenceNr="1">
                    <ns4:Data from="2019-01-15">
                        <Content unit="mL">1.0000</Content>
                    </ns4:Data>
                </ns4:AmppComponentEquivalent>
            </ns4:AmppComponent>
        </ns4:Ampp>
    </ns4:Amp>
    """.trimIndent()
    //endregion
}