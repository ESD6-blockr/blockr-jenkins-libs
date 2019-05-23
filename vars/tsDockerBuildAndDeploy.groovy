#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('master') {
        stage('Initialize') {
            def branch = env.BRANCH_NAME

            sh 'docker version'

            if (branch == 'develop' || branch.contains('feature')) {
                writeFile(file: ".npmrc", text: '@blockr:registry=https://npm-dev.naebers.me', encoding: "UTF-8")        
            }

            if (branch.contains('release')) {
                writeFile(file: ".npmrc", text: '@blockr:registry=https://npm-staging.naebers.me', encoding: "UTF-8")        
            }

            if (branch == 'master') {
                writeFile(file: ".npmrc", text: '@blockr:registry=https://registry.npmjs.org', encoding: "UTF-8")        
            }

            stash 'npmrc'
        }
    }

    node('docker') {
        try {
            unstash 'npmrc'

            scmClone()

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

    node('master') {
        tsDockerQualityStages(settings)
    }
}