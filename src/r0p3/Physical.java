package r0p3;
import java.util.InputMismatchException;
import java.util.Scanner;
import jpcap.*;

public class Physical extends Layer {

    private int interfaceSelect = 0;
    
    @Override
    public void configuration() {
        Scanner scan = new Scanner(System.in);
        boolean done = false;

        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        for (int i = 0; i < devices.length; i++)
            System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");

        do {
          try {
				System.out.print("Introduce the interface to use: ");
                System.out.flush();
				interfaceSelect = scan.nextInt();
                if (0 <= interfaceSelect && interfaceSelect < devices.length)
                    done = true;
                else
                    System.err.println("ERROR! Number out of range");
            } catch (InputMismatchException e) {
		        System.err.println("ERROR! Invalid integer input");
                scan = new Scanner(System.in);
		    }
        } while (done == false);

        scan.close();
    }

    @Override
    public void run() {
        configuration();
    }
}


/*
        //Obtain the list of network interfaces
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        //for each network interface
        for (int i = 0; i < devices.length; i++) {
            //print out its name and description
            System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");
        
            //print out its datalink name and description
            System.out.println(" datalink: "+devices[i].datalink_name + "(" + devices[i].datalink_description+")");
        
            //print out its MAC address
            System.out.print(" MAC address:");
            for (byte b : devices[i].mac_address)
              System.out.print(Integer.toHexString(b&0xff) + ":");
            System.out.println();
        
            //print out its IP address, subnet mask and broadcast address
            for (NetworkInterfaceAddress a : devices[i].addresses)
                System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
        }
*/
