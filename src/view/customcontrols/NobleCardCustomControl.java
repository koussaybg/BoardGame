package view.customcontrols;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Card;
import model.GemType;
import model.Gems;

import java.io.IOException;

public class NobleCardCustomControl extends AnchorPane {

    private final Card associatedCard;
    private boolean selected;
    private NobleSelectedListener nobleSelectedListener;

    @FXML
    Label gemCountDiamond;
    @FXML
    Label gemCountEmerald;
    @FXML
    Label gemCountOnyx;
    @FXML
    Label gemCountRuby;
    @FXML
    Label gemCountSapphire;
    @FXML
    Label prestigeLabel;

    public NobleCardCustomControl(Card card, int queenSuffix) {
        this.associatedCard = card;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/views/noble-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        getStylesheets().add(getClass().getResource("/styles/noble-card.css").toExternalForm());
        getStyleClass().add("background-" + queenSuffix);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        initalizeCosts(associatedCard.getCost());
        //prestigeLabel.setText(String.valueOf(associatedCard.getPrestige()));
        prestigeLabel.setOnMouseClicked(event -> onBuyClicked(event));
    }

    private void onBuyClicked(MouseEvent event) {
        this.setSelected(true);
    }


    private void initalizeCosts(Gems gems) {
        int diamondAmount = gems.getAmountOf(GemType.DIAMOND);
        int emeraldAmount = gems.getAmountOf(GemType.EMERALD);
        int onyxAmount = gems.getAmountOf(GemType.ONYX);
        int rubyAmount = gems.getAmountOf(GemType.RUBY);
        int sapphireAmount = gems.getAmountOf(GemType.SAPPHIRE);
        if (diamondAmount > 0) {
            gemCountDiamond.setText(String.valueOf(diamondAmount));
        } else {
            getChildren().remove(gemCountDiamond);
        }
        if (emeraldAmount > 0) {
            gemCountEmerald.setText(String.valueOf(emeraldAmount));
        } else {
            getChildren().remove(gemCountEmerald);
        }
        if (onyxAmount > 0) {
            gemCountOnyx.setText(String.valueOf(onyxAmount));
        } else {
            getChildren().remove(gemCountOnyx);
        }
        if (rubyAmount > 0) {
            gemCountRuby.setText(String.valueOf(rubyAmount));
        } else {
            getChildren().remove(gemCountRuby);
        }
        if (sapphireAmount > 0) {
            gemCountSapphire.setText(String.valueOf(sapphireAmount));
        } else {
            getChildren().remove(gemCountSapphire);
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected && this.nobleSelectedListener != null) {
            this.getStyleClass().add("highlight-border");
            this.nobleSelectedListener.selectedNobel(this);
        }
        if (!selected) {
            this.getStyleClass().remove("highlight-border");
        }
    }

    /**
     * Sets the nobleSelectedListener to passed nobleSelectedListener
     */
    public void setNobleSelectedListener(NobleSelectedListener nobleSelectedListener) {
        this.nobleSelectedListener = nobleSelectedListener;
    }

    /**
     * @return Get the selected
     */
    public boolean isSelected() {
        return selected;
    }

    public Card getAssociatedCard() {
        return this.associatedCard;
    }

}
