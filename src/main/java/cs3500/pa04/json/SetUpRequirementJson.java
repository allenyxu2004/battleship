package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.ships.ShipType;
import java.util.Map;

/**
 * Represents the json for setting up the game
 *
 * @param width of board
 * @param height of board
 * @param specifications the ship types to be placed
 */
public record SetUpRequirementJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> specifications) {
}
