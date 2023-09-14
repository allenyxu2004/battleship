package cs3500.pa03.controller.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for take shots validator
 */
class TakeShotsValidatorTest {

  BoardsState mockBoard;

  /**
   * Initializes the mock board for testing
   */
  @BeforeEach
  public void initBoard() {
    this.mockBoard = new BoardsState();
    this.mockBoard.initializeBoard(6, 7, new ArrayList< Ship >());
    this.mockBoard.addToShotTaken(new Coord(0, 0));
    this.mockBoard.addToShotTaken(new Coord(1, 0));
    this.mockBoard.addToShotTaken(new Coord(2, 0));
  }

  /**
   * Test for valid user inputs
   */
  @Test
  public void validate() {
    TakeShotsValidator validator = new TakeShotsValidator(this.mockBoard);
    assertEquals(new Coord(5, 5), validator.validate("5 5"));
    assertEquals(new Coord(6, 5), validator.validate("6 5"));
  }

  /**
   * Test for invalid user inputs
   */
  @Test
  public void validateExceptions() {
    TakeShotsValidator validator = new TakeShotsValidator(this.mockBoard);
    assertThrows(IllegalArgumentException.class, () -> validator.validate("5"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("5 6 2"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("0 0"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("6 7"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("7 5"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("-1 -2"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("1 -2"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("-1 2"));
  }
}