package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

public class GameTest {
	Game game;
	GameState gameState1;
	GameState gameState2;

	List<Card> dummyCards;
	List<Card> dummyNobles;

	@Before
	public void setUp() throws Exception {
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(new Player(PlayerType.EASY, "test1"));
		playerList.add(new Player(PlayerType.MEDIUM, "test2"));
		
		dummyCards = this.dummycards();
		dummyNobles = this.dummyNobles();
		game = new Game(playerList,dummyCards,dummyNobles);
	}

	public List<Card> dummycards(){
		List<Card> cards = new ArrayList<>();
		Gems cost = new Gems(GemAmountType.UNLIMITED);
		for(int i=0; i<40;i++) {
			cards.add(new Card(0, cost, false, 1));
		}
		
		for(int i=40; i<70;i++) {
			cards.add(new Card(0, cost, false, 2));
		}
		
		for(int i=70; i<90;i++) {
			cards.add(new Card(0, cost, false, 3));
		}
		return cards;
	}
	
	public List<Card> dummyNobles(){
		List<Card> cards = new ArrayList<>();
		Gems cost = new Gems(GemAmountType.UNLIMITED);
		for(int i=0; i<10;i++) {
			cards.add(new Card(2, cost, true,6));
		}
		return cards;
	}
	
	@Test
	public void testConstructorNoblesSizeTwoPlayer() {
		assertEquals(3, game.getCurrentGameState().getNobleCards().size());
	}
	
	@Test
	public void testConstructorNoblesSizeThreePlayer() {
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(new Player(PlayerType.EASY, "test1"));
		playerList.add(new Player(PlayerType.MEDIUM, "test2"));
		playerList.add(new Player(PlayerType.MEDIUM, "test3"));
		
		game = new Game(playerList,dummyCards,dummyNobles);
		assertEquals(4, game.getCurrentGameState().getNobleCards().size());
	}
	
	@Test
	public void testConstructorNoblesSizeFourPlayer() {
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(new Player(PlayerType.EASY, "test1"));
		playerList.add(new Player(PlayerType.MEDIUM, "test2"));
		playerList.add(new Player(PlayerType.MEDIUM, "test3"));
		playerList.add(new Player(PlayerType.MEDIUM, "test4"));
		
		game = new Game(playerList,dummyCards,dummyNobles);
		assertEquals(5, game.getCurrentGameState().getNobleCards().size());
	}
	
	@Test
	public void testConstructorCards() {
		GameState first= game.getCurrentGameState();
		
		List<Card> levelone = first.getLevelOneCards();		
		for(int i=0;i<40;i++) {
			assertEquals(dummyCards.get(i),levelone.get(i));
		}
		
		List<Card> leveltwo = first.getLevelTwoCards();		
		for(int i=40;i<70;i++) {
			assertEquals(dummyCards.get(i),leveltwo.get(i-40));
		}
		
		List<Card> levelthree = first.getLevelThreeCards();		
		for(int i=70;i<90;i++) {
			assertEquals(dummyCards.get(i),levelthree.get(i-70));
		}
	}
	
	@Test
	public void testConstructorPlayers() {
		GameState first= game.getCurrentGameState();
		List<Player> player = first.getPlayers();
		assertEquals(player.get(0).getName(),"test1");
		assertEquals(player.get(1).getName(),"test2");
		
	}
	
	@Test
	public void testhighscorable() {
		assertEquals(true,game.isHighscoreable());
		
	}
	
	
	@Test
	public void testCloneGameState() {
		gameState1 = game.getCurrentGameState();
		game.cloneGameState();
		gameState2 = game.getCurrentGameState();
		assertTrue(gameState1.getSuccessor() == gameState2);
		assertTrue(gameState2.getPredecessor() == gameState1);
		assertTrue(gameState1.getCurrentTrain()+1==gameState2.getCurrentTrain());
		assertTrue(gameState1.getLevelOneCards().equals(gameState2.getLevelOneCards()));
		assertTrue(gameState1.getLevelTwoCards().equals(gameState2.getLevelTwoCards()));
		assertTrue(gameState1.getLevelThreeCards().equals(gameState2.getLevelThreeCards()));
		assertTrue(gameState1.getNobleCards().equals(gameState2.getNobleCards()));
		
	}

}
