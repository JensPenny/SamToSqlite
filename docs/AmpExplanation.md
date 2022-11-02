# AMP actual tables

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

