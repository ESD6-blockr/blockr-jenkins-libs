#!/usr/bin/groovy

def call(Map settings) {
    stage('Test') {
        !settings.yarn ? 'npm run test' : 'yarn test'
    }

    stage('Record results') {
        junit 'junit.xml'
        step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
    }
}