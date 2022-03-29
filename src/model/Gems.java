package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents an amount of {@link GemType} and provides necessary
 * functions.
 * 
 * @author sopr057
 *
 */
public class Gems implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4136822403358757038L;

	/**
	 * The length of this array must be equal to the values from {@link GemType} at
	 * all times. The value at a position in this array represents the amount of
	 * gems of the corresponding EnumValue.
	 */
	private final int[] gems;

	/**
	 * The gemAmountType affects how many Gems are allowed in total.
	 */
	private final GemAmountType gemAmountType;

	private static final int MAX_JOKERS_PER_TRANSACTION = 1;
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;

	/**
	 * Constructor that creates an object based on the given input.
	 * 
	 * @param gems
	 * @param gemAmountType
	 * @throws {@link IllegalArgumentException} if the provided array is invalid.
	 */
	public Gems(int[] gems, GemAmountType gemAmountType) {
		super();
		if (!isValid(gems, gemAmountType)) {
			System.err.println(gemAmountType);
			System.err.println(Arrays.toString(gems));
			throw new IllegalArgumentException();
		}
		this.gems = gems;
		this.gemAmountType = gemAmountType;
	}

	/**
	 * Creates a gemObject with the given gemAmountType. The gem array is empty or
	 * filled in case of a Gameboard type, as specified by the splendor rules.
	 * 
	 * @param gemAmountType
	 */
	public Gems(GemAmountType gemAmountType) {
		this.gemAmountType = gemAmountType;
		this.gems = new int[GemType.values().length];

		int chipAmount = 0;
		if (gemAmountType.equals(GemAmountType.GAMEBOARD_TWO_PLAYERS)) {
			chipAmount = 4;
		} else if (gemAmountType.equals(GemAmountType.GAMEBOARD_THREE_PLAYERS)) {
			chipAmount = 5;
		} else if (gemAmountType.equals(GemAmountType.GAMEBOARD_FOUR_PLAYERS)) {
			chipAmount = 7;
		}
		if (chipAmount > 0) {
			for (GemType gemType : GemType.values()) {
				if (gemType.equals(GemType.JOKER)) {
					gems[gemType.ordinal()] = 5;
				} else {
					gems[gemType.ordinal()] = chipAmount;
				}
			}
		}
	}

	/**
	 * Creates a Gem object that resembles the amount of gem bonuses the given cards
	 * provide.
	 * 
	 * @param cards
	 */
	public Gems(List<Card> cards) {
		this.gemAmountType = GemAmountType.UNLIMITED;
		this.gems = new int[GemType.values().length];

		// Add one gem for each bonus. Nobles don't have bonuses, so they can be
		// skipped.
		for (Card card : cards) {
			if (!card.isNoble()) {
				gems[card.getGemType().ordinal()]++;
			}
		}
	}

	/**
	 * Creates a gem object with one chip of the given type.
	 * 
	 * @param gemType
	 */
	Gems(GemType gemType) {
		this.gemAmountType = GemAmountType.UNLIMITED;
		this.gems = new int[GemType.values().length];
		this.gems[gemType.ordinal()] = 1;
	}

	/**
	 * Alias : isGreaterOrEqualThan Checks, if this Object has more or equally many
	 * gems of each type. If gems are missing, remaining Jokers simulate their
	 * place.
	 * 
	 * @param other
	 * @return
	 */
	public boolean isGEQThan(Gems other) {
		int remainingJokers = this.getAmountOf(GemType.JOKER) - other.getAmountOf(GemType.JOKER);
		return getMissingGems(other) <= remainingJokers;
	}

	/**
	 * Returns if the gems contains all other.
	 * 
	 * @param other
	 * @return
	 */
	public boolean contains(Gems other) {
		int availableJokers = this.getAmountOf(GemType.JOKER) - other.getAmountOf(GemType.JOKER);
		return getMissingGems(other) - availableJokers <= 0;
	}

	public int getMissingGems(Gems other) {
		int missingGems = 0;
		for (GemType gemType : GemType.values()) {
			missingGems += Math.max(0, other.getAmountOf(gemType) - this.getAmountOf(gemType));
		}
		return missingGems;
	}

	/**
	 * Adds the amount of gems that are specified in other. If that is not possible,
	 * the method returns FAIL and neither this or other will be changed.
	 * 
	 * @param amount
	 * @return
	 */
	private GemTransferResult addGems(final Gems amount) {
		for (GemType gemType : GemType.values()) {
			gems[gemType.ordinal()] += amount.getAmountOf(gemType);
		}
		if (this.getTotalAmount() > this.gemAmountType.getMaxAmount()) {
			return GemTransferResult.RECEIVER_NOT_ENOUGH_CAPACITY;
		} else {
			return GemTransferResult.SUCCESS;
		}
	}

	/**
	 * Checks if this contains only one Joker or only other gems. Checks if the
	 * amount of each single gem type is less than or equal to 2 and are in total at
	 * maximum 3.
	 */
	public boolean canBeGivenToPlayer(Gems from, Gems into) {
		if (this.getTotalAmount() > THREE) {
			return false;
		}
		int transferedAmount = this.getTotalAmount();
		int jokerAmount = this.getAmountOf(GemType.JOKER);
		// contains exactly one Joker or none
		boolean containsNonJokers = (transferedAmount - jokerAmount) > 0;
		boolean containsJokers = jokerAmount > 0;
		if (containsJokers && containsNonJokers) {
			return false;
		}
		if (jokerAmount == MAX_JOKERS_PER_TRANSACTION) {
			return true;
		}

		for (GemType gemType : GemType.values()) {
			int currentAmount = this.getAmountOf(gemType);
			if (currentAmount > TWO) {
				return false;
			}
			if (currentAmount == TWO) {
				return transferedAmount == currentAmount && from.getAmountOf(gemType) >= 4;
			}
		}

		// If the player can take 3 gems and there are 3 different gems (except jokers)
		// , he must take 3.
		int availableGems = from.getTotalAmount() - from.getAmountOf(GemType.JOKER);
		if (availableGems >= THREE && this.getTotalAmount() < THREE && colorsLeft(from) > THREE) {
			return false;
		} else if (availableGems == TWO && this.getTotalAmount() < TWO && colorsLeft(from) > TWO) {
			return false;
		} else if (availableGems == ONE && this.getTotalAmount() < ONE && colorsLeft(from) > ONE) {
			return false;
		}
		for (int i = 1; i <= 3; i++) {
			if (availableGems >= i) {
				if (10 - into.getTotalAmount() >= i) {
					return transferedAmount >= i;
				}
			}
		}
		return transferedAmount <= THREE;
	}

	private int colorsLeft(Gems from) {
		int left = 0;
		for (GemType gemType : GemType.values()) {
			if (gemType != GemType.JOKER) {
				if (from.getAmountOf(gemType) != 0) {
					left++;
				}
			}
		}
		return left;
	}

	/**
	 * Subtracts the amount of gems that are specified in other.
	 * 
	 * @param other
	 */
	private void subtractGems(final Gems other) {
		for (GemType gemType : GemType.values()) {
			gems[gemType.ordinal()] = gems[gemType.ordinal()] - other.getAmountOf(gemType);
		}
	}

	/**
	 * Transfers amount gems from into. Return value is specified in
	 * {@link GemTransferResult}
	 * 
	 * @param from
	 * @param into
	 * @param amount
	 * @return
	 */
	public static GemTransferResult transferGems(Gems from, Gems into, final Gems amount) {
		if (into.getGemAmountType() == GemAmountType.PLAYER_HAND) {
			if (!amount.canBeGivenToPlayer(from, into)) {
				return GemTransferResult.FAIL;
			}
		}
		if (!from.contains(amount)) {
			return GemTransferResult.FAIL;
		}
		Gems fittingAmount = calculateTransferAmount(from, amount);
		from.subtractGems(fittingAmount);
		return into.addGems(fittingAmount);
	}

	public static Gems calculateTransferAmount(Player player, Gems amount) {
		Gems withoutBonus = amount.clone();
		withoutBonus.subtractGems(player.getBonus());
		for(GemType gemType : GemType.values()) {
			withoutBonus.gems[gemType.ordinal()] = Math.max(0, withoutBonus.gems[gemType.ordinal()]);
		}

		return calculateTransferAmount(player.getGems(), withoutBonus);
	}

	private static Gems calculateTransferAmount(Gems total, Gems amount) {
		if (!total.contains(amount)) {
			System.err.println(total.toString());
			System.err.println(amount.toString());
			throw new IllegalArgumentException("from does not contain amount");
		}
		int[] transfer = new int[GemType.values().length];

		for (GemType gemType : GemType.values()) {
			int neededJokers = Math.max(0, amount.getAmountOf(gemType) - total.getAmountOf(gemType));
			transfer[GemType.JOKER.ordinal()] += neededJokers;

			if (!gemType.equals(GemType.JOKER)) {
				if (neededJokers > 0) {
					transfer[gemType.ordinal()] = total.getAmountOf(gemType);
				} else {
					transfer[gemType.ordinal()] = amount.getAmountOf(gemType);
				}
			} else {
				transfer[GemType.JOKER.ordinal()] += amount.getAmountOf(GemType.JOKER);
			}
		}

		Gems result = new Gems(transfer, GemAmountType.UNLIMITED);
		return result;
	}

	/**
	 * Creates a new Object that is the sum of the gems in chips and the bonus of
	 * cards.
	 * 
	 * @param chips
	 * @param cards
	 * @return
	 */
	public static Gems combine(Gems chips, List<Card> cards) {
		int[] combinedAmount = new int[GemType.values().length];
		Gems cardGems = new Gems(cards);
		for (GemType gemType : GemType.values()) {
			combinedAmount[gemType.ordinal()] = chips.getAmountOf(gemType) + cardGems.getAmountOf(gemType);
		}
		Gems gems = new Gems(combinedAmount, GemAmountType.UNLIMITED);
		return gems;
	}
	/**
	 * Creates a new Object that is the sum of the gems in chips and the bonus of
	 * cards.
	 * 
	 * @param chips
	 * @param cards
	 * @return
	 */
	public static Gems combine(Gems chips, Gems other) {
		int[] combinedAmount = new int[GemType.values().length];
		for (GemType gemType : GemType.values()) {
			combinedAmount[gemType.ordinal()] = chips.getAmountOf(gemType) + other.getAmountOf(gemType);
		}
		Gems gems = new Gems(combinedAmount, GemAmountType.UNLIMITED);
		return gems;
	}

	/**
	 * Validates the data. The size of the array must be equal to the maxAmount of
	 * Gems specified in {@link GemType}.
	 * 
	 * @param gems
	 * @param isPlayer
	 * @return
	 */
	private boolean isValid(int[] gemAmount, GemAmountType gemAmountType) {
		int capacity = 0;

		if (gemAmount.length != GemType.values().length) {
			return false;
		}
		for (int i = 0; i < GemType.values().length; i++) {
			if (gemAmount[i] < 0) {
				return false;
			} else {
				capacity += gemAmount[i];
			}
		}
		return gemAmountType.getMaxAmount() >= capacity;
	}

	/**
	 * @return the gems
	 */
	public int[] getGems() {
		return gems;
	}

	/**
	 * @return the gemAmountType
	 */
	public GemAmountType getGemAmountType() {
		return gemAmountType;
	}

	/**
	 * Returns the amount of gems of the given type.
	 * 
	 * @param type the type of gem
	 * @return the amount of gems
	 */
	public int getAmountOf(GemType type) {
		return gems[type.ordinal()];
	}

	/**
	 * @return the total amount of gems held by this object.
	 */
	public int getTotalAmount() {
		return Arrays.stream(gems).sum();
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Gems clone() {
		int[] gemArr = new int[GemType.values().length];
		Gems clone = new Gems(gemArr, this.gemAmountType);
		for (GemType gemType : GemType.values()) {
			gemArr[gemType.ordinal()] = this.getAmountOf(gemType);
		}
		return clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gemAmountType == null) ? 0 : gemAmountType.hashCode());
		result = prime * result + Arrays.hashCode(gems);
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
		Gems other = (Gems) obj;
		if (gemAmountType != other.gemAmountType)
			return false;
		if (!Arrays.equals(gems, other.gems))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Gems [gems=" + Arrays.toString(gems) + ", gemAmountType=" + gemAmountType + "]";
	}

}
