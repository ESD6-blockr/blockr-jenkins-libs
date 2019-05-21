#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('docker') {
        try {
            scmClone()

            getVersion('npm')

            tsDockerBuildStages(repo, settings)
        }
        catch(all) {
            currentBuild.result = 'FAILURE'
        }
        finally {
            //cleanWorkSpace()
        }
    }
}