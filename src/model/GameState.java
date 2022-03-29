package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameState implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8883157908715677389L;

	private int currentTrain;

	private boolean gameDone;

	private List<Card> levelTwoCards;

	private List<Card> levelThreeCards;

	private List<Card> levelOneCards;

	private List<Player> players;

	private GameState successor;

	private GameState predecessor;

	private Gems gems;

	private List<Card> nobleCards;
	private boolean end = false;

	/**
	 * to get the Open Cards in the game.
	 * 
	 * @return liste from those cards
	 */

	public List<Card> getOpenCards() {
		List<Card> openCards = new ArrayList<>();

		openCards.addAll(levelOneCards.subList(0, 4 > levelOneCards.size() ? levelOneCards.size() : 4));
		openCards.addAll(levelTwoCards.subList(0, 4 > levelTwoCards.size() ? levelTwoCards.size() : 4));
		openCards.addAll(levelThreeCards.subList(0, 4 > levelThreeCards.size() ? levelThreeCards.size() : 4));

		return openCards;

	}

	/**
	 * to know which player plays.
	 * 
	 * @return player
	 */
	public Player getCurrentPlayer() {
		int countPlayer = players.size();
		Player currentPlayer = players.get(currentTrain % countPlayer);
		return currentPlayer;
	}

	/**
	 * @return the currentTrain
	 */
	public int getCurrentTrain() {
		return currentTrain;
	}

	/**
	 * @param currentTrain the currentTrain to set
	 */
	public void setCurrentTrain(int currentTrain) {
		this.currentTrain = currentTrain;
	}

	/**
	 * @return the gameDone
	 */
	public boolean isGameOver() {
		for (Player player : getPlayers()) {
			if (player.getPrestige() >= 15) {
				end = true;
			}
		}
		if (end) {
			return gameDone = (currentTrain % players.size() == 0);
		}
		return gameDone = false;
	}

	/**
	 * check if the current player can buy the card
	 * 
	 * @param card the card to check
	 * @param gems
	 * @return true if he can , flase if he can't
	 */
	public boolean canBuyCard(Card card, Gems gems) {
		return card.canBuy(getCurrentPlayer(), gems);
	}

	/**
	 * buy the given Card with the given Gems
	 * 
	 * @param card: we want to buy
	 * @param gems: we want to buy with
	 */
	public void buyCard(Card card, Gems gems) {
		if (!canBuyCard(card, gems)) {
			System.out.println("card : " + card.getCost().toString());
			System.out.println("gems : " + gems.toString());
			System.out.println("player : " + getCurrentPlayer().getGems().toString());
			System.out.println("transfer : " + Gems.calculateTransferAmount(getCurrentPlayer(), card.getCost()));
			throw new IllegalArgumentException("\n" + "Please don't be like that.");
		}
		Player player = getCurrentPlayer();
		GemTransferResult result = Gems.transferGems(player.getGems(), this.gems, gems);
		if (GemTransferResult.SUCCESS == result) {
			getCurrentPlayer().addBoughtCard(card);
			if (player.getReservedCards().contains(card)) {
				getCurrentPlayer().getReservedCards().remove(card);
			} else {
				switch (card.getLevel()) {
				case 1:
					levelOneCards.remove(card);
					break;
				case 2:
					levelTwoCards.remove(card);
					break;
				case 3:
					levelThreeCards.remove(card);
					break;
				}
			}
		} else {
			throw new IllegalArgumentException(
					"\n" + "Die ausgewählte Karte konnte nicht gekauft werden." + "\n" + result.toString());
		}
	}

	/**
	 * buy the given noble Card
	 * 
	 * @param noble: to buy
	 */

	public void buyNoble(Card noble) {
		if (noble.canBuy(getCurrentPlayer())) {
			nobleCards.remove(noble);
			getCurrentPlayer().addBoughtCard(noble);
		} else {
			System.out.println("can't buy noble");
			// use addboughtCard method in Player
		}
	}

	/**
	 * reserve the given Card
	 * 
	 * @param card: to reserve
	 */
	public void reserveCard(Card card) {
		if (getCurrentPlayer().canReserve()) {
			switch (card.getLevel()) {
			case 1:
				levelOneCards.remove(card);
				break;
			case 2:
				levelTwoCards.remove(card);
				break;
			case 3:
				levelThreeCards.remove(card);
				break;
			}
			getCurrentPlayer().addReservedCard(card);
			int[] jokerarray = { 0, 0, 0, 0, 0, 1 };
			Gems joker = new Gems(jokerarray, GemAmountType.UNLIMITED);
			Gems.transferGems(gems, getCurrentPlayer().getGems(), joker);
		} else {
			throw new IllegalArgumentException("\n" + "Es ist nicht möglich mehr als 3 Karten zu reservieren");
		}
	}

	/**
	 * Takes gems for current player
	 * 
	 * @param gems: to take
	 */
	public void takeGems(Gems gems) {
		Gems playergems = getCurrentPlayer().getGems();
		if (gems.canBeGivenToPlayer(this.gems, playergems)) {
			GemTransferResult result = Gems.transferGems(this.gems, playergems, gems);
			if (!result.equals(GemTransferResult.SUCCESS)
					&& !result.equals(GemTransferResult.RECEIVER_NOT_ENOUGH_CAPACITY)) {
				// should i throw an exception ? or just ignore it

				System.out.println("player : " + playergems.toString());
				System.out.println("gems   : " + gems.toString());
				System.out.println("board  : " + this.gems.toString());
				System.out.println(result);
				throw new IllegalArgumentException("\n"
						+ "Transfer der Edelsteine fehlgeschlagen. Bitte überprüfen Sie Ihre Auswahl und versuchen Sie es erneut.");
			}
		} else {
			System.out.println("player : " + playergems.toString());
			System.out.println("gems   : " + gems.toString());
			System.out.println("board  : " + this.gems.toString());
			throw new IllegalArgumentException("\n"
					+ "Ihre Edelsteinauswahl ist nicht gültig. Bitte nehmen Sie exakt 2 gleiche (insofern danach noch mindestens 3 Edelsteine dieser Sorte vorhanden sind) oder 3 verschiedene Edelsteine auf. Bei unzureichender Auslage nehmen Sie die maximal verfügbare gültige Menge an Edelsteinen.");
		}
	}

	/**
	 * give back Gems
	 * 
	 * @param gems the gems to return
	 */
	public void giveGemsBack(Gems gems) {
		GemTransferResult transferResult = Gems.transferGems(getCurrentPlayer().getGems(), this.gems, gems);
		if (!transferResult.equals(GemTransferResult.SUCCESS)) {
			throw new IllegalArgumentException("\n"
					+ "Die Edelsteine konnten nicht zurück gegeben werden. Bitte überprüfen Sie Ihre Auswahl und versuchen Sie es erneut.");
		}

	}

	/**
	 * @param gameDone the gameDone to set
	 */
	public void setGameDone(boolean gameDone) {
		this.gameDone = gameDone;
	}

	/**
	 * @return the levelTwoCards
	 */
	public List<Card> getLevelTwoCards() {
		return levelTwoCards;
	}

	/**
	 * @param levelTwoCards the levelTwoCards to set
	 */
	public void setLevelTwoCards(List<Card> levelTwoCards) {
		this.levelTwoCards = levelTwoCards;
	}

	/**
	 * @return the levelThreeCards
	 */
	public List<Card> getLevelThreeCards() {
		return levelThreeCards;
	}

	/**
	 * @param levelThreeCards the levelThreeCards to set
	 */
	public void setLevelThreeCards(List<Card> levelThreeCards) {
		this.levelThreeCards = levelThreeCards;
	}

	/**
	 * @return the levelOneCards
	 */
	public List<Card> getLevelOneCards() {
		return levelOneCards;
	}

	/**
	 * @param levelOneCards the levelOneCards to set
	 */
	public void setLevelOneCards(List<Card> levelOneCards) {
		this.levelOneCards = levelOneCards;
	}

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * @return the successor
	 */
	public GameState getSuccessor() {
		return successor;
	}

	/**
	 * @param successor the successor to set
	 */
	public void setSuccessor(GameState successor) {
		this.successor = successor;
	}

	/**
	 * @return the predecessor
	 */
	public GameState getPredecessor() {
		return predecessor;
	}

	/**
	 * @param predecessor the predecessor to set
	 */
	public void setPredecessor(GameState predecessor) {
		this.predecessor = predecessor;
	}

	/**
	 * @return the gems
	 */
	public Gems getGems() {
		return gems;
	}

	/**
	 * @param gems the gems to set
	 */
	public void setGems(Gems gems) {
		this.gems = gems;
	}

	/**
	 * @return the nobleCards
	 */
	public List<Card> getNobleCards() {
		return nobleCards;
	}

	/**
	 * @param nobleCards the nobleCards to set
	 */
	public void setNobleCards(List<Card> nobleCards) {
		this.nobleCards = nobleCards;
	}

	/**
	 * Default constructor
	 */
	public GameState() {
		super();
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentTrain;
		result = prime * result + (end ? 1231 : 1237);
		result = prime * result + (gameDone ? 1231 : 1237);
		result = prime * result + ((gems == null) ? 0 : gems.hashCode());
		result = prime * result + ((levelOneCards == null) ? 0 : levelOneCards.hashCode());
		result = prime * result + ((levelThreeCards == null) ? 0 : levelThreeCards.hashCode());
		result = prime * result + ((levelTwoCards == null) ? 0 : levelTwoCards.hashCode());
		result = prime * result + ((nobleCards == null) ? 0 : nobleCards.hashCode());
		result = prime * result + ((players == null) ? 0 : players.hashCode());
		result = prime * result + ((predecessor == null) ? 0 : predecessor.hashCode());
		result = prime * result + ((successor == null) ? 0 : successor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameState other = (GameState) obj;
		if (currentTrain != other.currentTrain)
			return false;
		if (end != other.end)
			return false;
		if (gameDone != other.gameDone)
			return false;
		if (gems == null) {
			if (other.gems != null)
				return false;
		} else if (!gems.equals(other.gems))
			return false;
		if (levelOneCards == null) {
			if (other.levelOneCards != null)
				return false;
		} else if (!levelOneCards.equals(other.levelOneCards))
			return false;
		if (levelThreeCards == null) {
			if (other.levelThreeCards != null)
				return false;
		} else if (!levelThreeCards.equals(other.levelThreeCards))
			return false;
		if (levelTwoCards == null) {
			if (other.levelTwoCards != null)
				return false;
		} else if (!levelTwoCards.equals(other.levelTwoCards))
			return false;
		if (nobleCards == null) {
			if (other.nobleCards != null)
				return false;
		} else if (!nobleCards.equals(other.nobleCards))
			return false;
		if (players == null) {
			if (other.players != null)
				return false;
		} else if (!players.equals(other.players))
			return false;
		if (predecessor == null) {
			if (other.predecessor != null)
				return false;
		} else if (!predecessor.equals(other.predecessor))
			return false;
		if (successor == null) {
			if (other.successor != null)
				return false;
		} else if (!successor.equals(other.successor))
			return false;
		return true;
	}

	public GameState clone() {
		GameState clonedState = new GameState();
		clonedState.setCurrentTrain(currentTrain);
		clonedState.setGameDone(gameDone);
		clonedState
				.setLevelOneCards(this.levelOneCards.stream().map(card -> card.clone()).collect(Collectors.toList()));
		clonedState
				.setLevelTwoCards(this.levelTwoCards.stream().map(card -> card.clone()).collect(Collectors.toList()));
		clonedState.setLevelThreeCards(
				this.levelThreeCards.stream().map(card -> card.clone()).collect(Collectors.toList()));
		clonedState.setPlayers(this.players.stream().map(player -> player.clone()).collect(Collectors.toList()));
		clonedState.setGems(this.gems.clone());
		clonedState.setNobleCards(this.nobleCards.stream().map(card -> card.clone()).collect(Collectors.toList()));
		return clonedState;
	}

}
