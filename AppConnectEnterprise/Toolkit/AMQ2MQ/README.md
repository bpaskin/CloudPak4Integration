### Active MQ to IBM MQ and IBM MQ to Active MQ Flows ###

There are two flows one that goes from Active MQ to IBM MQ using JMS and the other is just the reverse.  The flows use JMS to retrieve and place messages on the respective Queues and use a generic policy that can be updated for each deployment of the flows.  Each Flow contains a Subflow due to certain requirements for Active MQ.

The generic names being used for the JNDI lookup are:
1. `IN.Q` - The Queue that being listened to for messages
2. `OUT.Q` - The Queue where messages will be placed after they are pulled from the `IN.Q`
3. `BO.Q` - The Queue where messages will be placed, if the `OUT.Q` is unavailable.  This is not required.


To set the information necessary for IBM MQ, a `.bindings` file must be created and placed in the proper location to be read from the Flow.  The `.bindings` file is created using the `JMSAdmin` tool.  The tool can be installed through the Java install part of MQ or [separately](https://www.ibm.com/docs/en/ibm-mq/9.2?topic=jms-obtaining-mq-classes-separately).  The tool information and usage can be found in the [MQ Knowledge Center](https://www.ibm.com/docs/en/ibm-mq/9.2?topic=resources-configuring-jms-objects-using-administration-tool).  However, these are the mininal steps:

1. Update the `JMSAdmin.config` file, usually found in the `/opt/mqm/java/bin` directory.  In particular update the `PROVIDER_URL` to a directory that currently exists. The `.bindings` file will be created there.
2. Launch the JMSAdmin tool, which is usually in the `/opt/mqm/java/bin` directory
3. Create a Connection Factory, which must be called `MQ`, `DEFINE CF(MQ) QMGR(NAME_OF_QMGR) TRAN(CLIENT) CHAN(CHANNEL_NAME_SVRCONN) HOST(HOST_NAME_OF_QMGR) PORT(PORT_NUMBER)`
4. Create `IN.Q`, `OUT.Q`, and `BO.Q`, which must have these names, `DEFINE Q(IN.Q) QUEUE(INPUT_QUEUE_NAME) QMGR(NAME_OF_QMGR)`, `DEFINE Q(OUT.Q) QUEUE(OUTPUT_QUEUE_NAME) QMGR(NAME_OF_QMGR)` ...
5. Exit out of the command shell with the `end` command.

AMQ requires a `jndi.properties` file that contains the necessary information and place in a jar file so that it can be found on the classpath. The information about this file can be found on the [ActiveMQ documentation](https://activemq.apache.org/jndi-support.html).  In addition of creating a jar with the `jndi.properties` file in it, *the new jar file must be included in each Flow with the Active MQ libraries*, which can be found the [Download Page](https://activemq.apache.org/components/classic/download/).  The Subflow will search for the jndi.properties file and update the necessary AMQ system properties.

