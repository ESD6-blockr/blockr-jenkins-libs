#!/usr/bin/groovy

def call(Map sonarSettings = null) {
    node('nodejs') {
        scmClone()

        tagJenkinsBuild('npm')

        tsBuildStages(sonarSettings)

        npmPublish()

        archive('dist/')
        
        cleanWorkSpace()
    }
}