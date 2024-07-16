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
	private ServerSocket serversoc;//7777番ポートにバインドされたサーバーソケット作成//サーバソケット生成
	
	private ArrayList<Socket> socketlist = new ArrayList<>();
	private ArrayList<String> namelist = new ArrayList<>();	
	private ChatClient chatclient;
	
	private int socnum = 0;
	private final Object socnumLock = new Object();
	private Timer timer;
	
	//コンストラクタ開始
	public R05J15Panel(){

			 Dimension preferredSize = new Dimension(600,500);//ウィンドウサイズ設定
			 setPreferredSize(new Dimension(600, 500));
			 
			 setLayout(null);//レイアウトnull
			 
			 Font font = new Font(Font.MONOSPACED,Font.BOLD,12);
			 
			 jt_area.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			 jt_area.setFont(font);//JTextAreaのフォントを設定
			 jt_area.setEditable(false);
		
			 scrollpane.setBounds(0,0,600,479);
			 add(scrollpane);	
			 		 
			 new Thread(() -> {
		    	     	 
				  try {
					  serversoc = new ServerSocket(7777);
									  
						while(true) {//無限ループしてずっと待機					
							    
								System.out.println("サーバーは稼働しています。");
								Socket socket = serversoc.accept();
								
								

								socketlist.add(socket);//接続要求してきたソケットをリストに追加
								//jt_area.append(socketlist.size()+"こめ！\n");
								
						        socnum = socketlist.size();
								
							/*	new Thread(()->{								
								   chatclient = new ChatClient(socketlist.get(socnum-1));									
								}).start(); */
						        
						        new ChatClient(socketlist.get(socnum-1)).start();
						 }
						
					} catch (Exception e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
						jt_area.append("Error>> " + e + "\n");
					}
			    	
			   }).start();	
				 			 
	}//コンストラクタ終わり
		
		//メッセージ監視用のスレッド いるん？
	@Override
	public void run(){
			try{
				
			}catch(Exception e){
				
			}
	}
		
		
	 public class ChatClient extends Thread{//スレッドのサブクラスにする
		 
		private Socket socket;
		 
		ChatClient(Socket socket){//コンストラクタすぐ終える
			//インスタンスを初期化するためのものだけ
			//socket インスタンス変数もうける			
			this.socket = socket;			
		}//コンストラクタ終わり
		
		@Override
		public void run() {
			try {
				String name = null;
				
				timer = new Timer(2000,(e) -> {
					//socket.setSoTimeout(2000); //２秒以内に"入室"を受信しない場合						 
					timer.stop();
					
					socketlist.remove(socket);//インデックス１が長さ１の範囲外です
					try {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						bw.write("purge"+"\r\n");
						bw.flush();
					} catch (Exception e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}//強制退出メッセージを送る
							
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
							if(!namelist.contains(array[1])) {//もうリストに追加済みかどうか
								jt_area.append(array[1]+"> 接続\n");
								every_send("connect"+" "+array[1]+"さんが入室しました。"+"\r\n");//みんなに自分が送ったの配信
								name = array[1];//名前をとってきて、nameに入れる
		                        namelist.add(name);//namelistに追加 
							}else {
								socketlist.remove(socket);//インデックス１が長さ１の範囲外です
								bw.write("purge"+"\r\n");//強制退出メッセージを送る
								bw.flush();
								socket.close();//ソケット閉じる
							}
							break;
						case "getUsers":
							String namedata = String.join(" ", namelist);
								bw.write("getUsers"+" "+namedata+"\r\n");//名前リストを送る
								bw.flush();//物理デバイスに書き込み	
							break;
						case "sendMessage":
							every_send("sendMessage"+" "+name+" "+array[1]+"\r\n");//みんなに自分が送ったの配信
							break;							
						case "disconnect":															
	                        namelist.remove(name);
							socketlist.remove(socket);//インデックス１が長さ１の範囲外です
							
							bw.write("purge"+"\r\n");//強制退出メッセージを送る
							bw.flush();
							
							every_send("disconnect"+" "+name+" さんが退室しました。"+"\r\n");//みんなに自分が送ったの配信
							
							socket.close();//ソケット閉じる															
							break;
							
					}//switch文終わり	
				}//while終わり
					
			}catch(IOException e){
				// TODO 自動生成された catch ブロック	
				e.printStackTrace();
			}	
		
		
		}//run()終わり
		
		//他のみんなにメッセージ送るメソッド
	    public void every_send(String message) {	
			try {
				for(int i=0;i<socketlist.size();i++) {//他のみんなにおくる
					Socket soc = socketlist.get(i);
					BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
					
						bw1.write(message);
						bw1.flush();//物理デバイスに書き込み		
					
				}
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
				
	}//ChatClientクラス終わり
		
		

		
}




