package src.code.heap;


/**
 * 标量替换
 */
public class BLTH {


    public static void main(String[] args) {
        A();
    }

    public static void  A(){
        BL bl = new BL(1,2);
        System.out.println(bl.a);
        System.out.println(bl.b);
    }
}

class BL{
    public int a;
    public int b;

    public BL(int a, int b) {
        this.a = a;
        this.b = b;
    }
}