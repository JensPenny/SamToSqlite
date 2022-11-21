FROM openjdk:17-slim AS run
RUN mkdir /app
COPY . /app
RUN apt-get update  \
    && apt-get install -y \
    curl  \
    wget  \
    unzip  \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /app/res/latest \
    && cd /app/res/shell/ \
    && sh getLatestVersion.sh

# Command to create the data folder with the sqlite db
# RUN echo $(ls -1 /app/res/latest) # debug command to see whats going on
RUN mkdir -p /opt/samtosql/
WORKDIR /app
ENTRYPOINT ["java","-jar","samtosqlite-0.1.0.jar"]