package cs3500.pa04;

import cs3500.pa03.model.board.CellState;
import cs3500.pa03.model.coordinates.Coord;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a shot to focus on
 */
public class FocusShot {
  private List<Coord> shots;
  private Coord initialShot;
  private CellState[][] opponentBoard;
  private final int boardHeight;
  private final int boardWidth;

  private static final String LEFT = "left";
  private static final String RIGHT = "right";
  private static final String UP = "up";
  private static final String DOWN = "down";
  private static final String VERTICAL = "vertical";
  private static final String HORIZONTAL = "horizontal";
  private static final String BIDIRECTIONAL = "no direction";

  /**
   * Constructor for a focused shot
   *
   * @param initialShot   the shot to focus on
   * @param opponentBoard the board of the opponent
   */
  public FocusShot(Coord initialShot, CellState[][] opponentBoard) {
    this.shots = new ArrayList<>(Arrays.asList(initialShot));
    this.initialShot = initialShot;
    this.opponentBoard = opponentBoard;
    this.boardHeight = this.opponentBoard.length;
    this.boardWidth = this.opponentBoard[0].length;
  }

  private void addToShotQueueVertical(AtomicBoolean direction, AtomicBoolean oppositeDirection,
                                      AtomicInteger currentIndex, AtomicInteger shotSoFar,
                                      ArrayList<Coord> shotsToTake, AtomicInteger countNumOfShots,
                                      Deque<Coord> shotQueue) {
    int directionPosY = this.initialShot.posy + currentIndex.get();
    int oppositeDirectionPosY = this.initialShot.posy - currentIndex.get();
    if (this.isPosition(CellState.MISS, this.initialShot.posx, directionPosY)
        || !this.isPositionWithinBoard(this.initialShot.posx, directionPosY)) {
      direction.set(false);
    }

    if (this.isPosition(CellState.MISS, this.initialShot.posx, oppositeDirectionPosY)
        || !this.isPositionWithinBoard(this.initialShot.posx, oppositeDirectionPosY)) {
      oppositeDirection.set(false);
    }

    if (direction.get() && !this.isPosition(CellState.HIT, this.initialShot.posx, directionPosY)) {
      if (!shotQueue.contains(new Coord(this.initialShot.posx, directionPosY))) {
        shotsToTake.add(new Coord(this.initialShot.posx, directionPosY));
      }
      shotSoFar.incrementAndGet();
      countNumOfShots.incrementAndGet();

    }

    if (oppositeDirection.get() &&
        !this.isPosition(CellState.HIT, this.initialShot.posx, oppositeDirectionPosY)) {
      if (!shotQueue.contains(new Coord(this.initialShot.posx, oppositeDirectionPosY))) {
        shotsToTake.add(new Coord(this.initialShot.posx, oppositeDirectionPosY));
      }
      shotSoFar.incrementAndGet();
      countNumOfShots.incrementAndGet();
    }
  }

  private void addToShotQueueHorz(AtomicBoolean direction, AtomicBoolean oppositeDirection,
                                  AtomicInteger currentIndex, AtomicInteger shotSoFar,
                                  ArrayList<Coord> shotsToTake, AtomicInteger countNumOfShots,
                                  Deque<Coord> shotQueue) {
    int directionPosX = this.initialShot.posx + currentIndex.get();

    int oppositeDirectionPosX = this.initialShot.posx - currentIndex.get();

    if (this.isPosition(CellState.MISS, directionPosX, this.initialShot.posy)
        || !this.isPositionWithinBoard(directionPosX, this.initialShot.posy)) {
      direction.set(false);
    }

    if (this.isPosition(CellState.MISS, oppositeDirectionPosX, this.initialShot.posy)
        || !this.isPositionWithinBoard(oppositeDirectionPosX, this.initialShot.posy)) {
      oppositeDirection.set(false);
    }

    if (direction.get() && !this.isPosition(CellState.HIT, directionPosX, this.initialShot.posy)) {
      if (!shotQueue.contains(new Coord(directionPosX, this.initialShot.posy))) {
        shotsToTake.add(new Coord(directionPosX, this.initialShot.posy));
      }
      shotSoFar.incrementAndGet();
      countNumOfShots.incrementAndGet();
    }

    if (oppositeDirection.get() &&
        !this.isPosition(CellState.HIT, oppositeDirectionPosX, this.initialShot.posy)) {
      if (!shotQueue.contains(new Coord(oppositeDirectionPosX, this.initialShot.posy))) {
        shotsToTake.add(new Coord(oppositeDirectionPosX, this.initialShot.posy));
      }
      shotSoFar.incrementAndGet();
      countNumOfShots.incrementAndGet();
    }
  }


