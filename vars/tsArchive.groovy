#!/usr/bin/groovy

def call() {
    stage('Archive') {
        archiveArtifacts artifacts: 'dist/'
    }
}