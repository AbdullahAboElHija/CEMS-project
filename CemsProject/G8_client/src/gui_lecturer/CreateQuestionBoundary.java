package gui_lecturer;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_lecturer.ProfessionsController;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class CreateQuestionBoundary implements Initializable {
	
    @FXML
    private Label userName;
	
    @FXML
    private Button logoutBtn;
    
	@FXML
	private AnchorPane window;

	@FXML
	private Button backBtn;

	@FXML
	private Button AddQuestionBtn;

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
	private TextArea questionField;

	@FXML
	private ComboBox<Profession> professionComboBox;

	@FXML
	private ComboBox<Integer> CorrentAnswerComboBox;

	@FXML
	private Label msgArea;

	private ObservableList<Profession> professions = FXCollections.observableArrayList();
	private Integer selectedIndex = null;
	private Profession selectedProfession;
	private Integer[] answerNumbers = { 1, 2, 3, 4 };
	
	/**
	 * Initializes the controller after its root element has been completely processed.
	 *
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
 			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			e.printStackTrace();
		}

		ProfessionsController.fillProfessionsCombo(professionComboBox, professions);
		CorrentAnswerComboBox.setItems(FXCollections.observableArrayList(answerNumbers));
		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setVisible(false);
    	 }

	}
	
	/**
	 * Handles the click event of the "Add Question" button.
	 *
	 * @param event The ActionEvent triggered by clicking the button.
	 * @throws Exception If an unexpected message type is received from the server.
	 */
	@FXML
	void clickAddQuestionBtn(ActionEvent event) throws Exception {
		if (questionField.getText().trim().length() == 0) {
			showMsg("Please fill the Question Field");
		} else if (selectedProfession == null) {
			showMsg("Please choose a profession");
		} else if (answerAField.getText().trim().length() == 0 || answerBField.getText().trim().length() == 0
				|| answerCField.getText().trim().length() == 0 || answerDField.getText().trim().length() == 0) {
			showMsg("Please fill all answers");
		} else if (selectedIndex == null) {
			showMsg("Please choose a correct answer");
		} else {
			Lecturer lec = (Lecturer) LoggedInUser.getInstance().getUser();
			Question newQuestion = new Question("", selectedProfession.getProfessionID(),
					selectedProfession.getProfessionName(), lec.getId(), questionField.getText(),
					answerAField.getText(), answerBField.getText(), answerCField.getText(), answerDField.getText(),
					selectedIndex, instructionField.getText(), 0);

			// create
			Msg msg1 = new Msg("createNewQuestion", MsgType.FROM_CLIENT, newQuestion);
			ClientUI.chat.accept(msg1);
			// get question id given to question
			Msg response = CEMSClient.responseFromServer;
			if (!response.getMsg().equals("createNewQuestion")) {
				throw new Exception("Unexpected message type recieved");
			}

			// show pop up msg question saved
			newQuestion = (Question) response.getData();
			GuiCommon.getInstance().displayNextScreen("/gui_lecturer/QuestionBank1.fxml", "QuestionsBank", event, true);
			GuiCommon.getInstance().popUp("Question of ID:" + newQuestion.getQuestionID() + " saved successfully");
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
	 * Handles the selection of a correct answer from the combo box.
	 *
	 * @param event The ActionEvent triggered by selecting a correct answer.
	 */
	@FXML
	void selectCorrectAnswer(ActionEvent event) {
		selectedIndex = CorrentAnswerComboBox.getValue();
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
