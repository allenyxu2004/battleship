package cs3500.pa03.controller.validator;

/**
 * @param <T> the item to be returned after validating
 */
public interface BattleshipValidator<T> {
  /**
   * @param input a user input as a string
   * @return type T after validation
   */
  public T validate(String input);
}
