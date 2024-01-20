FROM eclipse-temurin:17-alpine
COPY /target/forward-0.1.jar /usr/local/lib/forward.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/forward.jar"]
