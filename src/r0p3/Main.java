package r0p3;
import jpcap.*;

public class Main {

    public static void main(String[] args) {
        // System.setProperty("java.library.path", (new java.io.File("../")).toString());
        // System.setProperty("java.library.path", "./lib/");
        // System.out.println(System.getProperty("java.library.path"));

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
    }

}