  /**
   * Represents adding shots to a queue
   *
   * @param shotQueue the shotqueue to be used
   * @param shipsLeft amount of ships left
   */
  public void addToShotQueue(Deque<Coord> shotQueue, int shipsLeft, AtomicInteger shotSoFar) {
    ArrayList<Coord> shotsToTake = new ArrayList<>();

    if (this.canConfirmShipDirection()) {
      addToShotQueueHelper(shotSoFar, shotsToTake, shipsLeft, 6, shotQueue);
    } else if (this.isThreeOrMoreHit()) {
      addToShotQueueHelper(shotSoFar, shotsToTake, shipsLeft, 4, shotQueue);
    } else if (this.isTwoHit()) {
      addToShotQueueHelper(shotSoFar, shotsToTake, shipsLeft, 4, shotQueue);
    } else if (this.isOneHitOneMiss()) {
      addToShotQueueHelper(shotSoFar, shotsToTake, shipsLeft, 2, shotQueue);
    } else if (this.isDiagonalMiss()) {
      addAvailableAdjacentShots(shotsToTake, shotSoFar, shipsLeft, shotQueue);
    } else if (this.isOneHit()) {
      addAvailableAdjacentShots(shotsToTake, shotSoFar, shipsLeft, shotQueue);
    }

    for (int i = 0; i < shotsToTake.size(); i++) {
      shotQueue.addLast(shotsToTake.get(i));
    }
  }

  private void addAvailableAdjacentShots(ArrayList<Coord> shotsToTake, AtomicInteger shotSoFar,
                                         int shipsLeft, Deque<Coord> shotQueue) {
    Coord center = this.initialShot;
    ArrayList<Coord> coordsToSort = new ArrayList();
    Coord top = new Coord(center.posx, center.posy - 1);
    Coord bottom = new Coord(center.posx, center.posy + 1);
    Coord left = new Coord(center.posx - 1, center.posy);
    Coord right = new Coord(center.posx + 1, center.posy);

    coordsToSort.add(top);
    coordsToSort.add(bottom);
    coordsToSort.add(left);
    coordsToSort.add(right);

    for (Coord c : coordsToSort) {
      if (this.isPositionWithinBoard(c.posx, c.posy)
          && shotSoFar.get() < shipsLeft
          && opponentBoard[c.posy][c.posx] == CellState.OCEAN
          && !shotQueue.contains(c)) {
        shotsToTake.add(c);
        shotSoFar.incrementAndGet();
      }
    }

  }


  private void addToShotQueueHelper(AtomicInteger shotSoFar, ArrayList<Coord> shotsToTake,
                                    int shipsLeft, int numOfShotsToTake, Deque<Coord> shotQueue) {
    AtomicBoolean direction = new AtomicBoolean();
    direction.set(true);
    AtomicBoolean oppositeDirection = new AtomicBoolean();
    oppositeDirection.set(true);
    AtomicInteger currentIndex = new AtomicInteger(1);
    AtomicInteger countNumOfShots = new AtomicInteger(0);
    while (countNumOfShots.get() < numOfShotsToTake && shotSoFar.get() < shipsLeft
        && (direction.get() || oppositeDirection.get())) {
      if (this.getDirection().equals(VERTICAL)) {
        addToShotQueueVertical(direction, oppositeDirection, currentIndex, shotSoFar, shotsToTake,
            countNumOfShots, shotQueue);
      } else if (this.getDirection().equals(HORIZONTAL)) {
        addToShotQueueHorz(direction, oppositeDirection, currentIndex, shotSoFar, shotsToTake,
            countNumOfShots, shotQueue);
      } else {
        direction.set(false);
        oppositeDirection.set(false);
      }
      currentIndex.incrementAndGet();
    }
  }

