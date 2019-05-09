#!/usr/bin/groovy

def call() {
    stage('Test') {
        sh 'npm run test'
    }

    stage('Record results') {
        junit 'junit.xml'
        step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
    }
}