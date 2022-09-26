package seamcarve;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import support.seamcarve.*;

/**
 *
 * This is the support code for the set-up of images that students can
 * run a version of seamcarve on.
 *
 * @author Surbhi Madan (sm15)
 * @version 01/17/19
 */
public class App extends Application {

	// Had to make this an instance variable to be able to reset it in the File Chooser handler
	private PicturePane _picturePane;

	public static void brownIDGen(int n) {
		int[] array = new int[n];
		for (int index = 0; index < array.length; index++) {
			int random = (int)(Math.random() * n);
			array[random]++;
		}
		int tracker = 0;
		for (int num : array) {
			if (num > tracker) tracker = num;
		}
		System.out.println("n: " + n + "\tlargest entry in B: " + tracker);
	}

	public static void brownIDGen2(int n) {
		int[] array = new int[n];
		for (int index = 0; index < array.length; index++) {
			int random1 = (int)(Math.random() * n);
			int random2 = (int)(Math.random() * n);
			if (array[random1] < array[random2]) array[random1]++;
			else array[random2]++;
		}
		int tracker = 0;
		for (int num : array) {
			if (num > tracker) tracker = num;
		}
		System.out.println("n: " + n + "\tlargest entry in B: " + tracker);
	}
	public static void main(String[] argv) {
	for (int n = 2; n < 1000000000; n*= 2) {
		brownIDGen2(n);
	}

//		launch(argv);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Default filename to start off with when user has not chosen any other image file
 		String defaultFilename = "src/seamcarve/seamcarve-images/blobfish.jpg";

		// Set up the root border pane and pass it to an instance of PicturePane to set up the images
		BorderPane pane = new BorderPane();
		_picturePane = new MyPicturePane(pane, defaultFilename);
		System.out.println(new File("").getAbsolutePath());

		// Add the file chooser to the root pane and set handler on it
		// I chose to add the File button here rather than within the PicturePane class
		// because a new PicturePane instance is instantiated if the user picks a different image file
		// and the button should still remain as is.
		HBox menuPane = new HBox();
		Button fileButton = new Button("File");
		final FileChooser chooser = new FileChooser();
		fileButton.setOnAction(new EventHandler<ActionEvent>() {

			// This handler instantiates a new instance of PicturePane if an image is selected
			// and does nothing if no image is selected (ex: if a user hits cancel on the file chooser)
			@Override
			public void handle(ActionEvent event) {
				File file = chooser.showOpenDialog(stage);
                if (file != null) {
					_picturePane = new MyPicturePane(pane, file.getAbsolutePath());
				}
            }
		});

		// Set the file button menu to the root pane
		menuPane.getChildren().add(fileButton);
		pane.setTop(menuPane);

		// Create the scene and set the stage to make everything show up
		Scene scene = new Scene(pane, 850, 700);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setTitle("Seamcarve!");
		stage.show();
	}
}
