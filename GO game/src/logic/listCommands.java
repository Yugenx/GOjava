package logic;

import java.util.HashMap;
import java.util.Map;


public class listCommands {
    private Map<Integer, String> histo;

    public listCommands() {
        this.histo = new HashMap<>();
    }

    public void inject (int id, String command){
        histo.put(id,command);
    }

        //on pourra rajouter dans cette classe les fonctions qui correspondent aux commandes "list_commands" et known_command"
}


