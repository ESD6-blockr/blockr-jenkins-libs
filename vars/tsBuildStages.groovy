#!/usr/bin/groovy

def call(String version, Map sonarSettings = null) {
    stage('Initialize') {
        def branch = env.BRANCH_NAME
       
        if (branch == 'develop' || branch.contains('feature')) {
            sh "echo '@blockr:registry=https://npm-dev.naebers.me' >> .npmrc"
        }

        if (branch.contains('release')) {
            sh "echo '@blockr:registry=https://npm-staging.naebers.me' >> .npmrc"
        }

        sh 'npm i'
    }

    stage('Lint') {
        sh 'npm run lint'
    }

    if (sonarSettings != null) {
        stage('Sonarqube scan') {
            def scannerHome  = tool 'DefaultScanner'
            
            withSonarQubeEnv('main') {
                sh "${scannerHome}/bin/sonar-scanner \
                -Dsonar.projectKey=${sonarSettings.key} \
                -Dsonar.sources=${sonarSettings.source} \
                -Dsonar.projectVersion=${env.PROJECT_VERSION} \
                -Dsonar.host.url=${SONAR_HOST_URL} \
                -Dsonar.login=${SONAR_AUTH_TOKEN}"
            }
        }

        stage('Await sonarqube quality gate') {
            timeout(time: 10, unit: 'MINUTES') {
              def qg = waitForQualityGate()
              if (qg.status != 'OK') {
                  error "Pipeline aborted due to quality gate failure: ${qg.status}"
              }
            }
        }
    }

    stage('Build') {
        sh 'npm run build'
    }
}