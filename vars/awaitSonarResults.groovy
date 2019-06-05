#!/usr/bin/groovy

def call() {
     stage('Await sonarqube quality gate') {
        timeout(time: 10, unit: 'MINUTES') {
            try {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    echo "The Build will be marked unstable due to quality gate failure: ${qg}"
                    currentBuild.result = 'UNSTABLE'
                }
            } catch (all) {
                currentBuild.result = 'UNSTABLE'
            }
        }
    }
}
