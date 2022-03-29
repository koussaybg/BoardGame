package view.viewController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.NewGameController;
import controller.SplendorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Player;
import model.PlayerType;

public class NewGameWindowController {

	@FXML
	private Label newGameLabel;
	@FXML
	private Button loadCardsButton;
	@FXML
	private Button startWindowButton;
	@FXML
	private Button startGameButton;

	@FXML
	private Label playerOneLabel;
	@FXML
	private Label playerTwoLabel;
	@FXML
	private Label playerThreeLabel;
	@FXML
	private Label playerFourLabel;

	@FXML
	private TextField playerOneName;
	@FXML
	private TextField playerTwoName;
	@FXML
	private TextField playerThreeName;
	@FXML
	private TextField playerFourName;

	@FXML
	private ComboBox<String> playerOneTypeBox;
	@FXML
	private ComboBox<String> playerTwoTypeBox;
	@FXML
	private ComboBox<String> playerThreeTypeBox;
	@FXML
	private ComboBox<String> playerFourTypeBox;

	private ObservableList<String> playerTypes = FXCollections.observableArrayList();

	private File cardsLoaded;
	private File noblesLoaded;

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
		playerTypes.setAll("Lokaler Spieler", "Bot-Einfach", "Bot-Mittel", "Bot-Schwer");

		playerOneTypeBox.setItems(playerTypes);
		playerTwoTypeBox.setItems(playerTypes);
		playerThreeTypeBox.setItems(playerTypes);
		playerFourTypeBox.setItems(playerTypes);

		this.refreshSettings();

		playerOneName.textProperty().addListener( (options1, oldValue, newValue) -> {
			if(oldValue != newValue && newValue != null)
			{
				playerTwoLabel.setVisible(true);
				playerTwoName.setVisible(true);
				playerTwoTypeBox.setVisible(true);
			}
			else
			{
				playerTwoLabel.setVisible(false);
				playerTwoName.setVisible(false);
				playerTwoTypeBox.setVisible(false);

				playerTwoTypeBox.setValue(playerTypes.get(0));

				playerThreeLabel.setVisible(false);
				playerThreeName.setVisible(false);
				playerThreeTypeBox.setVisible(false);

				playerThreeTypeBox.setValue(playerTypes.get(0));

				playerFourLabel.setVisible(false);
				playerFourName.setVisible(false);
				playerFourTypeBox.setVisible(false);
				
				playerFourTypeBox.setValue(playerTypes.get(0));
			}
		});

		playerTwoName.textProperty().addListener( (options2, oldValue, newValue) -> {
			if(oldValue != newValue && newValue != null)
			{
				playerThreeLabel.setVisible(true);
				playerThreeName.setVisible(true);
				playerThreeTypeBox.setVisible(true);
			}
			else
			{
				playerThreeLabel.setVisible(false);
				playerThreeName.setVisible(false);
				playerThreeTypeBox.setVisible(false);

				playerThreeTypeBox.setValue(playerTypes.get(0));

				playerFourLabel.setVisible(false);
				playerFourName.setVisible(false);
				playerFourTypeBox.setVisible(false);
				
				playerFourTypeBox.setValue(playerTypes.get(0));
			}
		});

