#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('master') {
        try {
            docker.image('blockr/jenkins-docker-slave:stable').withRun('-v /var/run/docker.sock:/var/run/docker.sock') {
                scmClone()

                getVersion('npm')

                tsDockerBuildStages(repo, settings)
            }
        }
        catch(all) {
            currentBuild.result = 'FAILURE'
        }
        finally {
            //cleanWorkSpace()
        }
    }
}