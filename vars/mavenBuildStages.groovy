def call(String version, Map sonarSettings = null) {
    if (sonarSettings != null) {
        stage('Build with sonarube scan') {
            withSonarQubeEnv('main') {
                    sh "mvn clean package sonar:sonar \
                        -Dsonar.projectKey=${sonarSettings.key} \
                        -Dsonar.projectVersion=${env.PROJECT_VERSION} \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_AUTH_TOKEN}"
            }
        }

        stage('Await sonarqube quality gate') {
            timeout(time: 10, unit: 'MINUTES') {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    error "Pipeline aborted due to quality gate failure: ${qg.status}"
                }
            }
        } 
    }
}