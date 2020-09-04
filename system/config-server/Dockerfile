FROM openjdk:13

MAINTAINER alexisgutierrezf.1997@gmail.com

VOLUME /tmp

EXPOSE 8888

ADD ./target/config-server.jar config-server.jar

ENTRYPOINT ["java", "-jar", "/config-server.jar"]