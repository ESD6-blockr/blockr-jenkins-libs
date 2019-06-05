#!/usr/bin/groovy

def call() {
     stage('Await sonarqube quality gate') {
        timeout(time: 10, unit: 'MINUTES') {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
                echo "The Build will be marked unstable due to quality gate failure: ${qg.status}"
                currentBuild.result = 'UNSTABLE'
            }
        }
    }
}
