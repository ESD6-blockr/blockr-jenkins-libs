#!/usr/bin/groovy

def call(String key) {
    stage('Maven build and scan') {
        withSonarQubeEnv('main') {
            sh "mvn clean package sonar:sonar \
                -Dsonar.projectKey=${key} \
                -Dsonar.projectVersion=${env.PROJECT_VERSION} \
                -Dsonar.host.url=${SONAR_HOST_URL} \
                -Dsonar.login=${SONAR_AUTH_TOKEN}"
        }
    }
}