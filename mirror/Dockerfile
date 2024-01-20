FROM eclipse-temurin:17-alpine
COPY /target/mirror-0.1.jar /usr/local/lib/mirror-0.1.jar
EXPOSE 8080 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar","/usr/local/lib/mirror-0.1.jar"]
