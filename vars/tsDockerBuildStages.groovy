#!/usr/bin/groovy

def call(String repo, Map settings) {
    def version
    def publishImageName
    def testImageName

    version = "${env.PROJECT_VERSION}.${env.BUILD_NUMBER}"
    publishImageName = "${repo}:${version}"
    testImageName = "${repo}.test:${version}"

    stage('Initialize') {
        def branch = env.BRANCH_NAME
       
        if (branch == 'develop' || branch.contains('feature') || branch.contains('fix')) {
            sh 'cp /home/jenkins/resources/npmdev ./.npmrc'
        }

        if (branch.contains('release')) {
            sh 'cp /home/jenkins/resources/npmstaging ./.npmrc'  
        }
    }

    stage('Build') {
        def dockerfile = 'Dockerfile'

        sh "docker build -t ${publishImageName} -f ${dockerfile} . "
                
        if (!settings.skip_tests) {
            sh "docker build --target TEST -t ${testImageName} -f ${dockerfile} --build-arg 'WORKDIR=${env.WORKSPACE}' . "
        }

        env.IMAGE_NAME = publishImageName
    }

    if (!settings.skip_tests) {
        stage('Unit Test') {
            sh "docker run --rm -e JEST_JUNIT_OUTPUT_DIR=/coverage -v /home/jenkins/reportFiles/coverage:${env.WORKSPACE}/coverage ${testImageName}"
            sh 'cp -r /home/jenkins/reportFiles/coverage ./coverage'
            
            junit 'coverage/junit.xml'
            step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])

            sh 'rm -rf /home/jenkins/reportFiles/*'
        }
    }

    stash 'context'

    if (settings.sonar_key != null) {
        node('nodejs') {
            try {
                unstash 'context'

                sh 'npm i'

                tsSonarScan(settings.sonar_key, settings.source_folder, settings.sonar_exclusions);

                awaitSonarResults()
            } finally {
                cleanWorkSpace()
            }
        }
    }
}