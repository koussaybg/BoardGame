package view.viewAUI;

import java.io.File;
import java.util.List;

import model.Tuple;

public interface LoadGameWindowAUI {

	void showGameList(List<Tuple<File,String>> games);

	void enterGame();
}
