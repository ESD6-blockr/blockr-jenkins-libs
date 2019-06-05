#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('master') {
        smcClone()

        stage('Initialize') {
            def branch = env.BRANCH_NAME
        
            if (branch == 'develop' || branch.contains('feature') || branch.contains('fix')) {
                sh 'cp /home/jenkins/resources/npmdev ./.npmrc'
            }

            if (branch.contains('release')) {
                sh 'cp /home/jenkins/resources/npmstaging ./.npmrc'  
            }

            stash 'context'
        }
    }
    node('docker') {
        try {
            unstash 'context'

            getVersion('npm')

            tsDockerBuildStages(repo, settings)

            tsDockerPublish(repo, settings.archive_folders)
        }
        catch(all) {
            echo 'an error occured'
            currentBuild.result = 'FAILURE'
        }
        finally {
            cleanWorkSpace()
        }
    }
}