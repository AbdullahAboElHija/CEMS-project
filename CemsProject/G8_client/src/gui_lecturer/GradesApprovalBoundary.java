package gui_lecturer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import control_lecturer.GradesApprovalController;
import entity.Exam;
import entity.LoggedInUser;
import entity.StudentExam;
import entity.User;
import gui.GuiCommon;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class GradesApprovalBoundary  implements Initializable{
	
	@FXML
    private TableView<StudentExam> gradesApprovalTable;
	

    @FXML
    private TableColumn<StudentExam, String> approvalStatusCol;

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<StudentExam, String> examCodeCol;

    @FXML
    private TableColumn<StudentExam, String> examIdCol;

    @FXML
    private TableColumn<StudentExam, Integer> gradeCol;

    @FXML
    private TableColumn<StudentExam, String> studentIdCol;

    @FXML
    private Button viewAndApproveBtn;

    @FXML
    private AnchorPane window;
    
    @FXML
    private Label msgArea;
    
    @FXML
    private Button logoutBtn;
    
    @FXML
    private Label userName;
    
    private ObservableList<StudentExam> studentExamsInTable;
    private ArrayList<StudentExam> examCopiesByLec;
    private User user;
    
    /**
     * Initializes the controller. This method is automatically called
     * after the FXML file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
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
 			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			e.printStackTrace();
		}
 		
 		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setDisable(true);

    	 }
	}
    
    /**
     * Sets up the user for the current session.
     */
    private void setupUser() {
    	try {
			user = LoggedInUser.getInstance().getUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Sets up the properties of the table.
     */
    private void setupTableProperties() {
    	examIdCol.setCellValueFactory(new PropertyValueFactory<>("examId"));
	 	examCodeCol.setCellValueFactory(new PropertyValueFactory<>("activationCode"));
	 	studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
	 	gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
	 	approvalStatusCol.setCellValueFactory(new PropertyValueFactory<>("approvalStatus"));
	 	studentExamsInTable=FXCollections.observableArrayList();
	 	gradesApprovalTable.setItems(studentExamsInTable); 
    }
    
    /**
     * Sets up the content of the table.
     */
    private void setupTableContent() {
    	Msg msg1 = new Msg("getStudentsExamCopyOfLec",MsgType.FROM_CLIENT,user.getId());
    	ClientUI.chat.accept(msg1);
	 	Msg response = CEMSClient.responseFromServer;
	 	try {
	 		if( !response.getMsg().equals("getStudentsExamCopyOfLec")) {
    	 		throw new Exception("Unexpected server response");
    	 	}
	 	}catch(Exception e) {
	 		e.printStackTrace();
	 	}
    	examCopiesByLec= (ArrayList<StudentExam>)response.getData();
    	ArrayList<StudentExam> itemsToRemove = new ArrayList<>(); 
	 	for(StudentExam studentExam : examCopiesByLec) {
	 		//System.out.println(studentExam);
	 		if(!studentExam.getStatus().equals("finished")) {
	 			 itemsToRemove.add(studentExam);
	 		}else {
	 			studentExamsInTable.add(studentExam);	 			
	 		}
	 	}
	 	examCopiesByLec.removeAll(itemsToRemove);
    }
    
    
    /**
     * Handles the action of clicking the back button.
     *
     * @param event The ActionEvent triggered by clicking the button.
     */
    @FXML
    void clickBack(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "Choose Report", event, true);
    }

    /**
     * Handles the action of clicking the view and approve button.
     *
     * @param event The ActionEvent triggered by clicking the button.
     */
    @FXML
    void clickViewAndApprove(ActionEvent event) {
    	StudentExam sExam = GradesApprovalController.getChosenStudentExam();
    	if(sExam!= null) {
    		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ApproveGradeForm.fxml", "Approve Grade", event,true);
    	}else {
    		showMsg("You must choose a student's exam first");
    	}
    	
    }
    
    /**
     * Handles the action of choosing a student's exam.
     *
     * @param event The MouseEvent triggered by choosing the exam.
     */
    @FXML
    void chooseStudentExam(MouseEvent event) {
    	GradesApprovalController.setChosenStudentExam(gradesApprovalTable.getSelectionModel().getSelectedItem()); 
    }
    
    /**
     * Displays the given message in the message area.
     *
     * @param msg The message to be displayed.
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
