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
curl -O -L https://_:$GITHUB_PACKAGE_TOKEN@maven.pkg.github.com/JensPenny/SamToSqlite/com/jenspenny/samtosql/0.3.0/samtosql-0.3.0.jar
echo "build new docker image"
docker build -t penny/samtosql -f digitalocean.Dockerfile .
echo "removing old data"
rm -f -v /opt/samtosql/*
echo "starting new (self-destructing) container"
docker run \
  --rm \
  -v /opt/samtosql:/opt/samtosql \
  --name samtosql \
  -m 350mb \
  -v logs:/var/log \
  --log-driver local \
  --log-opt max-size=10m \
  --log-opt max-file=3 \
  penny/samtosql
echo "sam export ran succesfully"

echo "ending script"