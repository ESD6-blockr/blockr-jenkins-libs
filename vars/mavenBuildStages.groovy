#!/usr/bin/groovy

def call(Map settings) {
    if (settings.sonar_key != null) {
        mavenSonarScan(settings.sonar_key)

        awaitSonarResults()
    } else {
        stage('maven build') {
            sh 'mvn clean package test'
        }
    }
}