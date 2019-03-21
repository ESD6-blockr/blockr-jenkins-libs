#!/usr/bin/groovy

def call(String type) {
    stage('Tag build') {
        def branch = env.BRANCH_NAME

        if (branch.contains('/')) {
            branch = branch.substring(0, branch.indexOf('/'))
        }

        switch (type) {
            case 'maven':
                def pom = readMavenPom file: 'pom.xml'
                currentBuild.displayName = "${pom.version}-${branch}-${env.BUILD_NUMBER}"
                env.PROJECT_VERSION = pom.version
                break
            case 'npm':
                def packageFile = readJSON file: 'package.json'
                currentBuild.displayName = "${packageFile.version}-${branch}-${env.BUILD_NUMBER}"
                env.PROJECT_VERSION = packageFile.version
                break
        }
    }
}