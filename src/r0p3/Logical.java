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
//Pedir al usuarios la mac destino o hardcodeada y introducir los demas datos
    }
	
	public void configuration() {
//Usar semaforo para enviar paquete
    }

    public void passPacketLayer(SelfPacket packet) {
    		
    }

}
