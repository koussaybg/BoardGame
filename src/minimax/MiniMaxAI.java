package minimax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import model.Card;
import model.GameState;
import model.GemType;
import model.Gems;
import model.Player;

/**
 * Contains methods and data to determine the (possibly) best move for the given
 * player.
 * 
 * @author sopr057
 *
 */
public class MiniMaxAI {
	private final GameState initialGameState;
	private final int depth;
	private HashMap<GameState, Integer> transpositionTable;

	/**
	 * @param initialGameState the gameState from which to calculate the best move
	 * @param searchDepth      the maximum depth of the miniMax tree
	 */
	public MiniMaxAI(final GameState initialGameState, int depth, HashMap<GameState, Integer> transpositionTable) {
		super();
		this.initialGameState = initialGameState.clone();
		this.depth = depth;
		this.transpositionTable = transpositionTable;
		Player aiPlayer = this.initialGameState.getCurrentPlayer();

		/*
		 * Get best other player. Optional has to be present else the GameState would
		 * not be valid
		 */
		Player enemyPlayer = this.initialGameState.getPlayers().stream().filter(player -> !player.equals(aiPlayer))
				.max(Comparator.comparingInt(Player::getPrestige)).get();

		/*
		 * change current round index and list structure for easy access in main
		 * algorithm
		 */
		this.initialGameState.setCurrentTrain(0);
		this.initialGameState.setPlayers(Arrays.asList(aiPlayer, enemyPlayer));
	}

	/**
	 * Calculates the best move.
	 */
	public Move getBestMove() {
		List<Move> moves = getPossibleMoves(initialGameState);
		Move bestMove = moves.get(0);
		int bestScore = Integer.MIN_VALUE;

		System.out.println(" - checking " + moves.size() + " possibilities ...");
		int counter = 1;
		long timer = System.currentTimeMillis();
		for (Move move : moves) {
			System.out.println(" - " + counter++);
			GameState cloned = initialGameState.clone();
			move.apply(cloned);
			cloned.setCurrentTrain(cloned.getCurrentTrain() + 1);

			int score = minimize(cloned, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);
			if (score > bestScore) {
				System.out.println(" - best move with " + score + " score");
				bestScore = score;
				bestMove = move;
			}
			// never take more than 10 seconds.
			if ((System.currentTimeMillis() - timer) / 1000 >= 8.8) {
				System.err.println(System.currentTimeMillis() - timer);
				break;
			}
		}
		if (bestMove == null)
			return new DoNothingMove();
		return bestMove;
	}

	/**
	 * Calculates the best move.
	 */
	public Move getTip() {
		List<Move> moves = new ArrayList<Move>();
		moves.addAll(getReserveCardMoves(initialGameState));
		moves.addAll(getBuyCardMoves(initialGameState));
		Move bestMove = moves.get(0);
		int bestScore = Integer.MIN_VALUE;

		System.out.println(" - checking " + moves.size() + " possibilities ...");
		int counter = 1;
		long timer = System.currentTimeMillis();
		for (Move move : moves) {
			System.out.println(" - " + counter++);
			GameState cloned = initialGameState.clone();
			move.apply(cloned);
			cloned.setCurrentTrain(cloned.getCurrentTrain() + 1);

			int score = minimize(cloned, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);
			if (score > bestScore) {
				System.out.println(" - best move with " + score + " score");
				bestScore = score;
				bestMove = move;
			}
			// never take more than 10 seconds.
			if ((System.currentTimeMillis() - timer) / 1000 >= 8.8) {
				System.err.println(System.currentTimeMillis() - timer);
				break;
			}
		}
		if (bestMove == null)
			return new DoNothingMove();
		return bestMove;
	}

	private int minimize(GameState parent, int alpha, int beta, int depth) {
		if (transpositionTable.containsKey(parent)) {
			return transpositionTable.get(parent);
		}
		if (depth <= 0 || parent.isGameOver()) {
			return evaluate(parent);
		}

		List<Move> possibleMoves = getPossibleMoves(parent);
		if (possibleMoves.isEmpty())
			return evaluate(parent);

		int minEval = Integer.MAX_VALUE;
		for (Move move : possibleMoves) {
			GameState child = parent.clone();
			move.apply(child);
			child.setCurrentTrain(parent.getCurrentTrain() + 1);
			int eval = maximize(child, alpha, beta, depth - 1);
			if (depth >= 3)
				transpositionTable.put(child, eval);
			if (eval < minEval) {
				minEval = eval;
				if (move instanceof BuyCardMove) {
					return minEval;
				}
			}
			beta = Math.min(beta, eval);
			if (alpha >= beta)
				break;
		}
		return minEval;
	}

	private int maximize(GameState parent, int alpha, int beta, int depth) {
		if (transpositionTable.containsKey(parent)) {
			return transpositionTable.get(parent);
		}
		if (depth <= 0 || parent.isGameOver()) {
			return evaluate(parent);
		}

		List<Move> possibleMoves = getPossibleMoves(parent);
		if (possibleMoves.isEmpty())
			return evaluate(parent);

		int maxEval = Integer.MIN_VALUE;
		for (Move move : possibleMoves) {
			GameState child = parent.clone();
			move.apply(child);
			child.setCurrentTrain(parent.getCurrentTrain() + 1);
			int eval = minimize(child, alpha, beta, depth - 1);
			if (depth > 2)
				transpositionTable.put(child, eval);
			if (eval > maxEval) {
				maxEval = eval;
				if (move instanceof BuyCardMove) {
					return maxEval;
				}
			}
			alpha = Math.max(alpha, eval);
			if (alpha >= beta)
				break;
		}
		return maxEval;
	}

