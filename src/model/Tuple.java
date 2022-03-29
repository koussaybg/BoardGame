package model;

public class Tuple<T1, T2> {

	T1 firstValue;
	T2 secondValue;
	
	/**
	 * Constructor for the Tupel 
	 */

	public Tuple() {
		super();
	}
	
	/**
	 * Constructor for the Tupel with given Values 
	 * @param firstValue
	 * @param secondValue
	 */

	public Tuple(T1 firstValue, T2 secondValue) {
		super();
		this.firstValue = firstValue;
		this.secondValue = secondValue;
	}

	/**
	 * @return the firstValue
	 */
	public T1 getFirstValue() {
		return firstValue;
	}

	/**
	 * @param firstValue the firstValue to set
	 */
	public void setFirstValue(T1 firstValue) {
		this.firstValue = firstValue;
	}

	/**
	 * @return the secondValue
	 */
	public T2 getSecondValue() {
		return secondValue;
	}

	/**
	 * @param secondValue the secondValue to set
	 */
	public void setSecondValue(T2 secondValue) {
		this.secondValue = secondValue;
	}

}
