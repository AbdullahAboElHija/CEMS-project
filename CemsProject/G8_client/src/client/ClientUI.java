package client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Msg;
import entity.LoggedInUser;
import entity.User;
import entity.UserType;
import gui_headDep.TimeRequestsBoundary;

import java.util.Vector;

import control_common.NotificationController;
//import client.ClientController;
import gui.ClientFrameBoundary;
import gui.GuiCommon;

public class ClientUI extends Application {
	
	public static ClientController chat; //only one instance
	public static LoggedInUser loggedInUser;
	
	//private static ClientFrameBoundary clientFrame ;//not used

	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		 GuiCommon.getInstance().displayNextScreen("/gui/client.fxml","Client",null, false);
	}

	public static void connectToServer(String serverIP, int defaultPort) {
		 chat= new ClientController( serverIP, defaultPort);
	}
	/**
	 * Handles server notification and shows by user type appropriate notification. if such is sent
	 * @param notificationFromServer
	 */
	public static void handleNotificationFrom(Msg notificationFromServer) {
		
		User user =LoggedInUser.getHeadDep();
		String phone = "";
		String email = "";
		
		if(user!=null) {
			//headDep is logged in
			//**
			phone = user.getPhone();
			email = user.getEmail();
			if(notificationFromServer.getMsg().equals("newTimeReq")) {
				String msg = phone +" "+email+" :"+" you have new time request check it in timeRequests Form";
				NotificationController.notifyForUpdateTimeReqInHD= true;
				Platform.runLater(() -> popUp(msg));
			}
		}else {
			User u = null;
			try {
				u = LoggedInUser.getInstance().getUser();
				phone = u.getPhone();
				email = u.getEmail();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(u.getUserType()==UserType.Lecturer) {
				if(notificationFromServer.getMsg().equals("AddingTimeApproved")) {
					String msg = phone +" "+email+" :"+" AddingTimeApproved" ;
					NotificationController.notifyForApproveOrDecline= true;
					Platform.runLater(() -> popUp(msg));
				}
				else if(notificationFromServer.getMsg().equals("AddingTimeDeclined")) {
					String msg = phone +" "+email+" :"+" AddingTimeDeclined" ;
					NotificationController.notifyForApproveOrDecline= true;
					Platform.runLater(() -> popUp(msg));
				}
			}
			else if(u.getUserType()==UserType.Student) {
				if(notificationFromServer.getMsg().equals("AddingTimeApproved")) {
					String msg = phone +" "+email+" :"+" AddingTimeApproved (" +(int)notificationFromServer.getData()+") for your exam";
					NotificationController.notifyForStudentAddingTime = true;
					NotificationController.timeToAddToStudent =(int) notificationFromServer.getData();
					Platform.runLater(() -> popUp(msg));
				}
				if(notificationFromServer.getMsg().equals("lockExam")) {
					String msg = phone +" "+email+" :"+" Exam locked by lecturer!.";
					NotificationController.notifyForStudentLockExam = true;
					Platform.runLater(() -> popUp(msg));
				}
				if(notificationFromServer.getMsg().equals("gradeApproved")) {
					Integer grade = (int) notificationFromServer.getData();
					String msg = phone +" "+email+" :"+" You got new grade ("+grade.toString()+") refresh grades page to see it";
					Platform.runLater(() -> popUp(msg));
				}
			}

		}
	}
	
	/**
	 * create a popUp with a given message.
	 * 
	 * @param msg string text input to method to display in popUp message.
	 */
	public static void popUp(String msg) {
		final Stage dialog = new Stage();
		VBox dialogVbox = new VBox(20);
		Label lbl = new Label(msg);
		lbl.setPadding(new Insets(15));
		lbl.setAlignment(Pos.CENTER);
		lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		dialogVbox.getChildren().add(lbl);
		Scene dialogScene = new Scene(dialogVbox, lbl.getMinWidth(), lbl.getMinHeight());
		dialog.setScene(dialogScene);
		dialog.show();
	}
	
}
