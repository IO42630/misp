FROM eclipse-temurin:17-alpine
COPY /target/reverse-0.1-jar-with-dependencies.jar /usr/local/lib/reverse-0.1-jar-with-dependencies.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/usr/local/lib/reverse-0.1-jar-with-dependencies.jar"]
