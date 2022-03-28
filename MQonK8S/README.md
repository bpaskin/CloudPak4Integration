### MQ on Kubernetes without OpenShift ###

This is an example of two Queue Managers (QMGRS) that will be deployed to a K8S environment without the need for OpenShift (OCP) environment.  The QMGRs, QM1 and QM2, will communicate over SSL with each other.

What do the scripts do:

1. Create a namespace called `mq-demo`
2. Cretae a persistent volume claim (PVC) with 25GB of space.  Note: The `storageClassName` will need to be changed for different environments.  This was on run on IBM Cloud.
3. Create a configuration map (ConfigMap) that contains the MQSC commands that will be execute upon start up of the Pod
4. Create a Secret that contains the QMGRs private and pub key for the keystore
5. Create a Secret that contains public keys to be put in the truststore
6. Create a Deployment that will create the QMGR by pulling the image from IBM Container Register (ICR), mount the PVC under `/mnt/mqm` for the data, mount the Secrets for the keystore and truststore, and mount the configuration ConfigMap.  The `securityContext` may be different on each platform
7. Create a LoadBalancing service with the necessary ports open

Please note: a Secret called `ibm-entitlement-key` must be created that contains the user's [IBM Entitlement Key](https://www.ibm.com/docs/en/cloud-paks/cp-integration/2020.2?topic=installation-entitled-registry-entitlement-keys) must be present in the namespace.  Add it by running the following command:

```
kubectl create secret docker-registry ibm-entitlement-key --docker-username=cp --docker-password=<entitlement-key> --docker-server=cp.icr.io --namespace=mq-demo
```

