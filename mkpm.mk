# File: /mkpm.mk
# Project: @risserlabs/keycloak-avatar-client
# File Created: 30-07-2022 11:23:18
# Author: Clay Risser
# -----
# Last Modified: 16-08-2022 11:56:54
# Modified By: Clay Risser
# -----
# Risser Labs LLC (c) Copyright 2022

export MKPM_PACKAGES_DEFAULT := \
	yarn=0.0.5 \
	docker=0.0.9 \
	dotenv=0.0.9 \
	envcache=0.1.0 \
	gnu=0.0.3 \
	mkchain=0.1.0

export MKPM_REPO_DEFAULT := \
	https://gitlab.com/risserlabs/community/mkpm-stable.git

############# MKPM BOOTSTRAP SCRIPT BEGIN #############
MKPM_BOOTSTRAP := https://gitlab.com/api/v4/projects/29276259/packages/generic/mkpm/0.2.0/bootstrap.mk
export PROJECT_ROOT := $(abspath $(dir $(lastword $(MAKEFILE_LIST))))
NULL := /dev/null
TRUE := true
ifneq ($(patsubst %.exe,%,$(SHELL)),$(SHELL))
	NULL = nul
	TRUE = type nul
endif
include $(PROJECT_ROOT)/.mkpm/.bootstrap.mk
$(PROJECT_ROOT)/.mkpm/.bootstrap.mk:
	@mkdir $(@D) 2>$(NULL) || $(TRUE)
	@$(shell curl --version >$(NULL) 2>$(NULL) && \
		echo curl -Lo || echo wget -O) \
		$@ $(MKPM_BOOTSTRAP) >$(NULL)
############## MKPM BOOTSTRAP SCRIPT END ##############
