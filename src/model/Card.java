package model;

import java.io.Serializable;

public class Card implements Serializable, Cloneable {
	/**
	 *
	 */
	private static final long serialVersionUID = -889504010306506313L;

	/**
	 * the prestige of the card
	 */
	private final int prestige;

	/**
	 * represent the cost of the card
	 */
	private final Gems cost;

	/**
	 * if is the card Nobel
	 */
	private final boolean isNoble;

	/**
	 * level of the card 1,2,3 or 4 for nobel
	 */
	private final int level;

	/**
	 * the type of the card
	 */
	private GemType gemType;

	/**
	 * default constructor, cards will be generated from file
	 * 
	 * @param prestige prestige of card
	 * @param cost     cost of card
	 * @param isNoble  is card noble
	 * @param level    level has to be between 1 and 3 for not nobel cards
	 */
	public Card(int prestige, Gems cost, boolean isNoble, int level) {
		this.prestige = prestige;
		this.cost = cost;
		this.isNoble = isNoble;
		if ((level > 0 && level <= 3) || isNoble) {
			this.level = level;
		} else {
			throw new IllegalArgumentException("Level have to be between 1 and 3 for usual cards");
		}
	}

	/**
	 * used to know if the specific player can play or not
	 * 
	 * @param player : the player who want to know
	 * @return true when he can play
	 * 
	 */
	public boolean canBuy(Player player) {
		if (!this.isNoble()) {
			Gems total = Gems.combine(player.getGems(), player.getBonus());
			return total.isGEQThan(this.getCost());
		} else {
			return player.getBonus().isGEQThan(this.getCost());
		}
	}

	/**
	 * used to know if the given Player is able to buy card with given gems
	 * 
	 * @param player Trying to buy card
	 * @param gems   Gems player is trying to use for payment
	 * @return True if True
	 */
	public boolean canBuy(Player player, Gems gems) {
		Gems total = Gems.combine(gems, player.getBonus());
		return player.getGems().contains(gems) && total.isGEQThan(this.getCost());
	}

	/**
	 * @return the gemType
	 */
	public GemType getGemType() {
		return gemType;
	}

	/**
	 * @param gemType the gemType to set
	 */
	public void setGemType(GemType gemType) {
		this.gemType = gemType;
	}

	/**
	 * @return the prestige
	 */
	public int getPrestige() {
		return prestige;
	}

	/**
	 * @return the isNoble
	 */
	public boolean isNoble() {
		return isNoble;
	}

	/**
	 * 
	 * @return the cost
	 */
	public Gems getCost() {
		return cost;
	}

	public Card clone() {
		Card clonedCard = new Card(this.prestige, this.getCost().clone(), this.isNoble, this.level);
		clonedCard.setGemType(this.getGemType());
		return clonedCard;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((gemType == null) ? 0 : gemType.hashCode());
		result = prime * result + (isNoble ? 1231 : 1237);
		result = prime * result + level;
		result = prime * result + prestige;
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
		Card other = (Card) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (gemType != other.gemType)
			return false;
		if (isNoble != other.isNoble)
			return false;
		if (level != other.level)
			return false;
		if (prestige != other.prestige)
			return false;
		return true;
	}

	/**
	 * @return Get the level
	 */
	public int getLevel() {
		return level;
	}

	@Override
	public String toString() {
		return "Card{" + "prestige=" + prestige + ", cost=" + cost + ", isNoble=" + isNoble + ", level=" + level
				+ ", gemType=" + gemType + '}';
	}
}
