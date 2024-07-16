package j315.r05j.r05j15;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.time.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class R05J15Panel extends JPanel implements Runnable{
	
	private Container     parentFrame = this.getParent();
	
	private JTextArea jt_area = new JTextArea();
	private JScrollPane scrollpane = new JScrollPane(jt_area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JScrollBar vsBar = scrollpane.getVerticalScrollBar();	
	private ServerSocket serversoc;//7777�ԃ|�[�g�Ƀo�C���h���ꂽ�T�[�o�[�\�P�b�g�쐬//�T�[�o�\�P�b�g����
	
	private ArrayList<Socket> socketlist = new ArrayList<>();
	private ArrayList<String> namelist = new ArrayList<>();	
	private ChatClient chatclient;
	
	private int socnum = 0;
	private final Object socnumLock = new Object();
	private Timer timer;
	
	//�R���X�g���N�^�J�n
	public R05J15Panel(){

			 Dimension preferredSize = new Dimension(600,500);//�E�B���h�E�T�C�Y�ݒ�
			 setPreferredSize(new Dimension(600, 500));
			 
			 setLayout(null);//���C�A�E�gnull
			 
			 Font font = new Font(Font.MONOSPACED,Font.BOLD,12);
			 
			 jt_area.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			 jt_area.setFont(font);//JTextArea�̃t�H���g��ݒ�
			 jt_area.setEditable(false);
		
			 scrollpane.setBounds(0,0,600,479);
			 add(scrollpane);	
			 		 
			 new Thread(() -> {
		    	     	 
				  try {
					  serversoc = new ServerSocket(7777);
									  
						while(true) {//�������[�v���Ă����Ƒҋ@					
							    
								System.out.println("�T�[�o�[�͉ғ����Ă��܂��B");
								Socket socket = serversoc.accept();
								
								

								socketlist.add(socket);//�ڑ��v�����Ă����\�P�b�g�����X�g�ɒǉ�
								//jt_area.append(socketlist.size()+"���߁I\n");
								
						        socnum = socketlist.size();
								
							/*	new Thread(()->{								
								   chatclient = new ChatClient(socketlist.get(socnum-1));									
								}).start(); */
						        
						        new ChatClient(socketlist.get(socnum-1)).start();
						 }
						
					} catch (Exception e) {
						// TODO �����������ꂽ catch �u���b�N
						e.printStackTrace();
						jt_area.append("Error>> " + e + "\n");
					}
			    	
			   }).start();	
				 			 
	}//�R���X�g���N�^�I���
		
		//���b�Z�[�W�Ď��p�̃X���b�h �����H
	@Override
	public void run(){
			try{
				
			}catch(Exception e){
				
			}
	}
		
		
	 public class ChatClient extends Thread{//�X���b�h�̃T�u�N���X�ɂ���
		 
		private Socket socket;
		 
		ChatClient(Socket socket){//�R���X�g���N�^�����I����
			//�C���X�^���X�����������邽�߂̂��̂���
			//socket �C���X�^���X�ϐ���������			
			this.socket = socket;			
		}//�R���X�g���N�^�I���
		
		@Override
		public void run() {
			try {
				String name = null;
				
				timer = new Timer(2000,(e) -> {
					//socket.setSoTimeout(2000); //�Q�b�ȓ���"����"����M���Ȃ��ꍇ						 
					timer.stop();
					
					socketlist.remove(socket);//�C���f�b�N�X�P�������P�͈̔͊O�ł�
					try {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						bw.write("purge"+"\r\n");
						bw.flush();
					} catch (Exception e1) {
						// TODO �����������ꂽ catch �u���b�N
						e1.printStackTrace();
					}//�����ޏo���b�Z�[�W�𑗂�
							
					return;
				 });
				
				 timer.start();
				
				while(!socket.isClosed()){	//run()
					
					BufferedReader br= new BufferedReader(new InputStreamReader(socket.getInputStream()));					
					String text = br.readLine();
						
					String[] array = text.split(" ");				
					
					jt_area.append(text+"\n");
									
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
					switch(array[0]){
						case "connect":	
							timer.stop();
							if(!namelist.contains(array[1])) {//�������X�g�ɒǉ��ς݂��ǂ���
								jt_area.append(array[1]+"> �ڑ�\n");
								every_send("connect"+" "+array[1]+"���񂪓������܂����B"+"\r\n");//�݂�ȂɎ������������̔z�M
								name = array[1];//���O���Ƃ��Ă��āAname�ɓ����
		                        namelist.add(name);//namelist�ɒǉ� 
							}else {
								socketlist.remove(socket);//�C���f�b�N�X�P�������P�͈̔͊O�ł�
								bw.write("purge"+"\r\n");//�����ޏo���b�Z�[�W�𑗂�
								bw.flush();
								socket.close();//�\�P�b�g����
							}
							break;
						case "getUsers":
							String namedata = String.join(" ", namelist);
								bw.write("getUsers"+" "+namedata+"\r\n");//���O���X�g�𑗂�
								bw.flush();//�����f�o�C�X�ɏ�������	
							break;
						case "sendMessage":
							every_send("sendMessage"+" "+name+" "+array[1]+"\r\n");//�݂�ȂɎ������������̔z�M
							break;							
						case "disconnect":															
	                        namelist.remove(name);
							socketlist.remove(socket);//�C���f�b�N�X�P�������P�͈̔͊O�ł�
							
							bw.write("purge"+"\r\n");//�����ޏo���b�Z�[�W�𑗂�
							bw.flush();
							
							every_send("disconnect"+" "+name+" ���񂪑ގ����܂����B"+"\r\n");//�݂�ȂɎ������������̔z�M
							
							socket.close();//�\�P�b�g����															
							break;
							
					}//switch���I���	
				}//while�I���
					
			}catch(IOException e){
				// TODO �����������ꂽ catch �u���b�N	
				e.printStackTrace();
			}	
		
		
		}//run()�I���
		
		//���݂̂�ȂɃ��b�Z�[�W���郁�\�b�h
	    public void every_send(String message) {	
			try {
				for(int i=0;i<socketlist.size();i++) {//���݂̂�Ȃɂ�����
					Socket soc = socketlist.get(i);
					BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
					
						bw1.write(message);
						bw1.flush();//�����f�o�C�X�ɏ�������		
					
				}
			} catch (Exception e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
		}
				
	}//ChatClient�N���X�I���
		
		

		
}




