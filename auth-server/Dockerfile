FROM openjdk:8-jre-alpine

RUN apk --no-cache add curl

# Add Maintainer Info
LABEL maintainer="er.rakeshgarg02@gmail.com"

VOLUME /tmp

EXPOSE 9003

ADD build/libs/*.jar app.jar

COPY entrypoint.sh /entrypoint.sh

RUN chmod 777 /entrypoint.sh

CMD ["/bin/sh", "-c", "/entrypoint.sh"]
