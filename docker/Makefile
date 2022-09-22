# File: /docker/Makefile
# Project: @promanager/keycloak-avatar-client
# File Created: 28-01-2022 12:45:21
# Author: Clay Risser
# -----
# Last Modified: 22-09-2022 11:02:03
# Modified By: Clay Risser
# -----
# ProManager LLC (c) Copyright 2022
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

include ../mkpm.mk
ifneq (,$(MKPM_READY))
include $(MKPM)/gnu

export DOCKER_COMPOSE ?= docker-compose
export CONTEXT := ..
export NAME := keycloak-provider
export REGISTRY := registry.gitlab.com/bitspur/community

export POM_ARTIFACT_ID ?= $(shell $(CAT) ../pom.xml | \
	$(GREP) "<artifactId>" | \
	$(HEAD) -n1 | \
	$(SED) 's|^\s*<artifactId>||g' | \
	$(SED) 's|</artifactId>||g')
export POM_VERSION ?= $(shell $(CAT) ../pom.xml | \
	$(GREP) "<version>" | \
	$(HEAD) -n1 | \
	$(SED) 's|^\s*<version>||g' | \
	$(SED) 's|</version>||g')

export VERSION := $(POM_VERSION)

-include $(MKPM)/docker

endif
