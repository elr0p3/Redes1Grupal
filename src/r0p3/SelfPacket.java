package r0p3;

import jpcap.packet.Packet;
import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;

public class SelfPacket {

	private Packet packet;
	private DatalinkPacket data_link;
	private EthernetPacket eth_data;
	private byte[] mac_src;
	private byte[] mac_dst;
	private short ether_type;


	// private boolean direction;

	
	public SelfPacket (Packet p) {
		packet = p;
		data_link = p.datalink;
		eth_data = (EthernetPacket)p.datalink;

		mac_src = eth_data.src_mac;
		mac_dst = eth_data.dst_mac;
		ether_type = eth_data.frametype;
	}

	public void printDataLinkEth() {
		System.out.println("DATA LINK -> " + data_link);
		System.out.println("ETH LINK  -> " + mac_src + " - " + mac_dst + " - " + ether_type);
	}

	public Packet getPacket () {
		return packet;
	}

	// public SelfPacket(Packet p, boolean dir) {
		// packet = p;
		// direction = dir;
	// }
	
	// public void setDirection (boolean dir) {
		// direction = dir;
	// }
}
