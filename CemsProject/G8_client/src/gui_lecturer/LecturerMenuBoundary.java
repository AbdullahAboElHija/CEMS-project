package gui_lecturer;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import control_common.LoginController;
import control_lecturer.ProfessionsController;
import entity.LoggedInUser;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class LecturerMenuBoundary implements Initializable {


	@FXML
	private Label userName;

	@FXML
	private AnchorPane window;

	@FXML
	private Button activateExamBtn;

	@FXML
	private Button examsBankBtn;

	@FXML
	private Button getReportsBtn;

    @FXML
    private Label lecNameLabel;

	@FXML
	private Button questionsBankBtn;

    @FXML
    private Button viewActiveExamsBtn;
    
    @FXML
    private Button logoutBtn;
    
    @FXML
    private Button gradesApprovalBtn; 
 
    /**
     * Initializes the controller and sets up the necessary event handlers.
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
			userName.setText("Hi " + LoggedInUser.getInstance().getUser().getFirstName() + " "
					+ LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if (LoggedInUser.getHeadDep() != null) {
			 logoutBtn.setDisable(true);
			activateExamBtn.setDisable(true);
		}

	}

    
    /**
     * Handles the click event of the "Question Bank" button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void clickQuestionBankBtn(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/QuestionBank1.fxml", "QuestionsBank", event,true);
    }

    /**
     * Handles the click event of the "Get Reports" button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void getReportsBtn(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ChooseReport.fxml", "Choose Report", event,true);

	}

    /**
     * Handles the click event of the "View Active Exams" button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void viewActiveExamsBtn(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ActivatedExams.fxml", "Activated Exams", event,true);
    }
    
    /**
     * Handles the click event of the "Activate Exam" button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void ActivateExamBtn(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ExamsActivation.fxml", "Activate Exam", event,true);
    }



    /**
     * Handles the click event of the "Exams Bank" button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void ExamsBankBtn(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ExamsBank.fxml", "Exams Bank", event,true);
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
    
    /**
     * Handles the click event of the "Grades Approval" button.
     *
     * @param event The action event triggered by clicking the button.
     */
	@FXML
	void clickGradesApproval(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/GradesApproval.fxml", "Grades Approval", event, true);
	}

}
