package src.上篇.code;

class Father {

}

public class zeroTwo extends Father{
    private int a = -1;

    public zeroTwo(){
        a = 100;
    }


    static {
        str = "def";
//        System.out.println(str);
    }

    public static String str = "abc";


    public static void main(String[] args) {
        System.out.println(str);

        // test clinit lock
        new Thread(()->{
            String name = Thread.currentThread().getName();
            System.out.println(String.format("%s start clinit",name));
            LockClass lockClass = new LockClass();
            System.out.println(String.format("%s end clinit",name));
        }).start();

        new Thread(()->{
            String name = Thread.currentThread().getName();
            System.out.println(String.format("%s start clinit",name));
            LockClass lockClass = new LockClass();
            System.out.println(String.format("%s end clinit",name));
        }).start();


    }
}

class LockClass{

    public static  boolean lock = false;

    static{
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("%s线程正在执行LockClass的clinit()",threadName));
        if(lock) {
            while (true) {

            }
        }
    }
}
