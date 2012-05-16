package jp.ac.kyoto_su.rudds.zola;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerListener extends Thread{
	
	/*�t�B�[���h*/
	  Socket socket;
	  ServerSocket serverSocket;
	  ArrayList<String> notSearchNames;
	
	/*�R���X�g���N�^*/
	ServerListener(ArrayList<String> notSearchNames){
		this.notSearchNames = notSearchNames;
		serverSocket = null;
    	socket = null;    	
	}
	
    /*run()���\�b�h*/
    public void run(){
    	try{ 
    		serverSocket = new ServerSocket(18080);//�C���X�^���X���쐬,�|�[�g�ԍ����w�肵�ăT�[�o�𗧂Ă�D
    		System.out.println("ServiceRelay�T�[�o���|�[�g18080�ŋN�����܂����D"); 
    		while(true){//�N���C�A���g�̐ڑ��i���ɑ҂�
    			socket = serverSocket.accept();//accept���\�b�h�ŃN���C�A���g����̐ڑ���҂��Ή������T�[�o�[�̃\�P�b�g��Ԃ�
        		System.out.println("�N���C�A���g���ڑ����Ă��܂����D");
          		System.out.println("�ڑ��z�X�g��IP�A�h���X�F�@" + (socket.getInetAddress()).getHostAddress());
        		System.out.println("�ڑ��z�X�g�̃z�X�g���F�@" + (socket.getInetAddress()).getHostName());
        		/*�N���C�A���g�𑊎肵�Ă����N���X���쐬���Ă����ɂ܂���*/
        		System.out.println("Handler�쐬�D"); 
        	    ClientHandler clientHandler = new ClientHandler(socket, notSearchNames);
        	    clientHandler.start();		        	 
    		}    		
       	}catch(IOException e2){
       		e2.printStackTrace(); 
       	}	
    }
    
    /*finish()���\�b�h(ServerSocket����鏈��,Socket�̓N���C�A���gHandler������ɕ��Ă����̂�)*/
	public void fisish(){
		if( serverSocket != null){
    		try{
    			serverSocket.close();
      			System.out.println("Server�FserverSocket closed");
			}catch (IOException e){
				e.printStackTrace();
			}
    	}
		this.stop();//���̃X���b�h���I������
	}
    
}
