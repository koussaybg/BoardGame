package controller;

import java.util.List;

import model.Game;
import model.Highscore;
import model.Player;
import view.viewAUI.HighscoreWindowAUI;

public class HighscoreController {

    /**
     * used to call controller and access model
     */
    private SplendorController splendorController;

    /**
     * used to adress view and refresh highscore list after editing or loading
     */
    private HighscoreWindowAUI highscoreWindowAUI;

    /**
     * constructor to initially connect with splendorController
     *
     * @param splendorController Mediator-Controller that will be called by methods of this class
     */
    public HighscoreController(SplendorController splendorController, HighscoreWindowAUI highscoreWindowAUI) {
        this.splendorController = splendorController;
        this.highscoreWindowAUI = highscoreWindowAUI;
    }

    /**
     * deletes all existing highscores and reloads view with empty highscore-list
     */
    public void resetHighscores() {
        IOController ioController = splendorController.getIOController();
        splendorController.getSplendor().getHighscores().clear();
        ioController.deleteHighscores();
        highscoreWindowAUI.refreshHighscores();
    }

    /**
     * loads all exisiting highscores with help of IOController and adds them to model
     */
    public void loadHighscores() {
        IOController ioController = splendorController.getIOController();
        splendorController.getSplendor().setHighscores(ioController.loadHighscores());
    }

    /**
     * calculates highscore from given game and safes it afterwards
     *
     * @param game game, which is supposed to get highscore
     */
    public void createHighscore(Game game) {
        if (game.isHighscoreable()) {
            IOController ioController = splendorController.getIOController();
            List<Player> players = game.getCurrentGameState().getPlayers();
            List<Highscore> highscoresList = ioController.loadHighscores();
            int roundsPlayed = (game.getCurrentGameState().getCurrentTrain()) / (players.size());
            for (Player player : players) {
                if (!player.isAI()) {
                    Highscore highscorePlayer = new Highscore(player, roundsPlayed);
                    ioController.saveHighscore(highscorePlayer);
                    highscoresList.add(highscorePlayer);
                    splendorController.getSplendor().getHighscores().add(highscorePlayer);
                }
            }
        }
    }
}
