package cs3500.pa03.controller.validator;

import cs3500.pa03.controller.converter.Converter;

/**
 * A class that validates user inputs for board dimensions
 */
public class BoardDimensionValidator implements BattleshipValidator<Integer[]> {

  /**
   * The minimum height/width for a board dimension
   */
  private static final int MIN_BOARD_DIMENSION = 6;

  /**
   * The maximum height/width for a board dimension
   */
  private static final int MAX_BOARD_DIMENSION = 15;

  /**
   * @param input a user input as a string
   * @return an array of integers containing the height and width of the board
   * @throws IllegalArgumentException user's input is not in the correct format
   */
  @Override
  public Integer[] validate(String input) throws IllegalArgumentException {
    Integer[] result = Converter.convertStrToIntArray(input);
    if (result.length != 2) {
      throw new IllegalArgumentException("Should only have two inputs for board dimension!");
    }
    if (result[0] < MIN_BOARD_DIMENSION || result[1] > MAX_BOARD_DIMENSION) {
      throw new IllegalArgumentException(
          "Board dimension should be in between 6 and 15 (inclusive)!");
    }
    return result;
  }
}
