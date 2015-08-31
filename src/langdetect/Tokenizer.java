package langdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class implements a tokenizer that splits a given text into n-grams. The
 * n-grams are stored in a profile that has to be passed to the tokenizer. After
 * adding all n-grams, the profile is finalized and represents the text's
 * frequency profile.
 * 
 * @author Tobias Falke
 * 
 */
public class Tokenizer {

	// space character for token padding
	private static final String pad = " ";
	// regex pattern: everything but unicode letters
	private static final String pattern = "[^\\p{L}]+";

	/**
	 * Minimum length of created n-grams
	 */
	private int minLen = 1;
	/**
	 * Maximum length of created n-grams
	 */
	private int maxLen = 5;

	/**
	 * Creates a tokenizer with default settings.
	 */
	public Tokenizer() {
	}

	/**
	 * Creates a tokenizer with the given setting.
	 * @param min
	 *        Minimum length of n-grams
	 * @param max
	 *        Maximum length of n-grams
	 */
	public Tokenizer(int min, int max) {
		this.minLen = min;
		this.maxLen = max;
	}

	/**
	 * Computes the frequency profile for the given text.
	 * @param profile
	 *        Empty profile
	 * @param text
	 *        Text
	 */
	public void computeProfile(Profile profile, String text) {
		Scanner scanner = new Scanner(text);
		this.computeProfile(profile, scanner);
	}

	/**
	 * Computes the frequency profile for a text stored in a file.
	 * @param profile
	 *        Empty profile
	 * @param file
	 *        File with text
	 */
	public void computeProfile(Profile profile, File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		this.computeProfile(profile, scanner);
	}

	/**
	 * Computes the frequency profile for a text given as a scanner.
	 * @param profile
	 *        Empty profile
	 * @param scanner
	 *        Text scanner
	 */
	private void computeProfile(Profile profile, Scanner scanner) {

		// initialize the scanner
		scanner.reset();
		scanner.useDelimiter(pattern); // every non-letter delimits a token

		// collect all n-grams
		this.collectNGrams(profile, scanner);
		scanner.close();

		// finalize
		profile.finalize();
	}

	/**
	 * Creates all n-grams for a text and stores them into the given profile.
	 * @param profile
	 *        Profile
	 * @param scanner
	 *        Text scanner
	 */
	private void collectNGrams(Profile profile, Scanner scanner) {

		// handle all token
		while (scanner.hasNext()) {

			// preprocess the token
			String token = this.prepareToken(scanner.next());
			if (token != null) {

				// for each length n within the limits
				for (int len = this.minLen; len <= this.maxLen && len <= token.length(); len++) {
					int pos = 0;
					// create all n-grams of the token
					while (pos + len <= token.length()) {
						String ngram = token.substring(pos, pos + len);
						// and add them to the profile
						if (!ngram.trim().equals("")) {
							profile.addNGram(ngram);
						}
						pos++;
					}
				}

			}

		}
	}

	/**
	 * Returns the given token in lower-case with padding.
	 * @param token
	 *        Raw token
	 * @return null, if the token is only whitespace, otherwise, the prepared
	 *         token
	 */
	private String prepareToken(String token) {

		// remove whitespace and upper-case letters
		token = token.trim().toLowerCase();

		// check for at least one letter
		if (!token.equals(""))
			// return with padding
			return pad + token.toLowerCase() + pad;
		else
			return null;

	}

}
