package r0p3.layers;

import jpcap.packet.Packet;
import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;

public class SelfPacket {

	private Packet		packet;
	private DatalinkPacket	data_link;
	private EthernetPacket	eth_data;
	private byte[]		mac_src;
	private byte[]		mac_dst;
	private short		ether_type;
	private boolean		up_down;	// true -> up || false -> down

	private byte[]		fake_mac_address;
	private short		fake_eth_type;

	private boolean		scanned_type;

	
	public SelfPacket(Packet p) {
		this.packet 	= p;
		this.data_link 	= p.datalink;

		if (this.data_link != null) {
			this.eth_data 	= (EthernetPacket)p.datalink;
			this.mac_src 	= this.eth_data.src_mac;
			this.mac_dst 	= this.eth_data.dst_mac;
			this.ether_type = this.eth_data.frametype;
		} else {
			this.eth_data	= new EthernetPacket();
		}
		
		
		this.up_down	= true;

		this.fake_mac_address 	= new byte[6];
		this.fake_eth_type		= EthernetPacket.ETHERTYPE_IP;

		this.scanned_type = false;
	}


	public DatalinkPacket getData_link() {
		return this.data_link;
	}

	public void setData_link(DatalinkPacket data_link) {
		this.data_link          = data_link;
		this.packet.datalink	= this.data_link;
	}

	public EthernetPacket getEth_data() {
		return this.eth_data;
	}

	public void setEth_data(EthernetPacket eth_data) {
		this.eth_data 			= eth_data;
		this.packet.datalink	= this.eth_data;
	}

	public byte[] getMac_src() {
		return this.mac_src;
	}

	public void setMac_src(byte[] mac_src) {
		this.mac_src 			= mac_src;
		this.eth_data.src_mac	= this.mac_src;
		this.packet.datalink 	= this.eth_data;
	}

	public byte[] getMac_dst() {
		return this.mac_dst;
	}

	public void setMac_dst(byte[] mac_dst) {
		this.mac_dst 			= mac_dst;
		this.eth_data.dst_mac	= this.mac_dst;
		this.packet.datalink 	= this.eth_data;
	}

	public short getEther_type() {
		return this.ether_type;
	}

	public void setEther_type(short ether_type) {
		this.ether_type 		= ether_type;
		this.eth_data.frametype	= this.ether_type;
		this.packet.datalink	= this.eth_data;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public Packet getPacket() {
		// this.eth_data.dst_mac 	= this.mac_dst;
		// this.eth_data.src_mac 	= this.mac_src;
		// this.eth_data.frametype	= this.ether_type;
		// this.packet.datalink 	= this.eth_data;

		return this.packet;
	}

	public void setGoUp() {
		this.up_down = true;
	}

	public void setgoDown() {
		this.up_down = false;
	}

	public boolean goUp() {
		return this.up_down;
	}


	public void setFakeMacAddress(byte[] fma) {
		this.fake_mac_address = fma;
	}

	public byte[] getFakeMacAddress() {
		return this.fake_mac_address;
	}

	public void setFakeEthType(short fe) {
		this.fake_eth_type = fe;
	}

	public short getFakeEthType() {
		return this.fake_eth_type;
	}


	public void setScanned_type(boolean st) {
		this.scanned_type = st;
	}

	public boolean getScanned_type() {
		return this.scanned_type;
	}
}
