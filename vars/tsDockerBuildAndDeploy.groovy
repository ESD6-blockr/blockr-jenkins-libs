#!/usr/bin/groovy

def call(String repo, Map settings) {
    pipeline() {
        agent {
            docker {
                image 'inogo/docker-compose:1.24.0'
            }
        }

        scripted {
            try {
                getVersion('npm')

                tsDockerBuildStages(repo, settings)
            }
            catch(all) {
                currentBuild.result = 'FAILURE'
            }
            finally {
                //cleanWorkSpace()
            }            
        }
    }
}