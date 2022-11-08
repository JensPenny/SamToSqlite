# Chapter 4 tables
There is a quirk that is featured in multiple data elements in the export: 
* I don't think `createdTimestamp` and `createdUserId` are documented. These are contained in multiple data-elements.

## Paragraph quirks
* Field `legalReference` is documented but is never used in the export. This should be a mandatory field though
* `publicationDate` can be null. 
```
        <ns2:Data from="2014-03-01" to="2018-03-31">
            <CreatedTimestamp>2022-09-02T01:31:04.480</CreatedTimestamp>
            <CreatedUserId>INAMI</CreatedUserId>
            <KeyStringNl>groeistoornissen bij kinderen vanaf 2 jaar en adolescenten met ernstige primaire insulineachtige-groeifactor-1-deficiëntie (primaire IGFD)</KeyStringNl>
            <KeyStringFr>retards de croissance chez l’enfant à partir de 2 ans et l’adolescent présentant un déficit primaire sévère en IGF-1 (IGFD primaire)</KeyStringFr>
            <ProcessType>7</ProcessType>
            <ProcessTypeOverrule>PQE</ProcessTypeOverrule>
            <ParagraphVersion>7</ParagraphVersion>
            <ModificationStatus>E</ModificationStatus>
        </ns2:Data>
```

## Verse quirks
* Field `legalReference` is documented but is never used in the export.

## Added document quirks
* Field `mimeType` is documented but is never used in the export.
* Field `documentContent` is documented but is never used in the export.

# Qualification list
* Field `purchasingAdvisorName` is documented but is never used in the export.
