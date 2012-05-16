package jp.ac.kyoto_su.rudds.zola;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class ServiceRelay{

	/*�t�B�[���h*/
	Socket socket; 
	String connectIp;
	int connectPort;
	BufferedReader socketBuffReader;
	BufferedWriter socketBuffWriter;
	ObjectInputStream inObject;
	String serviceType;//�t�@�C�����L�C���y���L�Ƃ������
    String search_protocol;//"�t�@�C�����L"->"_afpovertcp._tcp.local."
    HashMap<String, ArrayList<Object>> relayedService;

    /*�R���X�g���N�^*/
    public ServiceRelay(String search_protocol, String connectIp, String connectPort){
    	super();
    	this.search_protocol = search_protocol + ",";
    	this.connectIp = connectIp;
    	this.connectPort = Integer.parseInt(connectPort);
    }
    
    /*action���\�b�h:���̃N���X���ׂ��������L�q*/
    HashMap<String, ArrayList<Object>> action(){
       	connection();//�T�[�r�X���̎擾���\�b�h
       	streamOpen();
       	setServiceInfo();
       	close();
       	return relayedService;
    }
    
    /*connection()���\�b�h�D�T�[�o�[�ɐڑ����J�n���鏈��*/
    void connection(){ 
    	try{
    		socket = new Socket(connectIp, connectPort);
    		System.out.println("Client�FConection Start");
    	}catch(IOException e){
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null, "Server�ɐڑ��ł��܂���ł���");
    	} 
    } 
    
    /*streamOpen()���\�b�h�D�X�g���[�����J������*/
    void streamOpen(){
 	   try{
 		   /*���̓X�g���[���̓I�u�W�F�N�g�X�g���[���ƕ����X�g���[���̂Q��ނ�p��*/
 		   inObject = new ObjectInputStream(socket.getInputStream());//�I�u�W�F�N�g�X�g���[�����쐬
 		   socketBuffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//�\�P�b�g����̕����X�g���[�����쐬
 		   socketBuffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
 		   System.out.println(socketBuffReader.readLine()); //�T�[�o�[����̐ڑ��m�����b�Z�[�W��\��
 		  socketBuffWriter.write(search_protocol + "\r\n");
 		  socketBuffWriter.flush();
 	   }catch(IOException e){
 		   e.printStackTrace();
 	   } 
    }
    
    void setServiceInfo(){
		try {
			relayedService = (HashMap<String, ArrayList<Object>>) inObject.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    
    /*close()���\�b�h�D�F�X�J�������̂���鏈��*/
    void close(){
         if(inObject != null){
             try{
             	inObject.close();
       			System.out.println("Client�FObjectInputStream closed");
             }
 	    catch(IOException e){
             }
         }
         if(socketBuffReader != null){
             try{
             	socketBuffReader.close();
       			System.out.println("Client�FSocket's BufferedReader closed");
             }
 	    catch(IOException e){
             }
         }
         if(socket != null){
             try{
             	socket.close();
       			System.out.println("Client�FSocket closed");
             }
 	    catch(IOException e){
             }
         }
     } 
}
