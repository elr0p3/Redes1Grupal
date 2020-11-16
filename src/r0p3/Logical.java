package r0p3;

import java.util.Scanner;

public class Logical extends Layer {

	public byte[] srcMac;
	public byte[] destMac;
	public short type;
	public byte[] data;

	public Logical() {
		// destMac=p.
		
    }

    @Override
    public void run() {
//Usar semaforo para enviar paquete
    }
	
	public void configuration() {
//Pedir al usuarios la mac destino o hardcodeada y introducir los demas datos

		Scanner input = new Scanner(System.in);
        String direccion;
        boolean check = false;
        String macAddress;
        String[] macAddressParts;
        char[] ch = new char[12];
        
      	do {
			System.out.print("Write MAC Address destination: ");
			System.out.flush();
			direccion = input.nextLine();
        
			macAddress = direccion;
			macAddressParts = macAddress.split(":");
         
			if(macAddressParts.length == 6) {
				//Pass the string  
                direccion = "";
                for (int j = 0; j < 6; j++)
					direccion += macAddressParts[j]; 
               
                //introduce the values of each string
                for (int i = 0; i < 12; i++)
					ch[i] = direccion.charAt(i); 
                    
                for (char c : ch){
                    //check if each character is in the range
                    if(('0' <= c && c <= '9')||('a' <= c && c <= 'f')||('A' <= c && c <= 'F'))
						check = true;
                    else {
                        check = false;
						break;
                    }
                }
          	}
        } while(macAddressParts.length != 6 || check == false );
        
        // convert hex string to byte values
        Byte[] macAddressBytes = new Byte[6];
        for(int i = 0; i < 6; i++){
            Integer hex = Integer.parseInt(macAddressParts[i], 16);
            macAddressBytes[i] = hex.byteValue();
        }
        
        System.out.print("MIS PUTOS MUERTOS");
    }

    public void passPacketLayer(SelfPacket packet) {
    		
    }

}
