package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

	Player humanPlayer;
	Player aiplayer;
	
	@Before
	public void setUp() throws Exception {
		humanPlayer = new Player(PlayerType.HUMAN, "Toni");
		aiplayer = new Player(PlayerType.MEDIUM, "Aioli");
		
	}

	@Test
	public void testIsAI() {
		assertFalse(humanPlayer.isAI());
		assertTrue(aiplayer.isAI());
	}

	@Test
	public void testCanReserve() {
		int[] gems = {2,0,0,1,2,0};
		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
		Card card1 = new Card(1, price , false,2);
		Card card2 = new Card(1, price , false,2);
		Card card3 = new Card(1, price , false,2);
		humanPlayer.addReservedCard(card1) ;
		assertTrue(humanPlayer.canReserve());
		humanPlayer.addReservedCard(card2) ;
		assertTrue(humanPlayer.canReserve());
		humanPlayer.addReservedCard(card3) ;
		assertFalse(humanPlayer.canReserve());
		
		
	}
	@Test
	public void testPrestige(){
		int[] gems = {2,0,0,1,2,0};
		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
		Card card1 = new Card(1, price , false,2);
		assertEquals(humanPlayer.getPrestige(),0);
		humanPlayer.addBoughtCard(card1);
		assertEquals(humanPlayer.getPrestige(),1);

	}
	@Test
	public void testbonus() {
		int[] gems = {2,0,0,1,2,0};
		Gems price = new Gems(gems, GemAmountType.UNLIMITED);
		Card card1 = new Card(1, price , false,2);
		card1.setGemType(GemType.DIAMOND);
		humanPlayer.addBoughtCard(card1);
		assertEquals(humanPlayer.getBonus().getAmountOf(GemType.DIAMOND),1);
	}

}
