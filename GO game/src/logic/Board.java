package logic;


import java.util.*;

import static logic.StoneColor.getOppositeColor;
import static logic.StoneColor.getStoneColor;

public class Board {

    int size;
    String[][] board;

    private final ArrayList<Intersection> intersections = new ArrayList<>();

    private final ArrayList<Group> groups = new ArrayList<>();

    private static final int MAX_BOARD_SIZE = 25;

    private Map<StoneColor, Integer> scoreMap;

    public ArrayList<Intersection> getIntersections() {
        return intersections;
    }



    public Board(int size) {
        this.size = size;
        createBoard();
    }

    /*public Board(int size, String coord){ pas complet à revoir
        this.size = size;
        createBoard();
        String[] moves = coord.split(" ");
        int cmpt = 0;
        for (String position : moves){
            int column = obtainColumn(position.charAt(0));
            int row = obtainLine(Integer.parseInt(position.substring(1)));
            cmpt++;
            if (cmpt%2 != 0){
                board[row][column]= StoneColor.BLACK.getSymbol();
                Intersection newI = new Intersection(row, column, StoneColor.BLACK);
                intersections.add(newI);
            } else {
                board[row][column]= StoneColor.WHITE.getSymbol();
                Intersection newI = new Intersection(row, column, StoneColor.WHITE);
                intersections.add(newI);
            }
        }
    }*/

