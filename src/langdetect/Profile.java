package langdetect;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a frequency profile of a text. In the initial state,
 * n-grams of a text can be added and their numbers of occurrence will be
 * determined. Once the profile is finalized, the added n-grams are sorted in
 * reverse order of frequency and its position is stored in each n-gram.
 * 
 * @author Tobias Falke
 * 
 */
public class Profile {

	/**
	 * Maximum number of n-grams in a frequency profile
	 */
	private static final int cutOffPos = 300;
	/**
	 * Out of place measure for n-grams not present in the compared profile
	 */
	private static final int maxOoP = 300;

	/**
	 * Name of the profile
	 */
	private String name;
	/**
	 * Map of n-gram objects
	 */
	// to ensure fast access by name in order to increment the counter
	// or to get position
	private HashMap<String, NGram> nGramMap;
	/**
	 * Sorted list of n-gram objects
	 */
	// to iterate for out-of-place calculation or print to screen
	private List<NGram> nGramList;
	/**
	 * State
	 */
	private boolean finalized;

	/**
	 * Creates an empty profile with the given name.
	 * @param name
	 */
	public Profile(String name) {
		this.name = name;
		this.nGramMap = new HashMap<String, NGram>();
		this.nGramList = null;
		this.finalized = false;
	}

	/**
	 * Returns the profile's name.
	 * @return Name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Adds an n-gram to the profile. If the n-gram is already present, its
	 * counter is incremented. If not, it is added to the set of n-grams with a
	 * count of 1. The new number of occurrences of the n-gram is returned.
	 * @param ngram
	 *        n-gram
	 * @return New number of occurrences
	 * @throws IllegalStateException
	 *         if the profile is already finalized
	 */
	public int addNGram(String ngram) throws IllegalStateException {

		if (this.finalized)
			throw new IllegalStateException("Profile is already finalized");

		// find it in the map
		NGram nGramObject = this.nGramMap.get(ngram);
		if (nGramObject != null) {
			// already present -> increment counter
			nGramObject.incCount();
		} else {
			// not yet present -> create and add new object
			nGramObject = new NGram(ngram);
			this.nGramMap.put(ngram, nGramObject);
		}
		return nGramObject.getCount();
	}

	/**
	 * Finalizes the profile. The added n-grams with their current count are
	 * used to calculate the reverse-order frequency profile. The sort-order is
	 * now available, but all n-grams after the cut-off position are lost and no
	 * additional n-grams can be added.
	 */
	@Override
	public void finalize() {

		if (this.finalized)
			return;

		// sort all n-grams
		List<NGram> nGramListSorted = new LinkedList<NGram>(this.nGramMap.values());
		Collections.sort(nGramListSorted);

		// store the positions in the n-grams and remove all n-grams after the
		// cut-off limit from the map
		int pos = 0;
		for (NGram ngram : nGramListSorted) {
			if (pos < Profile.cutOffPos) {
				ngram.setPos(pos);
			} else {
				this.nGramMap.remove(ngram.getGram());
			}
			pos++;
		}

		// in addition to the map, store the sorted list
		if (nGramListSorted.size() > Profile.cutOffPos) {
			this.nGramList = nGramListSorted.subList(0, Profile.cutOffPos);
		} else {
			this.nGramList = nGramListSorted;
		}

		this.finalized = true;
	}

	/**
	 * Returns the current state of the profile.
	 * @return true, if already finalized, false otherwise
	 */
	public boolean isFinalized() {
		return this.finalized;
	}

	/**
	 * Calculates the out-of-place measure for this and another profile.
	 * @param oP
	 *        other profile
	 * @return out-of-place measure
	 * @throws IllegalStateException
	 *         if one of the profiles is not yet finalized
	 */
	public int getOutOfPlaceMeasure(Profile oP) throws IllegalStateException {

		if (!this.finalized || !oP.isFinalized())
			throw new IllegalStateException("Profile is not yet finalized");

		int dist = 0;
		// iterate over this profile's n-grams
		for (NGram ngram : this.nGramList) {
			// get the corresponding n-gram of the other profile
			NGram oNGram = oP.nGramMap.get(ngram.getGram());
			if (oNGram != null) {
				// compare the positions
				dist += Math.abs(ngram.getPos() - oNGram.getPos());
			} else {
				// n-gram not present in other profile -> return max
				dist += Profile.maxOoP;
			}
		}
		return dist;
	}

	/**
	 * Returns a textual representation of the profile.
	 * @return Text
	 */
	@Override
	public String toString() {
		return this.getName() + ": " + this.nGramList.toString();
	}

}
