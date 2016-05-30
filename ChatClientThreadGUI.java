package chattingGUI;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollBar;

import javafx.scene.control.ScrollBar;

public class ChatClientThreadGUI extends Thread {
	private DataInputStream dis;
	private String receviedData;
	private ChatClientGUI frame;
	private ArrayList<String> clientList;
	private boolean finish =false;
	
//	private ArrayList<String> clientL;
//  private ObjectInputStream ois;
//	private String clientsnames;
	
	public ChatClientThreadGUI( DataInputStream dis, ChatClientGUI frame) {
		this.dis = dis;
		this.frame = frame;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		try {
		
		String check =	dis.readUTF();
		System.out.println(check);
		frame.getDlm().addElement(check);
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		while(!finish){
			try {
				//clientL = (ArrayList<String>) ois.readObject(); 
				receviedData = dis.readUTF();
			} catch(EOFException er){
				try {
					dis.close();
					finish = true;
					System.exit(0);
					break;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
			catch ( IOException e) {
				e.printStackTrace();
				try {
					dis.close();
					finish = true;
					System.exit(0);
					break;
				} catch (IOException e1) {
					
					finish = true;
					System.exit(0);
					e1.printStackTrace();
				}
			} 
			//앞에 두 글자만 검사.
			String ex =receviedData.substring(0, 2);
			String name = receviedData.substring(3, receviedData.length());
			String names = receviedData.substring(6, receviedData.length());
			String wispering = receviedData.substring(3, receviedData.length());
			System.out.println("names: "+names);
			
			if(ex.equals("/#")){
				System.out.println("ex: "+ex);
					System.out.println("names: "+name);
				dilimiter(name);
			}else if(ex.equals("/@")){
				System.out.println(receviedData);
			String icon =	receviedData.substring(0, 5);
			System.out.println(icon);
			switch(icon){
			case "/@ 당황" :
				if(new File("당황.png").exists()){
		        ImageIcon icono = new ImageIcon("당황.png");
		        frame.getDlm().addElement(names);
				frame.getDlm().addElement(icono); 
				JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
				scroll.setValue(scroll.getMaximum()+1);}
				break;
			case "/@ 아직":
				if(new File("아직.jpg").exists()){
				ImageIcon icono1 = new ImageIcon("아직.jpg");
				frame.getDlm().addElement(names);
				frame.getDlm().addElement(icono1); 
				JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
				scroll.setValue(scroll.getMaximum()+1);}
				break;
			case "/@ 뭐냐":
				if(new File("doge1.jpg").exists()){
				ImageIcon icono2 = new ImageIcon("doge1.jpg");
				frame.getDlm().addElement(names);
				frame.getDlm().addElement(icono2); 
				JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
				scroll.setValue(scroll.getMaximum()+1);}
				break;
			case "/@ 으앙":
				if(new File("doge2.png").exists()){
					ImageIcon icono3 = new ImageIcon("doge2.png");
					frame.getDlm().addElement(names);
					frame.getDlm().addElement(icono3); 
					JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
					scroll.setValue(scroll.getMaximum()+1);}
				break;
			case "/@ 안녕":
				if(new File("코코안녕.png").exists()){
					ImageIcon icono3 = new ImageIcon("코코안녕.png");
					frame.getDlm().addElement(names);
					frame.getDlm().addElement(icono3); 
					JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
					scroll.setValue(scroll.getMaximum()+1);}
				break;
			case "/@ 뀨우":
				if(new File("코코뀨.png").exists()){
					ImageIcon icono3 = new ImageIcon("코코뀨.png");
					frame.getDlm().addElement(names);
					frame.getDlm().addElement(icono3); 
					JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
					scroll.setValue(scroll.getMaximum()+1);}
				break;
			default :
				break;
			}
			//귓속말 받은 거 출력하기 
			}else if(ex.equals("/w")){
				// /w 수신자name contents 송신자name
				String receiver = dilimiter2(wispering);
				//receiver == 수신자 name
				String sender = dilimiter3(wispering);
				System.out.println(receiver);
				//수신자 이름이 내 이름이랑 같거나 송신자 이름이 내 이름이랑 같으면 
				String myName = frame.getName();
				if(receiver.equals(myName)||sender.equals(myName)){
				String wmsg = dilimiter1(wispering);
				// wmsg == contents
				frame.getDlm().addElement("| 귓말 | "+wmsg);
				JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
				scroll.setValue(scroll.getMaximum()+1);
				}
			}
			else{
				frame.getDlm().addElement(receviedData); 
				//TODO 변수화
				JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
				scroll.setValue(scroll.getMaximum()+1);
			}}
		frame.setVisible(false);
		System.out.println("종료");
	}
	//대화자 명단 출력용 dilimiter
	public void dilimiter(String msg){
		String[] parts = msg.split("\\s+");
		frame.getDlm1().clear();
		for (String part : parts) {
			frame.getDlm1().addElement(part);
		}
		JScrollBar scroll=frame.getScrollPane().getVerticalScrollBar();
		scroll.setValue(scroll.getMaximum()+1);
	}
	//귓속말 대화 출력용 dilimiter
	public String dilimiter1(String msg){
		String wmsg ="";
		String[] parts = msg.split("\\s+");
		wmsg += parts[parts.length-1]+" : ";
		System.out.println(wmsg);
		for (int i = 1; i < parts.length -1 ; i++) {
			wmsg += parts[i]+" ";
		}
		return wmsg;
	}
	public String dilimiter2(String msg){
		String myname="";
		String[] parts = msg.split("\\s+");
		myname = parts[0];
		return myname;
	}
	public String dilimiter3(String msg){
		String sender = "";
		String[] parts = msg.split("\\s+");
		sender = parts[parts.length-1];
		return sender;
	}
}
