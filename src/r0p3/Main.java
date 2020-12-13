package r0p3;

import r0p3.layers.*;
// import r0p3.protocols.*;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        // System.setProperty("java.library.path", (new java.io.File("../")).toString());
        // System.setProperty("java.library.path", "./lib/");
        // System.out.println(System.getProperty("java.library.path"));

        try {
       		Layer l1, l2, l3;
	   		l1 = new Physical();
	   		l2 = new Logical();
			l3 = new Network();

       		// -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
	   		l1.setUp(l2);

	   		l2.setDown(l1);
			l2.setUp(l3);

	   		l3.setDown(l2);
	   		// l3.setUp(l4);

	   		// l4.setDown(l3);
	   		// l4.setUp(l5);
	   		
	   		// l5.setDown(l4);


       		// -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
       		l1.configuration();
       		l2.configuration();
       		// l3.configuration();
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

            Thread.sleep(5000);
			l1.setFinish(true);

			Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
			for(Thread t : threadSet)
				System.out.println("THREAD -- " + t);
        } catch (Exception err) {}

    }

}
