#!/usr/bin/groovy

def call(String imageName, String repo, String environment = null, String version, ArrayList archive_folders = null) {
    def image

    stage('Publish') {
        withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'pass', usernameVariable: 'usr')]) {
            sh "docker login -u ${usr} -p ${pass}"

            environment != null 
                ? image = "blockr/${repo}:develop-${version}"
                : image = "blockr/${repo}:${version}"

            sh "docker tag ${imageName} ${image}"
            sh "docker push ${image}"
        }

        sh "docker logout"
    }

    archive(archive_folders)
    logToJira("Docker Published", image)
}