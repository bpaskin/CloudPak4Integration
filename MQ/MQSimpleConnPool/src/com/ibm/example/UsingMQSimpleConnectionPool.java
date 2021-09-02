package com.ibm.example;

import java.io.IOException;
import java.util.Hashtable;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQSimpleConnectionManager;
import com.ibm.mq.constants.MQConstants;

public class UsingMQSimpleConnectionPool {

	private static String QMGR_NAME = "CLUSQM1";
	private static String QUEUE_NAME = "CLUSQM1.LQ";
	
	public static void main(String[] args) {

	// set up the connections
	MQSimpleConnectionManager conn = new MQSimpleConnectionManager();
        conn.setActive(MQSimpleConnectionManager.MODE_AUTO);
        conn.setTimeout(300000); // 5 minutes
        conn.setMaxConnections(10);
        conn.setMaxUnusedConnections(5);
        MQEnvironment.setDefaultConnectionManager(conn);
        
    	MQPutMessageOptions pmo = new MQPutMessageOptions();
    	
    	MQGetMessageOptions gmo = new MQGetMessageOptions();
    	gmo.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_CONVERT;
    	gmo.waitInterval = 5000; // wait upto 5 seconds for a message
    	
    	Hashtable<String, Object> props = new Hashtable<String, Object>();
        props.put(MQConstants.HOST_NAME_PROPERTY, "lovecraft"); 
        props.put(MQConstants.PORT_PROPERTY, 2000);
        props.put(MQConstants.CHANNEL_PROPERTY, "CLUSQM1.SVRCONN");
        props.put(MQConstants.USER_ID_PROPERTY, "brian");
        props.put(MQConstants.PASSWORD_PROPERTY, "mypassword");

    	
        try {
        	MQQueueManager qmgr1  = new MQQueueManager(QMGR_NAME, props, conn);
        	MQQueueManager qmgr2  = new MQQueueManager(QMGR_NAME, props, conn);
        	MQQueueManager qmgr3  = new MQQueueManager(QMGR_NAME, props, conn);
        	MQQueueManager qmgr4  = new MQQueueManager(QMGR_NAME, props, conn);
        	
        	int openOptions = MQConstants.MQOO_OUTPUT | MQConstants.MQOO_INPUT_AS_Q_DEF;
        	MQQueue queue1 = qmgr1.accessQueue(QUEUE_NAME, openOptions);
        	MQMessage msg1 = new MQMessage();
        	msg1.writeUTF("MQ is so cool!");
        	queue1.put(msg1, pmo);

        	MQQueue queue2 = qmgr1.accessQueue(QUEUE_NAME, openOptions);
        	MQMessage msg2 = new MQMessage();
        	queue2.get(msg2, gmo);
        	byte[] b = new byte[msg2.getMessageLength()];
        	msg2.readFully(b);
        	System.out.println("message : " + new String(b));
        	msg2.clearMessage();
        	
        	queue1.close();
        	queue2.close();
        	qmgr1.disconnect();
        	qmgr2.disconnect();
        	qmgr3.disconnect();
        	qmgr4.disconnect();
        } catch (MQException | IOException e) {
        	e.printStackTrace(System.err);
        }
     }

}
