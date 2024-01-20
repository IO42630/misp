FROM eclipse-temurin:17-alpine
COPY /target/mirror-0.1.jar /usr/local/lib/mirror-0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/mirror-0.1.jar"]
