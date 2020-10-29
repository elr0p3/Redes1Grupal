package r0p3;

import jpcap.packet.Packet;

public class SelfPacket {

	private Packet packet;
	private boolean direction;

	public SelfPacket(Packet p, boolean dir) {
		packet = p;
		direction = dir;
	}
	
	public void setDirection (boolean dir) {
		direction = dir;
	}
}
