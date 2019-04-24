#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('nodejs') {
        try {
            scmClone()

            getVersion('npm')

            tsBuildStages(settings)
        }
        catch(all) {
            throw
        }
        finally {
            cleanWorkSpace()
        }
    }
}