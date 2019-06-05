#!/usr/bin/groovy

def call() {
     stage('Await sonarqube quality gate') {
        timeout(time: 10, unit: 'MINUTES') {
            try {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    echo "The Build will be marked unstable due to quality gate failure: ${qg}"
                    echo '1'
                    currentBuild.result = 'UNSTABLE'
                    echo '2'
                }
            } catch (all) {
                echo '3'
                currentBuild.result = 'UNSTABLE'
            }
        }
    }
}
