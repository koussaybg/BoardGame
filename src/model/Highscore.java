package model;

import java.io.Serializable;
import java.util.List;

public class Highscore implements Serializable, Comparable<Highscore>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6137400500532846360L;

	private final String name;

	private final int score;

	private final int cardsBought;

	private final int roundsPlayed;

	private final int nobleVisits;

	private final int prestige;

	public Highscore(Player player, int roundsPlayed) {
		this.name = player.getName();
		this.cardsBought = player.getBoughtCards().size();
		this.roundsPlayed = roundsPlayed;
		this.nobleVisits= countNobles(player.getBoughtCards());
		this.prestige = player.getPrestige();
		
		this.score = calculateScore();
	}

	/**
	 * calculates score
	 * @param prestige
	 * @param roundsPlayed
	 * @param bougthcards
	 * @return
	 */
	private int calculateScore() {
    	double score = this.prestige*(1.0/this.cardsBought) - (this.roundsPlayed/20.0);
        return (int) (score * 1000);
    }

	/**
	 * counts nobles in given cards
	 * @param cards to be analized
	 * @return amount of nobles
	 */
	private int countNobles(List<Card> cards){
		int result = 0;
		for (Card card : cards) {
			if (card.isNoble()) {
				result += 1;
			}
		}
		return result;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @return the cardsBought
	 */
	public int getCardsBought() {
		return cardsBought;
	}

	/**
	 * @return the roundsPlayed
	 */
	public int getRoundsPlayed() {
		return roundsPlayed;
	}

	/**
	 * @return the nobleVisits
	 */
	public int getNobleVisits() {
		return nobleVisits;
	}

	/**
	 * @return the prestige
	 */
	public int getPrestige() {
		return prestige;
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
		result = prime * result + cardsBought;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + nobleVisits;
		result = prime * result + prestige;
		result = prime * result + roundsPlayed;
		result = prime * result + score;
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
		Highscore other = (Highscore) obj;
		if (cardsBought != other.cardsBought)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nobleVisits != other.nobleVisits)
			return false;
		if (prestige != other.prestige)
			return false;
		if (roundsPlayed != other.roundsPlayed)
			return false;
		if (score != other.score)
			return false;
		return true;
	}

	@Override
	public int compareTo(Highscore other) {
		if(this.score<other.score){
			return -1;
		} else if(this.score>other.score) {
			return 1;
		}else {
			return 0;
		}
	}
}
