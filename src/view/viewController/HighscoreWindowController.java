package view.viewController;

import java.util.Collections;

import controller.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Highscore;
import model.Splendor;
import view.viewAUI.HighscoreWindowAUI;

public class HighscoreWindowController implements HighscoreWindowAUI {

	@FXML
	private TableView<Highscore> highscoreList;
	@FXML
	private TableColumn<Highscore, String> nameColumn;
	@FXML
	private TableColumn<Highscore, Integer> scoreColumn;

	@FXML
	private Button startWindowButton;
	@FXML
	private Button resetHighscoresButton;

	private MenuViewController menuViewController;


	public void setMenuViewController(MenuViewController menuViewController){
		this.menuViewController = menuViewController;
	}
	
	public MenuViewController getMenuViewController() {
		return this.menuViewController;
	}

	@FXML
	public void initialize()
	{
		highscoreList.setPlaceholder(new Label("Es existieren noch keine Highscores."));

		nameColumn.setCellValueFactory(new PropertyValueFactory<Highscore, String>("name"));
		scoreColumn.setCellValueFactory(new PropertyValueFactory<Highscore, Integer>("score"));

		nameColumn.setSortable(false);
		scoreColumn.setSortable(false);
	}

	/**
	 * deletes all highscores
	 * 
	 */
	public void onResetHighscoresButtonClicked() {
		MainViewController mvc = menuViewController.getMainViewController();
		SplendorController spc = mvc.getSplendorController();
		HighscoreController hsc = spc.getHighscoreController();

		if (mvc.showValidation("Alle Highscores wirklich unwiderruflich löschen?") == ButtonType.YES) {
			hsc.resetHighscores();
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
	 * refresh highscoreview (e.g. after resetting highscores)
	 * 
	 * @see Model.View.HighscoreWindowAUI#refreshHighscores()
	 * 
	 * 
	 */
	public void refreshHighscores() {
		MainViewController mvc = menuViewController.getMainViewController();
		SplendorController spc = mvc.getSplendorController();
		Splendor spl = spc.getSplendor();

		ObservableList<Highscore> displayedHighscores = FXCollections.observableArrayList();
		displayedHighscores.addAll(spl.getHighscores());
		Collections.sort(displayedHighscores, (o1, o2) -> o1.getScore()<o2.getScore() ? 1 : o1.getScore()>o2.getScore() ? -1 : 0);
		highscoreList.getItems().setAll(displayedHighscores);
	}
}
