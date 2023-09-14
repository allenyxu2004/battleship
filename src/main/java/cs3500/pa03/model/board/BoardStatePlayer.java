package cs3500.pa03.model.board;

import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import java.util.List;

/**
 * Represents a read only board state
 */
public interface BoardStatePlayer {
  /**
   * @return gets the player's board in this board state
   */
  CellState[][] getPlayerBoard();

  /**
   * @return gets the opponent's board in this board state
   */
  CellState[][] getOpponentBoard();

  /**
   * @return gets the remaining ships left on the board
   */
  int getShipsLeft();

  /**
   * @return gets the list of shots taken before
   */
  List<Coord> getShotsTaken();

  /**
   * Obtains the player's ship list
   *
   * @return the ship list
   */
  List<Ship> getPlayerShipList();

  /**
   * Initalizes the player baord
   *
   * @param height board height
   * @param width board width
   * @param shipList list of ships
   */
  void initializeBoard(int height, int width, List<Ship> shipList);
}
