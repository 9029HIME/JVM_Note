package src;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JMMTest {
    static int a = 1;
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            a = 2;
            System.out.println("我已经将a改为2了：" + a);
        }).start();

        while (a == 1) {
//            Thread.sleep(5); 会跳出循环
//            synchronized (JMMTest.class) {
//                // 会跳出循环
//            }
//            Lock lock = new ReentrantLock();
//            lock.lock();
//             // 会跳出循环
//            lock.unlock();
        }
        System.out.println("好了，我退出了");
    }
}
