FROM openjdk:8
ADD target/accounting-client.jar accounting-client.jar
EXPOSE 9966
ENTRYPOINT ["java", "-jar", "accounting-client.jar"]