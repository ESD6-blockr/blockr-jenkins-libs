#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('nodejs') {
        try {
            scmClone()

            getVersion('npm')

            tsBuildStages(settings)

            npmPublish(settings)
        }
        catch(all) {
            currentBuild.result = 'FAILURE'
        }
        finally {
            cleanWorkSpace()
        }
    }
}