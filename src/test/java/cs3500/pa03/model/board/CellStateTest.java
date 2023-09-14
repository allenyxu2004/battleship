package cs3500.pa03.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test for CellState class
 */
class CellStateTest {

  /**
   * Test for method toChar
   */
  @Test
  public void toChar() {
    assertEquals('C', CellState.CARRIER.toChar());
    assertEquals('B', CellState.BATTLESHIP.toChar());
    assertEquals('D', CellState.DESTROYER.toChar());
    assertEquals('S', CellState.SUBMARINE.toChar());
    assertEquals('*', CellState.HIT.toChar());
    assertEquals('.', CellState.MISS.toChar());
    assertEquals('/', CellState.SUNK.toChar());
    assertEquals('_', CellState.OCEAN.toChar());
  }
}