package gui_lecturer;

import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_common.NotificationController;
import control_lecturer.CoursesController;
import control_lecturer.ProfessionsController;
import entity.ActivatedExam;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class ActivatedExamsBoundary implements Initializable {
	
    @FXML
    private Label userName;
	
    @FXML
    private Button logoutBtn;


	@FXML
	private AnchorPane window;


	@FXML
	private TableView<ActivatedExam> ExamsTable;


	@FXML
	private TableColumn<ActivatedExam, String> IDCol;


	@FXML
	private TableColumn<ActivatedExam, Profession> ActiveStatusCol;


	@FXML
	private TableColumn<ActivatedExam, Course> courseCol;


	@FXML
	private TableColumn<ActivatedExam, Integer> durCol;


	@FXML
	private Button backButton;


	@FXML
	private ComboBox<Profession> professionsCombo;


	@FXML
	private ComboBox<Course> coursesCombo;


	@FXML

	private TableColumn<ActivatedExam, String> typeCol;

	@FXML
	private Label ErrMsg;

	@FXML
	private Label correctMsg;

	@FXML
	private TableColumn<ActivatedExam, String> activationCodeCol;

	@FXML
	private TableColumn<ActivatedExam, Integer> studentsSubmittedCol;

	@FXML
	private Button addTimeReqBtn;

	@FXML
	private Button LockExamBtn;


	@FXML
	private TableColumn<ActivatedExam, String> timeRequestStatusCol;

	@FXML
	private TableColumn<ActivatedExam, Integer> timeRequestedCol;

	private ObservableList<ActivatedExam> examsInTable;
	private ArrayList<ActivatedExam> examsByLec = new ArrayList<>();

	public static ActivatedExam examToReqTime;
	private Timer notificationHandler;

	@SuppressWarnings("unchecked")
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
			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		}

		activationCodeCol.setCellValueFactory(new PropertyValueFactory<>("activationCode"));
		IDCol.setCellValueFactory(new PropertyValueFactory<>("ExamID"));
		ActiveStatusCol.setCellValueFactory(new PropertyValueFactory<>("ActiveStatus"));
		courseCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
		durCol.setCellValueFactory(new PropertyValueFactory<>("ActualDuration"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("examType"));
		studentsSubmittedCol.setCellValueFactory(new PropertyValueFactory<>("studentsSubmitted"));
		timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("TimeRequested"));
		timeRequestStatusCol.setCellValueFactory(new PropertyValueFactory<>("TimeRequestStatus"));
		
		updateData();

		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setDisable(true);
    		 addTimeReqBtn.setDisable(true);
    		 LockExamBtn.setDisable(true);
    		 
    	 }

		setupNotificationsHadler();

	}


	public void setupNotificationsHadler() {
		notificationHandler = new Timer();
		notificationHandler.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// When extra time is received updates the timer and notifies the student
				Platform.runLater(() -> checkServerNotification());

			}
		}, 0, 1000);
	}

	/**
	 * Checks for server notifications and handles them accordingly. This method is
	 * called every 5 seconds.
	 */

	private void checkServerNotification() {
		if (NotificationController.notifyForApproveOrDecline) {
			updateData();
			NotificationController.notifyForApproveOrDecline = false;
		}

	}


	/**
	 * Updates the data in the table view with the latest information from the
	 * server. Fetches the time request exams from the server and populates the
	 * table view. Called when there is a need to refresh the data in the table
	 * view.
	 */
	public void updateData() {
		Lecturer lec;
		ExamsTable.getItems().clear();
		try {
			lec = (Lecturer) LoggedInUser.getInstance().getUser();
			Msg msg = new Msg("getActivatedExamsOfLec", MsgType.FROM_CLIENT, lec.getId());
			ClientUI.chat.accept(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Msg response = CEMSClient.responseFromServer;
		try {
			if (!response.getMsg().equals("getActivatedExamsOfLec")) {
				System.out.println(response.getMsg());
				throw new Exception("Unexpected response from server");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		examsByLec = (ArrayList<ActivatedExam>) CEMSClient.responseFromServer.getData();
		examsInTable = FXCollections.observableArrayList(examsByLec);
		ExamsTable.setItems(examsInTable);
		ExamsTable.getSortOrder().add(ActiveStatusCol);
		ExamsTable.refresh();
	}

	@FXML
	void clickBackButton(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "LecturerMenu", event, true);

	}




	@FXML
	/**
	 * Handles the event when the "Lock Exam" button is clicked. Retrieves the
	 * selected exam from the table view and sends a lock request to the server.
	 * Displays appropriate error or success messages based on the selected exam's
	 * status. Updates the data in the table view after locking the exam.
	 * 
	 * @param event The action event triggered by clicking the "Lock Exam" button.
	 */
	void lockExam(ActionEvent event) {
		ErrMsg.setText("");
		correctMsg.setText("");
		ActivatedExam selectedExam = ExamsTable.getSelectionModel().getSelectedItem();
		Msg lockActivatedExamMsg = new Msg("lockActivatedExam", MsgType.FROM_CLIENT, selectedExam);
		if (selectedExam == null) {
			ErrMsg.setText("Please select an exam to lock.");
		} else if (selectedExam.getActiveStatus().equals("active")) {
			ClientUI.chat.accept(lockActivatedExamMsg);
			correctMsg.setText("Exam Locked!");
		} else {
			ErrMsg.setText("This exam is already inactive.");
		}
		updateData();

	}

	@FXML
	/**
	 * Handles the event when the "Time Request" button is clicked. Retrieves the
	 * selected exam from the table view and navigates to the TimeReq screen.
	 * Displays an error message if no exam is selected.
	 *
	 * @param event The action event triggered by clicking the "Time Request"
	 *              button.
	 */
	void timeRequest(ActionEvent event) {
		ErrMsg.setText("");
		correctMsg.setText("");
		ActivatedExam selectedExam = ExamsTable.getSelectionModel().getSelectedItem();
		if (selectedExam == null) {
			ErrMsg.setText("Please select an exam for time request.");
			return;
		}
		else if(! selectedExam.getTimeRequestStatus().equals("none")){
			ErrMsg.setText("You can make only one time request per exam.");
			return;
		}
		else if (selectedExam.getActiveStatus().equals("active")) {
			examToReqTime = selectedExam;
			GuiCommon.getInstance().displayNextScreen("/gui_lecturer/TimeReq.fxml", "Time Req", event, true);
		} else {
			ErrMsg.setText("You cant make a time request for an inactive exam.");
		}
		
	}

	@FXML
	void clickLogout(ActionEvent event) {
		LoginController.logOut();
		GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);

	}

}
