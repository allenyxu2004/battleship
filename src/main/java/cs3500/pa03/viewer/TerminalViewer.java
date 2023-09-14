package cs3500.pa03.viewer;

import cs3500.pa03.model.board.CellState;
import cs3500.pa03.viewer.writer.WriterAppendable;
import java.util.Scanner;

/**
 * Represents a viewer that's only shown in terminal
 */
public class TerminalViewer implements BattleshipViewer {
  /**
   * Something that can be appended to
   */
  private final WriterAppendable output;

  /**
   * Scanner that reads user's input
   */
  private final Scanner input;

  /**
   * @param readable something that can be scanned by the scanner
   * @param appendable something that can appended to
   */
  public TerminalViewer(Readable readable, Appendable appendable) {
    this.output = new WriterAppendable(appendable);
    this.input = new Scanner(readable);
  }

  /**
   * @param text the string to be displayed
   */
  @Override
  public void displayText(String text) {
    this.output.write(text);
  }

  /**
   * @param board the board to be displayed
   */
  @Override
  public void displayBoard(CellState[][] board) {
    StringBuilder columnIndex = new StringBuilder();
    columnIndex.append("  | " + 0 + " ");
    for (int col = 1; col < board[0].length; col++) {
      columnIndex.append(Integer.toHexString(col).toUpperCase() + " ");
    }
    this.output.write(columnIndex.toString().substring(0, columnIndex.length() - 1));
    this.output.write("\n----" + "-".repeat(board[0].length * 2) + "\n");
    for (int row = 0; row < board.length; row++) {
      StringBuilder rowState = new StringBuilder();
      rowState.append(Integer.toHexString(row).toUpperCase() + " | ");
      for (CellState cellState : board[row]) {
        rowState.append(cellState.toChar() + " ");
      }
      this.output.write(rowState.toString().trim() + "\n");
    }
    this.output.write("\n");
  }

  /**
   * @return the input of the user as a string
   */
  @Override
  public String getUserInput() {
    return this.input.nextLine();
  }
}
