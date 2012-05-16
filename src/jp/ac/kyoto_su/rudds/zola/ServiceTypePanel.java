package jp.ac.kyoto_su.rudds.zola;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ServiceTypePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	/*フィールド*/
	JLabel jLabelMessage;
	JComboBox serviceTypeList;
	
	/*コンストラクタ*/
	ServiceTypePanel(String message){
		super();
		this.jLabelMessage = new JLabel(message);
		this.setPreferredSize(new Dimension(450, 50));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		String[] combodata = {"Appleファイル共有", "Windowsファイル共有", "Web共有", "音楽共有", "リモートログイン", "画面共有", "SFTP", "プリントサービス", "プリンタ共有"};//ComboBoxの作成
    	serviceTypeList = new JComboBox(combodata);
    	serviceTypeList.setMaximumSize(new Dimension(280, 50));
    	jLabelMessage.setAlignmentX(0.5f);
    	serviceTypeList.setAlignmentX(0.5f);
    	this.add(jLabelMessage);
    	this.add(serviceTypeList);    	   	

	}
	
}

