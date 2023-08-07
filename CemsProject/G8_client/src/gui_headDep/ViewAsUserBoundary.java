package gui_headDep;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import control_common.LoginController;
import entity.Lecturer;
import entity.LoggedInUser;
import entity.Profession;
import entity.User;
import entity.UserType;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import logic.Msg;
import logic.MsgType;

public class ViewAsUserBoundary implements Initializable {

	@FXML
	private Button logoutBtn;

	@FXML
	private Button backBtn;


	@FXML
	private Label msgArea;

	@FXML
	private ComboBox<User> usersCombo;

	@FXML
	private Button viewAsBtn;

	private User chosenUser = null;

	@FXML
	private AnchorPane window;

	@FXML
	private Label userName;

	private ArrayList<User> users;
	private ObservableList<User> usersList;


    
    
    /**
     * Initializes the controller.
     *
     * This method is called automatically when the associated FXML file is loaded.
     * It is responsible for initializing the controller and setting up the initial state of the UI components.
     *
     * @param location  The URL location of the FXML file.
     * @param resources The ResourceBundle containing localized resources for the FXML file.
     */
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	Platform.runLater(() -> {
	   		 Stage stage = (Stage) window.getScene().getWindow();
	   	    	stage.setOnCloseRequest(event -> {
	   	            event.consume(); // Consume the event to prevent automatic window closing
	   	         LoginController.setWithReq(true);
	   	         LoginController.disconnect();	   
	   	         stage.close();
	   	        });
	        });
		Msg msg = new Msg("getAllUsers", MsgType.FROM_CLIENT, null);
		ClientUI.chat.accept(msg);
		Msg response = CEMSClient.responseFromServer;
		try {
			if (!response.getMsg().equals("getAllUsers")) {
				System.out.println(response.getMsg());
				throw new Exception("Unexpected server response");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		users = (ArrayList<User>) response.getData();

		usersList = FXCollections.observableArrayList(users);
		usersCombo.setItems(usersList);
		usersCombo.setConverter(new StringConverter<User>() {
			@Override
			public String toString(User user) {
				if (user == null)
					return null;
				return user.getUserType().toString() + " " + user.getFirstName() + " " + user.getLastName();
			}

			@Override
			public User fromString(String string) {
				return null;
			}
		});
		// usersCombo.getItems().addAll(users);
		
		try {
			userName.setText("Hi " + LoggedInUser.getHeadDep().getFirstName() + " "
					+ LoggedInUser.getHeadDep().getLastName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@FXML
	void chooseUser(ActionEvent event) {
		chosenUser = usersCombo.getValue();
	}

	@FXML
	void clickBack(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/headDepMenu.fxml", "Hello Head Department", event,
				true);
	}
    
    /**
     * Handles the event when the View As button is clicked.
     *
     * This method is triggered when the View As button is clicked.
     * It checks if a user has been chosen from the usersCombo ComboBox.
     * If a user is chosen, it checks if there is a currently logged-in user.
     * If there is a logged-in user, it displays an error message indicating that the user must log out before logging in as another user.
     * If there is no logged-in user, it logs in the chosen user and redirects to the corresponding menu based on the user type.
     *
     * @param event The ActionEvent triggered by clicking the View As button.
     */
    @FXML
    void clickViewAs(ActionEvent event) {
    	if(chosenUser==null) {
    		showMsg("Please choose a user");
    		return;
    	}
    	try {
    		User user= LoggedInUser.getInstance().getUser();
    		//if gotten her user is logged in 
    		showMsg("You must exit the View As window before viewing as a different user");
    		return;
    	}catch(Exception e) {
    		//no logged in user
    		LoginController.setWithReq(false);
    		String loginCase = LoginController.loginUser(chosenUser);
    		switch (UserType.getType(loginCase)) {
    		case Lecturer: {
    			GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "Hello Lecturer", null,false);
    			break;
    		}
    		case Student: {
    			GuiCommon.getInstance().displayNextScreen("/gui_student/studentMenu.fxml", "Hello Student", null,false);
    			break;
    		}
    		case HeadDep: {
    			GuiCommon.getInstance().displayNextScreen("/gui_headDep/headDepMenu.fxml", "Hello Head Department",null,false);
    			break;
    		}
    		}
    	}
    }

	@FXML
	void clickLogout(ActionEvent event) {
		if( LoginController.logOutHeadDep()) {
			GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
		}
	}

	public void showMsg(String msg) {
		msgArea.setText(msg);
	}

}
