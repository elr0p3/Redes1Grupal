package r0p3.protocols;

import r0p3.layers.*;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class SelfProtocol extends Thread {

	protected 	Semaphore 				lock;
	private 	LinkedList<SelfPacket> 	packet_list;
	protected	boolean					finish;

	public static final String IP 	= "IP";
	public static final String ARP 	= "ARP";

	public SelfProtocol() {
		this.lock 			= new Semaphore(1, true);
		this.packet_list	= new LinkedList<SelfPacket>();
		this.finish			= false;
	}

	@Override
	public void run() {}

	public void configuration() {}


	public void appendPacket(SelfPacket packet) {
        this.packet_list.add(packet);
    }

    public LinkedList<SelfPacket> getPacket_list() {
        return this.packet_list;
    }

    public SelfPacket getPacketDiscarding() {
        return this.packet_list.poll();
    }

	public void setFinish(boolean f) {
		this.finish = f;
	}

}
