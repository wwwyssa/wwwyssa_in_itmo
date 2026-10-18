FROM node:24-alpine AS frontend

WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY frontend/ ./
RUN npm run build

FROM gradle:9.6.0-jdk17-alpine AS build

WORKDIR /home/gradle/project
COPY --chown=gradle:gradle build.gradle settings.gradle ./
COPY --chown=gradle:gradle src ./src
COPY --from=frontend --chown=gradle:gradle /build/frontend ./build/frontend
RUN gradle --no-daemon -PfrontendPrebuilt war copyPostgresDriver copyEclipseLink

FROM quay.io/wildfly/wildfly:35.0.0.Final-jdk17

COPY --from=build --chown=jboss:0 /home/gradle/project/build/jdbc/postgresql.jar \
    /opt/jboss/wildfly/modules/org/postgresql/main/postgresql.jar
COPY --chown=jboss:0 backend/postgresql-module.xml \
    /opt/jboss/wildfly/modules/org/postgresql/main/module.xml

COPY --from=build --chown=jboss:0 /home/gradle/project/build/eclipselink/eclipselink.jar \
    /opt/jboss/wildfly/modules/system/layers/base/org/eclipse/persistence/main/eclipselink.jar
COPY --chown=jboss:0 backend/eclipselink-module.xml \
    /opt/jboss/wildfly/modules/system/layers/base/org/eclipse/persistence/main/module.xml

COPY --chown=jboss:0 backend/configure.cli /opt/jboss/configure.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/jboss/configure.cli \
    && rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history

COPY --from=build --chown=jboss:0 /home/gradle/project/build/libs/laba1-1.0-SNAPSHOT.war \
    /opt/jboss/wildfly/standalone/deployments/laba1.war
