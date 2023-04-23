FROM eclipse-temurin:17-jdk
COPY build/libs/baltic-0.0.1-SNAPSHOT.jar baltic.jar
ENTRYPOINT [ "java", "-jar", "baltic.jar" ]