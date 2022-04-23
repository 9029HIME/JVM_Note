package src.下篇.code;

public class DeadLock {
    public static void main(String[] args) throws InterruptedException {
        DeadLock lock1 = new DeadLock();
        DeadLock lock2 = new DeadLock();


        new Thread(() -> {
            synchronized (lock1) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("拿了锁1，准备拿锁2");


                synchronized (lock2){
                    System.out.println("成功拿到锁2了");
                }
            }
        }).start();


        new Thread(() -> {
            synchronized (lock2) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("拿了锁2，准备拿锁1");


                synchronized (lock1){
                    System.out.println("成功拿到锁1了");
                }
            }
        }).start();

    }
}
