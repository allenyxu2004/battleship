package cs3500.pa03.model.players;

import cs3500.pa03.model.board.BoardStatePlayer;
import cs3500.pa03.model.coordinates.Coord;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a human player
 */
public class ManualPlayer extends AbstractPlayer {

  private int shotsTakenBefore;

  /**
   * @param playerBoards view access only player board
   * @param random       random seed for testing
   */
  public ManualPlayer(BoardStatePlayer playerBoards, Random random) {
    super(playerBoards, random);
    this.shotsTakenBefore = 0;
  }

  /**
   * @param playerBoards view access only player board
   */
  public ManualPlayer(BoardStatePlayer playerBoards) {
    this(playerBoards, new Random());
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    List<Coord> allShots = this.playerBoard.getShotsTaken();
    int shotsToTake = this.playerBoard.getShipsLeft();
    int boardHeight = this.playerBoard.getPlayerBoard().length;
    int boardWidth = this.playerBoard.getPlayerBoard()[0].length;
    if (boardHeight * boardWidth - this.shotsTakenBefore < shotsToTake) {
      shotsToTake = boardHeight * boardWidth - this.shotsTakenBefore;
    }
    List<Coord> shotsTaken = new ArrayList<>();
    for (int i = allShots.size(); i > allShots.size() - shotsToTake; i--) {
      shotsTaken.add(allShots.get(i - 1));
    }
    this.shotsTakenBefore = allShots.size();
    return shotsTaken;
  }
}
