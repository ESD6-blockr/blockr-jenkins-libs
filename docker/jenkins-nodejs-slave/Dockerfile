FROM jenkins/slave:alpine as ALPINE
USER root
RUN apk add npm nodejs python alpine-sdk autoconf libpng-dev automake libtool nasm
RUN node -v && npm -v
USER jenkins

FROM jenkins/slave:latest as DEBIAN
USER root
RUN apt-get update
RUN apt-get install curl software-properties-common -y
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash -
RUN apt-get install nodejs python autoconf libpng-dev automake libtool nasm build-essential -y
RUN node -v && npm -v
USER jenkins