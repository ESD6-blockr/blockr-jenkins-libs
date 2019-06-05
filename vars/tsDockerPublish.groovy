#!/usr/bin/groovy

def call(String repo, ArrayList archive_folders = null) {
    def branch = env.BRANCH_NAME
    def imageName = env.IMAGE_NAME
    def version = env.PROJECT_VERSION

    if (branch == 'develop') {
        tsDockerPublishStages(imageName, repo, 'develop', version, archive_folders)
    }

    if (branch.contains('release')) {
        tsDockerPublishStages(imageName, repo, 'staging', version, archive_folders)
    }

    tsDockerPublishStages(imageName, repo, null, version, archive_folders)
}