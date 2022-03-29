package model;

import java.util.ArrayList;
import java.util.List;

public class Splendor {

	/**
	 * the game to play
	 */
	private Game game;

	/**
	 * represent the highscores
	 */
	private List<Highscore> highscores;

	/**
	 *represent the cards currently loaded
	 */
	private Card[] cards;

	/**
	 * constructor
	 * Liste der Highscores darf zwar leer sein, muss aber immer ein Objekt != null sein.
	 */
	public Splendor() {
		highscores = new ArrayList<Highscore>();
	}

	/**
	 * returns the current Game 
	 * @return Game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * sets a game
	 * @param game: game to set
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * returns the Highscores 
	 * @return
	 */
	public List<Highscore> getHighscores() {
		return highscores;
	}

	/**
	 * sets the Highscores
	 * @param highscores
	 */
	public void setHighscores(List<Highscore> highscores) {
		this.highscores = highscores;
	}
	
	/**
	 * ruterns the cards
	 * @return
	 */
	public Card[] getCards() {
		return cards;
	}

	/**
	 * sets the cards
	 * @param cards
	 */
	public void setCards(Card[] cards) {
		this.cards = cards;
	}
}
