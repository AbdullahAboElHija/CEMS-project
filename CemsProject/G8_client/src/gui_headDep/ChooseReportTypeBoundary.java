package gui_headDep;

import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChooseReportTypeBoundary implements Initializable {

	@FXML
	private AnchorPane window;

	@FXML
	private Button logoutBtn;

	@FXML
	private Label userName;
	
    @FXML
    private Label msgArea;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> {
	   		 Stage stage = (Stage) window.getScene().getWindow();
	   	    	stage.setOnCloseRequest(event -> {
	   	            event.consume(); // Consume the event to prevent automatic window closing
	   	         LoginController.setWithReq(true);
	   	         LoginController.disconnect();	   
	   	         stage.close();
	   	        });
	        });
		try {
			userName.setText("Hi " + LoggedInUser.getHeadDep().getFirstName() + " "
					+ LoggedInUser.getHeadDep().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void courseReport(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/ChooseCourseReport.fxml", "ChooseCourseReport", event,
				true);

	}

	@FXML
	void lecReport(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/ChooseReportExam.fxml", "ChooseReportExam", event,
				true);

	}

	@FXML
	void back(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/headDepMenu.fxml", "Main Menu", event, true);
	}

	@FXML
	void StudentReport(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/ChooseStudentReport.fxml", "ChooseStudentReport", event,
				true);
	}
	
    @FXML
    void clickLogout(ActionEvent event) {
    	if( LoginController.logOutHeadDep()) {
			GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
		}
    }
    
    public void showMsg( String msg) {
    	msgArea.setText(msg);
    }

}
