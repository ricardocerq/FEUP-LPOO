package network;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class Communicator {
	
	//public enum ConnectionState{CONNECTING, CONNECTED, REACHED};
	
	
	private static final int KEEP_ALIVE_TIME = 2000;
	public enum ConnectionState{SERVER_MAIN_ESTABLISHING, SERVER_MAIN_ESTABLISHED, CONNECTING, CONNECTED, CLIENT_REACHED};
	
	private ConnectionState state = ConnectionState.CONNECTING;
	//private String serverAddress = "172.30.50.1";
	//private int serverPublicPort = 4445;
	private int serverPrivatePort;
	private ServerSocket serverSocket= null;
	private Socket socket = null;
	private Thread runner;
	Receiver receiver;
	Transmitter transmitter;
	boolean mainComm = true;
	int index;
	private KeepAlive keep= new KeepAlive(System.currentTimeMillis());
	AtomicBoolean b = new AtomicBoolean();
	
	public Communicator(int index, boolean mainComm){
		if(!(mainComm && CommunicationManager.getIsServer())){
			receiver = new Receiver(Communicator.this);
			transmitter = new Transmitter(Communicator.this);
		}
		this.mainComm = mainComm;
		this.index = index;
		resetState();
	}
	public Communicator(Socket s, int index, boolean mainComm){
		this(index, mainComm);
		this.socket = s;
	}
	
	public String toString(){
		//return "Communicator: index: "+ index + ", mainComm" + mainComm +  ", privatePort: " + serverPrivatePort;
		return "Comm: index: "+ index + ", privatePort: " + serverPrivatePort + ", state " + state;
	}
	
	public void createServerSocket(int port){
		try {
			System.out.println("Creating Server Socket at port" + port + ", "  + Communicator.this.toString());
			serverSocket = new ServerSocket();
			serverSocket.setPerformancePreferences(1, 2, 1);
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(CommunicationManager.getInstance().getServerAddress(), port));
			//serverSocket = new ServerSocket(port);
			System.out.println("Server Socket successful, " + Communicator.this.toString());
		} catch (IOException e) {
			System.out.println("Server Socket failed, " + Communicator.this.toString());
			e.printStackTrace();
		}
		
	}
	
	public void serverMainCommSetup(){
		//if(state == ConnectionState.SERVER_MAIN_ESTABLISHING)
			createServerSocket(CommunicationManager.getInstance().getServerPublicPort());
	}
	public void serverSecCommSetup(){
		state = ConnectionState.CONNECTED;
	}
	
	public boolean clientSetup(){
		try {
			System.out.println("Creating Socket, " + Communicator.this.toString());
			System.out.println("reached, using "+CommunicationManager.getInstance().getServerAddress()+ " and " + CommunicationManager.getInstance().getServerPublicPort() +", " + Communicator.this.toString());
			socket = new Socket(CommunicationManager.getInstance().getServerAddress(), CommunicationManager.getInstance().getServerPublicPort());
			System.out.println("Done creating Socket, " + Communicator.this.toString());
		} catch (IOException e) {
			System.out.println("Failure creating Socket, " + Communicator.this.toString());
			return false;
		}
		return true;
	}
	
	
	
	public void open(){
		runner = new Thread(){
			public void run(){
				System.out.println("Entering Communicator.open(), " + Communicator.this.toString());
				if(state ==  ConnectionState.CONNECTED){
					System.out.println("failed Connected check, " + Communicator.this.toString());
					return;}
				else System.out.println("passed Connected check, " + Communicator.this.toString());
				if(CommunicationManager.getIsServer()){
					if(mainComm){
						serverMainCommSetup();
					}
					else{
						serverSecCommSetup();
					}
				}
				else{
					if(!clientSetup()){
						state =  ConnectionState.CONNECTING;
						synchronized(this){try {
							this.wait(1000);
							open();
						} catch (InterruptedException e) {
						}}
						return;
					}
						
				}
				if(socket != null)
					try {
						socket.setTcpNoDelay(true);
					} catch (SocketException e) {
						e.printStackTrace();
					}
				if(receiver != null && transmitter != null && (!mainComm || !CommunicationManager.getIsServer()) ){
					System.out.println("Opening receiver, " + Communicator.this.toString());
					receiver.open();
					System.out.println("Done Opening receiver, " + Communicator.this.toString());
					System.out.println("Opening transmitter, " + Communicator.this.toString());
					transmitter.open();
					System.out.println("Done Opening transmitter, " + Communicator.this.toString());
				}
				if(CommunicationManager.getIsServer()){
					if(mainComm){
						state = ConnectionState.SERVER_MAIN_ESTABLISHED;
					}
					else 
						state = ConnectionState.CONNECTED;
				}
				else {
					state = ConnectionState.CONNECTED;
				}
				commLoop();
				System.out.println("Leaving Communicator.open(), " + Communicator.this.toString());
			}
			public void commLoop(){
				if(mainComm && CommunicationManager.getIsServer())
					acceptSockets();
				else{
					while(!interrupted())
						synchronized(this){
							try {
									this.wait(KEEP_ALIVE_TIME);
									keep = new KeepAlive(0);
									keep.update();
									if(Math.abs(keep.time - receiver.getLastReceived()) > KEEP_ALIVE_TIME * 4){
										Communicator.this.fail();
										break;
									}
									else transmitter.transmit(keep);
									
							} catch (InterruptedException e) {
									System.out.println("wait stopped");
							}
						}
				}
			}
			public void acceptSockets(){
				while(!interrupted()){
					try {
						System.out.println("Accepting Socket, " + Communicator.this.toString());
						socket = serverSocket.accept();
						System.out.println("Socket Accepted, " + Communicator.this.toString());
						CommunicationManager.getInstance().addComm(socket);
					} catch (IOException e) {
						System.out.println("Socket Acceptance failed, " + Communicator.this.toString());
						e.printStackTrace();
					}
				}
			}
			
		
		};
		if (b.compareAndSet(false, true)) {
			runner.start();
		    b.set(false);
		}
		
	}
public void close() throws IOException{
	if(serverSocket!= null || socket != null)
		runner.interrupt();
	if(transmitter != null && receiver != null){
	transmitter.close();
	receiver.close();
	}
	System.out.println("closing sockets, "+ Communicator.this.toString());
	if(serverSocket != null)
		serverSocket.close();
	if(socket != null)
		socket.close();
	System.out.println("sockets closed, "+ Communicator.this.toString());
}
	
public void resetState(){
	if(CommunicationManager.getIsServer()&&mainComm)
			state = ConnectionState.SERVER_MAIN_ESTABLISHING;
	else 
		state = ConnectionState.CONNECTING;
}
	
	public boolean transmit(Serializable object){
		return transmitter.transmit(object);
	}
	public Serializable receive(){
		return receiver.pop();
	}
	public List<Serializable> receiveAll(){
		return receiver.popAll();
	}
	
	public Socket getSocket(){
		return socket;
	}
	public void clearTransmission() {
		transmitter.clearQueue();
	}
	public void fail() {
		System.out.println("Fail() called, " + Communicator.this.toString());
		if(state == ConnectionState.CONNECTED)
		try {
			this.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(mainComm){state = ConnectionState.CONNECTING; this.open();}
			//{if(CommunicationManager.getIsServer()|| state == ConnectionState.CONNECTED) this.open();}
		else CommunicationManager.getInstance().removeComm(this);
	}
}
