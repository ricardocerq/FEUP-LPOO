package network;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommunicationManager {
	private static CommunicationManager instance = null;
	private String serverAddress = "172.30.50.1";
	 int serverPublicPort = 4445;
	CopyOnWriteArrayList<ReceiverListener> receiverListeners = new CopyOnWriteArrayList<ReceiverListener>();
	private CopyOnWriteArrayList<ConnectedPlayer> players = new CopyOnWriteArrayList<ConnectedPlayer>();
	private CopyOnWriteArrayList<ConnectedPlayer> formerPlayers = new CopyOnWriteArrayList<ConnectedPlayer>();
	private Communicator mainComm;
	CopyOnWriteArrayList<Communicator> comms = new CopyOnWriteArrayList<Communicator>(); // empty if client
	private LinkedList<PlayerConnectionListener> playerConnectionListeners = new LinkedList<PlayerConnectionListener>();
	
	public void addPlayerConnectionListener(PlayerConnectionListener l){
		playerConnectionListeners.add(l);
	}
	
	public static boolean isServer;
	
	public void addReceiverListener(ReceiverListener lis){
		receiverListeners.add(lis);
	}
	
	public boolean onReceive(String ip, Serializable ser){
		int number= 0;
		for(ConnectedPlayer p : players){
			System.out.println("player" + p.getIP() + ", " + p.getNumber());
			if(p.verify(ip)){
				number = p.getNumber();
				break;
			}
		}
		if(number == 0)
			System.out.println("oops");
		for(ReceiverListener l : receiverListeners){
			if(l.onReceive(number, ser))
				return true;
		}
		return false;
	}
			
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	public int getServerPublicPort() {
		return serverPublicPort;
	}
	public void setServerPublicPort(int serverPublicPort) {
		this.serverPublicPort = serverPublicPort;
	}
	
	public static CommunicationManager getInstance(){
		if(instance == null)
			instance = new CommunicationManager();
		return instance;
	}
	
	public static void setThisIP() throws IOException{
		Socket s = new Socket("google.com", 80);
		String add = s.getLocalAddress().getHostAddress();
		getInstance().serverAddress = add;
		s.close();
	}
	
	public static void setIsServer(boolean server){
		isServer = server;
	}
	public static boolean getIsServer(){
		return isServer;
	}
	public void openAll() throws IOException{
		mainComm.open();
		for(Communicator comm : comms)
			comm.open();
	}
	public void closeAll() throws IOException{
		mainComm.close();
		mainComm.resetState();
		for(Communicator comm : comms){
			comm.close();
			comm.resetState();
		}
		comms.clear();
	}
	public synchronized void removeComm(Communicator comm){
		String ip =retrieveIP( comm.getSocket().getRemoteSocketAddress().toString());
		for(int i = 0; i < players.size();i++){
			if(players.get(i).verify(ip)){
				formerPlayers.add(players.get(i));
				players.remove(i);
				onPlayerChange();
				break;
			}
		}
		comms.remove(comm);
		for(int i = 0 ; i < comms.size(); i++){
			comms.get(i).index = i;
		}
	}
	
	private void onPlayerChange() {
		for(PlayerConnectionListener l : playerConnectionListeners){
			l.onPlayerChange();
		}
	}

	public synchronized boolean addComm(Socket s){
		String ip = retrieveIP(s.getRemoteSocketAddress().toString());
		for(int i = 0; i < comms.size(); i++){
			if(retrieveIP(comms.get(i).getSocket().getRemoteSocketAddress().toString()).equals(retrieveIP(s.getRemoteSocketAddress().toString()))){
				comms.remove(comms.get(i));
			}
		}
		if(comms.size() >= 4)
			return false;
		System.out.println("\n\n");
		for(ConnectedPlayer p : players){
			System.out.println("Player " + p.getNumber());
		}
		System.out.println("\n\n");
		/*for(ConnectedPlayer p : players){
			if(p.verify(ip))
				return false;
		}*/
		addPlayer(ip);
		Communicator com = new Communicator(s, comms.size(), false);
		com.open();
		comms.add(com);
		return true;
	}
	public void addPlayer(String playerIP){
		/*for(int i = 0; i < players.size(); i++){
			if(players.get(i).verify(playerIP)){
				players.remove(i);
				i--;
			}
		}*/
		int newNumber = firstAvailableNumber(playerIP);
		System.out.println("-->Number:" +  newNumber+ "<--");
		for(int i = 0; i < formerPlayers.size(); i++){
			if(formerPlayers.get(i).verify(playerIP)){
				if(!available(formerPlayers.get(i).getNumber()))
					formerPlayers.get(i).setNumber(newNumber);
				players.add(formerPlayers.get(i));
				onPlayerChange();
				formerPlayers.remove(i);
				return;
			}
		}
		ConnectedPlayer p = new ConnectedPlayer(playerIP, newNumber);
		players.add(p);
		onPlayerChange();
	}
	
	private boolean available(int number) {
		for(ConnectedPlayer p : players){
			if(p.getNumber() == number)
				return false;
		}
		return true;
	}
	public static String retrieveIP(String ip){
		return ip.split(":")[0];
	}
	public int firstAvailableNumber(String playerIP){
		List<Integer> possible = new LinkedList<Integer>(Arrays.asList(1,2,3,4));
		for(ConnectedPlayer p : players){
			System.out.println("ocupied: " + p.getNumber() + ", " + p.getIP() + " - " + playerIP);
			if(!p.verify(playerIP))
			possible.remove((Integer)p.getNumber());
		}
		return possible.get(0);
	}
	
	public CommunicationManager(){
		mainComm = new Communicator(-1,true);
	}
	private Communicator getComm(int i){
		if(i < 0)
			return mainComm;
		else return comms.get(i);
	}
	
	public boolean transmit(int i, Serializable object){
		if(i >= comms.size())
			return false;
		return getComm(i).transmit(object);
	}
	public Serializable receive(int i){
		if(i >= comms.size())
			return null;
		return  getComm(i).receive();
	}
	
	public Serializable receiveFromPlayer(int playerNumber){
		synchronized(players){
			for(int i = 0; i < players.size(); i++){
				if(players.get(i).getNumber() == playerNumber)
					return receive(i);
			}
		}
		return null;
	}
	
	public List<Serializable> receiveAll(int i){
		if(i >= comms.size())
			return new LinkedList<Serializable>();
		return  getComm(i).receiveAll();
	}
	
	public void info(){
		System.out.println("comms size : " + comms.size());
		if(comms.size() > 0){
			if(comms.get(0).receiver == null)
				System.out.println("comms.get(0).receiver is null");
			else if(comms.get(0).receiver.runner == null)
				System.out.println("comms.get(0).receiver.runner is null");
			else System.out.println(comms.get(0).receiver.runner.isAlive());
		}
	}
	public void clearReceiver(int i) {
		if(comms.size() > i )
		comms.get(i).receiver.clearQueue();
	}

	public int getIndex(Communicator comm) {
		return comms.indexOf(comm);
	}
	public String connectionInfo(){
		return serverAddress + "," + serverPublicPort; 
	}
	public void receiveConnectionInfo(String connectionInfo) throws IllegalArgumentException{
		String[] infos = connectionInfo.split(",");
		if(infos.length != 2)
			throw new IllegalArgumentException();
		serverAddress = infos[0];
		serverPublicPort = Integer.parseInt(infos[1]);
	}
	
	public List<Integer> getPlayers(){
		List<Integer> out = new LinkedList<Integer>();
		for(ConnectedPlayer p : players){
			out.add((Integer) p.getNumber());
		}
		return out;
	}
}
