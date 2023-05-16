FROM openjdk:17
COPY ./target/retroframe.jar /retroframe/app/retroframe.jar
WORKDIR /retroframe/data
EXPOSE 8080
#HOST PORT 16880
ENTRYPOINT ["java", "-jar","/memology-app/backend/memestore-backend.jar"]