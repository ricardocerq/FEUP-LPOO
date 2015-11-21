package network;

import java.io.Serializable;

public interface ReceiverListener {
	public boolean onReceive(int number, Serializable s);
}
