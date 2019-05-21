#!/usr/bin/groovy

def call(String repo, Map settings) {
    def version = env.PROJECT_VERSION
    def deployImageName
    def testImageName


    stage('Initialize') {
        def branch = env.BRANCH_NAME

        sh 'docker version'

        def path = libraryResource 'node-registry/dev'
        echo path
       
        if (branch == 'develop' || branch.contains('feature')) {
            sh "cp ${libraryResource 'node-registry/dev'} .npmrc"
        }

        if (branch.contains('release')) {
            sh "cp ${libraryResource 'node-registry/staging'} .npmrc"
        }

        if (branch == 'master') {
            sh "cp ${libraryResource 'node-registry/prod'} .npmrc"
        }
    }

    stage('Build') {
        deployImageName = "${repo}:${version}"
        testImageName = "${repo}-test:${version}"

        sh "docker build -t ${deployImageName} --build-arg 'VERSION=${version}' . "
        sh "docker build --target TEST -t ${testImageName} --build-arg 'VERSION=${version}' . "
    }

    stage('Test') {
        fileOperations([folderCreateOperation('coverage')])

        sh "docker run --rm -v ${env.WORKSPACE}/coverage:/coverage ${testImageName}"
    }

    stage('Record results') {
        step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
    }

    if (settings.sonar_key != null) {
       tsSonarScan(settings.sonar_key, settings.source_folder);

       awaitSonarResults()
    }
}