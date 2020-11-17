package r0p3;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import jpcap.*;
import jpcap.packet.*;

public class Physical extends Layer {

	private NetworkInterface 	selectInterface;
    private JpcapCaptor 		captor;
    private int 				numberInterface 	= 0;
    private int 				num_of_bytes 		= 0;
    private boolean 			promisc;
    private int 				caputure_timeout 	= 0;

	// Packet[] p_prueba = new Packet[10];

    public Physical(int num, boolean prmsc, int cap_time) {
    	this.num_of_bytes        = num;
    	this.promisc             = prmsc;
    	this.caputure_timeout    = cap_time;
    }

    public Physical() {
        this(65535, false, 20);
    }

	@Override
    public void run() {
        while (true) {
        	this.managePackages();
        }
    }
    
    @Override
    public void configuration() {
        Scanner scan = new Scanner(System.in);
        boolean done = false;
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        for (int i = 0; i < devices.length; i++)
            System.out.println(i + ": " + devices[i].name + '(' + devices[i].description + ')');

        do {
            try {
                System.out.print("Introduce the interface to use: ");
                System.out.flush();
                this.numberInterface = scan.nextInt();
                if (0 <= this.numberInterface && this.numberInterface < devices.length)
                    done = true;
                else
                    System.err.println("ERROR! Number out of range");
            } catch (InputMismatchException e) {
                System.err.println("ERROR! Invalid integer input");
                scan.nextLine();
            }
        } while (done == false);

        // scan.close();

        try {
			selectInterface = devices[this.numberInterface];
            this.captor = JpcapCaptor.openDevice(
                selectInterface,
                num_of_bytes,
                promisc,
                caputure_timeout
            );
        } catch (IOException err) {
            System.err.println("ERROR! Can't open the device\n" + err);
        }
    }   

    public void managePackages() {

        // 1. Recieve new packet from medium
        Packet pckt = captor.getPacket();
        System.out.println(" -- RECIEVE -> " + this.macAddressToString(pckt));

        // 2. Pass packet to Layer 2
		try {
            if (pckt != null) {
				System.out.println("SENDING TO LAYER 2");
			    this.sendToUpperLayer(new SelfPacket(pckt));
			}
		} catch (InterruptedException err) {
			System.err.println("ERROR PASSING PACKET TO LAYER 2:\n" + err);
		}

        // 6. Check if there is anything in the list
        if (this.getPacket_list().size() > 0) {
			System.out.println("PHYSICAL MARIKONG " + this.getPacket_list().size());
            // 7. Send Packet to medium
            try {
				this.sendPackage(this.getPacketDiscarding(0).getPacket());
	        } catch (IOException err) {
	        	System.err.println("ERROR SENDING PACKET TO MEDIUM:\n" + err);
	        }
        }

        // -- LAYER 2 THINGS --
        // 3. Coger un paquete de la lista
        // 4. Modificar paquete
        // 5. Mandar paquete modificado a capa 1
    }


	public void sendPackage(Packet p) throws IOException {

		JpcapSender sender = JpcapSender.openDevice(this.selectInterface);
		EthernetPacket ether = new EthernetPacket(); 	// create an Ethernet packet (frame)

		ether.frametype = EthernetPacket.ETHERTYPE_IP;	// set frame type as IP
		ether.src_mac = selectInterface.mac_address;	//set source and destination MAC addresses
		// ether.dst_mac = new byte[]{
			// (byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff
		// };
		// ether.dst_mac = selectInterface.mac_address;
		
		p.datalink = ether;

		System.out.println(" -- SEND -> " + this.macAddressToString(p));

		sender.sendPacket(p);	//send the packet p
		sender.close();
	}


	private String macAddressToString(Packet p) {
		String macs = "";

		for (byte b : ((EthernetPacket)p.datalink).dst_mac)
    		macs += Integer.toHexString(b&0xff) + ":";
		macs += " - ";
		for (byte b : ((EthernetPacket)p.datalink).src_mac)
    		macs += Integer.toHexString(b&0xff) + ":";

		return macs;
	}

}

