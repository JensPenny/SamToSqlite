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
- [x] Automatically download the files from the samv2 site
- [x] Export all files to a sqlite table
- [ ] Zip the sqlite database
- [ ] Dump the zipped .db to a blob storage file
- [ ] Let this job run daily in a container. Check if it survives and doesn't pull down the docker instance
- [ ] Make this file available somewhere

## For local testing
The database itself gets stored under /opt/samtosql. This is so you can swap out the volume on the container with ease.
For local testing you probably don't have root under /opt/. Just create the folder and give your local user rights to this folder.

sudo mkdir /opt/samtosql  
sudo chown -R $USER:$USER /opt/samtosql

## Creating a new version
This is a list of steps I 'think' I'll have to do to make a new version
1. Change the version in the build.gradle file
2. Change the dockerfiles so they run the new versions. You can anticipate the name the files are gonna have
3. Change the shell script so it downloads the new jar with the curl-command
4. Push all changes to github 
5. Make a new release on github. This should create a new deployment through the github actions interface with an artefact
6. Log in to digitalocean and update the repo that contains the script. Check if you need to update the script that runs the export