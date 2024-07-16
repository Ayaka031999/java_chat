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
	
	private JLabel jl_1 = new JLabel("サーバー");
	private JLabel jl_2 = new JLabel("ハンドル名");
	private JLabel jl_3 = new JLabel("参加者");
	private JLabel jl_4 = new JLabel("メッセージ");
	
	private JTextField jt_1 = new JTextField();
	private JTextField jt_2 = new JTextField();
	private JTextField jt_3 = new JTextField();
	
	private JButton btn1 = new JButton("入室");
	private JButton btn2 = new JButton("退室");
	private JButton btn3 = new JButton("送信");
	
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> jlist = new JList<String>(listModel);
	
	private JTextArea jt_area = new JTextArea();
		
	private JScrollPane scrollpane1 = new JScrollPane(jlist,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JScrollPane scrollpane2 = new JScrollPane(jt_area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JScrollBar vsBar = scrollpane1.getVerticalScrollBar();
	
	private Socket socket;
	private String text;


	//コンストラクタ開始
	public R05J14Panel(){
		
		 Dimension preferredSize = new Dimension(700,500);//ウィンドウサイズ設定
		 setPreferredSize(new Dimension(700, 555));
		 
		 setLayout(null);//レイアウトnull
		  
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
		 jlist.setFont(font);//JTextAreaのフォントを設定
	
		 scrollpane1.setBounds(25, 260, 230, 180);
		 add(scrollpane1);	
		
		 jt_area.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		 jt_area.setFont(font);//JTextAreaのフォントを設定
		 jt_area.setEditable(false);
	
		 scrollpane2.setBounds(311, 48, 362, 392);
		 add(scrollpane2);	
		 
		 btn2.setEnabled(false);
		 btn3.setEnabled(false);
		 jt_3.setEnabled(false);

		 
	   // 入室時処理
		 btn1.addActionListener(
		      new ActionListener(){
				@Override
			   	public void actionPerformed(ActionEvent ae){
					 btn2.setEnabled(true);//退室ボタン無効
					 btn3.setEnabled(true);//送信ボタン無効
					 jt_1.setEnabled(false);//サーバー入力無効
					 jt_2.setEnabled(false);//ハンドル名入力無効
					 jt_3.setEnabled(true);//送信メッセージ
					 btn1.setEnabled(false);//入室ボタン無効
					 

					 if(jt_1.getText() != null && jt_2.getText() != null) {//サーバーとハンドル名の両方が入力された状態で「入室」がクリックされると、
						                                                   //入力されたサーバーの 7777(TCP)番ポートに接続
						new Thread(() -> { 
							try {
								socket = new Socket(jt_1.getText(),7777);//接続要求
								
								BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
								
								/***connectを送る***/
								bw.write("connect"+" "+jt_2.getText()+"\r\n");//流しそうめん送れた
								bw.flush();//物理デバイスに書き込み
								
								System.out.println("クライアント　こねくと　おくった！");
									
								receiveMessage();
									
							} catch (Exception e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							} 
						 }).start();	
					 }					
				}					   	
		      }
		 );
 
		 
		 //退室時処理
		 btn2.addActionListener(
		      new ActionListener(){
				@Override
			   	public void actionPerformed(ActionEvent ae){
					
					 new Thread(() -> { 		
						try{
							
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
							
							/***disconnectを送る***/
							bw.write("disconnect"+"\r\n");//退室メッセージを送る
							bw.flush();//物理デバイスに書き込み
							
						}catch (Exception e) {
							e.printStackTrace();
						}
					 }).start();	
				}					   	
		      }
		 );
		 
		 
		 //送信時処理 
		 btn3.addActionListener(
			      new ActionListener(){
					@Override
				   	public void actionPerformed(ActionEvent ae){
						new Thread(() -> { 
							try {								
								BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));								
								/***sendMessageを送る***/
								bw.write("sendMessage"+" "+jt_3.getText()+"\r\n");//流しそうめん送れた
								bw.flush();//物理デバイスに書き込み													
							} catch (Exception e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
								jt_area.append("Error>> 通信エラー"+"\n");
								jt_area.setCaretPosition(jt_area.getText().length());
								vsBar.setValue(vsBar.getMaximum());
							}
						}).start();							
					}					   	
			      }
		  );
		 
	}//コンストラクタ終わり
	
	
	public void receiveMessage(){
		
		try {			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while(!socket.isClosed()){ //ソケットが閉じるまで
				
				text = br.readLine();
				
				System.out.print("****"+text+"****\n");
					
				String[] array = text.split(" ");
				
				switch(array[0]) {
					case "connect":
						jt_area.append(array[1]+"\n");						
						BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						
						/***getUsersを送る***/
						bw2.write("getUsers \r\n");//流しそうめん送れた
						bw2.flush();//物理デバイスに書き込み
						break;						
					case "getUsers":
						//int nameindex = 0;
						SwingUtilities.invokeLater(() -> {
							listModel.clear();
							String[] items = text.split(" "); // 空白で分割して配列に格納
				            for (int i=1;i<items.length;i++) {			            	
				                  listModel.addElement(items[i]); // JListにアイテムを追加
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
							int index = listModel.indexOf(array[1]); // 要素のインデックスを取得
					        if (index != -1) {
					            listModel.removeElementAt(index); // 要素を削除
					           // jt_area.append("要素が削除されました。");
					        } else {
					           // jt_area.append("要素が見つかりませんでした。");
					        }
						});
						break;
					case "purge":						
						jt_area.append("私は退室しました。"+"\n");
						listModel.clear();
						socket.close();	
						
						btn2.setEnabled(false);//退室ボタン無効
						btn3.setEnabled(false);//送信ボタン無効
						jt_1.setEnabled(true);//サーバー入力無効
						jt_2.setEnabled(true);//ハンドル名入力無効
						jt_3.setEnabled(false);//送信メッセージ
						btn1.setEnabled(true);//入室ボタン無効
						
						break;		
				}
			}
		}catch(IOException e){
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			jt_area.append("Error>> 通信エラー"+"\n");
		}
		
	}//receiveメソッド終わり
	
}//クラス終わり
