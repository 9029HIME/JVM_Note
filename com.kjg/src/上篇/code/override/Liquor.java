package src.上篇.code.override;

public class Liquor {
    public void drink(){
        System.out.println("drink normal liquor");
    }

    public static void main(String[] args) {
        Liquor liquor = new Beer();
        liquor.drink();
    }

    public static void invoke(Liquor liquor){
        liquor.drink();
    }

}

class Beer extends Liquor{

    public void drink(){
        System.out.println("Nice Beer!!!!");
    }
}

