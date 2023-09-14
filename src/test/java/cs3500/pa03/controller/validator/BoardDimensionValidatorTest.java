package cs3500.pa03.controller.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Test for board dimension validator
 */
class BoardDimensionValidatorTest {

  /**
   * Test for valid inputs
   */
  @Test
  public void validate() {
    BoardDimensionValidator validator = new BoardDimensionValidator();
    assertTrue(Arrays.equals(new Integer[]{8, 8}, validator.validate("8 8")));
    assertTrue(Arrays.equals(new Integer[]{9, 10}, validator.validate("9 10  ")));
  }

  /**
   * Test for invalid inputs
   */
  @Test
  public void validateExceptions() {
    BoardDimensionValidator validator = new BoardDimensionValidator();
    assertThrows(IllegalArgumentException.class, () -> validator.validate("not number"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("1 2 4"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("1"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("5 10"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("6 16"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("5 16"));
  }
}