  /**
   * Represents a completed focus shot
   *
   * @return whether the focus shot is completed or not
   */
  public boolean completeFocusShot() {
    if (this.getNoOfMisses() >= 2) {
      return true;
    }
    if (this.getDirection().equals(VERTICAL)) {
      return (this.getNoOfHitsAtTopDownBorders() == 1 && this.getNoOfMisses() == 1)
          || (this.getNoOfHitsAtTopDownBorders() >= 2);
    } else if (this.getDirection().equals(HORIZONTAL)) {
      return (this.getNoOfHitAtLeftRightBorders() == 1 && this.getNoOfMisses() == 1)
          || (this.getNoOfHitAtLeftRightBorders() >= 2);
    }
    return false;
  }

  private int getNoOfHitsAtTopDownBorders() {
    int noOfShotsAtBorder = 0;
    for (Coord singleShot : this.shots) {
      if (this.isHitAtTopDownBorders(singleShot)) {
        noOfShotsAtBorder++;
      }
    }
    return noOfShotsAtBorder;
  }

  private int getNoOfHitAtLeftRightBorders() {
    int noOfShotsAtBorder = 0;
    for (Coord singleShot : this.shots) {
      if (this.isHitAtLeftRightBorders(singleShot)) {
        noOfShotsAtBorder++;
      }
    }
    return noOfShotsAtBorder;
  }

  private boolean isHitAtLeftRightBorders(Coord singleShot) {
    return this.opponentBoard[singleShot.posy][singleShot.posx] == CellState.HIT
        && (!this.isPositionWithinBoard(singleShot.posx + 1, singleShot.posy)
        || !this.isPositionWithinBoard(singleShot.posx - 1, singleShot.posy));
  }

  private boolean isHitAtTopDownBorders(Coord singleShot) {
    return this.opponentBoard[singleShot.posy][singleShot.posx] == CellState.HIT
        && (!this.isPositionWithinBoard(singleShot.posx, singleShot.posy + 1)
        || !this.isPositionWithinBoard(singleShot.posx, singleShot.posy - 1));
  }

  private int getNoOfMisses() {
    int noOfMisses = 0;
    for (Coord singleShot : this.shots) {
      if (this.opponentBoard[singleShot.posy][singleShot.posx] == CellState.MISS) {
        noOfMisses++;
      }
    }
    return noOfMisses;
  }

  /**
   * Obtains the inital shot
   *
   * @return the inital shot
   */
  public Coord getInitialShot() {
    return this.initialShot;
  }

  /**
   * Obtains shots to take
   *
   * @return the shots to take
   */
  public List<Coord> getShots() {
    return this.shots;
  }

  /**
   * Checks of shots contains a shot
   *
   * @param shot shot to check
   * @return whether the shots contain it or not
   */
  public boolean containShot(Coord shot) {
    return this.shots.contains(shot);
  }

  /**
   * Adjusts the focus shot shots
   */
  public void recalibrateFocusShot() {
    this.shots = new ArrayList<>(Arrays.asList(this.initialShot));
    this.addNeighborsInDirection();
  }

  /**
   * Adds neighbors in a certain direction
   */
  public void addNeighborsInDirection() {
    if (this.getDirection().equals(VERTICAL)) {
      this.addNeighborHitsInDirectionHelper(0, 1);
    } else if (this.getDirection().equals(HORIZONTAL)) {
      this.addNeighborHitsInDirectionHelper(1, 0);
    }
  }

