package gui_student;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_lecturer.GradesApprovalController;
import entity.LoggedInUser;
import entity.StudentExam;
import entity.User;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class GradedExamsBoundary implements Initializable {
	@FXML
	private Button backBtn;

	@FXML
	private TableColumn<StudentExam, Boolean> changeCol;

	@FXML
	private TableColumn<StudentExam, String> examCodeCol;

	@FXML
	private TableColumn<StudentExam, Integer> gradeCol;

	@FXML
	private TableView<StudentExam> gradesApprovalTable;

	@FXML
	private Button viewBtn;

	@FXML
	private TableColumn<StudentExam, String> courseCol;

	@FXML
	private Label msgArea;

	@FXML
	private AnchorPane window;

	@FXML
	private Button logoutBtn;

	@FXML
	private Label userName;

	private User user;

	private ObservableList<StudentExam> studentExamsInTable;
	private ArrayList<StudentExam> examCopiesOfStudent;

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
		
		setupUser();
		setupTableProperties();
		setupTableContent();
		GradesApprovalController.setChosenStudentExam(null);
		try {
			userName.setText("Hi " + LoggedInUser.getInstance().getUser().getFirstName() + " "
					+ LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (LoggedInUser.getHeadDep() != null) {
			 logoutBtn.setDisable(true);
		}

	}

    /**
     * Sets up the currently logged in user.
     *
     * @return None.
     */
    private void setupUser() {
    	try {

			user = LoggedInUser.getInstance().getUser();
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	/**
	 * Sets up the properties of the table columns.
	 *
	 * @return None.
	 */
    private void setupTableProperties() {
    	
	 	examCodeCol.setCellValueFactory(new PropertyValueFactory<>("activationCode"));
	 	courseCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
	 	gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
	 	changeCol.setCellValueFactory(new PropertyValueFactory<>("gradeChange"));
	 	studentExamsInTable=FXCollections.observableArrayList();
	 	gradesApprovalTable.setItems(studentExamsInTable); 
    }
    
    /**
     * Retrieves the graded exams of the student and populates the table.
     *
     * @return None.
     */
    private void setupTableContent() {
    	Msg msg1 = new Msg("getAllExamCopiesOfStudent",MsgType.FROM_CLIENT,user.getId());
    	ClientUI.chat.accept(msg1);
	 	Msg response = CEMSClient.responseFromServer;
	 	try {
	 		if( !response.getMsg().equals("getAllExamCopiesOfStudent")) {
    	 		throw new Exception("Unexpected server response");
    	 	}
	 	}catch(Exception e) {
	 		e.printStackTrace();
	 	}
    	examCopiesOfStudent= (ArrayList<StudentExam>)response.getData();
    	ArrayList<StudentExam> itemsToRemove = new ArrayList<>(); 
	 	for(StudentExam studentExam : examCopiesOfStudent) {
	 	
	 		if( ! studentExam.isAvailableForVieweing()) {
	 			 itemsToRemove.add(studentExam);
	 		}else {
	 			studentExamsInTable.add(studentExam);	 			
	 		}
	 	}
	 	examCopiesOfStudent.removeAll(itemsToRemove);
    }
    /**
     * Handles the event when a student exam is selected in the table.
     *
     * @param event The MouseEvent triggered by selecting a row in the table.
     * @return None.
     */
    @FXML
    void chooseStudentExam(MouseEvent event) {
    	GradesApprovalController.setChosenStudentExam(gradesApprovalTable.getSelectionModel().getSelectedItem()); 
    }


    /**
     * Handles the event when the view button is clicked.
     *
     * @param event The ActionEvent triggered by clicking the view button.
     * @return None.
     */
    @FXML
    void clickView(ActionEvent event) {
    	StudentExam sExam = GradesApprovalController.getChosenStudentExam();
    	if(sExam!= null) {
    		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ApproveGradeForm.fxml", "Approve Grade", event,true);
    	}else {
    		showMsg("You must choose an exam first");
    	}
    }
	
    /**
     * Displays a message in the message area.
     *
     * @param msg The message to be displayed.
     * 
     */
    public void showMsg( String msg) {
    	msgArea.setText(msg);
    }


	@FXML
	void clickBack(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_student/studentMenu.fxml", "Hello Student", event,true);
	}
	
	@FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }



}
