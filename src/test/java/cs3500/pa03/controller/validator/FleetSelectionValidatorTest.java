package cs3500.pa03.controller.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cs3500.pa03.model.ships.ShipType;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test for fleet selection validator
 */
class FleetSelectionValidatorTest {

  /**
   * Test for valid inputs
   */
  @Test
  public void validate() {
    FleetSelectionValidator validator = new FleetSelectionValidator(6);
    Map<ShipType, Integer> expectedMap1 =
        Map.of(ShipType.CARRIER, 1, ShipType.BATTLESHIP, 2, ShipType.DESTROYER,
            2, ShipType.SUBMARINE, 1);
    assertEquals(expectedMap1, validator.validate("1 2 2 1"));

    Map<ShipType, Integer> expectedMap2 =
        Map.of(ShipType.CARRIER, 3, ShipType.BATTLESHIP, 1, ShipType.DESTROYER,
            1, ShipType.SUBMARINE, 1);
    assertEquals(expectedMap2, validator.validate("3 1 1 1"));
  }

  /**
   * Test for invalid inputs
   */
  @Test
  public void validateExceptions() {
    FleetSelectionValidator validator = new FleetSelectionValidator(6);
    assertThrows(IllegalArgumentException.class, () -> validator.validate("1 0 0 0"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("-1 -1 -1 0"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("1 2 2 2"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("1 2 2 2 2 2"));
    assertThrows(IllegalArgumentException.class, () -> validator.validate("1 2 2"));
  }
}