# Installing the MQ Operator and Running a QMGR #

All +++ NAMESPACE +++ must be updated in the yaml files.

These steps will install the MQ OCP Operator and create a QMGR with a few Queues and a channel.

1. logon to the OpenShift cluster of choice using the `oc login`
2. create a new project `oc new-project PROJECT_NAME`
3. Get your [entitlement key](https://myibm.ibm.com/products-services/containerlibrary)
4. Store the entitlement key from the previous step in a secret `oc create secret docker-registry ibm-entitlement-key --docker-server=cp.icr.io --docker-username=cp --docker-password=ENTITLEMENT_KEY --docker-email=OCP_LOGIN_CREDENTIALS`
5. Connect to the IBM Docker Catalog and install the necessary operators using the command `oc apply -f initial-setup-mq.yaml`

The next steps for creating an ephemeral QMGR.  To do so the QMGR will be secured with a certificate for `localhost`.  The certificate can be replaced with a proper certificate.  
1. Add a secret with the private and public key by running the command `oc apply -f mq-tls-secret.yaml`
2. Add the MQSC commands to a `ConfigMap`.  These will be run after the creation of the QMGR. Run the command `oc apply -f mq-mqsc-configmap.yaml`
3. Create the QMGR using the certificates and MQSC commands from the previous steps by running the command `oc apply -f qmgr-singleInstance.yaml` 
4. (Optional) To allow the QMGR to be contact outside of the OCP environment update the `mq-route-svrconn.yaml` with the proper MQ CHANNEL information using SNI and then run the command `oc apply -f mq-route-svrconn.yaml`

