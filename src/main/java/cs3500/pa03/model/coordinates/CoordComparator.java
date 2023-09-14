package cs3500.pa03.model.coordinates;

import java.util.Comparator;

/**
 * Represents a comparator for coords
 */
public class CoordComparator implements Comparator<Coord> {

  /**
   * Compares 2 coords
   *
   * @param posn1 the first object to be compared.
   * @param posn2 the second object to be compared.
   * @return the difference between the two
   */
  @Override
  public int compare(Coord posn1, Coord posn2) {
    // Compare x coordinates first
    int comparex = Integer.compare(posn1.posx, posn2.posx);
    if (comparex != 0) {
      return comparex;
    }

    // If x coordinates are equal, compare y coordinates
    return Integer.compare(posn1.posy, posn2.posy);
  }

}
