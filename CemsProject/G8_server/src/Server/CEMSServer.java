// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Server;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import ocsf.server.*;
import Server.ServerFrameController;
import common.MyFile;
import entity.ActivatedExam;
import entity.Client;
import entity.Exam;
import entity.Lecturer;
import entity.ManualExam;
import entity.OnlineExam;
import entity.Profession;
import entity.Question;
import entity.StudentExam;
import entity.User;
import DBConnection.mysqlConnection;
import logic.Msg;
import logic.MsgType;
//import logic.ResponseFromServer;

import java.sql.SQLException;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */

public class CEMSServer extends AbstractServer {// singleton
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * 
	 */

	private HashMap<String, User> loggedInUsers = new HashMap<String, User>();
	private HashMap<String, ConnectionToClient> loggedClients = new HashMap<String, ConnectionToClient>(); // HashMap<StudentID>

	private ServerFrameController serverController;
	final public static int DEFAULT_PORT = 5555;
	private String serverIP;
	private static mysqlConnection dbController = new mysqlConnection();
	private static CEMSServer instance = null;

	// private constructor (bc singleton) that takes port
	private CEMSServer(int port) {
		super(port);
		getServerIP();
	}

	// get instance that matches first constructor (that takes port only as
	// parameter)
	public static CEMSServer getInstance(int port) {
		if (instance == null) {
			instance = new CEMSServer(port);
		}
		return instance;
	}

	// private constructor (bc singleton) that takes port and server's frame
	// controller
	private CEMSServer(int port, ServerFrameController serverUI) {
		super(port);
		this.serverController = serverUI;
		getServerIP();

	}

	// get instance that matches second constructor (that takes port and server's
	// frame controller)
	public static CEMSServer getInstance(int port, ServerFrameController serverUI) {
		if (instance == null) {
			instance = new CEMSServer(port, serverUI);
		}
		return instance;
	}

