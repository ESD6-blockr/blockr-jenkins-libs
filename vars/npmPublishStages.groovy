#!/usr/bin/groovy

def call(String environment = null, ArrayList archive_folders = null) {
    if (environment != null) {
        stage('Set publish registry') {
            String registry = "npm-${environment}.naebers.me"
            
            withCredentials([string(credentialsId: "npm-${environment}", variable: 'token')]) {
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
        withCredentials([string(credentialsId: "npmjs", variable: 'token')]) {
            String content = "//registry.npmjs.org/:_authToken=${token}"
            
            writeFile(file: ".npmrc", text: content, encoding: "UTF-8")
        }
    }

    stage('Publish to registry') {
        sh 'npm publish --access public'
    }
}