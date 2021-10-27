A simple [Helm](https://helm.sh) chart to be used with the [MQ Operator](https://www.ibm.com/docs/en/ibm-mq/9.2?topic=openshift-installing-mq-operator-using-web-console).

The user must be logged on to the cluster before preceeding to use the chart.

If QMGR is already running and needs updating then the pod must be deleted to pick up the new changes.  A QMGR cannot be switched between one type to another (i.e. from Single Instance to Native HA).

The `values.yaml` can be used as template for others.  The QMGR characteristics, type, storage, MQSC commands, certificates and routes can be updated or added.

normal helm commands:<br/>
`helm install appName ibmmq`<br/>
`helm install appName ibmmq --value=/path/to/values.yaml`<br/>
`helm upgrade appName ibmmq --install --values=/path/to/values.yaml`<br/>
`helm rollback appName`<br/>
`helm uninstall appName`<br/>
ecc..
