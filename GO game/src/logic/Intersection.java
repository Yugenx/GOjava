package logic;

public class Intersection {

    private StoneColor stoneColor;
    private int row;
    private int column;

    public Intersection(int column, int row) {
        this.column = column;
        this.row = row;
        this.stoneColor = null;
    }
    public Intersection(int column, int row, StoneColor color) {
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


}
