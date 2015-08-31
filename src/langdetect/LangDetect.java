package langdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

public class LangDetect {

	public static void main(String[] args) throws FileNotFoundException {

		Tokenizer tok = new Tokenizer(1,3);
		
		Profile english = new Profile("en");
		tok.createProfile(english, new File("data/training/english.txt"));
		System.out.println(english);
		
		Profile german = new Profile("de");
		tok.createProfile(german, new File("data/training/german.txt"));
		System.out.println(german);
		
		Profile french = new Profile("fr");
		tok.createProfile(french, new File("data/training/french.txt"));
		System.out.println(french);
		
		Collection<Profile> profiles = new LinkedList<Profile>();
		profiles.add(english);
		profiles.add(german);
		profiles.add(french);
		
		Scanner input = new Scanner(System.in);
		while(true) {
			
			System.out.println("Query: ");
			String query = input.nextLine();
			if(query.equals("exit")) {
				break;
			} 
			
			Profile pQ = new Profile("");
			tok.createProfile(pQ, query);
			
			int min = Integer.MAX_VALUE;
			Profile best = null;
			for(Profile p : profiles) {
				int ooP = pQ.getOutOfPlaceMeasure(p);
				System.out.println(p.getName() + ": " + ooP);
				if(ooP < min) {
					min = ooP;
					best = p;
				}
			}
			System.out.println("-> " + best.getName());
			System.out.println();
		}
		input.close();

	}

}
