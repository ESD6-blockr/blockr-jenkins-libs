#!/usr/bin/groovy

def call(String type) {
    stage('Get version') {
        def branch = env.BRANCH_NAME

        switch (type) {
            case 'maven':
                def pomFile = readMavenPom file: 'pom.xml'

                parseVersion(pomFile)

                currentBuild.displayName = "${env.PROJECT_VERSION}-${env.BUILD_NUMBER}"
            break
            case 'npm':
                def packageFile = readJSON file: 'package.json'
            
                parseVersion(packageFile)

                if (branch != 'master') {
                    String version = "${env.PROJECT_VERSION}-build.${env.BUILD_NUMBER}"
                    packageFile.version = version
                    writeJSON file: 'package.json', json: packageFile
                }

                currentBuild.displayName = "${env.PROJECT_VERSION}-${env.BUILD_NUMBER}"     
            break                
        }
    }
}