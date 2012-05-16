package jp.ac.kyoto_su.rudds.zola;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;


public class ServiceMapHandler {
	
	/*フィールド*/
	ServiceInfo[] services;
	HashMap<String, String> protocolMap;
    HashMap<String, ArrayList<Object>> serviceMap;
    
    /*コンストラクタ*/
    ServiceMapHandler(){
    	serviceMap = new HashMap<String, ArrayList<Object>>();
    	protocolMap = new HashMap<String, String>();
    }

 
    
    /*getServiceMap()メソッド．ServiceMapを作って返す処理*/
	HashMap<String, ArrayList<Object>> getServiceMap(String searchName, String procolName){
	    try {
            final JmDNS jmdns = JmDNS.create();
            System.out.println("Serching...");
            services = jmdns.list(searchName);
            jmdns.close();
            System.out.println("Serching Done");
            System.out.println("");
        }catch (IOException e) {
            e.printStackTrace();
        }
	    /*finallyで囲んだ方が良い？*/
	   
	    int i =0;
		while(services.length > i){
			ArrayList<Object> infoList = new ArrayList<Object>();
			infoList.add(services[i].getInetAddress());
			infoList.add(services[i].getPort());
			infoList.add(services[i].getURL(procolName));//URLを代入

			serviceMap.put(services[i].getServer(), infoList);//サービスマップを作成
            i++;
		}
		return serviceMap;
	}	
}
