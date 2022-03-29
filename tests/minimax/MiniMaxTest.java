package minimax;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import controller.IOController;
import controller.NewGameController;
import controller.SplendorController;
import model.Card;
import model.Game;
import model.GameState;
import model.GemAmountType;
import model.GemType;
import model.Gems;
import model.Player;
import model.PlayerType;

//@Ignore
public class MiniMaxTest {

	private GameState gameState;
	private HashMap<GameState, Integer> transpositionTable;

	@Before
	public void setUp() {
		SplendorController splendorController = new SplendorController();
		NewGameController newGameController = new NewGameController(splendorController);
		IOController ioController = new IOController(splendorController);
		transpositionTable = new HashMap<GameState, Integer>();
		splendorController.setIOController(ioController);
		List<Player> playerList = Arrays.asList(new Player(PlayerType.HARD, "player 1"),
				new Player(PlayerType.HARD, "player 2"));
		newGameController.startNewGame(playerList);

		gameState = splendorController.getSplendor().getGame().getCurrentGameState();
	}

	@Test
	public void testGame() {

		int counter = 0;
		while (!gameState.isGameOver()) {
			long timer = System.currentTimeMillis();

			System.out.println("round " + counter++ + " : " + gameState.getCurrentTrain());

			MiniMaxAI miniMaxAi = new MiniMaxAI(gameState, 5, transpositionTable);
			Move move = miniMaxAi.getBestMove();

			if (move == null) {
				System.err.println("no clue");
			}

			move.apply(gameState);

			System.out.println(gameState.getCurrentPlayer().getName() + " : " + move.toString());
			GameState newState = gameState.clone();
			newState.setCurrentTrain(newState.getCurrentTrain() + 1);
			gameState = newState;

			System.err.println("time : " + (System.currentTimeMillis() - timer) / 1000);
		}
		assertTrue(gameState.isGameOver());
	}

	@Test
	public void simple() {
		List<Player> playerList = Arrays.asList(new Player(PlayerType.HARD, "player 1"),
				new Player(PlayerType.HARD, "player 2"));
		Card card = new Card(10, new Gems(new int[] { 1, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED), false, 1);
		card.setGemType(GemType.DIAMOND);
		Game game = new Game(playerList, Collections.singletonList(card), Collections.emptyList());
		game.getCurrentGameState().getGems().getGems()[1]--;
		playerList.get(0).setGems(new Gems(new int[] { 1, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED));

		MiniMaxAI miniMaxAi = new MiniMaxAI(game.getCurrentGameState(), 3, transpositionTable);
		Move move = miniMaxAi.getBestMove();
		System.out.println(move);

		move.apply(game.getCurrentGameState());
		assertTrue(game.getCurrentGameState().getCurrentPlayer().getBoughtCards().contains(card));

	}

	@Test
	public void getGemsThenBuyCardToWin() {
		List<Player> playerList = Arrays.asList(new Player(PlayerType.HARD, "player 1"),
				new Player(PlayerType.HARD, "player 2"));
		Card card = new Card(15, new Gems(new int[] { 0, 0, 1, 0, 3, 0 }, GemAmountType.UNLIMITED), false, 1);
		card.setGemType(GemType.DIAMOND);
		Game game = new Game(playerList, Collections.singletonList(card), Collections.emptyList());
		game.getCurrentGameState().setGems(new Gems(GemAmountType.GAMEBOARD_TWO_PLAYERS));
		game.getCurrentGameState().getCurrentPlayer()
				.setGems(new Gems(new int[] { 0, 0, 0, 0, 0, 0 }, GemAmountType.PLAYER_HAND));
		gameState = game.getCurrentGameState();
		gameState.reserveCard(card);
		gameState.setCurrentTrain(gameState.getCurrentTrain() + 1);
		gameState.takeGems(new Gems(new int[] { 0, 0, 0, 0, 2, 0 }, GemAmountType.UNLIMITED));
		gameState.setCurrentTrain(gameState.getCurrentTrain() + 1);

		MiniMaxAI miniMaxAi;
		Move move;
		for (int i = 0; i < 5 && !gameState.isGameOver(); i++) {
			Player currentPlayer = gameState.getCurrentPlayer();
			miniMaxAi = new MiniMaxAI(gameState, 5, transpositionTable);
			move = miniMaxAi.getBestMove();
			if (move == null) {
				break;
			}
			move.apply(gameState);
			System.out.println(currentPlayer.getName() + " :");
			System.out.println(move.toString());
			System.out.println(currentPlayer.getGems().toString());
			System.out.println(gameState.getGems().toString());
			gameState.setCurrentTrain(gameState.getCurrentTrain() + 1);

		}

		assertTrue(game.getCurrentGameState().getPlayers().get(0).getBoughtCards().contains(card));
	}

	@Test
	public void reserveThenGetGemsThenBuyCardToWin() {
		List<Player> playerList = Arrays.asList(new Player(PlayerType.HARD, "player 1"),
				new Player(PlayerType.HARD, "player 2"));
		Card card = new Card(15, new Gems(new int[] { 0, 0, 1, 0, 3, 0 }, GemAmountType.UNLIMITED), false, 1);
		card.setGemType(GemType.DIAMOND);
		Game game = new Game(playerList, Collections.singletonList(card), Collections.emptyList());
		game.getCurrentGameState().setGems(new Gems(GemAmountType.GAMEBOARD_TWO_PLAYERS));
		game.getCurrentGameState().getCurrentPlayer()
				.setGems(new Gems(new int[] { 0, 0, 0, 0, 0, 0 }, GemAmountType.PLAYER_HAND));
		gameState = game.getCurrentGameState();

		MiniMaxAI miniMaxAi;
		Move move;
		for (int i = 0; i < 10 && !gameState.isGameOver(); i++) {
			Player currentPlayer = gameState.getCurrentPlayer();
			miniMaxAi = new MiniMaxAI(gameState, 5, transpositionTable);
			move = miniMaxAi.getBestMove();
			System.out.println(currentPlayer.getName() + " " + move.toString());
			move.apply(gameState);
			gameState.setCurrentTrain(gameState.getCurrentTrain() + 1);

		}
		assertTrue(game.getCurrentGameState().getPlayers().get(0).getBoughtCards().contains(card));
	}
}
