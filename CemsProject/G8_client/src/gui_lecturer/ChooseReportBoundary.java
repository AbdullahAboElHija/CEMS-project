package gui_lecturer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import common.ReportController;
import entity.LoggedInUser;
import gui.GuiCommon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import logic.Msg;
import logic.MsgType;

public class ChooseReportBoundary implements Initializable {

	@FXML
	private AnchorPane window;

	@FXML
	private ComboBox<String> examIDsCombo;

	@FXML
	private ComboBox<String> activationCodesCombo;

	@FXML
	private Button getReportBtn;

	@FXML
	private Label ErrMsg;

	@FXML
	private Button logoutBtn;
	
    @FXML
    private Label userName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String lecID = null;
		try {
			lecID = LoggedInUser.getInstance().getUser().getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//  gets all the exams of the lecturer
		Msg getExamsIDOfLecturer = new Msg("getExamsIDOfLecturer", MsgType.FROM_CLIENT, lecID);
		ClientUI.chat.accept(getExamsIDOfLecturer);
		Msg response = CEMSClient.responseFromServer;
		if (response.getMsg().equals("getExamsIDOfLecturer")) {
			ArrayList<String> ExamsID = (ArrayList<String>) response.getData();
			examIDsCombo.getItems().addAll(ExamsID);
		}
		if (LoggedInUser.getHeadDep() != null) {
			logoutBtn.setVisible(false);
		}
 		try {
 			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * called when lecturer clicks get report button
	 * if an exam id or an activation code are not chosen, error message appears
	 * @param event
	 */
	@FXML
	void getReport(ActionEvent event) {
		if (activationCodesCombo.getValue() == null || examIDsCombo.getValue() == null) {
			ErrMsg.setText("You must choose ExamID and ActivationCode to get report.");
		} else {
			ErrMsg.setText("");
			ReportController.ActivationCodeUsinginLecReportBoundary = activationCodesCombo.getValue();
			ReportController.examIDCodeUsinginLecReportBoundary = examIDsCombo.getValue();
			GuiCommon.getInstance().displayNextScreen("/gui_lecturer/GetExamReport.fxml", "Report", event, true);
		}
	}
	
	/**
	 * called when lecturer selects an exam id, gets all the exams written by logged in lecturer in order to prepare data for statistics
	 * @param event
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
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "Main Menu", event, true);
	}

	@FXML
	void clickLogout(ActionEvent event) {
		LoginController.logOut();
		GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
	}

}
