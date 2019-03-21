#!/usr/bin/groovy

def call() {
    stage('Clone') {
        checkout scm
    }
}