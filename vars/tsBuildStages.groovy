#!/usr/bin/groovy

def call() {
    stage('Initialize') {
        sh 'npm i'
    }

    stage('Lint') {
        sh 'npm run lint'
    }

    stage('Build') {
        sh 'npm run build'
    }
}