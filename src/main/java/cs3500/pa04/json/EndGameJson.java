package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the end game json
 *
 * @param win the win message
 * @param reason the reason for win
 */
public record EndGameJson(
    @JsonProperty("result") String win,
    @JsonProperty("reason") String reason) {
}
