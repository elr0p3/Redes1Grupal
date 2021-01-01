package r0p3.protocols;

import r0p3.layers.*;

import jpcap.packet.ARPPacket;

public class Arp extends SelfProtocol {

	public Arp() {
		this.finish = false;
	}

	@Override
	public void run() {
		while(!this.finish) {
			try {
				if (!this.getPacket_list().isEmpty()) {
					this.lock.acquire();

					SelfPacket p = getPacketDiscarding();
					ARPPacket arpPacket = (ARPPacket) p.getPacket();
					System.out.println("ARP Packet recieved -> " + arpPacket.toString());

					this.lock.release();
				} else {
					Thread.sleep(1);
				}
			} catch(InterruptedException err) {
				System.err.println(err);
			}
		}
	}

	@Override
	public void configuration() {

	}

}
