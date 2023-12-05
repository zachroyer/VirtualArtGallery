package application;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;

/**
 * The {@code SymbolDigraph} class represents a digraph, where the vertex names
 * are arbitrary strings. By providing mappings between string vertex names and
 * integers, it serves as a wrapper around the {@link Digraph} data type, which
 * assumes the vertex names are integers between 0 and <em>V</em> - 1. This
 * implementation uses an {@link ST} to map from strings to Integers, an array
 * to map from integers to Artist Objects, and a {@link Digraph} to store the
 * underlying graph. The <em>indexOf</em> and <em>contains</em> operations take
 * time proportional to log <em>V</em>, where <em>V</em> is the number of
 * vertices. The <em>nameOf</em> operation takes constant time.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Zach Royer
 */
public class SymbolDigraph {
	private ST<String, Integer> st; // Artist name -> index
	private Artist[] keys; // index -> Artist object
	private Digraph graph; // the underlying digraph

	/**
	 * Initializes a digraph from an Artists object arraylist. Each line in the file
	 * contains the name of a vertex, followed by a list of the names of the
	 * vertices adjacent to that vertex, separated by the delimiter.
	 * 
	 * @param filename  the name of the file
	 * @param delimiter the delimiter between fields
	 */
	public SymbolDigraph(ArrayList<Artist> artists) {
		st = new ST<String, Integer>();

		// First pass: Assign each artist a unique integer index
		for (Artist artist : artists) {
			if (!st.contains(artist.getName())) {
				st.put(artist.getName(), st.size());
			}
		}

		// Initialize the array of keys
		keys = new Artist[st.size()];
		for (Artist artist : artists) {
			keys[st.get(artist.getName())] = artist;
		}

		// Initialize the digraph
		graph = new Digraph(st.size());

		// Second pass: Add edges based on influences
		for (Artist artist : artists) {
			int artistIndex = st.get(artist.getName());
			for (String influence : artist.getInfluences()) {

				if (st.contains(influence)) {
					int influenceIndex = st.get(influence);
					graph.addEdge(artistIndex, influenceIndex);
				}
			}
		}
	}

	/**
	 * Returns an Iterable of strings containing all artists to which the parameter
	 * has indirectly influenced. This list includes all artists which the parameter
	 * artist has a path on the graph to, except for its most adjacent vertices.
	 * 
	 * @param artist to find the indirect influences
	 * @return {@code Iterable} of strings containing indirect influences
	 */
	public Iterable<Artist> indirectInfluences(Artist artist) {
		// Checks if artist is contained in the Symbol Table
		// Returns an empty array if it is not contained
		if (!st.contains(artist.getName())) {
			return new ArrayList<>();
		}

		ArrayList<Artist> indirectInfluences = new ArrayList<Artist>();
		int artistIndex = indexOf(artist.getName());
		DepthFirstDirectedPaths dfds = new DepthFirstDirectedPaths(graph, artistIndex);
		ArrayList<Integer> directNeighbors = new ArrayList<>();
		for (int adj : graph.adj(artistIndex)) {
			directNeighbors.add(adj);
		}
		/*
		 * Adds every vertex which artistIndex has an indirect influence
		 */
		for (int v = 0; v < graph.V(); v++) {
			if (v != artistIndex && dfds.hasPathTo(v)) {
				if (!directNeighbors.contains(v)) {
					indirectInfluences.add(nameOf(v));
				}
			}
		}
		return indirectInfluences;
	}

	/**
	 * Does the digraph contain the vertex named {@code s}?
	 * 
	 * @param s the name of a vertex
	 * @return {@code true} if {@code s} is the name of a vertex, and {@code false}
	 *         otherwise
	 */
	public boolean contains(String s) {
		return st.contains(s);
	}

	/**
	 * Returns the integer associated with the vertex named {@code s}.
	 * 
	 * @param s the name of a vertex
	 * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex
	 *         named {@code s}
	 */
	public int indexOf(String s) {
		return st.get(s);
	}

	/**
	 * Returns the name of the vertex associated with the integer {@code v}.
	 * 
	 * @param v the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
	 * @return the name of the vertex associated with the integer {@code v}
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public Artist nameOf(int v) {
		validateVertex(v);
		return keys[v];
	}

	/**
	 * Returns the digraph associated with the symbol graph. It is the client's
	 * responsibility not to mutate the digraph.
	 *
	 * @return the digraph associated with the symbol digraph
	 */
	public Digraph digraph() {
		return graph;
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) {
		int V = graph.V();
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
	}

	public ArtistConnections getArtistConnections(String artistName) {
		int artistIndex = indexOf(artistName);
		Iterable<Integer> influencedIndices = graph.adj(artistIndex);
		ArrayList<Artist> influencedArtists = new ArrayList<>();
		for (int index : influencedIndices) {
			influencedArtists.add(nameOf(index));
		}

		Iterable<Artist> influencingArtists = indirectInfluences(keys[indexOf(artistName)]);

		return new ArtistConnections(influencedArtists, influencingArtists);
	}
}