package r0p3;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logical extends Layer {

	private byte[] srcMac;
	private byte[] destMac;

	public Logical() {
		// destMac=p.
		
    }

    @Override
    public void run() {

		while(true) {
			// MAC FILTER

			if(getPacket_list().size() > 0) {
				if(getPacket_list().get(0).getMac_src() != srcMac) {
                     //creamos un nuevo paquete y le enviamos los nuevos parametros
                     getPacket_list().get(0).setMac_src(srcMac);
					 try {
                     	sendToBottomLayer(getPacket_list().get(0));
						//enviamos el paquete a la capa de abajo
					 } catch (InterruptedException err) {
						System.err.println("ERROR! PASSING PACKET TO LAYER 1" + err);
					 }
				}
			}
		}
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
            System.out.print("Write MAC Address source: ");
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
            srcMac[i] = hex.byteValue();
        }

		
        
        
        //Broadcast for Destination Mac
        destMac = new byte[]{
            (byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff
        };
    }

	private boolean isValidMac(String mac_address) {
		Pattern p = Pattern.compile("^([\\dA-Fa-f]{2}[.:-]){5}([\\dA-Fa-f]{2})$");
		Matcher m = p.matcher(mac_address);
		return m.find();
	}

}
