
FROM openjdk:17-slim AS run
RUN mkdir /app
COPY . /app
RUN apk add --no-cache curl  \
     && mkdir -p /app/res/latest \
     && cd /app/res/shell/ \
     && sh getLatestVersion.sh \

# Command to create the data folder with the sqlite db
# RUN echo $(ls -1 /app/res/latest) # debug command to see whats going on
RUN mkdir -p /opt/samtosql/
WORKDIR /app
ENTRYPOINT ["java","-jar","SamToSqlite-0.1.0.jar"]