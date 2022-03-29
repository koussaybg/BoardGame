package view.viewController;

import java.io.File;

import controller.SplendorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Communicates with MenuViewController, GameBoardViewController & SplendorController.
 */
public class MainViewController {

	private MenuViewController menuViewController;
	private GameboardViewController gameboardViewController;
	private SplendorController splendorController;
	private StackPane rootPane;
	private Pane gameBoardPane;

	Stage primaryStage;

	public MainViewController(){
		this.menuViewController = new MenuViewController();
		this.menuViewController.setMainViewController(this);
	}
	public MenuViewController getMenuViewController()
	{
		return this.menuViewController;
	}

	public void enterGameStage()
	{
		try {
			FXMLLoader gameBoardLoader = new FXMLLoader(getClass().getResource("/views/gameBoard.fxml"));
			gameBoardLoader.setController(gameboardViewController);
			this.gameBoardPane = gameBoardLoader.load();
			this.rootPane = new StackPane(gameBoardPane);
			primaryStage.setScene(new Scene(rootPane));
			primaryStage.setWidth(1600);
			primaryStage.setHeight(900);
			primaryStage.show();
		} catch (Exception e) {
			//TODO: handle exception
		}
	}

	public void openStartWindow() {
		playSound();
		primaryStage.setScene(this.menuViewController.getStartWindowScene());
		primaryStage.show();
	}

	private void playSound() {
		try {
			Media menuMusic = new Media(new File("resources/sounds/menuMusic.wav").toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(menuMusic);
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.play();
		} catch (Exception e) {
			//TODO: handle exception
		}	
	}

	/**
	 * Shows an Error-Dialog with given Message
	*/
	public void showError(String errorMessage)
	{			
		Alert alert = new Alert(Alert.AlertType.ERROR);

		alert.setHeaderText("Es ist ein Fehler aufgetreten.");
		alert.setContentText(errorMessage);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
	}

	public ButtonType showValidation(String validationMessage)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, validationMessage, ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		return alert.getResult();
	}


	public void setStage(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
	}


	public void setMenuViewController(MenuViewController menuViewController)
	{
		this.menuViewController = menuViewController;
	}


	public GameboardViewController getGameboardViewController()
	{
		return this.gameboardViewController;
	}

	public void setGameBoardViewController(GameboardViewController gameboardViewController)
	{
		this.gameboardViewController = gameboardViewController;
	}

	
	public SplendorController getSplendorController()
	{
		return this.splendorController;
	}

	public void setSplendorController(SplendorController splendorController)
	{
		this.splendorController = splendorController;
	}

	public StackPane getRootPane(){
		return this.rootPane;
	}
}
