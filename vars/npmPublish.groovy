#!/usr/bin/groovy

def call(Map settings) {
    if (currentBuild.result == 'UNSTABLE') {
        timeout(time: 7, unit: 'DAYS') {
            result = input message: 'Publish package anyway?', submitter: null, parameters: [booleanParam(defaultValue: false, description: '', name: 'yes')]
        }
    }

    if (env.BRANCH_NAME == 'develop') {
        stage('Publish development package') {
            echo 'Publishing to dev environment'
            npmPublishStages('dev')
            archive(settings.archive_folders)
            logToJira("Published", "develop")
        }
    }

    if (env.BRANCH_NAME == 'release') {
        stage('Publish staging package') {
            echo 'Publishing to staging environment'
            npmPublishStages('staging')
            archive(settings.archive_folders)
            logToJira("Published", "staging")
        }
    }

    if (env.BRANCH_NAME == 'master') {
        stage('Publish production package') {
            echo 'Publishing to production environment'
            npmPublishStages()
            archive(settings.archive_folders)
            logToJira("Published", "production")
        }
    }
}