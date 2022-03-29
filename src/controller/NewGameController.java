package controller;

import java.io.File;
import java.util.Collections;
import java.util.List;

import model.Card;
import model.Game;
import model.Player;
import model.Tuple;


/**
 * Controller for new game creation.
 * 
 * @author sopr050
 *
 */


public class NewGameController {
	
	/**
	 * Must be provided to be able to communicate with the other controllers and entities.
	 */

	private SplendorController splendorController;
	
	public NewGameController(SplendorController splendorController) {
		this.splendorController = splendorController;
	}

	/**
	 * @return the splendorController
	 */
	public SplendorController getSplendorController() {
		return splendorController;
	}

	/**
	 * @param splendorController the splendorController to set
	 */
	public void setSplendorController(SplendorController splendorController) {
		this.splendorController = splendorController;
	}

	/**
	 * Creates new game with specific cards loaded.
	 * 
	 * @param playerList the list of players to set
	 * @param cardsFile cards to set
	 * @param noblesFile noble cards to set
	 */
	public void startNewGame(List<Player> playerList, File cardsFile, File noblesFile) {
		List<Card> cards= splendorController.getIOController().loadCards(cardsFile) ;
		List<Card> nobles=splendorController.getIOController().loadNobles(noblesFile) ;
		Game game =new Game(playerList,cards,nobles) ;
		splendorController.getSplendor().setGame(game);
	}
	
	/**
	 * Creates new game without specific cards loaded.
	 * 
	 * @param playerList the list of players to set
	 */

	public void startNewGame(List<Player> playerList) {
      Tuple<List<Card>, List<Card>> cards=getSplendorController().getIOController().getDefaultCards() ;
	  
	  List<Card> developmentCards= cards.getFirstValue();
	  Collections.shuffle(developmentCards);
      List<Card> nobleCards= cards.getSecondValue();
	  Collections.shuffle(nobleCards);

      Game game = new Game(playerList,developmentCards,nobleCards) ;
      splendorController.getSplendor().setGame(game);
	}

}
