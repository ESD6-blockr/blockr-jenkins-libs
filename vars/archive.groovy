#!/usr/bin/groovy

def call(String folder) {
    stage('Archive') {
        echo "${folder}"
        archiveArtifacts artifacts: "${folder}"
    }
}