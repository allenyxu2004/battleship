package cs3500.pa03.model.board;

/**
 * Represents the current items occupying a coordinate/cell in a battleship game
 */
public enum CellState {

  /**
   * Represents a carrier
   */
  CARRIER('C'),

  /**
   * Represents a battleship
   */
  BATTLESHIP('B'),

  /**
   * Represents a destroyer
   */
  DESTROYER('D'),

  /**
   * Represents a submarine
   */
  SUBMARINE('S'),

  /**
   * Represents a hit on a ship
   */
  HIT('*'),

  /**
   * Represents a missed shot
   */
  MISS('.'),

  /**
   * Represents a sunken ship
   */
  SUNK('/'),

  /**
   * Represents the ocean
   */
  OCEAN('_');

  /**
   * The character representation of the items above
   */
  private char charDisplay;

  /**
   * @param charDisplay The character representation of the items above
   */
  CellState(char charDisplay) {
    this.charDisplay = charDisplay;
  }

  /**
   * @return The character representation of this CellState
   */
  public char toChar() {
    return this.charDisplay;
  }
}