  /**
   * Helper for adding neighbors to a direction
   *
   * @param dx change x
   * @param dy change y
   */
  private void addNeighborHitsInDirectionHelper(int dx, int dy) {
    boolean direction = true;
    boolean oppositeDirection = true;
    boolean includeMissDirection = true;
    boolean includeMissOppositeDirection = true;
    int currentIndex = 1;
    while (direction || oppositeDirection) {

      int directionPosX = this.initialShot.posx + (dx * currentIndex);
      int directionPosY = this.initialShot.posy + (dy * currentIndex);
      int oppositeDirectionPosX = this.initialShot.posx - (dx * currentIndex);
      int oppositeDirectionPosY = this.initialShot.posy - (dy * currentIndex);

      if (!this.isPosition(CellState.HIT, directionPosX, directionPosY)) {
        direction = false;
        if (includeMissDirection && this.isPosition(CellState.MISS, directionPosX, directionPosY)) {
          this.shots.add(new Coord(directionPosX, directionPosY));
        }
        includeMissDirection = false;
      }

      if (!this.isPosition(CellState.HIT, oppositeDirectionPosX, oppositeDirectionPosY)) {
        oppositeDirection = false;
        if (includeMissOppositeDirection
            && this.isPosition(CellState.MISS, oppositeDirectionPosX, oppositeDirectionPosY)) {
          this.shots.add(new Coord(oppositeDirectionPosX, oppositeDirectionPosY));
        }
        includeMissOppositeDirection = false;
      }

      if (direction) {
        this.shots.add(new Coord(directionPosX, directionPosY));
      }

      if (oppositeDirection) {
        this.shots.add(new Coord(oppositeDirectionPosX, oppositeDirectionPosY));
      }
      currentIndex++;
    }
  }

  /**
   * Checks if a cellstate is in a position
   *
   * @param cellState the cellstate
   * @param posx      its x pos
   * @param posy      its y pos
   * @return whether it is in position or not
   */
  private boolean isPosition(CellState cellState, int posx, int posy) {
    return this.isPositionWithinBoard(posx, posy)
        && this.opponentBoard[posy][posx] == cellState;
  }

  private boolean isPositionWithinBoard(int posx, int posy) {
    return posx < this.boardWidth && posy < this.boardHeight && posx >= 0 && posy >= 0;
  }

  private String getDirection() {
    if (this.isVertical()) {
      return VERTICAL;
    } else if (this.isHorizontal()) {
      return HORIZONTAL;
    } else if (this.isVerticalExceptionCase()) {
      return VERTICAL;
    } else if (this.isHorizontalExceptionCase()) {
      return HORIZONTAL;
    } else {
      return BIDIRECTIONAL;
    }
  }

  private boolean isVertical() {
    return isVerticalMissSandwich()
        || isVerticalMissGapHitMiss()
        || isVerticalWallHitGapMiss()
        || isVerticalWallGapHitMiss()
        || (isVerticalTwoHit() && !isHorizontalExceptionCase())
        || isVerticalOneHitOneMiss();
  }

  private boolean isVerticalExceptionCase() {
    return isVerticalMissGapHitMiss()
        || isVerticalWallHitGapMiss()
        || isVerticalWallGapHitMiss();
  }

