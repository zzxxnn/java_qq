package Test;
/**
 * 
 * by ZXN
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class testfw extends JFrame {

	JTextField jTextField1;

	JButton jButton,jButton2;

	Socket sss;

	boolean aa = true;

	ServerSocket ss;
	
	int num = 0;
	
	JLabel jLabe1;
	
	Collection cClient = new ArrayList();
	
	String string;
	
	JLabel jLabe;
	
	MyPanel panel;

	// -----------------------------------属性------------------------------------
	 public testfw() {
		setSize(386, 293);
		setAlwaysOnTop(true);
		setLocation(500, 500);
		setResizable(false);
		Init();

		setVisible(true);
		setDefaultCloseOperation(3);
		
	}

	// -------------------------------------------图形界面------------------------
	private void Init() {
		setLayout(null);
		panel = new MyPanel("tupian/1.png");
		panel.setBounds(1, 1, 396, 303);
		panel.setLayout(null);
		jLabe = new JLabel("呵呵");
		jLabe.setBounds(279, 174, 100, 27);
		panel.add(jLabe);
		jLabe1 = new JLabel("等待客户连接");
		jLabe1.setForeground(Color.WHITE);
		jLabe1.setBounds(0, 0, 396, 80);
		panel.add(jLabe1);
		
		jTextField1 = new JTextField("1234");
		jTextField1.setBounds(112, 147, 167, 27);
		panel.add(jTextField1);
		jButton = new JButton("开启服务器");
		jButton.setBounds(113, 220, 158, 34);
		jButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						startserver();
					}
				}).start();
				
			}
		});
		panel.add(jButton);
//		jButton2 = new JButton("开启服务器");
//		jButton2.setBounds(113, 220, 158, 34);
//		jButton2.addActionListener(new ActionListener() {
//			
//			public void actionPerformed(ActionEvent e) {
//				
//				
//			}
//		});
//		panel.add(jButton2);
		
		

		add(panel);
		
	}

	// ------------------------------------------服务器---------------------------------------
	private void startserver() {
		try {
			ss = new ServerSocket(Integer.parseInt(jTextField1.getText()));
			jButton.setText("服务器创建成功");
			jButton.setEnabled(false);
			while (aa) {
				sss = ss.accept();
				cClient.add( new ClientConn(sss) );
				num = cClient.size();
				jLabe1.setText("当前连接人数"+num);
//				panel.repaint();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "创建服务器失败");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "创建服务器失败");
			return;
		}
	}

	
	class ClientConn implements Runnable
	{
		Socket s = null;
		public ClientConn(Socket s)
		{
			this.s = s;
			(new Thread(this)).start();
		}
		
		public void send(String str) throws IOException
		{
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(str);
		}
		
		public void dispose()
		{
			try {
				if (s != null) s.close();
				cClient.remove(this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			try {
				
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String str = dis.readUTF();
				while(str != null && str.length() !=0)
				{
					System.out.println(str);
					jLabe.setText("有人说:"+str);
					repaint();
					for(Iterator it = cClient.iterator(); it.hasNext(); )
					{
						ClientConn cc = (ClientConn)it.next();
						if(this != cc)
						{
							cc.send(str);
						}
					}
					str = dis.readUTF();
					//send(str);
				}
				this.dispose();
			} 
			catch (Exception e) 
			{
				
				num--;
				jLabe1.setText("当前连接人数"+num);
				this.dispose();
			}
			
		}
	}

	// ----------------------------------------------其他---------------------------------------
	public class MyPanel extends JPanel {
		private Image image = null;

		public MyPanel(String imagePath) {
			this.setOpaque(false);
			image = new ImageIcon(imagePath).getImage();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, null);
		}
	}

	public static void main(String[] args) {
		testfw aLoginframe = new testfw();
	}

}