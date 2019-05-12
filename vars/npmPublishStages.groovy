#!/usr/bin/groovy

def call(String environment = null, ArrayList archive_folders = null) {
    if (environment != null) {
        stage('Set publish registry') {
            String registry = "npm-${environment}.naebers.me"
            
            withCredentials([usernamePassword(credentialsId: "npm-${environment}", passwordVariable: 'token', usernameVariable: 'user')]) {
                def content = """
                registry=https://${registry}
                //${registry}/:_authToken=${token}
                """

                writeFile(file: ".npmrc", text: content, encoding: "UTF-8")
            }
        }

        stage('Publish to registry'){
            sh 'npm publish'
        }

        return
    }

    stage('Set publish registry') {
        withCredentials([usernamePassword(credentialsId: "npmjs", passwordVariable: 'token', usernameVariable: 'user')]) {
            String content = "//registry.npmjs.org/:_authToken=${token}"
            
            writeFile(file: ".npmrc", text: content, encoding: "UTF-8")
        }
    }

    stage('Publish to registry') {
        sh 'npm publish --access public'
    }
}