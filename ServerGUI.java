package chattingGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ServerGUI extends JFrame implements ActionListener{
	private static ServerSocket server;
	private static Socket socket;
	private static int port=7778;
	private JList list;
	private HashMap<String, DataOutputStream> hm;
	
	private JPanel contentPane;
	private DefaultListModel<String> dlm;
	private int deleteSel;
	private String key;
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ServerGUI frame = new ServerGUI();
		frame.setVisible(true);
//		frame.dlm.addElement("is waiting...");
					try {
					frame.hm = new HashMap<>();
					server = new ServerSocket(port);
					while(true){
						/** client 에서 접속 
						 *  client 에서 처음에  보내는 걸로 자기 아이디 
						 */
						
					socket = server.accept();
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					
				    String name = dis.readUTF();
					
				    synchronized(frame.hm){
				    frame.hm.put(name, dos);
				    }
				    frame.dlm.clear();
					for(String names : frame.hm.keySet()){
						frame.dlm.addElement(names); 
					}
					
					ChatServerThread thread = new ChatServerThread(name,socket, frame.hm,frame.dlm);
					thread.start();
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	/**
	 * Create the frame.
	 */
	public ServerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 381, 577);
		setTitle("Seohyun's Talk");
		Toolkit kit = Toolkit.getDefaultToolkit();
		java.awt.Image img = kit.getImage("mainIcon.png");
		setIconImage(img);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("접속자 명단");
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		
		list = new JList();
		scrollPane.setViewportView(list);
		dlm = new DefaultListModel();
		list.setModel(dlm);
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//삭제할 줄의 인덱스를 deleteSel에 저장한다.
				//강퇴를 누르면 그 줄의 element를 제거한다.
				deleteSel = list.getSelectedIndex();
				System.out.println("list에서 선택된 index: "+deleteSel);
				
				if(deleteSel >=0){
				key = (String)dlm.getElementAt(deleteSel);
				}
			}
		});
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("강퇴");
		panel_2.add(btnNewButton);
		btnNewButton.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		System.out.println("받은 액션리스너: "+ob);
		 if(ob instanceof JButton){
			System.out.println("저장된 deleteSel: "+deleteSel);
			try {
				this.hm.get(key).close(); //연결 끊기
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.hm.remove(key); //hashmap 에서 삭제
			dlm.remove(deleteSel); //list에서 제거
			deleteSel = 0;
		}
	}

}
