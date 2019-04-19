#!/usr/bin/groovy

def call(Map sonarSettings = null) {
    if (sonarSettings != null) {
        mavenSonarScan(sonarSettings)

        awaitSonarResults()
    } else {
        stage('maven build') {
            sh 'mvn clean package test'
        }
    }
}