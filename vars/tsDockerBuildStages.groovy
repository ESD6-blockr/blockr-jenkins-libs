#!/usr/bin/groovy

def call(String repo, Map settings) {
    def version = env.PROJECT_VERSION
    def deployImageName
    def testImageName


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

        if (branch == 'master') {
            String content =  '@blockr:registry=https://registry.npmjs.org/'
                           
            writeFile(file: ".npmrc", text: content, encoding: "UTF-8")        
        }
    }

    stage('Build') {
        deployImageName = "${repo}:${version}"
        testImageName = "${repo}-test:${version}"

        sh "docker build -t ${deployImageName} --build-arg 'VERSION='${version}' . "
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