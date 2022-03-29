package minimax;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import model.Card;
import model.GameState;
import model.Gems;

/**
 * Resembles the buying of the card. The apply method also handles the case,
 * that a noble can be bought.
 * 
 * @author sopr057
 *
 */
public class BuyCardMove implements Move {

	private final Card card;
	private final Card noble;

	BuyCardMove(GameState gameState, Card card) {
		List<Card> nobleCards = gameState.getNobleCards();
		Gems bonus = gameState.getCurrentPlayer().getBonus().clone();
		bonus.getGems()[card.getGemType().ordinal()]++;

		Optional<Card> nobleOpt = nobleCards.stream().filter(nobleCard -> bonus.isGEQThan(nobleCard.getCost()))
				.findFirst();

		if (nobleOpt.isPresent())
			this.noble = nobleOpt.get();
		else
			this.noble = null;

		this.card = card;
	}

	@Override
	public void apply(GameState gameState) {
		gameState.buyCard(card, Gems.calculateTransferAmount(gameState.getCurrentPlayer(), card.getCost()));
		if (noble != null) {
			gameState.buyNoble(noble);
		}
	}

	@Override
	public String toString() {
		String nobleStr = noble != null ? Arrays.toString(noble.getCost().getGems()) : " null ";
		return "BuyCardMove [card=" + card.getLevel() + " " + card.getPrestige() + " " + card.getCost().toString()
				+ ", noble=" + nobleStr + "]";
	}

	/**
	 * @return the card
	 */
	public Card getCard() {
		return card;
	}

	/**
	 * @return the noble
	 */
	public Card getNoble() {
		return noble;
	}

}