		playerThreeName.textProperty().addListener( (options3, oldValue, newValue) -> {
			if(newValue != null && oldValue != newValue)
			{
				playerFourLabel.setVisible(true);
				playerFourName.setVisible(true);
				playerFourTypeBox.setVisible(true);
			}
			else
			{
				playerFourLabel.setVisible(false);
				playerFourName.setVisible(false);
				playerFourTypeBox.setVisible(false);
				
				playerFourTypeBox.setValue(playerTypes.get(0));
			}
		});
	}

	/**
	 * loads the cards from a given csv-file
	 */
	public void onLoadCardsButtonClicked() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Entwicklungskarten wählen");
		fileChooser.setInitialFileName("Entwicklungskarten.csv");
		cardsLoaded = fileChooser.showOpenDialog(null);
		if(cardsLoaded == null)
		{
			return;
		}
		
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(cardsLoaded.getParent()));
		fileChooser.setTitle("Adligen-Karten wählen");
		fileChooser.setInitialFileName("AdligenKarten.csv");
		noblesLoaded = fileChooser.showOpenDialog(null);
	}

	/**
	 * starts a new Game with choosen parameters on the view
	 */
	public void onStartGameButtonClicked() {
		MainViewController mvc = menuViewController.getMainViewController();
		SplendorController splendorController = mvc.getSplendorController();
		NewGameController newGameController = splendorController.getNewGameController();

		List<Player> allPlayer = new ArrayList<Player>();
		List<String> allPlayerNames = new ArrayList<String>();
		List<String> allPlayerBoxes = new ArrayList<String>();

		allPlayerNames.add(playerOneName.getText().trim());
		allPlayerNames.add(playerTwoName.getText().trim());
		allPlayerNames.add(playerThreeName.getText().trim());
		allPlayerNames.add(playerFourName.getText().trim());

		List<String> evaluatePlayerNames = new ArrayList<String>();
		for (int i = 0; i < allPlayerNames.size(); i++) {
			if(!allPlayerNames.get(i).isEmpty()) {
				if(allPlayerNames.indexOf(allPlayerNames.get(i)) == allPlayerNames.lastIndexOf(allPlayerNames.get(i)))
					evaluatePlayerNames.add(allPlayerNames.get(i));
				else {
					mvc.showError("Spieler müssen eindeutige Namen besitzen.");
					return;
				}
			}
		}
		allPlayerNames = evaluatePlayerNames;

		if(allPlayerNames.size() < 2) {
			mvc.showError("Es müssen mindestens zwei Spieler mitspielen! (Macht sonst ja auch keinen Spaß)");
			return; }

		allPlayerBoxes.add(playerOneTypeBox.getSelectionModel().getSelectedItem());
		allPlayerBoxes.add(playerTwoTypeBox.getSelectionModel().getSelectedItem());
		allPlayerBoxes.add(playerThreeTypeBox.getSelectionModel().getSelectedItem());
		allPlayerBoxes.add(playerFourTypeBox.getSelectionModel().getSelectedItem());

		for (int i = 0; i < allPlayerNames.size(); i++) {
			Player player = null;
			switch (allPlayerBoxes.get(i)) {
				case "Lokaler Spieler":
					player = new Player(PlayerType.HUMAN, allPlayerNames.get(i));
					break;

				case "Bot-Einfach":
					player = new Player(PlayerType.EASY, allPlayerNames.get(i));
					break;

				case "Bot-Mittel":
					player = new Player(PlayerType.MEDIUM, allPlayerNames.get(i));
					break;

				case "Bot-Schwer":
					player = new Player(PlayerType.HARD, allPlayerNames.get(i));
					break;
			}
			allPlayer.add(player);
		}

		if(cardsLoaded == null && noblesLoaded == null)
		{
			newGameController.startNewGame(allPlayer);
		}
		else
		{
			if(!(cardsLoaded == null) && !(noblesLoaded == null))
				newGameController.startNewGame(allPlayer, cardsLoaded, noblesLoaded);
			else {
				mvc.showError("Bei dem Laden einer Kartendatei ist etwas schiefgegangen.");
				return; }
		}

		this.refreshSettings();
		menuViewController.getMainViewController().enterGameStage();
	}

	/**
	 * sends user back to start window
	 */
	 public void onStartWindowButtonClicked() {
		this.refreshSettings();
		Stage primaryStage = (Stage) startWindowButton.getScene().getWindow();
		primaryStage.setScene(this.menuViewController.getStartWindowScene());
	 }

	 /**
	  * Refreshs visibility of player2/player3/player4
	  * Clears Playernames and sets Playertypes to local-Player
	  */
	 public void refreshSettings()
	 {
		cardsLoaded = null;
		noblesLoaded = null;
		
		playerOneTypeBox.setValue(playerTypes.get(0));
		playerTwoTypeBox.setValue(playerTypes.get(0));
		playerThreeTypeBox.setValue(playerTypes.get(0));
		playerFourTypeBox.setValue(playerTypes.get(0));

		playerOneName.clear();
		playerTwoName.clear();
		playerThreeName.clear();
		playerFourName.clear();

		playerTwoLabel.setVisible(false);
		playerThreeLabel.setVisible(false);
		playerFourLabel.setVisible(false);

		playerTwoTypeBox.setVisible(false);
		playerThreeTypeBox.setVisible(false);
		playerFourTypeBox.setVisible(false);

		playerTwoName.setVisible(false);
		playerThreeName.setVisible(false);
		playerFourName.setVisible(false);
	 }
}
