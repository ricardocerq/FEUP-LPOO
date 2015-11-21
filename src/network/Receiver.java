package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import logic.Vec2;

public class Receiver{
	private Communicator comm;
	LinkedBlockingQueue<Serializable> queue = new LinkedBlockingQueue<Serializable>();
	Thread runner;
	private ObjectInputStream iStream;
	private long lastReceived = System.currentTimeMillis();
	
	public long getLastReceived() {
		return lastReceived;
	}

	public Receiver(Communicator comm){
		this.comm = comm;
	}
	
	public void open(){
		runner = new Thread(){
			public void run(){
				lastReceived = System.currentTimeMillis();
				try {
					iStream = new ObjectInputStream(comm.getSocket().getInputStream());
				} catch (IOException e1) {
					//e1.printStackTrace();
					return;
				}
				while(!this.isInterrupted()){
					try {
						//if(comm.index != -1)
							//System.out.println("Non main receiving");
						//System.out.println("receiving..." + comm.toString());
						Serializable message = (Serializable) iStream.readObject();
						//System.out.println("...received" + comm.toString());
						if(message== null)
							continue;
						if(message instanceof KeepAlive){}
						else {
							if(!CommunicationManager.getInstance().onReceive(CommunicationManager.retrieveIP(comm.getSocket().getRemoteSocketAddress().toString()), message))
								queue.add(message);
						}
						
						lastReceived = System.currentTimeMillis();
						if(message instanceof Vec2){
							System.out.println("index " + comm.index+ "Vec2: " + ((Vec2)message).getX() + ", " + ((Vec2)message).getY());
						}
					} catch (IOException e) {
						//comm.fail();
						//this.interrupt();
						//comm.reconnect();
						//e.printStackTrace();
						break;
					} catch (ClassNotFoundException e) {
					}
				}
				if(comm.index != -1)
					System.out.println("Non main receiving ended");
			}
		};
		runner.start();
	}
	public void close(){
		if(runner != null && runner.isAlive())
			runner.interrupt();
	}

	public Serializable take() throws InterruptedException {
		return queue.take();
	}
	public Serializable pop() {
		if(queue.isEmpty())
			return null;
		else return queue.remove();
	}
	public Serializable peek() {
		if(queue.isEmpty())
			return null;
		else return queue.peek();
	}
	public void clearQueue(){
		queue.clear();
	}

	public List<Serializable> popAll() {
		List<Serializable> out = new LinkedList<Serializable>();
		out.addAll(queue);
		queue.clear();
		return out;
	}
}
