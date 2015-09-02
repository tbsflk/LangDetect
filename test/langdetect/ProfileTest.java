package langdetect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit-Test for profile implementation
 * 
 * @author Tobias Falke
 * 
 */
public class ProfileTest {

	private Profile profile;

	@Before
	public void setUp() {
		this.profile = new Profile("Test");
		this.profile.addNGram("a");
	}

	@Test
	public void shouldNotBeFinalized() {

		Profile newProfile = new Profile("Test");
		assertTrue(!newProfile.isFinalized());

		// should not throw an exception
		this.profile.addNGram("abc");

		// should throw an exception
		try {
			this.profile.getOutOfPlaceMeasure(null);
			fail();
		} catch (IllegalStateException e) {
			// ok
		}
	}

	@Test
	public void shouldBeFinalized() {

		this.profile.setFinalized();
		assertTrue(this.profile.isFinalized());

		// should throw an exception
		try {
			this.profile.addNGram("abc");
			fail();
		} catch (IllegalStateException e) {
			// ok
		}

		// should be possible
		Profile newProfile = new Profile("Test");
		newProfile.addNGram("abc");
		newProfile.setFinalized();
		this.profile.getOutOfPlaceMeasure(newProfile);
	}

	@Test
	public void cannotBeFinalized() {

		Profile newProfile = new Profile("Test");

		// should throw an exception
		try {
			newProfile.setFinalized();
			fail();
		} catch (IllegalStateException e) {
			// ok
		}
	}

	@Test
	public void shouldBeZeroOOP() {

		this.profile.setFinalized();

		Profile newProfile = new Profile("Test");
		newProfile.addNGram("a");
		newProfile.setFinalized();

		assertEquals(this.profile.getOutOfPlaceMeasure(newProfile), 0);
	}

	@Test
	public void shouldBeMaxOOP() {

		// 3 distinct n-grams
		this.profile.addNGram("b");
		this.profile.addNGram("b");
		this.profile.addNGram("c");
		this.profile.setFinalized();

		// only one here, different from the 3 above
		Profile newProfile = new Profile("Test");
		newProfile.addNGram("d");
		newProfile.setFinalized();

		// hence, all 3 cannot be found in the second
		int oOP = 3 * Profile.maxOoP;
		assertEquals(this.profile.getOutOfPlaceMeasure(newProfile), oOP);
	}

	@Test
	public void shouldBeCertainOOP() {

		// first profile
		this.profile.addNGram("a");
		this.profile.addNGram("a");
		this.profile.addNGram("b");
		this.profile.addNGram("b");
		this.profile.addNGram("c");
		this.profile.setFinalized();
		// -> a (3), b (2), c(1)

		// second profile
		Profile newProfile = new Profile("Test");
		newProfile.addNGram("a");
		newProfile.addNGram("b");
		newProfile.addNGram("b");
		newProfile.addNGram("b");
		newProfile.addNGram("c");
		newProfile.addNGram("c");
		newProfile.setFinalized();
		// -> b (3), c (2), a(1)

		// hence, the distance should be 4, as a is 2 positions out of place,
		// and both b and c are 1 out of place
		assertEquals(this.profile.getOutOfPlaceMeasure(newProfile), 4);
	}
}
