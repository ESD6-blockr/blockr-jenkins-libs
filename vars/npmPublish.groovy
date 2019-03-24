#!/usr/bin/groovy

def call() {
    stage('Publish package') {
        parallel development: {
            if (env.BRANCH_NAME == 'develop') {
                echo 'Publishing to dev environemnt'
                npmPublishStages('dev')
                return;
            }
        },
        staging: {
            if (env.BRANCH_NAME.contains('release')) {
                echo 'Publishing to staging environemnt'
                npmPublishStages('staging')
                return;
            }
        },
        producion: {
            if (env.BRANCH_NAME == 'master') {
                echo 'Publishing to production environemnt'
                npmPublishStages()
                return;
            }
        }
        echo 'This package was not published'
    }
}