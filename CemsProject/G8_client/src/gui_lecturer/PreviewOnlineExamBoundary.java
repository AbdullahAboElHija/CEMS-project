package gui_lecturer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_common.NotificationController;
import control_lecturer.CreateExamController;
import entity.Exam;
import entity.LoggedInUser;
import entity.Question;
import entity.StudentExam;
import entity.Time;
import entity.User;
import entity.UserType;
import gui.GuiCommon;
import gui_student.PerformExamController;
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

public class PreviewOnlineExamBoundary implements Initializable{

    @FXML
    private Label userName;
    
    @FXML
    private AnchorPane window;

	@FXML
	private VBox questionContainer;


	private StudentExam studentExam;

	@FXML
	private TextArea instructionsBox;
	
	

	/**
		Initializes the controller when the corresponding FXML file is loaded.
		@param location The URL location of the FXML file.
		@param resources The ResourceBundle associated with the FXML file.
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

	} catch (Exception e) {
		e.printStackTrace();
	}

		
		ArrayList<Question> questionsOfExam = CreateExamController.questions;
		studentExam = new StudentExam();
		studentExam.setExam(CreateExamController.examForPrev);
		studentExam.setExamQuestions(questionsOfExam);

		setupVbox(questionsOfExam);
		 fillInstructions() ;
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
	}


	


	/**
	
	Sets up the VBox container with questions for the exam.
	
	@param questionsOfExam An ArrayList of Question objects representing the questions for the exam.
	
	
	
	*/
	public void setupVbox(ArrayList<Question> questionsOfExam) {
		for (int i = 0; i < questionsOfExam.size(); i++) {
			Label questionLabel = new Label((i+1) +". "+questionsOfExam.get(i).getQuestion());
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
//    	 instructionsBox.setEditable(false);
//    	 instructionsBox.appendText("Student Notes:\n");
//    	 String studentNotes = studentExam.getExam().getStudentNotes();
//    	 instructionsBox.appendText(studentNotes);
//    	 
//    	 if(user.getUserType()==UserType.Lecturer) {
//    		 instructionsBox.appendText("Lecturer Notes:\n");
//        	 String lecturerNotes = studentExam.getExam().getLecturerNotes();
//        	 instructionsBox.appendText(lecturerNotes);
//    	 } 
    	 instructionsBox.setEditable(false);
			instructionsBox.appendText("Student Instructions:\n");
			instructionsBox.appendText(studentExam.getExam().getStudentNotes());
			if(user.getUserType()==UserType.Lecturer) {
				instructionsBox.appendText("\nLecturer Instructions:\n");
				instructionsBox.appendText(studentExam.getExam().getLecturerNotes());
			}
    	 
     }
     
     
}
