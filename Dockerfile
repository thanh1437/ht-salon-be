FROM openjdk:11-jdk
COPY meeting-booking-1.0.jar meeting-booking-1.0.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Dspring.profiles.active=compose -jar meeting-booking-1.0.jar