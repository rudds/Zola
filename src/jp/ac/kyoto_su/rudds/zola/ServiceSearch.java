package jp.ac.kyoto_su.rudds.zola;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;

public class ServiceSearch{

	/*フィールド*/
    ServiceInfo[] serviceInfos;


    /*コンストラクタ*/
    public ServiceSearch(){
    	super();
    }
    
    
    public ServiceInfo[] search(String searchServiceName){
    	try {
			final JmDNS jmdns = JmDNS.create();
			serviceInfos = jmdns.list(searchServiceName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serviceInfos;
    }    
}
