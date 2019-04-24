#!/usr/bin/groovy

def call(Map settings) {
    if (env.BRANCH_NAME == 'develop') {
        stage('Publish development package') {
            echo 'Publishing to dev environemnt'
            npmPublishStages('dev')
            archive(settings.archive_folders)
            logToJira("Published", "develop")
        }
    }

    if (env.BRANCH_NAME == 'release') {
        stage('Publish staging package') {
            echo 'Publishing to staging environemnt'
            npmPublishStages('staging')
            archive(settings.archive_folders)
            logToJira("Published", "staging")
        }
    }

    if (env.BRANCH_NAME == 'master') {
        stage('Publish production package') {
            echo 'Publishing to production environemnt'
            npmPublishStages('production')
            archive(settings.archive_folders)
            logToJira("Published", "production")
        }
    }
}