package Players;

import IHM.IPlayer;

public class consolePlayer implements IPlayer {

    public consolePlayer(){};


    public String[] getMove(int size) {
        return new String[0];
    }

    @Override
    public char getColumn(int size) {
        return 'A';
    }

    @Override
    public int getRow(int size) {
        return 0;
    }
}
