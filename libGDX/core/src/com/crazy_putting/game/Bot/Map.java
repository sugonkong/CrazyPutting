package com.crazy_putting.game.Bot;

import java.util.ArrayList;
import java.util.List;

public class Map<T extends AbstractNode> {

    protected static boolean CANMOVEDIAGONALY = true;

    private T[][] nodes;

    protected int width;
    protected int length;


    private NodeFactory nodeFactory;

    public Map(int width, int length, NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        nodes = (T[][]) new AbstractNode[width][length];
        this.width = width - 1;
        this.length = length - 1;
        initEmptyNodes();
    }


    private void initEmptyNodes() {
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= length; j++) {
                nodes[i][j] = (T) nodeFactory.createNode(i, j);
            }
        }
    }


    public void setWalkable(int x, int y, boolean bool) {
        nodes[x][y].setWalkable(bool);
    }

    public final T getNode(int x, int y) {
        return nodes[x][y];
    }


    private void print(String s) {
        System.out.print(s);
    }

    private List<T> openList;
    private List<T> closedList;
    private boolean done = false;

    public final List<T> findPath(int oldX, int oldY, int newX, int newY) {
        openList = new ArrayList<T>();
        closedList = new ArrayList<T>();
        openList.add(nodes[oldX][oldY]);

        done = false;
        T current;
        while (!done) {
            current = lowestFInOpen(newX, newY);
            closedList.add(current);
            openList.remove(current);

            if ((current.getxPosition() == newX)
                    && (current.getyPosition() == newY)) {
                return calcPath(nodes[oldX][oldY], current);
            }

            List<T> adjacentNodes = getAdjacent(current);
            for (int i = 0; i < adjacentNodes.size(); i++) {
                T currentAdj = adjacentNodes.get(i);
                if (!openList.contains(currentAdj)) {
                    currentAdj.setPrevious(current);
                    currentAdj.setCosts(nodes[newX][newY]);
                    currentAdj.setgCosts(current);
                    openList.add(currentAdj);
                } else {
                    if (currentAdj.getgCosts() > currentAdj.calculategCosts(current)) {
                        currentAdj.setPrevious(current);
                        currentAdj.setgCosts(current);
                    }
                }
            }

            if (openList.isEmpty()) {
                return new ArrayList<T>();
            }
        }
        return null;
    }


    private List<T> calcPath(T start, T goal) {
        // TODO if invalid nodes are given (eg cannot find from goal to start, this method will result in an infinite loop!)
        ArrayList<T> path = new ArrayList<T>();

        T curr = goal;
        boolean done = false;
        while (!done) {
            path.add(0,curr);
            curr = (T) curr.getPrevious();

            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }


    private T lowestFInOpen(int goalX, int goalY) {
        T cheapest = openList.get(0);
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).calculateHcosts(goalX, goalY) < cheapest.gethCosts()) {
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }


    private List<T> getAdjacent(T node) {
        int x = node.getxPosition();
        int y = node.getyPosition();
        List<T> adj = new ArrayList<T>();

        T temp;
            // WEST WEST WEST
        if (x > 0) {
            temp = this.getNode((x - 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }
            // EAST EAST EAST
        if (x < width) {
            temp = this.getNode((x + 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }
            // SOUTH SOUTH SOUTH
        if (y > 0) {
            temp = this.getNode(x, (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }
            // NORTH NORTH NORTH
        if (y < length) {
            temp = this.getNode(x, (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }
            // NORTH EAST NORTH EAST NORTH EAST
        if (x < width && y < length) {
            temp = this.getNode((x + 1), (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(true);
                adj.add(temp);
            }
        }
            // SOUTH WEST SOUTH WEST SOUTH WEST
        if (x > 0 && y > 0) {
            temp = this.getNode((x - 1), (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(true);
                adj.add(temp);
            }
        }
            // NORTH WEST NORTH WEST NORTH WEST
        if (x > 0 && y < length) {
            temp = this.getNode((x - 1), (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(true);
                adj.add(temp);
            }
        }
            // SOUTH EAST SOUTH EAST SOUTH EAST
        if (x < width && y > 0) {
            temp = this.getNode((x + 1), (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(true);
                adj.add(temp);
            }
        }

        return adj;
    }


}
