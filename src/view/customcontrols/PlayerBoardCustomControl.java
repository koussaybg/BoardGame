package view.customcontrols;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.*;
import view.viewController.GameboardViewController;

import java.io.IOException;

public class PlayerBoardCustomControl extends AnchorPane {
    private final GameboardViewController gameBoardViewController;
    //private final Player player;
    @FXML
    Label prestigeDiamond;
    @FXML
    Label gemCountDiamond;
    @FXML
    Label prestigeEmerald;
    @FXML
    Label gemCountEmerald;
    @FXML
    Label prestigeOnyx;
    @FXML
    Label gemCountOnyx;
    @FXML
    Label prestigeRuby;
    @FXML
    Label gemCountRuby;
    @FXML
    Label prestigeSapphire;
    @FXML
    Label gemCountSapphire;

    @FXML
    Pane playerGems;

    @FXML
    Label nameLabel;

    @FXML
    Label prestigeLabel;

    @FXML
    HBox reservedCardsHBox;

    @FXML
    Button returnGemsButton;

    private final Player player;
    private final boolean currentPlayer;
    private GemStackCustomControl gemStackCustomControl;

    public PlayerBoardCustomControl(Player player, boolean currentPlayer, GameboardViewController gameboardViewController) {
        this.gameBoardViewController = gameboardViewController;
        this.player = player;
        this.currentPlayer = currentPlayer;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/views/player-board.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        getStylesheets().add(getClass().getResource("/styles/player-board.css").toExternalForm());

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        initCards();
        initReservedCards();
        returnGemsButton.setVisible(false);
        this.nameLabel.setText(player.getName());
        this.prestigeLabel.setText(String.valueOf(player.getPrestige()));
        gemStackCustomControl = new GemStackCustomControl(player.getGems(), true);
        gemStackCustomControl.setLayoutX(670);
        playerGems.getChildren().add(0, gemStackCustomControl);
        if (!currentPlayer) {
            /*Pane invisiblePane = new Pane();
            invisiblePane.setMinHeight(350.0);
            invisiblePane.setMinWidth(950.0);
            invisiblePane.setLayoutX(0);
            invisiblePane.setLayoutY(0);
            invisiblePane.setStyle("-fx-background-color: rgba(0.0,0.0,0.0, 0.0)");
            this.getChildren().add(invisiblePane);*/
            this.setScaleX(.5);
            this.setScaleY(.5);
            this.setTranslateX(-235.0);
        }
    }

    /**
     * May return null
     *
     * @return selected Gems or null, if no gems selected
     */
    public Gems getSelectedGems() {
        return this.gemStackCustomControl.getChosenGems(GemAmountType.PLAYER_HAND);
    }

    public void setReturnGemMode(int gemsToReturnCount) {
        this.returnGemsButton.setVisible(true);
    }

    private void initCards() {
        int[] prestiges = new int[5];
        int[] gems = new int[5];
        for (Card c : player.getBoughtCards()) {
            prestiges[c.getGemType().ordinal()] += c.getPrestige();
            gems[c.getGemType().ordinal()]++;
        }
        prestigeDiamond.setText("Prestige: " + prestiges[GemType.DIAMOND.ordinal()]);
        gemCountDiamond.setText("" + gems[GemType.DIAMOND.ordinal()]);
        prestigeEmerald.setText("Prestige: " + prestiges[GemType.EMERALD.ordinal()]);
        gemCountEmerald.setText("" + gems[GemType.EMERALD.ordinal()]);
        prestigeOnyx.setText("Prestige: " + prestiges[GemType.ONYX.ordinal()]);
        gemCountOnyx.setText("" + gems[GemType.ONYX.ordinal()]);
        prestigeRuby.setText("Prestige: " + prestiges[GemType.RUBY.ordinal()]);
        gemCountRuby.setText("" + gems[GemType.RUBY.ordinal()]);
        prestigeSapphire.setText("Prestige: " + prestiges[GemType.SAPPHIRE.ordinal()]);
        gemCountSapphire.setText("" + gems[GemType.SAPPHIRE.ordinal()]);

    }

    private void initReservedCards() {
        for (int i = 0; i < player.getReservedCards().size(); i++) {
            CardCustomControl reservedCardControl = new CardCustomControl(player.getReservedCards().get(i));
            reservedCardControl.setGameboardViewController(gameBoardViewController);
            reservedCardControl.setReservable(false);
            reservedCardControl.setBuyable(true);
            if (currentPlayer) {
                reservedCardsHBox.getChildren().add(reservedCardControl);
            } else {
                this.getChildren().add(reservedCardControl);
                reservedCardControl.setRotate(-90.0);
                reservedCardControl.setTranslateX(200 * i + 40);
                reservedCardControl.setTranslateY(185);
            }
        }
    }

    public Gems getCurrentPlayerGems() {
        return this.player.getGems();
    }
}

