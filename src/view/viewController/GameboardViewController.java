package view.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Card;
import model.GemAmountType;
import model.Gems;
import model.Player;
import view.customcontrols.*;
import view.viewAUI.GameboardWindowAUI;

import java.util.*;

public class GameboardViewController implements GameboardWindowAUI {

    private MainViewController mainViewController;

    //private List<Card> openCards;

    private GemStackCustomControl gemsGameBoard;
    private HBox[] cardRows;
    private BackgroundImage myBI;
    //private Gems selectedGems;

    @FXML
    VBox gameCards;

    @FXML
    VBox players;

    @FXML
    Pane gameGems;

    @FXML
    Pane playerHand;

    @FXML
    Pane nobleCards;

    @FXML
    Button undo;
    @FXML
    Button redo;
    @FXML
    Button saveButton;
    private PlayerBoardCustomControl currentPlayerBoard;

    @FXML
    public void initialize() {
        System.out.println("init gameboardviewcontroller");
        try {
            this.cardRows = new HBox[]{new HBox(25), new HBox(25), new HBox(25)};

            this.refreshCards();
            this.refreshCurrentPlayer();
            this.gemsGameBoard = new GemStackCustomControl(this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getGems(), false);
            gameGems.getChildren().add(gemsGameBoard);

            this.myBI = new BackgroundImage(new Image("/img/Nobles.png", 120, 120, false, false),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            this.refreshNobles();
            this.activateRedo(false);
            this.activateUndo(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    /**
     * the Player clicked on Tip Button
     */
    public void onTipButtonClicked(ActionEvent event) {
        boolean showTip = true;
        if (mainViewController.getSplendorController().getSplendor().getGame().isHighscoreable()) {
            showTip = showNotHighscoreableDialog();
        }
        if (showTip) {
            this.mainViewController.getSplendorController().getAIController().showTip();
        }
    }

    /**
     * the player clicked on Save Button
     */
    public void onSaveButtonClicked() {
        System.out.println("SaveButtonClicked");
        this.saveButton.setDisable(true);
        this.mainViewController.getSplendorController().getIOController().saveCurrentGame();
    }

    /**
     * the Player clicked on Undo Button
     */
    public void onUndoButtonClicked(ActionEvent event) {
        System.out.println("UndoButtonClicked");
        boolean doUndo = true;
        if (mainViewController.getSplendorController().getSplendor().getGame().isHighscoreable()) {
            doUndo = showNotHighscoreableDialog();
        }
        if (doUndo) {
            this.mainViewController.getSplendorController().getGameboardController().undo();
        }
    }

    /**
     * the player clicked on Redo Button
     */
    public void onRedoButtonClicked() {
        System.out.println("RedoButtonClicked");
        this.mainViewController.getSplendorController().getGameboardController().redo();
    }

    /**
     * the player clicked on Play Button
     */

    public void onPlayButtonClicked() {
        System.out.println("PlayButtonClicked");
        this.mainViewController.getSplendorController().getGameboardController().resume();
    }

    /**
     * the player clicked on Buy Card Button
     *
     * @param action the card, which the player choose to buy
     */

    public void onBuyCardButtonClicked(CardCustomControl cardControl) {
        System.out.println(cardControl.getAssociatedCard());
        try {
            Gems chosenGems = currentPlayerBoard.getSelectedGems();
            if (chosenGems.getTotalAmount() == 0) {
                this.mainViewController.getSplendorController().getPlayerController().buyCard(cardControl.getAssociatedCard());
            } else {
                System.out.println(Arrays.toString(chosenGems.getGems()));
                this.mainViewController.getSplendorController().getPlayerController().buyCard(cardControl.getAssociatedCard(), chosenGems);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.mainViewController.showError(e.toString());
        }

    }

    /**
     * the player clicked on Reserve Card Button
     */
    public void onReserveCardButtonClicked(CardCustomControl cardControl) {
        //CardCustomControl cardControl = (CardCustomControl) (((Node)action.getTarget()).getParent().getParent());
        System.out.println(cardControl.getAssociatedCard());
        try {
            this.mainViewController.getSplendorController().getPlayerController().reserveCard(cardControl.getAssociatedCard());
        } catch (Exception e) {
            this.mainViewController.showError(e.toString());
        }

    }

    /**
     * the player clicked on Buy Noble Button
     */
    public void onBuyNobleButtonClicked(Card nobleCard) {
        System.out.println("BuyNobleButtonClicked");
        this.mainViewController.getSplendorController().getPlayerController().buyNoble(nobleCard);
    }

    /**
     * the player clicked on End Game Button
     */
    public void onEndGameButtonClicked() {
        System.out.println("EndGameButtonClicked");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Spiel beenden");
        alert.setContentText("Möchtest du speichern?");
        ButtonType saveCloseButton = new ButtonType("Speichern");
        ButtonType closeButton = new ButtonType("Nicht Speichern");
        ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(saveCloseButton, closeButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == saveCloseButton) {
            System.out.println("Save and close");
            this.mainViewController.getSplendorController().getIOController().saveCurrentGame();
            this.mainViewController.openStartWindow();
        } else if (result.get() == closeButton) {
            System.out.println("Close without saving");
            this.mainViewController.openStartWindow();
        } else {
            System.out.println("cancel");
            //Do nothing
        }
    }

    /**
     * the player clicked on Take Gems Button
     */

    public void onTakeGemsButtonClicked() {
        // ??
        System.out.println("TakeGemsButtonClicked");
        try {
            this.mainViewController.getSplendorController().getPlayerController().takeGems(this.gemsGameBoard.getChosenGems(GemAmountType.PLAYER_HAND));
        } catch (Exception e) {
            e.printStackTrace();
            this.mainViewController.showError(e.toString());
        }

    }

    /**
     * @see Model.View.GameboardWindowAUI#refreshPlayersHand()
     * the Hand of the player will be refreshed
     */
    public void refreshPlayersHand() {
        this.refreshCurrentPlayer();
    }

    /**
     * @see Model.View.GameboardWindowAUI#refreshCurrentPlayer()
     * <p>
     * change to the next player
     */
    public void refreshCurrentPlayer() {
        List<Player> playerList = this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getPlayers();
        players.getChildren().clear();
        playerHand.getChildren().clear();
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).equals(this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getCurrentPlayer())) {
                currentPlayerBoard = new PlayerBoardCustomControl(playerList.get(i), true, this);
                playerHand.getChildren().add(currentPlayerBoard);
            } else {
                PlayerBoardCustomControl playerBoard = new PlayerBoardCustomControl(playerList.get(i), false, this);
                players.getChildren().add(playerBoard);
            }
        }
    }

    /**
     * @see Model.View.GameboardWindowAUI#refreshCards()
     * <p>
     * the cards will be refreshed
     */
    public void refreshCards() {
        gameCards.getChildren().clear();
        List<Card> levelOneCards = this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getLevelOneCards();
        List<Card> levelTwoCards = this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getLevelTwoCards();
        List<Card> levelThreeCards = this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getLevelThreeCards();
        List<Card> openCards = this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getOpenCards();
        for (int i = 0; i < cardRows.length; i++) {
            cardRows[i].getChildren().clear();
            Card covered;
            cardRows[i].setPadding(new Insets(25, 0, 0, 15));
            gameCards.getChildren().add(cardRows[i]);
            switch (i) {
                case 0:
                    covered = levelOneCards.get(4);
                    break;
                case 1:
                    covered = levelTwoCards.get(4);
                    break;
                case 2:
                    covered = levelThreeCards.get(4);
                    break;
                default:
                    covered = levelOneCards.get(4);
            }
            CardCustomControl cardCustomControl = new CardCustomControl(covered);
            cardCustomControl.setGameboardViewController(this);
            cardCustomControl.setCardBackVisible(true);
            cardRows[i].getChildren().add(cardCustomControl);
        }

        openCards.forEach(card -> {
            CardCustomControl cardCustomControl = new CardCustomControl(card);
            cardCustomControl.setGameboardViewController(this);
            if(card.canBuy(this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getCurrentPlayer()) ) {
            	cardCustomControl.setHighlight(true);
            }
            cardRows[card.getLevel() - 1].getChildren().add(cardCustomControl);

        });


    }

    /**
     * @see Model.View.GameboardWindowAUI#refreshGems()
     * <p>
     * the Gems will be refreshed
     */
    public void refreshGems() {
        this.gemsGameBoard.setGems(this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getGems());
        //refresh GemStacks of Players not needed done in recreation
    }

    /**
     * @see Model.View.GameboardWindowAUI#refreshNobles()
     * <p>
     * refreshes the noble cards
     */
    public void refreshNobles() {
        this.nobleCards.getChildren().clear();
        List<Integer> queenSuffixes = Arrays.asList(1, 2, 3, 4, 5);
        Collections.shuffle(queenSuffixes);
        List<Card> nobleCardsList = this.mainViewController.getSplendorController().getSplendor().getGame().getCurrentGameState().getNobleCards();
        for (int i = 0; i < nobleCardsList.size(); i++) {
            NobleCardCustomControl nobleCardControl = new NobleCardCustomControl(nobleCardsList.get(i),  queenSuffixes.get(i));
            //nobleCardControl.setDisable(true);
            nobleCardControl.setLayoutX(i % 2 != 0 ? 140 : 0);
            nobleCardControl.setLayoutY(i / 2 * 140);
            this.nobleCards.getChildren().add(nobleCardControl);
        }
    }

    /**
     * @param active if the parameter true the undo button will be activated
     * @see Model.View.GameboardWindowAUI#activateUndo(boolean)
     * <p>
     * Goes to the precedent State if possible
     */
    public void activateUndo(boolean active) {
        this.saveButton.setDisable(false);
        this.undo.setVisible(active);
    }

    /**
     * @param active if the parameter true the Redo button will be activated
     * @see Model.View.GameboardWindowAUI#activateRedo(boolean)
     * <p>
     * Goes to the next  State
     */
    public void activateRedo(boolean active) {
        this.redo.setVisible(active);
    }

    /**
     * @see Model.View.GameboardWindowAUI#refreshGameState()
     * <p>
     * the Game State will be refreshed
     */
    public void refreshGameState() {
        this.refreshCards();
        this.refreshCurrentPlayer();
        this.refreshGems();
        this.refreshNobles();
        this.refreshPlayersHand();
    }

    /**
     * @param card the Card in the player's Hand
     * @param buy  the possibility of the player to buy a card
     * @see Model.View.GameboardWindowAUI#showTip(Model.Model.Card, boolean)
     * <p>
     * shows the generated  Tipps
     */
    public void showTip(Card card, boolean buy) {
        // try this card
        System.out.println("show tip");
        for (HBox row : cardRows) {
            row.getChildren().forEach(cardCustomController -> {
                if (((CardCustomControl) cardCustomController).getAssociatedCard().equals(card)) {
                    ((CardCustomControl) cardCustomController).setTippHighlight(true);
                }
            });
        }
    }

    /**
     * @return if true, ok was selected
     */
    private boolean showNotHighscoreableDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Highscore");
        alert.setContentText("Bist du sicher, du bist danach nicht mehr im Highscore");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     * @param winner the winning player
     * @see Model.View.GameboardWindowAUI#gameOver(Model.Model.Player)
     * <p>
     * finishes the Game
     */
    public void gameOver(Player winner) {
        System.out.println("game over");
        Alert gameOverMsg = new Alert(Alert.AlertType.INFORMATION);
        gameOverMsg.setTitle("Game over");
        gameOverMsg.setHeaderText("Speil vorbei!");
        gameOverMsg.setContentText("Gewonnen hat: " + winner.getName());
        gameOverMsg.showAndWait();
        //this.mainViewController.openStartWindow();
    }

    /**
     * @see Model.View.GameboardWindowAUI#showNobles()
     * <p>
     * shows noble cards
     */
    public void showNobles(List<Card> nobles) {
        // that he can buy
        Optional<Card> selectedNoble = new NobleDialogControl(nobles).showAndWait();
        this.mainViewController.getSplendorController().getPlayerController().buyNoble(selectedNoble.get());
    }

    public void showBlitzdings() {
        hideBlitzdings();
        System.out.println("Trying to show Will");
        this.mainViewController.getRootPane().getChildren().add(new ImageView("/img/Blitzdings.gif"));
    }

    public void hideBlitzdings() {
        System.out.println("Trying to hide Will");
        try {
            this.mainViewController.getRootPane().getChildren().remove(1);
        } catch (Exception e) {
            System.out.println("No Will to hide");
        }
    }

    /**
     * @param amount total amount of gems
     */
    public void gemReturn(int amount) {
        System.out.println("on gem return callback");
        Optional<Gems> gemsToReturn = new GemStackDialog(currentPlayerBoard.getCurrentPlayerGems(), amount - 10).showAndWait();
        this.mainViewController.getSplendorController().getPlayerController().giveGemsBack(gemsToReturn.get());

    }
}
