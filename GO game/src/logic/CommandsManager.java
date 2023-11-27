package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommandsManager {
    private Map<Integer, String> histo;

    private ArrayList<String> list;

    public CommandsManager() {

        this.histo = new HashMap<>();
        this.list = new ArrayList<>();
        initList();
    }

    public void injectHisto(int id, String command){
        histo.put(id,command);
    }

    public void initList(){
        list.add("showboard");
        list.add("boardsize");
        list.add("quit");
        list.add("list_commands");
    }

    public void showListCommands(){
        for (String command : list) {
            System.out.println(command);
        }

    }

    public boolean verifValidity(String s){
        return list.contains(s);
    }


    //on pourra rajouter dans cette classe les fonctions qui correspondent aux commandes "list_commands" et known_command"
}


