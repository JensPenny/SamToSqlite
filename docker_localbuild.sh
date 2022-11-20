#!/bin/bash
echo "build new docker file"
docker build . -t penny/samtosql
echo "stopping existing container"
docker stop samtosql
echo "starting new container"
docker run \
--name samtosql \
-v /opt/samtosql_docker/:/opt/samtosql \
-d penny/samtosql \
--entrypoint /bin/bash # debugging purposes
echo "ending script samtosql_docker_local"