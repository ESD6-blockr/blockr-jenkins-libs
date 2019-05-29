#!/usr/bin/groovy

def call(Map settings) {
    String operator = !settings.yarn ? 'npm run' : 'yarn'

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

        String initializeCommand = !settings.yarn ? 'npm i' : 'yarn'
        sh "${initializeCommand}"
    }

    stage('Build') {
        "sh ${operator} build"
    }

    parallel lint: {
        stage('Lint') {
            "sh ${operator} lint"
        }
    }, unitTests: {
        if (!settings.skip_tests) {
            tsTest(operator, settings)
        }
    },
    failFast: true

    if (settings.sonar_key != null) {
       tsSonarScan(settings.sonar_key, settings.source_folder, settings.sonar_exclusions);

       awaitSonarResults()
    }
}