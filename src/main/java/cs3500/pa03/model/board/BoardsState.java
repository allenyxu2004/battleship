package cs3500.pa03.model.board;

import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the current state of the board
 */
public class BoardsState implements BoardStatePlayer {

  /**
   * the player's board where they place their ships on
   */
  private CellState[][] playerBoard;

  /**
   * the board where players mark their shots
   */
  private CellState[][] opponentBoard;

  /**
   * All shots taken by the player
   */
  private List<Coord> shotsTaken;

  /**
   * Player's ship
   */
  private List<Ship> shipList;

  @Override
  public void initializeBoard(int height, int width, List<Ship> ships) {
    this.playerBoard = this.setupBoard(height, width);
    this.opponentBoard = this.setupBoard(height, width);

    this.shipList = ships;
    this.shotsTaken = new ArrayList<>();

    this.placeShipsOnPlayerBoard(ships);
  }

  /**
   * @param height the height of the board
   * @param width the width of the board
   * @return the initial state of the board after set up
   */
  private CellState[][] setupBoard(int height, int width) {
    CellState[][] board = new CellState[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        board[row][col] = CellState.OCEAN;
      }
    }
    return board;
  }

  public List<Ship> getPlayerShipList() {
    return this.shipList;
  }

  /**
   * @param shotRound the shots the player fired that round
   */
  public void updatePlayerShotsRound(List<Coord> shotRound) {
    this.updateShotsRound(this.opponentBoard, shotRound);
  }

  /**
   * @param shotRound the shots the opponent fired that round
   */
  public void updateOpponentShotsRound(List<Coord> shotRound) {
    this.updateShotsRound(this.playerBoard, shotRound);
  }

  /**
   * @param board the board to update the round of shots on
   * @param shotRound the shots fired that round
   */
  private void updateShotsRound(CellState[][] board, List<Coord> shotRound) {
    for (Coord shots : shotRound) {
      board[shots.posy][shots.posx] = CellState.MISS;
    }
  }

  /**
   * @param playerShips the list of player's ship
   * @param playerDamage the list of shots that hits the player's ship
   */
  public void updatePlayerDamage(List<Ship> playerShips, List<Coord> playerDamage) {
    this.updateDamage(this.playerBoard, playerShips, playerDamage);
  }

  /**
   * @param opponentShips the list of opponent's ship
   * @param opponentDamage the list of shots the hits the opponent's ship
   */
  public void updateOpponentDamage(List<Ship> opponentShips, List<Coord> opponentDamage) {
    this.updateDamage(this.opponentBoard, opponentShips, opponentDamage);
  }

  /**
   * @param opponentDamage the successful hits on the opponent
   */
  public void updateOpponentDamage(List<Coord> opponentDamage) {
    for (Coord coord : opponentDamage) {
      this.opponentBoard[coord.posy][coord.posx] = CellState.HIT;
    }
  }

  /**
   * @param board the board to be updated
   * @param ships the list of ships
   * @param shotsHit the list of shots that hits the ships in the given list
   */
  private void updateDamage(CellState[][] board, List<Ship> ships, List<Coord> shotsHit) {
    for (Coord coord : shotsHit) {
      board[coord.posy][coord.posx] = CellState.HIT;
    }
    for (Ship ship : ships) {
      if (ship.isSunk()) {
        ship.sinkShip(board);
      }
    }
  }

  /**
   * @param shipList the list of ships to be placed onto the board
   */
  private void placeShipsOnPlayerBoard(List<Ship> shipList) {
    for (Ship ship : shipList) {
      ship.placeShipOnBoard(this.playerBoard);
    }
  }

  /**
   * @param target the shot to be taken
   * @return has the given shot been taken before?
   */
  public boolean isShotTaken(Coord target) {
    return shotsTaken.contains(target);
  }

  /**
   * @param target the shot to be added to the shotTaken list
   */
  public void addToShotTaken(Coord target) {
    this.shotsTaken.add(target);
  }

  /**
   * @return gets the player's board in this board state
   */
  @Override
  public CellState[][] getPlayerBoard() {
    return this.playerBoard;
  }

  /**
   * @return gets the opponent's board in this board state
   */
  @Override
  public CellState[][] getOpponentBoard() {
    return this.opponentBoard;
  }

  /**
   * @return gets the remaining ships left on the board
   */
  @Override
  public int getShipsLeft() {
    int shipCount = 0;
    for (Ship ship : this.shipList) {
      if (!ship.isSunk()) {
        shipCount++;
      }
    }
    return shipCount;
  }

  /**
   * @return gets the list of shots taken before
   */
  @Override
  public List<Coord> getShotsTaken() {
    return this.shotsTaken;
  }
}
