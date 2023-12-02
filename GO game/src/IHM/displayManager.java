package IHM;

public class displayManager {


    public void show(String s){
        System.out.println(s);
    }

    public void showOkMess(String id){
        System.out.println("=" + id);
    }



    public void showError(String id, String s){
        System.out.println("?"+ id + " " + s);
    }


}
