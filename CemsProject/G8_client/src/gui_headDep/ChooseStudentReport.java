package gui_headDep;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import com.mysql.cj.xdevapi.Client;

import client.CEMSClient;
import client.ClientUI;

import entity.LoggedInUser;

import common.ReportController;
import control_common.LoginController;
import entity.StudentExam;
import gui.GuiCommon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import logic.Msg;
import logic.MsgType;

public class ChooseStudentReport implements Initializable {

	@FXML
	private AnchorPane window;

	@FXML
	private Button getReportBtn;
	
	@FXML
	private Label ErrMsg;
	
	@FXML
	private ComboBox<String> StudentIDCombo;

	@FXML
	private Button logoutBtn;

	@FXML
	private Label userName;


    //initialize all students ID from data base

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Msg msg = new Msg("getStudentsID", MsgType.FROM_CLIENT, "");
		ClientUI.chat.accept(msg);
		Msg response = CEMSClient.responseFromServer;
		if (response.getMsg().equals("getStudentsID")) {
			HashSet<String> IDs = (HashSet<String>) response.getData();
			StudentIDCombo.getItems().addAll(IDs);
		}
		
		try {
			userName.setText("Hi " + LoggedInUser.getHeadDep().getFirstName() + " "
					+ LoggedInUser.getHeadDep().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
    /*
     * Generates a report for a selected student's exam. by student id's selected in combobox
     * */
    @FXML
    void getReport(ActionEvent event) {
    	String ID = StudentIDCombo.getValue();
    	if(ID!=null) {
    		ReportController.StudentIDinHDView = ID;
    		ReportController.headFromStudent=true;

			GuiCommon.getInstance().displayNextScreen("/gui_headDep/GetExamReport.fxml", "Report", event, true);
		} else {
			ErrMsg.setText("Please Choose Student First!");
		}

	}
    
    @FXML
    void clickLogout(ActionEvent event) {
    	if( LoginController.logOutHeadDep()) {
			GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
		}
    }
    
	@FXML
	void back(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/headDepMenu.fxml", "Main Menu", event, true);
	}

}
