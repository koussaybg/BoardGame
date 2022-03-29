package view.viewAUI;

import java.util.List;

import model.Card;
import model.Player;

public interface GameboardWindowAUI {

	void refreshPlayersHand();

	void refreshCurrentPlayer();

	void refreshCards();

	void refreshGems();

	void refreshNobles();

	void activateUndo(boolean active);

	void activateRedo(boolean active);

	void refreshGameState();

	void showTip(Card card, boolean buy);

	void gameOver(Player winner);

	/**
	 * Shows the NobleCards, from which the Player has to choose. If the List contains only one element,
	 * show the Card and resume automatically after e.g. 1 sec.
	 * 
	 * @param nobles Noble-Cards to display
	 */
	void showNobles(List<Card> nobles);

	void showBlitzdings();

	void hideBlitzdings();

	void gemReturn(int amount) ;

}
