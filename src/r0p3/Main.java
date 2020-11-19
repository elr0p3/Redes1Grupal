package r0p3;

public class Main {

    public static void main(String[] args) {
        // System.setProperty("java.library.path", (new java.io.File("../")).toString());
        // System.setProperty("java.library.path", "./lib/");
        // System.out.println(System.getProperty("java.library.path"));

		System.out.println("HOLA TONTO");
        Layer l1, l2;
		l1 = new Physical();
		l2 = new Logical();

        // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
		l1.setUp(l2);

		l2.setDown(l1);
		// l2.setUp(l3);

		// l3.setDown(l2);
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
        // l3.start();
        // l4.start();
        // l5.start();

    }

}
