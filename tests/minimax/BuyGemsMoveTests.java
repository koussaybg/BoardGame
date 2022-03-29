package minimax;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.GameState;
import model.GemAmountType;
import model.Gems;
import model.Player;
import model.PlayerType;

public class BuyGemsMoveTests {
	GameState gameState;
	Player curPlayer;

	@Before
	public void setUp() {
		gameState = new GameState();
		curPlayer = new Player(PlayerType.HARD, "AI");
		gameState.setPlayers(Collections.singletonList(curPlayer));
	}

	@Test
	public void takeThreeGiveNone() {
		gameState.setGems(new Gems(new int[] { 4, 4, 4, 4, 4, 5 }, GemAmountType.GAMEBOARD_TWO_PLAYERS));
		curPlayer.setGems(new Gems(new int[] { 0, 0, 0, 0, 0, 0 }, GemAmountType.PLAYER_HAND));

		List<GetGemsMove> moves = GetGemsMove.getPossibleMoves(gameState);
		for (GetGemsMove move : moves) {
			int giveAway = Arrays.stream(move.getGemsToGiveAway().getGems()).sum();
			assertTrue(giveAway == 0);
			System.out.println(move.toString());
		}
	}
	
	@Test
	public void onlyTwoPossibleGiveTwoBack() {
		gameState.setGems(new Gems(new int[] { 0, 0, 0, 2, 3, 2}, GemAmountType.GAMEBOARD_TWO_PLAYERS));
		curPlayer.setGems(new Gems(new int[] { 1, 2, 3, 2, 1, 1}, GemAmountType.PLAYER_HAND));
		
		List<GetGemsMove> moves = GetGemsMove.getPossibleMoves(gameState);
		for (GetGemsMove move : moves) {
			int take = Arrays.stream(move.getGemsToTake().getGems()).sum();
			int giveAway = Arrays.stream(move.getGemsToGiveAway().getGems()).sum();

			System.out.println(move.toString());
			assertTrue(giveAway == take);
			assertTrue(curPlayer.getGems().contains(move.getGemsToGiveAway()));
			assertTrue(gameState.getGems().contains(move.getGemsToTake()));
		}
	}
	
	@Test
	public void testOnlyTwoPossible() {
		gameState.setGems(new Gems(new int[] { 0, 0, 0, 2, 3, 4}, GemAmountType.GAMEBOARD_TWO_PLAYERS));
		curPlayer.setGems(new Gems(new int[] { 0, 0, 0, 0, 0, 0}, GemAmountType.PLAYER_HAND));
		
		List<GetGemsMove> moves = GetGemsMove.getPossibleMoves(gameState);
		System.out.println(moves.toString());
		System.err.println(moves.size());
		assertTrue(moves.size() == 1);
		for (GetGemsMove move : moves) {
			int take = Arrays.stream(move.getGemsToTake().getGems()).sum();
			int giveAway = Arrays.stream(move.getGemsToGiveAway().getGems()).sum();

			System.out.println(move.toString());
			assertTrue(curPlayer.getGems().contains(move.getGemsToGiveAway()));
			assertTrue(gameState.getGems().contains(move.getGemsToTake()));
		}
	}

}
