FROM jenkins/slave:alpine as ALPINE
USER root
RUN apk add npm nodejs python alpine-sdk autoconf libpng-dev automake libtool nasm
RUN node -v && npm -v
RUN npm install -g yarn
RUN chown -R jenkins:jenkins /home/jenkins
USER jenkins
