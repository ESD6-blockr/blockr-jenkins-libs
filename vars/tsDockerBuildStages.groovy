#!/usr/bin/groovy

def call(String repo, Map settings) {
    def version = "${env.PROJECT_VERSION}.${env.BUILD_NUMBER}"
    def publishImageName = "${repo}:${version}"
    def testImageName = "${repo}.test:${version}"
    
    String path = pwd()
    String registry

    stage('Initialize') {
        def branch = env.BRANCH_NAME

        if (branch == 'develop' || branch.contains('feature') || branch.contains('fix')) {
            registry = '@blockr:registry=https://npm-dev.naebers.me'
        }

        if (branch.contains('release')) {
            registry = '@blockr:registry=https://npm-staging.naebers.me'
        }
    }       

    stage('Build') {
        def dockerfile = 'Dockerfile'

        sh "docker build -t ${publishImageName} -f ${dockerfile} --build-arg REGISTRY=${registry} . "
                
        if (!settings.skip_tests) {
            sh "docker build --target TEST -t ${testImageName} -f ${dockerfile} --build-arg REGISTRY=${registry} WORKDIR=${path} . "
        }

        env.IMAGE_NAME = publishImageName
    }

    if (!settings.skip_tests) {
        stage('Unit Test') {
            sh "docker run --rm -e JEST_JUNIT_OUTPUT_DIR=${path}/coverage -v /home/jenkins/reportFiles/coverage:${path}/coverage ${testImageName}"
            sh 'cp -r /home/jenkins/reportFiles/coverage ./coverage'
            
            junit 'coverage/junit.xml'
            step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])

            sh 'rm -rf /home/jenkins/reportFiles/*'
        }
    }

    stash 'context'
}