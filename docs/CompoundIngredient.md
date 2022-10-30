# Compounding ingredients table

## CompoundingIngredient and compoundingFormula
The docs kind of allude to this, but this object is more complex then the documentation lets on. They just define this as a 
'complex' type, but this can be reduced to some real, normalized tables. 

The docs also require a cnk, but the xml itself is constructed as a namespace + id. The export here splits these up, we 
follow the data-model in the xml, not the theoretical model in the docs.

For that reason both tables are split into 2 parts: 
* An ingredient or formula-part, that defines the ingredient or formula. 
* A synonym-table that links the ingredient or formula to a list of names that describe the ingredient or the formula
