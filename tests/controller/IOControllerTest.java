package controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.*;

public class IOControllerTest {
	
	private IOController ioController;

	@Before
	public void setup() {
		ioController = new IOController(new SplendorController());
	}

	private Player createPlayer(int index){
		Player newPlayer = new Player(PlayerType.HUMAN, "testPlayer" + index);
		Gems gems = new Gems((new int[] { 3, 2, 1, 0, 0, 1 }), GemAmountType.PLAYER_HAND); 
		newPlayer.setGems(gems);
		Gems cardCost = new Gems(GemAmountType.UNLIMITED);
		Card cardDiamond = new Card(0, cardCost, false, 1);
		cardDiamond.setGemType(GemType.DIAMOND);
		Card cardEmerald = new Card(0, cardCost, false, 1);
		cardEmerald.setGemType(GemType.EMERALD);
		Card cardRuby = new Card(0, cardCost, false, 1);
		cardRuby.setGemType(GemType.RUBY);
		Card cardSapphire = new Card(0, cardCost, false, 1);
		cardSapphire.setGemType(GemType.SAPPHIRE);
		Card cardOnyx = new Card(0, cardCost, false, 1);
		cardOnyx.setGemType(GemType.ONYX);
		List<Card> cards = new ArrayList<Card>();
		cards.add(cardDiamond);
		cards.add(cardEmerald);
		cards.add(cardRuby);
		cards.add(cardSapphire);
		cards.add(cardOnyx);
		newPlayer.setBoughtCards(cards);
		return newPlayer;
	}
	
	@Test
	public void saveAndLoadHighscore() {
		Highscore highscore = new Highscore(createPlayer(1), 10);
		ioController.saveHighscore(highscore);
		
		Highscore savedHighscore = ioController.loadHighscores().get(0);
		assertTrue(highscore.equals(savedHighscore));
	}

	@Test
	public void deleteHighscores() {
		Highscore highscore = new Highscore(createPlayer(1), 10);
		ioController.saveHighscore(highscore);
		ioController.deleteHighscores();
		List<Highscore> savedHighscores = ioController.loadHighscores();
		assertTrue(savedHighscores.isEmpty());
	}

	@Test
	public void loadDefaultCards() {
		ioController.getDefaultCards();
	}
}
