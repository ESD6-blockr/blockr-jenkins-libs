#!/usr/bin/groovy

def call(def file) {
    env.MAJOR_VERSION = file.version.tokenize('.')[0]
    env.MINOR_VERSION = file.version.tokenize('.')[1]
    env.PATCH_VERSION = file.version.tokenize('.')[2]
    env.PROJECT_VERSION = file.version
}