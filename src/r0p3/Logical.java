package r0p3;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logical extends Layer {

	private final short MAC_LEN = 6;
	private byte[] srcMac	= new byte[MAC_LEN];
	private byte[] dstMac	= new byte[MAC_LEN];

	public Logical() {
		// destMac=p.
		
    }

    @Override
    public void run() {

		while(true) {
			// MAC FILTER

			if(getPacket_list().size() > 0) {
				if(getPacket_list().get(0).getMac_src() != srcMac) {
					//creamos un nuevo paquete y le enviamos los nuevos parametros
					getPacket_list().get(0).setMac_src(srcMac);
					try {
						sendToBottomLayer(getPacket_list().get(0));
						//enviamos el paquete a la capa de abajo
					} catch (InterruptedException err) {
						System.err.println("ERROR! PASSING PACKET TO LAYER 1" + err);
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
			if (!isValidMacAddress(mac_address))
				System.err.println("ERROR! Not valid mac address");
		} while(!isValidMacAddress(mac_address));
        
		mac_splited = mac_address.split("[.:-]");

		for (int i = 0; i < MAC_LEN; i++)
			srcMac[i] = (byte) Integer.parseInt(mac_splited[i], 16);
        
        
        //Broadcast for Destination Mac
        dstMac = new byte[]{
            (byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff
        };
    }

	private boolean isValidMacAddress(String mac_address) {
		Pattern p = Pattern.compile("^([\\dA-Fa-f]{2}[.:-]){5}([\\dA-Fa-f]{2})$");
		Matcher m = p.matcher(mac_address);
		return m.find();
	}

}
