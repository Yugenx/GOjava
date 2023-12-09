package logic;


import java.util.ArrayList;
import java.util.List;

public class Board {

    int size;
    String[][] board;

    private static final int MAX_BOARD_SIZE = 25;


    public Board(int size) {
        this.size = size;
        createBoard();
    }

    public void createBoard() {
        int trueSize = size + 2; //+2 pour compter les deux lignes et colonnes servant à mettre les coordonnéees

        board = new String[trueSize][trueSize];
        int lastIndex = board.length - 1;
        createCoord();

        for (int i = 1; i < lastIndex; i++) {
            for (int j = 1; j < lastIndex; j++) {
                board[i][j] = ".";
            }
        }
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                b.append(board[i][j] + " ");
            }
            if (i == board.length - 2) {
                b.append("     WHITE (O) has captured 0 stones");

            } else if (i == board.length - 1) {
                b.append("     BLACK (O) has captured 0 stones");
            }
            b.append("\n");
        }
        return b.toString();
    }


    public void changeSize(int newSize) {
        this.size = newSize;
        createBoard();

    }

    public void createCoord() {

        int lastIndex = board.length - 1;

        List<Character> alphabetList = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'I') {
                continue;
            }
            alphabetList.add(c);
        }
        //numérotation des colonnes

        for (int i = 1; i < lastIndex; i++) {
            board[0][i] = String.valueOf(alphabetList.get(i - 1));
            board[lastIndex][i] = String.valueOf(alphabetList.get(i - 1));
        }
        //numérotation des lignes

        for (int i = 1; i < lastIndex; i++) {
            if (i < 10) {
                board[lastIndex - i][0] = String.valueOf(i) + " ";
            } else {
                board[lastIndex - i][0] = String.valueOf(i);
            }
            board[lastIndex - i][lastIndex] = String.valueOf(i);
        }
        //coins vides
        board[0][0] = board[lastIndex][lastIndex] = board[0][lastIndex] = board[lastIndex][0] = "  ";
    }


    public void clear() {
        int lastIndex = board.length - 1;
        for (int i = 1; i < lastIndex; i++) {
            for (int j = 1; j < lastIndex; j++) {
                board[i][j] = ".";
            }
        }
        System.out.println("ça marche tqt");
    }

    public int obtainColumn(String letter){
        int compteur = 1;
        for (char c = 'A'; c <= 'Z'; c++) {
            if (letter.charAt(0) == c){
                return compteur;
            }
            compteur++;
        }
        return compteur;
    }

    public int obtainLine(String number){
        for (int i = 1; i < (size - 1); i++ ){
                if (Integer.parseInt(number) == (size-1) - i){
                    return (size-1) - i;
                }
        }
        return -1;
    }

    public boolean isFree(String[] coord){
        int c = obtainColumn(coord[0]);
        int l = obtainLine(coord[1]);
        return board[l][c].equals(".");
    }
}