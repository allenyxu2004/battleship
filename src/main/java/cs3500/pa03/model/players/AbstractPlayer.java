package cs3500.pa03.model.players;

import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.board.BoardStatePlayer;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents an abstract player to abstract common functionalities
 */
public abstract class AbstractPlayer implements Player {
  /**
   * Random seed for testing
   */
  protected Random random;

  /**
   * The coordinates that this player did not shoot yet
   */
  protected List<Coord> emptyCoord;

  /**
   * player's board with modified access
   */
  protected BoardStatePlayer playerBoard;

  /**
   * Represents an abstract player
   *
   * @param playerBoard the player's board
   * @param random the randomization method
   */
  public AbstractPlayer(BoardStatePlayer playerBoard, Random random) {
    this.playerBoard = playerBoard;
    this.random = random;
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "pa04-idk";
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    List<Ship> shipSetUp = new ArrayList<>();
    ArrayList<Coord> availableCoord = this.generateAllCoordinates(height, width);
    ShipType[] shipTypeList = specifications.keySet().toArray(new ShipType[0]);
    Arrays.sort(shipTypeList);
    for (ShipType shipType : shipTypeList) {
      Integer noOfShipType = specifications.get(shipType);
      for (int i = 0; i < noOfShipType; i++) {
        Collections.shuffle(availableCoord, this.random);
        boolean placedShip = false;
        for (int index = 0; index < availableCoord.size(); index++) {
          ArrayList<ArrayList<Coord>> allShipArea =
              this.generateShipAreaList(availableCoord.get(index), shipType);
          for (ArrayList<Coord> shipArea : allShipArea) {
            if (this.isValidShipArea(shipArea, shipSetUp, height, width)) {
              Ship ship = new Ship(shipArea, shipType);
              shipSetUp.add(ship);
              this.removeCoord(availableCoord, shipArea);
              placedShip = true;
              break;
            }
          }
          if (placedShip) {
            break;
          }
        }
      }
    }
    this.playerBoard.initializeBoard(height, width, shipSetUp);
    this.emptyCoord = this.generateAllCoordinates(height, width);
    Collections.shuffle(this.emptyCoord, this.random);
    return shipSetUp;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a ship
   *         on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> shotsHit = new ArrayList<>();
    for (Coord shots : opponentShotsOnBoard) {
      for (Ship ship : this.playerBoard.getPlayerShipList()) {
        if (ship.isHit(shots)) {
          shotsHit.add(shots);
        }
      }
    }
    return shotsHit;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    return;
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    return;
  }

  /**
   * @param coordList  list containing coordinates
   * @param removeList list containing coordinates to be removed
   */
  private void removeCoord(ArrayList<Coord> coordList, ArrayList<Coord> removeList) {
    for (Coord coord : removeList) {
      coordList.remove(coord);
    }
  }

  /**
   * @param randomCoord a random coordinate
   * @param shipType    the type of ship to generate the ship area list
   * @return a list of ship area going in all four direction
   */
  private ArrayList<ArrayList<Coord>> generateShipAreaList(Coord randomCoord,
                                                           ShipType shipType) {
    ArrayList<ArrayList<Coord>> shipAreaList = new ArrayList<>();
    boolean vertical = this.random.nextBoolean();
    boolean inverted = this.random.nextBoolean();
    shipAreaList.add(shipType.generateShipArea(randomCoord, vertical, inverted));
    shipAreaList.add(shipType.generateShipArea(randomCoord, vertical, !inverted));
    shipAreaList.add(shipType.generateShipArea(randomCoord, !vertical, inverted));
    shipAreaList.add(shipType.generateShipArea(randomCoord, !vertical, !inverted));
    return shipAreaList;
  }

  /**
   * @param shipArea  the list of coordinates that the ship takes up
   * @param shipSetUp the list of ships this player currently have
   * @param height    the height of the board
   * @param width     the width of the board
   * @return whether the ship area is valid or not
   */
  private boolean isValidShipArea(ArrayList<Coord> shipArea, List<Ship> shipSetUp, int height,
                                  int width) {
    for (Coord coord : shipArea) {
      for (Ship ship : shipSetUp) {
        if (ship.containsCoord(coord)) {
          return false;
        }
      }
      if (coord.posx < 0 || coord.posx > width - 1 || coord.posy < 0 || coord.posy > height - 1) {
        return false;
      }
    }
    return true;
  }

  /**
   * @param height the height of the board
   * @param width  the width of the board
   * @return an array list of all the coordinates on the board
   */
  private ArrayList<Coord> generateAllCoordinates(int height, int width) {
    ArrayList<Coord> availableCoord = new ArrayList<>();
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        availableCoord.add(new Coord(col, row));
      }
    }
    return availableCoord;
  }
}
