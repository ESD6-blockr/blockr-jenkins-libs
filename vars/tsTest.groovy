#!/usr/bin/groovy

def call(Map settings) {
    stage('Test') {
        if (!settings.yarn) {
            sh 'npm run test'
        } else {
            sh 'yarn test'
        }
    }

    stage('Record results') {
        junit 'junit.xml'
        step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
    }
}