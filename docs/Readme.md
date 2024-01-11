# Sam v2 to SQLite export

## What
This program will attempt to export the samv2 belgian medication databases found on https://www.vas.ehealth.fgov.be/websamcivics/samcivics/download/samv2.html 
in a format that doesn't boggle the mind.  
The tables are in the format described in the PIM-document on https://www.samportal.be/nl/sam/documentation.  
Extra documentation can be found under the same link, in the 'export' document

## Where
The files that are generated are available on the following S3 space: [samV2 space](https://samv2.ams3.digitaloceanspaces.com/)

You can find the latest version under following URI: [latest version - WARNING: direct download](https://samv2.ams3.digitaloceanspaces.com/latest/latest.zip)  
Other versions are zipped under the root space, with <version>.zip as name.   
Versions get constructed once a day at night.  

## Features
The idea is to support a download of the xml-export, but as a straight database file. The big idea here is that an xml export 
is way easier to export to a postgres, or another database, since straight search in xml files is painful at best.  
The download is not there yet, but these phases are being implemented: 
- [x] Automatically download the files from the samv2 site
- [x] Export all files to a sqlite table
- [x] Zip the sqlite database
- [x] Dump the zipped .db to a blob storage file
- [x] Let this job run daily in a container. Check if it survives and doesn't pull down the docker instance
- [x] Make this file available somewhere

## For local testing
The database itself gets stored under /opt/samtosql. This is so you can swap out the volume on the container with ease.
For local testing you probably don't have root under /opt/. Just create the folder and give your local user rights to this folder.

sudo mkdir /opt/samtosql  
sudo chown -R $USER:$USER /opt/samtosql

## Creating a new version
This is a list of steps that need to be done to create a new version
1. Change the version in the build.gradle file
2. Change the dockerfiles so they run the new versions. You can anticipate the name the files are gonna have
3. Change the shell script so it downloads the new jar with the curl-command
4. Push all changes to github 
5. Make a new release on github. This should create a new deployment through the github actions interface with an artefact
~~6. Log in to digitalocean and update the repo that contains the script. Check if you need to update the script that runs the export~~
6. The DO droplet has a self-updating script. The only thing that needs to happen is that a new release needs to be made. 

## Adding a new table
Adding a new table is pretty easy:  
1. Add the database scheme to the relevant class in the db - package
2. Fill that database scheme with inserts in the relevant parser. Don't forget to update the counter so we don't overextend on transaction memory
3. Add the table to the drop and create statements in DBInitialization