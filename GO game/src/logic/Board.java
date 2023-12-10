package logic;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static logic.StoneColor.getStoneColor;

public class Board {

    int size;
    String[][] board;

    private ArrayList<Intersection> intersections = new ArrayList<>();

    private ArrayList<Group> groups = new ArrayList<>();

    private static final int MAX_BOARD_SIZE = 25;

    public ArrayList<Intersection> getIntersections() {
        return intersections;
    }

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
        intersections.clear();


    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                b.append(board[i][j] + " ");
            }
            if (i == board.length - 2) {
                b.append("     WHITE (O) has captured " + getCapturedStone("white") + "  stones");

            } else if (i == board.length - 1) {
                b.append("     BLACK (X) has captured " + getCapturedStone("black") + " stones");
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
        intersections.clear();
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

    public void playMove(char column, int row, String color) {
        int columnIndex = obtainColumn(column);
        int rowIndex = obtainLine(row);
        StoneColor stoneColor = getStoneColor(color);
        board[rowIndex][columnIndex] = stoneColor.getSymbol();
        Intersection newIntersection = new Intersection(rowIndex, columnIndex, stoneColor);
        intersections.add(newIntersection);
        checkInpactNewStone(newIntersection);
        //printIntersections();
        //printGroups();
    }

    public void checkInpactNewStone(Intersection intersection){
        checkCapture(intersection);
        if (intersection.isCaptured()){
            return;
        } else {
            checkIsInGroup(intersection);
            checkIfCapturing(intersection);
        }

    }

    private void checkIfCapturing(Intersection intersection) {
        List<int[]> liberties = intersection.getCoordNeighbours();
        for (int[] coord : liberties) {
            for (Intersection stoneOnBoard : intersections) {
                if (Arrays.equals(stoneOnBoard.getCoordinates(), coord) && stoneOnBoard.getStoneColor() != intersection.getStoneColor()){
                    boolean adjacentIsInGroup = false;
                    for (Group group : groups) {
                        List<int[]> groupCoor = group.getAllCoordinates();
                        int r = stoneOnBoard.getRow();
                        int c =stoneOnBoard.getColumn();
                        int row1;
                        int c1;
                        for (int i = 0; i < groupCoor.size(); i++){
                            row1 =groupCoor.get(i)[0];
                             c1 = groupCoor.get(i)[1];

                            if (row1 == r && c == c){
                                //System.out.println("je suis rentré dans la boucle");
                                checkCaptureGroup(group);
                                adjacentIsInGroup = true;
                            }
                        }






                    }
                    if (!adjacentIsInGroup) {
                        checkCapture(stoneOnBoard);
                    }
                }
            }
        }

    }

    private void checkCaptureGroup(Group group) {
        List<int[]> liberties = group.getNeighboursCoordinates();

        List<int[]> groupCoordinates = group.getAllCoordinates();

        //System.out.println("Liberties:");


        for (int[] coord : liberties) {
            int r = coord[0];
            int c = coord[1];
            boolean toDelete = false;

            if (r < 1 || r > size || c < 1 || c > size) {
                toDelete = true;
            }


            int row1;
            int c1;

            for (int i = 0; i < groupCoordinates.size(); i++) {
                row1 = groupCoordinates.get(i)[0];
                c1 = groupCoordinates.get(i)[1];

                if (row1 == r && c1 == c) {
                    toDelete = true;
                }
            }

            if (toDelete){continue;}

            //System.out.println(Arrays.toString(coord));

            boolean isOccupied = false;

            for (Intersection stoneOnBoard : intersections) {
                if (Arrays.equals(stoneOnBoard.getCoordinates(), coord) && stoneOnBoard.getStoneColor() != group.getColor()) {
                    isOccupied = true;
                    break;
                }
            }

            if (!isOccupied) {
                group.setCapturedStateForAll(false);
                return;
            }
        }
        group.setCapturedStateForAll(true);
        List<int[]> allCoordinates = group.getAllCoordinates();
        for (int[] coordGroup : allCoordinates){
            board[coordGroup[0]][coordGroup[1]] = ".";
        }
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
                if (adjacentIntersection != null && !adjacentIntersection.isCaptured() && adjacentIntersection.getStoneColor() == intersection.getStoneColor()) {
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
            matchingGroups.get(0).addStone(intersection);
        }

    }


    private void checkCapture(Intersection intersection) {
        List<int[]> liberties = intersection.getCoordNeighbours();
        for (int[] coord : liberties) {
            if (coord[0] < 1 || coord[0] > size || coord[1] < 1 || coord[1] > size){
                continue;
            }
            boolean isOccupied = false;

            for (Intersection stoneOnBoard : intersections) {
                if (Arrays.equals(stoneOnBoard.getCoordinates(), coord) && stoneOnBoard.getStoneColor() != intersection.getStoneColor()) {
                    isOccupied = true;
                    break;
                }
            }

            if (!isOccupied) {
                intersection.setCaptured(false);
                return;
            }
        }
        intersection.setCaptured(true);
        int[] coord = intersection.getCoordinates();
        board[coord[0]][coord[1]]= ".";
    }

    public void printIntersections() {
        for (Intersection intersection : intersections) {
            System.out.println("Coordonnées : (" + intersection.getRow() + ", " + intersection.getColumn() + ")");
            System.out.println("Couleur : " + intersection.getStoneColor());
            System.out.println("Capturée : " + intersection.isCaptured());
            System.out.println("---------------------");
        }
    }

    private void printGroups() {
        System.out.println("=== Groups ===");
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            System.out.println("Group " + (i + 1) + ":");
            System.out.println("  Color: " + group.getColor());
            System.out.println("  Stones: ");
            for (Intersection stone : group.getStones()) {
                System.out.println("    - Coords: (" + stone.getRow() + ", " + stone.getColumn() + ")");
                System.out.println("    - Color: " + stone.getStoneColor());
                System.out.println("    - Captured: " + stone.isCaptured());
            }
            System.out.println("================");
        }
    }

    public Intersection getIntersectionAt(int row, int column) {
        for (Intersection intersection : intersections) {
            if (intersection.getRow() == row && intersection.getColumn() == column) {
                return intersection;
            }
        }
        return null;
    }

    public int getCapturedStone(String color){
        StoneColor colorStone = getStoneColor(color);
        int count = 0;

        for (Intersection intersection : intersections) {
            if (intersection.isCaptured() && intersection.getStoneColor() == colorStone) {
                count++;
            }
        }

        return count;

    }


}