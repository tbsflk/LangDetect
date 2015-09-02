package langdetect;

/**
 * This class represents a n-gram, which is a n-character slice of a text. It
 * stores the characters defining the n-gram and two integers indicating the
 * number of occurrences of the n-gram in a text and its position in the text's
 * frequency profile.
 * 
 * @author Tobias Falke
 * 
 */
public class NGram implements Comparable<NGram> {

	/**
	 * Characters of the n-gram
	 */
	private String gram;
	/**
	 * Number of occurrences in a text
	 */
	private int count;
	/**
	 * Position in the frequency profile.
	 */
	private int pos;

	/**
	 * Creates a new n-gram for the given string of characters. The count is
	 * initialized to 1 and now position is stored yet (-1).
	 * @param ngram
	 * @throws IllegalArgumentException
	 *         if no non-empty string is given
	 */
	public NGram(String ngram) throws IllegalArgumentException {

		if (ngram == null || ngram.length() == 0)
			throw new IllegalArgumentException("Gram must be specified");

		this.gram = ngram;
		this.count = 1;
		this.pos = -1;

	}

	/**
	 * Returns the string of characters.
	 * @return Characters
	 */
	public String getGram() {
		return this.gram;
	}

	/**
	 * Returns the number of occurrences.
	 * @return Number of occurrences
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * Increments the number of occurrences by one.
	 * @return New number of occurrences
	 */
	public int incCount() {
		return ++this.count;
	}

	/**
	 * Returns the position in the frequency profile.
	 * @return Position
	 */
	public int getPos() {
		return this.pos;
	}

	/**
	 * Sets the position in the frequency profile.
	 * @param pos
	 *        Position
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * Returns a textual representation of the n-gram.
	 * @return Text
	 */
	@Override
	public String toString() {
		return this.gram + " (" + this.count + "," + this.pos + ")";
	}

	/**
	 * Compares this n-gram to another n-gram. Returns -1, if this n-gram has a
	 * higher number of occurrences, 1, if the other has a higher number, and 0,
	 * if both have the same. Thus, with this comparison, n-grams can be sorted
	 * in reverse-order of occurrence.
	 */
	@Override
	public int compareTo(NGram o) {
		if (o == null)
			return -1;
		else {
			int diff = o.getCount() - this.getCount();
			if (diff < 0)
				return -1;
			else if (diff > 0)
				return 1;
			else
				return 0;
		}
	}

}
