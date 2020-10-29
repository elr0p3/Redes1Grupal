package r0p3;

public class Main {

    public static void main(String[] args) {
        // System.setProperty("java.library.path", (new java.io.File("../")).toString());
        // System.setProperty("java.library.path", "./lib/");
        // System.out.println(System.getProperty("java.library.path"));

        Layer l1 = new Physical(65535, false, 20, 10);
        l1.run();

    }

}
