package jp.ac.kyoto_su.rudds.zola;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class ReDistribution {

	/*フィールド*/
	HashMap<String, ArrayList<Object>> relayedService;
	String serchServiceName;
	JmDNS[] jmdns;
	int serviceNum;
	ServiceInfo[] serviceInfos;
	InetAddress[] addresses;
	String[] serverNames;
	int[] portNumbers;
	
	/*コンストラクタ*/
	ReDistribution(HashMap<String, ArrayList<Object>> relayedService, String serchServiceName){
		this.relayedService = relayedService;
		this.serchServiceName = serchServiceName;
		serviceNum = relayedService.size();
		jmdns =  new JmDNS [serviceNum];

		System.out.println("Number of Service is " + serviceNum);
		System.out.println("serchServiceName is " + serchServiceName);

		addresses = new InetAddress [serviceNum];
		serverNames = new String [serviceNum];
		portNumbers = new int [serviceNum]; 
        serviceInfos = new ServiceInfo[serviceNum];
	}
	

	
	void distribution(){

		Set<String> keyset = relayedService.keySet();
		Iterator<String> keyIte = keyset.iterator(); 
		   int i=0;
	        while(keyIte.hasNext()){
	        	String serverName = keyIte.next();
	   			ArrayList<Object> infoList = relayedService.get(serverName);
	   			addresses[i] = (InetAddress) infoList.get(0);
	   			portNumbers[i] = (Integer) infoList.get(1);
	   			if(portNumbers[i] == 0) serverNames[i] = "test" + i;
	   			else serverNames[i] = serverName.substring(0, serverName.indexOf("."));
	   			
	   			System.out.println("serverName" + serverNames[i]) ;
	   			i++;
	   		}
		
		for(int s = 0; s < serviceNum; s++){
			try {
	            System.out.println("serchService is " + serchServiceName);
				jmdns[s] = JmDNS.create(addresses[s]);
				serviceInfos[s] = ServiceInfo.create(serchServiceName, serverNames[s], portNumbers[s], 0, 0, "test of afp" + s);
	            System.out.println(serchServiceName);
	            jmdns[s].registerService(serviceInfos[s]);
	            System.out.println("registing" + s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}     
	}

	void stop() {
		  try {
				System.out.println("Closing JmDNS...");
				for(int s = 0; s < serviceNum; s++){	
					jmdns[s].unregisterService(serviceInfos[0]);
					//jmdns[s].unregisterAllServices();
					jmdns[s].close();
				}
	            System.out.println("Done!");
			} catch (IOException e) {
				e.printStackTrace();
			}  		
	}
}
