FROM eclipse-temurin:17-alpine
COPY /target/reverse-0.1-jar-with-dependencies.jar /usr/local/lib/reverse-0.1-jar-with-dependencies.jar
EXPOSE 8090 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar","/usr/local/lib/reverse-0.1-jar-with-dependencies.jar"]
