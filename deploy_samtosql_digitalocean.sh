#!/bin/bash
echo "move to the correct folder"
#mkdir -p /home/samtosql_app/
cd /home/samtosql_app/SamToSqlite || exit
echo "create and/or empty result database"
mkdir -p /opt/samtosql
rm /opt/samtosql/*
echo "pull latest git changes. We only need the last version of the shell to get samv2 though, but just pulling everything"
git pull
echo "grab the latest fat jar. This will work since the jar will reside in the base folder"
curl -O -L https://_:$GITHUB_PACKAGE_TOKEN@maven.pkg.github.com/JensPenny/SamToSqlite/com/jenspenny/samtosql/0.3.4/samtosql-0.3.4.jar
echo "build new docker image"
docker build -t penny/samtosql -f digitalocean.Dockerfile .
echo "removing old data"
rm -f -v /opt/samtosql/*
# cd ~/logs # I used to pipe the docker run data to another file with less, but this was pretty slow. Better to run this on a screen imo
echo "starting new (self-destructing) container"
docker run \
  --rm \
  -v /opt/samtosql:/opt/samtosql \
  --name samtosql \
  -m 350mb \
  --log-driver local \
  --log-opt max-size=10m \
  --log-opt max-file=3 \
  penny/samtosql
RETURN_VALUE=$?
if [ $RETURN_VALUE -ne 0 ]; then
    echo "Sam export failed. Return code: $RETURN_VALUE."
    echo "ending script"
    exit 1
fi
echo "sam export ran succesfully"
echo "zipping result"
dbfiles=( /opt/samtosql/*.db )
[[ -e $dbfiles ]] || { echo "Matched no files" >&2; exit 1; }
dbname=$(basename $dbfiles .db)
zip /opt/samtosql/$dbname.zip $dbfiles
echo "moving zip to spaces and as latest export"
s3cmd put /opt/samtosql/$dbname.zip s3://samv2/ --acl-public
s3cmd put /opt/samtosql/$dbname.zip s3://samv2/latest/latest.zip --acl-public
echo "cleanup"
rm /opt/samtosql/* -v
echo "ending script"