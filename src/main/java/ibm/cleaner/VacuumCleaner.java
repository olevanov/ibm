package ibm.cleaner;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Vacuum cleaner simulator
 * 
 * @author Anton Aliavanau
 *
 */
public class VacuumCleaner {

    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);
        int x = input.nextInt();
        int y = input.nextInt();
        input.nextLine();

        Floor floorObj = new Floor(x, y);

        while(input.hasNextLine()) {
            String state = input.nextLine();
            String commands = input.nextLine();
            if(state.length() == 0 || commands.length() == 0) {
              break;
            }
            String[] initState = state.split(" ");
            
            Cleaner cleaner = new Cleaner(floorObj, initState);
            StringBuilder repeatCountStr = new StringBuilder();
            for (int i = 0; i < commands.length(); i++) {
                char element = commands.charAt(i);
                if (Character.isDigit(element)) {
                  repeatCountStr.append(element);
                  continue;
                } else {
                    String actionElement = Character.toString(element);
                    int repeatCount = repeatCountStr.length() > 0 ? Integer.valueOf(repeatCountStr.toString()):1;
                    repeatCountStr = new StringBuilder();
                    for (int j = 0; j < repeatCount; j++) {
                        cleaner.changeCleanerLocation(Action.valueOf(actionElement));
                    }
                }
            }
            floorObj.addToBlockedList(new Location(cleaner.getPositionX(), cleaner.getPositionY()));
            System.out.println(cleaner.toString());
        }
        input.close();
    }
}

/**
 * Directions that cleaner can move
 * 
 * @author Anton Aliavanau
 *
 */
enum Direction {

    N(0), E(1), S(2), W(3);

    private int pos;

    Direction(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return this.pos;
    }

}

/**
 * Possible action R - turn right, L - turn left, F - move forward 
 * 
 * @author Anton Aliavanau
 *
 */
enum Action {
    R, L, F;
}

/**
 * Stores coordinate
 * 
 * @author Anton Aliavanau
 *
 */
class Location {
    private final int xCount;
    private final int yCount;

    public Location(int xCount, int yCount) {
        this.xCount = xCount;
        this.yCount = yCount;
    }

    public boolean isTaken(int x, int y) {
        return this.xCount == x && this.yCount == y;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + xCount;
      result = prime * result + yCount;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Location other = (Location) obj;
      if (xCount != other.xCount)
        return false;
      if (yCount != other.yCount)
        return false;
      return true;
    }
    
}

/**
 * Represent floor
 * 
 * @author Anton Aliavanau
 *
 */
class Floor {

    private final int xCount;
    private final int yCount;

    private Set<Location> blockedLocations = new HashSet<Location>();

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

    public Set<Location> getBlockedLocationList() {
        return blockedLocations;
    }

    public void setBlockedCoordinateList(Set<Location> blockedLocations) {
        this.blockedLocations = blockedLocations;
    }

    public void addToBlockedList(Location coordinate) {
        this.blockedLocations.add(coordinate);
    }

  public boolean containsTakenLocation(int x, int y) {
    return blockedLocations.contains(new Location(x, y));
  }

}

/**
 * Cleaner
 * 
 * @author Anton Aliavanau
 *
 */
class Cleaner {

    public Cleaner(Floor floor, String[] initState) {
      this.floor = floor;
      initInitialPosition(initState);
      if (floor.containsTakenLocation(this.positionX, this.positionY)) {
        throw new IllegalArgumentException("Invalid initial location for cleaner");
      }
    }

    private void initInitialPosition(String[] initState) {
      try {
        this.positionX = Integer.parseInt(initState[0]);
        this.positionY = Integer.parseInt(initState[1]);
        this.direction = Direction.valueOf(initState[2]);
      }
      catch (NumberFormatException e) {
        this.positionX = Integer.parseInt(initState[1]);
        this.positionY = Integer.parseInt(initState[2]);
        this.direction = Direction.valueOf(initState[0]);
      }
      
    }

    public Cleaner() {
    }

    int positionX;
    int positionY;

    Floor floor;

    Direction direction;

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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
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
            if (this.positionX > getFloor().getxCount() || this.positionX < 0 || getFloor().containsTakenLocation(this.positionX, this.positionY)) {
                this.positionX -= xIncr;
            }
        }

        if (yIncr != 0) {
            this.positionY += yIncr;
            if (this.positionY > getFloor().getyCount() || this.positionY < 0 || getFloor().containsTakenLocation(this.positionX, this.positionY)) {
                this.positionY -= yIncr;
            }
        }
    }

    public void changeCleanerLocation(Action action) {

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

    private Direction getDirection(int index) {

        int pos = getDirection().getPos() + index;
        if (pos > 3) {
            pos = 0;
        } else if (pos < 0) {
            pos = 3;
        }

        for (Direction dir: Direction.values()) {
            if (dir.getPos() == pos) {
                return dir;
            }
        }

        return Direction.N;
    }
    
    @Override
    public String toString() {
      return positionX + " " + positionY + " " + direction.name();
    }
}




