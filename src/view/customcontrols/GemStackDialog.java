package view.customcontrols;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import model.GemAmountType;
import model.Gems;

public class GemStackDialog extends Dialog<Gems> implements GemCountChangeListener{

    private final Node returnButtonNode;
    private final int returnAmount;

    /**
     *
     * @param playerGems
     * @param amount amount of gems to return
     */
    public GemStackDialog(Gems playerGems, int amount){
        this.returnAmount = amount;
        initModality(Modality.APPLICATION_MODAL);
        // not closable
        getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());

        GemStackCustomControl gemStackCustomControl = new GemStackCustomControl(playerGems, true);
        gemStackCustomControl.setGemCountChangeListener(this);
        this.setTitle("Zu viele Edelsteine");
        this.setHeaderText("Bitte wähle " + amount + " Edelsteine, die du zurückgibst.");

        ButtonType returnButton = new ButtonType("Return", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(returnButton);

        returnButtonNode = this.getDialogPane().lookupButton(returnButton);
        returnButtonNode.setDisable(true);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(gemStackCustomControl);
        this.getDialogPane().setContent(anchorPane);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == returnButton) {
                return gemStackCustomControl.getChosenGems(GemAmountType.UNLIMITED);
            }
            return null;
        });
    }

    @Override
    public void gemCountChanged(int currentAmount) {
        if(currentAmount == returnAmount){
            this.returnButtonNode.setDisable(false);
        }else{
            this.returnButtonNode.setDisable(true);
        }
    }
}
