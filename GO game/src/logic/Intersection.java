package logic;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

    private StoneColor stoneColor;
    private int row;
    private int column;

    private boolean isCaptured = false;

    public boolean isCaptured() {
        return isCaptured;
    }

    public int getRow() {
        return row;
    }



    public void setCaptured(boolean captured) {
        isCaptured = captured;
    }

    public int getColumn() {
        return column;
    }

    public Intersection(int row, int column, StoneColor color) {
        this.column = column;
        this.row = row;
        this.stoneColor = color;
    }

    public StoneColor getStoneColor() {
        return stoneColor;
    }

    public void setStoneColor(StoneColor stoneColor) {
        this.stoneColor = stoneColor;
    }

    public boolean isEmpty() {
        return stoneColor == null;
    }

    public int[] getCoordinates() {
        return new int[]{this.row, this.column};
    }

    public List<int[]> getCoordNeighbours(){
        List<int[]> neighbours = new ArrayList<>();
        neighbours.add(new int[]{row + 1, column});
        neighbours.add(new int[]{row -1, column});
        neighbours.add(new int[]{row, column + 1});
        neighbours.add(new int[]{row, column - 1});
        return neighbours;
    }




}
