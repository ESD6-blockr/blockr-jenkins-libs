def call(Map sonarSettings = null) {
    node('maven') {
        scmClone()

        tagJenkinsBuild('maven')

        mavenBuildStages(sonarSettings)

        cleanWorkSpace()
    }
}