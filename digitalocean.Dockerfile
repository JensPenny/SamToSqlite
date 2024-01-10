FROM openjdk:23-slim AS run
RUN mkdir /app
COPY . /app

# We ALWAYS want to run the run-command, since it will DL the latest sam version.
# A little hack makes sure this happens: src-https://stackoverflow.com/questions/35134713/disable-cache-for-specific-run-commands
ADD "https://www.random.org/cgi-bin/randbyte?nbytes=10&format=h" skipcache
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
ENTRYPOINT ["java","-jar","samtosql-0.3.6.jar"]