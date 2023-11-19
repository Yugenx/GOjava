package logic;
import java.util.Scanner;
public class Game  {
    private Board board;
    //attribut pour le nouveau plateau
    private Scanner sc;
    public void gameSession() {
        sc = new Scanner(System.in);
        board = new Board(19);
        String commande;
        do {
            commande = sc.next();
            if (commande.equals("showboard")){
                System.out.println("=");
                board.showBoard();
            }
            else if (commande.equals("boardsize")){
                int newSize = sc.nextInt();
                System.out.println("=");
                board.changeSize(newSize);
            }

        } while (!commande.equals("quit"));
        sc.close();
        System.out.println("=");
        //sortie de l'interface

    }

    public static void main(String[] args) {
        Game g1 = new Game();
        g1.gameSession();

    }
}

