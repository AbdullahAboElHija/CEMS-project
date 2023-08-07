package gui_lecturer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import client.ClientUI;
import control_common.LoginController;
import control_lecturer.GradesApprovalController;
import entity.LoggedInUser;
import entity.Question;
import entity.StudentExam;
import entity.User;
import entity.UserType;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class ApproveGradeFormBoundary implements Initializable{
	 @FXML
	    private Button approveBtn;

	    @FXML
	    private Button backBtn;

	    @FXML
	    private TextArea instructionsBox;

	    @FXML
	    private Label msgArea;

	    @FXML
	    private TextField newGradeText;

	    @FXML
	    private Button noBtn;

	    @FXML
	    private TextArea notesForStudentArea;

	    @FXML
	    private VBox questionContainer;

	    @FXML
	    private TextArea reasonTextArea;

	    @FXML
	    private Label studentIdLabel;

	    @FXML
	    private AnchorPane window;

	    @FXML
	    private Button yesBtn;
	    
	    @FXML
	    private Label reasonLabel;
	    
	    @FXML
	    private Label gradeLabel;
	    
	    @FXML
	    private Label examIdLabel;
	    
	    @FXML
	    private Button logoutBtn;
	    
	    @FXML
	    private Label userName;
	    
	    private User user;
	    private StudentExam studentExam;
	    private boolean change =false;
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
	    	//setup window closing 
	    	Platform.runLater(() -> {
	    		 Stage stage = (Stage) window.getScene().getWindow();
	    	    	stage.setOnCloseRequest(event -> {
	    	            event.consume(); // Consume the event to prevent automatic window closing
	    	            LoginController.disconnect();
	    	            stage.close();
	    	        });
	         });
	    	
	    	setupUser();
	    	
	    	
			studentExam= GradesApprovalController.getChosenStudentExam();
			ArrayList<Question> questionsOfExam=studentExam.getExam().getExamQuestions();
			setupVbox(questionsOfExam); 
			setupScreenByUser();
			
			setupInstructionsBox();
			studentIdLabel.setText(studentExam.getStudentId());
			gradeLabel.setText(studentExam.getGrade().toString());
			examIdLabel.setText(studentExam.getExamId());

	 		try {
	 			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		
	 		 if(LoggedInUser.getHeadDep()!=null) {
	    		 logoutBtn.setDisable(true);
	    		 approveBtn.setDisable(true);
	    		 
	    	 }
			
			
	    }
	    
	    /**
	     * Setup the screen according to the user that's logged in 
	     * both the student and the lecturer can use this boundary 
	     * Lecturer view: to view and approve a grade of an exam 
	     * Student view: to view the graded exam  
	     * 
	     */
	    public void setupScreenByUser() {
	    	if(user.getUserType()==UserType.Lecturer) {
	    		chooseNo(); 
	    		alreadyGraded(); 
	    	}
	    	
	    	if(user.getUserType()==UserType.Student) {
	    		approveBtn.setVisible(false);
	    		newGradeText.setVisible(false);
	    		yesBtn.setDisable(true);
	    		noBtn.setDisable(true);
	    		notesForStudentArea.setEditable(false);
	    		notesForStudentArea.setText(studentExam.getLecturerNotes());
	    		reasonTextArea.setEditable(false);
	    		if (studentExam.isGradeChanged()) {
	    			yesBtn.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	    			reasonTextArea.setText(studentExam.getReasonOfGradeChange());
	    		}else {
	    			noBtn.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	    			reasonTextArea.setText("No grade change was made");
	    			reasonTextArea.setDisable(true);
	    		}
	    	}
	    	
	    	
	    }
	    
	    /**
	     * stores the user logged in currently to the user variable
	     */
	    public void setupUser() {
	    	try {
				user = LoggedInUser.getInstance().getUser();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	    
	    /**
	     * sets  up the instructions box of the lecturer
	     */
	    public void setupInstructionsBox() {
	    	instructionsBox.setEditable(false);
			instructionsBox.appendText("Student Instructions:\n");
			instructionsBox.appendText(studentExam.getExam().getStudentNotes());
			if(user.getUserType()==UserType.Lecturer) {
				instructionsBox.appendText("\nLecturer Instructions:\n");
				instructionsBox.appendText(studentExam.getExam().getLecturerNotes());
			}
	    }
	    
	    /**
	     * sets up the screen accordingly if the exam's grade was already approved by lecturer
	     */
	    public void alreadyGraded() {
	    	if (studentExam.isAvailableForVieweing()) {
	    		yesBtn.setDisable(true);
	    		notesForStudentArea.setDisable(true);
	    		notesForStudentArea.setText(studentExam.getLecturerNotes());
	    		reasonTextArea.setText(studentExam.getReasonOfGradeChange());
	    		approveBtn.setDisable(true);
	    		showMsg("This grading has already been approved");
	    	}
	    }
	    
	    /**
	     * Sets up the VBox to include all the questions and their answers and the students answer to each question and a label stating whether the answer was correct or incorrect
	     * @param questionsOfExam the questions of the exam
	     */
	    public void setupVbox(ArrayList<Question> questionsOfExam) {
	    	for (int i = 0; i < questionsOfExam.size(); i++) {
	        	int qNum=i+1;
	            Label questionLabel = new Label(qNum+". "+questionsOfExam.get(i).getQuestion());
	            questionContainer.getChildren().add(questionLabel);

	            Label questionInstrucLabel = new Label(questionsOfExam.get(i).getQuestionInstructions());
	            questionContainer.getChildren().add(questionInstrucLabel);

	            String[] answersQ = {
	                questionsOfExam.get(i).getAnswer1(),
	                questionsOfExam.get(i).getAnswer2(),
	                questionsOfExam.get(i).getAnswer3(),
	                questionsOfExam.get(i).getAnswer4()
	            };
	            
	            ToggleGroup answerGroup = new ToggleGroup();
	            AtomicReference<RadioButton> selectedAnswerOption = new AtomicReference<>(null); // To store the selected radio button

	            for (int j = 0; j < answersQ.length; j++) {
	                RadioButton answerOption = new RadioButton(answersQ[j]);
	                answerOption.setToggleGroup(answerGroup);

	                // Check if this radio button should be selected based on certain condition
//	                if (questionsOfExam.get(i).getStudentAnswer()==questionsOfExam.get(i).getCorrectAnswer()) {
//	                    answerOption.setSelected(true);
//	                    selectedAnswerOption.set(answerOption);
//	                   // selectedAnswerOption = answerOption;
//	                }
	                if (questionsOfExam.get(i).getStudentAnswer()==j+1) {
	                    answerOption.setSelected(true);
	                    selectedAnswerOption.set(answerOption);
	                   // selectedAnswerOption = answerOption;
	                }

	                questionContainer.getChildren().add(answerOption);
	            }
	            Label result;
	            String correct;
	            String points;
	            
	            if(questionsOfExam.get(i).isCorrectAns() ) {
	            	correct="correct, ";
	            	points = questionsOfExam.get(i).getQuestionPoints();
	            	points+=" points";
	            	result = new Label(correct+points);
		             result.setTextFill(Color.GREEN);
	            }else {
	            	correct="incorrect, ";
		             points="0 points";
		             result = new Label(correct+points);
		             result.setTextFill(Color.RED);
	            }
	            questionContainer.getChildren().add(result);
	           
	            // Disable the other radio buttons in the ToggleGroup
	            answerGroup.getToggles().forEach(toggle -> {
	                RadioButton radioButton = (RadioButton) toggle;
	                if (radioButton != selectedAnswerOption.get()) {
	                    radioButton.setDisable(true);
	                }
	            });

	            Separator separator = new Separator();
	            questionContainer.getChildren().add(separator);
	        }
	    }
	    
	    /**
	     * 
	     * @param event
	     */
	    @FXML
	    void clickApprove(ActionEvent event) {
	    	String newgrade = newGradeText.getText();
    		String reason=reasonTextArea.getText();
    		
    		
	    	if(change) {
	    		if(newgrade.trim().length()==0) {
	    			showMsg("Enter new grade or select no grade change.");
	    			return;
	    		}
	    		String regex = "^[0-9]+$";
	    	    if( !newgrade.matches(regex)) {
	    	    	showMsg("Invalid grade");
	    	    	return;	    	
	    	    }
	    	    int g = Integer.parseInt(newgrade);
	    		if(g<0 || g>100){
	    			showMsg("New  grade must be in the range 0 -100");
	    			return;
	    		}
	    		else if(reason.trim().length()==0){
	    			showMsg("Enter reason for grade change or select no grade change.");
	    			return;
	    		}
	    		
	    	}else {
	    		newgrade=studentExam.getGrade().toString();
	    		reason="";
	    	}
	    	int g= Integer.parseInt(newgrade);
	    	studentExam.setGrade(g);
	    	studentExam.setGradeChanged(change);
	    	studentExam.setReasonOfGradeChange(reason);
	    	studentExam.setLecturerNotes(notesForStudentArea.getText());
	    	studentExam.setAvailableForVieweing(true);
	    	
	    	// save data in db
	    	//send the student alert that grade is available
	    	Msg msg = new Msg("gradeApproved", MsgType.FROM_CLIENT,studentExam);
	    	ClientUI.chat.accept(msg);
	     	
	    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/GradesApproval.fxml", "Grades Approval", event,true);
	    	GuiCommon.getInstance().popUp("Grade Approved");
	    }
	    
	    /**
	     * called when clicking back button
	     * differentiates between a lecturer user and a student user
	     * @param event
	     */
	    @FXML
	    void clickBack(ActionEvent event) {
	    	if(user.getUserType()==UserType.Lecturer) {
	    		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/GradesApproval.fxml", "Grades Approval", event,true);
	    	}
	    	if(user.getUserType()==UserType.Student) {
	    		GuiCommon.getInstance().displayNextScreen("/gui_student/GradedExamsTable.fxml", "Grades Approval", event,true);
	    	}
	    	
	    }
	    
	    /**
	     * lecturer clicks NO button for grade change 
	     * @param event
	     */
	    @FXML
	    void clickNo(ActionEvent event) {
	    	chooseNo(); 
	    	change=false;
	    }
	    
	    /**
	     * lecturer clicks yes for grade change
	     * @param event
	     */
	    @FXML
	    void clickYes(ActionEvent event) {
	    	yesBtn.setDisable(true);
	    	noBtn.setDisable(false);
	    	//newGradeText.setEditable(true);
	    	newGradeText.setDisable(false);
	    	reasonLabel.setDisable(false);
	    	//reasonTextArea.setEditable(true);
	    	reasonTextArea.setDisable(false);
	    	change=true;
	    }
	    
	    /**
	     * called whenever lecturer chooses no to grade change, enables/disables appropriate buttons
	     */
	    private void chooseNo() {
	    	yesBtn.setDisable(false);
	    	noBtn.setDisable(true);
	    	newGradeText.setDisable(true);
	    	//newGradeText.setEditable(false);
	    	reasonLabel.setDisable(true);
	    	reasonTextArea.setDisable(true);
	    }
	    
	    /**
	     * shows msg to messages area
	     * @param msg msg to be shown 
	     */
	    public void showMsg( String msg) {
	    	msgArea.setText(msg);
	    }
	    
	    @FXML
	    void clickLogout(ActionEvent event) {
	    	LoginController.logOut();
	    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
	    }
		

}
