#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('master') {
        try {
            docker.image('inogo/docker-compose:1.24.0').inside {
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