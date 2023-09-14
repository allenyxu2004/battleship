package cs3500.pa04.coordtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.coordinates.CoordComparator;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for the coord comparator
 */
public class ComparatorTest {

  /**
   * Tests the comparator comparing coords
   */
  @Test
  public void testComparator() {
    Coord coord1 = new Coord(3, 4);
    Coord coord2 = new Coord(3, 5);
    assertEquals(-1, new CoordComparator().compare(coord1, coord2));

    Coord coord3 = new Coord(4, 4);
    Coord coord4 = new Coord(3, 4);
    assertEquals(1, new CoordComparator().compare(coord3, coord4));

    assertEquals(0, new CoordComparator().compare(coord3, coord3));

  }


}
