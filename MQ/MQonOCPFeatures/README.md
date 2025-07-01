### Example of installation and setup of MQ with many features on OpenShift (OCP) with Operator

The script does the following:
1. Creates a namespace called `cp4i`
2. Creates the IBM Operator Catalog
3. For security create an OperatorGroup for cp4i
4. Installs the MQ Operator
5. Creates a secret for the SSL (private and public keys)
6. Create a secret for LDAP (this is only an example and must be changed to use)
7. Creates a secret for a ChannelExit called connwarn.  [Source code](https://github.com/ibm-messaging/mq-exits/tree/master/channel/connwarn).
8. Creates a ConfigMap for the web user interface granting all access to those authenticated
9. Creates a ConfigMap for the qm.ini and MQ definitions for the QMGR
10. Creates a QMGR called `QM1` which is a single instance QMGR that will inject the above (qm.ini, mqsc configs (both regular and ldap), certificates, exit, and web users for console)
11. Create Openshift Route for the channel EXTERNAL.TO.SVRCONN
