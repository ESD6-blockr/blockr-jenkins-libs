#!/usr/bin/groovy

def call(String repo, Map settings) {
    String nodeString = 'nodejs'

    if (settings.node) {
        nodeString = "${nodeString} && ${settings.node}"
    }

    node(nodeString) {
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