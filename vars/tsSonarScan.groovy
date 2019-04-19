#!/usr/bin/groovy

def call(Map sonarSettings) {
    stage('Sonarqube scan') {
        def scannerHome = tool 'DefaultScanner'

        withSonarQubeEnv('main') {
            sh "${scannerHome}/bin/sonar-scanner \
                -Dsonar.projectKey=${sonarSettings.key} \
                -Dsonar.sources=${sonarSettings.source} \
                -Dsonar.projectVersion=${env.PROJECT_VERSION} \
                -Dsonar.host.url=${SONAR_HOST_URL} \
                -Dsonar.login=${SONAR_AUTH_TOKEN}"
        }
    }
}