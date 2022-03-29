package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import model.Card;
import model.Game;
import model.Player;
import model.PlayerType;
import model.Splendor;

public class NewGameControllerTest {
	private SplendorController splController;
	private Splendor spl;
	private NewGameController ngController;
	private IOController ioController;

	@Before
	public void setUp() throws Exception {

		splController = new SplendorController();
		ngController = new NewGameController(splController);
		splController.setNewGameController(ngController);
		ioController = splController.getIOController();
		spl = new Splendor();
		splController.setSplendor(spl);
	}

	public void startNewGameWithFilesTest() {

		List<Player> playerList = new ArrayList<Player>();

		playerList.add(new Player(PlayerType.EASY, "AI 1"));
		playerList.add(new Player(PlayerType.EASY, "AI 2"));

		//Tuple<List<Card>, List<Card>> cardsList = ioController.getDefaultCards(); 

		ngController.startNewGame(playerList, new File("./resources/standardCards/splendor-entwicklungskarten.csv"), new File("./resources/standardCards/splendor-adligenkarten.csv"));
		List<Card> cards = ioController.loadCards(new File("./resources/standardCards/splendor-entwicklungskarten.csv"));
		List<Card> nobleCards = ioController.loadNobles(new File("./resources/standardCards/splendor-adligenkarten.csv"));
		Assert.assertEquals(spl.getGame(), new Game(playerList, cards, nobleCards));

		// Assert.assertNotNull(spl.getGame());
	}

	public void startNewGameNoFilesTest() {

		List<Player> playerList = new ArrayList<Player>();

		playerList.add(new Player(PlayerType.EASY, "AI 1"));
		playerList.add(new Player(PlayerType.EASY, "AI 2"));

		
		List<Card> cards = ioController.loadCards(new File("./resources/standardCards/splendor-entwicklungskarten.csv"));
		List<Card> nobleCards = ioController.loadNobles(new File("./resources/standardCards/splendor-adligenkarten.csv"));

		ngController.startNewGame(playerList);

		Assert.assertEquals(spl.getGame(), new Game(playerList, cards, nobleCards));
		// Assert.assertNotNull(spl.getGame());
	}

	public void startNewGameWithFilesHighscorableTest() {

		List<Player> playerList = new ArrayList<Player>();

		playerList.add(new Player(PlayerType.EASY, "AI 1"));
		playerList.add(new Player(PlayerType.EASY, "AI 2"));


		ngController.startNewGame(playerList, new File("./resources/standardCards/splendor-entwicklungskarten.csv"), new File("./resources/standardCards/splendor-adligenkarten.csv"));
		List<Card> cards = ioController.loadCards(new File("./resources/standardCards/splendor-entwicklungskarten.csv"));
		List<Card> nobleCards = ioController.loadNobles(new File("./resources/standardCards/splendor-adligenkarten.csv"));

		Assert.assertFalse(spl.getGame().isHighscoreable());
		// Assert.assertNotNull(spl.getGame());
	}

	public void startNewGameNoFilesHighscorableTest() {

		List<Player> playerList = new ArrayList<Player>();

		playerList.add(new Player(PlayerType.EASY, "AI 1"));
		playerList.add(new Player(PlayerType.EASY, "AI 2"));

		ngController.startNewGame(playerList);

		Assert.assertTrue(spl.getGame().isHighscoreable());
		// Assert.assertNotNull(spl.getGame());
	}

	public void startNewGameWithEmptyFilesTest() {

		List<Player> playerList = new ArrayList<Player>();

		playerList.add(new Player(PlayerType.EASY, "AI 1"));
		playerList.add(new Player(PlayerType.EASY, "AI 2"));


		ngController.startNewGame(playerList, new File("./resources/standardCards/splendor-entwickgskaren.csv"), new File("./resources/standardCards/splendor-adligenkarten.csv"));
		

		Assert.assertNull(spl.getGame());
		// Assert.assertNotNull(spl.getGame());
	}

	public void startNewAITournamentWithHumanTest() {

		List<Player> playerList = new ArrayList<Player>();

		playerList.add(new Player(PlayerType.HUMAN, "Human 1"));
		playerList.add(new Player(PlayerType.HUMAN, "Human 2"));
		playerList.add(new Player(PlayerType.EASY, "AI 2"));

		ngController.startNewGame(playerList, new File("./resources/standardCards/splendor-entwicklungskarten.csv"), new File("./resources/standardCards/splendor-adligenkarten.csv"));

		Assert.assertNull(spl.getGame());
		// Assert.assertNotNull(spl.getGame());
	}
}
