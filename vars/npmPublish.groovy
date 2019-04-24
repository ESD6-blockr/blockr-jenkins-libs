#!/usr/bin/groovy

def call(Map settings) {
    stage('Publish package') {
        parallel development: {
            if (env.BRANCH_NAME == 'develop') {
                echo 'Publishing to dev environemnt'
                npmPublishStages('dev')
                archive(settings.archive_folders)
                logToJira("Published", "develop")
                return;
            }
        },
        staging: {
            if (env.BRANCH_NAME.contains('release')) {
                echo 'Publishing to staging environemnt'
                npmPublishStages('staging')
                archive(settings.archive_folders)
                logToJira("Published", "staging")
                return;
            }
        },
        producion: {
            if (env.BRANCH_NAME == 'master') {
                echo 'Publishing to production environemnt'
                npmPublishStages()
                archive(settings.archive_folders)
                logToJira("Published", "production")
                return;
            }
        }
    }
}