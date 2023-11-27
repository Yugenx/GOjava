package logic;
import java.util.Scanner;
public class Game {
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
                if (!cmds.verifValidity(commande)) { //à mettre dans une fonction
                    System.out.println("?" + id + " invalid command");
                } else {
                    cmds.injectHisto(id, commande);
                    if (commande.equals("showboard")) {
                        System.out.println("=" + id);
                        board.showBoard();
                    } else if (commande.equals("boardsize")) {
                        if (sc.hasNextInt()) {
                            boardsizeManip(id);
                        } else if (!sc.hasNextInt()) {
                            System.out.println("? boardsize not an integer");
                            sc.next();
                        }
                    } else if (commande.equals("quit")) {
                        sc.close();
                        System.out.println("=" + id);
                        break;
                    }

                }

            } else {
                commande = sc.next();
                if (!cmds.verifValidity(commande)) {
                    System.out.println("? invalid command");
                } else {
                    if (commande.equals("showboard")) {
                        System.out.println("=");
                        board.showBoard();
                    } else if (commande.equals("boardsize")) {
                        if (sc.hasNextInt()) {
                            boardsizeManip();
                        } else if (!sc.hasNextInt()) {
                            System.out.println("? boardsize not an integer");
                        }
                    } else if (commande.equals("quit")) {
                        sc.close();
                        System.out.println("=");
                        break;
                    }
                }
            }
        } while (true);

        sc.close();
        //sortie de l'interface
    }


    public void boardsizeManip(int id) {

        int newSize = sc.nextInt();
        if (newSize > 25) {
            System.out.println("? unacceptable size");
        } else {
            board.changeSize(newSize);
            System.out.println("=" + id);
        }
    }



    public void boardsizeManip(){

            int newSize = sc.nextInt();
            if (newSize > 25) {
                System.out.println("? unacceptable size");
            } else {
                board.changeSize(newSize);
                System.out.println("=");
            }
    }



    public static void main(String[] args) {
        Game g1 = new Game();
        g1.gameSession();

    }
}

