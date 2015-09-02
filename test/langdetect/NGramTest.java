package langdetect;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit-Test for n-gram implementation
 * 
 * @author Tobias Falke
 * 
 */
public class NGramTest {

	private NGram ngram;
	private final String gram = "test";

	@Before
	public void setUp() {
		this.ngram = new NGram(this.gram);
	}

	@Test
	public void gramShouldBeSet() {
		assertEquals(this.ngram.getGram(), this.gram);
	}

	@Test
	public void defaultsShouldBeSet() {
		// new instance (to avoid dependencies)
		NGram newNGram = new NGram(this.gram);
		assertEquals(newNGram.getCount(), 1);
		assertEquals(newNGram.getPos(), -1);
	}

	@Test
	public void countShouldIncrement() {
		int count = this.ngram.getCount();
		this.ngram.incCount();
		assertEquals(this.ngram.getCount(), count + 1);
	}

	@Test
	public void shouldBeLess() {
		NGram ngram1 = new NGram(this.gram);
		ngram1.incCount();
		ngram1.incCount();
		ngram1.incCount();
		NGram ngram2 = new NGram(this.gram);
		assertEquals(ngram1.compareTo(ngram2), -1);
	}

	@Test
	public void shouldBeMore() {
		NGram ngram1 = new NGram(this.gram);
		NGram ngram2 = new NGram(this.gram);
		ngram2.incCount();
		ngram2.incCount();
		assertEquals(ngram1.compareTo(ngram2), 1);
	}

	@Test
	public void shouldBeSame() {
		NGram ngram1 = new NGram(this.gram);
		NGram ngram2 = new NGram(this.gram);
		assertEquals(ngram1.compareTo(ngram2), 0);
	}
}
