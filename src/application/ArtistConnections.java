package application;

/**
 * Represents encapsulating 'connection' data for a desired artist. Use this
 * class in tandem with a GUI to grab and isolate connection data from the
 * underlying graph
 * 
 * @author Zach Royer
 */
public class ArtistConnections {
	private final Iterable<Artist> influencedArtists;
	private final Iterable<Artist> indirectInfluences;

	public ArtistConnections(Iterable<Artist> influenced, Iterable<Artist> indirect) {
		this.influencedArtists = influenced;
		this.indirectInfluences = indirect;
	}

	public Iterable<Artist> getInfluencedArtists() {
		return influencedArtists;
	}

}
