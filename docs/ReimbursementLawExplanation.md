# RML explanations and quirks
This export looks pretty easy. Just three base-tables, right?  
The hard part starts when you notice that the references can refer to each other. One reference as a chapter can have multiple 
sub-elements. The last element is always a legal text though.  

The docs also mark this as 'deprecated'. See 3.2: Reimbursement Law Definition Part in the documentation. 
However, I'd take a guess and say this data is still needed if you are implementing civars-logic.
```
Deprecated. NIHDI will not be using this structure to model the Reimbursement Law. Instead, only Chapter IV and VIII data will be migrated directly from SAM v1. 

The Reimbursement Law definition will remain in SAM for non-Chapter IV legislation. This legislation did not have Formal Interpretations – that part has been removed completely from SAM.
```

## LegalBasis

## LegalReference
A legalreference has a field `legalReferencePath'. This is a 

## LegalText
legalRefKey = varchar("key", 70) //Own field to persist the key as well, since legalReferencePath is a combo field
val legalReferencePath