#!/usr/bin/groovy

def call(Map settings) {
    stage('Record results') {
        step([$class: 'CoberturaPublisher', coberturaReportFile: "coverage/cobertura-coverage.xml"])
    }

    if (settings.sonar_key != null) {
        tsSonarScan(settings.sonar_key, settings.source_folder);
        
        awaitSonarResults()
    }
}