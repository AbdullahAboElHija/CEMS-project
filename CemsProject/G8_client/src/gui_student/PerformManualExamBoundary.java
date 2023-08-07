package gui_student;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.CEMSClient;
import client.ClientUI;
import common.MyFile;
import control_common.LoginController;
import control_common.NotificationController;
import entity.Exam;
import entity.LoggedInUser;
import entity.ManualExam;
import entity.StudentExam;
import entity.Time;
import entity.User;
import gui.GuiCommon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.FileChooser.ExtensionFilter;
import logic.Msg;
import logic.MsgType;
import entity.ManualExam;

/*
 * @author Abdullah
 * class for Student to make manualExam
 * 
 * */
public class PerformManualExamBoundary implements Initializable {

	@FXML
	private Label userName;

	@FXML
	private AnchorPane window;

	@FXML
	private Button downloadExamBtn;

	@FXML
	private Button uploadExamBtn;

	@FXML
	private TextField pathFile;

	@FXML
	private Button selectFileButton;

	@FXML
	private Label examID;

	@FXML
	private Label msgArea;

	@FXML
	private Label timerLabel;
	
    @FXML
    private Button logoutBtn;

	private String examIdCode;
	private StudentExam studentExam;
	private String code;

	Msg msgExam;

	Time time ;
	String alarmTime= "00:00:00";
	private boolean flag=true;
	private Timer notificationHandler;
	private boolean timeOver=false;
	
	Timeline timeline = new Timeline(
			new KeyFrame(Duration.seconds(1),
					e -> {
						if(time.getCurrentTime().equals(alarmTime) && flag && !timeOver) {
							terminateExam();
							flag=false;
							
						}
						if(!timeOver) {
							time.oneSecondPassedDownCount();
							timerLabel.setText(time.getCurrentTime());
						}
						
					}));
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		examID.setText(EnterActivationCodeBoundary.examIdCode);
		examIdCode = examID.getText();
		code=EnterActivationCodeBoundary.getCode();
		
		
		studentExam= new StudentExam();
		studentExam.setExam(PerformExamController.getPerformExam()); 
		time = new Time(studentExam.getExam().getDuration());
		
		registerStudentInExam();
		 
		
		 setupNotificationsHadler();
		 Platform.runLater(() -> {
    		 Stage stage = (Stage) window.getScene().getWindow();
    	    	stage.setOnCloseRequest(event -> {
    	            event.consume(); // Consume the event to prevent automatic window closing
    	        	terminateExam();
    	            LoginController.disconnect();
    	            stage.close();
    	        });
    	    	timerLabel.setText(time.getCurrentTime());
    			timeline.setCycleCount(Timeline.INDEFINITE);
    			timeline.play();
         });
		 

		

		try {
			userName.setText("Hi " + LoggedInUser.getInstance().getUser().getFirstName() + " "
					+ LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setVisible(false);
    	 }

	}

	private void terminateExam() {
		// TO DO SET SOLVING TIME OF EXAM
		timeOver=true;
		studentExam.setSolvingTime(time.getTimePassedInMinutes());
		studentExam.setStatus("finished");
		Msg msg = new Msg("terminateStudentExam", MsgType.FROM_CLIENT, studentExam);
		ClientUI.chat.accept(msg);
		Stage currentStage = (Stage) window.getScene().getWindow();
		currentStage.close();
		GuiCommon.getInstance().displayNextScreen("/gui_student/studentMenu.fxml", "Student Menu", null, false);
		GuiCommon.getInstance().popUp("Exam ended");
	}

