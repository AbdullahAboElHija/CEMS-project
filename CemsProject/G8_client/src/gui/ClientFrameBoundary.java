package gui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Msg;
import logic.MsgType;

public class ClientFrameBoundary implements Initializable{
	final public static int DEFAULT_PORT = 5555;
		
		@FXML
	    private Button connectToServerBtn;
		
		@FXML
	    private TextField serversIP;
		
		@FXML
	    private TextField portField;
	
	    @FXML
	    void clickConnectToServer(ActionEvent event) throws Exception{
	    	String id;
			String server=serversIP.getText();
			String port=portField.getText();
			if(!server.isEmpty() && ! port.isEmpty()) {
				String ipStr=null;
				try {
		    		 InetAddress ip = InetAddress.getLocalHost();
		             ipStr=ip.getHostAddress();
		         } catch (UnknownHostException e) {
		             e.printStackTrace();
		         }
					ClientUI.connectToServer(server,Integer.parseInt(port));
					Msg msg1=new Msg("connect",MsgType.FROM_CLIENT,ipStr);
					ClientUI.chat.accept(msg1);//connect request
					//maybe reload server gui
					//GuiCommon.getInstance().displayNextScreen("/gui/LoginForm.fxml", "LoginCEMS", event,true);
					GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
			}
			
				//QuestionsBankBoundary qBankBoundary = loader.getController();
				//qBankBoundary.setCemsClient();
				//studentFormController.loadStudent(ChatClient.s1);
				//load questions from DB
							
					
			}
	    	//ClientUI.connectToServer("localhost",DEFAULT_PORT);

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			serversIP.setText("localhost");
			portField.setText("5555");
		}
	    
}
