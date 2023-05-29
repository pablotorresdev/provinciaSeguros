FROM adoptopenjdk:8-jdk-hotspot
WORKDIR /app
COPY target/provincia-seguros-*.jar provincia-seguros.jar
EXPOSE 8080
CMD ["java", "-jar", "provincia-seguros.jar"]