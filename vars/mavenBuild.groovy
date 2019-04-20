#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('maven') {
        scmClone()

        getVersion('maven')

        mavenBuildStages(settings)

        cleanWorkSpace()
    }
}