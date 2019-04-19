#!/usr/bin/groovy

def call(Map sonarSettings) {
    stage('Maven build and scan') {
        withSonarQubeEnv('main') {
            sh "mvn clean package sonar:sonar \
                -Dsonar.projectKey=${sonarSettings.key} \
                -Dsonar.projectVersion=${env.PROJECT_VERSION} \
                -Dsonar.host.url=${SONAR_HOST_URL} \
                -Dsonar.login=${SONAR_AUTH_TOKEN}"
        }
    }
}