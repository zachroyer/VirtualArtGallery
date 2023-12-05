package application;
	
import java.io.FileInputStream;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.SymbolDigraph;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Main extends Application {
	  private static final int NODE_RADIUS = 70;
	    private static final int IMAGE_SIZE = 100;
	    private static final double ARROW_SIZE = 20;
	public void start(Stage primaryStage) {
		try {


			Group group = new Group();
			//Digraph g = new Digraph(new In("src/Resources/graph.txt"));
			
			
			String fileName = "src/Resources/artists.csv";
			ArrayList<Artist> artists = Gallery.getArtists(fileName);

	
			SymbolDigraph sd = new SymbolDigraph(artists);
		
			
			
			  
			  ScrollPane scrollPane = new ScrollPane();

		        scrollPane.setContent(group);
		        
		        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		        Scene scene = new Scene(scrollPane, (2.0/3.0) * screenWidth, (2.5/3.0) * screenHeight);
		        
			
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
            primaryStage.setScene(scene);
            
            primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    private Circle createNode(double x, double y, double radius, int v) {
        Circle node = new Circle(x, y, radius);
        Text text = new Text(x, y, Integer.toString(v));
        text.setFill(Color.RED);
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
    	
        double windowWidth = scene.getWidth() - 100  ;
        double windowHeight = scene.getHeight() - 400 ;
        final ImageView[] currentlyClickedImageView = {null}; // Keep track of the currently clicked image

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
        double centerX = (scene.getWidth() / 2.0) ;
        double centerY = (scene.getHeight() / 2.0);


        
        // Loop through rows and columns to place nodes
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int v = row * numCols + col;
                double totalWidth = numCols * horizontalSpacing;
                double startX = centerX - totalWidth / 2.0 + col * horizontalSpacing + 50 ;
                // Calculate the position of the node with respect to the center
               
                double y = centerY + (row - (numRows - 1) / 2.0) * verticalSpacing + 100 + 300;

                Circle node = createNode(startX, y, NODE_RADIUS, v);
                Text text = new Text(startX, y, sd.nameOf(v).getName());
                
                
                
                // Check the name of the node and apply a different layout for nodes with a certain name
                if (sd.nameOf(v).getIsStyle()) {
                    // Apply a different layout for nodes with the name "SpecialNode"
                    // You can adjust the position and other properties as needed
                   double x = startX + col * horizontalSpacing;
                   totalWidth = 4 * horizontalSpacing;
                   startX = centerX - totalWidth / 2.0 + col * horizontalSpacing + 50 ;
                    y-= scene.getHeight() ;
                    node = createNode(startX, y, NODE_RADIUS, v);
                    text = new Text(x - NODE_RADIUS, y + NODE_RADIUS + 15, sd.nameOf(v).getName());
                 
                } else {
                    // Apply the regular layout for other nodes
                    node = createNode(startX, y, NODE_RADIUS, v);
                    text = new Text(startX - NODE_RADIUS , y + NODE_RADIUS + 85, sd.nameOf(v).getName());
                }

                // Adjust image size and position
                String imageName = sd.nameOf(v).getImagePath();
                String imageURL = "src/img/" + imageName;
                FileInputStream is = new FileInputStream(imageURL);
                Image image = new Image(is);
                ImageView imageView = createImageView(startX, y, text, image);
                imageView.setFitHeight(2 * IMAGE_SIZE);  // Adjust the size as needed
                imageView.setFitWidth(2 * IMAGE_SIZE);

                // Add click event handler to increase the size on click
                imageView.setOnMouseClicked(event -> {
                    if (currentlyClickedImageView[0] != null) {
                        // Reset the size of the previously clicked image
                        currentlyClickedImageView[0].setFitHeight(2 *IMAGE_SIZE);
                        currentlyClickedImageView[0].setFitWidth(2 *IMAGE_SIZE);
                    }

                    // Increase the size of the currently clicked image
                    double currentHeight = imageView.getFitHeight();
                    double currentWidth = imageView.getFitWidth();
                    imageView.setFitHeight(currentHeight * 1.2);
                    imageView.setFitWidth(currentWidth * 1.2);

                    // Update the currently clicked image
                    currentlyClickedImageView[0] = imageView;
                });

                
                group.getChildren().addAll(imageView, text);
                nodes[v] = node;
            }
        }

        return nodes;
    }
    private ImageView createImageView(double x, double y, Text text, Image image) {
        ImageView imageView = new ImageView(image);
        Bounds textBounds = text.getLayoutBounds();
        imageView.setFitHeight(IMAGE_SIZE);
        imageView.setFitWidth(IMAGE_SIZE);
        double imageCenterX = x - textBounds.getWidth() / 2 - imageView.getFitWidth() / 2;
        double imageCenterY = y - textBounds.getHeight() / 2 - imageView.getFitHeight() / 2;
        imageView.setX(imageCenterX);
        imageView.setY(imageCenterY);
        return imageView;
    }
	
    private void addEdges(Group group, SymbolDigraph sd, Circle[] nodes) {
        Digraph g = sd.digraph();
        Color[] edgeColors = {Color.RED, Color.BLUE, Color.GREEN, Color.BLACK};
        int colorIndex = 0;
        double angleIncrement = 2 * Math.PI / g.V();

        for (int v = 0; v < g.V(); v++) {
            for (int w : g.adj(v)) {
                // Check for null nodes[w]
                if (nodes[v] != null && nodes[w] != null) {
                    double startX = nodes[v].getCenterX() - (nodes[v].getRadius() * Math.cos(angleIncrement * v));
                    double startY = nodes[v].getCenterY() - (nodes[v].getRadius() * Math.sin(angleIncrement * v));

                    double endX = nodes[w].getCenterX() - (nodes[w].getRadius() * Math.cos(angleIncrement * w));
                    double endY = nodes[w].getCenterY() - (nodes[w].getRadius() * Math.sin(angleIncrement * w));

                    Line line = new Line(startX, startY, endX, endY);
                    line.setStroke(edgeColors[colorIndex]);

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
