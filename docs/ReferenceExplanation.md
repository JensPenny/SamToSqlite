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
It's a link-table between 'VirtualForm' and a code + standard. The standard encompasses the namespace of the standard we refer to, 
and the code is the code in that standard.  

## StandardRoute
The same as standard form. Contains a standard and a link to the route of administration that it links toward

