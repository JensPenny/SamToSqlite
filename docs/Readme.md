# Sam v2 to SQLite export

## What
This program will attempt to export the samv2 belgian medication databases found on https://www.vas.ehealth.fgov.be/websamcivics/samcivics/download/samv2.html 
in a format that doesn't boggle the mind.  
The tables are in the format described in the PIM-document on https://www.samportal.be/nl/sam/documentation.  
Extra documentation can be found under the same link, in the 'export' document

## Todo
The idea is to support a download of the xml-export, but as a straight database file. The big idea here is that an xml export 
is way easier to export to a postgres, or another database, since straight search in xml files is painful at best.  
The download is not there yet, but these phases are being implemented: 
- [ ] Automatically download the files from the samv2 site
- [x] Export all files to a sqlite table
- [ ] Zip the sqlite database
- [ ] Dump the zipped .db to a blob storage file
- [ ] Let this job run daily in a container. Check if it survives and doesn't pull down the docker instance
- [ ] Make this file available somewhere