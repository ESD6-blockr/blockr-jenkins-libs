#!/usr/bin/groovy

def call(Map sonarSettings) {
    node('nodejs') {
        scmClone()

        tagJenkinsBuild('npm')

        tsBuildStages(sonarSettings)

        npmPublish()
        
        cleanWorkSpace()
    }
}