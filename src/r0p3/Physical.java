package r0p3;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import jpcap.*;

public class Physical extends Layer {

    private JpcapCaptor captor;
    private int interfaceSelect = 0;
    private int num_of_bytes = 0;
    private boolean promisc;
    private int caputure_timeout = 0;

    public Physical(int num, boolean prmsc, int cap_time) {
        num_of_bytes        = num;
        promisc             = prmsc;
        caputure_timeout    = cap_time;
    }

    public Physical() {
        this(65535, false, 20);
    }
    
    @Override
    public void configuration() {
        Scanner scan = new Scanner(System.in);
        boolean done = false;
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        for (int i = 0; i < devices.length; i++)
            System.out.println(i + ": " + devices[i].name +
                    '(' + devices[i].description + ')');

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
                // scan = new Scanner(System.in);
                scan.nextLine();
            }
        } while (done == false);

        scan.close();

        try {
            captor = JpcapCaptor.openDevice(
                    devices[interfaceSelect],
                    num_of_bytes,
                    promisc,
                    caputure_timeout
            );
        } catch (IOException err) {
            System.err.println(err);
        }
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
