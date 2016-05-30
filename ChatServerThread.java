package chattingGUI;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.DefaultListModel;

import com.sun.jmx.snmp.Timestamp;

public class ChatServerThread extends Thread {
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	//ObjectOutputStream oos;
	String name;
	HashMap<String, DataOutputStream> hm;
	String msg; //broadcasting 할 메세지
	String cleNames = "/# ";
	DefaultListModel<String> dlm;
	//ArrayList<String> clientN ;
	//이 스레드는 한 명 접속할 때마다 생김 
	//한 명당 하나의 소켓 
	//해시맵은 하나만 존재 이름이랑 outputStream 가지고 있음. 
	//여기서 생긴 input Stream이 넘긴 socket을 통해 이 스레드와 연결된 클라이언트의 메세지를 읽어오는 것. 
	
	public ChatServerThread(String name, Socket socket, HashMap<String, DataOutputStream> hm, DefaultListModel<String> dlm) throws IOException {
		this.socket = socket;
		this.hm = hm;
		this.name = name;
		dis = new DataInputStream(socket.getInputStream());
		this.dlm = dlm;
	}
	
	public void run() {
		//data read
		System.out.println(name + "님 대화 시작");
		try {
			broadCasting("| "+name+" |님이 입장하셨습니다. ");
			System.out.println(nameParsing());
			broadCasting(nameParsing());
		} catch (IOException e1) {
			
			hm.remove(name);
			dlm.clear();
			for(String names : hm.keySet()){
				dlm.addElement(names); 
			}
			e1.printStackTrace();
		}
		while(true){
		try {
			//oos.writeObject(clientN);
			msg = dis.readUTF();
			//접속자 명단 전송
			if(msg.length()>=3 &&msg.substring(0, 2).equals("/@")){ 
				broadCasting(msg+" "+name+" : " );
			//귓속말 전송
			}else if(msg.length()>=3 &&msg.substring(0, 2).equals("/w")){
				broadCasting(msg+curTime()+" "+name);
			}else{
			System.out.println(name+" : "+ msg);
			msg = name +" : "+ msg;
			broadCasting( msg +" "+curTime());
			}
		}catch(SocketException se){
			try {
				//TODO: list에서 제거하기 
				socket.close();
				hm.remove(name);
				dlm.clear();
				for(String names : hm.keySet()){
					dlm.addElement(names); 
				}
				broadCasting("| "+name+ "님이 퇴장하셨습니다. |");
				break;
				
				
			} catch (IOException e) {
				hm.remove(name);
				dlm.clear();
				for(String names : hm.keySet()){
					dlm.addElement(names); 
				}
				e.printStackTrace();
			}
		} 
		catch (IOException e) {
			hm.remove(name);
			dlm.clear();
			for(String names : hm.keySet()){
				dlm.addElement(names); 
			}
			e.printStackTrace();
		}
		
	
		}
		System.out.println("| "+name+ "님이 퇴장하셨습니다. |");
		
	}
	//시간 출력
	public String curTime(){
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("|hh:mm:ss|");
		String str = dayTime.format(new Date(time));
		return str;
		
	}
	public void broadCasting(String message) throws IOException {
		//hashmap 전체를 뒤져서 client에서 온 말을 모든 사람들에게 write 해주는 역할
		//value가 몇갠지 ?
		
		for(DataOutputStream ddd : hm.values() ){
			ddd.writeUTF(message);
		
		}
	}
	public String nameParsing(){
		/* hm. get keys 해서 이름들을 하나씩 보내주기
		 * 
		 */
		this.cleNames = "/# ";
		for(String names :hm.keySet()){
			this.cleNames += names+" ";
		}
			System.out.println(cleNames);
		
		
		return cleNames;
	
	}
}
		
		
	

