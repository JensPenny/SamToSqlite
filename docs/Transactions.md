# Batch transactions and timing improvements

## Testing single transactions vs bulk
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

## Intermediate full export tests
### 1. Full export during AMP implementation 
```
22:07:31.831 [main] INFO  SamV2Exporter - AMP file parsed in 11:52
22:07:31.831 [main] INFO  SamV2Exporter - CMP file parsed in 0:33
22:07:31.831 [main] INFO  SamV2Exporter - CPN file parsed in 2:24
22:07:31.831 [main] INFO  SamV2Exporter - NONMEDICINAL file parsed in 20:40
22:07:31.831 [main] INFO  SamV2Exporter - REF file parsed in 6:27
22:07:31.831 [main] INFO  SamV2Exporter - Full export parsed in 41:59
```
* Nonmedicinal can use some improvements, for example when we batch by 50 inserts. I might try that
* A partial AMP export with most stuff implemented and that batches for each amp takes ~12 minutes. Not that bad too be
honest, especially since the file is 1.1 GB on disk. We'll just have to see how this works in a container. 

### 2. Full export. Extra AMP fields but still not complete. NONMEDICINAL transacts per 100
```
09:14:05.957 [main] INFO  SamV2Exporter - AMP file parsed in 15:35
09:14:05.957 [main] INFO  SamV2Exporter - CMP file parsed in 0:32
09:14:05.957 [main] INFO  SamV2Exporter - CPN file parsed in 2:27
09:14:05.957 [main] INFO  SamV2Exporter - NONMEDICINAL file parsed in 1:21
09:14:05.957 [main] INFO  SamV2Exporter - REF file parsed in 6:40
09:14:05.957 [main] INFO  SamV2Exporter - Full export parsed in 26:37
```


