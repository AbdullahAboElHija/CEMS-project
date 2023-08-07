package gui_student;

import java.net.URL;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import entity.LoggedInUser;
import entity.User;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class EnterIDFormBoundary implements Initializable {

	@FXML
	private Button backBtn;

	@FXML
	private TextField idField;

	@FXML
	private Label msgArea;

	@FXML
	private Button startBtn;

	@FXML
	private Button logoutBtn;

	@FXML
	private Label userName;

	@FXML
	private AnchorPane window;

	private User user;

    
    
    /**

    Initializes the EnterIDFormBoundary by retrieving the currently logged-in user's information.

    It sets up the form's components and prepares for user interactions.

    @param location The location used to resolve relative paths for the root object.

    @param resources The resources used to localize the root object.
    */
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// closing window functionality
    	 Platform.runLater(() -> {
    		 Stage stage = (Stage) window.getScene().getWindow();
    	    	stage.setOnCloseRequest(event -> {
    	            event.consume(); // Consume the event to prevent automatic window closing
    	            LoginController.disconnect();
    	            stage.close();
    	        });
         });
    	 
		try {
			user = LoggedInUser.getInstance().getUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			userName.setText("Hi " + LoggedInUser.getInstance().getUser().getFirstName() + " "
					+ LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (LoggedInUser.getHeadDep() != null) {
			logoutBtn.setVisible(false);
		}

	}


	@FXML
	void clickBack(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_student/EnterActivationCodeForm.fxml", "EnterActivationCode",
				event, true);
	}

    /**

    Handles the click event on the start button.

    It validates the entered ID against the user's ID and navigates the user to the next screen, PerformOnlineExam3, if the IDs match.

    Otherwise, it displays an error message indicating that the entered ID doesn't match the user's ID.

    @param event The action event triggered by clicking the start button.
    */
    @FXML
    void clickStart(ActionEvent event) {
    	String idEntered = idField.getText();
    	//check if id field is not empty
    	//trim
    	if(idEntered.trim().length()==0) {
    		showMsg("Enter your ID");
    		return;
    	}
    	if(user.getId().equals(idEntered)) {
    		GuiCommon.getInstance().displayNextScreen("/gui_student/PerformOnlineExam.fxml", "Perform Online Exam", event,true);
    	}
    	else {
    		showMsg("The ID you entered doesnt match your user ID.");
    	}
    	
    }
    
    
    public void showMsg( String msg) {
    	msgArea.setText(msg);
    }

	@FXML
	void clickLogout(ActionEvent event) {
		LoginController.logOut();
		GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
	}

}
