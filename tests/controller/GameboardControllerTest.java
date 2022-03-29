package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Card;
import model.Game;
import model.GameState;
import model.GemAmountType;
import model.GemType;
import model.Gems;
import model.Player;
import model.PlayerType;
import model.Splendor;
import view.viewAUI.GameboardWindowAUI;

public class GameboardControllerTest {

	private SplendorController splendorController;
	private GameboardController gameboardController;
	private NewGameController newGameController;
	private AIControllerOverwrite aiController;
	private Splendor splendor;
	private Game game;
	private GameboardWindowAUIImpl gameboardWindowAUI;

	@Before
	public void setup() {

		splendor = new Splendor();

		splendorController = new SplendorController();
		splendorController.setSplendor(splendor);
		gameboardWindowAUI = new GameboardWindowAUIImpl();
		gameboardController = new GameboardController(splendorController, gameboardWindowAUI);
		gameboardController.setGameboardWindowAUI(gameboardWindowAUI);
		newGameController = new NewGameController(splendorController);
		newGameController.setSplendorController(splendorController);
		aiController = new AIControllerOverwrite(splendorController);
		splendorController.setAIController(aiController);
		splendorController.setIOController(new IOController(splendorController));

		List<Player> playerList = Arrays.asList(new Player(PlayerType.HUMAN, "firstPlayer"),
				new Player(PlayerType.HUMAN, "secondPlayer"));
		newGameController.startNewGame(playerList);

		game = splendor.getGame();
	}

	@Test
	public void testUndo() {
		GameState firstState = game.getCurrentGameState();

		game.cloneGameState();
		gameboardController.undo();

		assertTrue(game.getCurrentGameState().equals(firstState));
		assertTrue(gameboardWindowAUI.gameStateRefreshed);
		assertTrue(gameboardWindowAUI.redoActivated);
		assertFalse(game.isHighscoreable());
	}

	@Test
	public void testUndoFirst() {
		GameState firstState = game.getCurrentGameState();

		gameboardController.undo();

		assertTrue(game.getCurrentGameState().equals(firstState));
		assertFalse(gameboardWindowAUI.gameStateRefreshed);
		assertFalse(gameboardWindowAUI.redoActivated);
	}

	@Test
	public void testRedo() {
		game.cloneGameState();
		GameState lastState = game.getCurrentGameState();
		gameboardController.undo();
		gameboardController.redo();

		assertTrue(game.getCurrentGameState().equals(lastState));
		assertTrue(gameboardWindowAUI.gameStateRefreshed);
		assertFalse(gameboardWindowAUI.redoActivated);
		assertTrue(gameboardWindowAUI.undoActivated);
	}

	@Test
	public void testRedoLast() {
		game.cloneGameState();
		GameState lastState = game.getCurrentGameState();
		gameboardController.redo();

		assertTrue(game.getCurrentGameState().equals(lastState));
		assertFalse(gameboardWindowAUI.gameStateRefreshed);
		assertFalse(gameboardWindowAUI.redoActivated);
	}

	@Test
	public void testResume() {
		game.cloneGameState();
		gameboardController.undo();
		GameState currentState = game.getCurrentGameState();
		GameState oldSuccessingState = currentState.getSuccessor();

		assertTrue(oldSuccessingState != null);

		gameboardController.resume();

		assertFalse(oldSuccessingState.equals(currentState.getSuccessor()));
		assertTrue(oldSuccessingState.getPredecessor() == null);
	}

	@Test
	public void testNextTrain() {
		List<Player> playerList = Arrays.asList(new Player(PlayerType.HUMAN, "firstPlayer"),
				new Player(PlayerType.EASY, "aiPlayer"));
		newGameController.startNewGame(playerList);
		game = splendor.getGame();
		
		GameState stateBefore = game.getCurrentGameState();
		gameboardController.nextTrain();
		assertFalse(stateBefore.equals(game.getCurrentGameState()));
		assertTrue(gameboardWindowAUI.gameStateRefreshed);
		assertTrue(gameboardWindowAUI.currentPlayerRefreshed);
		assertTrue(gameboardWindowAUI.undoActivated);
		assertTrue(aiController.makeTrainCalled);
	}

