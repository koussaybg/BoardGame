package controller;

import model.Splendor;

public class SplendorController {

	private LoadGameController loadGameController;

	private HighscoreController highscoreController;

	private NewGameController newGameController;

	private GameboardController gameboardController;

	private IOController ioController;

	private AIController aiController;

	private Splendor splendor;

	private PlayerController playerController;

	/**
	 * default constructor creates model
	 */
	public SplendorController() {
		this.splendor = new Splendor();
	}

	public LoadGameController getLoadGameController() {
		return loadGameController;
	}

	public void setLoadGameController(LoadGameController loadGameController) {
		this.loadGameController = loadGameController;
	}

	public HighscoreController getHighscoreController() {
		return highscoreController;
	}

	public void setHighscoreController(HighscoreController highscoreController) {
		this.highscoreController = highscoreController;
	}

	public NewGameController getNewGameController() {
		return newGameController;
	}

	public void setNewGameController(NewGameController newGameController) {
		this.newGameController = newGameController;
	}

	public GameboardController getGameboardController() {
		return gameboardController;
	}

	public void setGameboardController(GameboardController gameboardController) {
		this.gameboardController = gameboardController;
	}

	public IOController getIOController() {
		return ioController;
	}

	public void setIOController(IOController iOController) {
		ioController = iOController;
	}

	public AIController getAIController() {
		return aiController;
	}

	public void setAIController(AIController aIController) {
		aiController = aIController;
	}

	public Splendor getSplendor() {
		return splendor;
	}

	public void setSplendor(Splendor splendor) {
		this.splendor = splendor;
	}

	public PlayerController getPlayerController() {
		return playerController;
	}

	public void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}
}
