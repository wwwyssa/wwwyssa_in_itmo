FROM eclipse-temurin:21-jdk as builder
WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle/ gradle/

RUN chmod +x gradlew

COPY src ./src

RUN ./gradlew clean build

FROM quay.io/wildfly/wildfly:latest-jdk21

COPY --from=builder /app/build/libs/*.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

EXPOSE 8080 9990

CMD /opt/jboss/wildfly/bin/add-user.sh -u admin -p admin --silent && /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0