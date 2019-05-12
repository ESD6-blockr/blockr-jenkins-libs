#!/usr/bin/groovy

def call(String environment = null, ArrayList archive_folders = null) {
    if (environment != null) {
        stage('Set publish registry') {
            String registry = "npm-${environment}.naebers.me"
            
            withCredentials([usernamePassword(credentialsId: "npm-${environment}", passwordVariable: 'pass', usernameVariable: 'user')]) {
                def content = """
                @blockr:registry=https://${registry}
                //${registry}/:username=${user}
                //${registry}/:_password=${pass}
                //${registry}/:email=danenaebers@gmail.com
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