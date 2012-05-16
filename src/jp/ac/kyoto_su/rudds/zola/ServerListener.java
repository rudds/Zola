package jp.ac.kyoto_su.rudds.zola;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerListener extends Thread{
	
	/*フィールド*/
	  Socket socket;
	  ServerSocket serverSocket;
	  ArrayList<String> notSearchNames;
	
	/*コンストラクタ*/
	ServerListener(ArrayList<String> notSearchNames){
		this.notSearchNames = notSearchNames;
		serverSocket = null;
    	socket = null;    	
	}
	
    /*run()メソッド*/
    public void run(){
    	try{ 
    		serverSocket = new ServerSocket(18080);//インスタンスを作成,ポート番号を指定してサーバを立てる．
    		System.out.println("ServiceRelayサーバをポート18080で起動しました．"); 
    		while(true){//クライアントの接続永遠に待つ
    			socket = serverSocket.accept();//acceptメソッドでクライアントからの接続を待ち対応したサーバーのソケットを返す
        		System.out.println("クライアントが接続してきました．");
          		System.out.println("接続ホストのIPアドレス：　" + (socket.getInetAddress()).getHostAddress());
        		System.out.println("接続ホストのホスト名：　" + (socket.getInetAddress()).getHostName());
        		/*クライアントを相手してくれるクラスを作成してそいつにまかす*/
        		System.out.println("Handler作成．"); 
        	    ClientHandler clientHandler = new ClientHandler(socket, notSearchNames);
        	    clientHandler.start();		        	 
    		}    		
       	}catch(IOException e2){
       		e2.printStackTrace(); 
       	}	
    }
    
    /*finish()メソッド(ServerSocketを閉じる処理,SocketはクライアントHandlerが勝手に閉じてくれるので)*/
	public void fisish(){
		if( serverSocket != null){
    		try{
    			serverSocket.close();
      			System.out.println("Server：serverSocket closed");
			}catch (IOException e){
				e.printStackTrace();
			}
    	}
		this.stop();//このスレッドを終了する
	}
    
}
