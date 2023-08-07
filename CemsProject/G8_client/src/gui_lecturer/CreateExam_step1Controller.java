package gui_lecturer;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import control_common.LoginController;
import control_lecturer.CoursesController;
import control_lecturer.CreateExamController;
import control_lecturer.ProfessionsController;
import control_lecturer.QuestionBankController;
import entity.Course;
import entity.Exam;
import entity.LoggedInUser;
import entity.Profession;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class CreateExam_step1Controller implements Initializable {
	//This boundary serves for the first step of creating an exam for both manual and online exams
	
    @FXML
    private Label userName;
	
    @FXML
    private Button logoutBtn;
	
    @FXML
    private AnchorPane window;
    
	@FXML
	private RadioButton btnComputerized;

	@FXML
	private RadioButton btnManual;

	@FXML
	private ComboBox<Profession> selectProffessionList;

	@FXML
	private ComboBox<Course> selectCourseList;

	@FXML
	private TextField textExamDuration;

	@FXML
	private TextArea textLecturers_Instructions;

	@FXML
	private TextArea textStudent_Instructions;

	@FXML
	private Label textAuthor;

	@FXML
	private Button btnNext;

	@FXML
	private Button back;

	@FXML
	private Label ErrMsg;

	private ObservableList<Profession> professions = FXCollections.observableArrayList();
	private ObservableList<Course> courses = FXCollections.observableArrayList();

	/**
	 * called when users clicks back button , goes back to exams bank
	 * @param event
	 */
	@FXML
	void backBtn(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ExamsBank.fxml", "ExamsBank", event, true);

	}
	
	/**
	 * called when computerized/online exam button is chosen, manual is un-selected and user is allowed to choose profession
	 * @param event
	 */
	@FXML
	void btnComputerizedPress(ActionEvent event) {
		btnComputerized.setSelected(true);
		btnManual.setSelected(false);
		selectProffessionList.setDisable(false);
	}
	
	/**
	 * called when manual exam button is chosen, online/computerized is un-selected and user is allowed to choose profession
	 * @param event
	 */
	@FXML
	void btnManualPress(ActionEvent event) {
		btnComputerized.setSelected(false);
		btnManual.setSelected(true);
		selectProffessionList.setDisable(false);
	}
	
	/**
	 * called when the user clicks next button, saves temporarily in create exam controller and moves to the next step of creating an exam according to the chosen type 
	 * @param event
	 * @throws Exception if no user is logged in 
	 */
	@FXML
	void btnNext(ActionEvent event) throws Exception {
		String regex = "^[0-9]+$";

		boolean ManualExamIsSelected = btnManual.isSelected();
		boolean ComputrizedExamIsSelected = btnComputerized.isSelected();
		String examID = "000000";
		String authorID = LoggedInUser.getInstance().getUser().getId();
		if (selectProffessionList.getValue() == null) {
			ErrMsg.setText("Please Select Profession.");
			return;
		} else if (selectCourseList.getValue() == null) {
			ErrMsg.setText("Please Select Course.");
			return;
		}
		String professionID = selectProffessionList.getValue().getProfessionID();
		String courseID = selectCourseList.getValue().getCourseID();
		String lecturerNotes = textLecturers_Instructions.getText();
		String studentNotes = textStudent_Instructions.getText();
		if (textExamDuration.getText().isEmpty()) {
			ErrMsg.setText("Please Select duration for the exam.");
			return;
		} else if (ManualExamIsSelected) {
	        if( !textExamDuration.getText().matches(regex)) {
	            ErrMsg.setText("You should enter int to duration");
	            return;
	        }
	        else {
			int duration = Integer.parseInt(textExamDuration.getText());
			String examType = "manual";
			Exam ex = new Exam(examID, duration, authorID, courseID, examType, lecturerNotes, studentNotes,
					professionID, "0");
			CreateExamController.CreateExamID(ex);
			
			GuiCommon.getInstance().displayNextScreen("/gui_lecturer/CreateManualExamForm.fxml", "ExamsBank", event,
					true);
	        }
		} else if (ComputrizedExamIsSelected) {
	        if( !textExamDuration.getText().matches(regex)) {
	            ErrMsg.setText("You should enter int to duration");
	            return;
	        }
	        else {
			int duration = Integer.parseInt(textExamDuration.getText());
			String examType = "online";
			Exam ex = new Exam(examID, duration, authorID, courseID, examType, lecturerNotes, studentNotes,
					professionID, "0");
			CreateExamController.CreateExamID(ex);
			CreateExamController.examForPrev = ex;
			GuiCommon.getInstance().displayNextScreen("/gui_lecturer/CreateOnlineExam.fxml", "CreateOnlineExam", event,
					true);
	        }
		} else {
			ErrMsg.setText("Please Select Exam type.");
		}
	}

	@FXML
	void selectCourseList(ActionEvent event) {

	}
	
	/**
	 * called when lecturer chooses a profession and fills the courses combo box accordingly
	 * @param event
	 */
	@FXML
	void selectProffessionList(ActionEvent event) {
		Profession selectedProfession = (Profession) selectProffessionList.getValue();

		CoursesController.fillCourseComboByProfession(selectCourseList, courses, selectedProfession);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//automatic window closing
		Platform.runLater(() -> {
			Stage stage = (Stage) window.getScene().getWindow();
	    		stage.setOnCloseRequest(event -> {
	    			event.consume(); // Consume the event to prevent automatic window closing
	    			LoginController.disconnect();
	    			stage.close();
	    		});
    	});
		
		try {
			textAuthor.setText(LoggedInUser.getInstance().getUser().getFirstName());
			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());
			ProfessionsController.fillProfessionsCombo(selectProffessionList, professions);

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
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }

}
