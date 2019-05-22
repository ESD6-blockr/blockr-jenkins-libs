#!/usr/bin/groovy

def call(String repo, Map settings) {
    pipeline() {
        try {
            docker.image('blockr/jenkins-docker-slave:stable').withrun('-v /var/run/docker.sock:/var/run/docker.sock') {
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