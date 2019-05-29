#!/usr/bin/groovy

def call(String operator, Map settings) {
    stage('Test') {
        "sh ${operator} test"
    }

    stage('Record results') {
        junit 'junit.xml'
        step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
    }
}