FROM openjdk:13

MAINTAINER alexisgutierrezf.1997@gmail.com

VOLUME /tmp

EXPOSE 8761

ADD ./target/eureka-server.jar eureka-server.jar

ENTRYPOINT ["java", "-jar", "/eureka-server.jar"]