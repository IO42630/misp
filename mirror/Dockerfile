FROM eclipse-temurin:17-alpine
COPY /target/mirror-0.1.war /usr/local/lib/mirror-0.1.war
EXPOSE 8090
ENTRYPOINT ["java","-jar","/usr/local/lib/mirror-0.1.war"]
