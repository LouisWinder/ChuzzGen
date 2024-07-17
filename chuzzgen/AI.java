package chuzzgen;

public class AI extends Player {
	public AI(String colour) {
		super("Jeff the Chess Bot", "AI", colour);
	}
	
	/**
	 * Calculate the best move to make
	 * @return The best square to move to
	 */
	public Square determineMove() {
		int depth = 1;
		int evaluation = minimax(depth, false);
		return null;
	}
	
	public int minimax(int depth, boolean isMax) {
		return 1;
	}
}
