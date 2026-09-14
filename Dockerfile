ARG MAVEN_IMAGE=maven:3.9.9-eclipse-temurin-8
ARG TOMCAT_IMAGE=tomcat:8.5-jdk8-temurin

FROM ${MAVEN_IMAGE} AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM ${TOMCAT_IMAGE}
COPY --from=builder /build/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war
RUN mkdir -p /volume
EXPOSE 8080
CMD ["catalina.sh", "run"]
