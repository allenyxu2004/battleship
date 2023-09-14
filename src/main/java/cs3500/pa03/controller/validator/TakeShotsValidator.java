package cs3500.pa03.controller.validator;

import cs3500.pa03.controller.converter.Converter;
import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.coordinates.Coord;

/**
 * A class to validate user's shots
 */
public class TakeShotsValidator implements BattleshipValidator<Coord> {

  /**
   * The current board the user is playing on
   */
  private BoardsState boardsState;

  /**
   * @param boardsState The current board the user is playing on
   */
  public TakeShotsValidator(BoardsState boardsState) {
    this.boardsState = boardsState;
  }

  /**
   * Represents a validate method
   *
   * @param input a user input as a string
   * @return a valid shot coordinate
   * @throws IllegalArgumentException the user's shot coordinate is invalid
   */
  @Override
  public Coord validate(String input) throws IllegalArgumentException {
    int height = this.boardsState.getPlayerBoard().length;
    int width = this.boardsState.getPlayerBoard()[0].length;
    Integer[] coordArray = Converter.convertStrToIntArray(input);
    if (coordArray.length != 2) {
      throw new IllegalArgumentException("There must be 2 numbers to represent x and y!");
    }
    Integer x = coordArray[0];
    Integer y = coordArray[1];
    if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
      throw new IllegalArgumentException("Coordinates are outside of board dimension!");
    }
    Coord result = new Coord(x, y);
    if (this.boardsState.isShotTaken(result)) {
      throw new IllegalArgumentException("Shots already taken!");
    }
    return result;
  }
}
