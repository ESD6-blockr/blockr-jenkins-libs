#!/usr/bin/groovy

def call(String repo) {
    def version = env.PROJECT_VERSION
    def deployImageName
    def testImageName

    stage('Build') {
        deployImageName = "${repo}:${version}"
        testImageName = "${repo}-test:${version}"

        sh "docker build -t ${deployImageName} --build-arg 'VERSION=${version}' . "
        sh "docker build --target TEST -t ${testImageName} --build-arg 'VERSION=${version}' . "
    }

    stage('Test') {
        sh "docker run -v ${env.WORKSPACE}/coverage:/opt/coverage ${testImageName}"
    }    
}