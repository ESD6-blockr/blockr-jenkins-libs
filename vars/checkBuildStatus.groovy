#!/usr/bin/groovy

def call(String message) {
    if (currentBuild.result == 'UNSTABLE') {
        timeout(time: 7, unit: 'DAYS') {
            result = input message: message, submitter: null
        }
    }
}