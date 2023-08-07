package gui;

import java.io.IOException;

import entity.LoggedInUser;
import entity.User;
import gui_lecturer.ActivatedExamsBoundary;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Msg;

public class GuiCommon {//singleton
	private static final GuiCommon guiCommonInstance=new GuiCommon();
	public static GuiCommon getInstance() {
		return guiCommonInstance;
	}
	private GuiCommon() {
	}

	/**
	 * The method loads the desired right screen to which you want to move.
	 * 
	 * @param fxmlName input is the screen Name of the XML file of the screen to
	 *                 which you are moving by loading it
	 *                 
	 *                 returns The controller of screen
	 */
	public Object displayNextScreen(String fxmlName,String title,ActionEvent event, boolean hide) {
		if(hide)
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
		Stage primaryStage = new Stage();
			Pane root;
			try {
				//root = loader.load(getClass().getResource(fxmlName).openStream());
				root = loader.load();
				Scene scene = new Scene(root);
				primaryStage.setTitle(title);
				primaryStage.setScene(scene);		
				primaryStage.show();
			} catch (IOException e) {
				System.out.println("Couldnt load!");
				e.printStackTrace();
			}
			return loader.getController();
			
		}
	

	/**
	 * create a popUp with a given message.
	 * 
	 * @param msg string text input to method to display in popUp message.
	 */
	public static void popUp(String msg) {
		final Stage dialog = new Stage();
		VBox dialogVbox = new VBox(20);
		Label lbl = new Label(msg);
		lbl.setPadding(new Insets(15));
		lbl.setAlignment(Pos.CENTER);
		lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		dialogVbox.getChildren().add(lbl);
		Scene dialogScene = new Scene(dialogVbox, lbl.getMinWidth(), lbl.getMinHeight());
		dialog.setScene(dialogScene);
		dialog.show();
	}
	

		
}	
		



