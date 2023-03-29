.DEFAULT_GOAL := help

help: ## this help.
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)


DOCKER_EXEC?=$(shell which docker)

env:

app_version: ## get app version.

increment_version: ## increment app version.

increment_patch_version: ## increment app version.

verify: ## run tests

build_jar: ## build jar

build_image: ## build docker image
