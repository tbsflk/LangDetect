package langdetect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit-Test for tokenizer implementation
 * 
 * @author Tobias Falke
 * 
 */
public class TokenizerTest {

	private Tokenizer tok;
	private Profile profile;

	@Before
	public void setUp() {
		this.profile = new Profile("");
	}

	@Test
	public void shouldFailDueToNoNGrams() {

		this.tok = new Tokenizer();

		// should throw an exception -> no n-gram
		try {
			this.tok.computeProfile(this.profile, "");
			fail();
		} catch (IllegalStateException e) {
			// ok
		}

	}

	@Test
	public void shouldFind4NGrams() {

		this.tok = new Tokenizer();
		this.tok.computeProfile(this.profile, "a");

		// 4 n-grams expected ('a',' a','a ',' a ')
		assertEquals(this.profile.getNumberOfNGrams(), 4);

	}

	@Test
	public void shouldFindOnly4NGrams() {

		this.tok = new Tokenizer();
		this.tok.computeProfile(this.profile, "a A a");

		// 4 n-grams expected ('a',' a','a ',' a ')
		assertEquals(this.profile.getNumberOfNGrams(), 4);

	}

	@Test
	public void shouldFind21NGrams() {

		this.tok = new Tokenizer();
		this.tok.computeProfile(this.profile, "Hi you");

		// 21 n-grams expected
		// ('h','i',' h','hi','i ',' hi','hi ',' hi ')
		// ('y','o','u',' y','yo','ou','u ',' yo','you','ou ',
		// ' you','you ',' you ')
		assertEquals(this.profile.getNumberOfNGrams(), 21);

	}

	@Test
	public void shouldIgnoreNonLetters() {

		this.tok = new Tokenizer();
		this.tok.computeProfile(this.profile, "a )! 2190 $%");

		// 4 n-grams expected ('a',' a','a ',' a ')
		assertEquals(this.profile.getNumberOfNGrams(), 4);

	}
}
