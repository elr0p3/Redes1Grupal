package r0p3.layers;

import r0p3.protocols.*;

import java.util.HashMap;

public class Network extends Layer {

    private byte[] data;
    private HashMap<String, Protocol> protocols;

    public Network() {
        this.protocols  = new HashMap<String, Protocol>();
        this.finish     = false;
    }

    @Override
    public void run() {
        while(!this.finish) {
			// EthernetPacket ethP = (EthernetPacket) packet2process.datalink;
			// this.type = ethP.frametype;

        }
    }

    @Override
    public void configuration() {

    }

}
