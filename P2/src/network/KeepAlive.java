package network;

import java.io.Serializable;

public class KeepAlive implements Serializable{
	private static final long serialVersionUID = -1916653793902835060L;
	public long time;
	public KeepAlive(long l){
		this.time = l;
	}
	public void update(){
		time = System.currentTimeMillis();
	}
}
