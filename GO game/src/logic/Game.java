package logic;
import IHM.displayManager;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
public class Game {
    private Board board;
    private displayManager display;
    private Scanner sc;
    private CommandsManager cmds;



    private static final int MAX_BOARD_SIZE = 25;
    private static final int MIN_BOARD_SIZE = 1;

    public Game(displayManager display, CommandsManager commandsManager) {
        this.display = display;
        this.cmds = commandsManager;
    }
    public void gameSession() {
        Scanner sc = new Scanner(System.in);
        board = new Board(19);


        while(true){
            String[] commande = sc.nextLine().split(" ");
            String id;
            if(Arrays.asList(commande).contains("quit")){
                try{
                    int num = Integer.parseInt(commande[0]);
                    id = String.valueOf(num);
                }catch(NumberFormatException e){
                    id = "";
                }
                display.showOkMess(id);
                sc.close();
                break;
            } else{
                actionGTP(commande);
            }
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
            default:
                display.showError(id, "unknow command");
        }


    }

    public void showboard(String id){
        display.showOkMess(id);
        display.show(board.toString());
    }

    public void clearBoard(String id){
        display.showOkMess(id);
        board.clear();
    }

    public void boardsize(String id, String[] c){
        if(c.length < 2) {
            display.showError(id, "boardsize not an integer");
            return;
        }
        try {
            int newSize = Integer.parseInt(c[1]);
            if (newSize <= MAX_BOARD_SIZE && newSize >= MIN_BOARD_SIZE){
                board.changeSize(newSize);
                display.showOkMess(id);
            } else { display.showError(id, "unacceptable size");}

        } catch(NumberFormatException e){
            display.showError(id, "boardsize not an integer");
            return;
        }
    }

    public void play(String id, String[] command){
        if(command.length < 3) {
            display.showError(id, "invalid color or coordinate");
            return;
        }
        try {
            String[] arg = Arrays.copyOfRange(command,1,command.length);
            String colorStr = arg[0];
            try {
                StoneColor color = StoneColor.valueOf(colorStr.toUpperCase());
            } catch (IllegalArgumentException e){
                display.showError(id, " invalid color or coordinate");
                return;
            }
            StoneColor color = StoneColor.valueOf(colorStr.toUpperCase());
            char column = arg[1].charAt(0);
            int row = Integer.parseInt(arg[1].substring(1));

            if (!board.verifyCoord(column, row)){
                display.showError(id, " invalid color or coordinate");
                return;
            } else if (!board.isFree(column,row)){
                display.showError(id, " illegal move");
                return;
            } else {

                board.playMove(column,row,colorStr);

                display.showOkMess(id);
            }
        } catch(NumberFormatException e){
            display.showError(id, " invalid color or coordinate");
            return;
        }



    }



}

