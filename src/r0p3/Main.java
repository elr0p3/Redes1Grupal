package r0p3;

import r0p3.layers.*;
import r0p3.protocols.*;

import java.util.Scanner;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		// System.setProperty("java.library.path", (new java.io.File("../")).toString());
		// System.setProperty("java.library.path", "./lib/");
		// System.out.println(System.getProperty("java.library.path"));
		
		try {
			Physical l1	= new Physical();
			Logical l2	= new Logical();
			Network l3	= new Network();

			Arp arp	= new Arp(l3);
			Ip ip	= new Ip(l3);
		
			// -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
			l1.setUp(l2);
		
			l2.setDown(l1);
			l2.setUp(l3);
		
			l3.setDown(l2);
			l3.setProtocols(arp, ip);
			// l3.setUp(l4);
		
			// l4.setDown(l3);
			// l4.setUp(l5);
			
			// l5.setDown(l4);
		
		
			// -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
			l1.configuration();
			l2.configuration();
			l3.configuration();
			// l4.configuration();
			// l5.configuration();
		
		
			// -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
			l1.start();
			l2.start();
			l3.start();
			// l4.start();
			// l5.start();
		
			// while(l2.isAlive()){Thread.sleep(1);}
			// System.out.println("SAMATAOPACO");
		
			menu(arp);

			// Thread.sleep(5000);
			l1.setFinish(true);
		
			Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
			for(Thread t : threadSet)
				System.out.println("THREAD -- " + t);
		} catch (Exception err) {}

    }

	public static void menu(Arp arp) {
        System.out.println("Welcome to Covid Apocalipsis");

        Scanner sn = new Scanner(System.in);
		boolean exit = false;
        int option; //Guardaremos la opcion del usuario

		while (!exit) {
			System.out.println("1. Check MAC");
			System.out.println("2. Exit");

			System.out.print("Select the option with a value: ");
			System.out.flush();
			option = sn.nextInt();

			switch (option) {
				case 1:
					System.out.println("Let's check this mac");
					// Meter Toda la funcion de arpRequest
					// send arp request
					arp.requestIP();
					break;
				case 2:
					System.out.println("A por papel higienico");
					exit = true;
					break;
				default:
					System.out.println("Number out of range");
			}
		}
	}

}
