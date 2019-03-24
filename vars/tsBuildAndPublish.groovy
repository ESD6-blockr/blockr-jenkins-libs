#!/usr/bin/groovy

def call() {
    node('nodejs') {
        scmClone()

        tagJenkinsBuild('npm')

        tsBuildStages()

        npmPublish()
        
        cleanWorkSpace()
    }
}