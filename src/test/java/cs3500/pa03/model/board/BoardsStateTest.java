package cs3500.pa03.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for board state class
 */
class BoardsStateTest {

  BoardsState mockBoard;
  Ship mockSubmarine;
  Ship mockSunkenSubmarine;
  CellState[][] initialPlayerBoard;
  CellState[][] initialOpponentBoard;
  List<Coord> mockShots;
  List<Ship> shipList;

  /**
   * Initializes boards, ships and shots needed for testing
   */
  @BeforeEach
  public void initBoard() {
    Coord coord1 = new Coord(0, 0);
    Coord coord2 = new Coord(0, 1);
    Coord coord3 = new Coord(0, 2);
    final Coord coord4 = new Coord(1, 0);
    Coord coord5 = new Coord(2, 0);
    Coord coord6 = new Coord(2, 1);
    Coord coord7 = new Coord(2, 2);
    ArrayList<Coord> subArea = new ArrayList<>(Arrays.asList(coord1, coord2, coord3));
    this.mockSubmarine = new Ship(subArea, ShipType.SUBMARINE);
    ArrayList<Coord> sunkSubArea = new ArrayList<>(Arrays.asList(coord5, coord6, coord7));
    this.mockSunkenSubmarine = new Ship(sunkSubArea, ShipType.SUBMARINE);
    this.mockSunkenSubmarine.isHit(coord5);
    this.mockSunkenSubmarine.isHit(coord6);
    this.mockSunkenSubmarine.isHit(coord7);

    this.mockBoard = new BoardsState();
    this.shipList = new ArrayList<>(Arrays.asList(this.mockSubmarine, this.mockSunkenSubmarine));
    this.mockBoard.initializeBoard(3, 3, this.shipList);

    this.initialPlayerBoard =
        new CellState[][] {{CellState.SUBMARINE, CellState.OCEAN, CellState.SUBMARINE},
            {CellState.SUBMARINE, CellState.OCEAN, CellState.SUBMARINE},
            {CellState.SUBMARINE, CellState.OCEAN, CellState.SUBMARINE}};

    this.initialOpponentBoard =
        new CellState[][] {{CellState.OCEAN, CellState.OCEAN, CellState.OCEAN},
            {CellState.OCEAN, CellState.OCEAN, CellState.OCEAN},
            {CellState.OCEAN, CellState.OCEAN, CellState.OCEAN}};

    this.mockShots = new ArrayList<>(Arrays.asList(coord1, coord4, coord5));
  }

  /**
   * Test for updating the opponent's board after the player fires their shots for that round
   */
  @Test
  public void updatePlayerShotsRound() {
    assertTrue(Arrays.deepEquals(this.initialOpponentBoard, this.mockBoard.getOpponentBoard()));

    this.mockBoard.updatePlayerShotsRound(this.mockShots);

    CellState[][] resultOpponentBoard =
        new CellState[][] {{CellState.MISS, CellState.MISS, CellState.MISS},
            {CellState.OCEAN, CellState.OCEAN, CellState.OCEAN},
            {CellState.OCEAN, CellState.OCEAN, CellState.OCEAN}};
    assertTrue(Arrays.deepEquals(resultOpponentBoard, this.mockBoard.getOpponentBoard()));
  }

  /**
   * Test for updating the player's board after the opponent fires their shots for that round
   */
  @Test
  public void updateOpponentShotsRound() {
    assertTrue(Arrays.deepEquals(this.initialPlayerBoard, this.mockBoard.getPlayerBoard()));

    this.mockBoard.updateOpponentShotsRound(this.mockShots);

    CellState[][] resultPlayerBoard =
        new CellState[][] {{CellState.MISS, CellState.MISS, CellState.MISS},
            {CellState.SUBMARINE, CellState.OCEAN, CellState.SUBMARINE},
            {CellState.SUBMARINE, CellState.OCEAN, CellState.SUBMARINE}};
    assertTrue(Arrays.deepEquals(resultPlayerBoard, this.mockBoard.getPlayerBoard()));
  }

  /**
   * Test for updating the player's ship damage after the opponent fires the round
   */
  @Test
  public void updatePlayerDamage() {
    assertTrue(Arrays.deepEquals(this.initialPlayerBoard, this.mockBoard.getPlayerBoard()));

    this.mockBoard.updatePlayerDamage(this.shipList, this.mockShots);

    CellState[][] resultPlayerBoard =
        new CellState[][] {{CellState.HIT, CellState.HIT, CellState.SUNK},
            {CellState.SUBMARINE, CellState.OCEAN, CellState.SUNK},
            {CellState.SUBMARINE, CellState.OCEAN, CellState.SUNK}};

    assertTrue(Arrays.deepEquals(resultPlayerBoard, this.mockBoard.getPlayerBoard()));
  }

  /**
   * Test for updating the opponent's ship damage after the player fires the round
   */
  @Test
  public void updateOpponentDamage() {
    assertTrue(Arrays.deepEquals(this.initialOpponentBoard, this.mockBoard.getOpponentBoard()));

    this.mockBoard.updateOpponentDamage(this.shipList, this.mockShots);

    CellState[][] resultOpponentBoard =
        new CellState[][] {{CellState.HIT, CellState.HIT, CellState.SUNK},
            {CellState.OCEAN, CellState.OCEAN, CellState.SUNK},
            {CellState.OCEAN, CellState.OCEAN, CellState.SUNK}};

    assertTrue(Arrays.deepEquals(resultOpponentBoard, this.mockBoard.getOpponentBoard()));
  }

  /**
   * Test for checking if the shot has been taken before
   */
  @Test
  public void isShotTaken() {
    assertFalse(this.mockBoard.isShotTaken(new Coord(0, 0)));

    this.mockBoard.addToShotTaken(new Coord(0, 0));

    assertTrue(this.mockBoard.isShotTaken(new Coord(0, 0)));
  }

  /**
   * Test for adding a new shot to the list of shot taken
   */
  @Test
  public void addToShotTaken() {
    List<Coord> initialShotTaken = new ArrayList<>();
    assertEquals(initialShotTaken, this.mockBoard.getShotsTaken());

    this.mockBoard.addToShotTaken(new Coord(0, 0));

    List<Coord> resultShotTaken = new ArrayList<>(Arrays.asList(new Coord(0, 0)));
    assertEquals(resultShotTaken, this.mockBoard.getShotsTaken());
  }

  /**
   * Test for getting player board
   */
  @Test
  public void getPlayerBoard() {
    assertTrue(Arrays.deepEquals(this.initialPlayerBoard, this.mockBoard.getPlayerBoard()));
  }

  /**
   * Test for getting opponent board
   */
  @Test
  public void getOpponentBoard() {
    assertTrue(Arrays.deepEquals(this.initialOpponentBoard, this.mockBoard.getOpponentBoard()));
  }

  /**
   * Test for getting the ship left from the player
   */
  @Test
  public void getShipsLeft() {
    assertEquals(1, this.mockBoard.getShipsLeft());
  }

  /**
   * Test for getting the shots taken by the player
   */
  @Test
  public void getShotsTaken() {
    List<Coord> initialShotTaken = new ArrayList<>();
    assertEquals(initialShotTaken, this.mockBoard.getShotsTaken());
  }
}