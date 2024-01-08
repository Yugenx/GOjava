package logic;

import java.util.*;

public class Group {

    private Set<Intersection> stones;

    public Group() {
        this.stones = new HashSet<>();
    }

    public StoneColor getColor() {
        for (Intersection intersection : stones) {
            return intersection.getStoneColor();
        }
        return null;
    }

    public Set<Intersection> getStones() {
        return stones;
    }

    public void addStone(Intersection intersection) {
        stones.add(intersection);
    }

    public boolean removeStone(Intersection intersection) {
        return stones.remove(intersection);
    }

    public void merge(Group otherGroup) {
        stones.addAll(otherGroup.getStones());
    }

    public boolean containsIntersection(int[] coord) {
        if (stones.contains(getStoneAt(coord))){
            return true;
        }
        return false;
    }

    public List<int[]> getNeighboursCoordinates() {
        List<int[]> neighbours = new ArrayList<>();
        for (Intersection intersection : stones) {
            for (int[] coord : intersection.getCoordNeighbours()) {
                if (!containsIntersection(coord))
                    neighbours.add(coord);
            }
        }
        return neighbours;
    }

    public Intersection getStoneAt(int[] coord) {

         for (Intersection stone : stones) {
            if (Arrays.equals(stone.getCoordinates(), coord)) {
                return stone;
            }
        }
        return null;
    }



    public List<int[]> getAllCoordinates() {
        List<int[]> coordinatesList = new ArrayList<>();
        for (Intersection intersection : stones) {
            int[] coordinates = {intersection.getRow(), intersection.getColumn()};
            coordinatesList.add(coordinates);
        }
        return coordinatesList;
    }


}