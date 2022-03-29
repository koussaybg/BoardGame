package minimax;

import model.GameState;

public class DoNothingMove implements Move {

	@Override
	public void apply(GameState gameState) {
	}

	@Override
	public String toString() {
		return "DoNothingMove []";
	}

}
