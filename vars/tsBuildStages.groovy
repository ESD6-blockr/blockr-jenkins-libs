#!/usr/bin/groovy

def call(Map sonarSettings) {
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

    stage('Sonarqube scan') {
        def scannerHome  = tool 'DefaultScanner'
        withSonarQubeEnv('main') {
            sh "${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectKey=${sonarSettings.key} \
            -Dsonar.sources=${sonarSettings.source} \
            -Dsonar.host.url=${sonarSettings.host} \
            -Dsonar.login=${sonarSettings.token}"
        }   
    }

    stage('Build') {
        sh 'npm run build'
    }
}