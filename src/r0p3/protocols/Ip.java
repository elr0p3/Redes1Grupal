package r0p3.protocols;

import r0p3.layers.Network;
import r0p3.layers.SelfPacket;

import jpcap.packet.IPPacket;

public class Ip extends SelfProtocol {

	private Network network_l;

	public Ip(Network n) {
		this.network_l = n;
		this.finish = false;
	}

	@Override
	public void run() {
		while(!this.finish) {
			try {
				if (!this.getPacket_list().isEmpty()) {
					this.lock.acquire();

					SelfPacket p = this.getPacketDiscarding();
					IPPacket ipPacket = (IPPacket) p.getPacket();
					// System.out.println("IP Packet recieved -> " + ipPacket.toString());

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
