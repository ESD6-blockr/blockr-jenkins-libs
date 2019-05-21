#!/usr/bin/groovy

def call(String repo, Map settings) {
    def version = env.PROJECT_VERSION
    def deployImageName
    def testImageName


    stage('Initialize') {
        def branch = env.BRANCH_NAME

        sh 'docker version'

        if (branch == 'develop' || branch.contains('feature')) {
            sh "cp /home/jenkins/resources/node-registries/dev .npmrc"
        }

        if (branch.contains('release')) {
            sh "cp /home/jenkins/resources/node-registries/staging .npmrc"
        }

        if (branch == 'master') {
            sh "cp /home/jenkins/resources/node-registries/prod .npmrc"
        }
    }

    stage('Build') {
        deployImageName = "${repo}:${version}"
        testImageName = "${repo}-test:${version}"

        sh "docker build -t ${deployImageName} --build-arg 'VERSION=${version}' . "
        sh "docker build --target TEST -t ${testImageName} --build-arg 'VERSION=${version}' . "
    }

    stage('Test') {
        sh 'mkdir coverage'
        sh 'docker run -v "$PWD/coverage":/opt/coverage ${testImageName}'
        sh "docker run --rm -v ${env.WORKSPACE}/coverage:/opt/coverage ${testImageName}"
    }

    stage('Record results') {
        step([$class: 'CoberturaPublisher', coberturaReportFile: "coverage/cobertura-coverage.xml"])
    }

    if (settings.sonar_key != null) {
       tsSonarScan(settings.sonar_key, settings.source_folder);

       awaitSonarResults()
    }
}