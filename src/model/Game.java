package model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Game implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -4235529945938622342L;

	/**
	 * the ability to make a highscoreable
	 */
	private boolean highscoreable;

	/**
	 * the state of this game
	 */
	private GameState currentGameState;

	/**
	 * Constructor to make a new game with the given players ,cards and nobels
	 * 
	 * @param playerList
	 * @param cards
	 * @param nobles
	 */
	public Game(List<Player> playerList, List<Card> cards, List<Card> nobles) {
		GameState firstGameState = new GameState();
		firstGameState.setLevelOneCards(filterCards(cards, 1));
		firstGameState.setLevelTwoCards(filterCards(cards, 2));
		firstGameState.setLevelThreeCards(filterCards(cards, 3));
		firstGameState.setPlayers(playerList);
		if (playerList.size() == 2) {
			firstGameState.setNobleCards(nobles.stream().limit(3).collect(Collectors.toList()));
			firstGameState.setGems(new Gems(GemAmountType.GAMEBOARD_TWO_PLAYERS));
		} else if (playerList.size() == 3) {
			firstGameState.setNobleCards(nobles.stream().limit(4).collect(Collectors.toList()));
			firstGameState.setGems(new Gems(GemAmountType.GAMEBOARD_THREE_PLAYERS));
		} else if (playerList.size() == 4) {
			firstGameState.setNobleCards(nobles.stream().limit(5).collect(Collectors.toList()));
			firstGameState.setGems(new Gems(GemAmountType.GAMEBOARD_FOUR_PLAYERS));
		} else {
			throw new IllegalArgumentException("invalid player list size");
		}

		highscoreable = true;

		this.currentGameState = firstGameState;
	}

	private List<Card> filterCards(List<Card> cards, int level) {
		return cards.stream().filter(card -> card.getLevel() == level).collect(Collectors.toList());
	}

	/**
	 * used to clone the Game State
	 * 
	 * @throws CloneNotSupportedException
	 */
	public void cloneGameState() {
		GameState clonedGameState = currentGameState.clone();
		clonedGameState.setCurrentTrain(clonedGameState.getCurrentTrain() + 1);
		clonedGameState.setPredecessor(currentGameState);
		
		//remove old successor
		if(clonedGameState.getSuccessor() != null) {
			clonedGameState.getSuccessor().setPredecessor(null);
		}
		
		currentGameState.setSuccessor(clonedGameState);
		this.currentGameState = clonedGameState;
	}

	/**
	 * @return the highscoreable
	 */
	public boolean isHighscoreable() {
		return highscoreable;
	}

	/**
	 * @param highscoreable the highscoreable to set
	 */
	public void setHighscoreable(boolean highscoreable) {
		this.highscoreable = highscoreable;
	}

	/**
	 * @return the currentGameState
	 */
	public GameState getCurrentGameState() {
		return currentGameState;
	}

	/**
	 * sets the given game state
	 * 
	 * @param gameState: to set
	 */
	public void setCurrentGameState(GameState gameState) {
		this.currentGameState = gameState;
	}

}
