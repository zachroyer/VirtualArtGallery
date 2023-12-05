package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class to read in our CSV file and create Artist objects for each row
 * @author Zach Royer
 * @author Rafe Danos
 */
public class Gallery {

	public static ArrayList<Artist> getArtists(String filepath) {

		ArrayList<Artist> artists = new ArrayList<Artist>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			// Remove the header
			String line = reader.readLine();
			if (line == null) {
				return artists;
			}

			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				for (int i = 0; i < tokens.length; i++) {
					tokens[i] = tokens[i].trim();
				}

				if (tokens.length == 3) {
					artists.add(new Artist(tokens[0], tokens[1], Boolean.parseBoolean(tokens[2])));
				} else {
					if (tokens.length < 5) {
						artists.add(new Artist(tokens[0], tokens[1], Integer.parseInt(tokens[2]),
								Integer.parseInt(tokens[3])));
					} else {
						artists.add(new Artist(tokens[0], tokens[1], Integer.parseInt(tokens[2]),
								Integer.parseInt(tokens[3]), Arrays.copyOfRange(tokens, 4, tokens.length)));
					}
				}
			}

		} catch (IOException e) {
			System.out.println("A problem occured reading in the Artists.");
			e.printStackTrace();
		}

		return artists;
	}

//	public static void main(String[] args) {
//
//		String fileName = "src/Resources/artists.csv";
//		ArrayList<Artist> artists = getArtists(fileName);
//
//		SymbolDigraph sd = new SymbolDigraph(artists);
//		Iterable<Integer> sdAdj = sd.digraph().adj(0);
//
//		System.out.println(sd.digraph());
//
//		for (Integer w : sdAdj) {
//			System.out.println(w);
//		}
//
//	}

}