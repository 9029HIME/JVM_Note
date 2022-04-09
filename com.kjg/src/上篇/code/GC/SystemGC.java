package src.上篇.code.GC;

public class SystemGC {
    public static void main(String[] args) throws InterruptedException {
        new SystemGC();
        System.gc();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("SystemGCTest 重写了finalize()");
    }
}
