package cs3500.pa03.model.ships;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.coordinates.Coord;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Test for ship types enum
 */
class ShipTypeTest {

  /**
   * Test for generateShipArea() method for a ship type
   */
  @Test
  public void generateShipArea() {
    Coord coord1 = new Coord(0, 0);
    Coord coord2 = new Coord(0, 1);
    Coord coord3 = new Coord(0, 2);
    Coord coord4 = new Coord(0, 3);
    ArrayList<Coord> expectedShipArea =
        new ArrayList<>(Arrays.asList(coord1, coord2, coord3, coord4));

    assertEquals(expectedShipArea, ShipType.DESTROYER.generateShipArea(coord1, true, false));
  }
}