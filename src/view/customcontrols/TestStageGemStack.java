package view.customcontrols;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.GemAmountType;
import model.Gems;

import java.util.ArrayList;

public class TestStageGemStack extends Application {
    private final ArrayList<GemStackCustomControl> gemStack = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        try {

            int[] gemArr1 = new int[]{5, 5, 5, 5, 5, 5};
            Gems gem1 = new Gems(gemArr1, GemAmountType.GAMEBOARD_FOUR_PLAYERS);
            GemStackCustomControl stackVar1 = new GemStackCustomControl(gem1, true);
            gemStack.add(stackVar1);

            // int[] gemArr2 = new int[] {0,1,0,1,0,1};
            // Gems gem2 = new Gems(gemArr2, GemAmountType.GAMEBOARD_FOUR_PLAYERS);
            // GemStackCustomControl stackVar2 = new GemStackCustomControl(gem2);
            // gemStack.add(stackVar2);

            Scene scene = new Scene(stackVar1);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Custom Control");
            primaryStage.setWidth(1600);
            primaryStage.setHeight(800);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
