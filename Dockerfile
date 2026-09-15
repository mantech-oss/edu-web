ARG TOMCAT_IMAGE=tomcat:8.5-jdk8-temurin

FROM ${TOMCAT_IMAGE} AS builder
WORKDIR /build

COPY src/main/java ./src/main/java
COPY src/main/webapp ./webapp

RUN mkdir -p /build/webapp/WEB-INF/classes \
    && find /build/src/main/java -name '*.java' > /build/sources.txt \
    && javac -source 8 -target 8 -encoding UTF-8 \
       -cp /usr/local/tomcat/lib/servlet-api.jar \
       -d /build/webapp/WEB-INF/classes \
       @/build/sources.txt \
    && cd /build/webapp \
    && jar -cf /build/ROOT.war .

FROM ${TOMCAT_IMAGE}
COPY --from=builder /build/ROOT.war /usr/local/tomcat/webapps/ROOT.war
RUN mkdir -p /mnt
EXPOSE 8080
CMD ["catalina.sh", "run"]
