package cs3500.pa04;

import cs3500.pa03.model.board.BoardStatePlayer;
import cs3500.pa03.model.board.CellState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.players.AbstractPlayer;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

/**
 * Represents a improved bot
 */
public class SuperBot extends AbstractPlayer {

  private int[][] heatMap;
  private int height;
  private int width;
  private List<Ship> enemyShips = new ArrayList<>();
  private Deque<Coord> shotQueue = new LinkedList<>();
  private boolean searchMode = true;
  private ArrayList<Coord> shotsTaken = new ArrayList<>();
  private TargetingSystem targetingSystem;

  /**
   * Constructor for a superbot
   *
   * @param playerBoard the class to store info
   * @param rand        the randomization used
   */
  public SuperBot(BoardStatePlayer playerBoard, Random rand) {
    super(playerBoard, rand);
  }

  // call this method upon setup
  private void initHeatMap() {
    for (Ship s : playerBoard.getPlayerShipList()) {
      enemyShips.add(new Ship(new ArrayList<>(), s.getShipType()));
    }
    int divisor = minShipSize();
    height = playerBoard.getPlayerBoard().length;
    width = playerBoard.getPlayerBoard()[0].length;
    heatMap = new int[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        totalShipProb(i, j);
        if (i % divisor == 0 && j % divisor == 0) {
          heatMap[i][j] += 20;  // Increment the value at the specified position
        }
      }
    }
  }

  private void updateHeatmap() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        totalShipProb(i, j);
      }
    }
  }

  private void totalShipProb(int height, int width) {
    for (Ship s : enemyShips) {
      // edit to be for opponent ships
      if (!s.getSinkStatus()) {
        shipProb(s.getShipType().returnSize(), height, width, 1);
      }
    }
  }

  private void shipProb(int size, int i, int j, int amt) {
    CellState[][] board = playerBoard.getOpponentBoard();

    // Check rightward extension
    if (j + size - 1 < width && checkValidExtension(board, i, j, 0, 1, size)) {
      updateHeatMapAt(i, j, 0, 1, size, amt);
    }

    // Check leftward extension
    if (j - size + 1 >= 0 && checkValidExtension(board, i, j, 0, -1, size)) {
      updateHeatMapAt(i, j, 0, -1, size, amt);
    }

    // Check downward extension
    if (i + size - 1 < height && checkValidExtension(board, i, j, 1, 0, size)) {
      updateHeatMapAt(i, j, 1, 0, size, amt);
    }

    // Check upward extension
    if (i - size + 1 >= 0 && checkValidExtension(board, i, j, -1, 0, size)) {
      updateHeatMapAt(i, j, -1, 0, size, amt);
    }
  }

  private boolean checkValidExtension(CellState[][] board, int i, int j, int rowDelta, int colDelta,
                                      int size) {
    for (int k = 0; k < size; k++) {
      int row = i + rowDelta * k;
      int col = j + colDelta * k;

      if (board[row][col] == CellState.MISS || board[row][col] == CellState.HIT) {
        heatMap[row][col] = 0;
        return false;
      }
    }
    return true;
  }

  private void updateHeatMapAt(int i, int j, int rowDelta, int colDelta, int size, int amt) {
    for (int k = 0; k < size; k++) {
      int row = i + rowDelta * k;
      int col = j + colDelta * k;
      heatMap[row][col] = heatMap[row][col] + amt;
    }
  }

  /*
  =======================
  TARGETTING MODE
  =======================
   */


  // To determine direction, check targetShots for the x or y value in coords with most repeats

  private boolean determineDirection(ArrayList<Coord> shots) {
    HashMap<Integer, Integer> xCounts = new HashMap<>();
    HashMap<Integer, Integer> yCounts = new HashMap<>();
    for (Coord coord : shots) {
      int x = coord.posx;
      int y = coord.posy;
      if (xCounts.containsKey(x)) {
        xCounts.put(x, xCounts.get(x) + 1);
      } else {
        xCounts.put(x, 1);
      }
      if (yCounts.containsKey(y)) {
        yCounts.put(y, yCounts.get(y) + 1);
      } else {
        yCounts.put(y, 1);
      }
    }
    int maxX = 0;
    int maxXCount = 0;
    for (Map.Entry<Integer, Integer> entry : xCounts.entrySet()) {
      if (entry.getValue() > maxXCount) {
        maxX = entry.getKey();
        maxXCount = entry.getValue();
      }
    }
    int maxY = 0;
    int maxYCount = 0;
    for (Map.Entry<Integer, Integer> entry : yCounts.entrySet()) {
      if (entry.getValue() > maxYCount) {
        maxY = entry.getKey();
        maxYCount = entry.getValue();
      }
    }
    return maxYCount > maxXCount;
  }


  private void updateShotQueue() {
    Iterator<Coord> iterator = shotQueue.iterator();
    while (iterator.hasNext()) {
      Coord c = iterator.next();
      if (this.shotsTaken.contains(c)) {
        iterator.remove();
      }
    }
    if (shotQueue.isEmpty()) {
      searchMode = true;
    }
  }

  private int minShipSize() {
    ArrayList<Integer> sizes = new ArrayList<>();
    for (Ship s : enemyShips) {
      if (!s.getSinkStatus()) {
        sizes.add(s.getShipType().returnSize());
      }
    }
    if (sizes.size() == 0) {
      return 3;
    }
    return Collections.min(sizes);
  }

  private int maxShipSize() {
    ArrayList<Integer> sizes = new ArrayList<>();
    for (Ship s : enemyShips) {
      if (!s.getSinkStatus()) {
        sizes.add(s.getShipType().returnSize());
      }
    }
    if (sizes.size() == 0) {
      return 6;
    }
    return Collections.max(sizes);
  }


  private List<Coord> searchShots(int amt) {
    PriorityQueue<Coord> maxHeap = new PriorityQueue<>(new Comparator<Coord>() {
      @Override
      public int compare(Coord c1, Coord c2) {
        int value1 = heatMap[c1.posy][c1.posx];
        int value2 = heatMap[c2.posy][c2.posx];
        return Integer.compare(value2, value1);
      }
    });
    // Collect the coordinates
    for (int i = 0; i < heatMap.length; i++) {
      for (int j = 0; j < heatMap[i].length; j++) {
        if (playerBoard.getOpponentBoard()[i][j] == CellState.OCEAN
            && !this.shotsTaken.contains(new Coord(j, i))) {
          maxHeap.offer(new Coord(j, i));
        }
      }
    }
    List<Coord> randomlySelectedCoords = new ArrayList<>();
    int remainingCoords = Math.min(amt, maxHeap.size());
    int count = 0;
    while (count < remainingCoords) {
      Coord coord = maxHeap.poll();
      count++;
      randomlySelectedCoords.add(coord);
    }
    return randomlySelectedCoords;
  }

  /**
   * Generates a list of shots the AI is taking
   *
   * @return the list of shots
   */
  @Override
  public List<Coord> takeShots() {
    if (shotsTaken.isEmpty()) {
      initHeatMap();
    }
    updateHeatmap();
    List<Coord> shots = new ArrayList<>();
    while (shots.size() < this.playerBoard.getShipsLeft()) {
      this.updateShotQueue();
      if (shotsTaken.size() >= height * width) {
        return shots;
      }
      if (searchMode) {
        for (Coord c : searchShots(playerBoard.getShipsLeft() - shots.size())) {
          if (!this.shotsTaken.contains(c)) {
            shots.add(0, c);
            this.shotsTaken.add(c);
          }
        }
      } else if (!searchMode) {
        Coord c = shotQueue.removeFirst();
        while (playerBoard.getOpponentBoard()[c.posy][c.posx] != CellState.OCEAN &&
            this.shotsTaken.contains(c)) {
          updateShotQueue();
          if (shotQueue.isEmpty()) {
            searchMode = true;
            break;
          } else {
            c = shotQueue.removeFirst();
          }
        }
        if (!searchMode) {
          shots.add(c);
          this.shotsTaken.add(c);
        }
      }
    }
    Set<Coord> set = new HashSet<>(shots);
    return shots;
  }

  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {

    targetingSystem.transferExceptions();
    targetingSystem.recalibrate();
    targetingSystem.transferFocusShots();
    targetingSystem.sortSuccessfulHits(shotsThatHitOpponentShips);
    targetingSystem.filter();

    if (!targetingSystem.isEmpty() || !shotsThatHitOpponentShips.isEmpty()) {
      searchMode = false;
      targetingSystem.fireShots();
    } else {
      searchMode = true;
      updateHeatmap();
    }
  }


  private void updateSunkList(List<Coord> coords) {
    List<Integer> groupSizes = new ArrayList<>();
    int currentGroupSize = 1;

    for (int i = 1; i < coords.size(); i++) {
      Coord current = coords.get(i);
      Coord previous = coords.get(i - 1);
      if (isAdjacent(current, previous)) {
        currentGroupSize++;
      } else {
        groupSizes.add(currentGroupSize);
        currentGroupSize = 1;
      }
    }
    // Add the size of the last group
    groupSizes.add(currentGroupSize);

    for (Integer i : groupSizes) {
      shipIsSunk(i);
    }
  }

  private boolean isAdjacent(Coord coord1, Coord coord2) {
    return Math.abs(coord1.posx - coord2.posx) <= 1 && Math.abs(coord1.posy - coord2.posy) <= 1;
  }


  private void shipIsSunk(int length) {
    for (Ship s : enemyShips) {
      if (!s.getSinkStatus() && s.getShipType().returnSize() == length) {
        s.setSinkStatus(true);
      }
    }
  }

  /**
   * Setup to position ships near edge of board
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return The list of ships with their coords
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    createBoard(height, width);
    List<Ship> shipList = placeAllShips(specifications);
    this.playerBoard.initializeBoard(height, width, shipList);
    targetingSystem =
        new TargetingSystem(this.shotQueue, this.playerBoard.getOpponentBoard(), this.playerBoard);
    return shipList;
  }

//  @Override
//  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
//    List<Ship> ships = super.setup(height, width, specifications);
//    targetingSystem = new TargetingSystem(this.shotQueue, this.playerBoard.getOpponentBoard(), this.playerBoard);
//    return ships;
//  }

  /**
   * Stores the height and width to be
   * used later
   *
   * @param height height of baord
   * @param width  width of board
   */
  private void createBoard(int height, int width) {
    this.height = height;
    this.width = width;
  }

  /**
   * Places all the ships onto the board
   *
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the list of ships after getting placed
   */
  private List<Ship> placeAllShips(Map<ShipType, Integer> specifications) {
    List<Ship> shipList = new ArrayList<>();
    ShipType[] shipTypes =
        new ShipType[] {ShipType.CARRIER, ShipType.BATTLESHIP, ShipType.DESTROYER,
            ShipType.SUBMARINE};
    for (ShipType mapElement : shipTypes) {
      int amt = specifications.get(mapElement);
      for (int i = 0; i < amt; i++) {
        Ship ship = placeShip(shipList, mapElement);
        shipList.add(ship);
      }
    }
    return shipList;
  }

  /**
   * Places a single ship, ensuring it is
   * within bounds and doesn't intersect
   *
   * @param shipList the list of currently placed ships
   * @param shipType the type of ship to place
   * @return the single placed ship
   */
  private Ship placeShip(List<Ship> shipList, ShipType shipType) {
    boolean validCord = false;
    ArrayList<Coord> cords = new ArrayList<>();
    while (!validCord) {
      validCord = true;
      cords = generateCords(shipType.returnSize());
      for (Coord c : cords) {
        if (!c.inBounds(height, width)) {
          validCord = false;
          break;
        }
        for (Ship s : shipList) {
          if (s.coordOverlap(c)) {
            validCord = false;
            break;
          }
        }
      }
    }
    return new Ship(cords, shipType);
  }

  /**
   * Generates a random set of cords next to each other
   *
   * @param size the size of the set of cords
   * @return the set of cords generated
   */
  private ArrayList<Coord> generateCords(int size) {
    ArrayList<Coord> cords = new ArrayList<>();
    Random rand = this.random;
    Coord randCord = randomCord(height, width);
    cords.add(randCord);
    boolean changeX = !nearxBorder(randCord);
    boolean addValue = rand.nextBoolean();
    // Generate cords for ship
    for (int i = 0; i < size - 1; i++) {
      Coord newCord;
      if (changeX) {
        newCord = randCord.changeX(addValue);
      } else {
        newCord = randCord.changeY(addValue);
      }
      cords.add(newCord);
      randCord = newCord;
    }
    return cords;
  }

  private boolean nearxBorder(Coord coord) {
    int distanceFromY = Math.min(height - coord.posy, coord.posy);
    int distanceFromX = Math.min(width - coord.posx, coord.posx);
    return distanceFromX < distanceFromY;
  }

  private Coord randomCord(int height, int width) {
    int y;
    int x;
    int ring = Math.min(height, width) / 2;
    int outerRingStart = ring + 2;
    int outerRingEndX = width - ring - 1;
    int outerRingEndY = height - ring - 1;

    do {
      x = random.nextInt(width);
      y = random.nextInt(height);
    } while ((x >= outerRingStart && x <= outerRingEndX
        && (y == outerRingStart || y == outerRingEndY))
        ||
        (y >= outerRingStart && y <= outerRingEndY && (x == outerRingStart || x == outerRingEndX)));

    return new Coord(x, y);
  }

}
