package chattingGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.awt.Color;

public class GetName extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField nameField;
	private String name;
	private boolean start = true;
	private ChatClientGUI gui;
	private JTextField ipAd;
	
	public String getName(){
		return name;
	}
	
	public GetName(ChatClientGUI gui) {
		this.gui=gui;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 412, 172);
		setTitle("Seohyun's Talk");
		Toolkit kit = Toolkit.getDefaultToolkit();
		java.awt.Image img = kit.getImage("mainIcon.png");
		setIconImage(img);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.PINK);
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("접속할 서버의 IP와 사용할 ID 를 입력하세요.");
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaption);
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		
		JLabel lblNewLabel_1 = new JLabel("ID :  ");
		panel_2.add(lblNewLabel_1);
		
		nameField = new JTextField();
		panel_2.add(nameField);
		nameField.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.NORTH);
		
		JLabel lblNewLabel_2 = new JLabel("IP : ");
		panel_3.add(lblNewLabel_2);
		
		ipAd = new JTextField();
		panel_3.add(ipAd);
		ipAd.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.SOUTH);
		JButton btnNewButton = new JButton("등록");
		panel_4.add(btnNewButton);
		btnNewButton.addActionListener( new ActionListener() {
			/**
			 * 1) Client 키면 로그인 하라고 이름 입력!  enter 누르거나 클릭하면  
			 *	- 창 사라지고 
			 *	- 입력된 ip, 이름 옮겨 주고
			 *	- 채팅창 visible true로 바꿔주기
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				name= nameField.getText();
				String ip_Ad =""; 
				ip_Ad =	ipAd.getText(); 
				System.out.println(ip_Ad);
				System.out.println(name);
				setVisible();
				gui.setIp_addr(ip_Ad);
				gui.setUserName(name);
				gui.setVisible(true);
				try {
					gui.getDos().writeUTF(name);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setDefaultCloseOperation(HIDE_ON_CLOSE);
			}
		});
		nameField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					 name= nameField.getText();
						String ip_Ad =""; 
						ip_Ad =	ipAd.getText(); 
						System.out.println(ip_Ad);	
						System.out.println(name);
						setVisible();
						gui.setIp_addr(ip_Ad);
						gui.setUserName(name);
						gui.setName(name);
						gui.setVisible(true);
						try {
							gui.getDos().writeUTF(name);
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						setDefaultCloseOperation(HIDE_ON_CLOSE);
				 }
			}
		});
		
		this.setVisible(start);
	}
	
	/*setVisible을 바꿔줘야 하는데 저기 들어가는 변수 start를 바꾸고 나서 
	 * set Visible 
	 * 
	 */
	public void setVisible() {
		this.setVisible(false);
		
	}
	public void setStart(boolean start) {
		this.start = start;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
