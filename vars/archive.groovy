#!/usr/bin/groovy

def call(ArrayList archive_folders = null) {
    if (archive_folders) {
        stage('Archive') {
            for (folder in archive_folders) {
                archiveArtifacts artifacts: folder
            } 
        }
    }
}