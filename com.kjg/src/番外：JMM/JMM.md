# JMM和JVM

结合之前学的JVM可以了解到，JVM规范将Java内存划分成了多个区域，那么JMM又是什么呢？JMM虽然叫Java Memory Model，但本质和JVM的内存划分策略不一样，而是规定**多个线程操作JVM内存的策略与规范**。

JMM对于内存的定义有两种：主内存与工作内存。主内存即JVM对于多个线程的共享内存。
主内存：可以理解为堆内存与堆外内存（元空间），即多个线程共同操作的区域。
工作内存：存储线程执行的当前方法的所有本地变量信息，这个本地变量信息是主内存中的变量副本拷贝。每个线程只能访问自己的工作内存，线程对自己工作内存的修改对其他线程是不可见的。对于多个线程来说，他们之间的通讯还是依赖于主内存。

# JMM概述

JMM规定了所有线程在操作主内存变量时，先把主内存变量拷贝到自己的工作内存，然后操作工作内存里的变量，不能碰主内存的。如果操作过程中涉及到变量的更改，则操作完成后会将工作内存的变量刷写回主内存。

这里就引出一个问题：当时在JVM学习过程中可以知道，如果操作的是引用类型数据的话，线程的虚拟机栈的局部变量表里存储的是引用数据的指针，那么和JMM的说法就冲突了？

实际上，虽然存的是指针，但实际操作这个对象的时候线程会通过指针找到主存里的对象，然后将对象拷贝一份副本到自己的工作内存里。不过当操作的对象大小＞1MB时，线程只会拷贝自己要操作的那一部分。

# 指令重排与serial语义

CPU执行机器指令时，为了提高程序运行效率，会进行指令重排序，指令重排后线程所执行的指令不一定是按顺序的，有可能在代码中编写指令1 → 指令2，但CPU在执行过程中先执行指令2，再执行指令1。**虽然如此，指令重排是能保证代码最终执行结果和按顺序执行的结果是一致的，这个原则也被成为as-if-serial原则。**

# 可见性

上面已经说到，线程在操作变量的之前会复制变量到自己的工作内存，那在自己的工作内存操作变量后，变量被修改的结果能否被其他线程感知到呢？如果能，则说明这个变量是可见的，反之是不可见。

# volatile如何避免指令重排与保证可见性

![00cda68dc077a6b95ddfb36514b8b1a](https://user-images.githubusercontent.com/48977889/167879341-b9071859-58b8-4a4c-9efc-147916538842.jpg)

对volatile变量读操作前面加一个Load屏障，Load屏障能从主内存中刷新这个变量到工作内内存。对volatile变量写操作加一个Store屏障，Store屏障能强制将工作内存的这个变量刷新到主内存。这样多个线程共同操作一份volatile变量，之前的操作就变得可见了。与此同时，屏障还有禁止指令重排的作用，它能确保屏障前和屏障后的指令不会重排到屏障内，屏障内的指令也不会重排到屏障外，并且屏障内的指令也是严格按照顺序执行的。当然，屏障外的指令重排是不管的，只针对volatile变量操作的指令。

# 没有volatile，一定会出现可见性问题嘛？

即使没有volatile，JVM也会尽量保证可见性。JMM规定了sychronized和Unsafe.park加锁前会从主内存拿到最新值，刷新到工作内存，解锁前会从工作内存的值刷新到主内存。

而线程sleep时是阻塞状态，时间结束时变为可运行状态，获得cpu运行权限时从主内存中读取数据，读取到更新后的值跳出循环结束线程。以下是demo代码：

```java
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
```



# volatile可不保证原子性

volatile仅仅是保证了可见性和有序性，但不包含原子性，也就是说一个时间段内，1个volatile变量还是会别多个线程共同操作。打个比方，如果线程A对volatile变量i进行了+1处理，但是线程A将i刷新回主内存之前，线程B已经拿到了i的旧值，并且做出了修改，即使线程A已经刷新到主内存，线程B还是会再往主内存刷一遍，出现数据不同步的问题：

|          thread1           | 主内存 | thread2                    |
| :------------------------: | :----: | -------------------------- |
| 从主内存拿到volatile i = 0 |  i=0   | 从主内存拿到volatile i = 0 |
| 在工作内存将i++（操作中）  |        |                            |
| 在工作内存将i++（操作中）  |        | 在工作内存将i++（操作中）  |
|                            |        | 在工作内存将i++（操作中）  |
|      刷新i=1到主内存       |  i=1   |                            |
|                            |        | 刷新i=1到主内存            |
|                            |  i=1   |                            |

期望结果是i=2，结果i却还是=1。

# happens-before

JMM针对可见性定义了happens-before原则，如果A操作happens-before操作B，那么A操作对于B操作是可见的。换句话说，只要遵循happens-before原则，所写的代码就不用关心因为指令重排而引起的可见性问题：

1. 单线程规则：同一个线程中的每个操作都happens-before于出现在其后的任何一个操作。
2.  对一个监视器的解锁操作happens-before于每一个后续对同一个监视器的加锁操作。
3. 对volatile字段的写入操作happens-before于每一个后续的对同一个volatile字段的读操作。
4. Thread.start()的调用操作会happens-before于启动线程里面的操作。
5. 一个线程中的所有操作都happens-before于其他线程成功返回在该线程上的join()调用后的所有操作。
6. 一个对象构造函数的结束操作happens-before与该对象的finalizer的开始操作。
7. 传递性规则：如果A操作happens-before于B操作，而B操作happens-before与C操作，那么A动作happens-before于C操作。

# 为什么单例模式有synchonized了，还要volatile？

就拿以下经典代码为例：

```java
public class SingletonTest {
    private SingletonTest(){}
    private static volatile SingletonTest singleton;
    public static SingletonTest newInstance(){
        if(singleton==null){
            //1
            synchronized(SingletonTest.class){
                //2
                if(singleton==null){

                    singleton=new SingletonTest();
                }
            }
        }
        return singleton;//3
    }
}
```

