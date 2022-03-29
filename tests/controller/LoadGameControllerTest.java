
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import model.Card;
import model.Game;
import model.Player;
import model.PlayerType;
import model.Splendor;
import model.Tuple;
import view.viewAUI.LoadGameWindowAUI;
import view.viewController.LoadGameWindowController;

public class LoadGameControllerTest {

	private SplendorController splController;
	private IOController io;
	private Splendor spl;
	private File testFile;
	private LoadGameController lgController;
	private LoadGameWindowAUIImpl loadGameWindowAUI;
	private Game testGame;



	@Before
	public void setUp() throws Exception {
		splController = new SplendorController();
		loadGameWindowAUI = new LoadGameWindowAUIImpl();
		lgController = new LoadGameController(splController, new LoadGameWindowController());
		splController.setLoadGameController(lgController);
		io = new IOController(splController);
		splController.setIOController(io);
		spl = new Splendor();
		splController.setSplendor(spl);
		String GAME_PATH =  ("./resources/saveData/") + ("savedGames/") ;


		Player player1 = new Player(PlayerType.HUMAN, "test player");
		Player player2 = new Player(PlayerType.HUMAN, "test2");
		List<Player> players = new LinkedList<>();
		players.add(player1);
		players.add(player2);
		Tuple<List<Card>, List<Card>> cards = io.getDefaultCards();

		testGame = new Game(players, cards.getFirstValue(), cards.getSecondValue());

		testFile = new File (GAME_PATH + "testFile.ser");
	}

	@Test
	public void deleteGameTest() {
		io.saveCurrentGame();
		List<Tuple<File, String>> savedfiles = io.loadGames();
		Assert.assertTrue(contains(savedfiles, testFile));

		lgController.deleteGame(testFile);
		Assert.assertTrue(!contains(savedfiles, testFile));
	}

	@Test
	public void loadSavedGamesTest() throws IOException, ClassNotFoundException {
		FileOutputStream fileOutputStream  = new FileOutputStream(testFile);
		ObjectOutputStream objectOutputStream  = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(testGame);
		objectOutputStream.flush();
		objectOutputStream.close();

		FileInputStream fileInputStream   = new FileInputStream(testFile);
		ObjectInputStream objectInputStream  = new ObjectInputStream(fileInputStream);
		Game savedGame = (Game) objectInputStream.readObject();
		objectInputStream.close();

		spl.setGame(savedGame);
		io.saveCurrentGame();

		lgController.loadSavedGames();
		List<Tuple<File, String>> savedfiles = io.loadGames();
		Assert.assertEquals(savedfiles, io.loadGames() );
	}

	@Test
	public void startSavedGameTest() throws IOException, ClassNotFoundException {
		FileOutputStream fileOutputStream  = new FileOutputStream(testFile);
		ObjectOutputStream objectOutputStream  = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(testGame);
		objectOutputStream.flush();
		objectOutputStream.close();

		FileInputStream fileInputStream   = new FileInputStream(testFile);
		ObjectInputStream objectInputStream  = new ObjectInputStream(fileInputStream);
		Game savedGame = (Game) objectInputStream.readObject();
		objectInputStream.close();

		spl.setGame(savedGame);

		io.saveCurrentGame();

		List<Tuple<File, String>> savedfiles = io.loadGames();
		Assert.assertNotNull(savedfiles);

		lgController.startSavedGame(testFile);
		Assert.assertEquals( savedGame , spl.getGame());
	}

	private boolean contains(List<Tuple<File, String>> savedfiles, File file) {
		for (Tuple<File, String> tuple : savedfiles) {
			if (tuple.getFirstValue().equals(file)) {
				return true;
			}
		}
		return false;
	}

	class LoadGameWindowAUIImpl implements LoadGameWindowAUI {

		public boolean showenFiles = false;
		public boolean showenGames = false;


		@Override
		public void showGameList(List<Tuple<File, String>> games) {
			showenGames = true ;
		}

		@Override
		public void enterGame() {
			showenFiles = true;

		};
	}

}