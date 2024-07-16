package j315.r05j.r05j14;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class R05J14 extends JFrame {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run(){
					R05J14 frame = new R05J14("実習問題14:Chat Client");//タイトル名引数でウィンドウつくる					
				}
			}
		);
	}
	

	private R05J14Panel 	panel = new R05J14Panel();//GUI部品の乗ったパネルつける
	private JPanel      	contentPane;
	
	private JMenuBar jm_bar = new JMenuBar();
	private JMenu jm = new JMenu("ファイル");
    private JMenuItem jm_i = new JMenuItem("終了");
    
    R05J14(String title){
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
