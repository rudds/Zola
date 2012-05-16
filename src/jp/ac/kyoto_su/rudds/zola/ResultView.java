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
	String serviceType;//�t�@�C�����L�Ȃ�
	String serchServiceName;//�����A�o�^���邽�߂ɕK�v�Ȗ��O
	String protocolName;
    String[] url;//�T�[�r�X���p�̂��߂�URL���쐬
	
	/*�R���X�g���N�^*/
	public ResultView(String type, String serviceType, String protocolName, ServiceInfo[] serviceInfos){
		super();
		this.setTitle("��������");//��ʂɃo�[�W������\���I
    	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//�I���������L�q
       	this.setBounds(400,200,600,500);//�t���[���̏ꏊ�ƃT�C�Y��ݒ�(x���W,y���W,��,����)
        this.serviceInfos = serviceInfos;
        this.type = type;
        this.serviceType = serviceType;
        this.protocolName = protocolName;
        this.url = new String[serviceInfos.length];//url����͕ϐ����T�[�r�X���Ɠ������쐬
        setResizable(false);//��ʂ̃T�C�Y�Ȃǂ�ύX�s��
	}
	
	/*�R���X�g���N�^*/
	public ResultView(String type, String serviceType, String serchServiceName, HashMap<String, ArrayList<Object>> relayedService){
		super();
		this.setTitle("���p����");//��ʂɃo�[�W������\���I
    	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//�I���������L�q
       	this.setBounds(1550,200,600,500);//�t���[���̏ꏊ�ƃT�C�Y��ݒ�(x���W,y���W,��,����)
        this.relayedService = relayedService;
        this.type = type;
        this.serviceType = serviceType;
        this.serchServiceName = serchServiceName;
        if(relayedService != null){
        this.url = new String[relayedService.size()];
        }
        setResizable(false);//��ʂ̃T�C�Y�Ȃǂ�ύX�s��
	}
	
	
	public void draw(){
	    rootPanel = new JPanel();	    	
	   	/*�ǂ�ȃT�[�r�X�Ō������Ă��邩��\������p�l�����쐬*/
	   	serviceTypePanel = new JPanel();
    	serviceTypePanel.setPreferredSize(new Dimension(500, 30));
    	serviceTypePanel.setLayout(new BoxLayout(serviceTypePanel, BoxLayout.Y_AXIS));
    	label01 = new JLabel("�����T�[�r�X�^�C�v");
    	label02 = new JLabel(serviceType);
    	label02.setForeground(Color.BLUE);
    	label01.setAlignmentX(0.5f);
    	label02.setAlignmentX(0.5f);
       	serviceTypePanel.add(label01);
       	serviceTypePanel.add(label02);    		       	
    	
       	/*�T�[�r�X����\������p�l���̍쐬(JScrollPane��JTable���g�p)*/
    	serviceInfoPanel = new JScrollPane();
    	serviceInfoPanel.setPreferredSize(new Dimension(500, 350));      	
        //JTable���g�����߂̏���
    	String[] columnNames = {"�񋟃}�V��", "IP�A�h���X", "�|�[�g�ԍ�"};
    	MyTableModel tableModel = new MyTableModel(columnNames, 0);
    	
  	    //�����̕���_(Search��Relay�Ŕ����ɈႤ)
    	if(type.equals("Search")){
      	    Object[][] tabledata = new Object[serviceInfos.length][3];
    		for(int i=0; i<serviceInfos.length; i++){
    			tabledata[i][0]=serviceInfos[i].getServer();
    			tabledata[i][1]=serviceInfos[i].getInetAddress();
    			tabledata[i][2]=serviceInfos[i].getPort();
    			tableModel.addRow(tabledata[i]);
    			url[i] = serviceInfos[i].getURL(protocolName);//�T�[�r�X���p�ɕK�v��URL����
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
    	//�����̕���_(�����܂�)
    	
	    table = new JTable(tableModel);
	    serviceInfoPanel.getViewport().setView(table);        
	    useButton = new JButton("�T�[�r�X���p");//�{�^���̍쐬

	    /*Panel�̔z�u����*/
	    rootPanel.add(serviceTypePanel);
	    rootPanel.add(Box.createRigidArea(new Dimension(500,10)));//�]���̑}��
	    rootPanel.add(serviceInfoPanel);
	    rootPanel.add(Box.createRigidArea(new Dimension(500,15)));//�]���̑}��
	    rootPanel.add(useButton);
	    if(type.equals("Relay")){
		    reDistributionButton = new JButton("�Ĕz�z");
	    	rootPanel.add(Box.createRigidArea(new Dimension(50,0)));//�]���̑}��
		    rootPanel.add(reDistributionButton);
	    }
	    getContentPane().add(rootPanel);
	    
	    /*�C�x���g����*/
	    ActionListener useButtonAction = new ActionListener(){	    	    	
	    	public void actionPerformed(ActionEvent e) {
	    	
	    		
	    		try{
	    			Runtime runtime = Runtime.getRuntime();
	    			int row =table.getSelectionModel().getMinSelectionIndex();//�s�ԍ����擾
	    			if(row==-1){//�I������Ă��Ȃ��ꍇ�̏���
	    				JOptionPane.showMessageDialog(null, "�I������Ă��܂���D");
	    			}
	    			else{
	    				if(serviceType.equals("�v�����^���L")){
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
	    			if(rd == null){//�C���X�^���X�𐶐����Ă��Ȃ����
	    				rd = new ReDistribution(relayedService, serchServiceName);
	    			}
		    		if(status){
			    		rd.distribution();
			    		
			    		reDistributionButton.setText("�Ĕz�z��~");
			    		reDistributionButton.setVisible(true);
			    		status = false;
		    		}else{
			    		rd.stop();

			    		reDistributionButton.setText("�Ĕz�z");
			    		reDistributionButton.setVisible(true);
			    		status = true;
		    		}
		    				    		
		    	}
		    };
		    reDistributionButton.addActionListener(reDistributionAction);
	    }
	    
	    
	    this.setVisible(true); //���߂�off�ɂ���
	}	
	
}

/*�����N���X(JTable��ҏW�s�ɂ���)*/
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
