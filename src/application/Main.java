package application;


import controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.viewController.*;

import java.io.File;


public class Main extends Application{
	
	private MainViewController mainViewController;
	private Scene startWindowScene;
	private Scene newGameWindowScene;
	private Scene loadGameWindowScene;
	private Scene highscoreWindowScene;
	
	private MenuViewController menuViewController;
	private SplendorController splendorController;
	
	@Override
	public void start(Stage primary) {

		Platform.setImplicitExit(false);
		Stage primaryStage = primary;
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		
		try{
			//controller setup
			this.mainViewController = new MainViewController();
			this.menuViewController = mainViewController.getMenuViewController();
			this.mainViewController.setStage(primaryStage);
			
			//start window setup
			FXMLLoader startWindowLoader = new FXMLLoader(getClass().getResource("/views/StartWindow.fxml"));
			startWindowLoader.setController(this.menuViewController.getStartWindowViewController());
			AnchorPane startPane = startWindowLoader.load();
			startPane.setId("startPane");
			this.startWindowScene = new Scene(startPane);
			startWindowScene.getStylesheets().add(getClass().getResource("/styles/menu.css").toExternalForm());
			menuViewController.setStartWindowScene(this.startWindowScene);

			//new game window setup
			FXMLLoader newGameWindowLoader = new FXMLLoader(getClass().getResource("/views/NewGameWindow.fxml"));
			newGameWindowLoader.setController(this.menuViewController.getNewGameWindowController());
			AnchorPane newGamePane = newGameWindowLoader.load();
			newGamePane.setId("newGamePane");
			this.newGameWindowScene = new Scene(newGamePane);
			newGameWindowScene.getStylesheets().add(getClass().getResource("/styles/menu.css").toExternalForm());
			menuViewController.setNewGameWindowScene(this.newGameWindowScene);

			//load game window setup
			FXMLLoader loadGameWindowLoader = new FXMLLoader(getClass().getResource("/views/LoadGameWindow.fxml"));
			loadGameWindowLoader.setController(this.menuViewController.getLoadGameWindowController());
			AnchorPane loadGamePane = loadGameWindowLoader.load();
			loadGamePane.setId("loadGamePane");
			this.loadGameWindowScene = new Scene(loadGamePane);
			loadGameWindowScene.getStylesheets().add(getClass().getResource("/styles/menu.css").toExternalForm());
			menuViewController.setLoadGameWindowScene(this.loadGameWindowScene);
			
			//highscore window setup
			FXMLLoader highscoreWindowLoader = new FXMLLoader(getClass().getResource("/views/HighscoreWindow.fxml"));
			highscoreWindowLoader.setController(this.menuViewController.getHighscoreWindowController());
			AnchorPane highscorePane = highscoreWindowLoader.load();
			highscorePane.setId("highscorePane");
			this.highscoreWindowScene = new Scene(highscorePane);
			highscoreWindowScene.getStylesheets().add(getClass().getResource("/styles/menu.css").toExternalForm());
			menuViewController.setHighscoreWindowScene(this.highscoreWindowScene);

			//gameboard window setup
			GameboardViewController gameBoardViewController = new GameboardViewController();
			mainViewController.setGameBoardViewController(gameBoardViewController);
			gameBoardViewController.setMainViewController(this.mainViewController);
			
			
			//initialize Controller
			this.splendorController = new SplendorController();
			this.mainViewController.setSplendorController(splendorController);

			this.splendorController.setAIController(new AIController(this.splendorController, gameBoardViewController));
			this.splendorController.setGameboardController(new GameboardController(this.splendorController, gameBoardViewController));
			this.splendorController.setHighscoreController(new HighscoreController(this.splendorController, this.menuViewController.getHighscoreWindowController()));
			this.splendorController.setIOController(new IOController(this.splendorController));
			this.splendorController.setLoadGameController(new LoadGameController(this.splendorController, this.menuViewController.getLoadGameWindowController()));
			this.splendorController.setNewGameController(new NewGameController(this.splendorController));
			this.splendorController.setPlayerController(new PlayerController(this.splendorController, gameBoardViewController));
			
		mainViewController.openStartWindow();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
