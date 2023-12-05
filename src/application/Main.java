package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	private static final int NODE_RADIUS = 70;
	private static final int IMAGE_SIZE = 150;
	private static final double ARROW_SIZE = 20;
	private static final Color LIGHT_BLUE = new Color(62 / 255.0, 151 / 255.0, 233 / 255.0, 0.59);
	private static Color RED_BROWN = new Color(142 / 255.0, 63 / 255.0, 78 / 255.0, 0.8 / 255.0);
	private static Color LIME_GREEN = new Color(83 / 255.0, 188 / 255.0, 22 / 255.0, 0.96 / 255.0);
	private static Color FERN_GREEN = new Color(10 / 255.0, 144 / 255.0, 53 / 255.0, 0.6 / 255.0);
	private Label label;

	public void start(Stage primaryStage) {
		try {

			Group group = new Group();
			// Digraph g = new Digraph(new In("src/Resources/graph.txt"));

			String fileName = "Resources/artists.csv";
			ArrayList<Artist> artists = Gallery.getArtists(fileName);

			SymbolDigraph sd = new SymbolDigraph(artists);

			AnchorPane a = new AnchorPane();

			a.setPrefSize(1000, 1000);

			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setStyle("-fx-background-color:transparent;");
			scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			group.setStyle("-fx-margin: 25;");
			scrollPane.setContent(group);
			AnchorPane.setTopAnchor(scrollPane, 0.0);
			AnchorPane.setRightAnchor(scrollPane, 0.0);
			AnchorPane.setLeftAnchor(scrollPane, 25.0);
			AnchorPane.setBottomAnchor(scrollPane, 120.0); // Set to 20% of the height

			// Create and configure the TextField
			label = new Label("this is a text");
			label.setMinHeight(IMAGE_SIZE / 1.5);
			AnchorPane.setTopAnchor(label, null); // Set to 80% of the height
			AnchorPane.setRightAnchor(label, 10.0);
			AnchorPane.setLeftAnchor(label, 10.0);
			AnchorPane.setBottomAnchor(label, 10.0);

			a.getChildren().addAll(scrollPane, label);

			double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
			double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
			Scene scene = new Scene(a, (2.0 / 3.0) * screenWidth, (2.5 / 3.0) * screenHeight);

			// Set a handler to adjust node positions when the scene is shown
			primaryStage.setOnShown(event -> {
				try {
					Circle[] test = addNodes(group, sd, scene);
					addEdges(group, sd, test);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			// Create a stage and show the scene.
			String css = this.getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);

			primaryStage.setScene(scene);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Circle createNode(double x, double y, double radius, int v) {
		Circle node = new Circle(x, y, radius);
		node.setFill(Color.TRANSPARENT);
		node.setStroke(Color.BLACK);
		return node;
	}

	/*
	 * loops through a graph and puts nodes into gui
	 */
	private Circle[] addNodes(Group group, SymbolDigraph sd, Scene scene) throws Exception {
		// Get the dimensions of the window

		Group mainGroup = new Group();

		double windowWidth = scene.getWidth() - 100;
		double windowHeight = scene.getHeight() - 400;
		final ImageView[] currentlyClickedImageView = { null }; // Keep track of the currently clicked image

		// Get the number of nodes and calculate the number of rows and columns
		int numNodes = sd.digraph().V();
		int numRows = (int) Math.sqrt(numNodes);
		int numCols = numNodes / numRows;

		// Calculate the spacing between nodes
		double horizontalSpacing = windowWidth / (numCols - 1) - 25;
		double verticalSpacing = windowHeight / (numRows - 1) - 25;

		// Initialize array to hold the circles representing nodes
		Circle[] nodes = new Circle[numNodes];

		// Calculate the center of the window
		double centerX = (scene.getWidth() / 2.0);
		double centerY = (scene.getHeight() / 2.0);

		// Loop through rows and columns to place nodes
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				int v = row * numCols + col;
				double totalWidth = numCols * horizontalSpacing;
				double startX = centerX - totalWidth / 2.0 + col * horizontalSpacing + 50;
				// Calculate the position of the node with respect to the center

				double y = centerY + (row - (numRows - 1) / 2.0) * verticalSpacing + 100;

				Circle node = createNode(startX, y, NODE_RADIUS, v);
				Text text = new Text(startX, y, sd.nameOf(v).getName());

				// Check the name of the node and apply a different layout for nodes with a
				// certain name
				if (sd.nameOf(v).isStyle()) {
					// Apply a different layout for nodes with the name "SpecialNode"
					// You can adjust the position and other properties as needed
					double x = startX + col * horizontalSpacing;
					// totalWidth = 4 * horizontalSpacing;

					// startX = centerX - totalWidth / 2.0 + col * horizontalSpacing + 50 ;
					y -= scene.getHeight();
					node = createNode(startX - 170, y + 160, NODE_RADIUS, v);
					text = new Text(startX - 200, y + 260, sd.nameOf(v).getName());
					text.setStyle("-fx-font-size: 20px;");
					text.setTextAlignment(TextAlignment.CENTER);

				} else {
					// Apply the regular layout for other nodes

					// Adjust the position of the text based on the node position

					node = createNode(startX, y, NODE_RADIUS, v);
					text = new Text(startX - NODE_RADIUS, y + 100, sd.nameOf(v).getName());
					text.setStyle("-fx-font-size: 20px;");
					text.setTextAlignment(TextAlignment.CENTER);
				}

				// Adjust image size and positiona

				String imageName = sd.nameOf(v).getImagePath();
				String imageURL = "Resources/" + imageName;
				FileInputStream is = new FileInputStream(imageURL);
				Image image = new Image(is);
				if (sd.nameOf(v).isStyle()) {
					// Use horizontal spacing with 4 columns for positioning

					ImageView imageView = createImageView(startX - 170, y + 160, text, image);
					imageView.setFitHeight(IMAGE_SIZE); // Adjust the size as needed
					imageView.setFitWidth(IMAGE_SIZE);
					// Add click event handler to increase the size on click
					imageView.setOnMouseClicked(event -> {
						if (currentlyClickedImageView[0] != null) {
							// Reset the size of the previously clicked image
							currentlyClickedImageView[0].setFitHeight(IMAGE_SIZE);
							currentlyClickedImageView[0].setFitWidth(IMAGE_SIZE);

						}

						// Increase the size of the currently clicked image

						System.out.println("artist: " + sd.nameOf(v).getName());
						// Update the currently clicked image
						currentlyClickedImageView[0] = imageView;
					});

					group.getChildren().addAll(imageView, text);
					nodes[v] = node;
				} else {
					ImageView imageView = createImageView(startX, y, text, image);
					imageView.setFitHeight(IMAGE_SIZE); // Adjust the size as needed
					imageView.setFitWidth(IMAGE_SIZE);
					imageView.setOnMouseClicked(event -> {
						double enlargementFactor = 1.5;

						if (currentlyClickedImageView[0] != null && currentlyClickedImageView[0] != imageView) {
							// Reset the previously clicked ImageView
							currentlyClickedImageView[0].setFitHeight(IMAGE_SIZE);
							currentlyClickedImageView[0].setFitWidth(IMAGE_SIZE);
							// Re-center the ImageView
							currentlyClickedImageView[0].setLayoutX(currentlyClickedImageView[0].getLayoutX()
									+ IMAGE_SIZE / 2 * (enlargementFactor - 1));
							currentlyClickedImageView[0].setLayoutY(currentlyClickedImageView[0].getLayoutY()
									+ IMAGE_SIZE / 2 * (enlargementFactor - 1));
						}

						if (currentlyClickedImageView[0] != imageView) {
							// Enlarge the currently clicked ImageView
							imageView.setFitHeight(IMAGE_SIZE * enlargementFactor);
							imageView.setFitWidth(IMAGE_SIZE * enlargementFactor);
							// Move it to keep it centered
							imageView.setLayoutX(imageView.getLayoutX() - IMAGE_SIZE / 2 * (enlargementFactor - 1));
							imageView.setLayoutY(imageView.getLayoutY() - IMAGE_SIZE / 2 * (enlargementFactor - 1));

							// Update the currently clicked ImageView
							currentlyClickedImageView[0] = imageView;

//                        imageView.get
						}
					});

					group.getChildren().addAll(imageView, text);
					nodes[v] = node;
				}
			}
		}
			return nodes;
		
	}

	 private ImageView createImageView(double x, double y, Text text, Image image) {
	        ImageView imageView = new ImageView(image);
	        Bounds textBounds = text.getLayoutBounds();

	        double imageCenterX = x;
	        double imageCenterY = y;

	        // Adjust the position of the text and image

	        double imageX = x - IMAGE_SIZE / 2;  // Adjust this as needed
	        double imageY = y - IMAGE_SIZE / 2;  // Adjust this as needed

	        imageView.setFitHeight(IMAGE_SIZE);
	        imageView.setFitWidth(IMAGE_SIZE);
	        imageView.setX(imageX);
	        imageView.setY(imageY);


	        return imageView;
	    }
		
	    private void addEdges(Group group, SymbolDigraph sd, Circle[] nodes) {
	        Digraph g = sd.digraph();
	    
	        Color[] edgeColors = {
	                Color.RED,
	                Color.BLUE,
	                Color.GREEN,
	                Color.ORANGE,
	                Color.MAGENTA,
	                Color.CYAN,
	                Color.YELLOW,
	                Color.PINK,
	                Color.ORANGE,
	                Color.MAGENTA
	            };
	        int colorIndex = 0;
	        double angleIncrement = 2 * Math.PI / g.V();

	        for (int v = 0; v < g.V(); v++) {
	            for (int w : g.adj(v)) {
	                // Check for null nodes[w]
	                if (nodes[v] != null && nodes[w] != null) {
	                    double startX = nodes[v].getCenterX() ;
	                    double startY = nodes[v].getCenterY() ;

	                    double endX = nodes[w].getCenterX() ;
	                    double endY = nodes[w].getCenterY() ;

	                    Line line = new Line(startX, startY, endX, endY);
	                    line.setStroke(edgeColors[colorIndex]);
	                    line.setStrokeWidth(2.5);

	                    colorIndex = (colorIndex + 1) % edgeColors.length;

	                    group.getChildren().add(line);
	                    addArrow(line, endX, endY, startX, startY);
	                }
	            }
	        }
	    }
		
		/**
		 * 
		 * @param line
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * creates an arrow at the end of an edge 
		 */
		public void addArrow(Line line, double x1, double y1, double x2, double y2) {
		     double angle = Math.atan2(y2 - y1, x2 - x1);
		        double sin = Math.sin(angle);
		        double cos = Math.cos(angle);

		        double x3 = x1 + ARROW_SIZE * (cos + sin);
		        double y3 = y1 + ARROW_SIZE * (sin - cos);
		        double x4 = x1 + ARROW_SIZE * (cos - sin);
		        double y4 = y1 + ARROW_SIZE * (sin + cos);

		    // Create arrow polygon with reversed points
		    Polygon arrow = new Polygon(x1, y1, x3, y3, x4, y4);

		    // Set arrow color
		    arrow.setFill(line.getStroke());

		     Parent parent = line.getParent();
		        // Add arrow to the group
		        Group group = (Group) parent;
		        group.getChildren().add(arrow);
		    
		}
}