package src.code.heap;

import java.util.ArrayList;
import java.util.List;

public class HeapTest {
    public static HeapTest test = new HeapTest();
    public static void main(String[] args) throws InterruptedException {
        List list = new ArrayList<>();
        list.add(test);
        Thread.sleep(1000000);
    }
}
