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



	public DatalinkPacket getData_link() {
		return data_link;
	}

	public void setData_link(DatalinkPacket data_link) {
		this.data_link = data_link;
	}

	public EthernetPacket getEth_data() {
		return eth_data;
	}

	public void setEth_data(EthernetPacket eth_data) {
		this.eth_data = eth_data;
	}

	public byte[] getMac_src() {
		return mac_src;
	}

	public void setMac_src(byte[] mac_src) {
		this.mac_src = mac_src;
	}

	public byte[] getMac_dst() {
		return mac_dst;
	}

	public void setMac_dst(byte[] mac_dst) {
		this.mac_dst = mac_dst;
	}

	public short getEther_type() {
		return ether_type;
	}

	public void setEther_type(short ether_type) {
		this.ether_type = ether_type;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public Packet getPacket() {
		eth_data.dst_mac 	= this.mac_dst;
		eth_data.src_mac 	= this.mac_src;
		eth_data.frametype	= this.ether_type;
		packet.datalink 	= eth_data;

		return packet;
	}

}
