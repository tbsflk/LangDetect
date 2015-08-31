package langdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * This class implements a language detector. It can identify the language of
 * inputs entered on the command line based on the comparison to n-gram
 * frequency profile of reference texts for different languages. This approach
 * was first described in Cavnar, W., Trenkle, J.: N-Gram-Based Text
 * Categorization.
 * 
 * @author Tobias Falke
 * 
 */
public class LangDetect {

	/**
	 * Folder containing training texts for languages
	 */
	private static final String trainingFolder = "data/training";

	/**
	 * Tokenizer to create n-grams
	 */
	private Tokenizer tok;
	/**
	 * Frequency profiles of known languages
	 */
	private Collection<Profile> langProfiles;
	/**
	 * Command line input
	 */
	private Scanner input;

	/**
	 * Creates a new language detector that is initialized by reading the
	 * provided training texts.
	 */
	public LangDetect(String trainingFolder) {

		this.tok = new Tokenizer();

		System.out.println("Initializing...");
		String langIds = this.initProfiles(new File(trainingFolder));
		System.out.println("Available languages: " + langIds);

	}

	/**
	 * Instantiates a new language detector and starts a query session.
	 * @param args
	 *        as a parameter, a folder containing the training texts can be
	 *        passed
	 */
	public static void main(String[] args) {

		// get training folder
		String trainingFolder = null;
		if (args.length > 0) {
			trainingFolder = args[0];
		} else {
			trainingFolder = LangDetect.trainingFolder;
		}

		// initialize
		LangDetect detector = new LangDetect(trainingFolder);
		detector.startQuerySession();

	}

	/**
	 * Starts a query session in the command line.
	 */
	public void startQuerySession() {

		String query;
		while ((query = this.getQuery()) != null) {

			Profile queryProfile = new Profile("");
			this.tok.computeProfile(queryProfile, query);

			this.findBestProfile(queryProfile);
		}

		this.input.close();
	}

	/**
	 * Reads training texts from the given folder and calculates the
	 * corresponding frequency profiles.
	 * @param folder
	 *        Folder with training texts
	 * @return List of available profiles
	 */
	private String initProfiles(File folder) {

		this.langProfiles = new ArrayList<Profile>();
		String langIds = "";

		// process each file in the folder
		for (File file : folder.listFiles()) {

			// derive name from file name
			String langId = file.getName().substring(0, 2);
			langIds += langId + ", ";

			// compute the profile
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

	/**
	 * Reads the next query string from the command line.
	 * @return null, if the session was aborted typing "exit", otherwise, the
	 *         query
	 */
	private String getQuery() {

		// initialize if necessary
		if (this.input == null) {
			this.input = new Scanner(System.in);
		}

		// read next line
		System.out.println(System.lineSeparator() + "Query: ");
		String query = this.input.nextLine();

		// handle exit
		if (query.equals("exit"))
			return null;
		else
			return query;

	}

	/**
	 * Calculates out-of-place measures with all language profiles and returns
	 * the closest profile.
	 * @param queryProfile
	 *        Profile of query text
	 * @return Closest language profile
	 */
	private Profile findBestProfile(Profile queryProfile) {

		// calculate distance for each language profile
		java.util.List<ProfileDistance> distances = new LinkedList<ProfileDistance>();
		for (Profile p : this.langProfiles) {
			ProfileDistance dist = new ProfileDistance();
			dist.profile = p;
			dist.outOfPlace = queryProfile.getOutOfPlaceMeasure(p);
			distances.add(dist);
		}

		// sort by distance
		Collections.sort(distances);

		// print best matches
		int max = this.langProfiles.size() < 3 ? this.langProfiles.size() : 3;
		for (int i = 0; i < max; i++) {
			ProfileDistance dist = distances.get(i);
			System.out.print(" " + (i + 1) + ". ");
			System.out.print(dist.profile.getName() + " (");
			System.out.println(dist.outOfPlace + ")");
		}

		return distances.get(0).profile;
	}

	/**
	 * This class is a simple data structure that represents a profile together
	 * with its out-of-place measure to another profile.
	 * 
	 * @author Tobias Falke
	 * 
	 */
	private class ProfileDistance implements Comparable<ProfileDistance> {

		public Profile profile;
		public int outOfPlace;

		@Override
		public int compareTo(ProfileDistance o) {
			return this.outOfPlace - o.outOfPlace;
		}
	}
}