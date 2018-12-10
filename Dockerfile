FROM openjdk:8u181-jre-alpine

ENV PROJECT_HOME /usr/src/app

RUN mkdir -p $PROJECT_HOME

RUN apk add --no-cache bash

WORKDIR $PROJECT_HOME

#ADD ./target/universal/stage ./

EXPOSE 9000

CMD ./bin/$APP_NAME -Dhttp.port=9000

#docker build -t play-deploy .
#avoid using $PWD and $HOME in inner docker when using jenkins-docker (using absolute path of server)
#docker run --rm -v "$PWD/target/universal/stage:/usr/src/app" -e APP_NAME=chat-app -ip 9000:9000 play-deploy
#docker run --rm -v "D:/Docker/ChatApp/target/universal/stage:/usr/src/app" -e APP_NAME=chat-app -ip 9000:9000 play-deploy