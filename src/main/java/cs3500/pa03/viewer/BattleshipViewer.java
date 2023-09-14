package cs3500.pa03.viewer;

import cs3500.pa03.model.board.CellState;

/**
 * Represents a viewer to display to the user
 */
public interface BattleshipViewer {
  /**
   * @param text the string to be displayed
   */
  public void displayText(String text);

  /**
   * @param board the board to be displayed
   */
  public void displayBoard(CellState[][] board);

  /**
   * @return the input of the user as a string
   */
  public String getUserInput();
}
