package langdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * 
 * 
 * @author Tobias Falke
 * 
 */
public class LangDetect {

	private final String trainingFolder = "data/training";

	private Tokenizer tok;
	private Collection<Profile> langProfiles;
	private Scanner input;

	public LangDetect() {
		this.tok = new Tokenizer();
		System.out.println("Initializing...");
		String langIds = this.initProfiles(new File(this.trainingFolder));
		System.out.println("Available languages: " + langIds);
	}

	public static void main(String[] args) {
		LangDetect detector = new LangDetect();
		detector.startQuerySession();
	}

	public void startQuerySession() {

		String query;
		while ((query = this.getQuery()) != null) {

			Profile queryProfile = new Profile("");
			this.tok.computeProfile(queryProfile, query);

			Profile bestLangProfile = this.findBestProfile(queryProfile);
			System.out.println(" -> " + bestLangProfile.getName());
		}

		this.input.close();
	}

	private String initProfiles(File folder) {

		this.langProfiles = new ArrayList<Profile>();
		String langIds = "";

		for (File file : folder.listFiles()) {

			String langId = file.getName().substring(0, 2);
			langIds += langId + ", ";

			Profile profile = new Profile(langId);
			try {
				this.tok.computeProfile(profile, file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			this.langProfiles.add(profile);

		}

		return langIds.substring(0, langIds.length() - 2);
	}

	private String getQuery() {

		if (this.input == null) {
			this.input = new Scanner(System.in);
		}

		System.out.println(System.lineSeparator() + "Query: ");
		String query = this.input.nextLine();

		if (query.equals("exit"))
			return null;
		else
			return query;

	}

	private Profile findBestProfile(Profile queryProfile) {

		int min = Integer.MAX_VALUE;
		Profile best = null;

		for (Profile p : this.langProfiles) {
			int ooP = queryProfile.getOutOfPlaceMeasure(p);
			if (ooP < min) {
				min = ooP;
				best = p;
			}
		}

		return best;
	}
}