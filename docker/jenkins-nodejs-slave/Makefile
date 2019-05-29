build-alpine:
	docker build alpine/ --target ALPINE -t blockr/jenkins-nodejs-slave:alpine-stable

build-debian:
	docker build debian/ --target DEBIAN -t blockr/jenkins-nodejs-slave:debian-stable

push-alpine:
	docker push blockr/jenkins-nodejs-slave:alpine-stable

push-debian:
	docker push blockr/jenkins-nodejs-slave:debian-stable