    public int getSize() {
        return size;
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
        intersections.clear();
        groups.clear();
        scoreMap = new HashMap<>();
        scoreMap.put(StoneColor.WHITE, 0);
        scoreMap.put(StoneColor.BLACK, 0);

    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                b.append(board[i][j] + " ");
            }
            if (i == board.length - 2) {
                b.append("     WHITE (O) has captured " + scoreMap.get(StoneColor.WHITE) + "  stones");

            } else if (i == board.length - 1) {
                b.append("     BLACK (X) has captured " + scoreMap.get(StoneColor.BLACK) + " stones");
            }
            b.append("\n");
        }
        return b.toString();
    }

    public void changeScore(StoneColor color, int num) {
        int valeurActuelle = scoreMap.get(color);
        int newVal = valeurActuelle + num;
        scoreMap.put(color,newVal);
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
        intersections.clear();
        groups.clear();
    }

    public int obtainColumn(char letter){
        int compteur = 1;
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c =='I'){continue;}
            if (letter == c){
                return compteur;
            }
            compteur++;
        }
        return compteur;
    }

    public int obtainLine(int row){
        return (size + 1) - row;
    }

    public boolean isFree(char column, int row){
        int c = obtainColumn(column);
        int l = obtainLine(row);
        return board[l][c].equals(".");
    }

    public boolean verifyCoord(char column, int row) {
        int columnIndex = obtainColumn(column);
        if (columnIndex < 1 || columnIndex > size ) {
            return false;
        }

        int rowIndex = obtainLine(row);
        if (rowIndex < 1 || rowIndex > size) {
            return false;
        }

        return true;
    }

    public boolean verifyCoord(int column, int row) {
        if (column < 1 || column> size ) {
            return false;
        }

        if (row< 1 || row > size) {
            return false;
        }

        return true;
    }

    public void playMove(char column, int row, String color) {
        int columnIndex = obtainColumn(column);
        int rowIndex = obtainLine(row);
        StoneColor stoneColor = getStoneColor(color);
        board[rowIndex][columnIndex] = stoneColor.getSymbol();
        Intersection newIntersection = new Intersection(rowIndex, columnIndex, stoneColor);
        intersections.add(newIntersection);
        if (intersections.size() <= 1){
            return;
        } else{
            checkInpactNewStone(newIntersection);
        }
    }

    public void checkInpactNewStone(Intersection intersection){
        if (checkCapture(intersection)){
            return;
        } else {
            checkIsInGroup(intersection); //pour l'intégrer à un groupe déjà existant s'il faut ou en créer un nouveau
            checkIfCapturing(intersection);
        }

    }

    private boolean checkIfCapturing(Intersection intersection) { //sert à vérifier que la nouvelle pierre posée permet une capture ou non
        List<int[]> liberties = intersection.getCoordNeighbours();
        for (int[] coord : liberties) {
            for (Intersection stoneOnBoard : intersections) {
                if (Arrays.equals(stoneOnBoard.getCoordinates(), coord) && stoneOnBoard.getStoneColor() != intersection.getStoneColor()){
                    boolean adjacentIsInGroup = false;
                    for (Group group : groups) {
                        if(group.containsIntersection(stoneOnBoard.getCoordinates())){
                            return checkCaptureGroup(group);
                        }
                        /*List<int[]> groupCoor = group.getAllCoordinates();
                        int r = stoneOnBoard.getRow();
                        int c = stoneOnBoard.getColumn();
                        int row1;
                        int c1;
                        for (int i = 0; i < groupCoor.size(); i++){
                            row1 =groupCoor.get(i)[0];
                             c1 = groupCoor.get(i)[1];

                            if (row1 == r && c == c){
                                //System.out.println("je suis rentré dans la boucle - débug");
                                checkCaptureGroup(group);
                                adjacentIsInGroup = true;
                            }
                        }*/
                    }
                    if (!adjacentIsInGroup) {
                        return checkCapture(stoneOnBoard);
                    }
                }
            }

        }
        return false;
    }

    private boolean checkCaptureGroup(Group group) {
        List<int[]> liberties = group.getNeighboursCoordinates();
        //List<int[]> groupCoordinates = group.getAllCoordinates();
        boolean allOccupied = true;
        for (int[] coord : liberties) {
            int r = coord[0];
            int c = coord[1];
            boolean toDelete = false;
            if (!verifyCoord(c,r)) {
                toDelete = true;
            } else if (group.containsIntersection(coord)){
                toDelete = true;
            }
            if (toDelete){continue;}

            boolean isLibOccupied = false;

            for (Intersection stoneOnBoard : intersections) {
                if (Arrays.equals(stoneOnBoard.getCoordinates(), coord) && stoneOnBoard.getStoneColor() != group.getColor()) {
                    isLibOccupied = true;
                    break;
                }
            }
            if (!isLibOccupied){
                allOccupied = false;
                break;
            }
        }
        if (allOccupied){
            List<int[]> allCoordinates = group.getAllCoordinates();
            for (int[] coordGroup : allCoordinates){
                board[coordGroup[0]][coordGroup[1]] = ".";
                intersections.remove(getIntersectionAt(coordGroup[0],coordGroup[1]));
                changeScore(getOppositeColor(group.getColor()), 1);
            }
            groups.remove(group);
        }


        return allOccupied;
    }

    private void checkIsInGroup(Intersection intersection) {
        List<int[]> liberties = intersection.getCoordNeighbours();
        List<Group> matchingGroups = new ArrayList<>();

        for (int[] coord : liberties) {
            boolean foundNearGroup = false;
            for (Group group : groups) {
                Intersection stoneInGroup = group.getStoneAt(coord);
                if (stoneInGroup != null && stoneInGroup.getStoneColor() == intersection.getStoneColor()) {
                    matchingGroups.add(group);
                    foundNearGroup = true;
                    break;
                }
            }
            if (!foundNearGroup) {
                Intersection adjacentIntersection = getIntersectionAt(coord[0], coord[1]);
                if (adjacentIntersection != null && adjacentIntersection.getStoneColor() == intersection.getStoneColor()) {
                    Group newGroup = new Group(intersection.getStoneColor());
                    newGroup.addStone(intersection);
                    newGroup.addStone(adjacentIntersection);
                    groups.add(newGroup);
                }
            }
        }
        if (matchingGroups.size() > 1) {
            Group mergedGroup = new Group(intersection.getStoneColor());
            for (Group group : matchingGroups) {
                mergedGroup.merge(group);
                groups.remove(group);
            }
            mergedGroup.addStone(intersection);
            groups.add(mergedGroup);
        } else if (matchingGroups.size() == 1) {
            matchingGroups.getFirst().addStone(intersection);
        }

    }


    private boolean checkCapture(Intersection intersection) {
        List<int[]> liberties = intersection.getCoordNeighbours();
       boolean allOccupied = true;
        for (int[] coordLib : liberties) {
            //pour sauter les coordonnées correspondant aux lignes et colonnes du tableau contenant les num de lignes et colonnes
            if (!verifyCoord(coordLib[0], coordLib[1])){
                continue;
            }
            boolean isLibOccupied = false;
            for (Intersection stoneOnBoard : intersections) {
                if (Arrays.equals(stoneOnBoard.getCoordinates(), coordLib)){
                    if (stoneOnBoard.getStoneColor() != intersection.getStoneColor()){
                        isLibOccupied = true;
                        break;
                    }
                }
            }
            if (!isLibOccupied){
                allOccupied = false;
                break;
            }
        }
        if (allOccupied) {
            int[] coord = intersection.getCoordinates();
            board[coord[0]][coord[1]]= ".";
            intersections.remove(intersection);
            changeScore(getOppositeColor(intersection.getStoneColor()), 1);
            return true;
        }
        else {return false;}
    }

    public Intersection getIntersectionAt(int row, int column) {
        for (Intersection intersection : intersections) {
            if (intersection.getRow() == row && intersection.getColumn() == column) {
                return intersection;
            }
        }
        return null;
    }


    public boolean isFull() {
        boolean b = true;
        int lastIndex = board.length - 1;
        for (int i = 1; i < lastIndex; i++) {
            for (int j = 1; j < lastIndex; j++) {
                if (board[i][j] == ".") {
                    b = false;
                }
            }
        }
        return b;
    }
}