#!/usr/bin/groovy

def call() {
    stage('Cleanup') {
        step([$class: 'WsCleanup'])
    }
}