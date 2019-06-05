#!/usr/bin/groovy

def call(String imageName, String repo, String environment = null, String version, ArrayList archive_folders = null) {
    def versionTag
    def latestTag

    stage('Publish') {
        checkBuildStatus("Publish docker image anyway?")
        
        withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'pass', usernameVariable: 'usr')]) {
            sh "docker login -u ${usr} -p ${pass}"

            if (environment != null) {
                versionTag = "blockr/${repo}:${environment}-${version}"
                latestTag = "blockr/${repo}:${environment}-latest"
            } else {
                versionTag = "blockr/${repo}:${version}"
                latestTag = "blockr/${repo}:latest"
            }

            sh "docker tag ${imageName} ${versionTag}"
            sh "docker tag ${imageName} ${latestTag}"
            sh "docker push ${versionTag}"
            sh "docker push ${latestTag}"
        }

        sh "docker logout"
    }

    archive(archive_folders)
    logToJira("Docker Published", imageName)
}