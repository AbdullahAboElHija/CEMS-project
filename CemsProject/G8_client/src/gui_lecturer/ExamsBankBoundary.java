package gui_lecturer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_lecturer.CoursesController;
import control_lecturer.CreateExamController;
import control_lecturer.ProfessionsController;
import control_lecturer.QuestionBankController;
import entity.Course;
import entity.Exam;
import entity.Lecturer;
import entity.LoggedInUser;
import entity.Profession;
import entity.Question;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class ExamsBankBoundary implements Initializable {

	@FXML
	private Label userName;

	@FXML
	private Button logoutBtn;

	@FXML
	private AnchorPane window;

	@FXML
	private TableView<Exam> ExamsTable;

	@FXML
	private TableColumn<Exam, String> IDCol;

	@FXML
	private TableColumn<Exam, Profession> professionCol;

	@FXML
	private TableColumn<Exam, Course> courseCol;

	@FXML
	private TableColumn<Exam, Integer> durCol;

	@FXML
	private Button createExam;

	@FXML
	private Button deleteExamButton;

	@FXML
	private Button backButton;

	@FXML
	private ComboBox<Profession> professionsCombo;

	@FXML
	private ComboBox<Course> coursesCombo;

	@FXML
	private TableColumn<Exam, String> typeCol;

	@FXML
	private Label msgArea;

    @FXML
    private Button previewBtn;

	private ObservableList<Exam> examsInTable;
	private ObservableList<Profession> professions = FXCollections.observableArrayList();
	private ObservableList<Course> courses = FXCollections.observableArrayList();
	private ArrayList<Exam> examsByLec = new ArrayList<>();
	
	/**
	 * Initializes the controller. This method is automatically called
	 * after the FXML file has been loaded.
	 * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// automatic window closing
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

		IDCol.setCellValueFactory(new PropertyValueFactory<>("ExamID"));
		professionCol.setCellValueFactory(new PropertyValueFactory<>("professionName"));
		courseCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
		durCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("examType"));
		examsInTable = FXCollections.observableArrayList();
		ExamsTable.setItems(examsInTable);

		// filling the lecturers professions in the combo box
		ProfessionsController.fillProfessionsCombo(professionsCombo, professions);
		professionsCombo.getItems().add(new Profession("All", "All"));
		try {
			Lecturer lec = (Lecturer) LoggedInUser.getInstance().getUser();
			Msg msg = new Msg("getExamsByLec", MsgType.FROM_CLIENT, lec);
			ClientUI.chat.accept(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Msg response = CEMSClient.responseFromServer;
		try {
			if (!response.getMsg().equals("getExamsByLec")) {
				System.out.println(response.getMsg());
				throw new Exception("Unexpected response from server");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		examsByLec = (ArrayList<Exam>) CEMSClient.responseFromServer.getData();
		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setDisable(true);
    		 createExam.setDisable(true);
    		 deleteExamButton.setDisable(true);
    	 }
	}
	
	/**
	 * Handles the click event of the "Back" button.
	 * @param event The ActionEvent triggered by clicking the button.
	 */
	@FXML
	void clickBackButton(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "LecturerMenu", event, true);

	}
	

/**
 * Handles the click event of the "Create Exam" button.
 *
 * @param event The ActionEvent triggered by clicking the button.
 */
	@FXML
	void clickCreateExam(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/CreateExam.fxml", "CreateExam", event, true);

	}


/**
 * Handles the click event of the "Delete Exam" button.
 *
 * @param event The ActionEvent triggered by clicking the button.
 */
	@FXML
	void clickDeleteExam(ActionEvent event) {
		if (CreateExamController.getChosenExam() != null) {
			if (!CreateExamController.getChosenExam().getActivatedCounter().equals("0")) {
				showMsg("Cannot delete a activated exam");
			} else {
				Msg msg = new Msg("deleteExam", MsgType.FROM_CLIENT, CreateExamController.getChosenExam());
				ClientUI.chat.accept(msg);
				examsByLec.remove(CreateExamController.getChosenExam());
				examsInTable.remove(CreateExamController.getChosenExam());
				CreateExamController.setChosenExam(null);

			}
		} else {
			showMsg("Choose a exam to delete");
		}

	}
	

	
	/**
	 * Handles the selection of an exam from the exams table.
	 * @param event The MouseEvent triggered by selecting an exam.
	 */
	@FXML
	void selectExam(MouseEvent event) {
		CreateExamController.setChosenExam(ExamsTable.getSelectionModel().getSelectedItem());

	}
	
	/**
	 * Handles the selection of a course from the courses combo box.
	 *
	 * @param event The ActionEvent triggered by selecting a course.
	 */
	@FXML
	void chooseCourse(ActionEvent event) {
		CreateExamController.setChosenExam(null);
		Course course = coursesCombo.getValue();
		Profession prof = professionsCombo.getValue();
		if (prof != null) {
			if (course != null) {
				examsInTable.clear();
				int i = 0;
				for (Exam ex : examsByLec) {
					if (ex.getCourseID().equals(course.getCourseID()) || (course.getCourseID().equals("All") && ex.getProfessionID().equals(prof.getProfessionID())))
						examsInTable.add(ex);
				}
			}
		}
	}

	/**
	 * Handles the selection of a profession from the professions combo box.
	 *
	 * @param event The ActionEvent triggered by selecting a profession.
	 */
	@FXML
	void chooseProfession(ActionEvent event) {
		CreateExamController.setChosenExam(null);
		Profession prof = professionsCombo.getValue();
		if (prof != null) {
			CoursesController.fillCourseComboByProfession(coursesCombo, courses, prof);
			coursesCombo.getItems().add(new Course("All", "All", prof.getProfessionID()));
			examsInTable.clear();
			int i = 0;
			for (Exam ex : examsByLec) {

				if (ex.getProfessionID().equals(prof.getProfessionID()) || prof.getProfessionID().equals("All"))
					examsInTable.add(ex);
			}
		}

	}

	/**
	 * Handles the click event of the "Logout" button.
	 *
	 * @param event The ActionEvent triggered by clicking the button.
	 */
	@FXML
	void clickLogout(ActionEvent event) {
		LoginController.logOut();
		GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
	}

	/**
	 * Displays a message in the message area.
	 *
	 * @param msg The message to be displayed.
	 */
	public void showMsg(String msg) {
		msgArea.setText(msg);
	}
	
	@FXML
    void clickPreview(ActionEvent event) {
		if (CreateExamController.getChosenExam() != null) {
			if(!CreateExamController.getChosenExam().getExamType().equals("online")) {
				showMsg("You can preview online exams only");
			}else {
				CreateExamController.examForPrev=CreateExamController.getChosenExam();
				CreateExamController.questions=CreateExamController.getChosenExam().getExamQuestions();
				GuiCommon.getInstance().displayNextScreen("/gui_lecturer/PreviewOnlineExam.fxml", "Preview Online Exam", null,false);
			}
		} else {
			showMsg("Choose an exam to preview.");
		}
		CreateExamController.setChosenExam(null);

    }

}
