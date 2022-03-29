package controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Card;
import model.Game;
import model.GemAmountType;
import model.GemType;
import model.Gems;
import model.Highscore;
import model.Player;
import model.PlayerType;
import model.Splendor;
import model.Tuple;
import view.viewAUI.HighscoreWindowAUI;

/**
 * @author Konstantin
 */
public class HighscoreControllerTest {

	private HighscoreWindowAUIImpl highscoreWindowAUI;
	private HighscoreController highscoreController;
	private IOController ioController;
	private SplendorController splendorController;

	private Splendor splendor;
	private Player player;


	@Before
	public void setup()
	{
		this.highscoreWindowAUI = new HighscoreWindowAUIImpl();
		this.splendorController = new SplendorController();
		this.ioController = new IOController(splendorController);
		this.highscoreController = new HighscoreController(splendorController, highscoreWindowAUI);

		splendorController.setIOController(ioController);
		splendorController.setHighscoreController(highscoreController);
		this.player = createPlayer(0);
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
	public void resetExistingHighscoresTest() {
		splendor = new Splendor();
		splendorController.setSplendor(splendor);

		Highscore hs1 = new Highscore(this.player, 5);
		Highscore hs2 = new Highscore(this.player, 15);
		List<Highscore> hsList = new ArrayList<Highscore>();
		hsList.add(hs1);

		splendor.setHighscores(hsList);
		ioController.saveHighscore(hs2);

		highscoreController.resetHighscores();

		hsList.clear();

		Assert.assertNotNull(splendor.getHighscores());
		Assert.assertEquals(0, splendor.getHighscores().size());
		Assert.assertEquals(hsList, ioController.loadHighscores());
	}

	@Test
	public void resetNonExistingHighscoresTest() throws Exception {
		splendor = new Splendor();
		splendorController.setSplendor(splendor);

		highscoreController.resetHighscores();

		List<Highscore> hsList = new ArrayList<Highscore>();

		Assert.assertNotNull(splendor.getHighscores());
		Assert.assertEquals(0, splendor.getHighscores().size());
		Assert.assertEquals(hsList, ioController.loadHighscores());
	}

	@Test
	public void loadExistingHighscoresTest() {
		splendor = new Splendor();
		splendorController.setSplendor(splendor);

		Highscore hs1 = new Highscore(this.player, 5);
		Highscore hs2 = new Highscore(this.player, 15);

		List<Highscore> hsList = new ArrayList<Highscore>();
		hsList.add(hs1);
		hsList.add(hs2);

		ioController.saveHighscore(hs1);
		ioController.saveHighscore(hs2);

		highscoreController.loadHighscores();
		splendor.setHighscores(hsList);
		

		Assert.assertEquals(hsList, splendor.getHighscores());
	}

	@Test
	public void loadNonExistingHighscoresTest() {
		splendor = new Splendor();
		splendorController.setSplendor(splendor);

		highscoreController.resetHighscores();
		highscoreController.loadHighscores();

		List<Highscore> hsList = new ArrayList<Highscore>();
		
		Assert.assertNotNull(splendor.getHighscores());
		Assert.assertEquals(0, splendor.getHighscores().size());
		Assert.assertEquals(hsList, ioController.loadHighscores());
	}

	@Test
	public void createFirstHighscore() {
		splendor = new Splendor();
		splendorController.setSplendor(splendor);

		List<Player> plList = new ArrayList<Player>();
		plList.add(createPlayer(23));
		Player player2 = createPlayer(32);
		player2.setPlayerType(PlayerType.EASY);
		plList.add(player2);

		// späteren Dateipfad ergänzen
		Tuple<List<Card>, List<Card>> defaultCards = ioController.getDefaultCards();

		Game testGame = new Game(plList, defaultCards.getFirstValue(), defaultCards.getSecondValue());
		highscoreController.createHighscore(testGame);

		Assert.assertNotNull(splendor.getHighscores());
	}

	@Test
	public void createSecondHighscore() {
		splendor = new Splendor();
		splendorController.setSplendor(splendor);

		ioController.deleteHighscores();

		List<Highscore> hsList = new ArrayList<Highscore>();
		Highscore hs1 = new Highscore(createPlayer(2), 50);
		hsList.add(hs1);
		splendor.setHighscores(hsList);
		ioController.saveHighscore(hs1);

		List<Player> plList = new ArrayList<Player>();
		plList.add(createPlayer(23));
		Player player2 = createPlayer(32);
		player2.setPlayerType(PlayerType.EASY);
		plList.add(player2);

		// späteren Dateipfad ergänzen
		Tuple<List<Card>, List<Card>> defaultCards = ioController.getDefaultCards();

		Game testGame = new Game(plList, defaultCards.getFirstValue(), defaultCards.getSecondValue());
		highscoreController.createHighscore(testGame);
		

		Assert.assertEquals(2, splendor.getHighscores().size());
	}

	class HighscoreWindowAUIImpl implements HighscoreWindowAUI {

		@Override
		public void refreshHighscores()
		{
		}
	
	}
}