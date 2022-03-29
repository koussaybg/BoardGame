package view.viewController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.LoadGameController;
import controller.SplendorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Tuple;
import view.viewAUI.LoadGameWindowAUI;

/**
 * View controller for load game window.
 */
public class LoadGameWindowController implements LoadGameWindowAUI {


	@FXML
	private ListView<String> loadGameList;
	@FXML
	private Button startWindowButton;
	@FXML
	private Button startGameButton;

	private MenuViewController menuViewController;
	private List<Tuple<File,String>> gameFilesAndNames;

	public void setMenuViewController(MenuViewController menuViewController){
		this.menuViewController = menuViewController;
	}
	
	public MenuViewController getMenuViewController() {
		return this.menuViewController;
	}

	@FXML
	public void initialize()
	{
		this.loadGameList.setPlaceholder(new Label("Es existieren keine gespeicherten Spiele."));
	}

	/**
	 * called if startGameButton clicked.
	 * loads selected game and starts it
	 */
	public void onStartGameButtonClicked(ActionEvent action) {
		MainViewController mvc = menuViewController.getMainViewController();
		SplendorController splendorController = mvc.getSplendorController();
		LoadGameController loadGameController = splendorController.getLoadGameController();

		String gameName = loadGameList.getSelectionModel().getSelectedItem();
		
		if(gameName != null)
		{
			for (Tuple<File,String> current : gameFilesAndNames) {
				if(current.getSecondValue() == gameName)
				{
					loadGameController.startSavedGame(current.getFirstValue());
				}
			}
		}
	}
	
	/**
	 * sends user back to start window
	 */
	public void onStartWindowButtonClicked() {
		Stage primaryStage = (Stage) startWindowButton.getScene().getWindow();
		primaryStage.setScene(this.menuViewController.getStartWindowScene());
	}

	/**
	 * called if delete game is called from context menu
	 * @param action action event passed by view
	 */
	public void onDeleteGameButtonClicked(ActionEvent action) {
		MainViewController mvc = menuViewController.getMainViewController();
		SplendorController splendorController = mvc.getSplendorController();
		LoadGameController loadGameController = splendorController.getLoadGameController();

		if (mvc.showValidation("Soll das ausgewählte Spiel wirklich gelöscht werden?") == ButtonType.YES)
		{
			String gameName = loadGameList.getSelectionModel().getSelectedItem();
	
			if(gameName != null)
			{
				for (Tuple<File,String> current : gameFilesAndNames)
				{
					if(current.getSecondValue() == gameName)
					{
						loadGameController.deleteGame(current.getFirstValue());
						loadGameController.loadSavedGames();
					}
				}
			}
		}

	}

	/**
	 * called by controller to refresh window
	 * @see LoadGameWindowAUI#showGameList(List<Tuple<File,String>> games)
	 * 
	 *  
	 */
	public void showGameList(List<Tuple<File,String>> games)
	{
		gameFilesAndNames = games;

		ObservableList<String> displayedGames = FXCollections.observableList(new ArrayList<String>());

		for (Tuple<File,String> current : games) {
			displayedGames.add(current.getSecondValue());
		}

		loadGameList.setItems(displayedGames);
	}

	/**
	 * Is called by the LoadGameController to enter the gameStage for the loaded Game
	 * @see LoadGameWindowAUI#enterGame()
	 */
	public void enterGame()
	{
		menuViewController.getMainViewController().enterGameStage();
	}
}
