# AMP actual tables

The AMP-xml does have a tendency to repeat information that is available in reference tables. This does rather inflate the XML.  
There is no need for this though, since most of these things are available in the reference-export or the company-export. 

There is a small error in translation. AntiTemperingDevicePresent stands for anti tampering device.  

Content and strength are the same element-wise, but have other implementations

Commercialization and underlying data objects are also weird. The elements can be empty. 
Another thing is that Commercialization and SupplyProblem seem to be related. They seem to share fields in some cases, 
like reason and impact.  
For an example, see `<ns4:Ampp ctiExtended="000996-01">` that contains following commercialization-block:  
```
            <ns4:Commercialization>
                <ns4:Data from="2007-04-01" to="2020-10-20">
                    <EndOfCommercialization>
                        <ns2:Fr>Arrêt temporaire de la commercialisation</ns2:Fr>
                        <ns2:Nl>Tijdelijke stopzetting van de commercialisatie</ns2:Nl>
                        <ns2:En>Temporary end of commercialisation</ns2:En>
                    </EndOfCommercialization>
                    <Reason>
                        <ns2:Fr>Problèmes de production prolongés</ns2:Fr>
                        <ns2:Nl>Langdurige productieproblemen</ns2:Nl>
                        <ns2:De></ns2:De>
                        <ns2:En>Long-term production problems</ns2:En>
                    </Reason>
                    <Impact>
                        <ns2:Fr>-</ns2:Fr>
                        <ns2:Nl>-</ns2:Nl>
                        <ns2:De></ns2:De>
                        <ns2:En>-</ns2:En>
                    </Impact>
                </ns4:Data>
            </ns4:Commercialization>
```

There is a fun dmpp with an older data-segment where cheap is false, but cheapest is true. See drfGOPWJcLLfNA5X1jH+fJn2QmNorrRV2JsSPZ9dTbQ=. 
This looks fixed though. 

I have no idea why the table AMPC_FAMHP exists. The explanation in the docs say the following: 
``
• AMP Code is created by FAMHP;
• FAMHP creates an AMPC to add information about the pharmaceutical form (see section 3.1.2.3) and the route of administration (see section 3.1.4.3) which are linked to this entity (see relationships).
``
But why wouldn't you just leave the rest nullable in that case? Makes no sense. 

##Amp data quirks
* `abbreviatedName` should be mandatory, but some data elements don't contain this data
* The same for the other non-null fields here

##AmpComponent quirks
* Ampcomponent has a `crushable` property in the docs, but this is never filled in the actual export
* Ampcomponent has a `modified release type` property in the docs. This is never used, but modified release couplings are made through the link 
between ampcomponent and pharmaceutical form. The docs should reflect this though. 
* For the property `Note`: I don't think that any note is filled in on AMP-Component level. 
* Property `Concentration` is documented but doesn't exist in the export. 
* Property `Osmotic Concentration` is docunented but doesn't exist in the export. 
* Property `Caloric value` is documented but doesn't exist in the export. 

Two reference-tables are noted but aren't really spoonfed by the documentation. These are: 
* AMPC_TO_PHARMFORM: a table that links an amp component to 1 or more pharmaceutical forms
* AMPC_TO_ROA: a table that links an amp component to 1 or more routes of administration.