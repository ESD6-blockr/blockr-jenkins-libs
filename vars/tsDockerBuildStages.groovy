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

        sh "docker build -t ${publishImageName} -f ${dockerfile} --build-arg 'VERSION=${version}' . "
                
        if (!settings.skip_tests) {
            sh "docker build --target TEST -t ${testImageName} -f ${dockerfile} --build-arg 'VERSION=${version}' . "
        }

        env.IMAGE_NAME = publishImageName
    }

    if (!settings.skip_tests) {
        stage('Unit Test') {
            sh "docker run --rm -e JEST_JUNIT_OUTPUT_DIR=/opt/coverage -v /home/jenkins/reportFiles/coverage:/opt/coverage ${testImageName}"
            sh 'cp -r /home/jenkins/reportFiles/coverage ./coverage'
            
            junit 'coverage/junit.xml'
            step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])

            sh 'rm -rf /home/jenkins/reportFiles/*'
        }
    }

    stash includes: '**', name: 'context'

    if (settings.sonar_key != null) {
        node('nodejs') {
            unstash 'context'
            
            tsSonarScan(settings.sonar_key, settings.source_folder, settings.sonar_exclusions);

            awaitSonarResults()
        }
    }
}