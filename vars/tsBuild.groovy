#!/usr/bin/groovy

def call() {
    node('nodejs') {
        scmClone()

        tagJenkinsBuild('npm')

        stage('Initialize') {
            sh 'npm i'
        }

        stage('Lint') {
            sh 'npm run lint'
        }

        stage('Build') {
            sh 'npm run build'
        }

        cleanWorkspace()
    }
}