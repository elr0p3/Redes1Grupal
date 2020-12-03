package r0p3;

import java.util.HashMap;

public class Network extends Layer {

    private int type;
    private byte[] data;
    private HashMap<String, Protocol> protocols;

    public Network() {
        this.protocols  = new HashMap<String, Protocol>();
        this.finish     = false;
    }

    @Override
    public void run() {
        while(!this.finish) {

        }
    }

    @Override
    public void configuration() {

    }
    
}
