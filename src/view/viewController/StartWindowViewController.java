package view.viewController;

import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Controller for the start window.
 * 
 * @author sopr050
 *
 */


public class StartWindowViewController {

	@FXML
	private Button newGameButton;
	@FXML
	private Button loadGameButton;
	@FXML
	private Button highscoreButton;
	@FXML
	private Button closeGameButton;

	/**
	 * Must be provided to be able to communicate with the controller layer.
	 */
	private MenuViewController menuViewController;
	
	public void setMenuViewController(MenuViewController menuViewController){
		this.menuViewController = menuViewController;
	}

	public MenuViewController getMenuViewController() {
		return this.menuViewController;
	}

	/**
	 * Opens window for new game setup.
	 * 
	 * @param action click on onNewGameButton ??
	 */

	public void onNewGameButtonClicked() {
		Stage primaryStage = (Stage) newGameButton.getScene().getWindow();
		primaryStage.setScene(this.menuViewController.getNewGameWindowScene());
	}
	
	/**
	 * Opens window for old game loading.
	 * 
	 * @param action click on onLoadButton ??
	 */

	public void onLoadGameButtonClicked() {
		Stage primaryStage = (Stage) newGameButton.getScene().getWindow();
		primaryStage.setScene(this.menuViewController.getLoadGameWindowScene());

		this.menuViewController.getMainViewController().getSplendorController().getLoadGameController().loadSavedGames();
	}

	/**
	 * Opens highscores window.
	 * 
	 * @param action click on onNewGameButton ??
	 */
	public void onHighscoreButtonClicked() {
		Stage primaryStage = (Stage) newGameButton.getScene().getWindow();
		primaryStage.setScene(this.menuViewController.getHighscoreWindowScene());
		
		this.menuViewController.getMainViewController().getSplendorController().getHighscoreController().loadHighscores();
		this.menuViewController.getHighscoreWindowController().refreshHighscores();
	}

	public void onCloseGameButtonClicked(){
		Stage primaryStage = (Stage) newGameButton.getScene().getWindow();
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Spiel beenden");
		alert.setContentText("Möchten Sie das Spiel beenden");
		ButtonType closeButton = new ButtonType("Beenden", ButtonBar.ButtonData.APPLY);
		ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(closeButton, cancelButton);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == closeButton){
			System.out.println("close");
			this.menuViewController.getMainViewController().getSplendorController().getIOController().closeGame();
			primaryStage.close();
			Platform.exit();
		}else{
			System.out.println("cancel");
			//Do nothing
		}
		
	}
}
