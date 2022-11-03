# Testing single transactions vs bulk
To get a bit of a speed boost we will attempt to check if there is a difference between
persisting single transactions and multiple transactions. The hypothesis is that pooling 
the statements will have a large effect, since sqlite should be optimized for bulky updates. 

For this we exported the CMP file with components.
* Single transaction for each CMP row without exposed logger - 1:53 min
* Multi transaction for each CMP row without exposed logger  - 0:36 min

* Single transaction for each CMP row with exposed logger - 1:57 min
* Multi transaction for each CMP row with exposed logger - 0:35 min

Using transactions in this case is a no-brainer. We'll still use a transaction for each top-level element, 
just to keep memory in check, but transacting all changes for each element looks like the way to go. 
