FROM openjdk:13

MAINTAINER alexisgutierrezf.1997@gmail.com

VOLUME /tmp

EXPOSE 8090

ADD ./target/hotel-room-zuul-proxy.jar hotel-room-zuul-proxy.jar

ENTRYPOINT ["java", "-jar", "/hotel-room-zuul-proxy.jar"]