package IHM;

import logic.Intersection;
import logic.StoneColor;

import java.util.ArrayList;

public interface IBoard {
    ArrayList<Intersection> getIntersections();

    int getSize();

    void createBoard();

    String toString();

    void changeScore(StoneColor color, int num);

    void changeSize(int newSize);

    void createCoord();

    void clear();

    int obtainColumn(char letter);

    int obtainLine(int row);

    boolean isFree(char column, int row);

    boolean verifyCoord(char column, int row);

    boolean verifyCoord(int column, int row);

    void playMove(char column, int row, String color);

    void checkInpactNewStone(Intersection intersection);

    Intersection getIntersectionAt(int row, int column);

    int getLiberties(char column, int row);

    boolean isFull();

    public int getFinalScore (StoneColor color);
}
