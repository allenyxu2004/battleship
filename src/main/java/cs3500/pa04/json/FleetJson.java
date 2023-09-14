package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.ShipAdapter;
import java.util.List;

/**
 * Represents a fleet in json
 *
 * @param fleet the list of ship adapters
 */
public record FleetJson(
    @JsonProperty("fleet") List<ShipAdapter> fleet) {
}
