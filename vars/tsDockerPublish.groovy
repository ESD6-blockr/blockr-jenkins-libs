#!/usr/bin/groovy

def call(String repo, ArrayList archive_folders = null) {
    def branch = env.BRANCH_NAME
    def imageName = env.IMAGE_NAME
    def version = env.PROJECT_VERSION

    if (branch == 'develop' || branch.contains('feature') || branch.contains('fix')) {
        tsDockerPublishStages(imageName, repo 'develop', archive_folders)
    }

    if (branch.contains('release')) {
        tsDockerPublishStages(imageName, repo 'staging', archive_folders)
    }

    tsDockerPublishStages(imageName, repo, null, archive_folders)
}