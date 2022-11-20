#!/bin/bash
echo "move to the correct folder"
#mkdir -p /home/samtosql_app/
cd /home/samtosql_app/SamToSqlite || exit
echo "create and/or empty result database"
mkdir -p /opt/samtosql
rm /opt/samtosql/*
echo "pull latest git changes"
git pull 
echo "build new docker image"
docker build -t penny/samtosql .
echo "starting new (self-destructing) container"
docker run \
  --rm \
  -v /opt/samtosql:/opt/samtosql \
  --name samtosql \
  -m 350mb \
  penny/samtosql
echo "sam export ran succesfully"

echo "ending script"