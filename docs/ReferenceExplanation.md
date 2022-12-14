# Reference table

## AtcClassification

Contains the atc codes and description (in english) of these codes.

## DeliveryModus

Repository of the delivery modi.
For some weird stuff in the deliveryModus:

```    
<DeliveryModus code="M3 + M4">
        <Description>
            <ns2:Fr>Prescription médicale</ns2:Fr>
            <ns2:Nl>Medisch voorschrift</ns2:Nl>
            <ns2:De>Auf ärztliches Rezept</ns2:De>
            <ns2:En>Medical prescription</ns2:En>
        </Description>
    </DeliveryModus>
    <DeliveryModus code="M3+M4">
        <Description>
            <ns2:Fr>Prescription médicale spéciale + Prescription médicale restreinte</ns2:Fr>
            <ns2:Nl>Medisch voorschrift met bijzondere vermeldingen + Beperkt medisch voorschrift</ns2:Nl>
            <ns2:De>Arzneimittel auf besondere Verschreibung + Arzneimittel auf beschränkte Verschreibung</ns2:De>
            <ns2:En>Medical prescription with particular mentions + Restricted medical prescription</ns2:En>
        </Description>
    </DeliveryModus>
```

M3 + M4 has a semantic different meaning then M3+M4

## DeliveryModusSpecification

## DeviceType

the edqm-fields are empty.

## PackagingClosure

the edqm-fields are empty.

## PackagingMaterial

## PackagingType

## PharmaceuticalForm

## RouteOfAdministration

## Substance

Has a couple of fields, like note and chemical substance, but these are *very* empty.

## NoSwitchReason

## VirtualForm

## Wada

## NoGenericPrescriptionReason

## StandardForm

I have no idea why the documentation is what it is. It doesn't refer to a url.
It's a link-table between 'VirtualForm' and a code + standard. The standard encompasses the namespace of the standard we
refer to,
and the code is the code in that standard.

## StandardRoute

The same as standard form. Contains a standard and a link to the route of administration that it links toward

## StandardSubstance

The same as standard route, with the difference that this can link to multiple substances.
For example: I have no idea what this can mean:

```    
    <StandardSubstance standard="CAS" code="x">
        <Substance code="11107"/>
        <Substance code="11108"/>
        <Substance code="78"/>
    </StandardSubstance>
```

## StandardUnit

Issues: 
* `<StandardUnit name="1"/>` is ook dimensieloos, maar heeft geen content

## Appendix
Exists in the TOC of the documentation, but doesn't actually exist in the docs themselves. 
The tables do exist in the XML reference document though, so this is pretty confusing. 

## Parameter
Again, literally no documentation whatsoever. There is a reimbursementlaw-document that should shed light on this, but 
by god this documentation is dense and in most ways pretty hard to implement. I have no idea what the expectation here is.  

## ReimbursementCriterion
Civars stuff is included in samv2, don't get me wrong. It's just absolutely badly documented. 
This table is documented though, see RMBCRIT.