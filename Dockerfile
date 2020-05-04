FROM openjdk:8
ADD target/accounting-client.war accounting-client.war
EXPOSE 9966
ENTRYPOINT ["java", "-jar", "accounting-client.war"]