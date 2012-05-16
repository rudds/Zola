package jp.ac.kyoto_su.rudds.zola;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jmdns.ServiceInfo;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class ZolaLauncher extends JFrame{

	/* Identifier */
	private static final long serialVersionUID = 5212768720357876503L;

	/* Fields */
	JTabbedPane tabbedPane;	
	JTabbedPane relayTabbedPane;	
    JPanel sSerchPanel;
    JPanel sRelayClientPanel;
    JPanel serverInfoPanel;	
    JLabel label01;
    JLabel label02;    
    JTextField serverIP;
    JTextField serverPort;   
    JComboBox serviceTypeList;
    JButton serchButton;
    JButton connectButton;
    ServiceTypePanel searchSTP;
    ServiceTypePanel relaySTP;
    HashMap<String, String> searchInfo;
    
    Socket socket;
    ServerSocket serverSocket;
    JPanel sRelayServerPanel;
    JLabel message;
    JButton serverStartButton;
    JPanel checkPanel;
	JScrollPane notSearchPanel;
    JCheckBox[] checkBox;
    ArrayList<String> notSearchNames;
    

    /* Constructor */
    ZolaLauncher(){
    	super();
    	this.setTitle("ServiceScouter");//画面にバージョンを表示！
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//終了処理
       	this.setSize(500,360);//フレームをサイズ600x500で作成
        this.setLocationRelativeTo(null);//フレームを画面中央に表示
        mapping();//サービス情報をマッピング
        initComponents();//画面の設定メソッドへ...
        setResizable(false);//画面のサイズなどを変更不可に
        this.setVisible(true);//画面を表示する 
    }
       
/*    public static void main(String[] args) {
    	new Zola();
    }
    */

    public void mapping(){
        searchInfo = new HashMap<String, String> ();
        searchInfo.put("Appleファイル共有", "_afpovertcp._tcp.local.,afp");
        searchInfo.put("Windowsファイル共有", "_smb._tcp.local.,smb");
        searchInfo.put("Web共有", "_http._tcp.local.,http");
        searchInfo.put("音楽共有", "_daap._tcp.local.,daap" );
        searchInfo.put("リモートログイン", "_ssh._tcp.local.,ssh");      
        searchInfo.put("画面共有", "_rfb._tcp.local.,vnc");
        searchInfo.put("SFTP", "_sftp-ssh._tcp.local.,sftp");   
        searchInfo.put("プリントサービス", "_printer._tcp.local.,lpr");
        searchInfo.put("プリンタ共有", "_ipp._tcp.local.,ipp");
    }

    public void initComponents(){
    	    	    	
    	/*ServiceTypePanelを2つ作成*/
    	searchSTP = new ServiceTypePanel("検索するサービスを選択してください");
    	relaySTP = new ServiceTypePanel("中継するサービスを選択してください");

    	/*serverInfoPanelの作成*/
    	serverInfoPanel = new JPanel();
    	serverInfoPanel.setPreferredSize(new Dimension(450, 60));
    	serverInfoPanel.setLayout(new GridLayout(2, 2));
    	label01 = new JLabel("サーバーアドレス");
    	label02 = new JLabel("ポート番号");
    	serverIP = new JTextField("djembe00");//JTextFieldの幅を15文字分に設定
    	serverPort = new  JTextField("18080");//JTextFieldの幅を15文字分に設定
    	serverInfoPanel.add(label01);
    	serverInfoPanel.add(serverIP);
    	serverInfoPanel.add(label02);
    	serverInfoPanel.add(serverPort);
    	    	
        /*検知画面の作成*/
    	sSerchPanel = new JPanel();
    	serchButton = new JButton("検索");
    	sSerchPanel.add(Box.createRigidArea(new Dimension(500,20)));//余白の挿入
    	sSerchPanel.add(searchSTP);
    	sSerchPanel.add(Box.createRigidArea(new Dimension(500,90)));//余白の挿入
    	sSerchPanel.add(serchButton);
    	
    	/*中継Client画面の作成*/
    	sRelayClientPanel = new JPanel();
    	connectButton = new JButton("中継");
    	sRelayClientPanel.add(relaySTP);
    	sRelayClientPanel.add(Box.createRigidArea(new Dimension(500,5)));//余白の挿入
    	sRelayClientPanel.add(serverInfoPanel);
    	sRelayClientPanel.add(Box.createRigidArea(new Dimension(500,10)));//余白の挿入
    	sRelayClientPanel.add(connectButton);
    	
    	
    	/*中継Server画面の作成*/
    	notSearchNames = new ArrayList<String>();
    	serverSocket = null;
    	socket = null;    	
    	  	
    	sRelayServerPanel = new JPanel();
    	message = new JLabel("中継しないサービスを選択してください");
        serverStartButton = new JButton("ServerStart");
   	
    	checkPanel = new JPanel();
    	checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));//上から下にコンポーネントを配置
    	checkBox = new JCheckBox[8];
    	checkBox[0] = new JCheckBox("Appleファイル共有 ( _afpovertcp._tcp.local. )");
    	checkBox[1] = new JCheckBox("Windowsファイル共有 ( _smp._tcp.local. )");
    	checkBox[2] = new JCheckBox("Web共有 ( _http._tcp.local. )");
    	checkBox[3] = new JCheckBox("音楽共有 ( _daap._tcp.local. )");
    	checkBox[4] = new JCheckBox("リモートログイン ( _ssh._tcp.local. )");
    	checkBox[5] = new JCheckBox("SFTP ( _sftp-ssh._tcp.local. )");
    	checkBox[6] = new JCheckBox("プリントサービス ( _printer._tcp.local. )");
    	checkBox[7] = new JCheckBox("プリンタ共有 ( _ipp._tcp.local. )");
    	for(int i=0; i<checkBox.length; i++){
    		checkPanel.add(checkBox[i]);
    	}
    	
    	notSearchPanel = new JScrollPane();
    	notSearchPanel.setPreferredSize(new Dimension(400, 150));      	
    	notSearchPanel.getViewport().setView(checkPanel);        
   	
    	sRelayServerPanel.add(message);
    	sRelayServerPanel.add(Box.createRigidArea(new Dimension(500,5)));//余白の挿入
    	sRelayServerPanel.add(notSearchPanel);
    	sRelayServerPanel.add(Box.createRigidArea(new Dimension(500,5)));//余白の挿入
    	sRelayServerPanel.add(serverStartButton);
    	getContentPane().add(sRelayServerPanel);    	    	    	
    	
    	/*中継タブに貼付けå*/
    	relayTabbedPane = new JTabbedPane();
    	relayTabbedPane.add("Client",sRelayClientPanel);
    	relayTabbedPane.add("Server",sRelayServerPanel);

    	
    	/*検知画面と中継画面をタブ分け*/
    	tabbedPane = new JTabbedPane();
    	tabbedPane.add("検知", sSerchPanel);
    	tabbedPane.add("中継", relayTabbedPane);
    	getContentPane().add(tabbedPane);
	
		/*イベント処理*/
		ActionListener serchButtonAction = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String searchKey = (String) (searchSTP.serviceTypeList).getSelectedItem();//ComboBoxから値を受けとる
				String value = searchInfo.get(searchKey);//HashMapのvalueを取得
				String searchServiceName = value.substring(0, (value.lastIndexOf(",")));
				String protocolName = value.substring(value.lastIndexOf(",")+1);		
		    	ServiceSearch sSearch = new ServiceSearch(); //sSearchクラス
		    	ServiceInfo[] serviceInfos =sSearch.search(searchServiceName);
		    	ResultView view = new ResultView("Search", searchKey, protocolName, serviceInfos); 
		    	view.draw();
				}
		    };
		serchButton.addActionListener(serchButtonAction);
		
		ActionListener connectButtonAction = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String searchKey = (String) (relaySTP.serviceTypeList).getSelectedItem();//ComboBoxから値を受けとる
				String value = searchInfo.get(searchKey);//HashMapのvalueを取得
				String searchServiceName = value.substring(0, value.lastIndexOf(","));

				String connectIp = serverIP.getText();
				String connectPort = serverPort.getText();
				HashMap<String, ArrayList<Object>> relayedService;
				if(connectIp.equals("") || connectPort.equals("")){
				    JOptionPane.showMessageDialog(null, "入力内容が正しくありません．");
				}else{
			    	ServiceRelay sRelay = new ServiceRelay(value, connectIp, connectPort);
			    	relayedService = sRelay.action();
			    	ResultView view = new ResultView("Relay", searchKey, searchServiceName,  relayedService); 
			    	view.draw();			    
				}
			
			}				
		};
		connectButton.addActionListener(connectButtonAction);
		
		ActionListener serverStartAction = new ActionListener(){
	    	boolean status = true;
	    	ServerListener sListen = null;
			public void actionPerformed(ActionEvent e) {
				/*checkboxの値取得*/
				for(int i=0; i<checkBox.length; i++){
		    		if(checkBox[i].isSelected()){
		    			String value = checkBox[i].getText();
		    			notSearchNames.add(value.substring(value.indexOf("_"), value.lastIndexOf(".")+1));
		    		}
				}
				
				if(sListen == null){
					sListen = new ServerListener(notSearchNames);
				}
				if(status){
					sListen.start();
					serverStartButton.setText("ServerStop");
					serverStartButton.setVisible(true);	
					status = false;
				}
				else{
					sListen.fisish();	
					serverStartButton.setText("ServerStart");
					serverStartButton.setVisible(true);	
					sListen = null;
					status = true;
				}
			}
		};
		serverStartButton.addActionListener(serverStartAction);   
		
    }   
}


