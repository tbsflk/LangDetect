package langdetect;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Profile {
	
	private static final int cutoffPos = 300;
	private static final int maxOoP = 300;
	
	private String name;
	private HashMap<String,NGram> nGramMap;
	private List<NGram> nGramList;
	private boolean finalized;
	
	public Profile(String name) {
		this.name = name;
		this.nGramMap = new HashMap<String,NGram>();
		this.nGramList = null;
		this.finalized = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int addNGram(String ngram) {
		if(this.finalized) {
			throw new IllegalStateException("Profile is already finalized");
		}
		NGram nGramObject = this.nGramMap.get(ngram);
		if(nGramObject != null) {
			nGramObject.incCount();
		} else {
			nGramObject = new NGram(ngram);
			this.nGramMap.put(ngram, nGramObject);
		}
		return nGramObject.getCount();
	}
	
	public NGram getNGram(String ngram) {
		return this.nGramMap.get(ngram);
	}
	
	public void finalize() {
		if(this.finalized) {
			throw new IllegalStateException("Profile is already finalized");
		}
		List<NGram> nGramListSorted = new LinkedList<NGram>(this.nGramMap.values());
		Collections.sort(nGramListSorted);
		int pos = 0;
		for(NGram ngram : nGramListSorted) {
			if(pos < Profile.cutoffPos) {
				ngram.setPos(pos);
			} else {
				this.nGramMap.remove(ngram.getGram());
			}
			pos++;
		}
		if(nGramListSorted.size() > Profile.cutoffPos) {
			this.nGramList = nGramListSorted.subList(0, Profile.cutoffPos);
		} else {
			this.nGramList = nGramListSorted;
		}
		this.finalized = true;
	}
	
	public boolean isFinalized() {
		return this.finalized;
	}
	
	public int getOutOfPlaceMeasure(Profile oP) {
		if(!this.finalized || !oP.isFinalized()) {
			throw new IllegalStateException("Profile is not yet finalized");
		}
		int dist = 0;
		for(NGram ngram : this.nGramList) {
			NGram oNGram = oP.getNGram(ngram.getGram());
			if(oNGram != null) {
				dist += Math.abs(ngram.getPos() - oNGram.getPos());
			} else {
				dist += Profile.maxOoP;
			}
		}
		return dist;
	}
	
	public void readFromFile() {
		if(this.finalized) {
			throw new IllegalStateException("Profile is already finalized");
		}
		
	}
	
	public void saveToFile() {
		if(!this.finalized) {
			throw new IllegalStateException("Profile is not yet finalized");
		}
		
	}
	
	public String toString() {
		return this.getName() + ": " + this.nGramList.toString();
	}

}
