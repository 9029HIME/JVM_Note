package src.code;

public class LocalVariables {
    public static void main(String[] args) {
        int i = 123;
        LocalVariables localVariables = new LocalVariables();
        double d = (double) 23.2342;
        int j = 456;
        localVariables.testThis(999);
    }

    public void testThis(int x){

    }

    public void testSlotCircle(){
        int a = 123;
        {
            int b = a + 1;
            System.out.println(b);
        }
        int c = a + 2;
    }
}
