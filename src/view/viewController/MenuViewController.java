package view.viewController;

import javafx.scene.Scene;

/**
 * Communicates with mainViewController, newGameWindowController,
 * loadGameWindowController, highscoreWindowController &
 * startWindowViewController.
 */
public class MenuViewController {

	private MainViewController mainViewController;
	private NewGameWindowController newGameWindowController;
	private LoadGameWindowController loadGameWindowController;
	private HighscoreWindowController highscoreWindowController;
	private StartWindowViewController startWindowViewController;
	
	private Scene startWindowScene;
	private Scene newGameWindowScene;
	private Scene loadGameWindowScene;
	private Scene highscoreWindowScene;
	/*
	@FXML
	private TabPane menuViewTabs;
	@FXML
	private Tab startWindowTab;
	@FXML
	private Tab newGameWindowTab;
	@FXML
	private Tab loadGameWindowTab;
	@FXML
	private Tab higscoresWindowTab;
*/

	public MenuViewController(){
		this.startWindowViewController = new StartWindowViewController();
		this.startWindowViewController.setMenuViewController(this);
		this.newGameWindowController = new NewGameWindowController();
		this.newGameWindowController.setMenuViewController(this);
		this.loadGameWindowController = new LoadGameWindowController();
		this.loadGameWindowController.setMenuViewController(this);
		this.highscoreWindowController = new HighscoreWindowController();
		this.highscoreWindowController.setMenuViewController(this);
	}

	public MainViewController getMainViewController()
	{
		return this.mainViewController;
	}

	public void setMainViewController(MainViewController mainViewController)
	{
		this.mainViewController = mainViewController;
	}


	public NewGameWindowController getNewGameWindowController()
	{
		return this.newGameWindowController;
	}

	public void setNewGameWindowController(NewGameWindowController newGameWindowController)
	{
		this.newGameWindowController = newGameWindowController;
	}


	public LoadGameWindowController getLoadGameWindowController()
	{
		return this.loadGameWindowController;
	}

	public void setLoadGameWindowController(LoadGameWindowController loadGameWindowController)
	{
		this.loadGameWindowController = loadGameWindowController;
	}


	public HighscoreWindowController getHighscoreWindowController()
	{
		return this.highscoreWindowController;
	}

	public void setHighscoreWindowController(HighscoreWindowController highscoreWindowController)
	{
		this.highscoreWindowController = highscoreWindowController;
	}


	public StartWindowViewController getStartWindowViewController()
	{
		return this.startWindowViewController;
	}

	public void setStartWindowViewController(StartWindowViewController startWindowViewController)
	{
		this.startWindowViewController = startWindowViewController;
	}

	public void setStartWindowScene(Scene startWindowScene) {
		this.startWindowScene = startWindowScene;
	}

	public Scene getStartWindowScene() {
		return this.startWindowScene;
	}

	public void setNewGameWindowScene(Scene newGameWindowScene) {
		this.newGameWindowScene = newGameWindowScene;
	}

	public Scene getNewGameWindowScene() {
		return this.newGameWindowScene;
	}

	public void setLoadGameWindowScene(Scene loadGameWindowScene) {
		this.loadGameWindowScene = loadGameWindowScene;
	}

	public Scene getLoadGameWindowScene() {
		return this.loadGameWindowScene;
	}

	public void setHighscoreWindowScene(Scene highscoreWindowScene) {
		this.highscoreWindowScene = highscoreWindowScene;
	}

	public Scene getHighscoreWindowScene() {
		return this.highscoreWindowScene;
	}
}
