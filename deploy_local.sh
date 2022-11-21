#!/bin/bash

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

### Build the docker image
echo "Building image and running locally"
docker build -t penny/samtosql -f local.Dockerfile .
docker run --rm \
          -v /opt/samtosql_docker:/opt/samtosql \
          --name samtosql \
          -m 500mb \
          penny/samtosql