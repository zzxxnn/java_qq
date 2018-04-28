package Test;
/***
 * 参考了尚学堂的聊天器源码，集合和迭代器貌似没学到
 * 
 * by ZXN
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class testkh extends JFrame {
	String string;

	JLabel jLabel;

	Socket ss;

	DataInputStream dis = null;

	DataOutputStream dos = null;

	JTextArea jTextArea;

	JTextField jTextField;

	MyPanel myPanel;

	// ----------------------------------------属性--------------------------------------------
	public testkh() {
		setSize(549, 530);
		setLocation(500, 500);
		setResizable(false);
		setDefaultCloseOperation(3);
		Init();
		setVisible(true);
	}

	// ---------------------------------图形界面------------------------------------------------
	private void Init() {
		// String string1 = "与"+jTextField.getText()+"聊天中";
		this.setTitle("hehe");
		setLayout(null);
		myPanel = new MyPanel("tupian/2.png");
		myPanel.setBounds(0, 0, 549, 504);
		myPanel.setLayout(null);
		// ---------------------------------------------------------------
		jTextArea = new JTextArea();

		JScrollPane jScrollPane = new JScrollPane(jTextArea);
		jScrollPane.setBounds(0, 63, 405, 289);
		myPanel.add(jScrollPane);
		// ----------------------------------------------------------------
		final JTextArea jTextArea1 = new JTextArea();
		jTextArea1.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					string += "                                                                               （本人）"
							+ new Date(System.currentTimeMillis())
									.toLocaleString() + "\n";

					String sSend = jTextArea1.getText();
					if (sSend.trim().length() == 0)
						return;
					try {
						testkh.this.send(sSend);
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					string += "                                                                      "
							+ sSend;
					jTextArea.setText(string);

					jTextArea1.setText("");
					try {
						AudioPlayer.player.start(new AudioStream(
								new FileInputStream("tupian/msg.wav")));
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					String str = jTextArea1.getText();
					while (str != null && str.length() != 0) {
						try {
							send(str);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						str = jTextArea1.getText();
					}
				}
			}
		});
		// jTextArea.setText(string);
		jTextArea1.setBounds(0, 378, 405, 97);
		myPanel.add(jTextArea1);
		// -----------------------------------------------------
		jLabel = new JLabel("等待连接");
		jLabel.setBackground(Color.yellow);
		jLabel.setForeground(Color.yellow);
		// jLabel.setFont(new Font("宋体", Font.BOLD, 13));
		jLabel.setBounds(275, 0, 60, 24);
		myPanel.add(jLabel);
		// -------------------------------------右侧图形----------------------------
		jTextField = new JTextField("127.0.0.1");
		jTextField.setBounds(405, 295, 138, 27);
		myPanel.add(jTextField);
		final JTextField jTextField1 = new JTextField("1234");
		jTextField1.setBounds(405, 325, 68, 27);
		myPanel.add(jTextField1);
		final JButton jButton = new JButton("连接");
		jButton.setBounds(475, 325, 68, 27);
		jButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				startclint(jTextField.getText(), jTextField1.getText());
				try {
					AudioPlayer.player.start(new AudioStream(
							new FileInputStream("tupian/system.wav")));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				setTitle("与 " + jTextField.getText() + " 聊天中");
				jButton.setEnabled(false);
			}

		});
		myPanel.add(jButton);
		JButton jButton2 = new JButton("发送(enter)");
		jButton2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				string += "                                                                               （本人）"
						+ new Date(System.currentTimeMillis()).toLocaleString()
						+ "\n";

				String sSend = jTextArea1.getText();
				if (sSend.trim().length() == 0)
					return;
				try {
					testkh.this.send(sSend);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				string += "                                                                      "
						+ sSend;
				jTextArea.setText(string);

				jTextArea1.setText("");
				try {
					AudioPlayer.player.start(new AudioStream(
							new FileInputStream("tupian/msg.wav")));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				String str = jTextArea1.getText();
				while (str != null && str.length() != 0) {
					try {
						send(str);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					str = jTextArea1.getText();
				}

			}
		});
		jButton2.setBounds(240, 477, 155, 22);
		add(jButton2);
		// JTextArea jTextArea3 = new JTextArea();
		// JScrollPane jScrollPane2 = new JScrollPane(jTextArea3);
		// jScrollPane2.setBounds(405, 63, 138, 233);
		// myPanel.add(jScrollPane2);
		add(myPanel);
	}

	// --------------------------------------------客户端-------------------------------------------
	private void startclint(String ip, String dk) {
		try {
			ss = new Socket(ip, Integer.parseInt(dk));
			(new Thread(new ReceiveThread())).start();
			jLabel.setText("连接成功");
		} catch (Exception e) {
			jLabel.setText("连接失败");
			e.printStackTrace();
			return;
		}

	}

	private void close() {
		try {
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void send(String string) throws IOException {
		DataOutputStream dos = new DataOutputStream(ss.getOutputStream());
		dos.writeUTF(string);
	}

	boolean isconnect = true;

	public void run() {
		getstream();
	}

	public void getstream() {
		try {
			dis = new DataInputStream(ss.getInputStream());
			dos = new DataOutputStream(ss.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendmessage(String string) {
		try {
			dos.writeUTF(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ReceiveThread implements Runnable {
		public void run() {
			if (ss == null)
				return;
			try {
				DataInputStream dis = new DataInputStream(ss.getInputStream());
				String str = dis.readUTF();
				while (str != null && str.length() != 0) {
					// System.out.println(str);
					string += "（其他人）"
							+ new Date(System.currentTimeMillis())
									.toLocaleString() + "\n";
					string += str;
					jTextArea.setText(string);
					str = dis.readUTF();
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// ---------------------------------------其他----------------------------------------
	public static void main(String[] args) {
		new testkh();
	}

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

}
