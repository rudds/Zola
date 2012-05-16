package jp.ac.kyoto_su.rudds.zola;
/***Thread化し、マルチクライアントに対応***/

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

    /*フィールド*/
    Socket socket;
    BufferedReader socketBuffReader;
    BufferedWriter socketBuffWriter;
    ObjectOutputStream outObject;
    String search_protocol;
    ArrayList<String> notSearchNames;
    
    
    /*コンストラクタ*/
    ClientHandler(Socket socket, ArrayList<String> notSearchNames){
    	this.socket = socket;
    	this.notSearchNames = notSearchNames;
    	outObject = null;
    	socketBuffWriter = null;    	
    }
        
    /*run()メソッド*/
    public void run(){
    	streamOpen();
    	perform();
    	close();
    }
    
    
	/*streamOpen()メソッド．ストリームを作成する処理*/
    void streamOpen(){
		try{
			/*出力ストリームはオブジェクトストリームと文字ストリームの２本を用意する*/
			outObject = new ObjectOutputStream(socket.getOutputStream());//オブジェクトストリームの作成
			socketBuffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//文字ストリームの作成	
			socketBuffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//文字ストリームの作成	
			socketBuffWriter.write("Server: Connected to ServiceRelayServer\r\n");//復帰改行コードは忘れずに！
			socketBuffWriter.flush();
			search_protocol = socketBuffReader.readLine();//クライアントからのサービスタイプを受けとる
		}catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /*perform()メソッド．サービス情報を取得しクライアントに送信する*/
    void perform(){
    	boolean flag = true;//検索可能フラグに設定
    	HashMap<String, ArrayList<Object>> serviceMap;
    	
    	String searchServiceName = search_protocol.substring(0, (search_protocol.indexOf(",")));
    	String procolName = search_protocol.substring((search_protocol.indexOf(","))+1, (search_protocol.lastIndexOf(",")));

    	System.out.println("searchName: " + searchServiceName);
     	System.out.println("protocolName: " + procolName);
     	
     	for(int i=0; i < notSearchNames.size(); i++){
     		if(searchServiceName.equals(notSearchNames.get(i))){
     			flag = false;//検索不可能に
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
     			Thread.sleep(5000);//ちょっとまたせる
     		}
     		outObject.writeObject(serviceMap);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    	
    }
    
    
    /*close()メソッド．色々開いたモノを閉じる処理*/
    void close(){
    	if( outObject != null){
    		try{
				outObject.close();
      			System.out.println("Server：ObjectInputStream closed");
			}catch (IOException e){
				e.printStackTrace();
			}
    	}
    	if( socketBuffWriter != null){
    		try{
    			socketBuffWriter.close();
      			System.out.println("Server：BufferdWriter closed");
			}catch (IOException e){
				e.printStackTrace();
			}
    	}    
    	if( socket != null){
    		try{
				socket.close();
      			System.out.println("Server：Socket closed");
			}catch (IOException e){
				e.printStackTrace();
			}
    	}    	
    }   
} 