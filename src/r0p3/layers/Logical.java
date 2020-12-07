package r0p3.layers;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Logical extends Layer {

	private final 	short 	MAC_LEN = 6;
	private 		byte[] 	srcMac;
	private 		byte[] 	dstMac;

	public Logical() {
		this.srcMac	= new byte[MAC_LEN];
		this.dstMac	= new byte[MAC_LEN];
        this.finish = false;
	}

    @Override
    public void run() {
		SelfPacket s_packet;

		while(!this.finish /*&& !this.getPacket_list().isEmpty()*/) {
			try {
				if(!this.getPacket_list().isEmpty()) {
				System.out.println("\u001B[35m" + "LOGICAL LIST PCKT\t~" + this.getPacket_list().size() + "~" + "\u001B[0m");
					// 3. Take a packet from the list
					s_packet = this.getPacketDiscarding();
					System.out.println(s_packet);
					if(s_packet == null)
						System.err.println("WARNING! NULL PACKET");
					if(s_packet.getMac_src() != this.srcMac) {	// If it is a packet not send by us
						// 4. Modify MAC addresses from the packet
						s_packet.setMac_src(this.srcMac);
						s_packet.setMac_dst(this.dstMac);
						// 5. Send back to Layer 1 or Layer 3

						// s_packet.setgoDown();
						if (s_packet.goUp()) {
							System.out.println("\u001B[31m" + "SENDING TO NETWORK\t-1-" + "\u001B[0m");
							this.sendToUpperLayer(s_packet);
						} else {
							System.out.println("\u001B[34m" + "SENDING TO PHYSICAL\t-1-" + "\u001B[0m");
							this.sendToBottomLayer(s_packet);
						}
					}
				} else {
					Thread.sleep(1);
					// System.out.println("DENTRO -------------------------> " + this.getPacket_list().size());
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
        this.dstMac = new byte[]{
            (byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff
        };
    }

	private boolean isValidMacAddress(String mac_address) {
		return Pattern
				.compile("^([\\dA-Fa-f]{2}[.:-]){5}([\\dA-Fa-f]{2})$")
				.matcher(mac_address)
				.find();
	}

}
