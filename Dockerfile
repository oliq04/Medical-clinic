FROM eclipse-temurin:21-jdk
LABEL authors="oliwi"
COPY /target/medicalclinic-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "/app/medicalclinic-0.0.1-SNAPSHOT.jar"]