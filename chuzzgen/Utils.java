/**
 * Class containing useful utility functions.
 */

package chuzzgen;

public class Utils {
	/**
	 * Randomly sample integer values given a list of probabilities for each.
	 * @param values The integer values to be sampled.
	 * @param weights The weighted chance for each value to be chosen.
	 * @return The value that was chosen.
	 */
	public static int randomWeightedChoice(int[] values, double[] weights) {
		// Generate random number between 0 and 1
		double random = Math.random();
		// Choose a random value
		for (int i = 0; i < weights.length; i++) {
			random -= weights[i];
			if (random <= 0) {
				return values[i];
			}
		}
		throw new RuntimeException("Something went wrong.");
	}
	
	/**
	 * Reverse a String (re-writes it back-to-front).
	 * @param toReverse The String to be reversed.
	 * @return The reversed String.
	 */
	public static String reverseString(String toReverse) {
		String reversed = "";
		int stringLength = toReverse.length();
		for (int i = 1; i < stringLength + 1; i++) {
			reversed += toReverse.charAt(stringLength - i);
		}
		return reversed;
	}
}
