package r0p3;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import jpcap.*;
import jpcap.packet.*;

public class Physical extends Layer {

	private NetworkInterface selectInterface;
    private JpcapCaptor captor;
    private int numberInterface = 0;
    private int num_of_bytes = 0;
    private boolean promisc;
    private int caputure_timeout = 0;
	private int number_packets = 0;

	// Packet[] p_prueba = new Packet[10];

    public Physical(int num, boolean prmsc, int cap_time, int num_pac) {
        num_of_bytes        = num;
        promisc             = prmsc;
        caputure_timeout    = cap_time;
		number_packets		= num_pac;
    }

    public Physical(int n_recv) {
        this(65535, false, 20, n_recv);
    }

	@Override
    public void run() {

        if (number_packets == 0) {
            while (true) {
                managePackages();
            }
        } else {
            for (int i = 0; i < number_packets; i++) {
                managePackages();
            }
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
                numberInterface = scan.nextInt();
                if (0 <= numberInterface && numberInterface < devices.length)
                    done = true;
                else
                    System.err.println("ERROR! Number out of range");
            } catch (InputMismatchException e) {
                System.err.println("ERROR! Invalid integer input");
                // scan = new Scanner(System.in);
                scan.nextLine();
            }
        } while (done == false);

        // scan.close();

        try {
			selectInterface = devices[numberInterface];
            captor = JpcapCaptor.openDevice(
                selectInterface,
                num_of_bytes,
                promisc,
                caputure_timeout
            );
        } catch (IOException err) {
            System.err.println(err);
        }
    }   

    public void managePackages() {

        // 1. Recieve new packet from medium
        Packet pckt = captor.getPacket();
        System.out.println("RECIEVE -> " + pckt.toString());

        // 2. Pass packet to Layer 2
		try {
            if (pckt != null)
			    sendToUpperLayer(new SelfPacket(pckt));
		} catch (InterruptedException err) {
			System.err.println("ERROR PASSING PACKET TO LAYER 2:\n" + err);
		}

        // 6. Check if there is anything in the list
        if (this.getPacket_list().size() > 0) {
            // 7. Send Packet to medium
            try {
	        	for (SelfPacket p: getPacket_list())
	        		sendPackage(p.getPacket());
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

		JpcapSender sender = JpcapSender.openDevice(selectInterface);
		EthernetPacket ether = new EthernetPacket(); 	// create an Ethernet packet (frame)

		ether.frametype = EthernetPacket.ETHERTYPE_IP;	// set frame type as IP
		ether.src_mac = selectInterface.mac_address;	//set source and destination MAC addresses
		ether.dst_mac = new byte[]{
			(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff
		};
		// ether.dst_mac = selectInterface.mac_address;
		
		p.datalink = ether;


		System.out.println("MAC -> " + selectInterface.mac_address);
		System.out.print(" MAC address:");
  		for (byte b : selectInterface.mac_address)
    		System.out.print(Integer.toHexString(b&0xff) + ":");
  		System.out.println();


		sender.sendPacket(p);	//send the packet p
		sender.close();
	}


    // private void receivePackage() {
		// if (number_packets == 0)
			// while (true) {
                // System.out.println(captor.getPacket().toString());
            // }
		// else 
			// for (int i = 0; i < number_packets; i++) {
				// System.out.println(captor.getPacket().toString());
				// SelfPacket p = new SelfPacket(captor.getPacket());
				// p.printDataLinkEth();
				// appendPacket(p);
			// }

		// captor.close();
	// }

}


