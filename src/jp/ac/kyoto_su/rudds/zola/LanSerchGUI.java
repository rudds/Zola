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

	/*�̎��ʔԍ��H��������炵��...*/
	private static final long serialVersionUID = 5212768720357876503L;

	/*�t�B�[���h*/
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



    /*�R���X�g���N�^*/
    public LanSerchGUI(){
    	super();
    	this.setTitle("JmDNS Test");//��ʂɃo�[�W������\���I
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       	this.setSize(600,500);
        this.setLocationRelativeTo(null);
        initComponents();//��ʂ̐ݒ胁�\�b�h
        this.setVisible(true); 
    }
    
    
    public static void main(String[] args) {
    	new LanSerchGUI();
    }


    public void initComponents(){
    	rootPanel = new JPanel();
    	
    	/*Panel1�̍쐬*/
    	serviceTypePanel = new JPanel();
      	serviceTypePanel.setPreferredSize(new Dimension(500, 50));
    	serviceTypePanel.setLayout(new BoxLayout(serviceTypePanel, BoxLayout.Y_AXIS));
    	label01 = new JLabel("�T�[�r�X�^�C�v");
    	serviceTypeList = new JComboBox(combodata);
    	serviceTypeList.setMaximumSize(new Dimension(280, 100));
    	label01.setAlignmentX(0.5f);
    	serviceTypeList.setAlignmentX(0.5f);
       	serviceTypePanel.add(label01);
    	serviceTypePanel.add(serviceTypeList);
    	
    	/*Panel2�̍쐬*/
    	buttonPanel = new JPanel();
    	buttonPanel.setPreferredSize(new Dimension(500, 50));
    	startButton = new JButton("Start");
    	stopButton = new JButton("Stop");
    	buttonPanel.add(startButton);
    	buttonPanel.add(Box.createRigidArea(new Dimension(100,0)));//�]���̑}��
    	buttonPanel.add(stopButton);
    	
    	/*Panel3�̍쐬*/
    	serviceInfoPanel = new JScrollPane();
    	serviceInfoPanel.setPreferredSize(new Dimension(500, 300)); 
    	getServiceList = new JList(services);//���X�g��ǉ�
        serviceInfoPanel.getViewport().setView(getServiceList);
      	
        /*Panel�̔z�u����*/
    	rootPanel.add(serviceTypePanel);
    	rootPanel.add(Box.createRigidArea(new Dimension(500,10)));//�]���̑}��
    	rootPanel.add(buttonPanel);
    	rootPanel.add(Box.createRigidArea(new Dimension(500,10)));//�]���̑}��
    	rootPanel.add(serviceInfoPanel);
    	getContentPane().add(rootPanel);
	
		/*�C�x���g����*/
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
    
	

    /*�T�[�r�X���m����*/
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
    
    /*�T�[�r�X���m�I��*/
    public void serchStop(){
        try {
			jmdns.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    JOptionPane.showMessageDialog(null, "Done");
    }


/*�����N���X(ServuceLitener�C���^�t�F�[�X�̎���)*/
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