	private void getServerIP() {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			serverIP = ip.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg1    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @param
	 */
	public void handleMessageFromClient(Object msg1, ConnectionToClient client) {

		Msg msg = (Msg) msg1;
		if (msg.getMsgType() == MsgType.FROM_CLIENT) {
			serverController.printToMsgArea("Message received: " + msg.getMsg() + " from " + client);
			switch (msg.getMsg()) {
			case "connect":// obj in msg containg "ip" as String
				connect((String) msg.getData(), client);
				break;
			case "disconnect":
				disconnect((String) msg.getData(), client);
				break;
			case "login":
				loginUser((User) msg.getData(), client);
				break;
			case "logout":
				logout((User) msg.getData(), client);
				break;
			case "getLecturerProfessions":
				GetLecturerProfessions(msg1, client);
				break;
			case "getLecturerQuestions":
				LoadQuestionsByLec(msg1, client);
				break;
			// UPDATED
			case "createNewQuestion":
				CreateNewQuestion((Question) msg.getData(), client);
				break;
			// NEW
			case "editQuestionDetails":
				editQuestionDetails(msg1, client);
				break;
			// NEW
			case "deleteQuestion":
				deleteQuestion(msg1, client);
				break;
			case "deleteExam":
				deleteExam(msg1, client);
				break;
			case "LecUploadFile":
				System.out.println("FileUpdated");
				UploadFile((ManualExam) msg.getData(), client);
				break;
			case "CheckCode":
				CheckCode((String) msg.getData(), client);
				break;
			case "DownloadManualExam":
				StudentdownloadExam((ManualExam) msg.getData(), client);
				break;
			case "UploadFileStudentManualExam":
				UploadFileStudentManualExam((ManualExam) msg.getData(), client);
				break;
			case "StudentdownloadExam":
				StudentdownloadExam((ManualExam) msg.getData(), client);
				break;
			case "getLecturerCourses":
				GetLecturerCourses((ArrayList<Profession>) msg.getData(), client);
				break;
			case "CreateExamID":
				CreateExamID((Exam) msg.getData(), client);
				break;
			case "fillExamDetailsCreatingStep":
				fillExamDetailsCreatingStep((Exam) msg.getData(), client);
				break;
			case "LoadQuestionsByProfession":
				fillQuestionsByProf((String) msg.getData(), client);
				break;
			case "createNewOnlineExam":
				createNewOnlineExam((OnlineExam) msg.getData(), client);
				break;
			case "getExamsByLec":
				getExamsByLec(msg1, client);
				break;
			case "activateExam":
				activateExam((ActivatedExam) msg.getData(), client);
				break;
			case "getActivatedExamsOfLec":
				getActivatedExamsOfLec((String) msg.getData(), client);
				break;
			case "getExamDetails":
				getExamDetails((String) msg.getData(), client);
				break;
			case "timeReqMsg":
				timeReqMsg((ActivatedExam) msg.getData(), client);
				break;
			case "getTimeReqExams":
				getTimeReqExams(client);
				break;
			case "approveTimeReqForExam":
				approveTimeReqForExam((ActivatedExam) msg.getData(), client);
				break;
			case "lockActivatedExam":
				lockActivatedExam((ActivatedExam) msg.getData(), client);
				break;
			case "declineTimeReqForExam":
				declineTimeReqForExam((ActivatedExam) msg.getData(), client);
				break;
			case "getOnlineExamDetails":
				getOnlineExamDetails((String) msg.getData(), client);
				break;
			case "getManualExamDetails":
				getManualExamDetails((String) msg.getData(), client);
				break;
			case "registerStudentInExam":
				registerStudentInExam((StudentExam) msg.getData(), client);
				break;
			case "terminateStudentExam":
				terminateStudentExam((StudentExam) msg.getData(), client);
				break;
			case "hasStudentPerformed":
				hasStudentPerformed((StudentExam) msg.getData(), client);
				break;
			case "getGradeAnalyzer":
				getGradeAnalyzer((String) msg.getData(), client);
				break;
			case "returnAvgAndMedian":
				returnAvgAndMedian((String) msg.getData(), client);
				break;
			case "getExamsActivatedByLecAndAnotherLec":
				getExamsActivatedByLecAndAnotherLec((String) msg.getData(), client);
				break;
			case "getExamsIDOfLecturer":
				getExamsIDOfLecturer((String) msg.getData(), client);
				break;
			case "getExamCourseNameAndProfessionName":
				getExamCourseNameAndProfessionName((String) msg.getData(), client);
				break;
			case "getLecNameAndLecID":
				getLecNameAndLecID(client);
				break;
			case "getCourseNameAndCourseID":
				getCourseNameAndCourseID(client);
				break;
			case "getExamsActivatedByCourse":
				getExamsActivatedByCourse((String) msg.getData(), client);
				break;
			case "getExamIDByActivationCode":
				getExamIDByActivationCode((String) msg.getData(), client);
				break;
			case "getStudentsID":
				getStudentsID(client);
				break;
			case "getStudentNameByStudentID":
				getStudentNameByStudentID((String) msg.getData(), client);
				break;
			case "getStudentsExamCopyOfLec":
				getStudentsExamCopyOfLec((String) msg.getData(), client);
				break;
			case "gradeApproved":
				gradeApproved((StudentExam) msg.getData(), client);
				break;
			case "getAllExamCopiesOfStudent":
				getAllExamCopiesOfStudent((String) msg.getData(), client);
				break;
			case "getAllUsers":
				getAllUsers(client);
				break;
			}

		} else {
			serverController.printToMsgArea("Unexpected message type");
			Msg msg2 = new Msg("error", MsgType.SERVER_RESPONSE, null);
			sendToAllClients(msg2);
		}

	}

	/**
	 * 
	 * Retrieves all users from the database and sends the data to the specified
	 * client.
	 * 
	 * @param client The connection to the client.
	 */
	private void getAllUsers(ConnectionToClient client) {
		Msg data = dbController.getAllUsers();
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * Retrieves all exam copies of a specific student from the database and sends
	 * the data to the specified client.
	 * 
	 * @param studentId The ID of the student.
	 * @param client    The connection to the client.
	 */
	private void getAllExamCopiesOfStudent(String studentId, ConnectionToClient client) {
		Msg data = dbController.getAllExamCopiesOfStudent(studentId);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Updates the grade status of a student's exam to approved in the database.
	 * Sends a notification to the student and sends the updated data to the
	 * specified client.
	 * 
	 * @param studentExam The student exam object containing the exam information.
	 * @param client The connection to the client.
	 */
	private void gradeApproved(StudentExam studentExam, ConnectionToClient client) {

		Msg data = dbController.gradeApproved(studentExam);
		Msg notificationForStudent = new Msg("gradeApproved", MsgType.SERVER_NOTIFICATION, studentExam.getGrade());
		if (loggedClients.containsKey(studentExam.getStudentId())) {
			ConnectionToClient lecturerReqestor = loggedClients.get(studentExam.getStudentId());
			try {
				lecturerReqestor.sendToClient(notificationForStudent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Retrieves the exam copies of all students assigned to a specific lecturer
	 * from the database and sends the data to the specified client.
	 * 
	 * @param lecID  The ID of the lecturer.
	 * @param client The connection to the client.
	 */
	private void getStudentsExamCopyOfLec(String lecID, ConnectionToClient client) {
		Msg data = dbController.getStudentsExamCopyOfLec(lecID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getExamDetails(String examID, ConnectionToClient client) {// ???????????????????????????????????
	}

	/**
	 * Checks if a student has performed a specific exam and sends the result to the
	 * specified client.
	 * 
	 * @param studentExam The student exam object containing the exam information.
	 * @param client      The connection to the client.
	 */
	private void hasStudentPerformed(StudentExam studentExam, ConnectionToClient client) {
		Msg data = dbController.hasStudentPerformed(studentExam);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Terminates a student's exam based on the exam type (manual or online) and
	 * sends the result to the specified client.
	 * 
	 * @param studentExam The student exam object containing the exam information.
	 * @param client      The connection to the client.
	 */
	private void terminateStudentExam(StudentExam studentExam, ConnectionToClient client) {
		Msg data = null;
		if (studentExam.getExam().getExamType().equals("manual")) {
			data = dbController.terminateManualStudentExam(studentExam);
		}
		if (studentExam.getExam().getExamType().equals("online")) {
			data = dbController.terminateOnlineStudentExam(studentExam);
		}
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Registers a student in an exam and sends the result to the specified client.
	 * 
	 * @param studentExam The student exam object containing the exam information.
	 * @param client      The connection to the client.
	 */
	private void registerStudentInExam(StudentExam studentExam, ConnectionToClient client) {
		Msg data = dbController.registerStudentInExam(studentExam);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Retrieves the details of a manual exam from the database and sends the data
	 * to the specified client.
	 * 
	 * @param examID The ID of the manual exam.
	 * @param client The connection to the client.
	 */
	private void getManualExamDetails(String examID, ConnectionToClient client) {
		Msg data = dbController.getManualExamDetails(examID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Retrieves the details of an online exam from the database and sends the data
	 * to the specified client.
	 * 
	 * @param examID The ID of the online exam.
	 * @param client The connection to the client.
	 */
	private void getOnlineExamDetails(String examID, ConnectionToClient client) {

		Msg data = dbController.getOnlineExamDetails(examID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Retrieves exams associated with a specific lecturer from the database and
	 * sends the data to the specified client.
	 * 
	 * @param msg    The message object containing the lecturer information.
	 * @param client The connection to the client.
	 */
	private void getExamsByLec(Object msg, ConnectionToClient client) {
		Msg msg1 = (Msg) msg;
		Lecturer lec = (Lecturer) msg1.getData();
		Msg data = dbController.getExamsByLecFromDB(lec);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Deletes a question from the database and sends the result to the specified
	 * client.
	 * 
	 * @param msg    The message object containing the question information.
	 * @param client The connection to the client.
	 */
	private void deleteQuestion(Object msg, ConnectionToClient client) {
		Msg msg1 = (Msg) msg;
		Question question = (Question) msg1.getData();
		Msg data = dbController.deleteQuestionFromDB(question);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// @Omar

	/**
	 * Deletes an exam from the database and sends the result to the specified
	 * client.
	 * 
	 * @param msg    The message object containing the exam information.
	 * @param client The connection to the client.
	 */
	private void deleteExam(Object msg, ConnectionToClient client) {
		Msg msg1 = (Msg) msg;
		Exam exam = (Exam) msg1.getData();
		Msg data = dbController.deleteExamFromDB(exam);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// @Abdullah
	/// from

	/*
	 * @Abdullah Author
	 * 
	 * @params file
	 * 
	 * @return message to client in succsessfuly saving
	 */
	public void UploadFile(ManualExam exam, ConnectionToClient client) {
		serverController.printToMsgArea("File Arrived");
		// serverController.printToMsgArea("Message received: " + myFile.getFileName() +
		// " from " + client);
		// InputStream is = new ByteArrayInputStream(myFile.getMybytearray());
		Msg data = dbController.updateFile(exam);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if an exam code is valid in the database and sends the result to the
	 * specified client.
	 * 
	 * @param examCode The exam code to be checked.
	 * @param client   The connection to the client.
	 */
	private void CheckCode(String examCode, ConnectionToClient client) {
		Msg data = dbController.CheckCode(examCode);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Allows a student to download a manual exam. Sends the exam file to the
	 * specified client.
	 * 
	 * @param exam   The manual exam to be downloaded.
	 * @param client The connection to the client.
	 */
	private void StudentdownloadExam(ManualExam exam, ConnectionToClient client) {
		Msg data = dbController.StudentdownloadManualExam(exam);
		try {		
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Allows a student to upload a manual exam file. Sends the uploaded file to the
	 * server for processing.
	 * 
	 * @param Exam   The manual exam object containing the uploaded file.
	 * @param client The connection to the client.
	 */
	private void UploadFileStudentManualExam(ManualExam Exam, ConnectionToClient client) {
		
		Msg data = dbController.UploadFileStudentManualExam(Exam);
		try {
			System.out.println("File Uploaded");
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Edits the details of a question in the database and sends the result to the
	 * specified client.
	 * 
	 * @param msg1   The message object containing the question information.
	 * @param client The connection to the client.
	 */
	private void editQuestionDetails(Object msg1, ConnectionToClient client) {
		Msg msg = (Msg) msg1;
		Msg data;
		try {
			data = dbController.editQuestionDetailsInDB((Question) msg.getData());
			client.sendToClient(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates a new question in the database with the provided question data and
	 * sends the result to the specified client.
	 * 
	 * @param msg1   The message object containing the question information.
	 * @param client The connection to the client.
	 */
	private void CreateNewQuestion(Object msg1, ConnectionToClient client) {
		Question questionData = (Question) msg1;
		Msg data;
		Integer counter = dbController.getNextQuestionsNum(questionData.getProfession());
		String questionID = questionData.getProfessionID() + String.format("%03d", counter);
		questionData.setQuestionID(questionID);
		try {
			data = dbController.saveQuestionDetails((Question) questionData);
			client.sendToClient(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Processes client request to load all questions written by certain lecturer
	 * sends back to client ArrayList<Question> of wanted questions
	 * 
	 * @param msg1   expects Lecturer type in msg.Data (wanted lecturer)
	 * @param client ConnectionToClient
	 */
	private void LoadQuestionsByLec(Object msg1, ConnectionToClient client) {
		ArrayList<Question> questions = dbController.GetLecturerQuestionsFromDB((Lecturer) ((Msg) msg1).getData());
		Msg data = new Msg("getLecturerQuestions", MsgType.SERVER_RESPONSE, questions);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * The function processes the request of getting the lecturer professions from
	 * db and sends them to client
	 * 
	 * @param msg1:   expected to have in msgData Lecturer ID of type String
	 * @param client: the client making the server request
	 * 
	 * @return (sends a Msg to client) ArrayList<Profession> with all professions of
	 *         wanted lecturer
	 */
	private void GetLecturerProfessions(Object msg1, ConnectionToClient client) {

		ArrayList<Profession> profs = dbController.GetLecturerProfessionsFromDB((String) ((Msg) msg1).getData());
		Msg data = new Msg("getLecturerProfessions", MsgType.SERVER_RESPONSE, profs);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The function processes a connect request from client and shows it in server's
	 * connected clients sends the client a Msg of connection successful
	 * 
	 * @param ip     of client
	 * @param client ConnectionToClient
	 */
	public void connect(String ip, ConnectionToClient client) {

		serverController.connectClientInTable(ip, serverIP, "Connected");
		Msg msg3 = new Msg("connect", MsgType.SERVER_RESPONSE, null);
		try {
			client.sendToClient(msg3);
		} catch (IOException e) {
			System.out.println("Exception in connect method in CEMSServer");
			e.printStackTrace();
		}
	}

	/**
	 * The function processes a disconnect request from client and shows it in
	 * server's connected clients sends the client a Msg of disconnection successful
	 * 
	 * @param ip     of client
	 * @param client ConnectionToClient
	 */
	public void disconnect(String ip, ConnectionToClient client) {

		serverController.connectClientInTable(ip, serverIP, "Disconnected");
		Msg msg3 = new Msg("disconnect", MsgType.SERVER_RESPONSE, null);
		try {
			client.sendToClient(msg3);
		} catch (IOException e) {
			System.out.println("Exception in connect method in CEMSServer");
			e.printStackTrace();
		}
	}

	/**
	 * Processes a request from client to validate user details that is trying to
	 * log in
	 * 
	 * @param userDetails user attempting to log into the system
	 * @param client      ConnectionToClient
	 */
	private void loginUser(User userDetails, ConnectionToClient client) {
		Msg data;

		try {
			data = dbController.loginUserToDB(userDetails);
			User user = (User) data.getData();
			if (data != null && user != null && !data.getMsg().equals("AlreadyLoggedIn")) {
				loggedInUsers.put(user.getId(), user);
				loggedClients.put(user.getId(), client);
			}
			client.sendToClient(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param profs
	 * @param client
	 */
	private void GetLecturerCourses(ArrayList<Profession> profs, ConnectionToClient client) {
		//System.out.println("Course");
		Msg data = dbController.GetLecturerCourses(profs);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Creates an ID for a new exam in the database and sends the result to the
	 * specified client.
	 * 
	 * @param ex     The exam object for which the ID needs to be created.
	 * @param client The connection to the client.
	 */
	private void CreateExamID(Exam ex, ConnectionToClient client) {
		Msg data = dbController.CreateExamID(ex);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Fills in the details of an exam during the creation step and sends the result
	 * to the specified client.
	 * 
	 * @param ex     The exam object containing the details to be filled.
	 * @param client The connection to the client.
	 */
	private void fillExamDetailsCreatingStep(Exam ex, ConnectionToClient client) {
		Msg data = dbController.fillExamDetailsCreatingStep(ex);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Logs out a user, removes the user from the logged-in users list, and sends
	 * the logout status to the specified client.
	 * 
	 * @param user   The user to be logged out.
	 * @param client The connection to the client.
	 */
	private void logout(User user, ConnectionToClient client) {
		Msg data;
		loggedInUsers.remove(user.getId());
		loggedClients.remove(user.getId());
		try {
			data = dbController.logoutUserDB(user);
			client.sendToClient(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Retrieves and fills the questions associated with a specific profession from
	 * the database and sends the data to the specified client.
	 * 
	 * @param profId The ID of the profession.
	 * @param client The connection to the client.
	 */
	private void fillQuestionsByProf(String profId, ConnectionToClient client) {
		Msg data = dbController.fillQuestionsByProf(profId);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Creates a new online exam in the database and sends the result to the
	 * specified client.
	 * 
	 * @param exam   The online exam object containing the exam details.
	 * @param client The connection to the client.
	 */
	private void createNewOnlineExam(OnlineExam exam, ConnectionToClient client) {
		Boolean ifDoneAddingCntQuestions = dbController.addingCntUsedOfQuestions(exam.getQuestionsInExam());
		Msg data = dbController.createNewOnlineExam(exam);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Retrieves the activated exams associated with a specific lecturer from the
	 * database and sends the data to the specified client.
	 * 
	 * @param lecID  The ID of the lecturer.
	 * @param client The connection to the client.
	 */
	private void getActivatedExamsOfLec(String lecID, ConnectionToClient client) {
		Msg data = dbController.getActivatedExamsOfLec(lecID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
		Activates an exam and sends the activation status to the specified client.
		@param examToActivate The activated exam object containing the exam details.
		@param client The connection to the client.
	*/
	public void activateExam(ActivatedExam examToActivate, ConnectionToClient client) {
		Msg data = dbController.activateExam(examToActivate);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
		Locks an activated exam, notifies the relevant users, and sends the lock status to the specified client.
		@param selectedExam The activated exam to be locked.
		@param client The connection to the client.
	*/
	public void lockActivatedExam(ActivatedExam selectedExam, ConnectionToClient client) {
		Msg data = dbController.lockActivatedExam(selectedExam.getActivationCode());

		ArrayList<String> studentIDs = (ArrayList<String>) data.getData();
		Msg lockExamNotification = new Msg("lockExam", MsgType.SERVER_NOTIFICATION, "Exam Locked");
		System.out.println(studentIDs);
		if (loggedClients.containsKey(selectedExam.getActivatedLecturerID())) {
			ConnectionToClient lecturerReqestor = loggedClients.get(selectedExam.getActivatedLecturerID());
			try {
				lecturerReqestor.sendToClient(lockExamNotification);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (String s : studentIDs) {
			ConnectionToClient studentClient = loggedClients.get(s);
			try {
				studentClient.sendToClient(lockExamNotification);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			System.out.println("SHOULD SEND TO LEC");
			client.sendToClient(new Msg("lockExam", MsgType.SERVER_RESPONSE, "ExamLocked"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Abdullah

	/**
	
	Sends a time request message for an activated exam to the head department and updates the time request in the database.
	@param timeReqToExam The activated exam for which a time request is made.
	@param client The connection to the client.
	*/
	private void timeReqMsg(ActivatedExam timeReqToExam, ConnectionToClient client) {
		ConnectionToClient headDP;
		int flag = 0;
		if (loggedClients.containsKey("999999999")) {
			headDP = loggedClients.get("999999999");
			Msg notfyTimeReq = new Msg("newTimeReq", MsgType.SERVER_NOTIFICATION, timeReqToExam);
			try {
				// we want to update in DB the time Req;
				flag = dbController.updateTimeinReqInDB(timeReqToExam);
				headDP.sendToClient(notfyTimeReq); // this is just for popout;

			} catch (IOException e) {

				e.printStackTrace();
			}

		} else {
			flag = dbController.updateTimeinReqInDB(timeReqToExam);
		}
		if (flag == 1) {
			try {
				client.sendToClient(new Msg("timeAdditionRequested", MsgType.SERVER_RESPONSE, "Successfuly"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				client.sendToClient(new Msg("timeAdditionRequestedNot", MsgType.SERVER_RESPONSE, "Failed"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	/**
	
	Retrieves the time request exams from the database and sends the data to the specified client.
	@param client The connection to the client.
	*/
	private void getTimeReqExams(ConnectionToClient client) {
		Msg data = dbController.getTimeReqExams();
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**

	Approves the time request for an activated exam and notifies the relevant parties.

	@param selectedExam The activated exam for which the time request is approved.

	@param client The connection to the client.
	*/
	private void approveTimeReqForExam(ActivatedExam selectedExam, ConnectionToClient client) {

		Msg data = dbController.approveTimeReqForExam(selectedExam.getActivationCode());

		// Array of IDs of students in the Exam
		ArrayList<String> studentIDs = (ArrayList<String>) data.getData();
		Msg approveTimeReqForExam = new Msg("AddingTimeApproved", MsgType.SERVER_NOTIFICATION,
				selectedExam.getTimeRequested());
//		System.out.println("Time to add :" + selectedExam.getTimeRequested());
		if (loggedClients.containsKey(selectedExam.getActivatedLecturerID())) {
			ConnectionToClient lecturerReqestor = loggedClients.get(selectedExam.getActivatedLecturerID());
			try {
				lecturerReqestor.sendToClient(approveTimeReqForExam);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		for (String s : studentIDs) {
//			System.out.println(s);
			if (loggedClients.containsKey(s)) {
//				System.out.println(s);
				ConnectionToClient studentClient = loggedClients.get(s);
				try {
					studentClient.sendToClient(approveTimeReqForExam);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		try {
			client.sendToClient(new Msg("approveTimeReqForExam", MsgType.SERVER_RESPONSE, "ApprovedMsg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
		Declines the time request for an activated exam and notifies the relevant parties.
		@param selectedExam The activated exam for which the time request is declined.
		@param client The connection to the client.
	*/
	private void declineTimeReqForExam(ActivatedExam selectedExam, ConnectionToClient client) {
		Msg data = dbController.declineTimeReqForExam(selectedExam.getActivationCode());
		Msg declineTimeReqForExam = new Msg("AddingTimeDeclined", MsgType.SERVER_NOTIFICATION, "Declined");
		if (loggedClients.containsKey(selectedExam.getActivatedLecturerID())) {
			ConnectionToClient lecturerReqestor = loggedClients.get(selectedExam.getActivatedLecturerID());
			try {
				lecturerReqestor.sendToClient(declineTimeReqForExam);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
		Retrieves the grade analyzer data for a specific exam and sends it to the client.
		@param activationCode The activation code of the exam.
		@param client The connection to the client.
	*/
	private void getGradeAnalyzer(String activationCode, ConnectionToClient client) {
		Msg data = dbController.getGradeAnalyzer(activationCode);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
		Retrieves the average and median grades for a specific exam and sends them to the client.
		@param activationCode The activation code of the exam.
		@param client The connection to the client.
	*/
	private void returnAvgAndMedian(String activationCode, ConnectionToClient client) {
	
		Msg data = dbController.returnAvgAndMedian(activationCode);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
		
			e.printStackTrace();
		}

	}


	/**
		Retrieves all exams that are activated by a specific lecturer and exams created by the same lecturer that are also activated,
		and sends them to the client.
		@param lecID The ID of the lecturer.
		@param client The connection to the client.
	*/
	private void getExamsActivatedByLecAndAnotherLec(String lecID, ConnectionToClient client) {
		
		Msg data = dbController.getExamsActivatedByLecAndAnotherLec(lecID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
		Retrieves the IDs of all exams that are activated by a specific lecturer and sends them to the client.
		@param lecID The ID of the lecturer.
		@param client The connection to the client.
	*/
	private void getExamsIDOfLecturer(String lecID, ConnectionToClient client) {
	
		Msg data = dbController.getExamsIDOfLecturer(lecID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
		Retrieves the course name and profession name of an exam and sends them to the client.
		@param examID The ID of the exam.
		@param client The connection to the client.
	*/
	private void getExamCourseNameAndProfessionName(String examID, ConnectionToClient client) {
		
		Msg data = dbController.getExamCourseNameAndProfessionName(examID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
		Retrieves the names and IDs of lecturers and sends them to the client.
		@param client The connection to the client.
	*/
	private void getLecNameAndLecID(ConnectionToClient client) {
	
		Msg data = dbController.getLecNameAndLecID();
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	/**
	
	Retrieves the names and IDs of courses and sends them to the client.
	@param client The connection to the client.
	*/
	private void getCourseNameAndCourseID(ConnectionToClient client) {
	
		Msg data = dbController.getCourseNameAndCourseID();
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
		Retrieves the exams activated for a specific course ID and sends them to the client.
		@param ID The ID of the course.
		@param client The connection to the client.
	*/
	private void getExamsActivatedByCourse(String ID, ConnectionToClient client) {
	
		Msg data = dbController.getExamsActivatedByCourse(ID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	/**
	
		Retrieves the exam ID for a specific activation code and sends it to the client.
		@param actCode The activation code of the exam.
		@param client The connection to the client.
	*/
	private void getExamIDByActivationCode(String actCode, ConnectionToClient client) {

		Msg data = dbController.getExamIDByActivationCode(actCode);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	
	Retrieves the IDs of all students and sends them to the client.
	@param client The connection to the client.
	*/
	private void getStudentsID(ConnectionToClient client) {
	
		Msg data = dbController.getStudentsID();
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	
	Retrieves the name of a student by their ID and sends it to the client.
	@param ID The ID of the student.
	@param client The connection to the client.
	*/
	private void getStudentNameByStudentID(String ID, ConnectionToClient client) {
	
		Msg data = dbController.getStudentNameByStudentID(ID);
		try {
			client.sendToClient(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Abdullah end 840

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		mysqlConnection.connectToDB(serverController);
		serverController.printToMsgArea("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		serverController.printToMsgArea("Server has stopped listening for connections.");
	}


}
