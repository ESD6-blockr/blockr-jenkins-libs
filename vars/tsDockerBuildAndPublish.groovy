#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('docker') {
        try {
            scmClone()

            getVersion('npm')

            tsDockerBuildStages(repo, settings)

            tsDockerPublish(repo, settings.archive_folders)
        }
        catch(error) {
            echo 'an error occured'
            currentBuild.result = 'FAILURE'
            throw error
        }
        finally {
            cleanWorkSpace()
        }
    }
}