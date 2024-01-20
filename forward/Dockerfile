FROM eclipse-temurin:17-alpine
COPY /target/forward-0.1.jar /usr/local/lib/forward.jar
EXPOSE 8080 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar","/usr/local/lib/forward.jar"]
