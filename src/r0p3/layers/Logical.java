package r0p3.layers;

import java.util.Scanner;
import java.util.regex.Pattern;

// import jpcap.packet.EthernetPacket;

public class Logical extends Layer {

	private final 	short 	MAC_LEN = 6;
	public static	byte[] 	srcMac;
	private 	byte[] 	dstMac;
	private         byte[]  broadcast;

	public Logical() {
		this.srcMac	= new byte[MAC_LEN];
		this.dstMac	= new byte[MAC_LEN];
		this.broadcast	= new byte[]{
			(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff
		};
		this.finish	= false;
	}

	@Override
	public void run() {
		SelfPacket s_packet;

		while(!this.finish /*&& !this.getPacket_list().isEmpty()*/) {
			try {
				if(!this.getPacket_list().isEmpty()) {
					// System.out.println("\u001B[35m" + "LOGICAL LIST PCKT\t~" + this.getPacket_list().size() + "~" + "\u001B[0m");
					// 3. Take a packet from the list
					s_packet = this.getPacketDiscarding();
					// System.out.println(s_packet);

					if (s_packet.goUp()) {
							if (macAddressesFilter(s_packet)) {
							// System.out.println("--- IN ---\n"
								// + macAddressesToString(s_packet) + "\n"
								// + macAddressesToString(this.srcMac, this.dstMac) + "\n"
								// + s_packet.getMac_src() + " - " + s_packet.getMac_dst() + "\n"
								// + this.srcMac + " - " + this.dstMac);
							s_packet.setFakeMacAddress(this.srcMac);	    

							// 5. Send to Layer 3
							// System.out.println("\u001B[31m" + "SENDING TO NETWORK FROM LOGICAL\t-3-" + "\u001B[0m");
							this.sendToUpperLayer(s_packet);
						}
					} else {
						// 4. Modify MAC addresses from the packet
						s_packet.setMac_src(this.srcMac);
						s_packet.setMac_dst(this.dstMac);
						s_packet.setEther_type(s_packet.getFakeEthType());

						// Send back to Layer 1
						// System.out.println("\u001B[34m" + "SENDING TO PHYSICAL FROM LOGICAL\t-1-" + "\u001B[0m");
						this.sendToBottomLayer(s_packet);
					}
				} else {
					Thread.sleep(1);
				}
			} catch (Exception err) {
				System.err.println("ERROR!\n" + err);
			}
		}

		this.getUp().setFinish(true);
	}
	
	@Override
	public void configuration() {
		Scanner scan = new Scanner(System.in);
		String mac_address;
		String[] mac_splited;
        
		do {
			System.out.print("Write source MAC Address: ");
			System.out.flush();
			mac_address = scan.nextLine();
			if (!this.isValidMacAddress(mac_address))
				System.err.println("ERROR! Not valid mac address");
		} while(!this.isValidMacAddress(mac_address));

		mac_splited = mac_address.split("[.:-]");

		for (int i = 0; i < MAC_LEN; i++)
			this.srcMac[i] = (byte) Integer.parseInt(mac_splited[i], 16);
        
        
		//Broadcast for Destination Mac
		this.dstMac = this.broadcast;
	}

	private boolean isValidMacAddress(String mac_address) {
		return Pattern
			.compile("^([\\dA-Fa-f]{2}[.:-]){5}([\\dA-Fa-f]{2})$")
			.matcher(mac_address)
			.find();
	}


	private boolean macAddressesFilter(SelfPacket s_packet) {
		//if(s_packet.getMac_src() != this.srcMac /*&& (
			//s_packet.getMac_dst() == this.srcMac ||
			//s_packet.getMac_dst() == this.broadcast)*/) {	// Filter for packets send by us
		// if(!byteToStr(s_packet.getMac_src()).equals(byteToStr(this.srcMac)) && (
			// byteToStr(s_packet.getMac_dst()).equals(byteToStr(this.srcMac)) ||
			// byteToStr(s_packet.getMac_dst()).equals(byteToStr(this.broadcast))
		// )) {

		byte[] packet_src = s_packet.getMac_src();
		byte[] packet_dst = s_packet.getMac_dst();
		boolean check_1 = false;
		boolean check_2 = true;
		boolean check_3 = true;

		// 1. Check it is not a packet sended by us
		for (int i = 0; i < MAC_LEN; i++) {
			if (packet_src[i] != this.srcMac[i]) {
				check_1 = true;
			}
		}

		// 2. Packet is for us
		for (int i = 0; i < MAC_LEN; i++) {
			if (packet_dst[i] != this.srcMac[i]) {
				check_2 = false;
			}
		}

		// 3. Packet sended to broadcast
		for (int i = 0; i < MAC_LEN; i++) {
			if (packet_dst[i] != this.broadcast[i]) {
				check_3 = false;
			}
		}

		if (check_1 && (check_2 || check_3)) {
			return true;
		} else {
			return false;
		}

	}


	private String macAddressesToString(SelfPacket p) {
		String macs = "";
		for (byte b : p.getMac_src())
    		macs += Integer.toHexString(b&0xff) + ":";
		macs += " - ";
		for (byte b : p.getMac_dst())
    		macs += Integer.toHexString(b&0xff) + ":";
		return macs;
	}

	private String macAddressesToString(byte[] src, byte[] dst) {
		String macs = "";
		for (byte b : src)
    		macs += Integer.toHexString(b&0xff) + ":";
		macs += " - ";
		for (byte b : dst)
    		macs += Integer.toHexString(b&0xff) + ":";
		return macs;
	}

	private String byteToStr(byte[] mac) {
		String macs = "";
		for (byte b : mac)
    		macs += Integer.toHexString(b&0xff) + ":";
		return macs;
	}

}
