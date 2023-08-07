package gui_student;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_common.NotificationController;
import entity.Exam;
import entity.LoggedInUser;
import entity.Question;
import entity.StudentExam;
import entity.Time;
import entity.User;
import entity.UserType;
import gui.GuiCommon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.Msg;
import logic.MsgType;

public class PerformOnlineExamBoundary implements Initializable {
	
    @FXML
    private Label userName;
    
    @FXML
    private AnchorPane window;

	@FXML
	private VBox questionContainer;

	@FXML
	private Button submitBtn;

	@FXML
	private Label timeLabel;

	private StudentExam studentExam;

	@FXML
	private TextArea instructionsBox;
	
    @FXML
    private Button logoutBtn;

	Time time;
	String alarmTime = "00:00:00";
	private boolean flag = true;
	private Timer notificationHandler;
	private boolean timeOver=false;

	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
		if (time.getCurrentTime().equals(alarmTime) && flag && !timeOver) {
			terminateExam();
			flag = false;
		}
		if(!timeOver) {
			time.oneSecondPassedDownCount();
			timeLabel.setText(time.getCurrentTime());
		}
		
	
	}));


	/**
		Initializes the controller when the corresponding FXML file is loaded.
		@param location The URL location of the FXML file.
		@param resources The ResourceBundle associated with the FXML file.
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {
			Stage stage = (Stage) window.getScene().getWindow();
			stage.setOnCloseRequest(event -> {
				event.consume(); // Consume the event to prevent automatic window closing
				terminateExam();
				LoginController.disconnect();
				stage.close();
			});
			timeLabel.setText(time.getCurrentTime());
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
		});
	 
		try {
			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		setupNotificationsHadler();

		Exam exam = PerformExamController.getPerformExam();
		ArrayList<Question> questionsOfExam = exam.getExamQuestions();
		studentExam = new StudentExam();
		studentExam.setExam(exam);
		studentExam.setStatus("solving");
		studentExam.setExamQuestions(questionsOfExam);
		time = new Time(studentExam.getExam().getDuration());

		registerStudentInExam();

		setupVbox(questionsOfExam);
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setVisible(false);
    	 }
		 fillInstructions();
	}

	/**
		Handles the selection of an answer option.
		@param selectedOption The RadioButton representing the selected answer option.
	*/
	private void handleAnswerSelection(RadioButton selectedOption) {
		// Process the selected answer index for the corresponding question
		Integer[] indexes = (Integer[]) selectedOption.getUserData();
		studentExam.getExamQuestions().get(indexes[0]).setStudentAnswer(indexes[1] + 1);
		// Example:
		// exam.getExamQuestions().get(questionIndex).setSelectedAnswerIndex(selectedAnswerIndex);
		System.out.println("USER SELECTED AN ANSWER");
		System.out.println(studentExam.getExamQuestions().get(indexes[0]));
	}

	@FXML
	void clickSubmit(ActionEvent event) {
		studentExam.setSubmitted(true);
		terminateExam();
	}

	/**
	 * 	 * This method updates the solving time and status of the student's exam, displays the student's answers for each question,
	 * grades the exam, sends a termination message to the client, closes the current stage, navigates to the student menu,
	 * and displays a pop-up message indicating that the exam has ended.
	 *
	 */
	public void terminateExam() {
		timeOver=true;
		studentExam.setSolvingTime(time.getTimePassedInMinutes());
		studentExam.setStatus("finished");

		PerformExamController.gradeExam(studentExam);
		System.out.println(studentExam);
		Msg msg = new Msg("terminateStudentExam", MsgType.FROM_CLIENT, studentExam);
		ClientUI.chat.accept(msg);
		Stage currentStage = (Stage) submitBtn.getScene().getWindow();
		currentStage.close();
		
		GuiCommon.getInstance().displayNextScreen("/gui_student/studentMenu.fxml", "Student Menu", null, false);
		GuiCommon.getInstance().popUp("Exam ended");
	}

	
	/**

	Checks for server notifications related to the student.
	@param None.
	@return None.
	*/
	private void checkServerNotification() {
		if(NotificationController.notifyForStudentAddingTime) {
			addTime(NotificationController.timeToAddToStudent);
			NotificationController.timeToAddToStudent=null;
			NotificationController.notifyForStudentAddingTime=false;
		}
		if(NotificationController.notifyForStudentLockExam) {
			terminateExam();
			NotificationController.notifyForStudentLockExam=false;
		}
		
	}

	private void addTime(Integer timeAdded) {
		time.addTime(timeAdded);
	}


	/**
	
	Sets up the VBox container with questions for the exam.
	
	@param questionsOfExam An ArrayList of Question objects representing the questions for the exam.
	
	
	
	*/
	public void setupVbox(ArrayList<Question> questionsOfExam) {
		for (int i = 0; i < questionsOfExam.size(); i++) {
			Label questionLabel = new Label(questionsOfExam.get(i).getQuestion());
			questionContainer.getChildren().add(questionLabel);

			Label questionInstrucLabel = new Label(questionsOfExam.get(i).getQuestionInstructions());
			questionContainer.getChildren().add(questionInstrucLabel);

			String[] answersQ = { questionsOfExam.get(i).getAnswer1(), questionsOfExam.get(i).getAnswer2(),
					questionsOfExam.get(i).getAnswer3(), questionsOfExam.get(i).getAnswer4() };
			ToggleGroup answerGroup = new ToggleGroup();
			for (int j = 0; j < answersQ.length; j++) {
				RadioButton answerOption = new RadioButton(answersQ[j]);
				answerOption.setToggleGroup(answerGroup);
				Integer[] q_ans = { i, j };
				answerOption.setUserData(q_ans); // Set the index as user data
				answerOption.setOnAction(event -> handleAnswerSelection(answerOption));
				questionContainer.getChildren().add(answerOption);
			}
			Separator separator = new Separator();
			questionContainer.getChildren().add(separator);
		}
	}

	

	
	//Timer to check notification every second
	public void setupNotificationsHadler() {
		notificationHandler = new Timer();
		notificationHandler.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// When extra time is received updates the timer and notifies the student
				Platform.runLater(() ->checkServerNotification());
			}
		}, 0, 1000);
	}
	
	
	
    @FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }


     
    /**

	    Registers the student in the exam.
	    
	    
    */
     public void registerStudentInExam() {
    	 User user;
 		try {
 			user = LoggedInUser.getInstance().getUser();
 			String studentID = user.getId();
 			studentExam.setStudentId(studentID);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		 Msg msg = new Msg("registerStudentInExam",MsgType.FROM_CLIENT,studentExam);
 		 ClientUI.chat.accept(msg);
 		 Msg response  = CEMSClient.responseFromServer;
 		 
 		 try {
 			 if(!response.getMsg().equals("registerStudentInExam")) {
 				 throw new Exception("Unexpected response from server");
 			 }
 		 }catch(Exception e) {
 				 e.printStackTrace();
 		 }
     }
     

	/**
	
	Fills the instructions box with relevant instructions for the student.
	
	This method retrieves the logged-in user and sets the instructions box as non-editable.
	It appends the student notes from the studentExam's associated exam to the instructions box.
	*/
     public void fillInstructions() {
    	 User user =null;
    	 try {
		 user = LoggedInUser.getInstance().getUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 instructionsBox.setEditable(false);
    	 instructionsBox.appendText("Student Notes:\n");
    	 String studentNotes = studentExam.getExam().getStudentNotes();
    	 instructionsBox.appendText(studentNotes);
    	
    	 
     }
     
     
     

}
