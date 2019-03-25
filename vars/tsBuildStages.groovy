#!/usr/bin/groovy

def call() {
    stage('Initialize') {
        def branch = env.BRANCH_NAME
       
        if (branch == 'develop' || branch.contains('feature')) {
            sh "echo '@blockr:registry=https://npm-dev.naebers.me' >> .npmrc"
        }

        if (branch.contains('release')) {
            sh "echo '@blockr:registry=https://npm-staging.naebers.me' >> .npmrc"
        }

        sh 'npm i'
    }

    stage('Lint') {
        sh 'npm run lint'
    }

    stage('Build') {
        sh 'npm run build'
    }
}