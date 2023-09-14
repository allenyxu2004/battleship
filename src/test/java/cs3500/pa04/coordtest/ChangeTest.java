package cs3500.pa04.coordtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.coordinates.Coord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for coord changing methods
 */
public class ChangeTest {
  private Coord actual;

  /**
   * Sets up a single coord to be tested
   */
  @BeforeEach
  public void initCoords() {
    actual = new Coord(1, 1);
  }

  /**
   * Subtracts y coord
   */
  @Test
  public void changeTestSubY() {
    Coord expected1 = new Coord(1, 0);
    assertEquals(expected1, actual.changeY(false));
  }

  /**
   * Adds to y coord
   */
  @Test
  public void changeTestAddY() {
    Coord expected1 = new Coord(1, 2);
    assertEquals(expected1, actual.changeY(true));
  }

  /**
   * Subtracts x coord
   */
  @Test
  public void changeTestSubX() {
    Coord expected1 = new Coord(0, 1);
    assertEquals(expected1, actual.changeX(false));
  }

  /**
   * Adds to x coord
   */
  @Test
  public void changeTestAddX() {
    Coord expected1 = new Coord(2, 1);
    assertEquals(expected1, actual.changeX(true));
  }

  /**
   * Tests checking coordinates in bounds based off height width
   */
  @Test
  public void testBounds() {
    Coord inBounds = new Coord(5, 6);
    Coord outBounds = new Coord(15, 12);

    assertTrue(inBounds.inBounds(7, 6));
    assertFalse(outBounds.inBounds(6, 6));
    assertFalse(outBounds.inBounds(16, 5));
    assertFalse(outBounds.inBounds(15, 12));
    assertFalse(outBounds.inBounds(12, 15));

  }

}

