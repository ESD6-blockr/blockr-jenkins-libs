#!/usr/bin/groovy

def call(Map settings) {
    stage('Initialize') {
        def branch = env.BRANCH_NAME
       
        if (branch == 'develop' || branch.contains('feature')) {
            String content =  '@blockr:registry=https://npm-dev.naebers.me'
                           
            writeFile(file: ".npmrc", text: content, encoding: "UTF-8")
        }

        if (branch.contains('release')) {
            String content =  '@blockr:registry=https://npm-staging.naebers.me'
                           
            writeFile(file: ".npmrc", text: content, encoding: "UTF-8")        
        }

        sh 'npm i'
    }

    stage('Build') {
        sh 'npm run build'
    }

    parallel lint: {
        stage('Lint') {
            sh 'npm run lint'
        }
    }, unitTests: {
        if (!settings.skip_tests) {
            tsTest()
        }
    },
    failFast: true

    if (settings.sonar_key != null) {
       tsSonarScan(settings.sonar_key, settings.source_folder);

       awaitSonarResults()
    }
}