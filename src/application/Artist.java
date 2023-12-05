package application;

/**
 * Class to represent artists, including their name, years of birth and death,
 * an image path file to showcase an artpiece, and an array of other artists
 * that artist has influenced
 * 
 * @author Zach Royer
 * @author Rafe Danos
 */
public class Artist {
	private String name, imagePath;
	private int yearBorn, yearDied;
	private String[] influences = {};
	private boolean isStyle;

	public Artist(String name, String imagePath, int yearBorn, int yearDied, String[] influences) {
		this.name = name;
		this.imagePath = imagePath;
		this.yearBorn = yearBorn;
		this.yearDied = yearDied;
		this.influences = influences;
	}

	// Overloaded Constructor for Artists who don't directly influences other
	// artists
	public Artist(String name, String imagePath, int yearBorn, int yearDied) {
		this.name = name;
		this.imagePath = imagePath;
		this.yearBorn = yearBorn;
		this.yearDied = yearDied;
	}

	// Overloaded Constructor for Styles, which will be represented as Artists
	public Artist(String name, String imagePath, Boolean isStyle) {
		this.name = name;
		this.isStyle = isStyle;
	}

	public String getName() {
		return name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public int getYearBorn() {
		return yearBorn;
	}

	public int getYearDied() {
		return yearDied;
	}

	public String[] getInfluences() {
		return influences;
	}

	public boolean isStyle() {
		return isStyle;
	}
}
