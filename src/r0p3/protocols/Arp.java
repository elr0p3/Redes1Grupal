package r0p3.protocols;

import r0p3.layers.Logical;
import r0p3.layers.Network;
import r0p3.layers.SelfPacket;


import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

// import jpcap.*;
import jpcap.packet.ARPPacket;
// import jpcap.packet.Packet;
import jpcap.packet.EthernetPacket;

public class Arp extends SelfProtocol {

	//IP&MAC
	private HashMap<byte[], byte[]> directions;

	private Network network_l;

	public Arp(Network n) {
		this.network_l = n;
		this.finish	= false;
	}

	@Override
	public void run() {
		while(!this.finish) {
			try {
				if (!this.getPacket_list().isEmpty()) {
					this.lock.acquire();

					SelfPacket p = getPacketDiscarding();
					ARPPacket packetConverted = (ARPPacket) p.getPacket();
					ARPPacket arpPacket = new ARPPacket();

					//if packet.macDestination == 0x00,0x00 y opCode sea de request
					if(packetConverted.target_hardaddr == new byte[]{(byte)0x00,(byte)0x00} && packetConverted.operation == ARPPacket.ARP_REQUEST) {
						arpPacket = this.arpReply(packetConverted, p); //devolvemos un arpReply

						SelfPacket return_sp = new SelfPacket(arpPacket);
						return_sp.setScanned_type(true);
						return_sp.setFakeEthType(EthernetPacket.ETHERTYPE_ARP);
						this.network_l.appendPacket(return_sp);
					}//esto tendra que ser un elseif pero el opcode debe ser request
					else if(packetConverted.operation == ARPPacket.ARP_REPLY && !directions.containsKey(packetConverted.sender_protoaddr)) {
						//meter dato en la tabla
						this.directions.put(packetConverted.sender_protoaddr, packetConverted.sender_hardaddr);
					}

                    // System.out.println("ARP Packet recieved -> " + arpPacket.toString());

					this.lock.release();
				} else {
					Thread.sleep(1);
				}
			} catch(InterruptedException err) {
				System.err.println(err);
			}
		}
	}

	@Override
	public void configuration() {

	}



	public ARPPacket arpRequest(byte[] ip_address) {

		//tenemos que pedir por consola la ip y introducirla en la ultima variable
		//debemos crear una tabla con los datos de la mac  y Ip para no enviar una request si ya tenemos dicha mac	
		//if(si el dato no esta en la tabla)

		ARPPacket a = new ARPPacket();
		// a.hardtype = packetConverted.hardtype;
		// a.prototype = packetConverted.prototype;
		// a.hlen = packetConverted.hlen;
		// a.plen = packetConverted.plen;
		a.sender_hardaddr = Logical.srcMac; //MAC source
		// a.sender_protoaddr = packetConverted.sender_protoaddr; //IP source
		a.target_hardaddr = new byte[]{(byte)0x00,(byte)0x00}; //zeros  //MAC destination

		// a.target_protoaddr = packetConverted.target_protoaddr; //Consola IP dest

		return a;

	}




	public ARPPacket arpReply(ARPPacket packetConverted, SelfPacket sp) {

		//replyARP deberemos de cambiar los att de target y sender al contrario para enviarlo de vuelta y cambiar la mac source para que no sea 0x00

		ARPPacket a = new ARPPacket();
		a.hardtype = packetConverted.hardtype;
		a.prototype = packetConverted.prototype;
		a.hlen = packetConverted.hlen;
		a.plen = packetConverted.plen;
		// a.sender_hardaddr = new byte[]{(byte)0x00,(byte)0x00}; //introducir nuestra mac pedida por usuario en la capa 2
		a.sender_hardaddr = sp.getFakeMacAddress(); //introducir nuestra mac pedida por usuario en la capa 2
		a.sender_protoaddr = packetConverted.target_protoaddr; //aqui no hay que introducir ninguna ip por consola ya que ya la tenemos 
		a.target_hardaddr =  packetConverted.sender_hardaddr;
		a.target_protoaddr = packetConverted.sender_protoaddr;
		
		return a;


	}

	//Comprobar que este en la tabla la mac con la ip pedida
	// public boolean checkTable(byte [] ip) {
		
		// if(directions.containsValue(ip))
			// return true;
		// else
			// return false;
		
	// }

	//Usuario inserta IP por consola
	public void requestIP() {

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

		ARPPacket send_arp = this.arpRequest(IPFinal);
		SelfPacket return_sp = new SelfPacket(send_arp);
		return_sp.setScanned_type(true);
		return_sp.setFakeEthType(EthernetPacket.ETHERTYPE_ARP);
		this.network_l.appendPacket(return_sp);
	}

	//Checker para que la ip sea valida
	private boolean isValidIPAddress(String ip_address) {
		return Pattern
				.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
				.matcher(ip_address)
				.find();
	}




}
