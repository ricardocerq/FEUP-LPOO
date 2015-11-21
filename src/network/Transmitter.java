package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

import logic.Vec2;

public class Transmitter{
	
	private Communicator comm;
	LinkedBlockingQueue<Serializable> queue = new LinkedBlockingQueue<Serializable>();
	private ObjectOutputStream oStream;
	private Thread runner;
	
	Transmitter(Communicator comm){
		this.comm = comm;
	}
	
	public void open(){
		runner = new Thread(){
			@Override
			public void run(){
				try {
					oStream = new ObjectOutputStream(comm.getSocket().getOutputStream());
					oStream.flush();
				} catch (IOException e1) {
					
					//e1.printStackTrace();
					return;
				} 
				while(!this.isInterrupted()){
					try {
						//System.out.println("transmitting... " + comm.toString());
						Serializable object = queue.take();
						if(object == null)
							continue;
						//if(object instanceof KeepAlive)
							//System.out.println("transmitting keepalive");
						//System.out.println("...transmitted " + comm.toString());
						
						oStream.writeObject(object);
						oStream.reset();
						if(object instanceof Vec2){
							System.out.println("index " + comm.index + "Vec2: " + ((Vec2)object).getX() + ", " + ((Vec2)object).getY());
						}
					} catch (InterruptedException e) {
					} catch (IOException e) {
						//e.printStackTrace();
						break;
					}
				}
			}
		};
		runner.start();
	}
	
	public void close(){
		if(runner != null && runner.isAlive())
			runner.interrupt();
	}


	public boolean transmit(Serializable object) {
		queue.add(object);
		return true;
	}
	public void clearQueue(){
		queue.clear();
	}

}
