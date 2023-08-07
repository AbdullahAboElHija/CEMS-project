// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import client.*;
import common.ChatIF;
import entity.Question;
import gui.GuiCommon;
import logic.Msg;
import logic.MsgType;

import java.io.*;
import java.util.ArrayList;

//
/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class CEMSClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	public static boolean awaitResponse = false;
	public static Msg responseFromServer = new Msg(null, null, null);
	public static Msg notificationFromServer = new Msg(null, null, null);

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public CEMSClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg1) {

		Msg msg = (Msg) msg1;
		if (msg.getMsgType() == MsgType.SERVER_RESPONSE) {
			responseFromServer = (Msg) msg;
			awaitResponse = false;
			System.out.println("--> handleMessageFromServer [" + msg.getMsg() + "]");

			switch (msg.getMsg()) {
			// example:
			// case "sameNameOfMsgSentFromAndToServer":
			case "connect":
				System.out.println("client successfully connected");
				break;
			case "disconnect":
				quit();
				System.out.println("client successfully disconnected");
				break;

			}
		} else if (msg.getMsgType() == MsgType.SERVER_NOTIFICATION) {
			notificationFromServer = (Msg) msg;
			ClientUI.handleNotificationFrom(notificationFromServer);
			System.out.println("--> handlenotificationFromServer [" + msg.getMsg() + "]");
		} else {
			System.out.println("unexpected message type in CEMSClient-handleMessageFromServer\n");
		}

	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Object message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class
