package src.上篇.code;

public class JvmStack {
    public static void main(String[] args) {
        JvmStack jvmStack = new JvmStack();
        jvmStack.methodA();
    }

    public void methodA(){
        int i = 10;
        int j = 20;
    }

    public void methodB(){
        int k = 30;
    }
}
