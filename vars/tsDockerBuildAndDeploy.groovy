#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('master') {
        scmClone()

        stage('Initialize') {
            def branch = env.BRANCH_NAME

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

        node('docker') {
            try {
                unstash 'npmrc'

                getVersion('npm')

                tsDockerBuildStages(repo, settings)

                node('master') {
                    tsDockerQualityStages(settings)
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
}