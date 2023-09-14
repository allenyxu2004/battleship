package cs3500.pa04.shipteste;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for ship getter and setters
 */
public class TestShipGetSet {

  Ship testShip;
  ArrayList<Coord> coords;

  /**
   * Initalizes tests
   */
  @BeforeEach
  public void initTests() {
    coords = new ArrayList<>();
    coords.add(new Coord(0, 0));
    coords.add(new Coord(1, 0));
    coords.add(new Coord(2, 0));
    testShip = new Ship(coords, ShipType.SUBMARINE);
  }

  /**
   * Tests obtaining ship types
   */
  @Test
  public void testShipType() {
    assertEquals(ShipType.SUBMARINE, testShip.getShipType());
  }

  /**
   * Tests obtaining a ship sink status
   */
  @Test
  public void testSinkStatus() {
    assertEquals(false, testShip.getSinkStatus());
    testShip.setSinkStatus(true);
    assertEquals(true, testShip.getSinkStatus());
  }

  /**
   * Tests obtaining ship cords
   */
  @Test
  public void testShipCoords() {
    assertEquals(coords, testShip.getShipCoords());
  }

  /**
   * Tests seeing if a ship is vertical
   */
  @Test
  public void testVertical() {
    assertEquals(false, testShip.isVertical());
  }

  /**
   * Tests obtaining the ship start coord
   */
  @Test
  public void testStartCoord() {
    Coord start = new Coord(0, 0);
    assertEquals(start, testShip.getStartCoord());
  }

  /**
   * Tests if a coord overlaps with ship
   */
  @Test
  public void testCoordOverlap() {
    Coord coord1 = new Coord(1, 0);
    Coord coord2 = new Coord(2, 2);
    assertEquals(true, testShip.coordOverlap(coord1));
    assertEquals(false, testShip.coordOverlap(coord2));
  }

}
