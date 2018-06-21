FROM 10.31.57.177/dev1-ci/jdk:8u151
EXPOSE 8088

ENV LANG=en_US.utf8
ADD ./target/bh-eureka-server-ha-0.0.1.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]