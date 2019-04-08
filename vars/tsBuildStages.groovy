#!/usr/bin/groovy

def call(Map sonarSettings = null) {
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
            withCredentials([usernamePassword(credentialsId: "sonar.${sonarSettings.key}", passwordVariable: 'token', usernameVariable: 'user')]) {
                def scannerHome  = tool 'DefaultScanner'
                
                withSonarQubeEnv('main') {
                    sh "${scannerHome}/bin/sonar-scanner \
                    -Dsonar.projectKey=${sonarSettings.key} \
                    -Dsonar.sources=${sonarSettings.source} \
                    -Dsonar.host.url=${sonarSettings.host} \
                    -Dsonar.login=${token}"
                }
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