package r0p3.layers;

import r0p3.protocols.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import jpcap.packet.EthernetPacket;

public class Network extends Layer {

	private HashMap<String, SelfProtocol> protocols;
	public static byte[] ip_address;

	public Network() {
		this.protocols  = new HashMap<String, SelfProtocol>();
		this.finish     = false;

		// this.protocols.put(SelfProtocol.IP, new Ip(this));
		// this.protocols.put(SelfProtocol.ARP, new Arp(this));
	}

	public void setProtocols(Arp arp, Ip ip) {
		this.protocols.put(SelfProtocol.ARP, arp);
		this.protocols.put(SelfProtocol.IP, ip);
	}

	@Override
	public void run() {
		SelfPacket s_packet;
		short type;
		
		this.protocols.get(SelfProtocol.IP).start();
		this.protocols.get(SelfProtocol.ARP).start();

		while (!this.finish) {
			try {
				if (!this.getPacket_list().isEmpty()) {
					s_packet = this.getPacketDiscarding();

					if (!s_packet.getScanned_type()) {
						s_packet.setScanned_type(true);
						type = s_packet.getEther_type();

						if (type == EthernetPacket.ETHERTYPE_IP) {
							this.protocols.get(SelfProtocol.IP).appendPacket(s_packet);
						} else if (type == EthernetPacket.ETHERTYPE_ARP) {
							this.protocols.get(SelfProtocol.ARP).appendPacket(s_packet);
						} else {
							// System.out.println("\u001B[31m" + " -  -  NONE  -   -" + "\u001B[0m");
						}
						continue;
					}

					s_packet.setgoDown();
					if (s_packet.goUp()) {
						// this.sendToUpperLayer(s_packet);
					} else {
						// System.out.println("\u001B[32m" + "SENDING TO LOGICAL FROM NETWORK\t-2-" + "\u001B[0m");
						this.sendToBottomLayer(s_packet);
					}

				} else {
					Thread.sleep(1);
				}
			} catch (InterruptedException err) {
				System.err.println(err);
			}
		}

		// this.getUp().setFinish(true);
		this.protocols.get(SelfProtocol.IP).setFinish(true);
		this.protocols.get(SelfProtocol.ARP).setFinish(true);
	}

	@Override
	public void configuration() {
			
		Scanner scan = new Scanner(System.in);
		String ip_address;
		String[] ip_splited;
		short 	IP_LEN = 4;
		byte[]  IPFinal = new byte[IP_LEN];

		do {
			System.out.print("Write source IP Address: ");
			System.out.flush();
			ip_address = scan.nextLine();
			if (!this.isValidIPAddress(ip_address))
				System.err.println("ERROR! Not valid IP address");
		} while(!this.isValidIPAddress(ip_address));

		ip_splited = ip_address.split("\\.");

		for (int i = 0; i < IP_LEN; i++)
			IPFinal[i] = (byte) Integer.parseInt(ip_splited[i]);

		Network.ip_address = IPFinal;
	}

	//Checker para que la ip sea valida
	private boolean isValidIPAddress(String ip_address) {
		return Pattern
				.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
				.matcher(ip_address)
				.find();
	}


}
