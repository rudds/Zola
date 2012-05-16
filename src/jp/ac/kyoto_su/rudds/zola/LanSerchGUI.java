package jp.ac.kyoto_su.rudds.zola;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.swing.JFrame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;


import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;



public class LanSerchGUI extends JFrame{

	/*個体識別番号？何かいるらしい...*/
	private static final long serialVersionUID = 5212768720357876503L;

	/*フィールド*/
    public JPanel rootPanel;
    public JPanel serviceTypePanel;
    public JPanel buttonPanel;
    public JScrollPane serviceInfoPanel;
	
    public JLabel label01;
    public JComboBox serviceTypeList;
    public JButton startButton;
    public JButton stopButton;
    public JList getServiceList;
    public static DefaultListModel services = new DefaultListModel();
    public String[] combodata = {"Apple File Sharing", "HTTP", "SFTP", "SSH"};
    public String serviceType;
    public String serviceName;
	public JmDNS jmdns;



    /*コンストラクタ*/
    public LanSerchGUI(){
    	super();
    	this.setTitle("JmDNS Test");//画面にバージョンを表示！
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       	this.setSize(600,500);
        this.setLocationRelativeTo(null);
        initComponents();//画面の設定メソッド
        this.setVisible(true); 
    }
    
    
    public static void main(String[] args) {
    	new LanSerchGUI();
    }


    public void initComponents(){
    	rootPanel = new JPanel();
    	
    	/*Panel1の作成*/
    	serviceTypePanel = new JPanel();
      	serviceTypePanel.setPreferredSize(new Dimension(500, 50));
    	serviceTypePanel.setLayout(new BoxLayout(serviceTypePanel, BoxLayout.Y_AXIS));
    	label01 = new JLabel("サービスタイプ");
    	serviceTypeList = new JComboBox(combodata);
    	serviceTypeList.setMaximumSize(new Dimension(280, 100));
    	label01.setAlignmentX(0.5f);
    	serviceTypeList.setAlignmentX(0.5f);
       	serviceTypePanel.add(label01);
    	serviceTypePanel.add(serviceTypeList);
    	
    	/*Panel2の作成*/
    	buttonPanel = new JPanel();
    	buttonPanel.setPreferredSize(new Dimension(500, 50));
    	startButton = new JButton("Start");
    	stopButton = new JButton("Stop");
    	buttonPanel.add(startButton);
    	buttonPanel.add(Box.createRigidArea(new Dimension(100,0)));//余白の挿入
    	buttonPanel.add(stopButton);
    	
    	/*Panel3の作成*/
    	serviceInfoPanel = new JScrollPane();
    	serviceInfoPanel.setPreferredSize(new Dimension(500, 300)); 
    	getServiceList = new JList(services);//リストを追加
        serviceInfoPanel.getViewport().setView(getServiceList);
      	
        /*Panelの配置処理*/
    	rootPanel.add(serviceTypePanel);
    	rootPanel.add(Box.createRigidArea(new Dimension(500,10)));//余白の挿入
    	rootPanel.add(buttonPanel);
    	rootPanel.add(Box.createRigidArea(new Dimension(500,10)));//余白の挿入
    	rootPanel.add(serviceInfoPanel);
    	getContentPane().add(rootPanel);
	
		/*イベント処理*/
		ActionListener startButtonAcction = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				services.clear();
				services.addElement((String)serviceTypeList.getSelectedItem());
				serchStart((String)serviceTypeList.getSelectedItem());
			  		}
		    };
		startButton.addActionListener(startButtonAcction);
		
		ActionListener stopButtonAcction = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				serchStop();
			}	
		};
		stopButton.addActionListener(stopButtonAcction);
    }
    
	

    /*サービス検知処理*/
    public void serchStart(String serviceType){
    	this.serviceType = serviceType;
    	if(serviceType.equals("Apple File Sharing")) serviceName = "_afpovertcp._tcp.local.";
    	if(serviceType.equals("HTTP")) serviceName = "_http._tcp.local.";
    	if(serviceType.equals("SFTP")) serviceName = "_sftp-ssh._tcp.local.";
    	if(serviceType.equals("SSH")) serviceName = "_ssh._tcp.local.";
    	try {
			jmdns = JmDNS.create();
            jmdns.addServiceListener(serviceName, new SampleListener());		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /*サービス検知終了*/
    public void serchStop(){
        try {
			jmdns.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    JOptionPane.showMessageDialog(null, "Done");
    }


/*内部クラス(ServuceLitenerインタフェースの実装)*/
static class SampleListener implements ServiceListener {
        
        public void serviceAdded(ServiceEvent event) {
			services.addElement(event.getName() + "." + event.getType());
            //System.out.println("Service resolved: " + event.getInfo().getHostAddress());
			//System.out.println("HostName = " + event.getDNS().getHostName());
        }

        public void serviceRemoved(ServiceEvent event) {
            //System.out.println("Service removed : " + event.getName() + "." + event.getType());
        }

        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service resolved: " + event.getInfo());
        }
    }
	
}