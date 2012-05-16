package jp.ac.kyoto_su.rudds.zola;
/***Thread�����A�}���`�N���C�A���g�ɑΉ�***/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

 
class ClientHandler extends Thread{ 

    /*�t�B�[���h*/
    Socket socket;
    BufferedReader socketBuffReader;
    BufferedWriter socketBuffWriter;
    ObjectOutputStream outObject;
    String search_protocol;
    ArrayList<String> notSearchNames;
    
    
    /*�R���X�g���N�^*/
    ClientHandler(Socket socket, ArrayList<String> notSearchNames){
    	this.socket = socket;
    	this.notSearchNames = notSearchNames;
    	outObject = null;
    	socketBuffWriter = null;    	
    }
        
    /*run()���\�b�h*/
    public void run(){
    	streamOpen();
    	perform();
    	close();
    }
    
    
	/*streamOpen()���\�b�h�D�X�g���[�����쐬���鏈��*/
    void streamOpen(){
		try{
			/*�o�̓X�g���[���̓I�u�W�F�N�g�X�g���[���ƕ����X�g���[���̂Q�{��p�ӂ���*/
			outObject = new ObjectOutputStream(socket.getOutputStream());//�I�u�W�F�N�g�X�g���[���̍쐬
			socketBuffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//�����X�g���[���̍쐬	
			socketBuffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//�����X�g���[���̍쐬	
			socketBuffWriter.write("Server: Connected to ServiceRelayServer\r\n");//���A���s�R�[�h�͖Y�ꂸ�ɁI
			socketBuffWriter.flush();
			search_protocol = socketBuffReader.readLine();//�N���C�A���g����̃T�[�r�X�^�C�v���󂯂Ƃ�
		}catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /*perform()���\�b�h�D�T�[�r�X�����擾���N���C�A���g�ɑ��M����*/
    void perform(){
    	boolean flag = true;//�����\�t���O�ɐݒ�
    	HashMap<String, ArrayList<Object>> serviceMap;
    	
    	String searchServiceName = search_protocol.substring(0, (search_protocol.indexOf(",")));
    	String procolName = search_protocol.substring((search_protocol.indexOf(","))+1, (search_protocol.lastIndexOf(",")));

    	System.out.println("searchName: " + searchServiceName);
     	System.out.println("protocolName: " + procolName);
     	
     	for(int i=0; i < notSearchNames.size(); i++){
     		if(searchServiceName.equals(notSearchNames.get(i))){
     			flag = false;//�����s�\��
     			System.out.println("Not Search");
     		}
     	}
     	try {
     		if(flag){
     			ServiceMapHandler smh = new ServiceMapHandler();
     			serviceMap = smh.getServiceMap(searchServiceName, procolName); 		
     		}
     		else{
     			serviceMap = null;
     			Thread.sleep(5000);//������Ƃ܂�����
     		}
     		outObject.writeObject(serviceMap);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    	
    }
    
    
    /*close()���\�b�h�D�F�X�J�������m����鏈��*/
    void close(){
    	if( outObject != null){
    		try{
				outObject.close();
      			System.out.println("Server�FObjectInputStream closed");
			}catch (IOException e){
				e.printStackTrace();
			}
    	}
    	if( socketBuffWriter != null){
    		try{
    			socketBuffWriter.close();
      			System.out.println("Server�FBufferdWriter closed");
			}catch (IOException e){
				e.printStackTrace();
			}
    	}    
    	if( socket != null){
    		try{
				socket.close();
      			System.out.println("Server�FSocket closed");
			}catch (IOException e){
				e.printStackTrace();
			}
    	}    	
    }   
} 