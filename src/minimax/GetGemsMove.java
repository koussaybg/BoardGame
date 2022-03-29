package minimax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import model.GameState;
import model.GemAmountType;
import model.GemType;
import model.Gems;

public class GetGemsMove implements Move {

	/**
	 * The gems to take
	 */
	private final Gems gemsToTake;
	/**
	 * If the player has more than 10 gems after taking the new ones, these are the
	 * ones he gives away.
	 */
	private final Gems gemsToGiveAway;

	public GetGemsMove(Gems gemsToTake, Gems gemsToGiveAway) {
		super();
		this.gemsToTake = gemsToTake;
		this.gemsToGiveAway = gemsToGiveAway == null ? new Gems(GemAmountType.UNLIMITED) : gemsToGiveAway;
	}

	/**
	 * Takes the gemsToTake and gives gemsToTakeAway back.
	 */
	@Override
	public void apply(GameState gameState) {
		gameState.takeGems(gemsToTake);
		if (gemsToGiveAway != null)
			gameState.giveGemsBack(gemsToGiveAway);
	}

	public static List<GetGemsMove> getPossibleMoves(GameState gameState) {

		int[] possible = gameState.getGems().getGems();
		possible[GemType.JOKER.ordinal()] = 0;

		List<Gems> subsets = extractSubsets(new int[6], possible, 3);
		subsets.addAll(extractSubsets(new int[6], possible, 2));
		subsets.addAll(extractSubsets(new int[6], possible, 1));

		subsets = subsets.stream()
				.filter(gems -> gems.canBeGivenToPlayer(gameState.getGems(), gameState.getCurrentPlayer().getGems()))
				.collect(Collectors.toList());

		List<GetGemsMove> possibleMoves = subsets.stream().map(gems -> new GetGemsMove(gems, null))
				.flatMap(move -> addGiveAway(gameState, move).stream()).collect(Collectors.toList());

		return possibleMoves;

	}

	private static List<GetGemsMove> addGiveAmountAway(Gems from, GetGemsMove move, GameState state, int amount) {
		int[] possible = new int[GemType.values().length];
		for (GemType gemType : GemType.values()) {
			// don't give gems away we just took
			if (move.gemsToTake.getGems()[gemType.ordinal()] == 0) {
				possible[gemType.ordinal()] = from.getAmountOf(gemType);
			}
		}

		List<GetGemsMove> result = extractSubsets(new int[GemType.values().length], possible, amount).stream()
				.filter(gems -> state.getGems().contains(gems) && state.getCurrentPlayer().getGems().contains(gems))
				.map(giveBack -> new GetGemsMove(move.gemsToTake, giveBack)).collect(Collectors.toList());

		return result;
	}

	private static List<Gems> extractSubsets(int[] parent, int[] possible, int amount) {
		if (amount == 0) {
			return Collections.singletonList(new Gems(parent, GemAmountType.UNLIMITED));
		}
		List<Gems> result = new ArrayList<Gems>();
		for (GemType gemType : GemType.values()) {
			if (possible[gemType.ordinal()] > 0) {
				int[] cloned = parent.clone();
				cloned[gemType.ordinal()]++;
				result.addAll(extractSubsets(cloned, possible, amount - 1));
			}
		}
		result = result.stream().distinct().collect(Collectors.toList());
		return result;
	}

	private static List<GetGemsMove> addGiveAway(GameState state, GetGemsMove move) {
		int amountToMany = (state.getCurrentPlayer().getGems().getTotalAmount() + move.getGemsToTake().getTotalAmount())
				- GemAmountType.PLAYER_HAND.getMaxAmount();
		if (amountToMany <= 0) {
			return Collections.singletonList(move);
		} else {
			return addGiveAmountAway(state.getCurrentPlayer().getGems(), move, state, amountToMany);
		}
	}

	/**
	 * @return the gemsToTake
	 */
	public Gems getGemsToTake() {
		return gemsToTake;
	}

	/**
	 * @return the gemsToGiveAway
	 */
	public Gems getGemsToGiveAway() {
		return gemsToGiveAway;
	}

	@Override
	public String toString() {
		return "GetGemsMove [gemsToTake=" + Arrays.toString(gemsToTake.getGems()) + ", gemsToGiveAway="
				+ Arrays.toString(gemsToGiveAway.getGems()) + "]";
	}
}
