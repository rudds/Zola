package jp.ac.kyoto_su.rudds.zola;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;


public class ServiceMapHandler {
	
	/*�t�B�[���h*/
	ServiceInfo[] services;
	HashMap<String, String> protocolMap;
    HashMap<String, ArrayList<Object>> serviceMap;
    
    /*�R���X�g���N�^*/
    ServiceMapHandler(){
    	serviceMap = new HashMap<String, ArrayList<Object>>();
    	protocolMap = new HashMap<String, String>();
    }

 
    
    /*getServiceMap()���\�b�h�DServiceMap������ĕԂ�����*/
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
	    /*finally�ň͂񂾕����ǂ��H*/
	   
	    int i =0;
		while(services.length > i){
			ArrayList<Object> infoList = new ArrayList<Object>();
			infoList.add(services[i].getInetAddress());
			infoList.add(services[i].getPort());
			infoList.add(services[i].getURL(procolName));//URL����

			serviceMap.put(services[i].getServer(), infoList);//�T�[�r�X�}�b�v���쐬
            i++;
		}
		return serviceMap;
	}	
}
