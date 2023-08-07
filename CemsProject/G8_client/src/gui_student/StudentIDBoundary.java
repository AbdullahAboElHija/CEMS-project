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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StudentIDBoundary implements Initializable {

	@FXML
	private Label userName;

	@FXML
	private AnchorPane window;

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
	void clickBack(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_student/EnterActivationCodeForm.fxml", "EnterActivationCode",
				event, true);
	}

	@FXML
	void clickStart(ActionEvent event) {

	}

	/**
	
	Initializes the controller when the corresponding FXML file is loaded.
	@param location The URL location of the FXML file.
	@param resources The ResourceBundle associated with the FXML file.
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
			userName.setText("Hi " + LoggedInUser.getInstance().getUser().getFirstName() + " "
					+ LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setVisible(false);
    	 }

	}

	@FXML
	void clickLogout(ActionEvent event) {
		LoginController.logOut();
		GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
	}

}
