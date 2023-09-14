package cs3500.pa03.model.ships;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.board.CellState;
import cs3500.pa03.model.coordinates.Coord;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for ship class
 */
class ShipTest {

  Ship mockSubmarine;
  Ship mockSunkenSubmarine;
  BoardsState mockBoard;

  /**
   * Initializes ships and boards
   */
  @BeforeEach
  public void initShips() {
    Coord coord1 = new Coord(0, 0);
    Coord coord2 = new Coord(0, 1);
    Coord coord3 = new Coord(0, 2);
    ArrayList<Coord> subArea = new ArrayList<>(Arrays.asList(coord1, coord2, coord3));
    this.mockSubmarine = new Ship(subArea, ShipType.SUBMARINE);

    this.mockSunkenSubmarine = new Ship(subArea, ShipType.SUBMARINE);
    this.mockSunkenSubmarine.isHit(coord1);
    this.mockSunkenSubmarine.isHit(coord2);
    this.mockSunkenSubmarine.isHit(coord3);

    this.mockBoard = new BoardsState();
    this.mockBoard.initializeBoard(3, 3, new ArrayList<Ship>());
  }

  /**
   * Test for containsCoord() method for a ship
   */
  @Test
  public void containsCoord() {
    assertTrue(this.mockSubmarine.containsCoord(new Coord(0, 0)));
    assertFalse(this.mockSubmarine.containsCoord(new Coord(5, 5)));
  }

  /**
   * Test for isHit() method for a ship
   */
  @Test
  public void isHit() {
    assertFalse(this.mockSubmarine.isSunk());

    assertFalse(this.mockSubmarine.isHit(new Coord(4, 4)));
    assertTrue(this.mockSubmarine.isHit(new Coord(0, 0)));
    assertTrue(this.mockSubmarine.isHit(new Coord(0, 1)));
    assertTrue(this.mockSubmarine.isHit(new Coord(0, 2)));

    assertTrue(this.mockSubmarine.isSunk());
  }

  /**
   * Test for isSunk() method for a ship
   */
  @Test
  public void isSunk() {
    assertFalse(this.mockSubmarine.isSunk());
    assertTrue(this.mockSunkenSubmarine.isSunk());
  }

  /**
   * Test for placing a ship on board method
   */
  @Test
  public void placeShipOnBoard() {
    CellState[][] initialBoard =
        new CellState[][] {{CellState.OCEAN, CellState.OCEAN, CellState.OCEAN},
            {CellState.OCEAN, CellState.OCEAN, CellState.OCEAN},
            {CellState.OCEAN, CellState.OCEAN, CellState.OCEAN}};
    assertTrue(Arrays.deepEquals(initialBoard, this.mockBoard.getPlayerBoard()));

    this.mockSubmarine.placeShipOnBoard(this.mockBoard.getPlayerBoard());

    CellState[][] resultBoard =
        new CellState[][] {{CellState.SUBMARINE, CellState.OCEAN, CellState.OCEAN},
            {CellState.SUBMARINE, CellState.OCEAN, CellState.OCEAN},
            {CellState.SUBMARINE, CellState.OCEAN, CellState.OCEAN}};
    assertTrue(Arrays.deepEquals(resultBoard, this.mockBoard.getPlayerBoard()));
  }

  /**
   * Test for placing a sunken ship on board method
   */
  @Test
  public void sinkShip() {
    CellState[][] initialBoard =
        new CellState[][] {{CellState.OCEAN, CellState.OCEAN, CellState.OCEAN},
            {CellState.OCEAN, CellState.OCEAN, CellState.OCEAN},
            {CellState.OCEAN, CellState.OCEAN, CellState.OCEAN}};
    assertTrue(Arrays.deepEquals(initialBoard, this.mockBoard.getPlayerBoard()));

    this.mockSunkenSubmarine.sinkShip(this.mockBoard.getPlayerBoard());

    CellState[][] resultBoard =
        new CellState[][] {{CellState.SUNK, CellState.OCEAN, CellState.OCEAN},
            {CellState.SUNK, CellState.OCEAN, CellState.OCEAN},
            {CellState.SUNK, CellState.OCEAN, CellState.OCEAN}};
    assertTrue(Arrays.deepEquals(resultBoard, this.mockBoard.getPlayerBoard()));
  }

  /**
   * Test for equals method
   */
  @Test
  public void testEquals() {
    String notShip = "not a ship";
    assertTrue(this.mockSubmarine.equals(this.mockSubmarine));
    assertFalse(this.mockSubmarine.equals(notShip));
    assertFalse(this.mockSubmarine.equals(this.mockSunkenSubmarine));
  }

  /**
   * Test for hashCode method
   */
  @Test
  public void testHashCode() {
    assertEquals(591, this.mockSubmarine.hashCode());
    assertEquals(594, this.mockSunkenSubmarine.hashCode());
  }
}