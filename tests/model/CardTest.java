package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CardTest {
	Player player;
	Card card;


	@Before
	public void setUp(){
		int[] gems = {2,0,0,1,2,0};
		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
		card = new Card(1, price , false,2);
		player = new Player(PlayerType.HUMAN, "Toni");
		
	}

	@Test
	public void testCanBuypositive() {
		int []gems = {3,0,0,1,2,0};
		Gems playerGems = new Gems(gems, GemAmountType.PLAYER_HAND);
		player.setGems(playerGems);
		assertTrue(card.canBuy(player));
	}
	
	@Test
	public void testCanBuypositiveWithJoker() {
		int []gems = {3,0,0,1,1,1};
		Gems playerGems = new Gems(gems, GemAmountType.PLAYER_HAND);
		player.setGems(playerGems);
		assertTrue(card.canBuy(player));
	}
	
	@Test
	public void testCanBuyNegative() {
		int []gems = {3,0,0,2,1,0};
		Gems playerGems = new Gems(gems, GemAmountType.PLAYER_HAND);
		player.setGems(playerGems);
		assertFalse(card.canBuy(player));
	}
	
	@Test
	public void testCanBuy() {
		GameState gameState = new GameState();
		Player player1 = new Player(PlayerType.HUMAN, "player1");
		Player player2 = new Player(PlayerType.HUMAN, "player1");
		Card card = new Card(10, new Gems(new int[] { 0, 0, 2, 0, 0, 0 }, GemAmountType.UNLIMITED), false, 1);
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(player1);
		playerList.add(player2);
		gameState.setPlayers(playerList);
		gameState.setLevelOneCards(Collections.singletonList(card));
		
		player1.setGems(new Gems(new int[] { 0, 0, 2, 0, 0, 0 }, GemAmountType.PLAYER_HAND));
		player1.addReservedCard(card);
		
		card.setGemType(GemType.DIAMOND);
		System.out.println(card.canBuy(player1));
	}
	
	@Test
	public void testCanBuy2() {
		GameState gameState = new GameState();
		Player player1 = new Player(PlayerType.HUMAN, "player1");
		Player player2 = new Player(PlayerType.HUMAN, "player1");
		Card card = new Card(10, new Gems(new int[] { 4, 0, 0, 2, 1, 0 }, GemAmountType.UNLIMITED), false, 1);
		card.setGemType(GemType.DIAMOND);
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(player1);
		playerList.add(player2);
		gameState.setPlayers(playerList);
		gameState.setLevelOneCards(Collections.singletonList(card));
		
		player1.setGems(new Gems(new int[] { 1, 1, 1, 0, 2, 2 }, GemAmountType.PLAYER_HAND));
		player1.addReservedCard(card);
		int[] playerBonus = new int[] { 1, 0, 0, 1, 0, 0 };
		for(int i = 0; i < playerBonus.length; i++) {
			player1.getBonus().getGems()[i] = playerBonus[i];
		}
		
		assertTrue(card.canBuy(player1));
	}
	
}
