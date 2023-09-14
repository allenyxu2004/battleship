package cs3500.pa03.model.players;

import cs3500.pa03.model.board.BoardStatePlayer;
import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a computer player
 */
public class BotPlayer extends AbstractPlayer {



  /**
   * Represents a bot player
   *
   * @param random random seed for testing
   */
  public BotPlayer(BoardStatePlayer boardStatePlayer, Random random) {
    super(boardStatePlayer, random);
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      System.exit(1);
    }
    List<Coord> shots = new ArrayList<>();
    for (Ship ship : this.playerBoard.getPlayerShipList()) {
      if (!ship.isSunk() && this.emptyCoord.size() > 0) {
        shots.add(this.emptyCoord.remove(0));
      }
    }
    return shots;
  }
}