	/**
	 * Calculates the possible moves from the given gameState. List is already
	 * filtered based on usefulness and sorted based on probability of success to
	 * make pruning more probable.
	 * 
	 * @param gameState gameState from which to calculate
	 * @return the possible moves
	 */
	private List<Move> getPossibleMoves(GameState gameState) {
		List<Move> result = new ArrayList<Move>();

		result.addAll(getBuyCardMoves(gameState));
		result.addAll(getReserveCardMoves(gameState));
		result.addAll(GetGemsMove.getPossibleMoves(gameState));

		if (result.isEmpty()) {
			result.add(new DoNothingMove());
		}

		return result;
	}

	private List<? extends Move> getReserveCardMoves(GameState gameState) {
		if (gameState.getCurrentPlayer().getReservedCards().size() >= 3) {
			return Collections.emptyList();
		}
		List<Card> reservableCards = new ArrayList<Card>(gameState.getOpenCards());
		if (gameState.getLevelOneCards().size() >= 5) {
			reservableCards.add(gameState.getLevelOneCards().get(4));
		}
		if (gameState.getLevelTwoCards().size() >= 5) {
			reservableCards.add(gameState.getLevelTwoCards().get(4));
		}
		if (gameState.getLevelThreeCards().size() >= 5) {
			reservableCards.add(gameState.getLevelThreeCards().get(4));
		}
//
//		// dont reserve cards that we need to get more than 6 gems for
//		if (reservableCards.size() > 5) {
//			Gems playerTotal = Gems.combine(gameState.getCurrentPlayer().getGems(),
//					gameState.getCurrentPlayer().getBoughtCards());
//			reservableCards = reservableCards.stream().filter(card -> playerTotal.getMissingGems(card.getCost()) <= 6)
//					.collect(Collectors.toList());
//		}

		// sort by prestige
		List<ReserveCardMove> moveList = reservableCards.stream()
				.sorted(Comparator.comparingInt(card -> -card.getPrestige())).map(card -> new ReserveCardMove(card))
				.collect(Collectors.toList());

		return moveList;
	}

	private List<? extends Move> getBuyCardMoves(GameState gameState) {
		List<Card> cardsList = new ArrayList<Card>(gameState.getCurrentPlayer().getReservedCards());
		List<Card> openList = new ArrayList<Card>(gameState.getOpenCards());

		cardsList.addAll(openList);
		cardsList = cardsList.stream().filter(card -> card.canBuy(gameState.getCurrentPlayer()))
				.collect(Collectors.toList());

		List<BuyCardMove> moveList = cardsList.stream().sorted(Comparator.comparingInt(card -> -card.getPrestige()))
				.map(card -> new BuyCardMove(gameState, card)).collect(Collectors.toList());
		return moveList;
	}

	/**
	 * Returns maximum or minimum value if a player one certainly or the difference
	 * of the players prestige points.
	 */
	private int evaluate(GameState gameState) {
		int aiPrestige = gameState.getPlayers().get(0).getPrestige();
		int enemyPrestige = gameState.getPlayers().get(1).getPrestige();
		// this won
		if (aiPrestige >= 15 && aiPrestige > enemyPrestige) {
			return Integer.MAX_VALUE;
		}
		// enemy won
		if (enemyPrestige >= 15 && enemyPrestige > aiPrestige) {
			return Integer.MIN_VALUE;
		}
		// nobody won yet or tied
		aiPrestige = calculateScore(gameState, 0);
		enemyPrestige = -calculateScore(gameState, 1);
		return aiPrestige - enemyPrestige;
	}

	private int calculateScore(GameState gameState, int index) {
		Player player = gameState.getPlayers().get(index);

		int score = player.getPrestige() * 3000;
		score += gemScore(player.getGems());
		score += cardScore(player);
		score += reservedScore(player);

		return score;
	}

	private int reservedScore(Player player) {
		return player.getReservedCards().size() * 20;
	}

	private int cardScore(Player player) {
		Gems bonus = player.getBonus().clone();
		for (GemType gemType : GemType.values()) {
			if (bonus.getAmountOf(gemType) < 5)
				bonus.getGems()[gemType.ordinal()] += 1;
		}
		int cardScore = gemScore(bonus) * 100;
		return cardScore;
	}

	private int gemScore(Gems gems) {
		// Jokers are nice
		int score = gems.getTotalAmount() * 2;
		score += gems.getAmountOf(GemType.JOKER);
		score *= 4;
		return score;
	}

	/**
	 * @return the initialGameState
	 */
	public GameState getInitialGameState() {
		return initialGameState;
	}

	/**
	 * @return the transpositionTable
	 */
	public HashMap<GameState, Integer> getTranspositionTable() {
		return transpositionTable;
	}

	/**
	 * @param transpositionTable the transpositionTable to set
	 */
	public void setTranspositionTable(HashMap<GameState, Integer> transpositionTable) {
		this.transpositionTable = transpositionTable;
	}

}
