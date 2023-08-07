package gui_student;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import entity.ManualExam;
import entity.StudentExam;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;
import entity.Exam;
import entity.LoggedInUser;

public class EnterActivationCodeBoundary implements Initializable {

	@FXML
	private Label userName;

	@FXML
	private AnchorPane window;

	@FXML
	private TextField CodeTextFiled;

	@FXML
	private Button EnterCodeBtn;

	@FXML
	private Label msgArea;

	@FXML
	private Button logoutBtn;
	
	  @FXML
	    private Button backBtn;

	private static String codeString;
	ManualExam Exam;
	Boolean examIsActive;
	ArrayList<String> ExamActiveAndIDList;
	static String examIdCode;
	

	/**
	 * @Abdullah functionality : this method sends to the server ActivationExam code
	 *           that entered by student. input : click button and Activation Code
	 * @return Screen of MakingExam (by Type)
	 * @throws Exception
	 * 
	 */
	@FXML
	void enterCode(ActionEvent event) {
		codeString = CodeTextFiled.getText();
		// ask server if exam is active
		Msg msg = new Msg("CheckCode", MsgType.FROM_CLIENT, codeString);
		ClientUI.chat.accept(msg);
		// System.out.println(CEMSClient.responseFromServer.getData());
		ExamActiveAndIDList = (ArrayList<String>) CEMSClient.responseFromServer.getData();
		// System.out.println(ExamActiveAndIDList);
		if (ExamActiveAndIDList.get(0).equals("active")) {
			examIdCode = ExamActiveAndIDList.get(1);

			if (!PerformExamController.hasStudentPerformedExam(codeString)) {
				if (ExamActiveAndIDList.get(2).equals("manual")) {
					PerformExamController.getManualDetails(codeString, examIdCode);
					GuiCommon.getInstance().displayNextScreen("/gui_student/PerformManualExamForm.fxml",
							"Perform Manual Exam", event, true);
				} else if (ExamActiveAndIDList.get(2).equals("online")) {
					// [active/inactive,ID,manual/online]
					PerformExamController.getOnlineDetails(codeString, examIdCode);
					GuiCommon.getInstance().displayNextScreen("/gui_student/EnterIDForm.fxml", "Enter Your ID", event,
							true);
				}
			} else {
				showMsg("You have already performed this exam.");

			}
		} else {
			showMsg("Not an active exam code");
		}

	}

	public void showMsg(String msg) {
		msgArea.setText(msg);
	}

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
	
	 @FXML
	    void clickBack(ActionEvent event) {
		 GuiCommon.getInstance().displayNextScreen("/gui_student/studentMenu.fxml", "Hello Student", event,true);
	    }
	 
	 public static String getCode() {
		 return codeString;
	 }

}
