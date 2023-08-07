package gui_lecturer;

import java.net.URL;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_lecturer.QuestionBankController;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import logic.Msg;
import logic.MsgType;

public class EditQuestionBoundary implements Initializable {
	
    @FXML
    private Label userName;
	
    @FXML
    private Button logoutBtn;
    
	@FXML
	private AnchorPane window;

	@FXML
	private Button backBtn;

	@FXML
	private Button SaveQuestionBtn;

	@FXML
	private ComboBox<Integer> correctAnswerComboBox;

	@FXML
	private TextField answerAField;

	@FXML
	private TextField answerBField;

	@FXML
	private TextField answerCField;

	@FXML
	private TextField answerDField;

	@FXML
	private TextArea instructionField;

	@FXML
	private ComboBox<Profession> professionComboBox;

	@FXML
	private TextArea questionField;

	@FXML
	private Label qIDLabel;

	@FXML
	private Label authorLabel;

	@FXML
	private Label msgArea;

	private String selectedQuestionId;
	private Integer selectedIndex = 0;
	private Profession selectedProfession;
	private Question curQuestion;
	private Integer[] answerNumbers = { 1, 2, 3, 4 };

	ObservableList<Profession> items = FXCollections.observableArrayList();

//	public String getSelectedQuestionId() {
//		return selectedQuestionId;
//	}
//
//	public void setSelectedQuestionId(String selectedQuestionId) {
//		this.selectedQuestionId = selectedQuestionId;
//	}

	/**
	 * Initializes the controller after its root element has been completely processed.
	 *
	 * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String userFullName = null;
		try {
			userFullName = LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
 			userName.setText("Hi "+userFullName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		curQuestion = QuestionBankController.getChosenQuestion();
		qIDLabel.setText(curQuestion.getQuestionID());
		authorLabel.setText(userFullName);
		questionField.setText(curQuestion.getQuestion());
		answerAField.setText(curQuestion.getAnswer1());
		answerBField.setText(curQuestion.getAnswer2());
		answerCField.setText(curQuestion.getAnswer3());
		answerDField.setText(curQuestion.getAnswer4());
		correctAnswerComboBox.setValue(curQuestion.getCorrectAnswer());
		instructionField.setText(curQuestion.getQuestionInstructions());
		items.add(curQuestion.getProfession());
		professionComboBox.setItems(items);
		professionComboBox.setConverter(new StringConverter<Profession>() {
			@Override
			public String toString(Profession prof) {
				return prof.getProfessionName();
			}

			@Override
			public Profession fromString(String string) {
				return null;
			}
		});
		professionComboBox.setValue(curQuestion.getProfession());
		professionComboBox.setEditable(false);
		professionComboBox.setDisable(true);
		correctAnswerComboBox.setItems(FXCollections.observableArrayList(answerNumbers));
		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setVisible(false);
    	 }
	}
	
	/**
	 * Handles the selection of a profession from the combo box.
	 *
	 * @param event The ActionEvent triggered by choosing a profession.
	 */
	@FXML
	void chooseProfession(ActionEvent event) {
		selectedProfession = professionComboBox.getValue();
	}
	
	/**
	 * Handles the click event of the "Save Question" button.
	 *
	 * @param event The ActionEvent triggered by clicking the button.
	 * @throws Exception If an unexpected message type is received from the server.
	 */
	@FXML
	void clickSaveQuestionBtn(ActionEvent event) throws Exception {
		if (questionField.getText().trim().length() == 0) {
			showMsg("Please fill the Question Field");
		} else if (answerAField.getText().trim().length() == 0 || answerBField.getText().trim().length() == 0
				|| answerCField.getText().trim().length() == 0 || answerDField.getText().trim().length() == 0) {
			showMsg("Please fill all answers");
		} else if (selectedIndex == null) {
			showMsg("Please choose a correct answer");
		} else {
			curQuestion.setQuestion(questionField.getText());
			curQuestion.setAnswer1(answerAField.getText());
			curQuestion.setAnswer2(answerBField.getText());
			curQuestion.setAnswer3(answerCField.getText());
			curQuestion.setAnswer4(answerDField.getText());
			curQuestion.setCorrectAnswer(selectedIndex);
			// curQuestion.setProfession(selectedProfession);
			curQuestion.setQuestionInstructions(instructionField.getText());
			Msg msg1 = new Msg("editQuestionDetails", MsgType.FROM_CLIENT, curQuestion);
			ClientUI.chat.accept(msg1);
			GuiCommon.getInstance().displayNextScreen("/gui_lecturer/QuestionBank1.fxml", "Question Bank", event, true);
			GuiCommon.getInstance().popUp("Question of ID:" + curQuestion.getQuestionID() + " saved successfully");
		}

	}
	
	/**
	 * Handles the selection of the correct answer from the combo box.
	 *
	 * @param event The ActionEvent triggered by choosing a correct answer.
	 */
	@FXML
	void selectCorrectAnswer(ActionEvent event) {
		selectedIndex = correctAnswerComboBox.getValue();
	}
	
	/**
	 * Handles the click event of the "Back" button.
	 *
	 * @param event The ActionEvent triggered by clicking the button.
	 */
	@FXML
	void clickBack(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/QuestionBank1.fxml", "QuestionsBank", event, true);
	}
	
	/**
	 * Sets the message text in the GUI.
	 *
	 * @param msg The message to be displayed.
	 */
	public void showMsg(String msg) {
		msgArea.setText(msg);
	}
	
	/**
	 * Log out the user and navigate to the login screen.
	 *
	 * @param event The ActionEvent triggered by clicking the "Logout" button.
	 */
    @FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }

}
