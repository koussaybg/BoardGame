package minimax;

import model.Card;
import model.GameState;

public class ReserveCardMove implements Move {

	private final Card card;

	public ReserveCardMove(Card card) {
		this.card = card;
	}

	/**
	 * @return the card
	 */
	public Card getCard() {
		return card;
	}

	@Override
	public void apply(GameState gameState) {
		gameState.reserveCard(card);
	}

	@Override
	public String toString() {
		return "ReserveCardMove [card=" + card.getLevel() + " " + card.getPrestige() + " " + card.getCost().toString()
				+ "]";
	}

}
