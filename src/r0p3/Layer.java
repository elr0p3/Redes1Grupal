package r0p3;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Layer extends Thread {

	private Layer 					up;
	private Layer 					down;
	private ArrayList<SelfPacket> 	packet_list = new ArrayList<SelfPacket>();
	private Semaphore 				lock 		= new Semaphore(1, true);


	public Layer() {}
    
    public void configuration() {}

    @Override
    public void run() {}

	
	public void appendPacket(SelfPacket packet) {
		this.packet_list.add(packet);
	}

	public ArrayList<SelfPacket> getPacket_list() {
		return this.packet_list;
	}

	public SelfPacket getPacketDiscarding(int i) {
		return this.packet_list.remove(i);
	}
	public SelfPacket getPacketDiscarding(SelfPacket sp) {
		int i = this.packet_list.indexOf(sp);
		return this.packet_list.remove(i);
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
		System.out.println("\u001B[33m" + "SENDED TO LAYER 2" + "\u001B[0m");
    	this.lock.release();
    }
    
    public void sendToBottomLayer(SelfPacket packet) throws InterruptedException {
    	this.lock.acquire();
    	this.down.packet_list.add(packet);
		System.out.println("\u001B[34m" + "SENDED TO LAYER 1" + "\u001B[0m");
    	this.lock.release();
    }

    // public void setPacket_list(ArrayList<SelfPacket> packet_list) {
		// this.packet_list = packet_list;
	// }

}
