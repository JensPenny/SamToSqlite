#!/bin/bash
# We have more power on ovh, so we can build locally and deploy here
echo "move to the correct folder"
#mkdir -p /home/samtosql_app/
cd /home/debian/samv2/SamToSqlite || exit
echo "create and/or empty result database"
### Check if /opt/samtosql exists ###
if [ ! -d "/opt/samtosql/" ]
then
    sudo mkdir -p /opt/samtosql/
fi
### Check if the user has write access to that folder
if [ "$(find "/opt/samtosql/" -not -user "$USER")" ]
then
  sudo chown -R "$USER:$USER" /opt/samtosql
fi

echo "removing old data"
rm -f -v /opt/samtosql/*

echo "pull latest git changes. We only need the last version of the shell to get samv2 though, but just pulling everything"
git pull

### Build the docker image
echo "build new docker image"
docker build -t penny/samtosql -f local.Dockerfile .

# cd ~/logs # I used to pipe the docker run data to another file with less, but this was pretty slow. Better to run this on a screen imo
echo "starting new (self-destructing) container"
docker run \
  --rm \
  -v /opt/samtosql:/opt/samtosql \
  --name samtosql \
  -m 500mb \
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
zip -D /opt/samtosql/$dbname.zip $dbfiles
echo "moving zip to spaces and as latest export"

/usr/bin/rclone copyto /opt/samtosql/$dbname.zip ovh-rbx:samv2/hist/$dbname.zip \
  --s3-acl public-read \
  --stats 1m \
  --log-level INFO

/usr/bin/rclone copyto /opt/samtosql/$dbname.zip ovh-rbx:samv2/latest/latest.zip \
  --s3-acl public-read \
  --stats 1m \
  --log-level INFO

echo "ending script"
