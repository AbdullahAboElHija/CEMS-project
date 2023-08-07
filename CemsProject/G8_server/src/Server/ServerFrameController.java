package Server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;

import entity.Client;

public class ServerFrameController implements Initializable {
	
	final public static String DEFAULT_PORT = "5555";
	 @FXML
	    private TableView<Client> clientsTable;
	 
	 @FXML
	    private TableColumn<Client, String> hostCol;

	    @FXML
	    private TableColumn<Client, String> ipCol;
	    
	    @FXML
	    private TableColumn<Client, String> statusCol;

	@FXML
    private TextArea MsgArea;
	
    @FXML
    private Button stopServerBtn;

    @FXML
    private Button startServerBtn;

    @FXML
    private TextField ipInput;

    @FXML
    private TextField portInput;

    @FXML
    private TextField dbNameInput;

    @FXML
    private TextField dbUserName;

    @FXML
    private PasswordField dbPassword;


    private String ip ;
    private String port ;
    private String dbName ;
    private String dbUsername;
    private String dbPassword1;
    
    /*
    public void start(Stage primaryStage) throws Exception {
    	Pane root = FXMLLoader.load(getClass().getResource("/server/server.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Server");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	
    }*/

    public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword1() {
		return dbPassword1;
	}

	public void setDbPassword1(String dbPassword1) {
		this.dbPassword1 = dbPassword1;
	}
	
	ArrayList<Client> clientInfo = new ArrayList<>();
	private ObservableList<Client> clients;

	//click start server button
    @FXML
    void startServer(ActionEvent event) {
    	ip = ipInput.getText();
        port = portInput.getText();
        dbName = dbNameInput.getText();
        dbUsername = dbUserName.getText();
        dbPassword1 = dbPassword.getText();
    	if(ip.trim().isEmpty() || port.trim().isEmpty() || dbName.trim().isEmpty() || dbUsername.trim().isEmpty() || dbPassword1.trim().isEmpty()) {
    		MsgArea.appendText("You must enter missing information\n");
		}
		else
		{
			
	         startServerBtn.setDisable(true);
	         stopServerBtn.setDisable(false);
	         ServerUI.runServer(DEFAULT_PORT);
		}
         
    }
    
/**
	 * @param msg string message that print into error messages area.
	 */
	public void printToMsgArea(String msg) {
		MsgArea.appendText(msg + "\n");
	}
	
	//click stop server button
	  @FXML
	    void stopServer(ActionEvent event) {
		  startServerBtn.setDisable(false);
	         stopServerBtn.setDisable(true);
		  	ServerUI.closeServer(DEFAULT_PORT, this);
	    }
	  /*
	  public void start(Stage primaryStage) throws Exception {	
			Parent root = FXMLLoader.load(getClass().getResource("/Server/server.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("CEMS Server");
			primaryStage.setScene(scene);
			primaryStage.show();
	
		}*/

	  
	  @Override
		public void initialize(URL location, ResourceBundle resources) {
		  try {
			  // set up the columns
		        ipCol.setCellValueFactory(new PropertyValueFactory<>("ip"));
		        hostCol.setCellValueFactory(new PropertyValueFactory<>("host"));
		        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		        //ArrayList<Client> al=new ArrayList<>();
		        clients = FXCollections.observableArrayList();
		        clientsTable.setItems(clients);
		  }
		  catch(Exception ex) {
			  System.out.println("caught an exception");
		  }
		  stopServerBtn.setDisable(true);
	       
	    }

	    
	
	public void connectClientInTable(String clientIP,String serverIP,String status) {
		int cl_indx=getClientByIP( clientIP);
		  if(cl_indx==-1 && status.equals("Connected")) {
			  //cl=new Client(clientIP,serverIP,"Connected");
			  //System.out.println(clientIP + serverIP );
			  clients.add(new Client(clientIP,serverIP,"Connected"));
		  }
		  else if(cl_indx !=-1) {
			  Client new_cl=new Client(clients.get(cl_indx));
			  new_cl.setStatus(status);
			  clients.remove(cl_indx);
			  clients.add(new_cl);
			  clientsTable.refresh();
		  }
	}

	public ObservableList<Client> getClients() {
		return clients;
	}

	public void setClients(ObservableList<Client> clients) {
		this.clients = clients;
	}
	
	public int getClientByIP(String ip) {
		for (int i=0; i< clients.size(); i++) {
			if(clients.get(i).getIp().equals(ip))
				return i;
		}
		return -1;
	} 
	
    public void init() {
    	InetAddress ip;
    	String serverIP;
		try {
			ip = InetAddress.getLocalHost();
			serverIP=ip.getHostAddress();
			//ipInput.setText(serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		ipInput.setText("localhost");
    	 portInput.setText(DEFAULT_PORT);
    	 dbNameInput.setText("cems1");
    	 dbUserName.setText("root");

    	 //dbPassword.setText("Aa123456");
    	 //dbPassword.setText("Abd2115");

    	 dbPassword.setText("Aa123456");
    	 //dbPassword.setText("Abd2115");

    	 //dbPassword.setText("a4a8.r9y24us");
    	 MsgArea.setEditable(false);
    }


}
