package chattingGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sun.glass.events.WindowEvent;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Image;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.SwingConstants;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.imageio.ImageIO;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;

public class ChatClientGUI extends JFrame implements ActionListener, WindowListener{
	
	static Socket socket;
	private String ip_addr;

	private int port;
	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static String sendData;
	boolean flag = false;
	
	private JPanel contentPane;
	private DefaultListModel dlm;
	private JTextField textField;
	private String name;
	private ChatClientGUI gui;
	private JLabel userName;
	private ChatClientGUI frame;
	private DefaultListModel dlm1;
	private JScrollPane scrollPane;
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		ChatClientGUI frame;
		frame = new ChatClientGUI();
		frame.port = 7778;
		GetName getName;
		try {
			getName = new GetName(frame);
		} catch (Exception e) {e.printStackTrace();}
		
		// 내 IP 203.233.194.251
		System.out.println("client 연결 요청");
		socket = new Socket(frame.getIp_addr(), frame.port);
		System.out.println("Chatting Server 연결됨.");
		dis =new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		ChatClientThreadGUI chatClientThread = new ChatClientThreadGUI(dis, frame);
		chatClientThread.start();
	}
				
	/**
	 * Create the frame.
	 */

	public ChatClientGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 427, 603);
		setTitle("Seohyun's Talk");
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		java.awt.Image img = kit.getImage("mainIcon.png");
		setIconImage(img);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("menu");
		mnNewMenu.setMnemonic('s');
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("help");
		mnNewMenu.add(mntmNewMenuItem_3);
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 누르면 도움말 텍스트 띄워주기
				JOptionPane.showMessageDialog(null, "귓속말 사용법\n /w 수신자이름 contents");
			}
		});
		
		JMenuItem mntmNewMenuItem = new JMenuItem("send file");
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("send image");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("search phrase");
		mnNewMenu.add(mntmNewMenuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 228, 225));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.textHighlight);
		panel.add(panel_1, BorderLayout.WEST);

		userName = new JLabel("UserName");
		panel.add(userName, BorderLayout.WEST);
		setDlm(new DefaultListModel());
		
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String str = dayTime.format(new Date(time));
		getDlm().addElement("-------------------------"+str+"-------------------------");

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(192, 192, 192));
		contentPane.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(18);

		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				//textfield에 뭔가 작성하고 나서 엔터 누르면 서버로 넘기게 
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					//data 보내기
					sendData = textField.getText();
					try {
						dos.writeUTF(sendData);
						textField.setText("");
					
					} catch (IOException ee) {ee.printStackTrace();}
				}
				
			}
		});
		
		JButton btnNewButton = new JButton("send");
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		panel_3.add(btnNewButton, BorderLayout.EAST);
		btnNewButton.addActionListener(this);
		btnNewButton.setActionCommand("send");
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(192, 192, 192));
		panel_2.add(panel_4, BorderLayout.WEST);
		
		JMenuBar menuBar_1 = new JMenuBar();
		panel_4.add(menuBar_1);
		
		JMenu mnNewMenu_1 = new JMenu("imoticon");
		menuBar_1.add(mnNewMenu_1);
		mnNewMenu_1.setIcon(new ImageIcon("C:\\workspace\\Thread\\이모티콘2.jpg"));
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("당황");
		mnNewMenu_1.add(mntmNewMenuItem_6);
		mntmNewMenuItem_6.setIcon(new ImageIcon("C:\\workspace\\Thread\\하은1.png"));
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("아직");
		mnNewMenu_1.add(mntmNewMenuItem_7);
		mntmNewMenuItem_7.setIcon(new ImageIcon("C:\\workspace\\Thread\\하은2.png"));
		
		JMenuItem menuItem = new JMenuItem("뭐냐");
		mnNewMenu_1.add(menuItem);
		menuItem.setIcon(new ImageIcon("C:\\workspace\\Thread\\doge1m.jpg"));
		
		JMenuItem menuItem_1 = new JMenuItem("으앙");
		mnNewMenu_1.add(menuItem_1);
		menuItem_1.setIcon(new ImageIcon("C:\\workspace\\Thread\\doge2m.png"));
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("안녕");
		mntmNewMenuItem_8.setIcon(new ImageIcon("C:\\workspace\\Thread\\코코안녕m.png"));
		mnNewMenu_1.add(mntmNewMenuItem_8);
		mntmNewMenuItem_8.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String imo = "/@ 안녕";
		        try {
					dos.writeUTF(imo);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("뀨우");
		mntmNewMenuItem_5.setIcon(new ImageIcon("C:\\workspace\\Thread\\코코뀨m.png"));
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String imo = "/@ 뀨우";
		        try {
					dos.writeUTF(imo);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		mnNewMenu_1.add(mntmNewMenuItem_5);
		menuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String imo = "/@ 으앙";
		        try {
					dos.writeUTF(imo);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String imo = "/@ 뭐냐";
		        try {
					dos.writeUTF(imo);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String imo = "/@ 아직";
	        try {
				dos.writeUTF(imo);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		});
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//해당 이미지 텍스트 필드에 보이게 하기 
				// 해당 이미지 text 로 바꿔서 server로 전송
				String imo = "/@ 당황";
		        try {
					dos.writeUTF(imo);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		JMenu mnNewMenu_2 = new JMenu("file");
		menuBar_1.add(mnNewMenu_2);
		mnNewMenu_2.setIcon(new ImageIcon("C:\\workspace\\Thread\\clip2.gif"));
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("image");
		mntmNewMenuItem_4.setIcon(new ImageIcon("C:\\Users\\SCMaster\\Desktop\\images\\image2.png"));
		mnNewMenu_2.add(mntmNewMenuItem_4);
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane, BorderLayout.WEST);
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(20);
		scrollPane.setViewportView(list);
		list.setModel(dlm);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1, BorderLayout.CENTER);
		
		JList list_1 = new JList();
		scrollPane_1.setViewportView(list_1);
		
		dlm1 = new DefaultListModel();
		
		list_1.setModel(dlm1);
		
		JLabel lblNewLabel_3 = new JLabel("그룹채팅");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(lblNewLabel_3);
		
		this.addWindowListener(new WindowAdapter() {
			 public void windowOpened( WindowEvent e ){
				 textField.requestFocus();
			 }
		});
		}
	
		public DefaultListModel getDlm1() {
			return dlm1;
		}
	
		public void setDlm1(DefaultListModel dlm1) {
			this.dlm1 = dlm1;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			e.getSource();
			System.out.println(e.getActionCommand());
			if(e.getActionCommand() == "send"){
					//data 보내기
					sendData = textField.getText();
					try {
						dos.writeUTF(sendData);
						textField.setText("");
					
					} catch (IOException ee) {ee.printStackTrace();}
			}
		}
	
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public JLabel getUserName() {
			return userName;
		}
	
		public void setUserName(String userName) {
			this.userName.setText("|user| "+userName);
		}
	
		public static DataOutputStream getDos() {
			return dos;
		}
	
		public static void setDos(DataOutputStream dos) {
			ChatClientGUI.dos = dos;
		}
		@Override
		public void windowActivated(java.awt.event.WindowEvent e) {
			textField.grabFocus();
		}
		@Override
		public void windowClosed(java.awt.event.WindowEvent e) {
		}
	
		@Override
		public void windowClosing(java.awt.event.WindowEvent e) {
		}
		@Override
		public void windowDeactivated(java.awt.event.WindowEvent e) {
		}
	
		@Override
		public void windowDeiconified(java.awt.event.WindowEvent e) {
		}
	
		@Override
		public void windowIconified(java.awt.event.WindowEvent e) {
		}
	
		@Override
		public void windowOpened(java.awt.event.WindowEvent e) {
			textField.grabFocus();
		}
	
		public static DataInputStream getDis() {
			return dis;
		}
	
		public static void setDis(DataInputStream dis) {
			ChatClientGUI.dis = dis;
		}
		public DefaultListModel getDlm() {
			return dlm;
		}
		public void setDlm(DefaultListModel dlm) {
			this.dlm = dlm;
		}
		public String getIp_addr() {
			return ip_addr;
		}
	
		public void setIp_addr(String ip_addr) {
			this.ip_addr = ip_addr;
		}
		public JScrollPane getScrollPane() {
			return scrollPane;
		}
}
