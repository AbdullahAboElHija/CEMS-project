package gui_headDep;

import java.net.URL;
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
import entity.User;
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

public class TimeRequestsBoundary implements Initializable {

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
	private Button approveRequestBtn;
	@FXML
	private Button declineRequestBtn;

	@FXML
	private TableColumn<ActivatedExam, String> timeRequestStatusCol;
	@FXML
	private TableColumn<ActivatedExam, String> reasonCol;

	@FXML
	private TableColumn<ActivatedExam, Integer> timeRequestedCol;

	private ObservableList<ActivatedExam> examsInTable;
	private ArrayList<ActivatedExam> timeReqExams = new ArrayList<>();

	@FXML
	private AnchorPane window;


	@FXML
	private Button logoutBtn;

	@FXML
	private Timer notificationHandler;
	
	@FXML
	private Label userName;


	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		activationCodeCol.setCellValueFactory(new PropertyValueFactory<>("activationCode"));
		IDCol.setCellValueFactory(new PropertyValueFactory<>("ExamID"));
		ActiveStatusCol.setCellValueFactory(new PropertyValueFactory<>("ActiveStatus"));
		courseCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
		durCol.setCellValueFactory(new PropertyValueFactory<>("ActualDuration"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("examType"));
		studentsSubmittedCol.setCellValueFactory(new PropertyValueFactory<>("studentsSubmitted"));
		timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("TimeRequested"));
		timeRequestStatusCol.setCellValueFactory(new PropertyValueFactory<>("TimeRequestStatus"));
		reasonCol.setCellValueFactory(new PropertyValueFactory<>("ReasonTimeReq"));
		timeRequestStatusCol.setSortType(TableColumn.SortType.DESCENDING);
		
		Platform.runLater(() -> {
			Stage stage = (Stage) window.getScene().getWindow();
			stage.setOnCloseRequest(event -> {
				event.consume(); // Consume the event to prevent automatic window closing
				LoginController.setWithReq(true);
				LoginController.disconnect();
				stage.close();
			});
		});

		setupNotificationsHadler();
		updateData();
		
		try {
			userName.setText("Hi " + LoggedInUser.getHeadDep().getFirstName() + " "
					+ LoggedInUser.getHeadDep().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


//    @FXML
//    private Button approveRequestBtn;
//    @FXML
//    private Button declineRequestBtn;

	@FXML
	void onRowSelection(MouseEvent event) {
		ActivatedExam selectedExam = ExamsTable.getSelectionModel().getSelectedItem();
		approveRequestBtn.setDisable(false);
		declineRequestBtn.setDisable(false);
		if (selectedExam.getTimeRequestStatus().equals("approved")
				|| selectedExam.getTimeRequestStatus().equals("declined")) {
			approveRequestBtn.setDisable(true);
			declineRequestBtn.setDisable(true);
		}
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
	 * called every 10 seconds.
	 */
	private void checkServerNotification() {
		if (NotificationController.notifyForUpdateTimeReqInHD) {
			updateData();
			NotificationController.notifyForUpdateTimeReqInHD = false;
		}

	}


	/**
	 * Updates the table view with the latest data for time request exams.
	 *
	 * This function retrieves the latest data for time request exams from the server.
	 * It sends a message to the server to get the time request exams and updates the ExamsTable accordingly.
	 * The function also sets the sorting order based on the time request status column and refreshes the table view.
	 */
	public void updateData() {
		try {
			Msg msg = new Msg("getTimeReqExams", MsgType.FROM_CLIENT, "");
			ClientUI.chat.accept(msg);
			timeReqExams = (ArrayList<ActivatedExam>) CEMSClient.responseFromServer.getData();
			examsInTable = FXCollections.observableArrayList(timeReqExams);
			ExamsTable.setItems(examsInTable);
			ExamsTable.getSortOrder().add(timeRequestStatusCol);
			ExamsTable.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}




	@FXML
	public void clickBackButton(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/headDepMenu.fxml", "headDepMenu", event, true);

	}


	@FXML
	void clickLogout(ActionEvent event) {
		if( LoginController.logOutHeadDep()) {
			GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
		}
	}

	
	/**
	 * Approves a time request for an exam.
	 *
	 * This function is triggered when the user clicks the "Approve Request" button.
	 * It retrieves the selected exam from the ExamsTable and checks if it has a pending time request.
	 * If a pending request exists, it sends an approve request message to the server.
	 * The function then updates the data, refreshes the ExamsTable, and displays the response from the server if available.
	 *
	 * @param event The action event generated by the "Approve Request" button click.
	 */
	@FXML
	public void approveRequest(ActionEvent event) {
		ErrMsg.setText("");
		correctMsg.setText("");
		ActivatedExam selectedExam = ExamsTable.getSelectionModel().getSelectedItem();
		if (selectedExam == null) {
			ErrMsg.setText("Please select a request first.");
			return ;
		}
		if (selectedExam.getTimeRequestStatus().equals("pending")) {
			Msg approveRequestMsg = new Msg("approveTimeReqForExam", MsgType.FROM_CLIENT, selectedExam);
			
			ClientUI.chat.accept(approveRequestMsg);
			Msg response = CEMSClient.responseFromServer;
			//System.out.println("1");
			//System.out.println(response.getData());
			updateData();
			ExamsTable.refresh();
		} else {
			ErrMsg.setText("You have already responded to this request.");
		}
	}

	
	
	
	/**
	 * Declines a time request for an exam.
	 *
	 * This function is triggered when the user clicks the "Decline Request" button.
	 * It retrieves the selected exam from the ExamsTable and checks if it has a pending time request.
	 * If a pending request exists, it sends a decline request message to the server.
	 * The function then updates the data and refreshes the ExamsTable accordingly.
	 *
	 * @param event The action event generated by the "Decline Request" button click.
	 */
	@FXML
	public void declineRequest(ActionEvent event) {
		ErrMsg.setText("");
		correctMsg.setText("");
		ActivatedExam selectedExam = ExamsTable.getSelectionModel().getSelectedItem();
		if (selectedExam == null) {
			ErrMsg.setText("Please select a request first.");
		} else if (selectedExam.getTimeRequestStatus().equals("pending")) {
			Msg declineRequestMsg = new Msg("declineTimeReqForExam", MsgType.FROM_CLIENT, selectedExam);
			ClientUI.chat.accept(declineRequestMsg);
			Msg response = CEMSClient.responseFromServer;
			updateData();
			ExamsTable.refresh();

		} else {
			ErrMsg.setText("You have already responded to this request.");
		}

	}

}
