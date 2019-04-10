def call(Map sonarSettings = null) {
    if (sonarSettings != null) {
        stage('Build with sonarube scan') {
            withCredentials([usernamePassword(credentialsId: "sonar.${sonarSettings.key}", passwordVariable: 'token', usernameVariable: 'user')]) {
                withSonarQubeEnv('main') {
                    sh "mvn clean package sonar:sonar \
                        -Dsonar.projectKey=${sonarSettings.key} \
                        -Dsonar.host.url=${sonarSettings.host} \
                        -Dsonar.login=${token}"
                }
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