  private boolean isVerticalMissSandwich() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    // . * .
    return (adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.get(LEFT) == CellState.MISS
        && adjacentCells.get(RIGHT) == CellState.MISS)
        // . * |
        || (adjacentCells.containsKey(LEFT) && !adjacentCells.containsKey(RIGHT)
        && adjacentCells.get(LEFT) == CellState.MISS)
        // | * .
        || (!adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.get(RIGHT) == CellState.MISS);
  }

  private boolean isVerticalMissGapHitMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    // . * _ .
    return (adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx + 2, this.initialShot.posy))

        // . - * .
        || (adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx - 2, this.initialShot.posy));
  }

  private boolean isVerticalWallHitGapMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();

    // | * - .
    return (!adjacentCells.containsKey(LEFT) &&
        this.isPosition(CellState.MISS, this.initialShot.posx + 2, this.initialShot.posy))

        // . - * |
        || (!adjacentCells.containsKey(RIGHT) &&
        this.isPosition(CellState.MISS, this.initialShot.posx - 2, this.initialShot.posy));
  }

  private boolean isVerticalWallGapHitMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();

    // . * - |
    return (adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx + 2, this.initialShot.posy))

        // | - * .
        || (adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx - 2, this.initialShot.posy));
  }


  private boolean isVerticalTwoHit() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    // *
    // *
    return (adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.HIT)

        // *
        // *
        || (adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.HIT);

  }

  private boolean isVerticalOneHitOneMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    //   -
    // . * -
    //   -
    return (adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.MISS
        && adjacentCells.containsKey(UP) && adjacentCells.get(UP) != CellState.MISS
        && adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) != CellState.MISS
        && adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) != CellState.MISS)

        //   -
        // - * .
        //   -
        || (adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.MISS
        && adjacentCells.containsKey(UP) && adjacentCells.get(UP) != CellState.MISS
        && adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) != CellState.MISS
        && adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) != CellState.MISS);

  }

  private boolean isHorizontal() {
    return isHorizontalMissGapHitMiss()
        || isHorizontalMissSandwich()
        || isHorizontalWallHitGapMiss()
        || isHorizontalWallGapHitMiss()
        || (isHorizontalTwoHit() && !isVerticalExceptionCase())
        || isHorizontalOneHitOneMiss();
  }

  private boolean isHorizontalExceptionCase() {
    return isHorizontalMissGapHitMiss()
        || isHorizontalWallHitGapMiss()
        || isHorizontalWallGapHitMiss();
  }

  private boolean isHorizontalMissSandwich() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();

    // .
    // *
    // .
    return (adjacentCells.containsKey(UP) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(UP) == CellState.MISS
        && adjacentCells.get(DOWN) == CellState.MISS)

        //  .
        //  *
        // ___
        || (adjacentCells.containsKey(UP) && !adjacentCells.containsKey(DOWN)
        && adjacentCells.get(UP) == CellState.MISS)

        // ___
        //  *
        //  .
        || (!adjacentCells.containsKey(UP) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(DOWN) == CellState.MISS);
  }

  private boolean isHorizontalMissGapHitMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();

    // .
    // *
    // -
    // .
    return (adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy + 2))

        // .
        // -
        // *
        // .
        || (adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy - 2));
  }

  private boolean isHorizontalWallHitGapMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();

    // ___
    //  *
    //  -
    //  .
    return (!adjacentCells.containsKey(UP) &&
        this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy + 2))

        //  .
        //  -
        //  *
        // ___
        || (!adjacentCells.containsKey(DOWN) &&
        this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy - 2));
  }

  private boolean isHorizontalWallGapHitMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();

    //  .
    //  *
    //  -
    // ___
    return (adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx, this.initialShot.posy + 2))

        // ___
        //  -
        //  *
        //  .
        || (adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx, this.initialShot.posy - 2));
  }

  private boolean isHorizontalTwoHit() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    // * *
    return (adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.HIT)
        // * *
        || (adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.HIT);

  }

  private boolean isHorizontalOneHitOneMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();

    //   .
    // - * -
    //   -
    return (adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.MISS
        && adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) != CellState.MISS
        && adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) != CellState.MISS
        && adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) != CellState.MISS)


        //   -
        // - * -
        //   .
        || (adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.MISS
        && adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) != CellState.MISS
        && adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) != CellState.MISS
        && adjacentCells.containsKey(UP) && adjacentCells.get(UP) != CellState.MISS);

  }

  /**
   * Does a scan of the adjacent cells
   *
   * @return whether the scan of the shot has finished
   * and is surrounded
   */
  public boolean completeScan() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    for (String key : adjacentCells.keySet()) {
      if (adjacentCells.get(key) == CellState.OCEAN) {
        return false;
      }
    }
    return true;
  }

  /**
   * Confirms the direction of a ship
   * based off the misses around it
   *
   * @return if a direction can be confirmed
   */
  public boolean canConfirmShipDirection() {
    return this.sandwichByTwoMiss() || this.sandwichByWallAndMiss()
        || this.wallGapHitMiss() || this.wallHitGapMiss() || this.missGapHitMiss();
  }

  private boolean sandwichByTwoMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    return (adjacentCells.containsKey(UP) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(UP) == CellState.MISS && adjacentCells.get(DOWN) == CellState.MISS)
        || (adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.get(LEFT) == CellState.MISS && adjacentCells.get(RIGHT) == CellState.MISS);
  }

  private boolean sandwichByWallAndMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    return (adjacentCells.containsKey(UP) && !adjacentCells.containsKey(DOWN)
        && adjacentCells.get(UP) == CellState.MISS)
        || (adjacentCells.containsKey(DOWN) && !adjacentCells.containsKey(UP)
        && adjacentCells.get(DOWN) == CellState.MISS)
        || (adjacentCells.containsKey(LEFT) && !adjacentCells.containsKey(RIGHT)
        && adjacentCells.get(LEFT) == CellState.MISS)
        || (adjacentCells.containsKey(RIGHT) && !adjacentCells.containsKey(LEFT)
        && adjacentCells.get(RIGHT) == CellState.MISS);
  }

  private boolean wallGapHitMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    return (adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx, this.initialShot.posy + 2))

        || (adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx, this.initialShot.posy - 2))

        || (adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx + 2, this.initialShot.posy))

        || (adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx - 2, this.initialShot.posy));
  }

  private boolean wallHitGapMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();

    return (!adjacentCells.containsKey(UP) &&
        this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy + 2))
        || (!adjacentCells.containsKey(DOWN) &&
        this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy - 2))
        || (!adjacentCells.containsKey(LEFT) &&
        this.isPosition(CellState.MISS, this.initialShot.posx + 2, this.initialShot.posy))
        || (!adjacentCells.containsKey(RIGHT) &&
        this.isPosition(CellState.MISS, this.initialShot.posx - 2, this.initialShot.posy));
  }

  private boolean missGapHitMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    return (adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx - 2, this.initialShot.posy))

        || (adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx + 2, this.initialShot.posy))

        || (adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy + 2))

        || (adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy - 2));
  }

  private int numberOfHitsInShotList() {
    int count = 0;
    for (Coord c : this.shots) {
      if (this.isPosition(CellState.HIT, c.posx, c.posy)) {
        count++;
      }
    }
    return count;
  }

  public boolean isThreeOrMoreHit() {
    this.shots = new ArrayList<>(Arrays.asList(this.initialShot));
    this.addNeighborsInDirection();
    boolean threeOrMoreHit = this.numberOfHitsInShotList() >= 3;
    return threeOrMoreHit && !this.completeFocusShot();
  }

  public boolean isTwoHit() {
    this.shots = new ArrayList<>(Arrays.asList(this.initialShot));
    this.addNeighborsInDirection();
    boolean twoHit = this.numberOfHitsInShotList() == 2;
    return twoHit && !this.completeFocusShot();
  }

  public boolean isOneHitOneMiss() {
    return this.oneHitOneMiss();
  }

  private boolean oneHitOneMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    return (adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.MISS
        && adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.OCEAN
        && adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.OCEAN
        && adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.OCEAN)

        || (adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.MISS
        && adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.OCEAN
        && adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.OCEAN
        && adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.OCEAN)

        || (adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.MISS
        && adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.OCEAN
        && adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.OCEAN
        && adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.OCEAN)

        || (adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.MISS
        && adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.OCEAN
        && adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.OCEAN
        && adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.OCEAN);
  }

  public boolean isDiagonalMiss() {
    return this.diagonalMiss() || this.diagonalMissByEdge();
  }

  // check for ocean?
  private boolean diagonalMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    return (adjacentCells.containsKey(UP) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.containsKey(DOWN) && adjacentCells.containsKey(LEFT)
        && adjacentCells.get(UP) == CellState.MISS && adjacentCells.get(RIGHT) == CellState.MISS
        && adjacentCells.get(DOWN) == CellState.OCEAN && adjacentCells.get(LEFT) == CellState.OCEAN)

        || (adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(DOWN)
        && adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(UP)
        && adjacentCells.get(RIGHT) == CellState.MISS && adjacentCells.get(DOWN) == CellState.MISS
        && adjacentCells.get(LEFT) == CellState.OCEAN && adjacentCells.get(UP) == CellState.OCEAN)


        || (adjacentCells.containsKey(DOWN) && adjacentCells.containsKey(LEFT)
        && adjacentCells.containsKey(UP) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.get(DOWN) == CellState.MISS && adjacentCells.get(LEFT) == CellState.MISS
        && adjacentCells.get(UP) == CellState.OCEAN && adjacentCells.get(RIGHT) == CellState.OCEAN)


        || (adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(UP)
        && adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(LEFT) == CellState.MISS && adjacentCells.get(UP) == CellState.MISS
        && adjacentCells.get(RIGHT) == CellState.OCEAN &&
        adjacentCells.get(DOWN) == CellState.OCEAN);
  }

  private boolean diagonalMissByEdge() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    return (!adjacentCells.containsKey(UP) && !adjacentCells.containsKey(RIGHT)
        && adjacentCells.containsKey(DOWN) && adjacentCells.containsKey(LEFT)
        && adjacentCells.get(DOWN) == CellState.OCEAN && adjacentCells.get(LEFT) == CellState.OCEAN)

        || (!adjacentCells.containsKey(RIGHT) && !adjacentCells.containsKey(DOWN)
        && adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(UP)
        && adjacentCells.get(LEFT) == CellState.OCEAN && adjacentCells.get(UP) == CellState.OCEAN)


        || (!adjacentCells.containsKey(DOWN) && !adjacentCells.containsKey(LEFT)
        && adjacentCells.containsKey(UP) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.get(UP) == CellState.OCEAN && adjacentCells.get(RIGHT) == CellState.OCEAN)

        // miss with left/right edge to form diagonal
        || (!adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(UP)
        && adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(UP) == CellState.MISS
        && adjacentCells.get(RIGHT) == CellState.OCEAN &&
        adjacentCells.get(DOWN) == CellState.OCEAN)

        || (!adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(UP)
        && adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(DOWN) == CellState.MISS
        && adjacentCells.get(RIGHT) == CellState.OCEAN &&
        adjacentCells.get(UP) == CellState.OCEAN)

        || (!adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(UP)
        && adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(UP) == CellState.MISS
        && adjacentCells.get(LEFT) == CellState.OCEAN &&
        adjacentCells.get(DOWN) == CellState.OCEAN)

        || (!adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(UP)
        && adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(DOWN) == CellState.MISS
        && adjacentCells.get(LEFT) == CellState.OCEAN &&
        adjacentCells.get(UP) == CellState.OCEAN)

        // miss with top/bottom edge to form diagonal border
        || (!adjacentCells.containsKey(UP) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(LEFT) == CellState.MISS
        && adjacentCells.get(DOWN) == CellState.OCEAN &&
        adjacentCells.get(RIGHT) == CellState.OCEAN)

        || (!adjacentCells.containsKey(UP) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(RIGHT) == CellState.MISS
        && adjacentCells.get(DOWN) == CellState.OCEAN &&
        adjacentCells.get(LEFT) == CellState.OCEAN)

        || (!adjacentCells.containsKey(DOWN) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(UP)
        && adjacentCells.get(LEFT) == CellState.MISS
        && adjacentCells.get(UP) == CellState.OCEAN &&
        adjacentCells.get(RIGHT) == CellState.OCEAN)

        || (!adjacentCells.containsKey(DOWN) && adjacentCells.containsKey(RIGHT)
        && adjacentCells.containsKey(LEFT) && adjacentCells.containsKey(UP)
        && adjacentCells.get(RIGHT) == CellState.MISS
        && adjacentCells.get(UP) == CellState.OCEAN &&
        adjacentCells.get(LEFT) == CellState.OCEAN);
  }

  public boolean isOneHit() {
    return this.oneHit();
  }

  private boolean oneHit() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    return (adjacentCells.containsKey(UP) && adjacentCells.get(UP) == CellState.OCEAN)
        && (adjacentCells.containsKey(DOWN) && adjacentCells.get(DOWN) == CellState.OCEAN)
        && (adjacentCells.containsKey(LEFT) && adjacentCells.get(LEFT) == CellState.OCEAN)
        && (adjacentCells.containsKey(RIGHT) && adjacentCells.get(RIGHT) == CellState.OCEAN);
  }

  public ArrayList<FocusShot> getExceptionFocusShots() {
    ArrayList<FocusShot> result = new ArrayList<>();
    int noOfShotsHit = 0;
    this.recalibrationForExceptions();
    if (missHitHitMissCases()) {
      for (Coord c : this.shots) {
        FocusShot focusShot = new FocusShot(c, opponentBoard);
        if (opponentBoard[c.posy][c.posx] == CellState.HIT && focusShot.missHitHitMissCases()) {
          noOfShotsHit++;
          result.add(focusShot);
        }
      }
    }
    return result;
  }

  private void recalibrationForExceptions() {
    if (missHitHitMissCases()) {
      if (this.isVerticalExceptionCase()) {
        this.addNeighborHitsInDirectionHelper(1, 0);
      } else if (this.isHorizontalExceptionCase()) {
        this.addNeighborHitsInDirectionHelper(0, 1);
      }
    }
  }

  private boolean missHitHitMissCases() {
    return missHitHitMiss() || hitHitMissWithWall();
  }

  private boolean missHitHitMiss() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    // . ** .
    return (adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(LEFT)
        && adjacentCells.get(RIGHT) == CellState.HIT
        && adjacentCells.get(LEFT) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx + 2, this.initialShot.posy))

        // . * * .
        || (adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(LEFT)
        && adjacentCells.get(LEFT) == CellState.HIT
        && adjacentCells.get(RIGHT) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx - 2, this.initialShot.posy))

        || (adjacentCells.containsKey(UP) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(DOWN) == CellState.HIT
        && adjacentCells.get(UP) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy + 2))

        || (adjacentCells.containsKey(UP) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(UP) == CellState.HIT
        && adjacentCells.get(DOWN) == CellState.MISS
        && this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy - 2));
  }

  private boolean hitHitMissWithWall() {
    HashMap<String, CellState> adjacentCells = this.getAdjacentCell();
    // | O * .
    return (adjacentCells.containsKey(RIGHT) && !adjacentCells.containsKey(LEFT)
        && adjacentCells.get(RIGHT) == CellState.HIT
        && this.isPosition(CellState.MISS, this.initialShot.posx + 2, this.initialShot.posy))

        // |* O .
        || (adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(LEFT)
        && adjacentCells.get(LEFT) == CellState.HIT
        && adjacentCells.get(RIGHT) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx - 2, this.initialShot.posy))

        // . * O |
        || (!adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(LEFT)
        && adjacentCells.get(LEFT) == CellState.HIT
        && this.isPosition(CellState.MISS, this.initialShot.posx - 2, this.initialShot.posy))

        // . O * |
        || (adjacentCells.containsKey(RIGHT) && adjacentCells.containsKey(LEFT)
        && adjacentCells.get(RIGHT) == CellState.HIT
        && adjacentCells.get(LEFT) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx + 2, this.initialShot.posy))

        // ___
        //  *
        //  O
        //  .
        || (adjacentCells.containsKey(UP) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(DOWN) == CellState.MISS
        && adjacentCells.get(UP) == CellState.HIT
        && !this.isPositionWithinBoard(this.initialShot.posx, this.initialShot.posy - 2))

        // ___
        //  O
        //  *
        //  .
        || (!adjacentCells.containsKey(UP) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(DOWN) == CellState.HIT
        && this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy + 2))

        //  .
        //  O
        //  *
        // ___
        || (adjacentCells.containsKey(UP) && adjacentCells.containsKey(DOWN)
        && adjacentCells.get(DOWN) == CellState.HIT
        && adjacentCells.get(UP) == CellState.MISS
        && !this.isPositionWithinBoard(this.initialShot.posx, this.initialShot.posy + 2))

        //  .
        //  *
        //  O
        // ___
        || (adjacentCells.containsKey(UP) && !adjacentCells.containsKey(DOWN)
        && adjacentCells.get(UP) == CellState.HIT
        && this.isPosition(CellState.MISS, this.initialShot.posx, this.initialShot.posy - 2));
  }


  private HashMap<String, CellState> getAdjacentCell() {
    HashMap<String, CellState> adjacentCells = new HashMap<>();
    if (this.initialShot.posx + 1 < this.boardWidth) {
      adjacentCells.put(RIGHT,
          this.opponentBoard[this.initialShot.posy][this.initialShot.posx + 1]);
    }
    if (this.initialShot.posx - 1 >= 0) {
      adjacentCells.put(LEFT, this.opponentBoard[this.initialShot.posy][this.initialShot.posx - 1]);
    }
    if (this.initialShot.posy + 1 < this.boardHeight) {
      adjacentCells.put(DOWN, this.opponentBoard[this.initialShot.posy + 1][this.initialShot.posx]);
    }
    if (this.initialShot.posy - 1 >= 0) {
      adjacentCells.put(UP, this.opponentBoard[this.initialShot.posy - 1][this.initialShot.posx]);
    }
    return adjacentCells;
  }


}
