import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;


public class AMQExamole_JavaCompute extends MbJavaComputeNode {
	@Override
	public void onPreSetupValidation() throws MbException {
	}

	@Override
	public void onSetup() throws MbException {
	}

	@Override
	public void onStart() throws MbException {
		System.out.println(">>> Startup of reading jndi.properties file");
		
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("jndi.properties");	
		
		if (in == null) {
			System.out.println("Could not find jndi.properties file for AMQ");
			return;
		} else {
			System.out.println("Found jndi.properties file for AMQ");
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		String[] value;
		try {
			while ((line = br.readLine()) != null) {
				if (line.contains("java.naming.provider.url")) {
					
					value = line.split("=");
					if (value.length != 2) {
						System.err.println("String/Value pair for java.naming.provider.url is not correct");
						return;
					}
					
					String trimmed = value[1].trim();
					System.out.println("trimmed value = " + trimmed);
					
					value = trimmed.split("tcp://");
					
					if (value.length != 2) {
						System.err.println("java.naming.provider.url must be in format tcp://host:port");
						return;
					}
					
					value = value[1].split(":");
					if (value.length != 2) {
						System.err.println("java.naming.provider.url must have host:port");
						return;
					}
					
					System.setProperty("org.apache.activemq.AMQ_HOST", value[0]);
					System.setProperty("org.apache.activemq.AMQ_PORT", value[1]);	
					
					
					// update file /home/aceuser/ace-server/run/JMSPolicies/ActiveMQ.policyxml
//					File policy = new File("/home/aceuser/ace-server/run/JMSPolicies/ActiveMQ.policyxml");
//					
//					if (policy.exists()) {
//						System.out.println("Found ActiveMQ.policyxml for updating");
//					} else{
//						System.err.println("Could not find ActiveMQ.policyxml for updating");
//						return;
//					}
//					
//					String lines = new String();
//					Scanner scanner = new Scanner(policy);
//					while (scanner.hasNextLine()) {
//						String data = scanner.nextLine();
//						
//						if (data.contains("jndiBindingsLocation")) {
//							lines = lines + "<jndiBindingsLocation>" + trimmed + "</jndiBindingsLocation>";
//						} else {
//							lines = lines + data;
//						}
//					}
//					scanner.close();
//					System.out.println(lines);	
//					
//					//FileWriter fw = new FileWriter("/home/aceuser/ace-server/run/JMSPolicies/ActiveMQ.policyxml");
//					FileWriter fw = new FileWriter("/home/aceuser/ace-server/overrides/ActiveMQ.policyxml");
//
//					fw.write(lines);
//					fw.close();
//					
//					System.out.println("Policy file has been updated!");

				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return;
		}		
		
	}
	
	@Override
	public void onStop(boolean wait) throws MbException {
	}

	@Override
	public void onTearDown() throws MbException {
	}

	@Override
	public void evaluate(MbMessageAssembly arg0) throws MbException {
		
	}

}
