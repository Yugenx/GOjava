package Players;

import IHM.IPlayer;

import java.util.Random;

public class RandomPlayer implements IPlayer {
    @Override/*
    public String[] getMove(int size) {
        String[] coord = new String[2];
        coord[0] = String.valueOf(getColumnAlea(size));
        coord[1] = String.valueOf(getLineAlea(size));
        return coord;
    }*/

    public char getColumn(int size) {
        Random random = new Random();
        char lettre = 'I';
        while (lettre == 'I'){
            lettre = (char) ('A' + random.nextInt(size));
        }
        return lettre;
    }

    public int getRow(int size) {
        Random random = new Random();
        int row = random.nextInt(1,size + 1);
        return row;
    }
}
