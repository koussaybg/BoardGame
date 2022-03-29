package view.customcontrols;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import model.*;

import java.io.IOException;

public class GemStackCustomControl extends AnchorPane {

    private final boolean jokerClickable;
    @FXML
    private Button diamondButton;
    @FXML
    private Button emeraldButton;
    @FXML
    private Button onyxButton;
    @FXML
    private Button rubyButton;
    @FXML
    private Button sapphireButton;
    @FXML
    private Button jokerButton;

    private Button[] gemButtons;

    private Gems gems;
    private int[] chosenGems = new int[6];

    private GemCountChangeListener gemCountChangeListener;

    public GemStackCustomControl(Gems gems, boolean jokerClickable) {
        this.gems = gems;
        this.jokerClickable = jokerClickable;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/views/gemStack.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        getStylesheets().add(getClass().getResource("/styles/gem.css").toExternalForm());

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        gemButtons = new Button[]{diamondButton, emeraldButton, rubyButton, sapphireButton, onyxButton, jokerButton};
        for (GemType currentGemType : GemType.values()) {
            gemButtons[currentGemType.ordinal()].setTextFill(Color.web("#ffcc00"));
            gemButtons[currentGemType.ordinal()].setFont(Font.font(30));
            gemButtons[currentGemType.ordinal()].scaleXProperty().setValue(0.9);
            gemButtons[currentGemType.ordinal()].scaleYProperty().setValue(0.9);

        }
        this.jokerButton.setDisable(!jokerClickable);
        this.refreshStack();
    }


    /**
     * Call on each beginning of a new train before refresh. Only necessary for the Board-Stack.
     *
     * @param gems Stack of the current GameState.
     */
    public void setGems(Gems gems) {
        this.gems = gems;
        this.refreshStack();
    }

    public void refreshStack() {
        chosenGems = new int[6];
        for (GemType currentGemType : GemType.values()) {
            if (gems.getGems()[currentGemType.ordinal()] == 0) {
                gemButtons[currentGemType.ordinal()].setVisible(false);
            } else {
                gemButtons[currentGemType.ordinal()].setVisible(true);
                gemButtons[currentGemType.ordinal()].setText(String.valueOf(gems.getGems()[currentGemType.ordinal()]));
            }
        }
    }

    /**
     * Provides an Gem Object which contains the current Amount of Gems chosen by the Player
     *
     * @param gemAmountType Type of Stack
     * @return
     */
    public Gems getChosenGems(GemAmountType gemAmountType) {
        return new Gems(chosenGems, gemAmountType);
    }

    public void onDiamondClicked(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && chosenGems[0] < gems.getGems()[0])
            chosenGems[0]++;
        else {
            if ((event.getButton() == MouseButton.SECONDARY) && chosenGems[0] > 0)
                chosenGems[0]--;
        }

        if (chosenGems[0] > 0)
            gemButtons[0].setText(chosenGems[0] + "/" + String.valueOf(gems.getGems()[0]));
        else
            gemButtons[0].setText(String.valueOf(gems.getGems()[0]));

        System.out.println(GemType.values()[0] + ":" + chosenGems[0]);
        if (gemCountChangeListener != null) {
            gemCountChangeListener.gemCountChanged(getChosenGems(GemAmountType.UNLIMITED).getTotalAmount());
        }
    }

    public void onEmeraldClicked(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && chosenGems[1] < gems.getGems()[1])
            chosenGems[1]++;
        else {
            if ((event.getButton() == MouseButton.SECONDARY) && chosenGems[1] > 0)
                chosenGems[1]--;
        }

        if (chosenGems[1] > 0)
            gemButtons[1].setText(chosenGems[1] + "/" + String.valueOf(gems.getGems()[1]));
        else
            gemButtons[1].setText(String.valueOf(gems.getGems()[1]));

        System.out.println(GemType.values()[1] + ":" + chosenGems[1]);
        if (gemCountChangeListener != null) {
            gemCountChangeListener.gemCountChanged(getChosenGems(GemAmountType.UNLIMITED).getTotalAmount());
        }
    }

    public void onRubyClicked(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && chosenGems[2] < gems.getGems()[2])
            chosenGems[2]++;
        else {
            if ((event.getButton() == MouseButton.SECONDARY) && chosenGems[2] > 0)
                chosenGems[2]--;
        }

        if (chosenGems[2] > 0)
            gemButtons[2].setText(chosenGems[2] + "/" + String.valueOf(gems.getGems()[2]));
        else
            gemButtons[2].setText(String.valueOf(gems.getGems()[2]));

        System.out.println(GemType.values()[2] + ":" + chosenGems[2]);
        if (gemCountChangeListener != null) {
            gemCountChangeListener.gemCountChanged(getChosenGems(GemAmountType.UNLIMITED).getTotalAmount());
        }
    }

    public void onSapphireClicked(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && chosenGems[3] < gems.getGems()[3])
            chosenGems[3]++;
        else {
            if ((event.getButton() == MouseButton.SECONDARY) && chosenGems[3] > 0)
                chosenGems[3]--;
        }

        if (chosenGems[3] > 0)
            gemButtons[3].setText(chosenGems[3] + "/" + String.valueOf(gems.getGems()[3]));
        else
            gemButtons[3].setText(String.valueOf(gems.getGems()[3]));

        System.out.println(GemType.values()[3] + ":" + chosenGems[3]);
        if (gemCountChangeListener != null) {
            gemCountChangeListener.gemCountChanged(getChosenGems(GemAmountType.UNLIMITED).getTotalAmount());
        }
    }

    public void onOnyxClicked(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && chosenGems[4] < gems.getGems()[4])
            chosenGems[4]++;
        else {
            if ((event.getButton() == MouseButton.SECONDARY) && chosenGems[4] > 0)
                chosenGems[4]--;
        }

        if (chosenGems[4] > 0)
            gemButtons[4].setText(chosenGems[4] + "/" + String.valueOf(gems.getGems()[4]));
        else
            gemButtons[4].setText(String.valueOf(gems.getGems()[2]));

        System.out.println(GemType.values()[4] + ":" + chosenGems[2]);
        if (gemCountChangeListener != null) {
            gemCountChangeListener.gemCountChanged(getChosenGems(GemAmountType.UNLIMITED).getTotalAmount());
        }
    }

    /**
     * Sets the gemCountChangeListener to passed gemCountChangeListener
     */
    public void setGemCountChangeListener(GemCountChangeListener gemCountChangeListener) {
        this.gemCountChangeListener = gemCountChangeListener;
    }

    public void onJokerClicked(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && chosenGems[5] < gems.getGems()[5])
            chosenGems[5]++;
        else {
            if ((event.getButton() == MouseButton.SECONDARY) && chosenGems[5] > 0)
                chosenGems[5]--;
        }

        if (chosenGems[5] > 0)
            gemButtons[5].setText(chosenGems[5] + "/" + String.valueOf(gems.getGems()[5]));
        else
            gemButtons[5].setText(String.valueOf(gems.getGems()[5]));

        System.out.println(GemType.values()[5] + ":" + chosenGems[5]);
        if (gemCountChangeListener != null) {
            gemCountChangeListener.gemCountChanged(getChosenGems(GemAmountType.UNLIMITED).getTotalAmount());
        }
    }
}
