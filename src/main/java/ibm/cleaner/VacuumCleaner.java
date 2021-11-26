package ibm.cleaner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VacuumCleaner {

    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);
        int x = input.nextInt();
        int y = input.nextInt();
        input.nextLine();
        int[][] floor = new int[x][y];

        Floor floorObj = new Floor(x, y);

        while(input.hasNextLine()) {
            String[] initState = input.nextLine().split(" ");
            String commands = input.nextLine();

            Cleaner cleaner = new Cleaner(floorObj, Integer.valueOf(initState[1]), Integer.valueOf(initState[2]), DirectionsEnum.valueOf(initState[0]));

            for (int i = 0; i < commands.length(); i++) {
                char element = commands.charAt(i);

                if (Character.isDigit(element)) {
                    int repeatCount = 0;
                    String repeatStr = Character.toString(element);
                    char element1 = commands.charAt(++i);
                    if (Character.isDigit(element1)) {
                        repeatStr += Character.toString(element1);
                        ++i;
                    }

                    repeatCount = Integer.valueOf(repeatStr);
                    String actionType = Character.toString(commands.charAt(i));
                    for (int j = 0; j < repeatCount; j++) {
                        cleaner.changeCleanerLocation(Actions.valueOf(actionType));
                    }
                } else {
                    cleaner.changeCleanerLocation(Actions.valueOf(Character.toString(element)));
                }
            }
            floorObj.addToBlockedList(new Coordinate(cleaner.getPositionX(), cleaner.getPositionY()));
            System.out.println(cleaner.getPositionX() + " " + cleaner.getPositionY() + " " + cleaner.getDirection().name());
        }
    }
}

enum DirectionsEnum {

    N(0), E(1), S(2), W(3);

    private int pos;

    DirectionsEnum(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return this.pos;
    }

}

enum Actions {
    R, L, F;
}

class Coordinate {
    private final int xCount;
    private final int yCount;

    public Coordinate(int xCount, int yCount) {
        this.xCount = xCount;
        this.yCount = yCount;
    }

    public boolean blocked(int x, int y) {
        return this.xCount == x && this.yCount == y;
    }
}

class Floor {

    private final int xCount;
    private final int yCount;

    public List<Coordinate> blockedCoordinateList = new ArrayList<>();

    public Floor(int xCount, int yCount) {
        this.xCount = xCount;
        this.yCount = yCount;
    }

    public int getxCount() {
        return xCount;
    }

    public int getyCount() {
        return yCount;
    }

    public List<Coordinate> getBlockedCoordinateList() {
        return blockedCoordinateList;
    }

    public void setBlockedCoordinateList(List<Coordinate> blockedCoordinateList) {
        this.blockedCoordinateList = blockedCoordinateList;
    }

    public void addToBlockedList(Coordinate coordinate) {
        this.blockedCoordinateList.add(coordinate);
    }

    public boolean containsBlocked(int x, int y) {
        for (Coordinate coordinate: blockedCoordinateList) {
            if (coordinate.blocked(x,y)) {
                return true;
            }
        }

        return false;
    }

}

class Cleaner {

    public Cleaner(Floor floor, int positionX, int positionY, DirectionsEnum direction) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
        this.floor = floor;
    }

    public Cleaner() {
    }

    int positionX;
    int positionY;

    Floor floor;

    DirectionsEnum direction;

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public DirectionsEnum getDirection() {
        return direction;
    }

    public void setDirection(DirectionsEnum direction) {
        this.direction = direction;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public void move(int xIncr, int yIncr) {
        if (xIncr != 0) {
            this.positionX += xIncr;
            if (this.positionX > getFloor().getxCount() || this.positionX < 0 || getFloor().containsBlocked(this.positionX, this.positionY)) {
                this.positionX -= xIncr;
            }
        }

        if (yIncr != 0) {
            this.positionY += yIncr;
            if (this.positionY > getFloor().getyCount() || this.positionY < 0 || getFloor().containsBlocked(this.positionX, this.positionY)) {
                this.positionY -= yIncr;
            }
        }
    }

    public void changeCleanerLocation(Actions action) {

        switch (action) {
            case R:
                this.setDirection(getDirection(1));
                break;
            case L:
                this.setDirection(getDirection(-1));
                break;
            case F:
                moveForward();
                break;
        }

    }

    private void moveForward() {
        switch (this.getDirection()) {
            case E:
                this.move(1,0);
                break;
            case N:
                this.move(0,1);
                break;
            case W:
                this.move(-1,0);
                break;
            case S:
                this.move(0, -1);
                break;
        }
    }

    private DirectionsEnum getDirection(int index) {

        int pos = getDirection().getPos() + index;
        if (pos > 3) {
            pos = 0;
        } else if (pos < 0) {
            pos = 3;
        }

        for (DirectionsEnum dir: DirectionsEnum.values()) {
            if (dir.getPos() == pos) {
                return dir;
            }
        }

        return DirectionsEnum.N;
    }
}




