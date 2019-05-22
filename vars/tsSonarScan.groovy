#!/usr/bin/groovy

def call(String key, String source, String exclusions) {
    stage('Sonarqube scan') {
        def scannerHome = tool 'DefaultScanner'

        withSonarQubeEnv('main') {
            sh "${scannerHome}/bin/sonar-scanner \
                -Dsonar.projectKey=${key} \
                -Dsonar.sources=${source} \
                -Dsonar.projectVersion=${env.PROJECT_VERSION} \
                -Dsonar.host.url=${SONAR_HOST_URL} \
                -Dsonar.login=${SONAR_AUTH_TOKEN} \
                -Dsonar.coverage.exclusions=${exclusions}"
        }
    }
}