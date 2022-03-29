package model;

public enum GemAmountType {
	UNLIMITED(Integer.MAX_VALUE), GAMEBOARD_FOUR_PLAYERS(40), GAMEBOARD_THREE_PLAYERS(30), GAMEBOARD_TWO_PLAYERS(25), PLAYER_HAND(10);

	private final int maxAmount;

	private GemAmountType(int maxAmount) {
		this.maxAmount = maxAmount;
	}

	/**
	 * @return the maxAmount
	 */
	public int getMaxAmount() {
		return maxAmount;
	}

}
