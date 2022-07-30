# File: /Makefile
# Project: keycloak-account-avatar
# File Created: 28-01-2022 12:35:10
# Author: Clay Risser
# -----
# Last Modified: 30-07-2022 11:47:55
# Modified By: Clay Risser
# -----
# RisserLabs LLC (c) Copyright 2022
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

include mkpm.mk
ifneq (,$(MKPM_READY))
include $(MKPM)/gnu
include $(MKPM)/mkchain
include $(MKPM)/envcache
include $(MKPM)/dotenv

export MVN ?= mvn
export BABEL ?= $(call yarn_binary,babel)
export BABEL_NODE ?= $(call yarn_binary,babel-node)
export CLOC ?= cloc
export CSPELL ?= $(call yarn_binary,cspell)
export ESLINT ?= $(call yarn_binary,eslint)
export JEST ?= $(call yarn_binary,jest)
export NODEMON ?= $(call yarn_binary,nodemon)
export PRETTIER ?= $(call yarn_binary,prettier)
export TSC ?= $(call yarn_binary,tsc)
export WEBPACK ?= $(call yarn_binary,webpack)

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
$(ACTION)/install:
	@$(MVN) clean install
	@$(call done,install)

.PHONY: build
build: install
	@$(MVN) clean package
	@$(MKDIR) -p docker/volumes/providers

.PHONY: clean
clean: ##
	-@$(GIT) clean -fXd \
		$(MKPM_GIT_CLEAN_FLAGS) \
		$(NOFAIL)

.PHONY: purge
purge: clean ##
	@$(GIT) clean -fXd

.PHONY: start
start:
	@$(MAKE) -s build
	@$(MAKE) -s docker/down
	@$(MAKE) -s docker/up

.PHONY: docker/%
docker/%:
	@$(MAKE) -sC docker $(subst docker/,,$@) ARGS=$(ARGS)

-include $(call actions,$(ACTIONS))

CACHE_ENVS += \

endif
