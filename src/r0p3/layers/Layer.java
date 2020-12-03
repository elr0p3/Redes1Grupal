package r0p3.layers;

// import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Layer extends Thread {

	private     Layer 					up;
	private     Layer 					down;
	private     LinkedList<SelfPacket> 	packet_list;
	private		LinkedList<SelfPacket>	pckt_list_bottom;
	private     Semaphore 				lock;
    protected   boolean                 finish;


	public Layer() {
		packet_list = new LinkedList<SelfPacket>();
		lock		= new Semaphore(1, true);
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

	

	public void sendToUpperLayer(SelfPacket packet) throws InterruptedException {
    	this.lock.acquire();
    	this.up.packet_list.add(packet);
		System.out.println("\u001B[33m" + "SENDED TO LOGICAL\t-2-" + "\u001B[0m");
    	this.lock.release();
    }
    
    public void sendToBottomLayer(SelfPacket packet) throws InterruptedException {
    	this.lock.acquire();
    	this.down.packet_list.add(packet);
		System.out.println("\u001B[34m" + "SENDED TO PHYSICAL\t-1-" + "\u001B[0m");
    	this.lock.release();
    }

    // public void setPacket_list(ArrayList<SelfPacket> packet_list) {
		// this.packet_list = packet_list;
	// }

}
