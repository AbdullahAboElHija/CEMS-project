package gui_lecturer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import common.MyFile;
import control_common.LoginController;
import control_lecturer.CreateExamController;
import entity.Exam;
import entity.LoggedInUser;
import entity.ManualExam;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import logic.Msg;
import logic.MsgType;



/*@Abdullah Author
 * Class Controller for Creating manual Exam Form 
 * */
public class CreateManualExamBoundary implements Initializable {
	
    @FXML
    private Label userName;
	
    @FXML
    private Button logoutBtn;
	
    @FXML
    private AnchorPane window;

	@FXML
	private TextField pathFile;

	@FXML
	private Button selectFileButton;

	@FXML
	private Button uploadButton;
	
	@FXML
	  private Label msgArea;
	
	@FXML
    private Button cancelBtn;
	
    @FXML
    private Button back;
	
	String filePath;
	/*@Abdullah Author
	 * In event open file explorer for to choose file and upload it
	 * */
	@FXML
	void selectFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select File");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.*")); // Allow all file types
		File selectedFile = fileChooser.showOpenDialog(selectFileButton.getScene().getWindow());
		if (selectedFile != null) {
			pathFile.setText(selectedFile.getAbsolutePath());
		}
	}

	/*@Abdullah Author
	 * In Event sending the file to the server to Upload It
	 * */
	@FXML
	void CreateTheExam(ActionEvent event) throws IOException {
		String LocalfilePath = pathFile.getText();
		if(LocalfilePath.length()==0) {
			showMsg("You must choose a file to upload");
			return;
		}
		if (!LocalfilePath.endsWith(".docx")) {
			showMsg("You Must Upload Word Document!");
			return;
		}
		CreateExamController.fillExamDetails();
		CreateExamController.UploadManualExam(LocalfilePath);
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "LecturerMenu", event,true);
    	GuiCommon.getInstance().popUp("Manual Exam uploaded successfully");
	}
	
	/**
	 * called when user cancels the creating exam operation
	 * @param event
	 */
	 @FXML
	    void clickCancel(ActionEvent event) {
		 	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "LecturerMenu", event,true);
	    } 
	
	 /**
	  * shows message in the message area
	  * @param msg
	  */
	public void showMsg( String msg) {
    	msgArea.setText(msg);
    }
	
	/**
	 * sets up the screen
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setDisable(true);
    	 }

	}
	
	/**
	 * 
	 * @param event
	 */
    @FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }
    
	@FXML
	void backBtn(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ExamsBank.fxml", "ExamsBank", event, true);

	}
}
