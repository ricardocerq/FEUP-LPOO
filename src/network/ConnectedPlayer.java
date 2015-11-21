package network;

public class ConnectedPlayer {
	private String ip;
	private int number;
	
	public ConnectedPlayer(String s, int number){
		this.ip = s;
		this.number = number;
	}
	
	public void setNumber(int index){
		this.number = index;
	}
	
	public String getIP(){
		return ip;
	}
	public int getNumber(){
		return number;
	}
	public boolean verify(String s){
		return ip.equals(s);
	}
}
