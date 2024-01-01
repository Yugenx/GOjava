package IHM;

import Players.RandomPlayer;
import Players.consolePlayer;
import logic.Board;
import logic.StoneColor;

import java.util.HashMap;
import java.util.Map;

import java.util.Arrays;
import java.util.Scanner;

public class consoleManager {

    private IBoard board;

    private Map<StoneColor, IPlayer> players;
    private StoneColor currentPlayerColor;

    private static final int MAX_BOARD_SIZE = 25;
    private static final int MIN_BOARD_SIZE = 1;



    private int passCount;

    public consoleManager() {
    }

    public StoneColor getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    public void setCurrentPlayerColor(StoneColor currentPlayerColor) {
        this.currentPlayerColor = currentPlayerColor;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public void addPassCount(int pass) {
        this.passCount = this.passCount + pass;
    }

    public IPlayer currentPlayer() {
        return players.get(getCurrentPlayerColor());
    }

    public void gameSession() throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        board = new Board(19);
        players = new HashMap<>();
        setPassCount(0);
        players.put(StoneColor.WHITE, new consolePlayer());
        players.put(StoneColor.BLACK, new consolePlayer());
        setCurrentPlayerColor(StoneColor.BLACK);
        String[] commande;

        while(!board.isFull() && getPassCount() != 2){
            System.out.println(getPassCount());
            if (!(currentPlayer() instanceof consolePlayer)){

                char columnAlea = currentPlayer().getColumn(board.getSize());
                int rowAlea = currentPlayer().getRow(board.getSize());
                if (board.isFree(columnAlea, rowAlea)) {
                    board.playMove(columnAlea,rowAlea,getCurrentPlayerColor().name());
                    System.out.println("Le joueur " + getCurrentPlayerColor().name() + " a joué le coup suivant : " + columnAlea+rowAlea);
                    endTurn();

                    showboard("");
                    Thread.sleep(1000);
                    continue;
                }
                continue;

            }
            commande = sc.nextLine().split(" ");
            String id;
            if(Arrays.asList(commande).contains("quit")){
                try{
                    int num = Integer.parseInt(commande[0]);
                    id = String.valueOf(num);
                }catch(NumberFormatException e){
                    id = "";
                }
                showOkMess(id, "le score final est : BLACK - WHITE : " + board.getFinalScore(StoneColor.BLACK) + " - " + board.getFinalScore(StoneColor.WHITE));
                sc.close();
                break;
            } else{
                actionGTP(commande);

            }
        }
        showOkMess("", "le score final est : BLACK - WHITE : " + board.getFinalScore(StoneColor.BLACK) + " - " + board.getFinalScore(StoneColor.WHITE));
        sc.close();
        return;
    }




    public void endTurn(){
        if (getCurrentPlayerColor().equals(StoneColor.BLACK)){
            setCurrentPlayerColor(StoneColor.WHITE);
        } else {
            setCurrentPlayerColor(StoneColor.BLACK);
        }
    }



    public void actionGTP(String[] c){
        String[] commande = c.clone();
        String id;
        try{
            int num = Integer.parseInt(c[0]);
            id = String.valueOf(num);
            commande = Arrays.copyOfRange(c,1,c.length);
        } catch(NumberFormatException e){
            id = "";
        }
        switch (commande[0]) {
            case "boardsize":
                boardsize(id, commande);
                break;
            case "showboard":
                showboard(id);
                break;
            case "clear_board":
                clearBoard(id);
                break;
            case "play":
                play(id, commande);
                break;
            case "player":
                player(id, commande);
                break;
            case "liberties":
                getLiberties(id, commande);
                break;
            case "pass":
                addPassCount(1);
                showOkMess(id, "le joueur " + getCurrentPlayerColor() + " a passé son tour");
                endTurn();
                break;
            default:
                showError(id, "unknow command");
        }
    }

    private void player(String id, String[] command) {
        if(command.length < 3) {
            showError(id, "invalid player command");
        }
        String color = command[1];
        String type = command[2];
        StoneColor colorS = StoneColor.getStoneColor(color);
        switch (type.toLowerCase()){
            case "random":
                players.put(colorS,new RandomPlayer());
                break;
            case "console":
                players.put(colorS,new consolePlayer());
                break;
            default:
                showError(id, "unknown player type");
        }
    }

    public void showboard(String id){
        showOkMess(id);
        show(board.toString());
    }

    public void clearBoard(String id){
        showOkMess(id);
        board.clear();
    }

    public void boardsize(String id, String[] c){
        if(c.length < 2) {
            showError(id, "boardsize not an integer");
            return;
        }
        try {
            int newSize = Integer.parseInt(c[1]);
            if (newSize <= MAX_BOARD_SIZE && newSize >= MIN_BOARD_SIZE){
                board.changeSize(newSize);
                showOkMess(id);
            } else { showError(id, "unacceptable size");}

        } catch(NumberFormatException e){
            showError(id, "boardsize not an integer");
            return;
        }
    }

    public void play(String id, String[] command){
        if(command.length < 3) {
            showError(id, "invalid color or coordinate");
            return;
        }
        try {
            String[] arg = Arrays.copyOfRange(command,1,command.length);
            String colorStr = arg[0].toUpperCase();
            try {
                StoneColor color = StoneColor.valueOf(colorStr);
            } catch (IllegalArgumentException e){
                showError(id, " invalid color or coordinate");
                return;
            }
            if (!colorStr.equals(getCurrentPlayerColor().name())){
                showError(id, " uncorrect color");
                return;
            }
            char column = arg[1].charAt(0);
            int row = Integer.parseInt(arg[1].substring(1));

            if (!board.verifyCoord(column, row)){
                showError(id, " invalid color or coordinate");
            } else if (!board.isFree(column,row)){
                showError(id, " illegal move");
            } else {

                board.playMove(column,row,colorStr);
                if (getPassCount() > 0){
                    addPassCount(-1);
                }
                endTurn();
                showOkMess(id);
            }
        } catch(NumberFormatException e){
            showError(id, " invalid color or coordinate");
        }

    }

    public void getLiberties(String id, String[] commande){
        if(commande.length < 2) {
            showError(id, "invalid coordinate");
            return;
        }
        char column = commande[1].charAt(0);
        int row = Integer.parseInt(commande[1].substring(1));

        if (!board.verifyCoord(column, row)){
            showError(id, " invalid coordinate");
        } else if (board.isFree(column,row)){
            showError(id, " there's no stone here");
        } else {
            int numberLib = board.getLiberties(column,row);
            System.out.println("la pierre en " + commande[1] + " a " + numberLib + " libertés");

        }
    }




    public void show(String s){
        System.out.println(s);
    }

    public void showOkMess(String id){
        System.out.println("=" + id);
    }
    public void showOkMess(String id, String mess){
        System.out.println("=" + id + " " +mess);
    }



    public void showError(String id, String s){
        System.out.println("?"+ id + " " + s);
    }


}
