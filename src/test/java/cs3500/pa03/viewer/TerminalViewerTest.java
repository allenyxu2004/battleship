package cs3500.pa03.viewer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test terminal viewer class
 */
class TerminalViewerTest {

  TerminalViewer viewer;
  StringBuilder output;
  StringReader input;

  /**
   * Initializes viewer, input and output streams needed for testing
   */
  @BeforeEach
  public void initTerminalViewer() {
    this.output = new StringBuilder();
    this.input = new StringReader("a user input");
    this.viewer = new TerminalViewer(this.input, this.output);
  }

  /**
   * Test for displaying a given string to terminal
   */
  @Test
  public void displayText() {
    assertEquals("", this.output.toString());

    this.viewer.displayText("An output string\n");

    assertEquals("An output string\n", this.output.toString());
  }

  /**
   * Test for displaying a given board to terminal
   */
  @Test
  public void displayBoard() {
    BoardsState mockBoard = new BoardsState();
    Coord coord1 = new Coord(0, 0);
    Coord coord2 = new Coord(0, 1);
    Coord coord3 = new Coord(0, 2);
    ArrayList<Coord> subArea = new ArrayList<>(Arrays.asList(coord1, coord2, coord3));
    Ship mockSubmarine = new Ship(subArea, ShipType.SUBMARINE);
    mockBoard.initializeBoard(3, 3, List.of(mockSubmarine));

    assertEquals("", this.output.toString());

    this.viewer.displayBoard(mockBoard.getPlayerBoard());

    String row0 = "  | 0 1 2\n";
    String row1 = "----------\n";
    String row2 = "0 | S _ _\n";
    String row3 = "1 | S _ _\n";
    String row4 = "2 | S _ _\n\n";
    String resultBoard = row0 + row1 + row2 + row3 + row4;

    assertEquals(resultBoard, this.output.toString());
  }

  /**
   * Test for retrieving user input
   */
  @Test
  public void getUserInput() {
    assertEquals("a user input", this.viewer.getUserInput());
  }
}