package j315.r05j.r05j15;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;




public class R05J15 extends JFrame{

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run(){
					R05J15 frame = new R05J15("���K���15:Chat Server");//�^�C�g���������ŃE�B���h�E����
					
				}
			}
		);
	}
	
	
	private R05J15Panel 			panel = new R05J15Panel();//GUI���i�̏�����p�l������
	private JPanel      			contentPane;
	
	private JMenuBar jm_bar = new JMenuBar();
	private JMenu jm = new JMenu("�t�@�C��");
    private JMenuItem jm_i = new JMenuItem("�I��");
    
    R05J15(String title){
		super(title);
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(
				new WindowAdapter(){ 	
					@Override
					public void windowClosing(WindowEvent e){			
						int retVal = JOptionPane.showConfirmDialog(
							null,
							"�v���O�������I�����܂����H",
							"�I���m�F",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
						);			
			
						if( retVal == JOptionPane.YES_OPTION ){
							System.exit(0); 
						}
					}
				}
		);
		
		contentPane = (JPanel)getContentPane();//�E�B���h�E�̃R���e���c�y�[�����擾
		Dimension panelPreferredSize = panel.getPreferredSize();//�p�l���̐����T�C�Y���擾

		//�E�B���h�E�̃R���e���c�y�[���̐����T�C�Y���p�l���̂���Ɠ����l�ɐݒ�
		contentPane.setPreferredSize(panelPreferredSize);
		pack();//�E�B���h�E�̃T�C�Y���T�u�R���|�[�l���g�ɍ��킹��

		//��ʂ̒����Ɂi��ƂȂ�R���|�[�l���g��null�ɂ����ꍇ�AJFrame�͉�ʒ����ɔz�u�����j
		setLocationRelativeTo(null);
		setContentPane(panel);//�R���e���c�y�[���Ƀp�l��1��ǉ�
				
		setResizable(false);//�E�B���h�E�̃T�C�Y�ύX�s��
		
		this.setVisible(true);//���̃E�B���h�E�\��
		
		 jm.add(jm_i);
		 jm_bar.add(jm);
		 setJMenuBar(jm_bar);
		 
		 jm_i.addActionListener(
			      new ActionListener(){
					@Override
				   	public void actionPerformed(ActionEvent ae){
		      				System.exit(0);
		    		}			   	
			      }
	      );
	}

}



