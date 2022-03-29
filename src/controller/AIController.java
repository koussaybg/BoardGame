package controller;

import java.util.HashMap;

import minimax.BuyCardMove;
import minimax.MiniMaxAI;
import minimax.Move;
import minimax.ReserveCardMove;
import model.Card;
import model.GameState;
import model.PlayerType;
import view.viewAUI.GameboardWindowAUI;

/**
 * @author sopr051
 *
 *         The AIController controls the AI of the Game
 */
public class AIController {

	private SplendorController splendorController;
	private GameboardWindowAUI gameboardWindowAUI;

	private HashMap<GameState, Integer> transpositionTable;

	public AIController(SplendorController splendorController, GameboardWindowAUI gameboardWindowAUI) {
		this.splendorController = splendorController;
		this.gameboardWindowAUI = gameboardWindowAUI;

		transpositionTable = new HashMap<GameState, Integer>();
	}

	/**
	 * Generate a Tip (according the condition) to Help the player
	 */
	public void showTip() {

		GameState currentGameState = splendorController.getSplendor().getGame().getCurrentGameState();
		MiniMaxAI miniMax = new MiniMaxAI(currentGameState, 5, transpositionTable);
		Move move = miniMax.getBestMove();
		boolean buy = false;
		Card card;
		if (move instanceof BuyCardMove) {
			card = ((BuyCardMove) move).getCard();
			buy = true;
		} else if (move instanceof ReserveCardMove) {
			card = ((ReserveCardMove) move).getCard();
		} else {
			// dummy
			card = currentGameState.getOpenCards().get(0);
			buy = false;
		}
		splendorController.getSplendor().getGame().setHighscoreable(false);
		gameboardWindowAUI.showTip(card, buy);

	}

	/**
	 *
	 */
	void makeTrain() {
		System.out.println("AI called");
		GameState currentGameState = splendorController.getSplendor().getGame().getCurrentGameState();

		int searchDepth = 1;
		if (currentGameState.getCurrentPlayer().getPlayerType() == PlayerType.MEDIUM)
			searchDepth = 3;
		if (currentGameState.getCurrentPlayer().getPlayerType() == PlayerType.HARD)
			searchDepth = 5;

		MiniMaxAI miniMax = new MiniMaxAI(currentGameState, searchDepth, transpositionTable);
		Move move = miniMax.getBestMove();
		move.apply(currentGameState);
		System.out.println("best move : " + move.toString());
		splendorController.getGameboardController().nextTrain();
	}

}
