package r0p3;

public abstract class Layer extends Thread {
    
    public abstract void configuration();

    @Override
    public abstract void run();
}
