package controller;

import model.Card;
import model.GameState;
import model.Gems;
import view.viewAUI.GameboardWindowAUI;

public class PlayerController {

	private SplendorController splendorController;

	private GameboardWindowAUI gameboardWindowAUI;

	/**
	 * default constructor
	 * 
	 * @param splendorController
	 */
	public PlayerController(SplendorController splendorController, GameboardWindowAUI gameboardWindowAUI) {
		this.splendorController = splendorController;
		this.gameboardWindowAUI = gameboardWindowAUI;
	}

	/**
	 * buys passed card with suitable gems. Card will be bought by current player.
	 * Gems will be withdrawn. Refreshes prestige in player model
	 * 
	 * @param card card to buy
	 */
	public void buyCard(Card card) {
		if (!card.canBuy(getCurrentGameState().getCurrentPlayer())) {
			throw new IllegalArgumentException("\n" + "Cant buy this card.");
		}
		Gems gems = Gems.calculateTransferAmount(getCurrentGameState().getCurrentPlayer(), card.getCost());
		getCurrentGameState().buyCard(card, gems);
		gameboardWindowAUI.refreshGems();
		gameboardWindowAUI.refreshCards();
		gameboardWindowAUI.refreshPlayersHand();
		splendorController.getGameboardController().checkNobles();
	}

	/**
	 * buys passed card with passed gems. Card will be buoyed by current player.
	 * Gems will be withdrawn. Refreshes prestige in player model
	 * 
	 * @param card card to buy
	 * @param gems gems to pay card
	 */
	public void buyCard(Card card, Gems gems) {
		getCurrentGameState().buyCard(card, gems);
		gameboardWindowAUI.refreshGems();
		gameboardWindowAUI.refreshCards();
		gameboardWindowAUI.refreshPlayersHand();
		splendorController.getGameboardController().checkNobles();
	}

	/**
	 * reserves card for current player. refunds to jokers to current player.
	 * 
	 * @param card card to reserve
	 */
	public void reserveCard(Card card) {
		getCurrentGameState().reserveCard(card);
		gameboardWindowAUI.refreshGems();
		gameboardWindowAUI.refreshCards();
		gameboardWindowAUI.refreshPlayersHand();
		splendorController.getGameboardController().checkNobles();
	}

	/**
	 * buys noble for current player no gems will be withdrawn. Refreshes prestige
	 * in player model
	 * 
	 * @param card noble to buy
	 */
	public void buyNoble(Card card) {
		getCurrentGameState().buyNoble(card);
		gameboardWindowAUI.refreshGems();
		gameboardWindowAUI.refreshNobles();
		gameboardWindowAUI.refreshPlayersHand();
		splendorController.getGameboardController().nextTrain();
		// TODO nextTrain() or checkNobles() ?
	}

	/**
	 * Takes gems for current player
	 * 
	 * @param gems gems to take
	 */
	public void takeGems(Gems gems) {
		getCurrentGameState().takeGems(gems);
		gameboardWindowAUI.refreshGems();
		gameboardWindowAUI.refreshPlayersHand();
		splendorController.getGameboardController().checkNobles();
	}

	public void giveGemsBack(Gems gems) {

		getCurrentGameState().giveGemsBack(gems);
		gameboardWindowAUI.refreshGems();
		gameboardWindowAUI.refreshPlayersHand();

	}

	private GameState getCurrentGameState() {
		return splendorController.getSplendor().getGame().getCurrentGameState();
	}

}
