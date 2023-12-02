package IHM;

import logic.CommandsManager;
import logic.Game;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        displayManager display = new displayManager();
        CommandsManager commandsManager = new CommandsManager();

        Game g1 = new Game(display, commandsManager);
        g1.gameSession();

    }
}
