#!/usr/bin/groovy

def call(Map settings) {
    String message = 'Publish package anyway?'

    if (env.BRANCH_NAME == 'develop') {
        stage('Publish development package') {
            checkBuildStatus(message)
            echo 'Publishing to dev environment'
            npmPublishStages('dev')
            archive(settings.archive_folders)
            logToJira("Published", "develop")
        }
    }

    if (env.BRANCH_NAME == 'release') {
        stage('Publish staging package') {
            checkBuildStatus(message)
            echo 'Publishing to staging environment'
            npmPublishStages('staging')
            archive(settings.archive_folders)
            logToJira("Published", "staging")
        }
    }

    if (env.BRANCH_NAME == 'master') {
        stage('Publish production package') {
            checkBuildStatus(message)
            echo 'Publishing to production environment'
            npmPublishStages()
            archive(settings.archive_folders)
            logToJira("Published", "production")
        }
    }
}