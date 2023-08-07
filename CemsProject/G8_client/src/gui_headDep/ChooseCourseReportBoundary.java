package gui_headDep;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import common.ReportController;
import control_common.LoginController;

import common.ReportController;
import entity.LoggedInUser;
import entity.User;
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

public class ChooseCourseReportBoundary implements Initializable {
	@FXML
	private AnchorPane window;

	@FXML
	private ComboBox<String> courseNameCombo;

	@FXML
	private ComboBox<String> actCodeCombo;

	@FXML
	private Button logoutBtn;

	@FXML
	private Label userName;

	@FXML
	private Label msgArea;
	
    @FXML
    private Label ErrMsg;

	private HashMap<String, String> map;

	/**
	 * Event handler for generating a report.
	 *
	 * @param event The action event triggered by the user.
	 */
	@FXML
	void getRerpot(ActionEvent event) {
		ErrMsg.setText("");
		String actCode = actCodeCombo.getValue();
		if (actCode == null) {
			ErrMsg.setText("Choose First!");
		} else {
			ReportController.headFromCourse = true;
			ReportController.ActivationCodeUsinginHDReportBoundary = actCode;
			Msg getExamID = new Msg("getExamIDByActivationCode", MsgType.FROM_CLIENT, actCode);
			ClientUI.chat.accept(getExamID);
			Msg response = CEMSClient.responseFromServer;
			if (response.getMsg().equals("getExamIDByActivationCode")) {
				ReportController.examIDCodeUsinginHDReportBoundary = (String) response.getData();
				// System.out.println(ReportController.examIDCodeUsinginHDReportBoundary);
				GuiCommon.getInstance().displayNextScreen("/gui_headDep/GetExamReport.fxml", "Report", event, true);
			}
		}
		
	}
	
	/**
	 * Event handler for generating a report.
	 *
	 * @param event The action event triggered by the user.
	 */
	@FXML
	void onSelectCourseName(ActionEvent event) {
		String courseName = courseNameCombo.getValue();
		String courseID = map.get(courseName);
		actCodeCombo.getItems().clear();
		Msg msg = new Msg("getExamsActivatedByCourse", MsgType.FROM_CLIENT, courseID);
		ClientUI.chat.accept(msg);
		Msg response = CEMSClient.responseFromServer;
		if (response.getMsg().equals("getExamsActivatedByCourse")) {
			HashSet<String> lst = (HashSet<String>) response.getData();

			actCodeCombo.getItems().addAll(lst);
		}
	}

	/**
	 * Initializes the controller.
	 *
	 * This method is called automatically when the associated FXML file is loaded.
	 * It is responsible for initializing the controller and setting up the initial
	 * state of the UI components.
	 *
	 * @param location  The URL location of the FXML file.
	 * @param resources The ResourceBundle containing localized resources for the
	 *                  FXML file.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Msg msg = new Msg("getCourseNameAndCourseID", MsgType.FROM_CLIENT, "");
		ClientUI.chat.accept(msg);
		Msg response = CEMSClient.responseFromServer;
		if (response.getMsg().equals("getCourseNameAndCourseID")) {
			map = (HashMap<String, String>) response.getData();

			courseNameCombo.getItems().addAll(map.keySet());
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
			userName.setText(
					"Hi " + LoggedInUser.getHeadDep().getFirstName() + " " + LoggedInUser.getHeadDep().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void back(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/ChooseReportType.fxml", "ChooseReportExam", event,
				true);
	}

	@FXML
	void clickLogout(ActionEvent event) {
		if (LoginController.logOutHeadDep()) {
			GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
		}

	}

	public void showMsg(String msg) {
		msgArea.setText(msg);
	}

}
