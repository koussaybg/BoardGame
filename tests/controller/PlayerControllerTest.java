package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;
import view.viewAUI.GameboardWindowAUI;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PlayerControllerTest {

	private PlayerController playerController;

	private SplendorController splendorController;
	private Splendor splendor;
	private Player player ;
    private GameboardWindowAUI gameboardWindowAUI ;
    private GameboardController gameboardController ;
	@Before
	public void setup() {

		splendor = new Splendor();
		splendorController = new SplendorController();
		splendorController.setSplendor(splendor);
		gameboardWindowAUI=new GameboardWindowAUIImpl() ;
		gameboardController = new GameboardController(splendorController, gameboardWindowAUI);
		gameboardController.setGameboardWindowAUI(gameboardWindowAUI);
		playerController = new PlayerController(splendorController, new GameboardWindowAUIImpl());
		splendorController.setGameboardController(gameboardController);

		Gems gems=new Gems(new int[]{5,5,5,5,5,5},GemAmountType.GAMEBOARD_FOUR_PLAYERS) ;
		player = new Player(PlayerType.HUMAN, "test player");
		Player player2 = new Player(PlayerType.HUMAN, "test2");
		List<Player> players = new LinkedList<>();
		players.add(player);
		players.add(player2);
		// TODO add cards
		Tuple<List<Card>, List<Card>> cards = new IOController(splendorController).getDefaultCards();
		splendor.setGame(new Game(players, cards.getFirstValue(), cards.getSecondValue()));
		splendor.getGame().getCurrentGameState().setGems(gems);
	}

	@Test
	public void testBuyCardHavingGems() {
		System.out.println(splendor.getGame().getCurrentGameState().getCurrentPlayer().getName());
		int[] playerGemArray = new int[]{1,0,0,0,0,0};
		Gems playerGems = new Gems(playerGemArray, GemAmountType.PLAYER_HAND);
        player.setGems(playerGems);
		int[] costGemArray = new int[]{1,0,0,0,0,0};
		Gems costGems = new Gems(costGemArray, GemAmountType.GAMEBOARD_TWO_PLAYERS);
        Card card = new Card(1, costGems, false,2);
        playerController.buyCard(card);

		assertTrue(player.getBoughtCards().contains(card));
		assertTrue(player.getGems().getAmountOf(GemType.DIAMOND) == 0);

	}
	@Test
	public void testBuyCardHavingGemsJoker() {
		int[] playerGemArray = new int[]{1,0,0,0,0,1};
		Gems playerGems = new Gems(playerGemArray, GemAmountType.PLAYER_HAND);player.setGems(playerGems);
		int[] costGemArray = new int[]{2,0,0,0,0,0};
		Gems costGems = new Gems(costGemArray, GemAmountType.GAMEBOARD_TWO_PLAYERS);
		Card card = new Card(1, costGems, false,2);
		playerController.buyCard(card);

		assertTrue(player.getBoughtCards().contains(card));
		assertEquals(0, player.getGems().getAmountOf(GemType.DIAMOND));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBuyCardNotHavingGems() {
		Gems gems = new Gems(GemAmountType.PLAYER_HAND);
		player.setGems(gems);

		int[] costGemArray = new int[]{2,0,0,0,0,0};
		Gems costGems = new Gems(costGemArray, GemAmountType.GAMEBOARD_TWO_PLAYERS);
		Card card = new Card(1, costGems, false,2);

		playerController.buyCard(card);

		assertFalse(Arrays.asList(player.getBoughtCards()).contains(card));
	}

	@Test
	public void testReserveCard() {
		Gems gems = new Gems(GemAmountType.PLAYER_HAND);
		player.setGems(gems);

		int[] costGemArray = new int[]{2,0,0,0,0,0};
		Gems costGems = new Gems(costGemArray, GemAmountType.GAMEBOARD_TWO_PLAYERS);
		Card card = new Card(1, costGems, false,2);

		playerController.reserveCard(card);
		assertTrue((player.getReservedCards()).contains(card));
		assertArrayEquals(new int[]{0,0,0,0,0,1},player.getGems().getGems());
	}

	@Test
	public void testBuyNobleEnoughPrestige() {
		Gems gems = new Gems(GemAmountType.PLAYER_HAND);
		player.setGems(gems);

		List<Card> cards = new LinkedList<>();
		cards.add(new Card(4, new Gems(GemAmountType.UNLIMITED), false,2));
		player.setBoughtCards(cards);

		Card card = new Card(4, new Gems(GemAmountType.UNLIMITED), true,4);

		playerController.buyNoble(card);

		assertTrue(player.getBoughtCards().contains(card));
		assertEquals(player.getGems(), gems);
	}

	@Test
	public void testBuyNobleNotEnoughPrestige() {
		Gems gems = new Gems(GemAmountType.PLAYER_HAND);
		player.setGems(gems);
		List<Card> cards = new LinkedList<>();
		cards.add(new Card(3, new Gems(GemAmountType.UNLIMITED), false,2));
		cards.get(0).setGemType(GemType.ONYX);
		player.setBoughtCards(cards);
        Gems cost= new Gems(new int[]{1,0,0,0,0,0},GemAmountType.UNLIMITED) ;
		Card card = new Card(4, cost, true,4);
        Player pointed_player=splendor.getGame().getCurrentGameState().getCurrentPlayer() ;
        playerController.buyNoble(card);
        // as NextTrain() is called in buyNoble , we need the pointer to point on the first player
		assertFalse(pointed_player.getBoughtCards().contains(card));
		assertEquals(player.getGems(), gems);
	}

	@Test
	public void testTakeGemsThreeGems() {
		int[] gemAmountArray = new int[]{1,1,1,0,0,0};
		Gems gems = new Gems(gemAmountArray, GemAmountType.UNLIMITED);
		playerController.takeGems(gems);
        assertTrue(player.getGems().getAmountOf(GemType.DIAMOND) == 1);
		assertTrue(player.getGems().getAmountOf(GemType.RUBY) == 1);
		assertTrue(player.getGems().getAmountOf(GemType.EMERALD) == 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTakeGemsThreeGemsTwoEqual() {
		int[] gemAmountArray = new int[]{1,2,0,0,0,0};
		Gems gems = new Gems(gemAmountArray, GemAmountType.UNLIMITED);
		playerController.takeGems(gems);
		assertTrue(player.getGems().getAmountOf(GemType.DIAMOND) == 0);
		assertTrue(player.getGems().getAmountOf(GemType.EMERALD) == 0);
	}

	@Test
	public void testTakeGemsTwoGems() {
		int[] gemAmountArray = new int[]{0,0,2,0,0,0};
		Gems gems = new Gems(gemAmountArray, GemAmountType.UNLIMITED);
		playerController.takeGems(gems);
		assertTrue(player.getGems().getGems()[2] == 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTakeGemsFourGems() {
		int[] gemAmountArray = new int[]{1,1,1,1,0,0};
		Gems gems = new Gems(gemAmountArray, GemAmountType.UNLIMITED);

		playerController.takeGems(gems);
		assertTrue(player.getGems().getAmountOf(GemType.DIAMOND) == 0);
		assertTrue(player.getGems().getAmountOf(GemType.EMERALD) == 0);
		assertTrue(player.getGems().getAmountOf(GemType.RUBY) == 0);
	}

	@Test
	public void testBuyCardWithBonus() {
		int[] playerGemArray = new int[]{1,0,0,0,0,0};
		Gems playerGems = new Gems(playerGemArray, GemAmountType.PLAYER_HAND);
        player.setGems(playerGems);
        Card card = new Card(0, new Gems(GemAmountType.UNLIMITED), false, 1);
        card.setGemType(GemType.DIAMOND);
        player.addBoughtCard(card);
        
        Gems cardCost = new Gems(new int[]{2,0,0,0,0,0}, GemAmountType.UNLIMITED);
        Card cardToBuy = new Card(0, cardCost, false, 1);
        
        playerController.buyCard(cardToBuy);
        assertTrue(player.getBoughtCards().contains(cardToBuy));
	}

	class GameboardWindowAUIImpl implements GameboardWindowAUI {


		@Override
		public void refreshPlayersHand() {
			
		}

		@Override
		public void refreshCurrentPlayer() {

		}

		@Override
		public void refreshCards() {

		}

		@Override
		public void refreshGems() {

		}

		@Override
		public void refreshNobles() {

		}

		@Override
		public void activateUndo(boolean active) {

		}

		@Override
		public void activateRedo(boolean active) {

		}

		@Override
		public void refreshGameState() {

		}

		@Override
		public void showTip(Card card, boolean buy) {

		}

		@Override
		public void gameOver(Player winner) {

		}

		@Override
		public void showNobles(List<Card> nobles) {

		}

		@Override
		public void showBlitzdings(){
			
		}

		@Override
		public void hideBlitzdings(){
			
		}

        @Override
        public void gemReturn(int amount) {

        }

    }
}