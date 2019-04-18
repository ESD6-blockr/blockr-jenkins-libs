#!/usr/bin/groovy

def call(String folder) {
    stage('Archive') {
        archiveArtifacts artifacts: folder
    }
}