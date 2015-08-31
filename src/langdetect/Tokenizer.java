package langdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tokenizer {
	
	private static final String pad = " ";
	private static final String pattern = "[^\\p{L}]+";

	private int minLen = 1;
	private int maxLen = 5;
	
	public Tokenizer() {
	}
	
	public Tokenizer(int min, int max) {
		this.minLen = min;
		this.maxLen = max;
	}

	public void createProfile(Profile profile, String text) {
		Scanner scanner = new Scanner(text);
		this.createProfile(profile, scanner);
	}

	public void createProfile(Profile profile, File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		this.createProfile(profile, scanner);
	}

	public void createProfile(Profile profile, Scanner scanner) {
		scanner.reset();
		scanner.useDelimiter(pattern);
		this.collectNGrams(profile, scanner);
		scanner.close();
		profile.finalize();
	}

	private void collectNGrams(Profile profile, Scanner scanner) {
		while(scanner.hasNext()) {
			String token = this.prepareToken(scanner.next());
			if(token != null) {
				for(int len = minLen; len <= maxLen && len <= token.length(); len++) {
					int pos = 0;
					while(pos + len <= token.length()) {
						String ngram = token.substring(pos, pos + len);
						if(!ngram.trim().equals("")) {
							profile.addNGram(ngram);
						}
						pos++;
					}
				}
			}
		}
	}
	
	private String prepareToken(String token) {
		token.trim();
		if(token.equals(""))
			return null;
		else {
			return pad + token.toLowerCase() + pad;
		}
	}

}
