package logic;


import java.util.ArrayList;
import java.util.List;

public class Board {

    int size;
    String[][] board;



    public Board(int size){
        this.size = size;
        createBoard();
    }

    public void createBoard(){
        int trueSize = size+2; //+2 pour compter les deux lignes et colonnes servant à mettre les coordonnéees

        board = new String[trueSize][trueSize];
        int lastIndex = board.length - 1;
        createCoord();

        for (int i=1; i < lastIndex; i++ ){
            for (int j = 1; j < lastIndex;j++){
                board[i][j] = ".";
            }
        }
    }
    public void showBoard(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            if (i == board.length - 2){
                System.out.print("     WHITE (O) has captured 0 stones");

            } else if (i == board.length - 1) {
                System.out.print("    BLACK (O) has captured 0 stones");
            }
            System.out.println();
        }
    }




    public void changeSize(int newSize){
        this.size = newSize;
        createBoard();

    }

    public void createCoord(){

        int lastIndex = board.length - 1;

        List<Character> alphabetList = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'I') { continue;}
            alphabetList.add(c);
        }
        //numérotation des colonnes

        for (int i = 1; i < lastIndex; i++){
            board[0][i] = String.valueOf(alphabetList.get(i-1));
            board[lastIndex][i] = String.valueOf(alphabetList.get(i-1));
        }
        //numérotation des lignes

        for (int i = 1; i < lastIndex; i++){
            if (i < 10){
                board[lastIndex - i][0]= String.valueOf(i) + " ";
            } else {
                board[lastIndex - i][0]= String.valueOf(i);
            }
            board[lastIndex - i][lastIndex] = String.valueOf(i);
        }
        //coins vides
        board[0][0] = board[lastIndex][lastIndex] = board[0][lastIndex] = board[lastIndex][0] = "  ";
    }
}
