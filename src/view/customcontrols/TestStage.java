package view.customcontrols;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TestStage extends Application {
    private final ArrayList<CardCustomControl> cards = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        Random randomizer = new Random();
        HBox hBox = new HBox();
        try {
//            for (GemType gemType : GemType.values()) {
//                if (!gemType.equals(GemType.JOKER)) {
//                    int level = randomizer.nextInt(3) + 1;
//                    int prestige = randomizer.nextInt(5) + 1;
//                    CardCustomControl card = new CardCustomControl(level, prestige);
//                    card.setGemType(gemType);
//                    int[] gemAmount = new int[6];
//                    for(int i = 0; i<gemAmount.length-1; i++){
//                        gemAmount[i] = randomizer.nextInt(5);
//                    }
//                    Gems gems = new Gems(gemAmount,GemAmountType.UNLIMITED);
//                    card.setCosts(gems);
//                    hBox.getChildren().addAll(card);
//                    cards.add(card);
//                }
//            }
//            Button flipButton = new Button("Flip");
//            flipButton.setOnMouseClicked((e)->{
//                for(CardCustomControl c : cards){
//                    c.setCardBackVisible(!c.getCardBackVisible());
//                }
//            });
//            hBox.getChildren().addAll(flipButton);
            Gems gems = new Gems(new int[]{1,2,0,3,2,0},GemAmountType.UNLIMITED);
            Card card1 = new Card(1, gems, true, 1);
            Card card2 = new Card(2, gems, true, 1);
            Card card3 = new Card(3, gems, true, 1);
            Card card4 = new Card(4, gems, true, 1);
            Card card5 = new Card(5, gems, true, 1);


            Scene scene = new Scene(new NobleCardCustomControl(card1,  1));
            primaryStage.setScene(scene);
            primaryStage.setTitle("Custom Control");
            primaryStage.setWidth(1600);
            primaryStage.setHeight(800);
            primaryStage.show();
            System.out.println(new NobleDialogControl(Arrays.asList(card1,card2,card3,card4,card5)).showAndWait().get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
