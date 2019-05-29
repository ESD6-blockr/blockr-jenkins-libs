build-alpine:
	docker build . --target ALPINE -t blockr/jenkins-nodejs-slave:alpine-stable

build-debian:
	docker build . --target DEBIAN -t blockr/jenkins-nodejs-slave:debian-stable

push-alpine:
	docker push blockr/jenkins-nodejs-slave:alpine-stable

push-debian:
	docker push blockr/jenkins-nodejs-slave:debian-stable