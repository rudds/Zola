package jp.ac.kyoto_su.rudds.zola;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class ResultView extends JFrame{
	private static final long serialVersionUID = 1L;
	
	JPanel rootPanel;
	JPanel serviceTypePanel;
	JScrollPane serviceInfoPanel;
	JTable table;
	JLabel label01;
	JLabel label02;
	JButton useButton;
	JButton reDistributionButton;
	ServiceInfo[] serviceInfos;
	HashMap<String, ArrayList<Object>> relayedService;
	HashMap<String, String> protocolMap;
	JmDNS jmdns;
	String type;
	String serviceType;//ファイル共有など
	String serchServiceName;//検索、登録するために必要な名前
	String protocolName;
    String[] url;//サービス利用のためのURLを作成
	
	/*コンストラクタ*/
	public ResultView(String type, String serviceType, String protocolName, ServiceInfo[] serviceInfos){
		super();
		this.setTitle("検索結果");//画面にバージョンを表示！
    	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//終了処理を記述
       	this.setBounds(400,200,600,500);//フレームの場所とサイズを設定(x座標,y座標,幅,高さ)
        this.serviceInfos = serviceInfos;
        this.type = type;
        this.serviceType = serviceType;
        this.protocolName = protocolName;
        this.url = new String[serviceInfos.length];//urlを入力変数をサービス情報と同じ数作成
        setResizable(false);//画面のサイズなどを変更不可に
	}
	
	/*コンストラクタ*/
	public ResultView(String type, String serviceType, String serchServiceName, HashMap<String, ArrayList<Object>> relayedService){
		super();
		this.setTitle("中継結果");//画面にバージョンを表示！
    	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//終了処理を記述
       	this.setBounds(1550,200,600,500);//フレームの場所とサイズを設定(x座標,y座標,幅,高さ)
        this.relayedService = relayedService;
        this.type = type;
        this.serviceType = serviceType;
        this.serchServiceName = serchServiceName;
        if(relayedService != null){
        this.url = new String[relayedService.size()];
        }
        setResizable(false);//画面のサイズなどを変更不可に
	}
	
	
	public void draw(){
	    rootPanel = new JPanel();	    	
	   	/*どんなサービスで検索しているかを表示するパネルを作成*/
	   	serviceTypePanel = new JPanel();
    	serviceTypePanel.setPreferredSize(new Dimension(500, 30));
    	serviceTypePanel.setLayout(new BoxLayout(serviceTypePanel, BoxLayout.Y_AXIS));
    	label01 = new JLabel("検索サービスタイプ");
    	label02 = new JLabel(serviceType);
    	label02.setForeground(Color.BLUE);
    	label01.setAlignmentX(0.5f);
    	label02.setAlignmentX(0.5f);
       	serviceTypePanel.add(label01);
       	serviceTypePanel.add(label02);    		       	
    	
       	/*サービス情報を表示するパネルの作成(JScrollPaneとJTableを使用)*/
    	serviceInfoPanel = new JScrollPane();
    	serviceInfoPanel.setPreferredSize(new Dimension(500, 350));      	
        //JTableを使うための準備
    	String[] columnNames = {"提供マシン", "IPアドレス", "ポート番号"};
    	MyTableModel tableModel = new MyTableModel(columnNames, 0);
    	
  	    //処理の分岐点(SearchとRelayで微妙に違う)
    	if(type.equals("Search")){
      	    Object[][] tabledata = new Object[serviceInfos.length][3];
    		for(int i=0; i<serviceInfos.length; i++){
    			tabledata[i][0]=serviceInfos[i].getServer();
    			tabledata[i][1]=serviceInfos[i].getInetAddress();
    			tabledata[i][2]=serviceInfos[i].getPort();
    			tableModel.addRow(tabledata[i]);
    			url[i] = serviceInfos[i].getURL(protocolName);//サービス利用に必要なURLを代入
    			System.out.println(serviceInfos[i].getTextString());
    			//System.out.println(serviceInfos[i].getName() + ":" + serviceInfos[i].getQualifiedName());
    		}
    	}
    	else if(type.equals("Relay")){
    		if(relayedService != null){
    			 Object[][] tabledata = new Object[relayedService.size()][3];
    	    		Set<String> keyset = relayedService.keySet();
    				Iterator<String> keyIte = keyset.iterator(); 
    		        int i=0;
    		        while(keyIte.hasNext()){
    		        	String serverName = keyIte.next();
    		   			ArrayList<Object> infoList = relayedService.get(serverName);
    		   			InetAddress ipAddress = ( InetAddress) infoList.get(0);
    		   			int portNumber = (Integer) infoList.get(1);
    		   			tabledata[i][0]=serverName;
    		        	tabledata[i][1]=ipAddress;
    		        	tabledata[i][2]=portNumber;
    		        	tableModel.addRow(tabledata[i]);
    		        	url[i] = (String ) infoList.get(2);
    		        	System.out.println("Relayed URL: " + (String ) infoList.get(2));
    		        	i++;
    		        }
    		}	       
    	}
    	//処理の分岐点(ここまで)
    	
	    table = new JTable(tableModel);
	    serviceInfoPanel.getViewport().setView(table);        
	    useButton = new JButton("サービス利用");//ボタンの作成

	    /*Panelの配置処理*/
	    rootPanel.add(serviceTypePanel);
	    rootPanel.add(Box.createRigidArea(new Dimension(500,10)));//余白の挿入
	    rootPanel.add(serviceInfoPanel);
	    rootPanel.add(Box.createRigidArea(new Dimension(500,15)));//余白の挿入
	    rootPanel.add(useButton);
	    if(type.equals("Relay")){
		    reDistributionButton = new JButton("再配布");
	    	rootPanel.add(Box.createRigidArea(new Dimension(50,0)));//余白の挿入
		    rootPanel.add(reDistributionButton);
	    }
	    getContentPane().add(rootPanel);
	    
	    /*イベント処理*/
	    ActionListener useButtonAction = new ActionListener(){	    	    	
	    	public void actionPerformed(ActionEvent e) {
	    	
	    		
	    		try{
	    			Runtime runtime = Runtime.getRuntime();
	    			int row =table.getSelectionModel().getMinSelectionIndex();//行番号を取得
	    			if(row==-1){//選択されていない場合の処理
	    				JOptionPane.showMessageDialog(null, "選択されていません．");
	    			}
	    			else{
	    				if(serviceType.equals("プリンタ共有")){
	    		    		String filePath;
	    		    		String ipAddress;
	    		    		ipAddress = (table.getValueAt(row, 1)).toString();
	    	    			ipAddress = ipAddress.substring(1);
	    	    		    JFileChooser filechooser = new JFileChooser();
	    	    		    int selected = filechooser.showOpenDialog(null);
	    	    		    if (selected == JFileChooser.APPROVE_OPTION){
	    	    		        File file = filechooser.getSelectedFile();
	    	    		        filePath = file.getAbsolutePath();
	    	    		        filePath = filePath.replace(" ", "\\ ");
	    	    		        runtime.exec("lpr -H " + ipAddress + " " + filePath);
	    	    		        System.out.println("lpr -H " + ipAddress + " " + filePath);
	    	    		      }
	    	    		}
	    				else{
	    					System.out.println(url[row]);
		    				runtime.exec("open " + url[row]);
		    				}
	    				}	    				
	    		}catch(IOException e1){
	    			e1.printStackTrace();
	    		}
	    	}
	    };
	    useButton.addActionListener(useButtonAction);
	    
	    if(type.equals("Relay")){
		    ActionListener reDistributionAction = new ActionListener(){
		    	boolean status = true;
		    	ReDistribution rd = null;
		    	public void actionPerformed(ActionEvent e) {
	    			if(rd == null){//インスタンスを生成していなければ
	    				rd = new ReDistribution(relayedService, serchServiceName);
	    			}
		    		if(status){
			    		rd.distribution();
			    		
			    		reDistributionButton.setText("再配布停止");
			    		reDistributionButton.setVisible(true);
			    		status = false;
		    		}else{
			    		rd.stop();

			    		reDistributionButton.setText("再配布");
			    		reDistributionButton.setVisible(true);
			    		status = true;
		    		}
		    				    		
		    	}
		    };
		    reDistributionButton.addActionListener(reDistributionAction);
	    }
	    
	    
	    this.setVisible(true); //透過をoffにする
	}	
	
}

/*内部クラス(JTableを編集不可にする)*/
class MyTableModel extends DefaultTableModel{
	private static final long serialVersionUID = 1L;
	public MyTableModel(){
		super();
	}
	public MyTableModel(String[] aString, int aInt){
		super(aString,aInt);
	}	
	//@Override
	public boolean isCellEditable(int row, int column) {
			return false;
	}
}
