#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('master') {
        try {
            docker.image('blockr/jenkins-docker-slave:stable').inside {
                sh 'docker ps'
            }

        //     scmClone()

        //     getVersion('npm')

        //     tsDockerBuildStages(repo, settings)
        }
        catch(all) {
            currentBuild.result = 'FAILURE'
        }
        finally {
            //cleanWorkSpace()
        }
    }
}