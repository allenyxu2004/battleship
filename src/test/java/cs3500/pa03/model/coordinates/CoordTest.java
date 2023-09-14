package cs3500.pa03.model.coordinates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for coordinates
 */
class CoordTest {

  Coord coord1;
  Coord coord2;
  Coord coord3;
  Coord sameAsCoord1;
  String notCoord;

  /**
   * Set up coordinates for testing
   */
  @BeforeEach
  public void initCoord() {
    this.coord1 = new Coord(1, 2);
    this.coord2 = new Coord(2, 3);
    this.coord3 = new Coord(1, 3);
    this.sameAsCoord1 = new Coord(1, 2);
    this.notCoord = "Not a coord";
  }


  /**
   * Test equals method
   */
  @Test
  public void testEquals() {
    assertTrue(this.coord1.equals(this.coord1));
    assertTrue(this.coord1.equals(this.sameAsCoord1));
    assertFalse(this.coord1.equals(this.coord2));
    assertFalse(this.coord1.equals(this.notCoord));
    assertFalse(this.coord1.equals(this.coord3));
  }

  /**
   * Test hashcode method
   */
  @Test
  public void testHashCode() {
    assertEquals(1641, this.coord1.hashCode());
    assertEquals(1641, this.sameAsCoord1.hashCode());
    assertEquals(2735, this.coord2.hashCode());
  }
}