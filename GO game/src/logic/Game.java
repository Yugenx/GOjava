package logic;
import java.util.Scanner;
public class Game  {
    private Board board;
    //attribut pour le nouveau plateau
    private Scanner sc;

    private CommandsManager cmds;
    public void gameSession() {
        sc = new Scanner(System.in);
        board = new Board(19);
        cmds = new CommandsManager();

        String commande;
        do {
            if (sc.hasNextInt()) {
                int id = sc.nextInt();
                commande = sc.next();
                if (!cmds.verifValidity(commande)) {
                    System.out.println("? invalid command");
                } else {
                    cmds.injectHisto(id, commande);
                    System.out.println("=" + id);
                }

            } else {
                commande = sc.next();
                if (!cmds.verifValidity(commande)) {
                    System.out.println("? invalid command");
                } else {System.out.println("=");}
            }

            if (commande.equals("showboard")) {
                board.showBoard();
            } else if (commande.equals("boardsize")) {
                    int newSize = sc.nextInt();
                    board.changeSize(newSize);
            }




        } while (!commande.equals("quit")) ;
        sc.close();
        //sortie de l'interface
    }



    public static void main(String[] args) {
        Game g1 = new Game();
        g1.gameSession();

    }
}

