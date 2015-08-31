package langdetect;

public class NGram implements Comparable<NGram> {
	
	private int n;
	private String gram;
	private int count;
	private int pos;
	
	public NGram(String ngram) {
		this.gram = ngram;
		this.n = ngram.length();
		this.count = 1;
		this.pos = -1;
	}
	
	public String getGram() {
		return this.gram;
	}
	
	public int getN() {
		return this.n;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public int incCount() {
		return ++this.count;
	}
	
	public int getPos() {
		return this.pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		return this.gram + " (" + this.count + "," + this.pos + ")";
	}

	@Override
	public int compareTo(NGram o) {
		return o.getCount() - this.getCount();
	}

}
