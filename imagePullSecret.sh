#!/bin/bash
#
# To get your entitlement key go to : https://myibm.ibm.com/products-services/containerlibrary
# then update below with the correct key and namespace
#
# This scripts then creates and applies the yaml

SERVER=cp.icr.io
USER=cp
PW=YOUR_ENTITLEMENT_KEY
EMAIL=

AUTH_ENCODED=`echo $USER:$PW | base64`
CONFIG_ENCODED=`echo "{\"auths\":{\"$SERVER\":{\"username\":\"$USER\",\"password\":\"$PW\",\"auth\":\"$AUTH_ENCODED\",\"email\":\"$EMAIL\"}}}" | base64`

NAMESPACE=cp4i
SECRET=ibm-entitlement-key

echo "kind: Secret
apiVersion: v1
metadata:
  name: $SECRET
  namespace: $NAMESPACE
data:
  .dockerconfigjson: >-
     $CONFIG_ENCODED
type: kubernetes.io/dockerconfigjson" > ./secret.yaml

oc apply -f secret.yaml
rm secret.yaml
