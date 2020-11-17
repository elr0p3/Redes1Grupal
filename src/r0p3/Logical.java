package r0p3;

import java.util.Scanner;
// import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logical extends Layer {

	private final 	short 	MAC_LEN = 6;
	private 		byte[] 	srcMac;
	private 		byte[] 	dstMac;

	public Logical() {
		this.srcMac	= new byte[MAC_LEN];
		this.dstMac	= new byte[MAC_LEN];
	}

    @Override
    public void run() {
		SelfPacket s_packet;

		while(true) {
			if(this.getPacket_list().size() > 0) {
			System.out.println("LOGICAL MARIKONG " + this.getPacket_list().size());
				s_packet = this.getPacketDiscarding(0);
				// If it is a packet not send by us
				if(s_packet.getMac_src() != srcMac) {
					s_packet.setMac_src(this.srcMac);
					s_packet.setMac_dst(this.dstMac);
					try {
						this.sendToBottomLayer(s_packet);
						System.out.println("SENDING TO LAYER 1");
					} catch (InterruptedException err) {
						System.err.println("ERROR! PASSING PACKET TO LAYER 1:\n" + err);
					}
				}
			}
		}

    }
	
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
		// Pattern p = Pattern.compile("^([\\dA-Fa-f]{2}[.:-]){5}([\\dA-Fa-f]{2})$");
		// Matcher m = p.matcher(mac_address);
		// return m.find();
		return Pattern
				.compile("^([\\dA-Fa-f]{2}[.:-]){5}([\\dA-Fa-f]{2})$")
				.matcher(mac_address)
				.find();
	}

}
