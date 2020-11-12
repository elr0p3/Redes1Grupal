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

	Packet[] p_prueba = new Packet[10];

    public Physical(int num, boolean prmsc, int cap_time, int num_pac) {
        num_of_bytes        = num;
        promisc             = prmsc;
        caputure_timeout    = cap_time;
		number_packets		= num_pac;
    }

    public Physical() {
        this(65535, false, 20, 10);
    }

	@Override
    public void run() {
        configuration();
		receivePackage(number_packets);
		try {
			for (Packet p: p_prueba)
				sendPackage(p);
		} catch (IOException err) {
			System.err.println("TE LA MAMASTE: " + err);
		}
    }
    
    @Override
    public void configuration() {
        Scanner scan = new Scanner(System.in);
        boolean done = false;
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        for (int i = 0; i < devices.length; i++)
            System.out.println(i + ": " + devices[i].name +
                    '(' + devices[i].description + ')');

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

        scan.close();

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

	public void receivePackage(int pac_num) {
		if (pac_num == 0)
			while (true)
				System.out.println(captor.getPacket().toString());
		else 
			for (int i = 0; i < pac_num; i++) {
				System.out.println(captor.getPacket().toString());
				p_prueba[i] = captor.getPacket();
			}

		captor.close();
	}

	public void sendPackage(Packet p) throws IOException {

		JpcapSender sender = JpcapSender.openDevice(selectInterface);
		EthernetPacket ether = new EthernetPacket(); 	// create an Ethernet packet (frame)

		ether.frametype = EthernetPacket.ETHERTYPE_IP;	// set frame type as IP
		ether.src_mac = selectInterface.mac_address;	//set source and destination MAC addresses
		ether.dst_mac = new byte[]{
			(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
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
}


/*
        //Obtain the list of network interfaces
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        //for each network interface
        for (int i = 0; i < devices.length; i++) {
            //print out its name and description
            System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");
        
            //print out its datalink name and description
            System.out.println(" datalink: "+devices[i].datalink_name + "(" + devices[i].datalink_description+")");
        
            //print out its MAC address
            System.out.print(" MAC address:");
            for (byte b : devices[i].mac_address)
              System.out.print(Integer.toHexString(b&0xff) + ":");
            System.out.println();
        
            //print out its IP address, subnet mask and broadcast address
            for (NetworkInterfaceAddress a : devices[i].addresses)
                System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
        }
*/
