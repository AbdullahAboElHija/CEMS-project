package gui_lecturer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
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

public class ExamActivationBoundary implements Initializable{
	
    @FXML
    private Label userName;
	
    @FXML
    private Button logoutBtn;
    
    @FXML
    private AnchorPane window;

    @FXML
    private TableView<Exam> ExamsTable;

    @FXML
    private TableColumn<Exam, String> IDCol;

    @FXML
    private TableColumn<Exam, Profession> professionCol;

    @FXML
    private TableColumn<Exam, Course> courseCol;

    @FXML
    private TableColumn<Exam, Integer> durCol;

    @FXML
    private Button activateExamBtn;

    @FXML
    private Button backButton;
    
    @FXML
    private ComboBox<Profession> professionsCombo;
    
    @FXML
    private ComboBox<Course> coursesCombo;
    
    @FXML
    private TableColumn<Exam, String> typeCol;
    
    @FXML
    private TextField ActivationCode;
    
    @FXML
    private Label ErrMsg;
    
    @FXML
    private Label correctMsg;
    
    private ObservableList<Exam> examsInTable;
    private ObservableList<Profession> professions = FXCollections.observableArrayList();
    private ObservableList<Course> courses = FXCollections.observableArrayList();
     private ArrayList<Exam> examsByLec =new ArrayList<>();
     
     /**
      * Initializes the controller. This method is automatically called after the FXML file has been loaded.
      * It performs necessary initialization tasks such as setting up event listeners, populating UI elements, and retrieving data from the server.
      * @param location  The location used to resolve relative paths for the root object, or {@code null} if the location is not known.
      * @param resources The resources used to localize the root object, or {@code null} if the root object was not localized.
      */
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
				e.printStackTrace();
		}
    	
    	IDCol.setCellValueFactory(new PropertyValueFactory<>("ExamID"));
    	professionCol.setCellValueFactory(new PropertyValueFactory<>("professionName"));
    	courseCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
    	durCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
    	typeCol.setCellValueFactory(new PropertyValueFactory<>("examType"));
    	examsInTable=FXCollections.observableArrayList();
    	ExamsTable.setItems(examsInTable);
    	
    	//filling the lecturers professions in the combo box
    	ProfessionsController.fillProfessionsCombo(professionsCombo, professions);
		professionsCombo.getItems().add(new Profession("All","All"));
		try {
			Lecturer lec = (Lecturer)LoggedInUser.getInstance().getUser();
			Msg msg= new Msg("getExamsByLec",MsgType.FROM_CLIENT,lec);
			ClientUI.chat.accept(msg);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		Msg response =CEMSClient.responseFromServer;
		try {
			if(!response.getMsg().equals("getExamsByLec")) {
				System.out.println(response.getMsg());
				throw new Exception("Unexpected response from server");
			}
		}catch(Exception e) {e.printStackTrace();}
		examsByLec= (ArrayList<Exam>)CEMSClient.responseFromServer.getData();	
		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setVisible(false);
    	 }
	}
    
	/**
	 * Handles the click event of the "Back" button.
	 * @param event The ActionEvent triggered by clicking the button.
	 */
    @FXML
    void clickBackButton(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "LecturerMenu", event,true);

    }
    
    /**
     * Handles the click event of the "Activate Exam" button.
     *checks for validity of the code entered.
     * @param event The ActionEvent triggered by clicking the button.
     */
    @FXML
    void activateExam(ActionEvent event) {
		ErrMsg.setText("");
		correctMsg.setText("");
    	String actCode = ActivationCode.getText();
    	Exam selectedExam = ExamsTable.getSelectionModel().getSelectedItem();
    	if ((selectedExam==null)) {
    		ErrMsg.setText("You must select an exam.");
    	}		
    	else if(actCode.length()==0) {
    		ErrMsg.setText("You must enter activation code.");
    	}
    	else if(actCode.length()!=4) {
    		ErrMsg.setText("You must enter 4 digits activation code.");
    	}
    	else if(!isAlphaNumeric(actCode)) {
    		ErrMsg.setText("You must enter 4 alpha-numeric activation code.");
    	}
    	else{
			ActivatedExam actExam = new ActivatedExam(actCode,selectedExam);
			Msg activeExamMsg = new Msg("activateExam",MsgType.FROM_CLIENT,actExam);
			ClientUI.chat.accept(activeExamMsg);
			//if the activation code used for another Exam
			if(CEMSClient.responseFromServer.getMsg().equals("activateExamCodeExist")) {
	    		ErrMsg.setText("This code used for another exam\nplease choose another one");
			}
			else{correctMsg.setText("Exam activated.");}
		}
    }
    
    /**
     * checks if the input is alpha-numerical
     * @param input
     * @return
     */
    public static boolean isAlphaNumeric(String input) {
        String pattern = "^[a-zA-Z0-9]+$";
        return input.matches(pattern);
    }
    
    /**
     * Handles the selection of a course from the courses combo box.
     * @param event The ActionEvent triggered by selecting a course.
     */
    @FXML
    void chooseCourse(ActionEvent event) {
    	Course course = coursesCombo.getValue();
		if (course != null) {
			examsInTable.clear();
			int i = 0;
			for (Exam ex : examsByLec) {
				if (ex.getCourseID().equals(course.getCourseID()) || course.getCourseID().equals("All"))
					examsInTable.add(ex);
			}
		}
    }
    
    /**
     * Handles the selection of a profession from the professions combo box.
     * @param event The ActionEvent triggered by selecting a profession.
     */
    @FXML
    void chooseProfession(ActionEvent event) {
    	Profession prof = professionsCombo.getValue();
		if (prof != null) {
			CoursesController.fillCourseComboByProfession(coursesCombo, courses, prof);
			coursesCombo.getItems().add(new Course("All","All",prof.getProfessionID()));
			examsInTable.clear();
			int i = 0;
			for (Exam ex : examsByLec) {

				if (ex.getProfessionID().equals(prof.getProfessionID()) || prof.getProfessionID().equals("All"))
					examsInTable.add(ex);
			}
		}
    	
    }
    
    /**
     * Handles the click event of the "Logout" button.
     * @param event The ActionEvent triggered by clicking the button.
     */
    @FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }

	

}
