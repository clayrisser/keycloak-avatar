# File: /Makefile
# Project: @risserlabs/keycloak-account-avatar-client
# File Created: 31-07-2022 14:43:01
# Author: Clay Risser
# -----
# Last Modified: 07-08-2022 14:10:33
# Modified By: Clay Risser
# -----
# Risser Labs LLC (c) Copyright 2022

include mkpm.mk
ifneq (,$(MKPM_READY))
include $(MKPM)/gnu
include $(MKPM)/mkchain
include $(MKPM)/yarn
include $(MKPM)/envcache
include $(MKPM)/dotenv

export NPM_AUTH_TOKEN ?=

export BABEL ?= $(call yarn_binary,babel)
export BABEL_NODE ?= $(call yarn_binary,babel-node)
export CLOC ?= cloc
export CSPELL ?= $(call yarn_binary,cspell)
export ESLINT ?= $(call yarn_binary,eslint)
export JEST ?= $(call yarn_binary,jest)
export MVN ?= mvn
export PARCEL ?= $(call yarn_binary,parcel)
export PRETTIER ?= $(call yarn_binary,prettier)
export TSC ?= $(call yarn_binary,tsc)

export POM_ARTIFACT_ID ?= $(shell $(CAT) pom.xml | \
	$(GREP) "<artifactId>" | \
	$(HEAD) -n1 | \
	$(SED) 's|^\s*<artifactId>||g' | \
	$(SED) 's|</artifactId>||g')
export POM_VERSION ?= $(shell $(CAT) pom.xml | \
	$(GREP) "<version>" | \
	$(HEAD) -n1 | \
	$(SED) 's|^\s*<version>||g' | \
	$(SED) 's|</version>||g')

ACTIONS += install
$(ACTION)/install: package.json
	@$(YARN) install $(ARGS)
	@$(MVN) clean install
	@$(call done,install)

ACTIONS += format~install ##
$(ACTION)/format: $(call git_deps,\.((json)|(md)|([jt]sx?))$$)
	-@$(call prettier,$?,$(ARGS))
	@$(call done,format)

ACTIONS += spellcheck~format ##
$(ACTION)/spellcheck: $(call git_deps,\.(md)$$)
	-@$(call cspell,$?,$(ARGS))
	@$(call done,spellcheck)

ACTIONS += lint~spellcheck ##
$(ACTION)/lint: $(call git_deps,\.([jt]sx?)$$)
	-@$(call eslint,$?,$(ARGS))
	@$(call done,lint)

ACTIONS += test~lint ##
$(ACTION)/test: $(call git_deps,\.([jt]sx?)$$)
	-@$(MKDIR) -p node_modules/.tmp
	-@$(call jest,$?,$(ARGS))
	@$(call done,test)

.PHONY: mvn/bulid
mvn/bulid:
	@$(MVN) clean package
	@$(MKDIR) -p docker/volumes/providers

ACTIONS += build~test ##
BUILD_TARGET := lib/index.js
lib/index.js:
	@$(call reset,build)
$(ACTION)/build: $(call git_deps,\.([jt]sx?)$$)
	@$(MAKE) -s mvn/bulid
	@$(BABEL) --env-name umd client -d lib --extensions '.js,.jsx,.ts,.tsx' --source-maps
	@$(BABEL) --env-name esm client -d es --extensions '.js,.jsx,.ts,.tsx' --source-maps
	@$(TSC) -p tsconfig.build.json -d
	@$(call done,build)

.PHONY: start +start
start: | ~install +start ##
+start:
#	@$(NODEMON) --exec $(BABEL_NODE) --extensions .ts client/index.ts $(ARGS)

COLLECT_COVERAGE_FROM := ["client/**/*.{js,jsx,ts,tsx}"]
.PHONY: coverage +coverage
coverage: | ~lint +coverage
+coverage:
	@$(JEST) --coverage --collectCoverageFrom='$(COLLECT_COVERAGE_FROM)' $(ARGS)

.PHONY: docker/%
docker/%:
	@$(MAKE) -sC docker $(subst docker/,,$@) ARGS=$(ARGS)

.PHONY: prepare
prepare: ;

.PHONY: upgrade
upgrade:
	@$(YARN) upgrade-interactive

.PHONY: inc
inc:
	@$(NPM) version patch --git=false $(NOFAIL)

.PHONY: count
count: ## count lines of code in project
	@$(CLOC) $(shell $(GIT) ls-files | $(GREP) -vE "^\.yarn")

.PHONY: example +example
example: | ~test +example ## open the example
+example:
	@$(PARCEL) example/index.html

.PHONY: clean
clean: ##
	-@$(MKCACHE_CLEAN)
	-@$(JEST) --clearCache $(NOFAIL)
	-@$(GIT) clean -fXd \
		$(MKPM_GIT_CLEAN_FLAGS) \
		$(YARN_GIT_CLEAN_FLAGS) \
		$(NOFAIL)

CACHE_ENVS += \
	BABEL \
	BABEL_NODE \
	CLOC \
	CSPELL \
	ESLINT \
	JEST \
	PRETTIER \
	TSC

-include $(call actions)

endif
