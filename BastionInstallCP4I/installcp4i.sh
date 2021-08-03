#!/bin/bash

# Fill in the necessary variables first
NAMESPACE=cp4i
ENTTITLEMENT_KEY=
LOCAL_DOCKER_REGISTRY_HOST=
LOCAL_DOCKER_REGISTRY_PORT=443
LOCAL_DOCKER_USER=
LOCAL_DOCKER_PASSWORD=
CASE_VERSION=2.3.0

# ----------#
CASE_NAME=ibm-cp-integration
CASE_ARCHIVE=${CASE_NAME}-${CASE_VERSION}.tgz
CASE_INVENTORY_SETUP=operator
OFFLINEDIR=$HOME/offline
CASE_REPO_PATH=https://github.com/IBM/cloud-pak/raw/master/repo/case
CASE_LOCAL_PATH=$OFFLINEDIR/$CASE_ARCHIVE
CASE_INVENTORY_SETUP=operator
OFFLINEDIR_ARCHIVE=offline.tgz
CASE_LOCAL_PATH=$OFFLINEDIR/$CASE_ARCHIVE
LOCAL_DOCKER_REGISTRY=$LOCAL_DOCKER_REGISTRY_HOST:$LOCAL_DOCKER_REGISTRY_PORT

# check if the oc login command has been issued
oc status

if [ $? -ne 0 ];
then
   echo "Must login to oc first - exiting"
   exit 1
fi

# create namespace and switch over to it
oc new-project $NAMESPACE
oc project $NAMESPACE

# save the data
cloudctl case save --repo $CASE_REPO_PATH --case $CASE_NAME --version $CASE_VERSION --outputdir $OFFLINEDIR

if [ $? -ne 0 ];
then
   echo "Failed saving - exiting"
   exit 1
fi

# Store credentials 
cloudctl case launch --case $HOME/offline/$CASE_ARCHIVE --inventory $CASE_INVENTORY_SETUP --action configure-creds-airgap --args "--registry $LOCAL_DOCKER_REGISTRY_HOST --user $LOCAL_DOCKER_USER --pass $LOCAL_DOCKER_PASSWORD"

if [ $? -ne 0 ];
then
   echo "Failed storing credentials - exiting"
   exit 1
fi

cloudctl case launch --case $HOME/offline/$CASE_ARCHIVE --inventory $CASE_INVENTORY_SETUP --action configure-creds-airgap --args "--registry cp.icr.io --user cp --pass $ENTTITLEMENT_KEY"

if [ $? -ne 0 ];
then
   echo "Failed storing credentials - exiting"
   exit 1
fi

# mirror the image locally
cloudctl case launch --case $CASE_LOCAL_PATH --inventory $CASE_INVENTORY_SETUP --action mirror-images --args "--registry $LOCAL_DOCKER_REGISTRY --inputDir $OFFLINEDIR"

if [ $? -ne 0 ];
then
   echo "Failed mirroring images - exiting"
   exit 1
fi

# configure global image pull secret and ImageContentSourcePolicy
cloudctl case launch --case $HOME/offline/$CASE_ARCHIVE --inventory $CASE_INVENTORY_SETUP --action configure-cluster-airgap --namespace $NAMESPACE --args "--registry $LOCAL_DOCKER_REGISTRY --user $LOCAL_DOCKER_USER --pass $LOCAL_DOCKER_PASSWORD --inputDir $OFFLINEDIR"

if [ $? -ne 0 ];
then
   echo "Failed setting image content - exiting"
   exit 1
fi

# verify above step
oc get imageContentSourcePolicy

# create CatalogSource
cloudctl case launch --case $HOME/offline/${CASE_ARCHIVE} --inventory ${CASE_INVENTORY_SETUP} --action install-catalog --namespace ${NAMESPACE} --args "--registry ${LOCAL_DOCKER_REGISTRY} --inputDir $HOME/offline --recursive"

if [ $? -ne 0 ];
then
   echo "Failed setting CatalogSource- exiting"
   exit 1
fi

# verify the CatalogSource
oc get pods -n openshift-marketplace
oc get catalogsource -n openshift-marketplace

