package r0p3;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Layer extends Thread {

	private Layer up;
	private Layer down;
	private ArrayList<SelfPacket> packet_list = new ArrayList<SelfPacket>();
	private Semaphore lock = new Semaphore(1, true);


	// public Layer(Layer u, Layer d) {
		// up = u;
		// down = d;
		// packet_list = new ArrayList<SelfPacket>();
	// }

	// public Layer() {
		// this(null, null);
	// }
    
    public void configuration() {

    }

    @Override
    public void run() {

    }

	
	public void appendPacket(SelfPacket packet) {
		packet_list.add(packet);
	}

	public ArrayList<SelfPacket> getPacket_list() {
		return packet_list;
	}


	public Layer getUp() {
		return up;
	}

	public void setUp(Layer up) {
		this.up = up;
	}

	public Layer getDown() {
		return down;
	}

	public void setDown(Layer down) {
		this.down = down;
	}

	// public ArrayList<SelfPacket> getPacket_list() {
		// return packet_list;
	// }

	// public void setPacket_list(ArrayList<SelfPacket> packet_list) {
		// this.packet_list = packet_list;
	// }
	

	public void sendToUpperLayer(SelfPacket packet) throws InterruptedException {
    	lock.acquire();
    	up.packet_list.add(packet);
    	lock.release();
    }
    
    public void sendToBottomLayer(SelfPacket packet) throws InterruptedException {
    	lock.acquire();
    	down.packet_list.add(packet);
    	lock.release();
    }
}
