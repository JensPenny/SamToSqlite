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

### 3. Full export. Most tables are transactional, except the AMP
```
22:59:12.764 [main] INFO  SamV2Exporter - AMP file parsed in 14:58
22:59:12.764 [main] INFO  SamV2Exporter - CHAPTERIV file parsed in 4:24
22:59:12.764 [main] INFO  SamV2Exporter - CMP file parsed in 0:34
22:59:12.764 [main] INFO  SamV2Exporter - CPN file parsed in 0:6
22:59:12.764 [main] INFO  SamV2Exporter - NONMEDICINAL file parsed in 1:21
22:59:12.764 [main] INFO  SamV2Exporter - REF file parsed in 0:14
22:59:12.764 [main] INFO  SamV2Exporter - RMB file parsed in 3:46
22:59:12.764 [main] INFO  SamV2Exporter - RML file parsed in 0:22
22:59:12.764 [main] INFO  SamV2Exporter - VMP file parsed in 0:28
22:59:12.764 [main] INFO  SamV2Exporter - Full export parsed in 26:18
```

### 4. Updated to ubuntu 22.04 and performance fell off a cliff :(. 
Still troubleshooting, but its pretty hard w/o a profiler.
While these transactions are interesting for me, the real test will happen when we run this on a container and the dinky DO instance I have running.
```
22:02:00.906 [main] INFO  SamV2Exporter - AMP file parsed in 21:18
22:02:00.907 [main] INFO  SamV2Exporter - CHAPTERIV file parsed in 8:19
22:02:00.907 [main] INFO  SamV2Exporter - CMP file parsed in 0:43
22:02:00.908 [main] INFO  SamV2Exporter - CPN file parsed in 0:28
22:02:00.908 [main] INFO  SamV2Exporter - NONMEDICINAL file parsed in 3:19
22:02:00.908 [main] INFO  SamV2Exporter - REF file parsed in 0:35
22:02:00.909 [main] INFO  SamV2Exporter - RMB file parsed in 8:37
22:02:00.909 [main] INFO  SamV2Exporter - RML file parsed in 1:13
22:02:00.909 [main] INFO  SamV2Exporter - VMP file parsed in 1:15
22:02:00.910 [main] INFO  SamV2Exporter - Full export parsed in 45:52
```

### 5. Digitalocean performance
Running this on a $6 1Gb ram + 1vCPU DO server, the export takes about 30 minutes without the RML-export.  
I'm not going to lie, I'm pretty happy with that result, especially because we don't use that much RAM with this export.
