FROM openjdk:13

MAINTAINER alexisgutierrezf.1997@gmail.com

VOLUME /tmp

EXPOSE 9003

ADD ./target/hotel-room-composite.jar hotel-room-composite.jar

ENTRYPOINT ["java", "-jar", "/hotel-room-composite.jar"]