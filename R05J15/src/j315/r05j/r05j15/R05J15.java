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
		// TODO 自動生成されたメソッド・スタブ
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run(){
					R05J15 frame = new R05J15("実習問題15:Chat Server");//タイトル名引数でウィンドウつくる
					
				}
			}
		);
	}
	
	
	private R05J15Panel 			panel = new R05J15Panel();//GUI部品の乗ったパネルつける
	private JPanel      			contentPane;
	
	private JMenuBar jm_bar = new JMenuBar();
	private JMenu jm = new JMenu("ファイル");
    private JMenuItem jm_i = new JMenuItem("終了");
    
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
							"プログラムを終了しますか？",
							"終了確認",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
						);			
			
						if( retVal == JOptionPane.YES_OPTION ){
							System.exit(0); 
						}
					}
				}
		);
		
		contentPane = (JPanel)getContentPane();//ウィンドウのコンテンツペーンを取得
		Dimension panelPreferredSize = panel.getPreferredSize();//パネルの推奨サイズを取得

		//ウィンドウのコンテンツペーンの推奨サイズをパネルのそれと同じ値に設定
		contentPane.setPreferredSize(panelPreferredSize);
		pack();//ウィンドウのサイズをサブコンポーネントに合わせる

		//画面の中央に（基準となるコンポーネントをnullにした場合、JFrameは画面中央に配置される）
		setLocationRelativeTo(null);
		setContentPane(panel);//コンテンツペーンにパネル1を追加
				
		setResizable(false);//ウィンドウのサイズ変更不可
		
		this.setVisible(true);//このウィンドウ表示
		
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



