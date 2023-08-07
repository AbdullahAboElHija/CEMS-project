package gui_lecturer;

//import java.awt.event.MouseEvent;
import java.io.IOException;

import control_lecturer.ProfessionsController;
import java.net.URL;
import entity.Lecturer;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_lecturer.QuestionBankController;
import entity.LoggedInUser;
import entity.Profession;
import entity.Question;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import logic.Msg;
import logic.MsgType;
import java.util.ArrayList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

/**
 * Controller class for managing the Question Bank boundary in the lecturer's GUI.
 */
public class QuestionBankBoundary implements Initializable {
	
    @FXML
    private Label userName;
    
    @FXML
    private AnchorPane window;

	@FXML
	private TableView<Question> Qtable;

	@FXML
	private TableColumn<Question, Integer> questionIDCol;

	@FXML
	private ComboBox<Profession> professionComboBox;

	@FXML
	private TableColumn<Profession, Integer> professionNameCol;

	@FXML
	private TableColumn<Question, Integer> contentCol;

	@FXML
	private Button BackBtn;

	@FXML
	private Button DeleteQuestionBtn;

	@FXML
	private Button EditQuestionBtn;

	@FXML
	private Button logoutBtn;

    @FXML
    private Label msgArea;
    
    @FXML
    private Button AddQuestion;

	private ObservableList<Question> questionsInTable;
	private ObservableList<Profession> professions = FXCollections.observableArrayList();
	private ArrayList<Question> questionsByLec = new ArrayList<Question>();

	/* initialize the QuestionBank frame */
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
 			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// initialize table view
		questionIDCol.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		professionNameCol.setCellValueFactory(new PropertyValueFactory<>("professionName"));
		contentCol.setCellValueFactory(new PropertyValueFactory<>("question"));
		questionsInTable = FXCollections.observableArrayList();
		Qtable.setItems(questionsInTable);

		// load Professions of lecturer to combo box
		
		ProfessionsController.fillProfessionsCombo(professionComboBox, professions);
		professionComboBox.getItems().add(new Profession("All","All"));

		// load all Questions written by lecturer
		questionsByLec = QuestionBankController.loadQuestionsByLec();
		QuestionBankController.setChosenQuestion(null);
		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setDisable(true);
    		 DeleteQuestionBtn.setDisable(true);
    		 EditQuestionBtn.setDisable(true);
    		 AddQuestion.setDisable(true);
    		 
    	 }
	}

	 /**
     * Handles the click event of the "Add Question" button.
     *
     * @param event The action event triggered by clicking the button.
     */
	@FXML
	void clickAddQuestion(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/CreateQuestion.fxml", "CreateQuestion", event, true);
	}

	 /**
     * Handles the click event of the "Back" button.
     *
     * @param event The action event triggered by clicking the button.
     */
	@FXML
	void clickBackBtn(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "Lecturer Menu", event, true);
	}

	/**
     * Handles the click event of the "Delete Question" button.
     *
     * @param event The action event triggered by clicking the button.
     */
	@FXML
	void clickDeleteQuestionBtn(ActionEvent event) {
		showMsg("");
		if (QuestionBankController.getChosenQuestion() != null) {
			if(QuestionBankController.getChosenQuestion().getUsedCounter()!=0) {
				showMsg("Cannot delete a question used in exams");
			}
			else {
				Msg msg= new Msg("deleteQuestion",MsgType.FROM_CLIENT,QuestionBankController.getChosenQuestion());
				ClientUI.chat.accept(msg);
				questionsByLec.remove(QuestionBankController.getChosenQuestion());
				questionsInTable.remove(QuestionBankController.getChosenQuestion());
				QuestionBankController.setChosenQuestion(null);
				
				
			}
		}
		else {
			showMsg("Choose a question to delete");
		}
	}

	/**
     * Handles the click event of the "Edit Question" button.
     *
     * @param event The action event triggered by clicking the button.
     * @throws IOException If an I/O error occurs.
     */
	@FXML
	void clickEditQuestionBtn(ActionEvent event) throws IOException {
		showMsg("");
		if (QuestionBankController.getChosenQuestion() != null) {
			if(QuestionBankController.getChosenQuestion().getUsedCounter()!=0) {
				showMsg("Cannot edit a question used in exams");
			}
			else {
				GuiCommon.getInstance().displayNextScreen("/gui_lecturer/EditQuestion.fxml", "Edit Question", event, true);
			}	
		}
		else {
			showMsg("Choose a question to edit");
		}
	}


    /**
     * Handles the click event of the "Logout" button.
     *
     * @param event The action event triggered by clicking the button.
     */
	@FXML
	void clickLogoutBtn(ActionEvent event) {
		LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
	}

	/**
     * Handles the click event of the "Main Menu" button.
     *
     * @param event The action event triggered by clicking the button.
     */
	@FXML
	void clickMainMenu(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "Lecturer Menu", event, true);
	}

	/**
     * Handles the selection event of a question in the table.
     *
     * @param event The mouse event triggered by selecting a question in the table.
     */
	@FXML
	void selectQuestion(MouseEvent event) {
		QuestionBankController.setChosenQuestion( Qtable.getSelectionModel().getSelectedItem());
	}

	/**
     * Updates the table to display only questions of the chosen profession.
     *
     * @param event The action event triggered by selecting a profession from the combo box.
     */
	@FXML
	void chooseProfession(ActionEvent event) {
		Profession prof = professionComboBox.getValue();
		QuestionBankController.setChosenQuestion(null);
		if (prof != null) {
			questionsInTable.clear();
			int i = 0;
			for (Question q : questionsByLec) {

				if (q.getProfessionID().equals(prof.getProfessionID()) || prof.getProfessionID().equals("All"))
					questionsInTable.add(q);
			}
		}
	}
	
	 /**
     * Displays a message in the message area.
     *
     * @param msg The message to be displayed.
     */
	public void showMsg( String msg) {
    	msgArea.setText(msg);
    }
}
