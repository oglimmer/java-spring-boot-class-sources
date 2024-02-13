# we have to start with "FROM" this defines the base filesystem.
# as we want to build a java system, we use the "openjdk" base system
FROM eclipse-temurin:21-jdk as builder

WORKDIR /opt/build

# copy the "context root" into /opt/build
# for us "context root" is the base of this project
COPY . .

# execute mvn package
RUN ./mvnw package

# we start over again, as we only want to run java, we don't need a
# jdk anymore, use a jre instead
FROM eclipse-temurin:21-jre-alpine

# we need curl for the healthcheck in a docker compose
RUN apk add --update curl && \
    rm -rf /var/cache/apk/*

WORKDIR /opt/app

# copy the jar from the first build stage into this image
COPY --from=builder /opt/build/target/kniffel-0.0.1-SNAPSHOT.jar /opt/app/

# define how the container should run
CMD ["java", "-jar", "kniffel-0.0.1-SNAPSHOT.jar"]
