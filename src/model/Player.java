package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Player implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6920310242612886915L;
	/**
	 * the name of the player
	 */
	private final String name;
	/**
	 * the type of the player
	 */
	private PlayerType playerType;
	/**
	 * gems which the player has
	 */
	private Gems gems;
	/**
	 * the cards which the player have been bought
	 */
	private List<Card> boughtCards;
	/**
	 * the cards which the player have been reserved
	 */
	private List<Card> reservedCards;
	/**
	 * the Prestige points of the Player
	 */
	private int prestige;

	private Gems bonus;

	/**
	 * a constructor to make a new player
	 *
	 * @param playerType
	 * @param name
	 */
	public Player(PlayerType playerType, String name) {
		this.name = name;
		this.playerType = playerType;
		this.prestige = 0;
		this.reservedCards = new LinkedList<>();
		this.boughtCards = new LinkedList<>();
		this.gems = new Gems(GemAmountType.PLAYER_HAND);
		bonus = new Gems(new int[] { 0, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED);
	}

	public Gems getBonus() {
		return bonus;
	}

	private void calculateBonus() {
		if (getBoughtCards().size() > 0) {
			for (Card card : getBoughtCards()) {
				if (!card.isNoble() && card.getGemType() != null) {
					bonus.getGems()[card.getGemType().ordinal()]++;
				}
			}
		}
	}

	/**
	 * if the player is AI
	 * 
	 * @return True=The Player is an AI , false=the Player is a Human
	 */

	public boolean isAI() {
		return !playerType.equals(PlayerType.HUMAN);
	}

	/**
	 * check if the player can reserve cards
	 *
	 */
	public boolean canReserve() {

		return reservedCards.size() < 3;
	}

	/**
	 * Reserve a card , if the player can't reserve it will return false
	 * 
	 * @param card the card you want to reserve
	 * @return it return false if he can reserve , true if he can
	 */
	public boolean addReservedCard(Card card) {
		if (canReserve()) {
			reservedCards.add(card);

			return true;
		}
		return false;
	}

	/**
	 * @return Get the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Get the playerType
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}

	/**
	 * Sets the playerType to passed playerType
	 */
	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	/**
	 * @return Get the gems
	 */
	public Gems getGems() {
		return gems;
	}

	/**
	 * Sets the gems to passed gems
	 */
	public void setGems(Gems gems) {
		this.gems = gems;
	}

	/**
	 * @return Get the boughtCards
	 */
	public List<Card> getBoughtCards() {
		return boughtCards;
	}

	/**
	 * Sets the boughtCards to passed boughtCards
	 */
	public void setBoughtCards(List<Card> boughtCards) {
		this.boughtCards = boughtCards;
		calculateBonus();
	}

	/**
	 * @return Get the reservedCards
	 */
	public List<Card> getReservedCards() {
		return reservedCards;
	}

	void setReservedCards(List<Card> reservedCards) {
		this.reservedCards = reservedCards;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 * @return prestige for the Player
	 */
	public int getPrestige() {

		return this.prestige;
	}

	public void setPrestige(int prestige) {
		this.prestige = prestige;
	}

	public void addBoughtCard(Card card) {
		boughtCards.add(card);
		prestige += card.getPrestige();
		calculateBonus();

	}

	public Player clone() {
		Player clone = new Player(this.playerType, this.name);
		clone.setGems(this.gems.clone());
		clone.setBoughtCards(this.boughtCards.stream().map(card -> card.clone()).collect(Collectors.toList()));
		clone.setReservedCards(this.reservedCards.stream().map(card -> card.clone()).collect(Collectors.toList()));
		clone.prestige = this.prestige;
		return clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bonus == null) ? 0 : bonus.hashCode());
		result = prime * result + ((boughtCards == null) ? 0 : boughtCards.hashCode());
		result = prime * result + ((gems == null) ? 0 : gems.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((playerType == null) ? 0 : playerType.hashCode());
		result = prime * result + prestige;
		result = prime * result + ((reservedCards == null) ? 0 : reservedCards.hashCode());
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
		Player other = (Player) obj;
		if (bonus == null) {
			if (other.bonus != null)
				return false;
		} else if (!bonus.equals(other.bonus))
			return false;
		if (boughtCards == null) {
			if (other.boughtCards != null)
				return false;
		} else if (!boughtCards.equals(other.boughtCards))
			return false;
		if (gems == null) {
			if (other.gems != null)
				return false;
		} else if (!gems.equals(other.gems))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (playerType != other.playerType)
			return false;
		if (prestige != other.prestige)
			return false;
		if (reservedCards == null) {
			if (other.reservedCards != null)
				return false;
		} else if (!reservedCards.equals(other.reservedCards))
			return false;
		return true;
	}

}
