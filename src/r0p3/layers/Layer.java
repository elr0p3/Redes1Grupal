package r0p3.layers;

// import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Layer extends Thread {

	private     Layer			up;
	private     Layer			down;
	private     LinkedList<SelfPacket> 	packet_list;
	protected   Semaphore			lock;
	protected   boolean			finish;
	// public static final byte[] IP_ADDRESS;


	public Layer() {
		packet_list = new LinkedList<SelfPacket>();
		this.lock   = new Semaphore(1, true);
		this.finish = false;
	}
    
	public void configuration() {}

	@Override
	public void run() {}

	
	public void appendPacket(SelfPacket packet) {
		this.packet_list.add(packet);
	}

	public LinkedList<SelfPacket> getPacket_list() {
		return this.packet_list;
	}

	public SelfPacket getPacketDiscarding() {
		return this.packet_list.poll();
	}


	public Layer getUp() {
		return this.up;
	}

	public void setUp(Layer up) {
		this.up = up;
	}

	public Layer getDown() {
		return this.down;
	}

	public void setDown(Layer down) {
		this.down = down;
	}

	public void setFinish(boolean f) {
		this.finish = f;
	}
	

	public void sendToUpperLayer(SelfPacket packet) throws InterruptedException {
		this.lock.acquire();
		this.up.packet_list.add(packet);
		this.lock.release();
	}
    
	public void sendToBottomLayer(SelfPacket packet) throws InterruptedException {
		this.lock.acquire();
		this.down.packet_list.add(packet);
		this.lock.release();
	}

	// public void setPacket_list(ArrayList<SelfPacket> packet_list) {
		// this.packet_list = packet_list;
	// }

}
