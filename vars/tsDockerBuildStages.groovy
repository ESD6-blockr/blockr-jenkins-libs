#!/usr/bin/groovy

def call(String repo, Map settings) {
    def version = env.PROJECT_VERSION
    def deployImageName
    def testImageName


    stage('Initialize') {
        def branch = env.BRANCH_NAME

        sh 'docker version'

        if (branch == 'develop' || branch.contains('feature')) {
            writeFile(file: ".npmrc", text: '@blockr:registry=https://npm-dev.naebers.me', encoding: "UTF-8")        
        }

        if (branch.contains('release')) {
            writeFile(file: ".npmrc", text: '@blockr:registry=https://npm-staging.naebers.me', encoding: "UTF-8")        
        }

        if (branch == 'master') {
            writeFile(file: ".npmrc", text: '@blockr:registry=https://registry.npmjs.org', encoding: "UTF-8")        
        }
    }

    stage('Build') {
        deployImageName = "${repo}:${version}"
        testImageName = "${repo}-test:${version}"

        sh "docker build -t ${deployImageName} --build-arg 'VERSION=${version}' . "
        sh "docker build --target TEST -t ${testImageName} --build-arg 'VERSION=${version}' . "
    }

    stage('Test') {
        String pwd = sh 'pwd' 
        echo pwd
        sh "docker run -v ${pwd}/coverage:/opt/coverage ${testImageName}"
    }

    stage('Record results') {
        step([$class: 'CoberturaPublisher', coberturaReportFile: "coverage/cobertura-coverage.xml"])
    }

    if (settings.sonar_key != null) {
       tsSonarScan(settings.sonar_key, settings.source_folder);

       awaitSonarResults()
    }
}