# we have to start with "FROM" this defines the base filesystem.
# as we want to build a java system, we use the "openjdk" base system
FROM eclipse-temurin:21-jdk as builder

WORKDIR /opt/build

# copy the "context root" into /opt/build
# for us "context root" is the base of this project
COPY . .

# execute mvn verify
RUN ./mvnw verify

# we start over again, as we only want to run java, we don't need a
# jdk anymore, use a jre instead
FROM eclipse-temurin:21-jre-alpine

WORKDIR /opt/app

# copy the jar from the first build stage into this image
COPY --from=builder /opt/build/target/kniffel-0.0.1-SNAPSHOT.jar /opt/app/

# define how the container should run
CMD ["java", "-jar", "kniffel-0.0.1-SNAPSHOT.jar"]
