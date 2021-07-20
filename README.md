# CloudPak4Integration #
A collection of scripts and code to help on the journey with IBM's CloudPak for Integration (CP4I) on the Red Hat OpenShift Container Platform.

______

# Installing CloudPak for Integration #
The current version is 3.1 and may need to be updated.

All *NAMESPACE* must be updated in the yaml files.

These steps will install the MQ OCP Operator and create a QMGR with a few Queues and a channel.

1. logon to the OpenShift cluster of choice using the `oc login`
2. create a new project `oc new-project PROJECT_NAME`
3. Get your [entitlement key](https://myibm.ibm.com/products-services/containerlibrary)
4. Store the entitlement key from the previous step in a secret `oc create secret docker-registry ibm-entitlement-key --docker-server=cp.icr.io --docker-username=cp --docker-password=ENTITLEMENT_KEY --docker-email=OCP_LOGIN_CREDENTIALS`
5. Connect to the IBM Docker Catalog and install the necessary operators using the command `oc apply -f installcp4i.yaml`

It may take awhile to pull and install all features of the CloudPak.  This does not create a Platform Navigator or other integrations.
