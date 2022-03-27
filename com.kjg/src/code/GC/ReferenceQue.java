package src.code.GC;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class ReferenceQue {
    static ReferenceQueue<PendingRemove> queue = new ReferenceQueue<>();//引用队列
    private WeakReference<PendingRemove> ref;
    static PendingRemove remove = new PendingRemove();

    public static void main(String[] args) throws InterruptedException {
        ReferenceQue que = new ReferenceQue();
        // 弱引用赋值，指定引用队列
        que.ref = new WeakReference<PendingRemove>(remove,queue);

        Thread trigger = new Thread(()->{
            // 去掉局部变量表的强引用，此时只有弱引用指向remove对象
            try {
                Thread.sleep(5000);
                System.out.println("======================trigger准备干预======================");
                remove = null;
                System.gc();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        trigger.start();


        Thread watcher = new Thread(()->{
            while(true){
                try {
                    Thread.sleep(1000);
                    System.out.println("watcher正在观察");

                    Reference<PendingRemove> inQueueRef = (Reference<PendingRemove>) queue.poll();
                    if(inQueueRef != null && inQueueRef.equals(que.ref)) {
                        que.ref = null;
                        System.out.println(String.format("hashCode = %s的ReferenceQue对象的弱引用指向已被回收",que.hashCode()));
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        watcher.start();
    }
}


class PendingRemove{
    @Override
    protected void finalize() throws Throwable {
        System.out.println("======================我要走啦======================");
    }
}
