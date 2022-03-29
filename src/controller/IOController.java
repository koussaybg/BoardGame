package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import model.Card;
import model.Game;
import model.GameState;
import model.GemAmountType;
import model.GemType;
import model.Gems;
import model.Highscore;
import model.Tuple;

public class IOController {

	/**
	 * provides path to highscore files
	 */
	private final String HIGHSCORE_PATH;

	/**
	 * provides path to game files
	 */
	private final String GAME_PATH;

	private final String CARD_PATH;

	private final String NOBLE_CARD_PATH;

	/**
	 * used for communication with controll layer
	 */
	private SplendorController splendorController;

	/**
	 * default constructor
	 *
	 * @param splendorController the splendorConroller
	 */
	public IOController(SplendorController splendorController) {
		this.splendorController = splendorController;
		String saveDataBasePath = "./resources/saveData/";
		File saveDataBaseDir = new File(saveDataBasePath);

		HIGHSCORE_PATH = saveDataBasePath + "highscores/";
		GAME_PATH = saveDataBasePath + "savedGames/";
		CARD_PATH = "./resources/standardCards/splendor-entwicklungskarten.csv";
		NOBLE_CARD_PATH = "./resources/standardCards/splendor-adligenkarten.csv";

		if (!saveDataBaseDir.exists()) {
			saveDataBaseDir.mkdir();
		}

		File savedGamesDir = new File(GAME_PATH);
		if (!savedGamesDir.exists()) {
			savedGamesDir.mkdir();
		}

		File highscoresDir = new File(HIGHSCORE_PATH);
		if (!highscoresDir.exists()) {
			highscoresDir.mkdir();
		}
	}

	/**
	 * saves current game to GAME_PATH
	 * 
	 * @throws IOException thrown if game file could not be saved
	 */
	public void saveCurrentGame() {
		File file = new File(GAME_PATH + serializeGameToFileName() + ".ser");
		Game currentGame = splendorController.getSplendor().getGame();
		saveFile(file, currentGame);
	}

	/**
	 * loads set of cards
	 * 
	 * @param file file containing cards
	 * @throws IOException thrown if file not found
	 */
	List<Card> loadCards(File file) {
		return parseFile(file, false);
	}

	/**
	 * laods set of noble cards
	 * 
	 * @param file file containing noble cards
	 * @throws IOException thrown if file not found
	 */
	List<Card> loadNobles(File file) {
		return parseFile(file, true);
	}

	/**
	 * loads game to resume
	 * 
	 * @param file file containing saved game
	 * @throws IOException thrown if file not found
	 */
	Game loadGame(File file) {
		Game game = loadFile(file);
		return game;
	}

	/**
	 * returns list of all files in GAME_PATH containing saved games
	 * 
	 * @return list of saved games as files
	 * @throws IOException thrown if GAME_PATH not found
	 */
	List<Tuple<File, String>> loadGames() {
		File gamePath = new File(GAME_PATH);
		File[] savedGames = gamePath.listFiles();
		return Arrays.stream(savedGames).map(file -> toFileGameNameTuple(file)).filter(tuple -> tuple != null)
				.collect(Collectors.toList());
	}

