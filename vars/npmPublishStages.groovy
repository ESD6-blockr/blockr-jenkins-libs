#!/usr/bin/groovy

def call(String environment = null) {
    if (environment != null) {
        stage('Set publish registry') {
            String registry = "npm-${environment}.naebers.me"
            
            withCredentials([usernamePassword(credentialsId: "npm-${environment}", passwordVariable: 'pass', usernameVariable: 'user')]) {
                sh "echo '@blockr:registry=https://${registry}' >> .npmrc"
                sh "echo '//${registry}/:username=${user}' >> .npmrc"
                sh "echo '//${registry}/:_password=${pass}' >> .npmrc"
                sh "echo '//${registry}/:email=danenaebers@gmail.com' >> .npmrc"
            }
        }

        stage('Publish to registry'){
            sh 'npm publish'
        }

        return
    }

    stage('Set publish registry') {
        withCredentials([usernamePassword(credentialsId: "npmjs", passwordVariable: 'token')]) {
            sh "echo '//registry.npmjs.org/:_authToken=${token}' >> .npmrc"
        }
    }

    stage('Publish to registry') {
        sh 'npm publish --acccess public'
    }
}