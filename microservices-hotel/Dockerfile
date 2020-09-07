FROM openjdk:13

MAINTAINER alexisgutierrezf.1997@gmail.com

VOLUME /tmp

EXPOSE 9002

ADD ./target/microservices-hotel.jar microservices-hotel.jar

ENTRYPOINT ["java", "-jar", "/microservices-hotel.jar"]