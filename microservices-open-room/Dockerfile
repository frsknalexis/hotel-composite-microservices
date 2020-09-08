FROM openjdk:13

MAINTAINER alexisgutierrezf.1997@gmail.com

VOLUME /tmp

EXPOSE 9001

ADD ./target/microservices-open-room.jar microservices-open-room.jar

ENTRYPOINT ["java", "-jar", "/microservices-open-room.jar"]