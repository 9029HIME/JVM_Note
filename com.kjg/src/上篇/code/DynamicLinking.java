package src.上篇.code;

public class DynamicLinking {

    int num = 10;

    public void A(){
        B();
    }

    public void B(){
        System.out.println(num);
    }

    public static void main(String[] args) {

    }

}


