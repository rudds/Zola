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

	/*フィールド*/
	Socket socket; 
	String connectIp;
	int connectPort;
	BufferedReader socketBuffReader;
	BufferedWriter socketBuffWriter;
	ObjectInputStream inObject;
	String serviceType;//ファイル共有，音楽共有という情報
    String search_protocol;//"ファイル共有"->"_afpovertcp._tcp.local."
    HashMap<String, ArrayList<Object>> relayedService;

    /*コンストラクタ*/
    public ServiceRelay(String search_protocol, String connectIp, String connectPort){
    	super();
    	this.search_protocol = search_protocol + ",";
    	this.connectIp = connectIp;
    	this.connectPort = Integer.parseInt(connectPort);
    }
    
    /*actionメソッド:このクラスすべき処理を記述*/
    HashMap<String, ArrayList<Object>> action(){
       	connection();//サービス情報の取得メソッド
       	streamOpen();
       	setServiceInfo();
       	close();
       	return relayedService;
    }
    
    /*connection()メソッド．サーバーに接続を開始する処理*/
    void connection(){ 
    	try{
    		socket = new Socket(connectIp, connectPort);
    		System.out.println("Client：Conection Start");
    	}catch(IOException e){
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null, "Serverに接続できませんでした");
    	} 
    } 
    
    /*streamOpen()メソッド．ストリームを開く処理*/
    void streamOpen(){
 	   try{
 		   /*入力ストリームはオブジェクトストリームと文字ストリームの２種類を用意*/
 		   inObject = new ObjectInputStream(socket.getInputStream());//オブジェクトストリームを作成
 		   socketBuffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//ソケットからの文字ストリームを作成
 		   socketBuffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
 		   System.out.println(socketBuffReader.readLine()); //サーバーからの接続確立メッセージを表示
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
    
    
    /*close()メソッド．色々開いたものを閉じる処理*/
    void close(){
         if(inObject != null){
             try{
             	inObject.close();
       			System.out.println("Client：ObjectInputStream closed");
             }
 	    catch(IOException e){
             }
         }
         if(socketBuffReader != null){
             try{
             	socketBuffReader.close();
       			System.out.println("Client：Socket's BufferedReader closed");
             }
 	    catch(IOException e){
             }
         }
         if(socket != null){
             try{
             	socket.close();
       			System.out.println("Client：Socket closed");
             }
 	    catch(IOException e){
             }
         }
     } 
}
