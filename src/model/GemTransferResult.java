package model;

public enum GemTransferResult {
	/**
	 * Transferring succeeded
	 */
	SUCCESS, 
	/**
	 * Transferring didn't succeed
	 */
	FAIL, 
	/**
	 * Transferring succeeded but the receiver has now more gems than allowed.
	 */
	RECEIVER_NOT_ENOUGH_CAPACITY
}
