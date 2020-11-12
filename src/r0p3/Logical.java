package r0p3;

public class Logical extends Layer {

	public byte[] srcMac;
	public byte[] destMac;
	public short type;
	public byte[] data;

	public Logical(SelfPacket p) {
		// destMac=p.
		
    }

    @Override
    public void run() {

    }
	
	public void configuration() {
		
    }

    public void passPacketLayer(SelfPacket packet) {
    		
    }

}
