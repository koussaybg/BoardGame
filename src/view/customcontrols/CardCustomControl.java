package view.customcontrols;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Card;
import model.GemType;
import model.Gems;
import view.viewController.GameboardViewController;

import java.io.IOException;

public class CardCustomControl extends AnchorPane {


    private final int level;
    private final int prestige;
    private final Button reserveBackCard;
    private final ImageView cardBack;
    private boolean cardBackVisible = false;
    private boolean reservable = true;
    private boolean buyable = true;

    private final Card associatedCard;
    GameboardViewController gameboardViewController;

    @FXML
    private VBox costBox;
    @FXML
    private Label prestigeLabel;
    @FXML
    private ImageView gemImageView;

    private Button buyButton;
    private Button reserveButton;

    public CardCustomControl(Card card) {
        this.associatedCard = card;
        this.level = card.getLevel();
        this.prestige = card.getPrestige();
        this.reserveBackCard = createReserveCardBack();
        this.cardBack = createCardBack();
        initButtons();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/views/card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        getStylesheets().add(getClass().getResource("/styles/card.css").toExternalForm());

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        initalizeCosts(associatedCard.getCost());
        initalizeGemType(associatedCard.getGemType());
        prestigeLabel.setText(String.valueOf(prestige));
        prestigeLabel.getStyleClass().add("prestige");
        getChildren().remove(buyButton);
        getChildren().remove(reserveButton);
        hoverProperty().addListener((hover, oldVal, hovering) -> {
            if (hovering) {
                showHoverButtons();
            } else {
                getChildren().remove(buyButton);
                getChildren().remove(reserveButton);
            }
        });
    }

    private void showHoverButtons() {
        if (buyable && reservable) {
            buyButton.setPrefHeight(90.0);
            reserveButton.setPrefHeight(90.0);
            reserveButton.setLayoutY(90.0);
            getChildren().addAll(buyButton, reserveButton);
        }
        if (buyable && !reservable) {
            buyButton.setPrefHeight(180.0);
            getChildren().remove(reserveButton);
            getChildren().add(buyButton);
        }
        if(!buyable && reservable){
            reserveButton.setPrefHeight(180.0);
            getChildren().remove(buyButton);
            getChildren().addAll(reserveButton);
        }
    }

    public void onReserveClicked(MouseEvent action) {
        System.out.println("Reserve card clicked");
        this.gameboardViewController.onReserveCardButtonClicked(this);
    }

    public void onBuyClicked(MouseEvent action) {
        System.out.println("Buy card clicked");
        this.gameboardViewController.onBuyCardButtonClicked(this);
    }


    public void setCardBackVisible(boolean cardBackUp) {
        this.cardBackVisible = cardBackUp;
        this.buyable = false;
        this.reservable = true;
        if (cardBackUp) {
            getChildren().add(cardBack);
        } else {
            getChildren().remove(cardBack);
        }
    }

    public void setHighlight(boolean highlight) {
        if (highlight) {
            this.getStyleClass().add("highlight-border");
        } else {
            this.getStyleClass().remove("highlight-border");
        }
    }
    
    public void setTippHighlight(boolean highlight) {
        if (highlight) {
            this.getStyleClass().add("tipp-highlight-border");
        } else {
            this.getStyleClass().remove("tipp-highlight-border");
        }
    }


    private void initalizeCosts(Gems gems) {
        for (GemType gemType : GemType.values()) {
            if (gemType.ordinal() != GemType.JOKER.ordinal()) {
                int amount = gems.getAmountOf(gemType);
                if (amount > 0) {
                    addCostDiamond(gemType, amount);
                }
            }
        }
    }

    private void addCostDiamond(GemType gemType, int amount) {
        if (gemType == GemType.JOKER) {
            throw new IllegalArgumentException("Cost diamond can't be Joker");
        }
        String gemTypeName = gemType.toString().toLowerCase();
        String url = String.format("/img/gems_%s.png", gemTypeName);
        Image image = new Image(getClass().getResource(url).toExternalForm());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(30.0);
        imageView.setFitWidth(30.0);
        Label label = new Label();
        label.setTextFill(Color.web("#ffff00"));
        label.setText(String.valueOf(amount));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(imageView, label);
        costBox.getChildren().addAll(hBox);
    }

    private void initalizeGemType(GemType gemType) {
        if (gemType == GemType.JOKER) {
            throw new IllegalArgumentException("Card gem type can't be Joker");
        }
        String gemTypeName = gemType.toString().toLowerCase();
        getStyleClass().add("card-front-" + gemTypeName);
        String url = String.format("/img/gems_%s.png", gemTypeName);
        Image image = new Image(getClass().getResource(url).toExternalForm());
        gemImageView.setImage(image);
    }

    private ImageView createCardBack() {
        String backCardUrl = String.format("/img/card_back_level_%d.png", this.level);
        Image image = new Image(getClass().getResource(backCardUrl).toExternalForm());
        ImageView cardBack = new ImageView();
        cardBack.setFitHeight(180);
        cardBack.setFitWidth(120);
        cardBack.setImage(image);
        return cardBack;
    }

    private Button createReserveCardBack() {
        Button reserveBackCard = new Button("Reserve");
        reserveBackCard.setPrefHeight(180.0);
        reserveBackCard.setPrefWidth(120.0);
        reserveBackCard.getStyleClass().add("hover-button-background");
        reserveBackCard.setOnMouseClicked(event -> onReserveClicked(event));
        return reserveBackCard;
    }


    public boolean getCardBackVisible() {
        return cardBackVisible;
    }

    public void setGameboardViewController(GameboardViewController gameboardViewController) {
        this.gameboardViewController = gameboardViewController;
    }

    /**
     * Sets the reservable to passed reservable
     */
    public void setReservable(boolean reservable) {
        this.reservable = reservable;
    }

    /**
     * Sets the buyable to passed buyable
     */
    public void setBuyable(boolean buyable) {
        this.buyable = buyable;
    }


    public Card getAssociatedCard() {
        return this.associatedCard;
    }

    private void initButtons() {
        buyButton = new Button("Buy");
        buyButton.setPrefWidth(120.0);
        buyButton.getStyleClass().add("hover-button-background");
        buyButton.setOnMouseClicked((event -> onBuyClicked(event)));
        reserveButton = new Button("Reserve");
        reserveButton.setPrefWidth(120.0);
        reserveButton.getStyleClass().add("hover-button-background");
        reserveButton.setOnMouseClicked((event -> onReserveClicked(event)));
    }

}