	@Test
	public void testCheckNobles() {
		Card buyableNobleCard = new Card(3, new Gems(new int[] { 1, 1, 0, 0, 0, 0 }, GemAmountType.UNLIMITED), true,4);
		Card notBuyableNobleCard = new Card(3, new Gems(new int[] { 10, 10, 10, 10, 10, 10 }, GemAmountType.UNLIMITED),
				true,4);

		Card diamondCard = new Card(1, new Gems(new int[] { 1, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED), false,1);
		diamondCard.setGemType(GemType.DIAMOND);
		Card emeraldCard = new Card(1, new Gems(new int[] { 1, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED), false,1);
		emeraldCard.setGemType(GemType.EMERALD);

		GameState gameState = game.getCurrentGameState();
		List<Card> cards = new LinkedList<>();
		cards.add(diamondCard);
		cards.add(emeraldCard);
		gameState.getCurrentPlayer().setBoughtCards(cards);
		List<Card> nobleList = new ArrayList<Card>();
		nobleList.add(buyableNobleCard);
		nobleList.add(notBuyableNobleCard);
		gameState.setNobleCards(nobleList);

		gameboardController.checkNobles();

		assertTrue(gameboardWindowAUI.noblesShown);
		assertFalse(gameboardWindowAUI.gameStateRefreshed);
	}

	@Test
	public void testCheckNoblesNoNoblesAvailable() {
		Card notBuyableNobleCard = new Card(3, new Gems(new int[] { 10, 10, 10, 10, 10, 10 }, GemAmountType.UNLIMITED),
				true, 4);

		Card diamondCard = new Card(1, new Gems(new int[] { 1, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED), false,1);
		diamondCard.setGemType(GemType.DIAMOND);
		Card emeraldCard = new Card(1, new Gems(new int[] { 1, 0, 0, 0, 0, 0 }, GemAmountType.UNLIMITED), false,1);
		emeraldCard.setGemType(GemType.EMERALD);

		GameState gameState = game.getCurrentGameState();
		List<Card> cards = new LinkedList<>();
		cards.add(diamondCard);
		cards.add(emeraldCard);
		gameState.getCurrentPlayer().setBoughtCards(cards);
		gameState.setNobleCards(Arrays.asList(notBuyableNobleCard ));

		gameboardController.checkNobles();

		assertFalse(gameboardWindowAUI.noblesShown);
		assertTrue(gameboardWindowAUI.gameStateRefreshed);
	}

	class AIControllerOverwrite extends AIController {
		public AIControllerOverwrite(SplendorController splendorController) {
			super(splendorController, gameboardWindowAUI);
		}

		public boolean makeTrainCalled;

		@Override
		void makeTrain() {
			makeTrainCalled = true;
		}
	}

	class GameboardWindowAUIImpl implements GameboardWindowAUI {

		public boolean noblesShown;
		public boolean handRefreshed;
		public boolean gameStateRefreshed;
		public boolean playersHandRefreshed;
		public boolean currentPlayerRefreshed;
		public boolean undoActivated;
		public boolean redoActivated;

		public void setAllFalse() {
			noblesShown = false;
			handRefreshed = false;
			gameStateRefreshed = false;
			playersHandRefreshed = false;
			currentPlayerRefreshed = false;
			undoActivated = false;
			redoActivated = false;
		}

		@Override
		public void refreshPlayersHand() {
			playersHandRefreshed = true;
		}

		@Override
		public void refreshCurrentPlayer() {
			currentPlayerRefreshed = true;
		}

		@Override
		public void refreshCards() {
		}

		@Override
		public void refreshGems() {
		}

		@Override
		public void refreshNobles() {
			noblesShown = true;
		}

		@Override
		public void activateUndo(boolean active) {
			undoActivated = active;
		}

		@Override
		public void activateRedo(boolean active) {
			redoActivated = active;
		}

		@Override
		public void refreshGameState() {
			gameStateRefreshed = true;
		}

		@Override
		public void showTip(Card card, boolean buy) {
		}

		@Override
		public void gameOver(Player winner) {
		}

		@Override
		public void showNobles(List<Card> nobles) {
			noblesShown = true;
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
