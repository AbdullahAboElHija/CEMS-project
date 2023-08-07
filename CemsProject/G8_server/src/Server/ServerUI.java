package Server;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Server.ServerFrameController;
import gui.GuiCommon;

import java.io.IOException;
import java.util.Vector;

import Server.CEMSServer;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	private static ServerFrameController sFrame ;
	private static CEMSServer sv;

	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub				  		
		//aFrame = new ServerFrameController(); 
		 
		//aFrame.start(primaryStage);
		/*
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Server/server.fxml"));
		Pane root = loader.load();*/
		sFrame = (ServerFrameController) GuiCommon.getInstance().displayNextScreen("/Server/server.fxml","CEMS Server",null,false); 
		/*Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Server");
		primaryStage.setScene(scene);
		primaryStage.show();*/
		sFrame.init();
	}

	
	public static void runServer (String p)
	{
		 int port = 0; //Port to listen on

	        try{
	        	port = Integer.parseInt(p); //Set port to 5555
	          
	        }
	        catch(Throwable t){
	        	System.out.println("ERROR - Could not connect!");
	        }
	    	
	        sv =  CEMSServer.getInstance(port,sFrame);
	        
	        try {	       
	          sv.listen(); //Start listening for connections	          
	        } 
	        catch (Exception ex) {
	          System.out.println("ERROR - Could not listen for clients!");
	        }
	}
	public static void closeServer(String p, ServerFrameController serverController) {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(p); // Set port to 5555
		} catch (Throwable t) {
			serverController.printToMsgArea("ERROR - Wrong Port!");
		}

		try {
			sv.close();
		} catch (IOException e) {
			serverController.printToMsgArea("ERROR - Could not Disconnect!");
		}

	}
	

}