	private Tuple<File, String> toFileGameNameTuple(File file) {
		try {
			Tuple<File, String> tuple = new Tuple<File, String>();
			tuple.setFirstValue(file);

			String name = file.getName();
			name = name.substring(0, name.lastIndexOf("."));

			tuple.setSecondValue(name);

			return tuple;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * returns list of all files in HIGHSCORE_PATH containing saved highscores
	 * 
	 * @return list of saved highscores as files
	 * @throws IOException thrown if HIGHSCORE_PATH not found
	 */
	List<Highscore> loadHighscores() {
		File highscorePath = new File(HIGHSCORE_PATH);
		File[] highscores = highscorePath.listFiles();
		List<Highscore> highscoreList = Arrays.stream(highscores).map(file -> toHighscore(file))
				.collect(Collectors.toList());
		return highscoreList;
	}

	private Highscore toHighscore(File file) {
		Highscore highscore = loadFile(file);
		return highscore;
	}

	/**
	 * saves given highscore (summary of game stats) as new highscore
	 * 
	 * @param highscore highscore to save
	 * @throws IOException thrown if highscore file could not be saved
	 */
	void saveHighscore(Highscore highscore) {
		File file = new File(HIGHSCORE_PATH + Math.abs(UUID.randomUUID().getMostSignificantBits()) + ".ser");
		saveFile(file, highscore);
	}

	/**
	 * deletes given game file
	 * 
	 * @param file game file to delete
	 * @throws IOException thrown if file not found
	 */
	void deleteGame(File file) {
		file.delete();
	}

	/**
	 * deletes all highscores in HIGHSCORE_PATH
	 * 
	 * @throws IOException thrown if HIGHSCORE_PATH not found
	 */
	void deleteHighscores() {
		File highscores = new File(HIGHSCORE_PATH);
		for (File file : highscores.listFiles()) {
			if (!file.isDirectory()) {
				file.delete();
			}
		}
	}

	private String serializeGameToFileName() {
		GameState currentGameState = splendorController.getSplendor().getGame().getCurrentGameState();
		String fileName = LocalDateTime.now().toString().replace(':', '-') + "-"
				+ currentGameState.getCurrentPlayer().getName() + "-" + currentGameState.getCurrentTrain();
		return fileName;
	}

	private void saveFile(File file, Object object) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(object);
			outStream.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private <T> T loadFile(File file) {
		T object = null;
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			object = (T) inStream.readObject();
			inStream.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * Loads the default csv files and parses them to cards.
	 * 
	 * @return Tuple of development cards, noble cards.
	 */
	public Tuple<List<Card>, List<Card>> getDefaultCards() {
		List<Card> devCards = parseFile(new File(CARD_PATH), false);
		List<Card> nobleCards = parseFile(new File(NOBLE_CARD_PATH), true);
		return new Tuple<List<Card>, List<Card>>(devCards, nobleCards);
	}

	private List<Card> parseFile(File file, boolean isNobleFile) {
		String line = "";
		ArrayList<Card> cardList = new ArrayList<Card>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			// Skip first line because its just the column names
			bufferedReader.readLine();
			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(",");
				Card card = parseToCard(values, isNobleFile);
				cardList.add(card);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cardList;
	}

	private Card parseToCard(String[] line, boolean isNoble) {
		int prestige = Integer.valueOf(line[6].trim());
		int level = isNoble ? 4 : Integer.valueOf(line[7].trim());
		int[] cardCosts = new int[6];
		cardCosts[GemType.DIAMOND.ordinal()] = Integer.valueOf(line[1].trim());
		cardCosts[GemType.SAPPHIRE.ordinal()] = Integer.valueOf(line[2].trim());
		cardCosts[GemType.EMERALD.ordinal()] = Integer.valueOf(line[3].trim());
		cardCosts[GemType.RUBY.ordinal()] = Integer.valueOf(line[4].trim());
		cardCosts[GemType.ONYX.ordinal()] = Integer.valueOf(line[5].trim());
		Gems cost = new Gems(cardCosts, GemAmountType.UNLIMITED);
		Card card = new Card(prestige, cost, isNoble, level);

		if (!isNoble) {
			String gemTypeStr = line[8].trim();
			card.setGemType(getGemTypeOf(gemTypeStr));
		}

		return card;
	}

	private GemType getGemTypeOf(String string) {
		switch (string) {
		case "diamant":
			return GemType.DIAMOND;
		case "saphir":
			return GemType.SAPPHIRE;
		case "smaragd":
			return GemType.EMERALD;
		case "rubin":
			return GemType.RUBY;
		case "onyx":
			return GemType.ONYX;
		}
		throw new IllegalArgumentException("String " + string + " cannot be parsed.");
	}

	public void closeGame() {
		System.out.println("Spiel wird geschlossen");
	}
}
