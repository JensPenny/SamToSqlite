package pojo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xml.createXmlInputFactory
import xml.createXmlMapper
import javax.xml.stream.XMLInputFactory
import kotlin.test.assertEquals

class VmpElementTest {

    private lateinit var inputFactory: XMLInputFactory
    private lateinit var xmlMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        inputFactory = createXmlInputFactory()
        xmlMapper = createXmlMapper(inputFactory)
    }

    @Test
    fun testVmpElement() {
        val vmp = xmlMapper.readValue<VmpElement>(vmpTestData)

        assertEquals("16378", vmp.code)

        val vmpData = vmp.dataBlocks[0]
        assertEquals("2016-10-01", vmpData.from)
        assertEquals("2019-09-15", vmpData.to)
        assertEquals("Groupe Toplexil sirop, solution (or.)", vmpData.name.fr)
        assertEquals("Groep Toplexil siroop, oplossing (or.)", vmpData.abbreviation.nl)
        assertEquals("BQEAAA/9", vmpData.commentedClassificationReference.codeReference)
        assertEquals("15313", vmpData.vmpGroupReference.codeReference)

        val vmpComponent = vmp.vmpComponents[0]
        val vmpComponentData = vmpComponent.dataBlocks[0]

        assertEquals("16378", vmpComponent.code)
        assertEquals("2016-10-01", vmpComponentData.from)
        assertEquals("2019-09-15", vmpComponentData.to)
        assertEquals("1", vmpComponentData.phaseNumber)
        assertEquals("70", vmpComponentData.virtualForm.codeReference)
        assertEquals("57", vmpComponentData.routeOfAdministration.codeReference)

        val virtualIngredient = vmpComponent.virtualIngredients[0]
        val virtualIngredientData = virtualIngredient.dataBlocks[0]

        assertEquals("4", virtualIngredient.rank)
        assertEquals("2016-10-01", virtualIngredientData.from)
        assertEquals("2019-09-15", virtualIngredientData.to)
        assertEquals("ACTIVE_SUBSTANCE", virtualIngredientData.type)
        assertEquals("mg", virtualIngredientData.strength.numeratorRange?.unit)
        assertEquals("33.3000", virtualIngredientData.strength.numeratorRange?.min)
        assertEquals("33.3000", virtualIngredientData.strength.numeratorRange?.max)
        assertEquals("mL", virtualIngredientData.strength.denominator?.unit)
        assertEquals("5.0000", virtualIngredientData.strength.denominator?.Denominator)
        assertEquals("4", virtualIngredientData.substanceReference.codeReference)
    }
    var vmpTestData =
        """<ns3:Vmp code="16378">
    <ns3:Data from="2016-10-01" to="2019-09-15">
        <ns2:Name>
            <Fr>Groupe Toplexil sirop, solution (or.)</Fr>
            <Nl>Groep Toplexil siroop, oplossing (or.)</Nl>
        </ns2:Name>
        <ns2:Abbreviation>
            <Fr>Groupe Toplexil sirop, solution (or.)</Fr>
            <Nl>Groep Toplexil siroop, oplossing (or.)</Nl>
        </ns2:Abbreviation>
        <ns3:CommentedClassification code="BQEAAA/9">
            <ns3:Data from="2016-10-01" to="2020-07-21">
                <ns2:Title>
                    <Fr>Spécialités [Associations]</Fr>
                    <Nl>Specialiteiten [Combinatiepreparaten]</Nl>
                </ns2:Title>
                <ns2:PosologyNote>
                    <Fr>/</Fr>
                    <Nl>/</Nl>
                </ns2:PosologyNote>
                <ns2:Url>
                    <Fr>http://www.bcfi.be/nl/chapters/5?frag=4093</Fr>
                    <Nl>http://www.bcfi.be/nl/chapters/5?frag=4093</Nl>
                </ns2:Url>
            </ns3:Data>
        </ns3:CommentedClassification>
        <ns3:VmpGroup ProductId="CTDuuQgGXjlhjq32wcvVNQ==" code="15313">
            <ns3:Data from="2016-10-01" to="2019-09-15">
                <ns2:Name>
                    <Fr>Groupe Toplexil sir. oral</Fr>
                    <Nl>Groep Toplexil sir. oraal</Nl>
                </ns2:Name>
                <ns3:NoGenericPrescriptionReason code="1">
                    <ns4:Description>
                        <Fr>3+</Fr>
                        <Nl>3+</Nl>
                    </ns4:Description>
                </ns3:NoGenericPrescriptionReason>
                <ns3:NoSwitchReason code="99">
                    <ns4:Description>
                        <Fr>no DCI no switch</Fr>
                        <Nl>no VOS no switch</Nl>
                    </ns4:Description>
                </ns3:NoSwitchReason>
            </ns3:Data>
        </ns3:VmpGroup>
    </ns3:Data>
    <ns3:VmpComponent code="16378">
        <ns3:Data from="2016-10-01" to="2019-09-15">
            <ns2:PhaseNumber>1</ns2:PhaseNumber>
            <ns2:Name>
                <Fr>Groupe Toplexil sirop, solution (or.)</Fr>
                <Nl>Groep Toplexil siroop, oplossing (or.)</Nl>
            </ns2:Name>
            <ns3:VirtualForm code="70">
                <ns4:Abbreviation>
                    <Fr>sol.</Fr>
                    <Nl>oploss.</Nl>
                </ns4:Abbreviation>
                <ns4:Name>
                    <Fr>solution buvable</Fr>
                    <Nl>oplossing voor oraal gebruik</Nl>
                </ns4:Name>
            </ns3:VirtualForm>
            <ns3:RouteOfAdministration code="57">
                <ns4:Name>
                    <Fr>Voie orale</Fr>
                    <Nl>Oraal gebruik</Nl>
                    <De>zum Einnehmen</De>
                    <En>Oral use</En>
                </ns4:Name>
                <ns3:StandardRoute standard="SNOMED_CT" code="26643006"/>
            </ns3:RouteOfAdministration>
        </ns3:Data>
        <ns3:VirtualIngredient rank="4">
            <ns3:Data from="2016-10-01" to="2019-09-15">
                <ns2:Type>ACTIVE_SUBSTANCE</ns2:Type>
                <ns2:Strength>
                    <NumeratorRange unit="mg">
                        <Min>33.3000</Min>
                        <Max>33.3000</Max>
                    </NumeratorRange>
                    <Denominator unit="mL">5.0000</Denominator>
                </ns2:Strength>
                <ns3:Substance code="4">
                    <ns4:Name>
                        <Fr>Paracétamol</Fr>
                        <Nl>Paracetamol</Nl>
                        <En>Paracetamol</En>
                    </ns4:Name>
                    <ns3:StandardSubstance standard="CAS" code="103-90-2"/>
                    <ns3:StandardSubstance standard="SNOMED_CT" code="387517004"/>
                </ns3:Substance>
            </ns3:Data>
        </ns3:VirtualIngredient>
        <ns3:VirtualIngredient rank="3">
            <ns3:Data from="2016-10-01" to="2019-09-15">
                <ns2:Type>ACTIVE_SUBSTANCE</ns2:Type>
                <ns2:Strength>
                    <NumeratorRange unit="mg">
                        <Min>1.7000</Min>
                        <Max>1.7000</Max>
                    </NumeratorRange>
                    <Denominator unit="mL">5.0000</Denominator>
                </ns2:Strength>
                <ns3:Substance code="281">
                    <ns4:Name>
                        <Fr>Oxomémazine</Fr>
                        <Nl>Oxomemazine</Nl>
                        <En>Oxomemazine</En>
                    </ns4:Name>
                    <ns3:StandardSubstance standard="SNOMED_CT" code="772837001"/>
                    <ns3:StandardSubstance standard="CAS" code="3689-50-7"/>
                </ns3:Substance>
            </ns3:Data>
        </ns3:VirtualIngredient>
        <ns3:VirtualIngredient rank="2">
            <ns3:Data from="2016-10-01" to="2019-09-15">
                <ns2:Type>ACTIVE_SUBSTANCE</ns2:Type>
                <ns2:Strength>
                    <NumeratorRange unit="mg">
                        <Min>33.3000</Min>
                        <Max>33.3000</Max>
                    </NumeratorRange>
                    <Denominator unit="mL">5.0000</Denominator>
                </ns2:Strength>
                <ns3:Substance code="5">
                    <ns4:Name>
                        <Fr>Benzoate de Sodium</Fr>
                        <Nl>Natriumbenzoaat</Nl>
                        <En>Sodium Benzoate</En>
                    </ns4:Name>
                    <ns3:StandardSubstance standard="CAS" code="532-32-1"/>
                    <ns3:StandardSubstance standard="SNOMED_CT" code="125706008"/>
                </ns3:Substance>
            </ns3:Data>
        </ns3:VirtualIngredient>
        <ns3:VirtualIngredient rank="1">
            <ns3:Data from="2016-10-01" to="2019-09-15">
                <ns2:Type>ACTIVE_SUBSTANCE</ns2:Type>
                <ns2:Strength>
                    <NumeratorRange unit="mg">
                        <Min>33.3000</Min>
                        <Max>33.3000</Max>
                    </NumeratorRange>
                    <Denominator unit="mL">5.0000</Denominator>
                </ns2:Strength>
                <ns3:Substance code="3">
                    <ns4:Name>
                        <Fr>Guaïfénésine</Fr>
                        <Nl>Guaifenesine</Nl>
                        <En>Guaifenesin</En>
                    </ns4:Name>
                    <ns3:StandardSubstance standard="CAS" code="93-14-1"/>
                    <ns3:StandardSubstance standard="SNOMED_CT" code="87174009"/>
                </ns3:Substance>
            </ns3:Data>
        </ns3:VirtualIngredient>
    </ns3:VmpComponent>
</ns3:Vmp>"""
}