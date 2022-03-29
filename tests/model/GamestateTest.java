package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GamestateTest {

	GameState gameState;

	@Before
	public void setUp() throws Exception {
		gameState = new GameState();

		List<Player> playerList = new ArrayList<Player>();
		playerList.add(new Player(PlayerType.EASY, "test1"));
		playerList.add(new Player(PlayerType.MEDIUM, "test2"));
		gameState.setPlayers(playerList);

		gameState.setCurrentTrain(0);

		gameState.setGems(new Gems(GemAmountType.GAMEBOARD_TWO_PLAYERS));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testgiveGemsBack() {
		Player player = gameState.getCurrentPlayer();
		Gems gems = new Gems(new int[] { 1, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED);
		player.setGems(gems);
		Gems gamegems = new Gems(new int[] { 4, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED);
		Gems givengems = new Gems(new int[] { 1, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED);
		gameState.setGems(gamegems);
		gameState.giveGemsBack(givengems);
		assertEquals(gameState.getGems().getAmountOf(GemType.DIAMOND), 5);
		assertEquals(player.getGems().getAmountOf(GemType.DIAMOND), 0);
		gameState.giveGemsBack(givengems);

	}

	@Test
	public void testGetOpenCards() {
		Gems cost = new Gems(GemAmountType.UNLIMITED);
		List<Card> levelOneCards = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			levelOneCards.add(new Card(i, cost, false, 1));
		}
		gameState.setLevelOneCards(levelOneCards);

		List<Card> levelTwoCards = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			levelTwoCards.add(new Card(i, cost, false, 2));
		}
		gameState.setLevelTwoCards(levelTwoCards);

		List<Card> levelThreeCards = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			levelThreeCards.add(new Card(i, cost, false, 3));
		}
		gameState.setLevelThreeCards(levelThreeCards);

		List<Card> openCards = gameState.getOpenCards();
		assertTrue(openCards.size() == 12);
		int falsecards = 0;
		for (Card card : openCards) {
			if (!levelOneCards.contains(card) && !levelTwoCards.contains(card) && !levelThreeCards.contains(card)) {
				falsecards++;
			}
		}
		assertTrue(falsecards == 0);
	}

	@Test
	public void testGetCurrentPlayer() {
		gameState.setCurrentTrain(0);
		assertTrue(gameState.getCurrentPlayer().getName().equals("test1"));
		gameState.setCurrentTrain(1);
		assertTrue(gameState.getCurrentPlayer().getName().equals("test2"));
	}

	@Test
	public void testIsGameOver() {
		// setup
		List<Player> playerList = new ArrayList<Player>();
		Player player1 = new Player(PlayerType.EASY, "test1");
		Player player2 = new Player(PlayerType.EASY, "test2");
		playerList.add(player1);
		playerList.add(player2);
		player1.setPrestige(10);
		player2.setPrestige(12);
		gameState.setPlayers(playerList);

		// no Player with 15 Prestige Points
		assertFalse(gameState.isGameOver());

		// player1 has 15 Prestige but player2 is on turn
		gameState.setCurrentTrain(1);
		player1.setPrestige(15);
		assertFalse(gameState.isGameOver());

		// player1 has 15 Prestige and is on turn again
		gameState.setCurrentTrain(2);
		assertTrue(gameState.isGameOver());

	}

	@Test
	public void testBuyCard() {
		int[] gems = { 2, 0, 0, 1, 2, 0 };
		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
		Card card = new Card(1, price, false, 1);
		card.setGemType(GemType.DIAMOND);
		List<Card> levelOneCards = new ArrayList<>();
		levelOneCards.add(card);
		gameState.setLevelOneCards(levelOneCards);

		Player player = gameState.getCurrentPlayer();
		Gems playerGems = new Gems(gems, GemAmountType.PLAYER_HAND);
		player.setGems(playerGems);
		gameState.setGems(new Gems(new int[GemType.values().length], GemAmountType.GAMEBOARD_TWO_PLAYERS));
		gameState.buyCard(card, playerGems);
		assertTrue(player.getBoughtCards().contains(card));
		assertTrue(player.getBoughtCards().size() == 1);
		int[] empty = new int[6];
		assertTrue(Arrays.equals(empty, player.getGems().getGems()));
	}

//	@Test(expected=IllegalArgumentException.class)
//	public void testBuyCardWithoutGems() {
//		int[] gems = {2,0,0,1,2,0};
//		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
//		Card card = new Card(1, price , false, 3,1);
//		
//		Player player = gameState.getCurrentPlayer();
//		Gems playerGems = new Gems(GemAmountType.PLAYER_HAND);
//		player.setGems(playerGems);
//		gameState.buyCard(card, playerGems);
//	}

	@Test
	public void testBuyNoble() {
		int[] gems = { 0, 0, 0, 1, 2, 0 };
		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
		Card cardDiamond = new Card(0, price, false, 1);
		cardDiamond.setGemType(GemType.DIAMOND);
		Card cardRuby = new Card(0, price, false, 1);
		cardRuby.setGemType(GemType.RUBY);
		List<Card> bought = new ArrayList<>();
		bought.add(cardDiamond);
		bought.add(cardRuby);

		Player player = gameState.getCurrentPlayer();
		Gems playerGems = new Gems(gems, GemAmountType.PLAYER_HAND);
		player.setGems(playerGems);
		player.setBoughtCards(bought);

		int[] ncost = { 1, 0, 1, 0, 0, 0 };
		Gems noblecost = new Gems(ncost, GemAmountType.UNLIMITED);
		Card noble = new Card(4, noblecost, true, 0);
		List<Card> nobles = new ArrayList<>();
		nobles.add(noble);
		gameState.setNobleCards(nobles);
		gameState.buyNoble(noble);

		assertEquals(4, player.getPrestige());
		assertTrue(player.getBoughtCards().contains(noble));
	}

//	@Test(expected=IllegalArgumentException.class)
//	public void testBuyNobleWithoutCards() {
//		int[] gems = {1,0,3,1,2,0};
//		List<Card> bought = new ArrayList<>();
//		
//		
//		Player player = gameState.getCurrentPlayer();
//		Gems playerGems = new Gems(gems, GemAmountType.PLAYER_HAND);
//		player.setGems(playerGems);
//		player.setBoughtCards(bought);
//		
//		int[] ncost = {1,0,1,0,0,0};
//		Gems noblecost = new Gems(ncost,GemAmountType.UNLIMITED);
//		Card noble = new Card(4,noblecost, true, 4,0);
//		
//		gameState.buyNoble(noble);
//		
//		assertEquals(4,player.getPrestige());	
//	}

	@Test
	public void testReserveCard() {
		int[] gems = { 2, 0, 0, 1, 2, 0 };
		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
		Card card = new Card(1, price, false, 1);
		List<Card> levelone = new ArrayList<>();
		levelone.add(card);
		gameState.setLevelOneCards(levelone);
		gameState.getLevelOneCards().add(card);

		Player player = gameState.getCurrentPlayer();
		Gems playerGems = new Gems(gems, GemAmountType.PLAYER_HAND);
		player.setGems(playerGems);
		gameState.reserveCard(card);

		assertTrue(player.getReservedCards().contains(card));
		assertTrue(player.getReservedCards().size() == 1);
		int[] expected = { 2, 0, 0, 1, 2, 1 };
		assertTrue(Arrays.equals(expected, player.getGems().getGems()));
	}

//	@Test(expected=IllegalArgumentException.class)
//	public void testReserveCardIllegal() {
//		int[] gems = {0,0,0,1,2,0};
//		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
//		Card card1 = new Card(0, price , false, 3,1);
//		Card card2 = new Card(0, price , false, 4,1);
//		Card card3 = new Card(0, price , false, 5,1);
//		List<Card> reserved = new ArrayList<>();
//		reserved.add(card1);		
//		reserved.add(card2);	
//		reserved.add(card3);	
//		
//
//		Card card4 = new Card(1, price , false, 6,1);
//		
//		Player player = gameState.getCurrentPlayer();
//		Gems playerGems = new Gems(gems, GemAmountType.PLAYER_HAND);
//		player.setGems(playerGems);
//		player.setReservedCards(reserved);
//		gameState.reserveCard(card4);
//		
//	}

	@Test
	public void testTakeGems() {

		Player player = gameState.getCurrentPlayer();
		Gems playerGems = new Gems(GemAmountType.PLAYER_HAND);
		player.setGems(playerGems);

		int[] gems = { 1, 1, 0, 0, 1, 0 };
		Gems toTake = new Gems(gems, GemAmountType.UNLIMITED);

		gameState.takeGems(toTake);

		assertTrue(Arrays.equals(player.getGems().getGems(), gems));

		int[] expectedBoardGems = { 3, 3, 4, 4, 3, 5 };
		assertTrue(Arrays.equals(expectedBoardGems, gameState.getGems().getGems()));
	}

	@Test
	public void testBuyCardBonusAndJoker() {
		int[] cost = { 2, 0, 0, 0, 0, 0 };
		Gems price = new Gems(cost, GemAmountType.UNLIMITED);
		Card card = new Card(1, price, false, 1);
		card.setGemType(GemType.DIAMOND);
		List<Card> levelOneCards = new ArrayList<>();
		levelOneCards.add(card);
		gameState.setLevelOneCards(levelOneCards);

		int[] playerGemAmnt = { 1, 0, 0, 0, 0, 0 };
		Gems playerGems = new Gems(playerGemAmnt, GemAmountType.PLAYER_HAND);
		Player player = gameState.getCurrentPlayer();
		player.setGems(playerGems);
		gameState.setPlayers(Collections.singletonList(player));
		gameState.reserveCard(card);
		
		System.err.println(player.getBonus());
		System.err.println(player.getGems());
		System.err.println(card.getCost());
		
		gameState.setGems(new Gems(new int[GemType.values().length], GemAmountType.GAMEBOARD_TWO_PLAYERS));
		gameState.buyCard(card, playerGems);
		assertTrue(player.getBoughtCards().contains(card));
		assertTrue(player.getBoughtCards().size() == 1);
		int[] empty = new int[6];
		assertTrue(Arrays.equals(empty, player.getGems().getGems()));

	}

}
