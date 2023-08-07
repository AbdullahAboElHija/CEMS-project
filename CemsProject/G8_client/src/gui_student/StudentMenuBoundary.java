package gui_student;
import java.net.URL;
import java.util.ResourceBundle;

import control_common.LoginController;
import entity.LoggedInUser;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import entity.LoggedInUser; 


public class StudentMenuBoundary implements Initializable {
	
    @FXML
    private Label userName;
    
    @FXML
    private AnchorPane window;

    @FXML
    private Button performExamBtn;

//    @FXML
//    private Label studentNameLabel;

    @FXML
    private Button viewExamsBtn;
    
    @FXML
    private Button logoutBtn;

    /**
     * Initializes the controller.
     *
     * This method is called automatically when the associated FXML file is loaded.
     * It is responsible for initializing the controller and setting up the initial state of the UI components.
     *
     * @param location  The URL location of the FXML file.
     * @param resources The ResourceBundle containing localized resources for the FXML file.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
   	 Platform.runLater(() -> {
		 Stage stage = (Stage) window.getScene().getWindow();
	    	stage.setOnCloseRequest(event -> {
	            event.consume(); // Consume the event to prevent automatic window closing
	            LoginController.disconnect();
	            stage.close();
	        });
     });
	 
		try {
			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
//		String name = null;
//		try {
//			name=LoggedInUser.getInstance().getUser().getFirstName();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		studentNameLabel.setText(name);
		
	if (LoggedInUser.getHeadDep() != null) {
		 logoutBtn.setDisable(true);
		performExamBtn.setDisable(true);
		
	}
		
	}
	/*
	 
	 * functionality : move screen to enter Activation code for making exam */
    @FXML
    void performExam(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_student/EnterActivationCodeForm.fxml","EnterActivationCode", event,true);

    }
    
    @FXML
    void clickViewExams(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_student/GradedExamsTable.fxml", "Grades Approval", event,true);

    }

    @FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }
    
}
