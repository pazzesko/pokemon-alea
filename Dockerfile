FROM openjdk:14-jdk-alpine
RUN addgroup -S user && adduser -S user -G user
USER user:user
ARG JAR_FILE=service/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xdebug" , "-Xrunjdwp:transport=dt_socket,address=*:7080,server=y,suspend=n", "-jar", "app.jar"]