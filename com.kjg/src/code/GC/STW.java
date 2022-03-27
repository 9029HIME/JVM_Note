package src.code.GC;

import java.util.LinkedList;
import java.util.List;

public class STW {
    public static void main(String[] args) {
        List<byte[]> workLine = new LinkedList<>();

        Thread worker = new Thread(()->{
            while (true) {
                byte[] buffer = new byte[1024];
                workLine.add(buffer);

                if(workLine.size() > 10000){
                    workLine.clear();
                    System.gc();//会触发full gc，进而会出现STW事件
                }
            }
        });

        long startTime = System.currentTimeMillis();
        Thread printer = new Thread(()->{
            while (true) {
                // 每秒打印时间信息
                long t = System.currentTimeMillis() - startTime;
                System.out.println(t / 1000 + "." + t % 1000);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        worker.start();
        printer.start();
    }
}
