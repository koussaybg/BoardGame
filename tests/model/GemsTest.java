package model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GemsTest {
	private Gems gems;
	private Card cardDiamond;
	private Card cardEmerald;
	private Card cardRuby;
	private Card cardSapphire;
	private Card cardOnyx;
	private List<Card> cards;
	private Gems cardCost;

	@Before
	public void setup() {
		cardCost = new Gems(GemAmountType.UNLIMITED);
		// gameState.setPlayers(new List<Player>{player});
		cardDiamond = new Card(0, cardCost, false, 1);
		cardDiamond.setGemType(GemType.DIAMOND);
		cardEmerald = new Card(0, cardCost, false, 1);
		cardEmerald.setGemType(GemType.EMERALD);
		cardRuby = new Card(0, cardCost, false, 1);
		cardRuby.setGemType(GemType.RUBY);
		cardSapphire = new Card(0, cardCost, false, 1);
		cardSapphire.setGemType(GemType.SAPPHIRE);
		cardOnyx = new Card(0, cardCost, false, 1);
		cardOnyx.setGemType(GemType.ONYX);
		cards = new ArrayList<Card>();
		cards.add(cardDiamond);
		cards.add(cardEmerald);
		cards.add(cardRuby);
		cards.add(cardSapphire);
		cards.add(cardOnyx);
	}

	private boolean compare(int[] one, int[] two) {
		boolean same = true;
		for (int i = 0; i < two.length - 1; i++) {
			same = same && (one[i] == two[i]);
		}
		return same;
	}

	@Test
	public void testConstructorCards() {
		gems = new Gems(cards);
		int[] solution = new int[] { 1, 1, 1, 1, 1, 0 };
		assertTrue(compare(gems.getGems(), solution));
	}

	@Test
	public void testConstructorType() {
		// GAMEBOARD 4 Player
		int[] gameboard4 = new int[] { 7, 7, 7, 7, 7, 5 };
		Gems gameboard4Gems = new Gems(GemAmountType.GAMEBOARD_FOUR_PLAYERS);
		assertTrue(gameboard4Gems.getGemAmountType() == GemAmountType.GAMEBOARD_FOUR_PLAYERS);
		assertTrue(compare(gameboard4Gems.getGems(), gameboard4));

		// GAMEBOARD 3 Player
		int[] gameboard3 = new int[] { 5, 5, 5, 5, 5, 5 };
		Gems gameboard3Gems = new Gems(GemAmountType.GAMEBOARD_THREE_PLAYERS);
		assertTrue(gameboard3Gems.getGemAmountType() == GemAmountType.GAMEBOARD_THREE_PLAYERS);
		assertTrue(compare(gameboard3Gems.getGems(), gameboard3));

		// GAMEBOARD 2 Player
		int[] gameboard2 = new int[] { 4, 4, 4, 4, 4, 5 };
		Gems gameboard2Gems = new Gems(GemAmountType.GAMEBOARD_TWO_PLAYERS);
		assertTrue(gameboard2Gems.getGemAmountType() == GemAmountType.GAMEBOARD_TWO_PLAYERS);
		assertTrue(compare(gameboard2Gems.getGems(), gameboard2));

		// UNLIMITED
		Gems unlimitedGems = new Gems(GemAmountType.UNLIMITED);
		assertTrue(unlimitedGems.getGemAmountType() == GemAmountType.UNLIMITED);

		// PLAYER_HAND
		Gems playerGems = new Gems(GemAmountType.PLAYER_HAND);
		assertTrue(playerGems.getGemAmountType() == GemAmountType.PLAYER_HAND);
	}

	@Test
	public void testConstructorTypeIntTypeGameboard() {
		int[] input = new int[] { 1, 2, 3, 4, 5, 6 };
		Gems gems = new Gems(input, GemAmountType.GAMEBOARD_FOUR_PLAYERS);
		assertTrue(compare(gems.getGems(), input) && gems.getGemAmountType() == GemAmountType.GAMEBOARD_FOUR_PLAYERS);
	}

	@Test
	public void testConstructorTypeIntTypePlayer() {
		int[] input = new int[] { 0, 0, 0, 0, 0, 0 };
		Gems gems = new Gems(input, GemAmountType.PLAYER_HAND);
		assertTrue(compare(gems.getGems(), input));
		assertTrue(gems.getGemAmountType() == GemAmountType.PLAYER_HAND);
	}

	@Test
	public void testConstructorTypeIntTypeUnlimited() {
		int[] input = new int[] { 1, 2, 3, 4, 5, 6 };
		Gems gems = new Gems(input, GemAmountType.UNLIMITED);
		assertTrue(compare(gems.getGems(), input) && gems.getGemAmountType() == GemAmountType.UNLIMITED);
	}

	@Test
	public void testIsGEQThan() {
		int[] small = new int[] { 2, 2, 2, 2, 0, 0 };
		int[] great = new int[] { 2, 3, 4, 5, 0, 0 };
		int[] greatJoke = new int[] { 4, 3, 2, 2, 0, 1 };
		int[] smallJoke = new int[] { 3, 2, 1, 0, 0, 1 };
		Gems smallGems = new Gems(small, GemAmountType.UNLIMITED);
		Gems greatGems = new Gems(great, GemAmountType.UNLIMITED);
		Gems greatJokeGems = new Gems(greatJoke, GemAmountType.UNLIMITED);
		Gems smallJokeGems = new Gems(smallJoke, GemAmountType.UNLIMITED);
		assertTrue(greatGems.isGEQThan(smallGems));
		assertTrue(greatJokeGems.isGEQThan(smallGems));
		assertFalse(smallGems.isGEQThan(greatGems));
		assertFalse(smallJokeGems.isGEQThan(smallGems));
	}

	@Test
	public void testTransferGems() {
		// subtract valid, add valid
		int[] from = new int[] { 2, 0, 0, 0, 0, 0 };
		int[] to = new int[] { 2, 0, 0, 0, 0, 0 };
		int[] amount = new int[] { 1, 0, 0, 0, 0, 0 };
		int[] solutionFrom = new int[] { 1, 0, 0, 0, 0, 0 };
		int[] solutionTo = new int[] { 3, 0, 0, 0, 0, 0 };
		Gems fromGems = new Gems(from, GemAmountType.GAMEBOARD_FOUR_PLAYERS);
		Gems toGems = new Gems(to, GemAmountType.PLAYER_HAND);
		Gems amountGems = new Gems(amount, GemAmountType.UNLIMITED);
		assertTrue(Gems.transferGems(fromGems, toGems, amountGems) == GemTransferResult.SUCCESS);
		assertTrue(compare(fromGems.getGems(), solutionFrom));
		assertTrue(compare(toGems.getGems(), solutionTo));

		// subtract invalid
		from = new int[] { 2, 0, 0, 0, 0, 0 };
		to = new int[] { 2, 0, 0, 0, 0, 0 };
		amount = new int[] { 3, 0, 0, 0, 0, 0 };
		solutionFrom = new int[] { 2, 0, 0, 0, 0, 0 };
		solutionTo = new int[] { 2, 0, 0, 0, 0, 0 };
		fromGems = new Gems(from, GemAmountType.GAMEBOARD_FOUR_PLAYERS);
		toGems = new Gems(to, GemAmountType.UNLIMITED);
		amountGems = new Gems(amount, GemAmountType.UNLIMITED);
		assertTrue(Gems.transferGems(fromGems, toGems, amountGems) == GemTransferResult.FAIL);
		assertTrue(compare(fromGems.getGems(), solutionFrom));
		assertTrue(compare(toGems.getGems(), solutionTo));

		// subtract valid, add invalid
		from = new int[] { 4, 0, 0, 0, 0, 0 };
		to = new int[] { 9, 0, 0, 0, 0, 0 };
		amount = new int[] { 2, 0, 0, 0, 0, 0 };
		solutionFrom = new int[] { 2, 0, 0, 0, 0, 0 };
		solutionTo = new int[] { 11, 0, 0, 0, 0, 0 };
		fromGems = new Gems(from, GemAmountType.GAMEBOARD_FOUR_PLAYERS);
		toGems = new Gems(to, GemAmountType.PLAYER_HAND);
		amountGems = new Gems(amount, GemAmountType.UNLIMITED);
		GemTransferResult result = Gems.transferGems(fromGems, toGems, amountGems);
		assertTrue(result == GemTransferResult.RECEIVER_NOT_ENOUGH_CAPACITY);
		assertTrue(compare(fromGems.getGems(), solutionFrom));
		assertTrue(compare(toGems.getGems(), solutionTo));
	}

	@Test
	public void testTransferGemsToPlayerInvalid() {
		int[] from = new int[] { 5, 0, 0, 0, 0, 0 };
		int[] to = new int[] { 0, 0, 0, 0, 0, 0 };
		int[] amount = new int[] { 4, 0, 0, 0, 0, 0 };
		int[] solutionFrom = new int[] { 5, 0, 0, 0, 0, 0 };
		int[] solutionTo = new int[] { 0, 0, 0, 0, 0, 0 };
		Gems fromGems = new Gems(from, GemAmountType.GAMEBOARD_FOUR_PLAYERS);
		Gems toGems = new Gems(to, GemAmountType.PLAYER_HAND);
		Gems amountGems = new Gems(amount, GemAmountType.UNLIMITED);
		GemTransferResult result = Gems.transferGems(fromGems, toGems, amountGems);
		assertTrue(GemTransferResult.FAIL.equals(result));
		assertTrue(Arrays.equals(fromGems.getGems(), solutionFrom));
		assertTrue(Arrays.equals(toGems.getGems(), solutionTo));
	}

	@Test
	public void testCombine() {
		int[] player = new int[] { 1, 0, 1, 0, 1, 0 };
		int[] solution = new int[] { 2, 1, 2, 1, 2, 1 };
		Gems playerGems = new Gems(player, GemAmountType.PLAYER_HAND);
		Gems resultGems = Gems.combine(playerGems, cards);
		assertTrue(compare(resultGems.getGems(), solution));
	}

	@Test
	public void testBuyCardWithJoker() {
		int[] player = new int[] { 1, 0, 0, 0, 0, 2 };
		int[] gameboard = new int[] { 0, 0, 0, 0, 0, 0 };
		int[] amount = new int[] { 1, 1, 1, 0, 0, 0 };
		Gems playerGems = new Gems(player, GemAmountType.PLAYER_HAND);
		Gems gameboardGems = new Gems(gameboard, GemAmountType.GAMEBOARD_TWO_PLAYERS);
		Gems amountGems = new Gems(amount, GemAmountType.UNLIMITED);
		GemTransferResult result = Gems.transferGems(playerGems, gameboardGems, amountGems);
		assertEquals(GemTransferResult.SUCCESS, result);
		assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0 }, playerGems.getGems());
		assertArrayEquals(new int[] { 1, 0, 0, 0, 0, 2 }, gameboardGems.getGems());

	}

	@Test
	public void testClone() {
		int[] gemAmnt = new int[] { 1, 0, 0, 0, 0, 2 };
		Gems gems = new Gems(gemAmnt, GemAmountType.UNLIMITED);
		Gems clone = gems.clone();
		assertTrue(gems.equals(clone));
	}
}