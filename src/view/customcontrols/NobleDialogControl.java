package view.customcontrols;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import model.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NobleDialogControl extends Dialog<Card> implements NobleSelectedListener {

    private final Node selectButton;
    private final List<NobleCardCustomControl> cardControls = new ArrayList<>();
    private Card selectedCard;


    public NobleDialogControl(List<Card> nobles) {
        initModality(Modality.APPLICATION_MODAL);
        // not closable
        getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());

        this.setTitle("Adeligen auswählen");
        this.setHeaderText("Wähle den Adeligen, der dich besuchen soll.");

        ButtonType selectButton = new ButtonType("Auswählen", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(selectButton);

        this.selectButton = this.getDialogPane().lookupButton(selectButton);
        this.selectButton.setDisable(true);

        AnchorPane noblePane = new AnchorPane();
        List<Integer> queenSuffixes = Arrays.asList(1, 2, 3, 4, 5);
        Collections.shuffle(queenSuffixes);
        for (int i = 0; i < nobles.size(); i++) {
            NobleCardCustomControl nobleCardControl = new NobleCardCustomControl(nobles.get(i), queenSuffixes.get(i));
            nobleCardControl.setLayoutX(i % 2 != 0 ? 140 : 0);
            nobleCardControl.setLayoutY(i / 2 * 140);
            nobleCardControl.setNobleSelectedListener(this);
            cardControls.add(nobleCardControl);
            noblePane.getChildren().add(nobleCardControl);
        }

        this.getDialogPane().setContent(noblePane);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == selectButton) {
                return selectedCard;
            }
            return null;
        });
    }

    @Override
    public void selectedNobel(NobleCardCustomControl selectedCardControl) {
        this.selectButton.setDisable(false);
        this.selectedCard = selectedCardControl.getAssociatedCard();
        for(NobleCardCustomControl nobleCardCustomControl: cardControls){
            if(nobleCardCustomControl != selectedCardControl){
                nobleCardCustomControl.setSelected(false);
            }
        }
    }
}
