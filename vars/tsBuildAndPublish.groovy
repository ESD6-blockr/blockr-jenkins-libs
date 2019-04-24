#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('nodejs') {
        scmClone()

        getVersion('npm')

        tsBuildStages(settings)

        npmPublish(settings)
        
        cleanWorkSpace()
    }
}