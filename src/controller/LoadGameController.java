package controller;

import java.io.File;
import java.util.List;

import model.Game;
import model.Tuple;
import view.viewAUI.LoadGameWindowAUI;

public class LoadGameController {

	/**
	 * used to call controller and access model
	 */
	private SplendorController splendorController;

	/**
	 * used to address view and refresh loadGame list after editing or loading.
	 */
	private LoadGameWindowAUI loadGameWindowAUI;

	/**
	 * constructor to initially connect with splendorController
	 * 
	 * @param splendorController
	 */
	public LoadGameController(SplendorController splendorController, LoadGameWindowAUI loadGameWindowAUI) {
		this.splendorController = splendorController;
		this.loadGameWindowAUI = loadGameWindowAUI;
	}

	/**
	 * used to delete a specific Game.
	 * 
	 * @param file
	 */
	public void deleteGame(File file) {
		List<Tuple<File, String>> allgames = splendorController.getIOController().loadGames();
		if(allgames == null ){return ;}
		else {
			for(Tuple<File, String> game : allgames){
				if(game.getFirstValue().equals(file)){
					splendorController.getIOController().deleteGame(file);
				}
			}
		}
	}

	/**
	 * used to load all the Games that you have saved.
	 */
	public void loadSavedGames() {
		List<Tuple<File,String>> savedgames = splendorController.getIOController().loadGames();
		if(savedgames == null) 
			return;

		for(Tuple<File,String> game : savedgames) {
			splendorController.getIOController().loadGame(game.getFirstValue());
		}
		loadGameWindowAUI.showGameList(savedgames);
	}

	/**
	 * to start a specific Game that you choosed it from the saved games liste.
	 * 
	 * @param file
	 */

	public void startSavedGame(File file) {
		Game game = splendorController.getIOController().loadGame(file);
		splendorController.getSplendor().setGame(game);
		loadGameWindowAUI.enterGame();
		splendorController.getGameboardController().resume();
	}

	/**
	 * @return the splendorController
	 */
	public SplendorController getSplendorController() {
		return splendorController;
	}

	/**
	 * @param splendorController the splendorController to set
	 */
	public void setSplendorController(SplendorController splendorController) {
		this.splendorController = splendorController;
	}

	/**
	 * @return the loadGameWindowAUI
	 */
	public LoadGameWindowAUI getLoadGameWindowAUI() {
		return loadGameWindowAUI;
	}

	/**
	 * @param loadGameWindowAUI the loadGameWindowAUI to set
	 */
	public void setLoadGameWindowAUI(LoadGameWindowAUI loadGameWindowAUI) {
		this.loadGameWindowAUI = loadGameWindowAUI;
	}

}
