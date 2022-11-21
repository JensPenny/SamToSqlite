FROM gradle:7.4.2-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# RUN echo $(ls -1 /home/gradle/src) # debug command to see whats going on
# RUN echo $(ls -1 /home/gradle/src/res) # debug command to see whats going on
RUN apk add --no-cache curl  \
     && mkdir -p /home/gradle/src/res/latest \
     && cd /home/gradle/src/res/shell/ \
     && sh getLatestVersion.sh \
     && cd /home/gradle/src \
     && gradle build --no-daemon

FROM openjdk:17-slim AS run
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/ /app/
COPY --from=build /home/gradle/src/res/latest /app/res/latest
# Command to create the data folder with the sqlite db
# RUN echo $(ls -1 /app/res/latest) # debug command to see whats going on
RUN mkdir -p /opt/samtosql/
WORKDIR /app
ENTRYPOINT ["java","-jar","SamToSqlite-0.1.0.jar"]