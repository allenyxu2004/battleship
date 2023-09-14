package cs3500.pa03.controller.validator;

import cs3500.pa03.controller.converter.Converter;
import cs3500.pa03.model.ships.ShipType;
import java.util.Map;

/**
 * A class responsible for validating fleet selection from the user
 */
public class FleetSelectionValidator implements BattleshipValidator<Map<ShipType, Integer>> {

  /**
   * The smaller dimension of the board dimension. This is needed to validate fleet size.
   */
  private int smallerDimension;

  /**
   * @param smallerDimension The smaller dimension of the board dimension.
   *                         This is needed to validate fleet size.
   */
  public FleetSelectionValidator(int smallerDimension) {
    this.smallerDimension = smallerDimension;
  }

  /**
   * @param input a user input as a string
   * @return The fleet selection format as a map of ShipType and Integer
   * @throws IllegalArgumentException user inputs invalid fleet selection
   */
  @Override
  public Map<ShipType, Integer> validate(String input) throws IllegalArgumentException {
    int totalFleetSize = 0;
    Integer[] arrInt = Converter.convertStrToIntArray(input);
    if (arrInt.length != 4) {
      throw new IllegalArgumentException("Should have exactly four numbers to set fleet size!");
    }
    for (Integer n : arrInt) {
      if (n <= 0) {
        throw new IllegalArgumentException("Should have at least one for each ship!");
      }
      totalFleetSize += n;
    }
    if (totalFleetSize > this.smallerDimension) {
      throw new IllegalArgumentException("Fleet size cannot exceed smaller dimension!");
    }
    Map<ShipType, Integer> result = Converter.convertIntArrayToMapShipType(arrInt);
    return result;
  }
}
