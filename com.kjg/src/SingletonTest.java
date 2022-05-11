package src;

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
