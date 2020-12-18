package r0p3.layers;

import r0p3.protocols.*;

import java.util.HashMap;

import jpcap.packet.EthernetPacket;

public class Network extends Layer {

    private HashMap<String, SelfProtocol> protocols;

    public Network() {
        this.protocols  = new HashMap<String, SelfProtocol>();
        this.finish     = false;

		this.protocols.put(SelfProtocol.IP, new Ip());
		this.protocols.put(SelfProtocol.ARP, new Arp());
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
					type = s_packet.getEther_type();
					if (type == EthernetPacket.ETHERTYPE_IP) {
						this.protocols.get(SelfProtocol.IP).appendPacket(s_packet);
					} else if (type == EthernetPacket.ETHERTYPE_ARP) {
						this.protocols.get(SelfProtocol.ARP).appendPacket(s_packet);
					} else {
						System.out.println("\u001B[31m" + " -  -  NONE  -   -" + "\u001B[0m");
					}

					s_packet.setgoDown();
					if (s_packet.goUp()) {
						// this.sendToUpperLayer(s_packet);
                    } else {
						System.out.println("\u001B[32m" + "SENDING TO LOGICAL FROM NETWORK\t-2-" + "\u001B[0m");
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

    }

}
