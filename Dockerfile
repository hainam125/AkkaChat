FROM openjdk:8u181-jre-alpine

ENV PROJECT_HOME /usr/src/app

RUN mkdir -p $PROJECT_HOME

RUN apk add --no-cache bash

WORKDIR $PROJECT_HOME

EXPOSE 9000

CMD ./bin/$APP_NAME -Dhttp.port=9000

#docker build -t play-deploy .
#docker run --rm -v "$PWD/target/universal/stage:/usr/src/app" -e APP_NAME=chat-app -ip 9000:9000 play-deploy