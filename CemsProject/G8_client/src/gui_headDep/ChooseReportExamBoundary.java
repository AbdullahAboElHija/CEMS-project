package gui_headDep;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import common.ReportController;
import control_common.LoginController;
import entity.LoggedInUser;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class ChooseReportExamBoundary implements Initializable {

	@FXML
	private AnchorPane window;

	@FXML
	private ComboBox<String> examIDsCombo;

	@FXML
	private ComboBox<String> activationCodesCombo;

	@FXML
	private Button getReportBtn;
	@FXML
	private ComboBox<String> lecNameCombo;

	@FXML
	private Label ErrMsg;
	
	@FXML
	private Button logoutBtn;
	
	@FXML
	private Label userName;

	private HashMap<String, String> map;

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
		Msg getlecsNameAndIDSMSG = new Msg("getLecNameAndLecID", MsgType.FROM_CLIENT, "");
		ClientUI.chat.accept(getlecsNameAndIDSMSG);
		Msg response = CEMSClient.responseFromServer;
		if (response.getMsg().equals("getLecNameAndLecID")) {
			map = (HashMap<String, String>) response.getData();
			lecNameCombo.getItems().addAll(map.keySet());
		}
		
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

	
	/**
	 * Handles the event when a lecturer name is selected.
	 *
	 * @param event The ActionEvent triggered by selecting a lecturer name.
	 */
	@FXML
	void onSelectLecName(ActionEvent event) {
		examIDsCombo.getItems().clear();
		String lecName = lecNameCombo.getValue();
	
		String lecID = map.get(lecName);
		
		if (lecID != null) {
			Msg getExamsIDOfLecturer = new Msg("getExamsIDOfLecturer", MsgType.FROM_CLIENT, lecID);
			ClientUI.chat.accept(getExamsIDOfLecturer);
			Msg response = CEMSClient.responseFromServer;
			if (response.getMsg().equals("getExamsIDOfLecturer")) {
				ArrayList<String> ExamsID = (ArrayList<String>) response.getData();
				examIDsCombo.getItems().addAll(ExamsID);
			}
		}
	}

	/**
	 * Handles the event when the Get Report button is clicked.
	 * @param event The ActionEvent triggered by clicking the Get Report button.
	 */
	@FXML
	void getReport(ActionEvent event) {
		if (activationCodesCombo.getValue() == null || examIDsCombo.getValue() == null) {
			ErrMsg.setText("You must choose ExamID and ActivationCode to get report!");
		} else {
			ErrMsg.setText("");
			ReportController.ActivationCodeUsinginHDReportBoundary = activationCodesCombo.getValue();
			ReportController.examIDCodeUsinginHDReportBoundary = examIDsCombo.getValue();
			ReportController.headFromLec = true;
			GuiCommon.getInstance().displayNextScreen("/gui_headDep/GetExamReport.fxml", "Report", event, true);
		}
	}

	/**
	 * Handles the event when an exam ID is selected.
	 * @param event The ActionEvent triggered by selecting an exam ID.
	 */
	@FXML
	void onSelectExamID(ActionEvent event) {
		activationCodesCombo.getItems().clear();
		String selectedExamID = examIDsCombo.getValue();
		Msg getExamsActivatedByLecAndAnotherLec = new Msg("getExamsActivatedByLecAndAnotherLec", MsgType.FROM_CLIENT,
				selectedExamID);
		ClientUI.chat.accept(getExamsActivatedByLecAndAnotherLec);
		Msg response = CEMSClient.responseFromServer;
		if (response.getMsg().equals("getExamsActivatedByLecAndAnotherLec")) {
			ArrayList<String> activationIDs = (ArrayList<String>) response.getData();
			activationCodesCombo.getItems().addAll(activationIDs);

		}
	}

	@FXML
	void back(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/headDepMenu.fxml", "headDepMenu", event, true);
	}
	
	@FXML
	void clickLogout(ActionEvent event) {
		if( LoginController.logOutHeadDep()) {
			GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
		}
	}

}