	/*
	 * @author Abdullah
	 * 
	 * @Functionality : The downloadExam function allows the user to download a
	 * manual exam from the server. It prompts the user to select a directory to
	 * save the exam file, sends a request to the server to download the exam, and
	 * saves the received file in the selected directory.
	 * 
	 * @input : event: An ActionEvent object representing the event triggered by the
	 * user interface.
	 * 
	 */
	@FXML
	void downloadExam(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		Stage stage = (Stage) downloadExamBtn.getScene().getWindow();
		File selectedDirectory = directoryChooser.showDialog(stage);
		String pathFileName = selectedDirectory.getPath();

		ManualExam exam1 = new ManualExam(examIdCode, pathFileName);
		Msg msg1 = new Msg("StudentdownloadExam", MsgType.FROM_CLIENT, exam1);
		ClientUI.chat.accept(msg1);
		msgExam = CEMSClient.responseFromServer;
		MyFile exam = (MyFile) msgExam.getData();
		int fileSize = (exam).getSize();

		File myfile = new File((exam).getFileName());

		try {
			FileOutputStream fos = new FileOutputStream(myfile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			try {
				bos.write((exam).getMybytearray(), 0, fileSize);
				bos.flush();
				bos.close();
				fos.flush();
				fos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/*
	 * @author Abdullah
	 * 
	 * @Functionality : The selectFile function allows the user to select a file
	 * from their local system using a file chooser dialog. It retrieves the
	 * selected file's absolute path and sets it in a text field on the user
	 * interface.
	 * 
	 * @input : event: An ActionEvent object representing the event triggered by the
	 * user interface.
	 * 
	 */
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

	/*
	 * @author Abdullah
	 * 
	 * @Functionality : The uploadExam function is responsible for uploading a
	 * student's exam to the server, which in turn stores the exam data in the
	 * database. The function accepts a Word document file from the user interface,
	 * reads the file, and sends it to the server for processing and storage.
	 * 
	 * @input : event: An ActionEvent object representing the event triggered by the
	 * user interface.
	 * 
	 * 
	 */
	@FXML
	void uploadExam(ActionEvent event) {

		try {
			String LocalfilePath = pathFile.getText();
			if (!LocalfilePath.endsWith(".docx")) {
				showMsg("You Must Upload Word Document");
				return;
			}
			if (LocalfilePath.length() == 0) {
				showMsg("You must choose a file to upload");
				return;
			}
			File newFile = new File(LocalfilePath);
			String name = newFile.getName();
			MyFile msg = new MyFile(name);
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);

			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);
			bis.read(msg.getMybytearray(), 0, mybytearray.length);

			String studdentID = LoggedInUser.getInstance().getUser().getId();
			//ManualExam exam = new ManualExam(studdentID, examIdCode, msg);
			ManualExam exam = new ManualExam(studdentID, code, msg);
			Exam e= new Exam();
			e.setExamID(examIdCode);
			exam.setExam(e);
			Msg msg1 = new Msg("UploadFileStudentManualExam", MsgType.FROM_CLIENT, exam);
			ClientUI.chat.accept(msg1);
			//System.out.println(CEMSClient.responseFromServer.getMsg());
		} catch (Exception e) {
			System.out.println("Error send (Files)msg) to Server");
		}
		studentExam.setSubmitted(true);
		terminateExam();
	}


	// Method to check for server notification
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
	
	public void showMsg( String msg) {
    	msgArea.setText(msg);
    }
	
	

	/**
	
		Registers the student in the exam.
		
		This method retrieves the logged-in user, obtains the student ID, and sets it in the studentExam object.
		
		It then sends a registration message to the server using ClientUI.chat.
		
		It expects a response message from the server with the "registerStudentInExam" message type.
		
		If the response message is unexpected, it throws an exception and prints the stack trace.
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
	 
	 
	 //Checking notification every second
	 public void setupNotificationsHadler() {
	    	notificationHandler = new Timer();
	    	notificationHandler.scheduleAtFixedRate(new TimerTask() {
	 			@Override
	 			public void run() {
	 				// When extra time is received updates the timer and notifies the student
	 				checkServerNotification();
	 			}
	 			}, 0, 1000);
	     }
	
	
	
    @FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }

}
