#!/usr/bin/groovy

def call(String repo, Map settings) {
    node('docker') {
        try {
            scmClone()

            getVersion('npm')

            tsDockerBuildStages(repo, settings)

            if (settings.sonar_key != null) {
                node('nodejs') {
                    unstash 'context'

                    sh 'npm i typescript'

                    tsSonarScan(settings.sonar_key, settings.source_folder, settings.sonar_exclusions);
                    awaitSonarResults()

                    cleanWorkSpace()
                }
            }

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