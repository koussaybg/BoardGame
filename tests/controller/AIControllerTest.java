package controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Card;
import model.Game;
import model.Player;
import model.PlayerType;
import model.Splendor;
import view.viewAUI.GameboardWindowAUI;

class AIControllerTest {
	private PlayerController playerController;
	private SplendorController splendorController;
	private Splendor splendor;
	private Player player;
	private NewGameController ngc;

	@Before
	void setUp() {
		splendor = new Splendor();
		splendorController = new SplendorController();
		splendorController.setSplendor(splendor);
		playerController = new PlayerController(splendorController, new GameboardWindowAUIImpl());
		/*
		 * ngc= new NewGameController() ; splendorController.setNewGameController(ngc);
		 */
		List<Player> playerList = new ArrayList<Player>();

		playerList.add(new Player(PlayerType.EASY, "AIeasy"));
		playerList.add(new Player(PlayerType.MEDIUM, "AImedium"));
		playerList.add(new Player(PlayerType.HARD, "AIhard"));
		playerList.add(new Player(PlayerType.HUMAN, "Test"));
		splendor.setGame(new Game(playerList, null, null));

	}

	@Test
	void showTip() {
	}

	@Test
	void makeTrain() {
	}

	class GameboardWindowAUIImpl implements GameboardWindowAUI{

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