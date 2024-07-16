package j315.r05j.r05j14;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class R05J14Panel extends JPanel {

	private Container parentFrame = this.getParent();
	
	private JLabel jl_1 = new JLabel("�T�[�o�[");
	private JLabel jl_2 = new JLabel("�n���h����");
	private JLabel jl_3 = new JLabel("�Q����");
	private JLabel jl_4 = new JLabel("���b�Z�[�W");
	
	private JTextField jt_1 = new JTextField();
	private JTextField jt_2 = new JTextField();
	private JTextField jt_3 = new JTextField();
	
	private JButton btn1 = new JButton("����");
	private JButton btn2 = new JButton("�ގ�");
	private JButton btn3 = new JButton("���M");
	
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> jlist = new JList<String>(listModel);
	
	private JTextArea jt_area = new JTextArea();
		
	private JScrollPane scrollpane1 = new JScrollPane(jlist,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JScrollPane scrollpane2 = new JScrollPane(jt_area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JScrollBar vsBar = scrollpane1.getVerticalScrollBar();
	
	private Socket socket;
	private String text;


	//�R���X�g���N�^�J�n
	public R05J14Panel(){
		
		 Dimension preferredSize = new Dimension(700,500);//�E�B���h�E�T�C�Y�ݒ�
		 setPreferredSize(new Dimension(700, 555));
		 
		 setLayout(null);//���C�A�E�gnull
		  
         Font font = new Font(Font.MONOSPACED,Font.BOLD,12);
		 
		 jl_1.setBounds(27, 48, 50, 13);
		 add(jl_1);
		 jl_2.setBounds(27, 110, 70, 13);
		 add(jl_2);
		 jl_3.setBounds(27, 237, 50, 13);
		 add(jl_3);
		 jl_4.setBounds(27, 481, 70, 13);
		 add(jl_4);
		 
		 jt_1.setBounds(27, 71, 228, 19);
		 add(jt_1);
		 jt_2.setBounds(27, 133, 228, 19);
		 add(jt_2);
		 jt_3.setBounds(100, 478, 480, 19);
		 add(jt_3);
		 
		 btn1.setBounds(27, 179, 74, 21);
		 add(btn1);
		 btn2.setBounds(181, 179, 74, 21);
		 add(btn2);
		 btn3.setBounds(599, 477, 74, 21);
		 add(btn3);		 
		
		 jlist.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		 jlist.setFont(font);//JTextArea�̃t�H���g��ݒ�
	
		 scrollpane1.setBounds(25, 260, 230, 180);
		 add(scrollpane1);	
		
		 jt_area.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		 jt_area.setFont(font);//JTextArea�̃t�H���g��ݒ�
		 jt_area.setEditable(false);
	
		 scrollpane2.setBounds(311, 48, 362, 392);
		 add(scrollpane2);	
		 
		 btn2.setEnabled(false);
		 btn3.setEnabled(false);
		 jt_3.setEnabled(false);

		 
	   // ����������
		 btn1.addActionListener(
		      new ActionListener(){
				@Override
			   	public void actionPerformed(ActionEvent ae){
					 btn2.setEnabled(true);//�ގ��{�^������
					 btn3.setEnabled(true);//���M�{�^������
					 jt_1.setEnabled(false);//�T�[�o�[���͖���
					 jt_2.setEnabled(false);//�n���h�������͖���
					 jt_3.setEnabled(true);//���M���b�Z�[�W
					 btn1.setEnabled(false);//�����{�^������
					 

					 if(jt_1.getText() != null && jt_2.getText() != null) {//�T�[�o�[�ƃn���h�����̗��������͂��ꂽ��ԂŁu�����v���N���b�N�����ƁA
						                                                   //���͂��ꂽ�T�[�o�[�� 7777(TCP)�ԃ|�[�g�ɐڑ�
						new Thread(() -> { 
							try {
								socket = new Socket(jt_1.getText(),7777);//�ڑ��v��
								
								BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
								
								/***connect�𑗂�***/
								bw.write("connect"+" "+jt_2.getText()+"\r\n");//���������߂񑗂ꂽ
								bw.flush();//�����f�o�C�X�ɏ�������
								
								System.out.println("�N���C�A���g�@���˂��Ɓ@���������I");
									
								receiveMessage();
									
							} catch (Exception e) {
								// TODO �����������ꂽ catch �u���b�N
								e.printStackTrace();
							} 
						 }).start();	
					 }					
				}					   	
		      }
		 );
 
		 
		 //�ގ�������
		 btn2.addActionListener(
		      new ActionListener(){
				@Override
			   	public void actionPerformed(ActionEvent ae){
					
					 new Thread(() -> { 		
						try{
							
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
							
							/***disconnect�𑗂�***/
							bw.write("disconnect"+"\r\n");//�ގ����b�Z�[�W�𑗂�
							bw.flush();//�����f�o�C�X�ɏ�������
							
						}catch (Exception e) {
							e.printStackTrace();
						}
					 }).start();	
				}					   	
		      }
		 );
		 
		 
		 //���M������ 
		 btn3.addActionListener(
			      new ActionListener(){
					@Override
				   	public void actionPerformed(ActionEvent ae){
						new Thread(() -> { 
							try {								
								BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));								
								/***sendMessage�𑗂�***/
								bw.write("sendMessage"+" "+jt_3.getText()+"\r\n");//���������߂񑗂ꂽ
								bw.flush();//�����f�o�C�X�ɏ�������													
							} catch (Exception e) {
								// TODO �����������ꂽ catch �u���b�N
								e.printStackTrace();
								jt_area.append("Error>> �ʐM�G���["+"\n");
								jt_area.setCaretPosition(jt_area.getText().length());
								vsBar.setValue(vsBar.getMaximum());
							}
						}).start();							
					}					   	
			      }
		  );
		 
	}//�R���X�g���N�^�I���
	
	
	public void receiveMessage(){
		
		try {			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while(!socket.isClosed()){ //�\�P�b�g������܂�
				
				text = br.readLine();
				
				System.out.print("****"+text+"****\n");
					
				String[] array = text.split(" ");
				
				switch(array[0]) {
					case "connect":
						jt_area.append(array[1]+"\n");						
						BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						
						/***getUsers�𑗂�***/
						bw2.write("getUsers \r\n");//���������߂񑗂ꂽ
						bw2.flush();//�����f�o�C�X�ɏ�������
						break;						
					case "getUsers":
						//int nameindex = 0;
						SwingUtilities.invokeLater(() -> {
							listModel.clear();
							String[] items = text.split(" "); // �󔒂ŕ������Ĕz��Ɋi�[
				            for (int i=1;i<items.length;i++) {			            	
				                  listModel.addElement(items[i]); // JList�ɃA�C�e����ǉ�
				                  //jt_area.append("\n#"+items[i]+"\n");
				            }
						 });
								
						break;
					case "sendMessage":
						jt_area.append(array[1]+"> "+array[2]+"\n");
						break;
					case "disconnect":
						jt_area.append(array[1]+array[2]+"\n");
						SwingUtilities.invokeLater(() -> { 
							int index = listModel.indexOf(array[1]); // �v�f�̃C���f�b�N�X���擾
					        if (index != -1) {
					            listModel.removeElementAt(index); // �v�f���폜
					           // jt_area.append("�v�f���폜����܂����B");
					        } else {
					           // jt_area.append("�v�f��������܂���ł����B");
					        }
						});
						break;
					case "purge":						
						jt_area.append("���͑ގ����܂����B"+"\n");
						listModel.clear();
						socket.close();	
						
						btn2.setEnabled(false);//�ގ��{�^������
						btn3.setEnabled(false);//���M�{�^������
						jt_1.setEnabled(true);//�T�[�o�[���͖���
						jt_2.setEnabled(true);//�n���h�������͖���
						jt_3.setEnabled(false);//���M���b�Z�[�W
						btn1.setEnabled(true);//�����{�^������
						
						break;		
				}
			}
		}catch(IOException e){
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
			jt_area.append("Error>> �ʐM�G���["+"\n");
		}
		
	}//receive���\�b�h�I���
	
}//�N���X�I���
