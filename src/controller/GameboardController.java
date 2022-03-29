package controller;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Game;
import model.GameState;
import model.GemAmountType;
import model.Player;
import model.PlayerType;
import view.viewAUI.GameboardWindowAUI;

/**
 * 
 * @author sopr057 ( Ben Koussay )
 * 
 *         The GameboardController controls the flow of the game by managing the
 *         gameStates and checking for nobles.
 *
 */
public class GameboardController {

	private GameboardWindowAUI gameboardWindowAUI;

	private SplendorController splendorController;

	/**
	 * default constructor
	 * 
	 * @param splendorController
	 */
	public GameboardController(SplendorController splendorController, GameboardWindowAUI gameboardWindowAUI) {
		this.splendorController = splendorController;
		this.gameboardWindowAUI = gameboardWindowAUI;
	}
	
	/**
	 * @return the gameboardWindowAUI
	 */
	public GameboardWindowAUI getGameboardWindowAUI() {
		return gameboardWindowAUI;
	}

	/**
	 * @param gameboardWindowAUI the gameboardWindowAUI to set
	 */
	public void setGameboardWindowAUI(GameboardWindowAUI gameboardWindowAUI) {
		this.gameboardWindowAUI = gameboardWindowAUI;
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
	 * Checks for the possible nobles and presents them in the view. Starts the next
	 * round if no nobles are available.
	 */
	void checkNobles() {
		Game game = splendorController.getSplendor().getGame();
		Player player = game.getCurrentGameState().getCurrentPlayer();
		List<Card> nobles = game.getCurrentGameState().getNobleCards();
		List<Card> buyable = new ArrayList<Card>();
		for(Card noble:nobles) {
			if(noble.canBuy(player)) {
				buyable.add(noble);
			}
		}
		
		if(buyable.size()>=1) {
			gameboardWindowAUI.showNobles(buyable);
		}	
		if(buyable.size() ==1) {
			game.getCurrentGameState().buyNoble(buyable.get(0));		
			gameboardWindowAUI.refreshNobles();
			gameboardWindowAUI.refreshPlayersHand();
		}
		nextTrain();
	}

	/**
	 * Generates the next gameState. Sets the gameState as the current one.
	 */
	void nextTrain() {
		Game game = splendorController.getSplendor().getGame();
		//check if a player have more than 10 gems
		int amount = game.getCurrentGameState().getCurrentPlayer().getGems().getTotalAmount();
		if(amount > GemAmountType.PLAYER_HAND.getMaxAmount()) {
			gameboardWindowAUI.gemReturn(amount);
		}
		if(!game.getCurrentGameState().isGameOver()) {
			game.cloneGameState();
			//gameboardWindowAUI.refreshCurrentPlayer();
			gameboardWindowAUI.refreshGameState();
			gameboardWindowAUI.refreshCurrentPlayer();
			gameboardWindowAUI.activateUndo(true);
			if(game.getCurrentGameState().getCurrentPlayer().getPlayerType()!=PlayerType.HUMAN) {
				splendorController.getAIController().makeTrain();
			}
		}else {
			endGame();
		}
	}

	/**
	 * Goes back in the gameState list if possible
	 */
	public void undo() {
		Game game = splendorController.getSplendor().getGame();
		game.setHighscoreable(false);
		if (game.getCurrentGameState().getSuccessor() == null) {
			gameboardWindowAUI.showBlitzdings();
		}
		GameState preGamestate = game.getCurrentGameState().getPredecessor();
		if(preGamestate!=null) {
			game.setCurrentGameState(preGamestate);
			gameboardWindowAUI.refreshCurrentPlayer();
			gameboardWindowAUI.refreshGameState();
			gameboardWindowAUI.activateRedo(true);
			if(preGamestate.getPredecessor()==null) {
				gameboardWindowAUI.activateUndo(false);
			}
		}
		
	}

	/**
	 * Goes forward in the gameState list if possible.
	 */
	public void redo() {
		Game game = splendorController.getSplendor().getGame();
		GameState succGamestate = game.getCurrentGameState().getSuccessor();
		if(succGamestate!=null) {
			game.setCurrentGameState(succGamestate);
			gameboardWindowAUI.refreshCurrentPlayer();
			gameboardWindowAUI.refreshGameState();
			gameboardWindowAUI.activateUndo(true);
			if(succGamestate.getSuccessor()==null) {
				gameboardWindowAUI.activateRedo(false);
			}
		}
	}

	/**
	 * Used to continue playing after undoing. Sets current gameState as last
	 * gameState and lets the game go on from here. Any old successing states will
	 * be disconnected completely.
	 */
	public void resume() {
		Game game = splendorController.getSplendor().getGame();
		GameState gamestate = game.getCurrentGameState();
		gamestate.setSuccessor(null);
		gameboardWindowAUI.activateRedo(false);
		if(game.getCurrentGameState().getCurrentPlayer().getPlayerType()!=PlayerType.HUMAN) {
			splendorController.getAIController().makeTrain();
		}
	}

	/**
	 * To be called at the end of the game. Builds the highscore if possible and
	 * closes the current game.
	 */
	public void endGame() {
		Game game = splendorController.getSplendor().getGame();
		GameState lastGameState =game.getCurrentGameState();
		while(lastGameState.getPredecessor()!=null) {
			lastGameState=lastGameState.getPredecessor();
		}
		Player winner = lastGameState.getCurrentPlayer();
		
		if(lastGameState.isGameOver()) {
			for(Player player : lastGameState.getPlayers()) {
				if(player.getPrestige()>winner.getPrestige()) {
					winner = player;
				}else if(player.getPrestige()==winner.getPrestige()) {
					winner =getBoughtCardsWithoutNoble(winner)>getBoughtCardsWithoutNoble(player)?player:winner;
				}
			}
			
			if(game.isHighscoreable()) {
				splendorController.getHighscoreController().createHighscore(game);
			}
		}
		gameboardWindowAUI.gameOver(winner);
	}
	
	private int getBoughtCardsWithoutNoble(Player player){
		int boughtCards=0;
		for(Card card : player.getBoughtCards()) {
			if(!card.isNoble()) {
				boughtCards++;
			}
		}
		return boughtCards;
	}

}
