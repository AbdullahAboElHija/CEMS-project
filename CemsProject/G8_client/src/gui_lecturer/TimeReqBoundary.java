package gui_lecturer;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import client.ClientUI;
import control_common.LoginController;
import entity.ActivatedExam;
import entity.LoggedInUser;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

/**
 * Represents the controller for the time request boundary in the lecturer GUI.
 * It handles the functionality related to adding time for an activated exam.
 */
public class TimeReqBoundary implements Initializable {
	
    @FXML
    private Label userName;
	
    @FXML
    private Button logoutBtn;
    
    @FXML
    private AnchorPane window;
    
	@FXML
    private TextField timeAmount;

    @FXML
    private Label ErrMsg;

    @FXML
    private Label CrrMsg;
    
	private Timer notificationHandler;
	

    @FXML
    private TextArea reasonTextArea;
    
	@FXML
	private Button LogoutBtn;

    /**
     * Handles the request to add time for an activated exam.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void reqAddTime(ActionEvent event) {
    	String input = timeAmount.getText();
    	String reason = reasonTextArea.getText();
		ActivatedExam ex = ActivatedExamsBoundary.examToReqTime;
		if(!ex.getActiveStatus().equals("active")) {
			return;
		}
		else if(input.length()==0) {
			ErrMsg.setText("Please enter amount of minutes to request.");
    	}
    	else if(!isInteger(input)) {
    		ErrMsg.setText("Invalid time input.");
    	}
    	else if(reason.length()==0) {
    		ErrMsg.setText("You must enter reason for you time request.");
    	}
    	else {
    		int time = Integer.parseInt(input);
    		ex.setTimeRequested(time);
    		ex.setReasonTimeReq(reason);
    		
    		Msg reqtimeMsg = new Msg("timeReqMsg",MsgType.FROM_CLIENT,ex);
	    	ClientUI.chat.accept(reqtimeMsg);
	    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ActivatedExams.fxml", "Activated Exams", event,true);
    	}
    }
    
    /**
     * Checks if the given input is an integer.
     *
     * @param input The input to be checked.
     * @return {@code true} if the input is an integer, {@code false} otherwise.
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Handles the click event of the "Back" button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void clickBackButton(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ActivatedExams.fxml", "ActivatedExams", event,true);

    }
    
    /**
     * Initializes the controller.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
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
 		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setVisible(false);
    	 }

		
	}
	
	/**
     * Handles the click event of the "Logout" button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }
    
    
    
}
