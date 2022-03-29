package minimax;

import model.GameState;

/**
 * Interface to make a AI calculated move applicable in a playerController.
 * 
 * @author sopr057
 *
 */
public interface Move {
	/**
	 * Applies the given move by calling the specific methods in the given
	 * gameState.
	 */
	public void apply(GameState gameState);
}
