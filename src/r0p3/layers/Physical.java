package r0p3.layers;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import jpcap.*;
import jpcap.packet.*;

public class Physical extends Layer {

	private NetworkInterface	selectInterface;
	private JpcapCaptor 		captor;
	private int			numberInterface  = 0;
	private int			num_of_bytes 	 = 0;
	private boolean			promisc;
	private int			caputure_timeout = 0;


	public Physical(int num, boolean prmsc, int cap_time) {
		this.num_of_bytes       = num;
		this.promisc            = prmsc;
		this.caputure_timeout   = cap_time;
		this.finish             = false;
	}

	public Physical() {
		this(65535, false, 20);
	}

	@Override
	public void run() {
		while (!this.finish) {
			try {
				// 1. Recieve new packet from medium
				Packet pckt = captor.getPacket();
				if (pckt != null) {
					// System.out.println("\u001B[36m" + " -- RECIEVE -> " + this.macAddressesToString(pckt) + "\u001B[0m");
					// System.out.println("\u001B[33m" + "SENDING TO LOGICAL FROM PHYSICAL\t-2-" + "\u001B[0m");
					// 2. Pass packet to Layer 2
					this.sendToUpperLayer(new SelfPacket(pckt));
					// Thread.sleep(50);
				}
			} catch (Exception err) {
				System.err.println("ERROR PASSING PACKET TO LAYER 2:");
				err.printStackTrace();
				System.err.println();
			}

			// 6. Check if there is anything in the list
			if (!this.getPacket_list().isEmpty()) {
				// System.out.println("\u001B[35m" + "PHYSICAL LIST PCKT\t~" + this.getPacket_list().size() + "~" + "\u001B[0m");
				// 7. Send Packet to medium
				try {
					Packet pckt_send = this.getPacketDiscarding().getPacket();
					this.sendPackage(pckt_send);
				} catch (Exception err) {
					System.err.println("ERROR SENDING PACKET TO MEDIUM:");
					err.printStackTrace();
					System.err.println();
				}
				// System.out.println("--- SENDED ---");
			}
		}

		this.getUp().setFinish(true);
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
			System.err.println("ERROR! Can't open the device");
			err.printStackTrace();
			System.err.println();
		}
	}


	public void sendPackage(Packet p) throws IOException {
		JpcapSender sender = JpcapSender.openDevice(this.selectInterface);
		
		// System.out.println("\u001B[36m" + " -- SEND -> " + this.macAddressesToString(p) + "\u001B[0m");
		
		sender.sendPacket(p);
		sender.close();
	}


	private String macAddressesToString(Packet p) {
		String macs = "";
		for (byte b : ((EthernetPacket)p.datalink).dst_mac)
    		macs += Integer.toHexString(b&0xff) + ":";
		macs += " - ";
		for (byte b : ((EthernetPacket)p.datalink).src_mac)
    		macs += Integer.toHexString(b&0xff) + ":";
		return macs;
	}


}

