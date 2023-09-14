package cs3500.pa04.shipteste;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import cs3500.pa04.ShipAdapter;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for a ship adaptor
 */
public class TestShipAdaptor {

  ShipAdapter ship;

  /**
   * Intalizes a ship adapter for testing
   */
  @BeforeEach
  public void initTest() {
    ArrayList<Coord> shipCords = new ArrayList<>();
    shipCords.add(new Coord(0, 0));
    shipCords.add(new Coord(1, 0));
    shipCords.add(new Coord(2, 0));
    Ship sub = new Ship(shipCords, ShipType.SUBMARINE);
    ship = new ShipAdapter(sub);
  }

  /**
   * Tests obtaining inital coord of ship
   */
  @Test
  public void testGetCoord() {
    assertEquals(new Coord(0, 0), ship.getCoord());
  }

  /**
   * Tests obtaining length of ship
   */
  @Test
  public void testGetLength() {
    assertEquals(3, ship.getLength());
  }

}
