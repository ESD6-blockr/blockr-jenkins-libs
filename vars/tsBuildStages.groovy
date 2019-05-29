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

        !settings.yarn ? 'npm i' : 'yarn'
    }

    stage('Build') {
        !settings.yarn ? 'npm i' : 'yarn build'
    }

    parallel lint: {
        stage('Lint') {
            !settings.yarn ? 'npm run lint' : 'yarn lint'
        }
    }, unitTests: {
        if (!settings.skip_tests) {
            tsTest(settings)
        }
    },
    failFast: true

    if (settings.sonar_key != null) {
       tsSonarScan(settings.sonar_key, settings.source_folder, settings.sonar_exclusions);

       awaitSonarResults()
    }
}