package jp.ac.kyoto_su.rudds.zola;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ServiceTypePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	/*�t�B�[���h*/
	JLabel jLabelMessage;
	JComboBox serviceTypeList;
	
	/*�R���X�g���N�^*/
	ServiceTypePanel(String message){
		super();
		this.jLabelMessage = new JLabel(message);
		this.setPreferredSize(new Dimension(450, 50));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		String[] combodata = {"Apple�t�@�C�����L", "Windows�t�@�C�����L", "Web���L", "���y���L", "�����[�g���O�C��", "��ʋ��L", "SFTP", "�v�����g�T�[�r�X", "�v�����^���L"};//ComboBox�̍쐬
    	serviceTypeList = new JComboBox(combodata);
    	serviceTypeList.setMaximumSize(new Dimension(280, 50));
    	jLabelMessage.setAlignmentX(0.5f);
    	serviceTypeList.setAlignmentX(0.5f);
    	this.add(jLabelMessage);
    	this.add(serviceTypeList);    	   	

	}
	
}

