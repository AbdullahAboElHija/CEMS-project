package DBConnection;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Server.ServerFrameController;
import common.MyFile;
import entity.Lecturer;
import entity.ManualExam;
import entity.OnlineExam;
import entity.Profession;
import entity.Question;
import entity.StudentExam;
import entity.User;
import entity.UserType;
import javafx.collections.ObservableList;
import logic.Msg;
import logic.MsgType;
import ocsf.server.ConnectionToClient;
import entity.ActivatedExam;
import entity.Course;
import entity.Exam;

import java.util.*;

public class mysqlConnection {
	static Connection conn = null;
	static Statement statement;
	private static String dbName;
	public static ServerFrameController serverController;

	@SuppressWarnings("deprecation")
	public static void connectToDB(ServerFrameController serverController1) {
		try {
			serverController = serverController1;
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			serverController.printToMsgArea("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			serverController.printToMsgArea("Driver definition failed");
		}

		try {
			dbName = serverController.getDbName();
			String dbPath = "jdbc:mysql://" + serverController.getIp() + "/" + serverController.getDbName()
					+ "?serverTimezone=IST";
			conn = DriverManager.getConnection(dbPath, serverController.getDbUsername(),
					serverController.getDbPassword1());
			// Connection conn =
			// DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
			serverController.printToMsgArea("SQL connection succeed");
			statement = conn.createStatement();

		} catch (SQLException ex) {/* handle any errors */
			serverController.printToMsgArea("SQLException: " + ex.getMessage());
			serverController.printToMsgArea("SQLState: " + ex.getSQLState());
			serverController.printToMsgArea("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * 
	 * @param userDetails: gets a user that has username and password
	 * @return if found in DB returns in Msg data wanted user with full details
	 *         otherwise returns null as wanted user
	 * 
	 */
	public Msg loginUserToDB(User userDetails) {
		String query = "SELECT * FROM cems1.users WHERE username = ? AND password = ?;";
		Msg msg1 = new Msg("UserNotFound", MsgType.SERVER_RESPONSE, null);
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, userDetails.getUsername());
			preparedStatement.setString(2, userDetails.getPassword());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {

				// ResultSet is not empty, user is found
				// Retrieve data from each column
				String id = resultSet.getString("userID");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String email = resultSet.getString("email");
				String role = resultSet.getString("role");
				Integer status = resultSet.getInt("status");
				String phone = resultSet.getString("phone");
				User user = new User(id, username, password, firstName, lastName, email, UserType.getType(role),
						status);
				user.setPhone(phone);
				msg1.setData(user);
				preparedStatement.close();
				// LoginController.handleLoginCases(msg1, status);
				if (status == 1) {
					msg1.setMsg("AlreadyLoggedIn");
				} else {
					msg1.setMsg("UserFound");
					query = "UPDATE cems1.users SET status=1 WHERE username = ? ;";
					preparedStatement = conn.prepareStatement(query);
					preparedStatement.setString(1, userDetails.getUsername());
					int rowsAffected = preparedStatement.executeUpdate();
					if (rowsAffected == 0) {
						// Update wasn't successful
						throw new Exception("Updating login failed");
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg1;
	}
	/**
	 * Retrieves the professions associated with a specific lecturer from the database.
	 *
	 * @param lecturerID The ID of the lecturer.
	 * @return An ArrayList of Profession objects representing the lecturer's professions.
	 */
	public ArrayList<Profession> GetLecturerProfessionsFromDB(String lecturerID) {
		ArrayList<Profession> professions = new ArrayList<Profession>();
		String query = "SELECT * FROM cems1.users u, cems1.professions p, cems1.lecturers_professions l_p WHERE u.userID = ? "
				+ "AND u.userID = l_p.lecturerID AND l_p.professionID=p.professionID;";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, lecturerID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Profession prof = new Profession(resultSet.getString("p.professionID"),
						resultSet.getString("p.professionName"));
				professions.add(prof);
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return professions;
	}

	/**
	 * gets from db questions written by lec
	 * 
	 * @param lec wanted lecturer
	 * @return ArrayList<Question> including all the questions requested
	 */
	public ArrayList<Question> GetLecturerQuestionsFromDB(Lecturer lec) {

		ArrayList<Question> questions = new ArrayList<Question>();
		String lecturerID = lec.getId();
		String query = "SELECT * FROM cems1.questions q_table, cems1.professions p WHERE q_table.authorID = ? AND p.professionID=q_table.professionID;";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, lecturerID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Question q = new Question();
				q.setQuestionID(resultSet.getString("q_table.questionID"));
				Profession prof = new Profession(resultSet.getString("p.professionID"),
						resultSet.getString("p.professionName"));
				q.setProfession(prof);
				q.setQuestion(resultSet.getString("q_table.question"));
				q.setAuthorID(resultSet.getString("q_table.authorID"));
				q.setAnswer1(resultSet.getString("q_table.answer1"));
				q.setAnswer2(resultSet.getString("q_table.answer2"));
				q.setAnswer3(resultSet.getString("q_table.answer3"));
				q.setAnswer4(resultSet.getString("q_table.answer4"));
				q.setCorrectAnswer(resultSet.getInt("q_table.correctAnswer"));
				q.setQuestionInstructions(resultSet.getString("q_table.questionInstructions"));
				q.setUsedCounter(resultSet.getInt("q_table.usedCounter"));

				questions.add(q);
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questions;
	}
	/**
	 * Retrieves the number of questions in a specific profession from the database.
	 *
	 * @param professionID The ID of the profession.
	 * @return The number of questions in the profession.
	 */
	public int getNumOfQuestionsInProfession(String professionID) {
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT SUM(professionID=?) as sum FROM questions;");
			pstmt.setString(1, professionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int x = rs.getInt(1);
				return x;
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Saves the details of a new question in the database.
	 *
	 * @param question The Question object containing the question details to be saved.
	 * @return A Msg object indicating the success of the operation.
	 * @throws SQLException if there is an error executing the SQL query.
	 */
	public Msg saveQuestionDetails(Question question) throws SQLException {
		PreparedStatement pstmt;
		int rowsSaved = 0;
		try {
			pstmt = conn.prepareStatement("INSERT INTO cems1.questions VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			pstmt.setString(1, question.getQuestionID());
			pstmt.setString(2, question.getProfessionID());
			pstmt.setString(3, question.getAuthorID());
			pstmt.setString(4, question.getQuestion());
			pstmt.setString(5, question.getAnswer1());
			pstmt.setString(6, question.getAnswer2());
			pstmt.setString(7, question.getAnswer3());
			pstmt.setString(8, question.getAnswer4());
			pstmt.setInt(9, question.getCorrectAnswer());
			pstmt.setString(10, question.getQuestionInstructions());
			pstmt.setInt(11, 0);
			rowsSaved = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (rowsSaved == 0) {
			throw new SQLException("Question create failed, no rows affected.");
		}

		return new Msg("createNewQuestion", MsgType.SERVER_RESPONSE, question);
	}

	/**
	 * Retrieves the next available question number for a specific profession from the database.
	 * @param prof The Profession object representing the profession.
	 * @return The next available question number as an Integer, or null if an error occurs.
	 */
	public Integer getNextQuestionsNum(Profession prof) {
		PreparedStatement pstmt;
		Integer num;
		try {
			pstmt = conn.prepareStatement("SELECT numQuestions FROM cems1.professions p WHERE p.professionID= ?");
			pstmt.setString(1, prof.getProfessionID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getInt(1);
				pstmt.close();
				pstmt = conn
						.prepareStatement("UPDATE cems1.professions p SET p.numQuestions= ? WHERE p.professionID= ?");
				pstmt.setInt(1, num + 1);
				pstmt.setString(2, prof.getProfessionID());
				pstmt.executeUpdate();
				pstmt.close();
				return num;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param question : the question to be edited in DB
	 * @return Msg with msg "editQuestionDetails"
	 * @throws SQLException if an sql exception occurred
	 */
	public Msg editQuestionDetailsInDB(Question question) throws SQLException {
		String sql = "UPDATE " + dbName
				+ ".questions SET question=?, answer1=?, answer1=?, answer1=?, answer1=?, correctAnswer=?,questionInstructions=? WHERE questionID=?";
		PreparedStatement statement;
		int rowsUpdated = 0;
		try {
			statement = conn.prepareStatement(sql);
			statement.setString(1, question.getQuestion());
			statement.setString(2, question.getAnswer1());
			statement.setString(3, question.getAnswer2());
			statement.setString(4, question.getAnswer3());
			statement.setString(5, question.getAnswer4());
			statement.setInt(6, question.getCorrectAnswer());
			statement.setString(7, question.getQuestionInstructions());
			statement.setString(8, question.getQuestionID());
			rowsUpdated = statement.executeUpdate();

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (rowsUpdated == 0) {
			throw new SQLException("Question update failed, no rows affected.");
		}

		return new Msg("editQuestionDetails", MsgType.SERVER_RESPONSE, null);
	}

	/**
	 * @author Abdullah Adds a exam in the database.
	 *
	 * @param inputStream The InputStream of the file to be updated.
	 * @return A Msg object indicating the status of the file update.
	 */
	public Msg updateFile(ManualExam exam) {
		// TODO Auto-generated method stub
		InputStream inputStream = new ByteArrayInputStream(exam.getExamFile().getMybytearray());
	
		String sql = "INSERT INTO manual_exams (examID,examFile) values (?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, exam.getExam().getExamID()); // we want to make it from here
			statement.setBlob(2, inputStream);
			statement.executeUpdate();
			serverController.printToMsgArea("File Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// After Uploading the exam is created we add 1 to the numOfExams
		Boolean ifNumOfExamsIncreaced = addNumOfExams(exam.getExam().getCourseID());
		if (!ifNumOfExamsIncreaced) {
			System.out.println("Proplem in adding nums of exam");
		}
		return new Msg("UploadFile", MsgType.SERVER_RESPONSE, "successfuly");
	}

	/**
	 * @author Abdullah Checks the activation code of an exam.
	 *
	 * @param ActivationCode The activation code of the exam to check.
	 * @return A Msg object containing the status and details of the exam. details =
	 *         1. active/notActive 2.examID 3.examType
	 */
	public Msg CheckCode(String ActivationCode) {
		String query = "SELECT * FROM " + dbName + ".activated_exams WHERE activationCode = ?";
		ArrayList<String> ExamActiveAndIDList = new ArrayList<>();
		ExamActiveAndIDList.add("notActive");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, ActivationCode);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String actCode = resultSet.getString("activationCode");
					if (!actCode.equals(ActivationCode)) {
						throw new Exception("Unexpected activeCode");
					}
					if (resultSet.getString("activeStatus").equals("active")) {
						ExamActiveAndIDList.set(0, "active");
						String examID = String.valueOf(resultSet.getString("examID"));
						String examType = checkTypeExam(examID);
						ExamActiveAndIDList.add(examID);
						ExamActiveAndIDList.add(examType);
					

					}

				}
			} catch (Exception e) {
				System.out.println("somthing Wrong happen by finding active exam");
			}

		} catch (SQLException e) {
			System.out.println("Sql exception active exam 1");

		}
		return new Msg("IfExamActive", MsgType.SERVER_RESPONSE, ExamActiveAndIDList);
	}

	/*
	 * @Abdullah Functionality : helper for CheckCode to find Exam Type input :
	 * ExamId String
	 * 
	 * @returns examType String
	 */
	public String checkTypeExam(String examID) {
		try {
			String query = "SELECT * FROM " + dbName + ".exams WHERE examID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, examID);
			System.out.println("we are in:" + examID);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String ExamType = resultSet.getString("examType");
					return ExamType;
				}
			}
		} catch (SQLException e) {
			System.out.println("Sql exception active exam");
		}
		return "noExamLikeThat";

	}

	/**
	 * @author Abdullah Downloads a manual exam for a student.
	 *
	 * @param exam1 The ManualExam object representing the exam to download.
	 * @return A Msg object containing the downloaded exam as a MyFile object.
	 */
	public Msg StudentdownloadManualExam(ManualExam exam1) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM manual_exams WHERE examID = ?";
		InputStream inputStream = null;
		MyFile exam = null;
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, Integer.parseInt(exam1.getExamCode()));

			// Execute the query
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// Fetch the blob data
					inputStream = resultSet.getBinaryStream("examFile");
					try {
						String path = new File("").getCanonicalPath();
						// we want to send the path of the selected directory to save it into the client
						String LocalfilePath = exam1.getPathToDownload() + "\\exam.docx";
						File newFile = new File(LocalfilePath);
						exam = new MyFile(LocalfilePath);
						byte[] mybytearray = new byte[inputStream.available()];
						// FileInputStream fis = (FileInputStream)inputStream;
						BufferedInputStream bis = new BufferedInputStream(inputStream);
						exam.initArray(mybytearray.length);
						exam.setSize(mybytearray.length);
						bis.read(exam.getMybytearray(), 0, mybytearray.length);
						inputStream.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			} catch (Exception e) {
				System.out.println("Exception StudentdownloadManualExam");
			}
		} catch (SQLException e) {
			System.out.println("Exception StudentdownloadManualExam");

		}
		return new Msg("StudentDownload", MsgType.SERVER_RESPONSE, exam);
	}

	/*
	 * @Abdullah
	 *
	 * Uploads a file for a student's manual exam.
	 *
	 * @param Exam The ManualExam object representing the student's exam
	 * information.
	 * 
	 * @return A Msg object indicating the status of the file upload.
	 */
	public Msg UploadFileStudentManualExam(ManualExam Exam) {

		Msg msg = new Msg("FileUploaded", MsgType.SERVER_RESPONSE, "successfuly");
		InputStream inputStream = new ByteArrayInputStream(Exam.getExamFile().getMybytearray());
		//String query = "INSERT INTO manualexamofstudent (studentID, examID, examFile) VALUES (?, ?, ?)";
		String query = "INSERT INTO manualexamofstudent (studentID, examID,activationCode, examFile) VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, Exam.getStudentID());
			statement.setString(2, Exam.getExam().getExamID());
			statement.setString(3, Exam.getExamCode()); // ExamID
			statement.setBlob(4, inputStream);
			statement.executeUpdate();
			serverController.printToMsgArea("File Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return msg;
	}

	/**
	 * Retrieves the courses associated with a list of professions.
	 *
	 * @param profs An ArrayList of Profession objects representing the desired professions.
	 * @return A Msg object containing the response message and the retrieved courses as a list.
	 */
	public Msg GetLecturerCourses(ArrayList<Profession> profs) {

		String query = "SELECT * FROM courses WHERE professionID = ?";

		ArrayList<Course> courses = new ArrayList<>();
		Iterator<Profession> iterator = profs.iterator();
		while (iterator.hasNext()) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement(query);
				String profID = iterator.next().getProfessionID();
				preparedStatement.setString(1, profID);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						String CourseName = resultSet.getString("courseName");
						String CourseID = resultSet.getString("courseID");
						Course c = new Course(CourseID, CourseName, profID);
						courses.add(c);
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		Msg coursesMsg = new Msg("getCoursesOfLec", MsgType.SERVER_RESPONSE, courses);
		return coursesMsg;
	}

	/**
	 * Updates the status of a user in the database to indicate logout.
	 *
	 * @param user The User object representing the user to be logged out.
	 * @return A Msg object indicating the success of the logout operation.
	 */
	public Msg logoutUserDB(User user) {
		String query = "UPDATE cems1.users SET status=0 WHERE username = ? ;";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, user.getUsername());
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 0) {
				// Update wasn't successful
				throw new Exception("Updating logout failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Msg("logout", MsgType.SERVER_RESPONSE, null);
	}

	// Create Exam ID
	/**
	 * Creates an exam ID for a given Exam object.
	 *
	 * @param ex The Exam object for which the exam ID needs to be created.
	 * @return A Msg object containing the response message and the Exam object with the generated exam ID.
	 */
	public Msg CreateExamID(Exam ex) {
		// first of all we return the num of exams in the course from ex.courseID
		String query = "SELECT * FROM courses WHERE courseID = ?";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, ex.getCourseID());
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String numOfexams = resultSet.getString("numExams");
					Integer num = Integer.parseInt(numOfexams);
					ex.setExamID(ex.getProfessionID() + ex.getCourseID() + String.format("%02d", num));

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		Msg data = new Msg("ExamIDCreated", MsgType.SERVER_RESPONSE, ex);

		return data;

	}

	/**
	Increments the number of exams for a given course.
	@param courseID The ID of the course for which the number of exams should be incremented.
	@return A boolean value indicating the success of the operation.
	*/
	public Boolean addNumOfExams(String courseID) {
		String updateQuery = "UPDATE courses SET numExams = numExams + 1 WHERE courseID = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = conn.prepareStatement(updateQuery);
			preparedStatement.setString(1, courseID);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 0) {
				// Update wasn't successful
				throw new Exception("Updating logout failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Fills in the exam details during the creation step and inserts them into the database.
	 *
	 * @param ex The Exam object containing the details of the exam to be filled.
	 * @return A Msg object indicating the success of the operation.
	 */
	public Msg fillExamDetailsCreatingStep(Exam ex) {
		String sql = "INSERT INTO exams (examID,duration,authorID,professionID,courseID,examType,lecturerNotes,studentNotes,activatedCounter) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, ex.getExamID()); // we want to make it from here
			statement.setInt(2, ex.getDuration());
			statement.setString(3, ex.getAuthorID());
			statement.setString(4, ex.getProfessionID());
			statement.setString(5, ex.getCourseID());
			statement.setString(6, ex.getExamType());
			statement.setString(7, ex.getLecturerNotes());
			statement.setString(8, ex.getStudentNotes());
			statement.setString(9, ex.getActivatedCounter());
			statement.executeUpdate();
			serverController.printToMsgArea("exam added to the List");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("UploadFile", MsgType.SERVER_RESPONSE, "successfuly");
	}

	/**
	 * Deletes a question from the database.
	 *
	 * @param question The Question object representing the question to be deleted.
	 * @return A Msg object indicating the success of the operation.
	 */
	public Msg deleteQuestionFromDB(Question question) {
		String query = "DELETE FROM cems1.questions q WHERE q.questionID=?";
		try {
			PreparedStatement statement = conn.prepareStatement(query);

			statement.setString(1, question.getQuestionID());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("deleteQuestion", MsgType.SERVER_RESPONSE, null);
	}

	// @Omar
	/**
	 * Deletes an exam from the database.
	 *
	 * @param exam The Exam object representing the exam to be deleted.
	 * @return A Msg object indicating the success of the operation.
	 */
	public Msg deleteExamFromDB(Exam exam) {
		String query = "DELETE FROM cems1.exams q WHERE q.examID=?";
		try {
			PreparedStatement statement = conn.prepareStatement(query);

			statement.setString(1, exam.getExamID());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("deleteExam", MsgType.SERVER_RESPONSE, null);
	}

	/**
	 * Retrieves exams associated with a specific lecturer from the database.
	 *
	 * @param lec The Lecturer object representing the lecturer for whom to retrieve exams.
	 * @return A Msg object containing the response message and a list of Exam objects associated with the lecturer.
	 */
	public Msg getExamsByLecFromDB(Lecturer lec) {
		ArrayList<Exam> exams = new ArrayList<Exam>();
		String query = "SELECT * FROM cems1.exams e, cems1.professions p, cems1.courses c WHERE e.authorID= ? AND e.professionID=p.professionID AND e.courseID=c.courseID";
		Msg msg = new Msg("getExamsByLec", MsgType.SERVER_RESPONSE, exams);
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, lec.getId());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String profID = resultSet.getString("e.professionID");
				String profName = resultSet.getString("p.professionName");
				Profession prof = new Profession(profID, profName);

				String courseID = resultSet.getString("e.courseID");
				String courseName = resultSet.getString("c.courseName");
				Course course = new Course(courseID, courseName, prof);

				String examID = resultSet.getString("e.examID");
				String authorID = resultSet.getString("e.authorID");
				String examType = resultSet.getString("e.examType");
				String lecturerNotes = resultSet.getString("e.lecturerNotes");
				String studentNotes = resultSet.getString("e.studentNotes");
				String activatedCounter = resultSet.getString("e.activatedCounter");
				Integer duration = resultSet.getInt("e.duration");
				Exam exam = new Exam(examID, authorID, examType, lecturerNotes, studentNotes, activatedCounter, course,
						duration);
				if(examType.equals("online")) {
					Exam e= (Exam)getOnlineExamDetails(examID).getData();
					exam.setExamQuestions(e.getExamQuestions());
				}
				
			
				exams.add(exam);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return msg;
	}

	/**
	 * Retrieves questions for a specific profession from the database.
	 *
	 * @param profId The ID of the profession for which to retrieve questions.
	 * @return A Msg object containing the response message and a list of Question objects for the specified profession.
	 */
	public Msg fillQuestionsByProf(String profId) {
		String query = "SELECT * FROM " + dbName
				+ ".questions q,professions p,users u  WHERE q.professionID = ? AND p.professionID = q.professionID AND u.userID=q.authorID";
		ArrayList<Question> questions = new ArrayList<>();
		Msg msg = new Msg("fillQuestionsByProf", MsgType.SERVER_RESPONSE, "Empty");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, profId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					Question question = new Question();
					question.setQuestionID(resultSet.getString("q.questionID"));
					question.setProfession(new Profession(resultSet.getString("q.professionID"),
							resultSet.getString("p.professionName")));
					question.setAuthor(new User(resultSet.getString("u.userID"), resultSet.getString("u.firstName"),
							resultSet.getString("u.lastName")));
					question.setQuestion(resultSet.getString("q.question"));
				
					question.setAnswer1(resultSet.getString("q.answer1"));
					question.setAnswer2(resultSet.getString("q.answer2"));
					question.setAnswer3(resultSet.getString("q.answer3"));
					question.setAnswer4(resultSet.getString("q.answer4"));
					question.setCorrectAnswer(resultSet.getInt("q.correctAnswer"));
					question.setQuestionInstructions(resultSet.getString("q.questionInstructions"));
					question.setUsedCounter(resultSet.getInt("q.usedCounter"));
					questions.add(question);
				}

			}
		} catch (SQLException e) {
			System.out.println("CAUGHT AN EXCEPTION mysqlConnection class");
		}
		msg.setData(questions);
		return msg;

	}

	/**
	 * Creates a new online exam and adds it to the database.
	 *
	 * @param exam The OnlineExam object representing the exam to be created.
	 * @return A Msg object indicating the success of the operation.
	 */
	public Msg createNewOnlineExam(OnlineExam exam) {
		String sql = "INSERT INTO questions_in_exams (examID,questionID,points,serialNumber) values (?, ?, ?, ?)";
		ArrayList<Question> questionsInExam = exam.getQuestionsInExam();
		Iterator<Question> iterator = questionsInExam.iterator();
		while (iterator.hasNext()) {
			Question question = iterator.next();
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, exam.getExam().getExamID()); // we want to make it from here
				statement.setString(2, question.getQuestionID());
				statement.setString(3, question.getQuestionPoints());
				statement.setInt(4, question.getSerialNumber());
				statement.executeUpdate();
				// serverController.printToMsgArea("examCreated");
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Boolean ifNumOfExamsIncreaced = addNumOfExams(exam.getExam().getCourseID());

		if (!ifNumOfExamsIncreaced) {
			System.out.println("Proplem in adding nums of exam");
		}
		return new Msg("createNewOnlineExam", MsgType.SERVER_RESPONSE, "SuccessfulyCreated");
	}

	/**
	 * Updates the used counter of questions in the database.
	 *
	 * @param questions The ArrayList of Question objects representing the questions to update.
	 * @return true if the update was successful, false otherwise.
	 */
	public Boolean addingCntUsedOfQuestions(ArrayList<Question> questions) {
		String sql = "UPDATE questions SET usedCounter = ? WHERE questionID = ?";
		Iterator<Question> iterator = questions.iterator();
		while (iterator.hasNext()) {
			Question question = iterator.next();
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setInt(1, question.getUsedCounter()); // we want to make it from here
				statement.setString(2, question.getQuestionID());
				statement.executeUpdate();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// Abduallah
	/**
	 * Retrieves the activated exams of a specific lecturer from the database.
	 *
	 * @param lecID The ID of the lecturer.
	 * @return A Msg object containing the activated exams as an ArrayList of ActivatedExam objects.
	 */
	public Msg getActivatedExamsOfLec(String lecID) {

		String query = "SELECT * FROM activated_exams WHERE activatedLecturerID = ?";
		PreparedStatement statement;
		ArrayList<ActivatedExam> actExams = new ArrayList<>();

		try {
			statement = conn.prepareStatement(query);
			statement.setString(1, lecID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String activationCode = resultSet.getString("activationCode");
				String examID = resultSet.getString("examID");
				String timeRequestStatus = resultSet.getString("timeRequestStatus");
				int actualDuration = resultSet.getInt("actualDuration");
				String activeStatus = resultSet.getString("activeStatus");
				String activatedLecturerID = resultSet.getString("activatedLecturerID");
				int studentsTaken = resultSet.getInt("studentsTaken");
				int studentsSubmitted = resultSet.getInt("studentsSubmitted");
				int timeReq = resultSet.getInt("timeReq");
				ActivatedExam ex = new ActivatedExam(activationCode, examID, timeRequestStatus, actualDuration,
						activeStatus, activatedLecturerID, studentsTaken, studentsSubmitted);
				ArrayList<String> lst = getExamCourseNameAndExamType(examID);
				ex.setCourseName(lst.get(0));
				ex.setExamType(lst.get(1));
				ex.setTimeRequested(timeReq);
				actExams.add(ex);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("getActivatedExamsOfLec", MsgType.SERVER_RESPONSE, actExams);

	}

	/**
	 * Retrieves the course name and exam type for a specific exam from the database.
	 *
	 * @param examID The ID of the exam.
	 * @return An ArrayList of Strings containing the course name and exam type.
	 */
	public ArrayList<String> getExamCourseNameAndExamType(String ExamID) {
		String sqlQuery = "SELECT c.courseName, e.examType FROM courses c, exams e WHERE e.examID = ? AND e.courseID = c.courseID";
		PreparedStatement statement;

		ArrayList<String> lst = new ArrayList<>();
		try {
			statement = conn.prepareStatement(sqlQuery);
			statement.setString(1, ExamID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String CourseName = resultSet.getString("c.courseName");// getcoursesname
				String examType = resultSet.getString("e.examType");
				lst.add(CourseName);
				lst.add(examType);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lst;

	}

	/**
	 * Activates an exam by adding it to the activated_exams table in the database.
	 * @param examToActivate The ActivatedExam object representing the exam to activate.
	 * @return A Msg object indicating the success of the operation.
	 */
	public Msg activateExam(ActivatedExam examToActivate) {
		String sql = "INSERT INTO activated_exams (activationCode, examID, actualDuration, "
				+ "activeStatus, activatedLecturerID, studentsTaken, studentsSubmitted) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		boolean checkIfCodeExist = CheckIfCodeExist(examToActivate.getActivationCode());
		if (checkIfCodeExist) {
			return new Msg("activateExamCodeExist", MsgType.SERVER_RESPONSE, "Please Choose another Code");
		}
		String add1toCntActivatiedExam = "UPDATE exams SET activatedCounter = (activatedCounter+1) WHERE examID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, examToActivate.getActivationCode());
			pstmt.setString(2, examToActivate.getExam().getExamID());
			pstmt.setInt(3, examToActivate.getExam().getDuration());
			pstmt.setString(4, "active");
			pstmt.setString(5, examToActivate.getExam().getAuthorID());
			pstmt.setInt(6, 0);
			pstmt.setInt(7, 0);
			int rowsAffected = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			PreparedStatement pstmt = conn.prepareStatement(add1toCntActivatiedExam);
			pstmt.setString(1, examToActivate.getExam().getExamID());
			int rowsAffected = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("activateExam", MsgType.SERVER_RESPONSE, "SuccesfulyActivatedExam");
	}

	/**
	 * Retrieves the details of a specific question from the database.
	 *
	 * @param questionID The ID of the question.
	 * @return A Msg object containing the Question object with the question details.
	 */
	public Msg getQuestionDetails(String questionID) {
		String query = "SELECT * FROM " + dbName + ".questions WHERE questionID = ?";

		Msg msg1 = new Msg("getQuestionDetails", MsgType.SERVER_RESPONSE, null);
		Question question = new Question();
		question.setQuestionID(questionID);
		try {

			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, questionID);

			try {
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {

					String id = resultSet.getString("questionID");
					if (!id.equals(questionID)) {
						throw new Exception("Unexpected question id");
					}

					question.setProfession(new Profession(resultSet.getString("professionID")));
					question.setAuthorID(resultSet.getString("authorID"));
					question.setQuestion(resultSet.getString("question"));
					question.setAnswer1(resultSet.getString("answer1"));
					question.setAnswer2(resultSet.getString("answer2"));
					question.setAnswer3(resultSet.getString("answer3"));
					question.setAnswer4(resultSet.getString("answer4"));
					question.setCorrectAnswer(resultSet.getInt("correctAnswer"));
					question.setQuestionInstructions(resultSet.getString("questionInstructions"));

					msg1.setData(question);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg1;
	}

	/**
	 * gets the online exam details with the passed examID
	 * 
	 * @param examID
	 * @return Exam object with the details filled including the questions
	 */
	public Msg getOnlineExamDetails(String examID) {
		Exam exam = null;
		String query = "SELECT * FROM cems1.exams e, cems1.questions_in_exams qInExam WHERE e.examID=? AND e.examID=qInExam.examID;";
		System.out.println("IM HERE");
		try {
			//Exam ex= new Exam();
			//ex.setExamID(examID);
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, examID);
			ResultSet resultSet = statement.executeQuery();
			boolean flag = true;// first time flag
			while (resultSet.next()) {
				if (flag) {
					String examID1 = resultSet.getString("e.examID");
					String authorID = resultSet.getString("e.authorID");
					//ex.setAuthorID(authorID);
					
					String examType = resultSet.getString("e.examType");
					//ex.setExamType(examType);
					
					String lecturerNotes = resultSet.getString("e.lecturerNotes");
					//ex.setLecturerNotes(lecturerNotes);
					
					String studentNotes = resultSet.getString("e.studentNotes");
					//ex.setStudentNotes(studentNotes);
					
					String activatedCounter = resultSet.getString("e.activatedCounter");
					//ex.setActivatedCounter(activatedCounter);
					
					Integer duration = resultSet.getInt("e.duration");
					//ex.setDuration(duration);
					
					Course course = getExamCourse(examID);
					//ex.setCourse(course);
					System.out.println("I CREATED AN EXAM1S");
					exam = new Exam(examID, authorID, examType, lecturerNotes, studentNotes, activatedCounter, course, duration);
					System.out.println("I CREATED AN EXAM");
					flag = false;
				}
				String questionID = resultSet.getString("qInExam.questionID");
				Question question = (Question) getQuestionDetails(questionID).getData();
				question.setQuestionPoints(resultSet.getString("qInExam.points"));
				question.setSerialNumber(resultSet.getInt("qInExam.serialNumber"));
				exam.getExamQuestions().add(question);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		if (exam != null) {
			Collections.sort(exam.getExamQuestions(), new Comparator<Question>() {
				@Override
				public int compare(Question o1, Question o2) {
					return Integer.compare(o1.getSerialNumber(), o2.getSerialNumber());
				}
			});
		}
		return new Msg("getOnlineExamDetails", MsgType.SERVER_RESPONSE, exam);
	}

	/**
	 * Retrieves the course associated with a given exam from the database.
	 * 
	 * @param examID The ID of the exam.
	 * @return The course associated with the exam, or null if not found.
	 */
	public Course getExamCourse(String examID) {
		Course c = null;
		String query = "SELECT * FROM cems1.exams ex, cems1.courses c, cems1.professions p WHERE ex.examID= ? AND "
				+ "ex.courseID=c.courseID " + "AND ex.professionID=p.professionID;";

		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, examID);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				String courseID = rs.getString("c.courseID");
				String courseName = rs.getString("c.courseName");
				String profID = rs.getString("p.professionID");
				String profName = rs.getString("p.professionName");
				Profession prof = new Profession(profID, profName);
				c = new Course(courseID, courseName, prof);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return c;
	}

	/**
	 * get manual exam details
	 * 
	 * @param examID
	 * @return exam object with the exam details filled
	 */
	public Msg getManualExamDetails(String examID) {
		Exam exam = null;
		String query = "SELECT * FROM cems1.exams e WHERE e.examID=?";
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, examID);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String examID1 = resultSet.getString("e.examID");
				String authorID = resultSet.getString("e.authorID");
				String examType = resultSet.getString("e.examType");
				String lecturerNotes = resultSet.getString("e.lecturerNotes");
				String studentNotes = resultSet.getString("e.studentNotes");
				String activatedCounter = resultSet.getString("e.activatedCounter");
				Integer duration = resultSet.getInt("e.duration");
				exam = new Exam(examID, authorID, examType, lecturerNotes, studentNotes, activatedCounter, null,
						duration);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("getManualExamDetails", MsgType.SERVER_RESPONSE, exam);
	}

	/**
	 * Registers student in an exam in the exams_of students table in database
	 * 
	 * @param studentExam contains the relevant information about the student and
	 *                    the exam to store in the data base that s/he has started
	 *                    solving
	 * @return Msg with msg data as null (nothing special to return)
	 */
	public Msg registerStudentInExam(StudentExam studentExam) {

		String query = "INSERT INTO cems1.exams_of_students(examActivationCode,studentID,status) VALUES(?,?,?);";
		int rowsSaved = 0;
		String response = null;
		Msg msg1 = new Msg("registerStudentInExam", MsgType.SERVER_RESPONSE, null);

		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, studentExam.getActivationCode());
			statement.setString(2, studentExam.getStudentId());
			statement.setString(3, "solving");
			rowsSaved = statement.executeUpdate();
			if (rowsSaved == 0) {
				throw new Exception("Registering student failed");
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		increaseNumStudentsInExam("Taken", studentExam);
		return msg1;
	}

	/**
	 * checks whether student has taken the exam with activationCode specified in
	 * studentExam or not
	 * 
	 * @param studentExam includes information about the student, the exam, the
	 *                    activation code and other relevant info
	 * @return Msg data true if student has already performed this exam Msg data
	 *         false if the student has not performed this exam
	 */
	public Msg hasStudentPerformed(StudentExam studentExam) {
		String query = "SELECT * FROM cems1.exams_of_students WHERE studentID=? AND examActivationCode=? ;";
		String response;
		Msg msg1 = new Msg("hasStudentPerformed", MsgType.SERVER_RESPONSE, null);
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, studentExam.getStudentId());
			statement.setString(2, studentExam.getActivationCode());

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				msg1.setData("true");
			else {
				msg1.setData("false");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg1;
	}

	/**
	 * terminates manual exam for student, saves info if student has submitted or
	 * not, and the time taken to solve the exam
	 * 
	 * @param studentExam includes information about the exam being terminated and
	 *                    the information about the student performing
	 * @return Msg object with message "terminateStudentExam" and no msgData
	 */
	public Msg terminateManualStudentExam(StudentExam studentExam) {
		String query = "UPDATE cems1.exams_of_students SET status= ? , submitted = ?, solvingTime=? WHERE studentID= ? AND examActivationCode=? ;";

		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, "finished");

			String submit = studentExam.isSubmitted() ? "true" : "false";
			statement.setString(2, submit);

			statement.setInt(3, studentExam.getSolvingTime());
			statement.setString(4, studentExam.getStudentId());
			statement.setString(5, studentExam.getActivationCode());

			int rows = statement.executeUpdate();
			if (rows == 0) {
				throw new Exception("Updating the students exam failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (studentExam.isSubmitted()) {
			increaseNumStudentsInExam("Submitted", studentExam);
		}
		return new Msg("terminateStudentExam", MsgType.SERVER_RESPONSE, null);
	}

	/**
	 * terminates online exam for student, saves info if student has submitted or
	 * not, and the time taken to solve the exam, and their answers in the exam and
	 * their grade
	 * 
	 * @param studentExam includes information about the exam being terminated and
	 *                    the information about the student performing
	 * @return Msg object with message "terminateStudentExam" and no msgData
	 */
	public Msg terminateOnlineStudentExam(StudentExam studentExam) {
		terminateManualStudentExam(studentExam);
		String query1 = "UPDATE cems1.exams_of_students e_s SET e_s.grade=? WHERE e_s.studentID= ? AND e_s.examActivationCode=? ;";
		String query2 = "INSERT INTO cems1.students_answer_in_exam(studentID,activationCode,questionID,studentAnswerIndx,correct) VALUES(?,?,?,?,?);";

		try {
			PreparedStatement statement = conn.prepareStatement(query1);
			statement.setInt(1, studentExam.getGrade());
			statement.setString(2, studentExam.getStudentId());
			statement.setString(3, studentExam.getActivationCode());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Question> answers = studentExam.getExamQuestions();
		for (Question ans : answers) {
			try {
				PreparedStatement statement = conn.prepareStatement(query2);
				statement.setString(1, studentExam.getStudentId());
				statement.setString(2, studentExam.getActivationCode());
				statement.setString(3, ans.getQuestionID());
				statement.setInt(4, ans.getStudentAnswer());
				String correct = ans.isCorrectAns() ? "true" : "false";
				statement.setString(5, correct);
				statement.executeUpdate();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (studentExam.isSubmitted()) {
			increaseNumStudentsInExam("Submitted", studentExam);
		}
		return new Msg("terminateStudentExam", MsgType.SERVER_RESPONSE, null);
	}

	/**
	 * Helping function to register student to exam and terminate student exam
	 * Updates the number of the student that took the exam or have submitted
	 * (updating is according to param takenOrSubmitted)
	 * 
	 * @param takenOrSubmitted is either the string "Taken" or the string
	 *                         "Submitted"
	 * @param studentExam      includes the exams information including activation
	 *                         code and exam id
	 */
	public void increaseNumStudentsInExam(String takenOrSubmitted, StudentExam studentExam) {
		Integer num = getNumStudentInExam(takenOrSubmitted, studentExam);
		setStudentNumInExam(takenOrSubmitted, studentExam, num + 1);
	}

	/**
	 * helping function to addStudentsToExam, get the num of student taken/submitted
	 * a certain exam
	 * 
	 * @param takenOrSubmitted is either the string "Taken" or the string
	 *                         "Submitted"
	 * @param studentExam
	 * @return includes the exams information including activation code and exam id
	 */
	public Integer getNumStudentInExam(String takenOrSubmitted, StudentExam studentExam) {
		Integer num = null;
		if (takenOrSubmitted.equals("Taken") || takenOrSubmitted.equals("Submitted")) {
			String getNum = "SELECT students" + takenOrSubmitted
					+ " FROM cems1.activated_exams  WHERE activationCode=?;";
			try {
				PreparedStatement statement = conn.prepareStatement(getNum);
				// statement.setString(1, studentExam.getStudentId());
				statement.setString(1, studentExam.getActivationCode());
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					num = rs.getInt(1);
					return num;
				}
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return num;
	}

	/**
	 * helping function to addStudentsToExam, sets the number of student
	 * taken/submitted a certain exam
	 * 
	 * @param takenOrSubmitted is either the string "Taken" or the string
	 *                         "Submitted"
	 * @param studentExam      includes exam info
	 * 
	 */
	public void setStudentNumInExam(String takenOrSubmitted, StudentExam studentExam, Integer newNum) {
		if (takenOrSubmitted.equals("Taken") || takenOrSubmitted.equals("Submitted")) {
			String setNewNum = "UPDATE cems1.activated_exams SET students" + takenOrSubmitted
					+ " =? WHERE activationCode=?;";
			try {
				PreparedStatement statement = conn.prepareStatement(setNewNum);
				statement.setInt(1, newNum);
				statement.setString(2, studentExam.getActivationCode());
				Integer rows = statement.executeUpdate();
				if (rows == 0) {
					throw new Exception("No rows were affected");
				}
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * gets a certain exam performed by the student
	 * 
	 * @param studentExam: studentId of wanted student, and activationCode of wanted
	 *                     exam must be not null
	 * @return the exam details of the exam including the questions and the students
	 *         answers in it
	 */
	public Msg getCertainExamByStudent(StudentExam studentExam) {
		String examID;
		getSolvingDetailsOfExam(studentExam);
		String query = "SELECT * FROM cems1.activated_exams WHERE activationCode=?;";
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, studentExam.getActivationCode());
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				examID = rs.getString("examID");
				Msg examDetails = getOnlineExamDetails(examID);
				studentExam.setExam((Exam) examDetails.getData());
				if (studentExam.getExam() != null) {
					for (Question q : studentExam.getExamQuestions()) {
						getStudentAnsInQuestion(studentExam, q);
					}
				}

			}
			System.out.println(studentExam);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Msg("getCertainExamByStudent", MsgType.SERVER_RESPONSE, studentExam);
	}

	/**
	 * Retrieves the students answer from the data base for a specific question
	 * 
	 * @param studentExam: includes the students id and the activation code of the
	 *                     exam
	 * @param q            the question specified to get the answer for no return,
	 *                     the information is saved directly in the passed argument
	 *                     q
	 */
	public void getStudentAnsInQuestion(StudentExam studentExam, Question q) {

		String query = "SELECT * FROM cems1.students_answer_in_exam sae WHERE sae.studentID=? AND sae.activationCode=? AND questionID =?;";
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, studentExam.getStudentId());
			statement.setString(2, studentExam.getActivationCode());
			statement.setString(3, q.getQuestionID());
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				q.setStudentAnswer(rs.getInt("studentAnswerIndx"));
				boolean correct = rs.getString("correct").equals("true") ? true : false;
				q.setCorrectAns(correct);

			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the solving details of a certain exam ( grade, submittion status, solving
	 * status, solvingTime, available for viewing..) in short : all info in
	 * exams_of_students table in db for a certain student and exam
	 * 
	 * @param studentExam the student id and the activation code are provided (must
	 *                    be not null) no return, the information is inserted in the
	 *                    passed parameter studentExam
	 */
	public void getSolvingDetailsOfExam(StudentExam studentExam) {
		String query = "SELECT * FROM cems1.exams_of_students es WHERE es.studentID= ? AND es.examActivationCode=?";
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, studentExam.getStudentId());
			statement.setString(2, studentExam.getActivationCode());
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				boolean submitted = rs.getString("submitted").equals("true") ? true : false;
				studentExam.setSubmitted(submitted);

				studentExam.setGrade(rs.getInt("grade"));

				boolean gChange = rs.getString("gradeChanged").equals("true") ? true : false;
				studentExam.setGradeChanged(gChange);

				studentExam.setLecturerNotes(rs.getString("lecturerNotes"));
				studentExam.setReasonOfGradeChange(rs.getString("reasonOfGradeChange"));

				boolean available = rs.getString("availableForViewing").equals("true") ? true : false;
				studentExam.setAvailableForVieweing(available);

				studentExam.setSolvingTime(rs.getInt("solvingTime"));
				studentExam.setStatus(rs.getString("status"));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets all student copies of performed exams that were activated by the
	 * provided lecturer ID
	 * 
	 * @param lecID the activated lecturer ID
	 * @return Msg with msg= "getStudentsExamCopyOfLec" and msgData; an array list
	 *         of StudentExam objects with all students that performed an exam the
	 *         lec activated
	 */
	public Msg getStudentsExamCopyOfLec(String lecID) {
		Msg msg = getActivatedExamsOfLec(lecID);
		ArrayList<ActivatedExam> examsByLec = (ArrayList<ActivatedExam>) msg.getData();
		ArrayList<StudentExam> allCopies = new ArrayList<StudentExam>();
		for (ActivatedExam acExam : examsByLec) {
			if(acExam.getExamType().equals("online")) {
				allCopies.addAll(getStudentsPerformedExam(acExam));
			}
		}
		System.out.println(allCopies);

		for (StudentExam studentExam : allCopies) {
			//System.out.println(studentExam);
			getCertainExamByStudent(studentExam);
		}
		return new Msg("getStudentsExamCopyOfLec", MsgType.SERVER_RESPONSE, allCopies);
	}

	/**
	 * helping function for getStudentsExamCopyOfLec gets all students that
	 * performed exam with activation code specified in acExam
	 * 
	 * @param acExam includes the exam activation code
	 * @return an arraylist of StudentExam objects that include the student id and
	 *         the activation code of the exam
	 */
	private ArrayList<StudentExam> getStudentsPerformedExam(ActivatedExam acExam) {
		String query = "SELECT studentID, examActivationCode FROM cems1.exams_of_students WHERE examActivationCode=?;";
		ArrayList<StudentExam> allStudents = new ArrayList<StudentExam>();

		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, acExam.getActivationCode());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				StudentExam studentExam = new StudentExam();
				studentExam.setActivationCode(rs.getString("examActivationCode"));
				studentExam.setStudentId(rs.getString("studentID"));
				allStudents.add(studentExam);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allStudents;
	}

	/**
	 * helping function. gets all activation codes for online exams that student (of
	 * provided studentID) have performed
	 * 
	 * @param studentID of wanted student
	 * @return an array list with all the exam activation codes that the student
	 *         performed
	 */
	public ArrayList<String> getActivationCodesPerformedByStudent(String studentID) {
		ArrayList<String> codes = new ArrayList<String>();
		String query = "SELECT estudent.examActivationCode, ex.examType  FROM cems1.exams_of_students estudent, cems1.exams ex, cems1.activated_exams ac_ex "
				+ "WHERE  estudent.studentID=? AND  estudent.examActivationCode=ac_ex.activationCode AND ac_ex.examID=ex.examID;";
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, studentID);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				if(rs.getString("ex.examType").equals("online")){
					codes.add(rs.getString("estudent.examActivationCode"));
				}
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codes;
	}

	/**
	 * Gets all exam copies of student specified with studentID
	 * 
	 * @param studentId
	 * @return
	 */
	public Msg getAllExamCopiesOfStudent(String studentId) {
		ArrayList<StudentExam> allCopies = new ArrayList<StudentExam>();
		ArrayList<String> codes = getActivationCodesPerformedByStudent(studentId);
		for (String code : codes) {
			StudentExam studentExam = new StudentExam();
			studentExam.setStudentId(studentId);
			studentExam.setActivationCode(code);
			getCertainExamByStudent(studentExam);
			
			allCopies.add(studentExam);
		}
		return new Msg("getAllExamCopiesOfStudent", MsgType.SERVER_RESPONSE, allCopies);
	}

	/**
	 * get all users from database
	 * 
	 * @return msg with msg string "getAllUsers" and message data containing
	 *         arrayList<User> containing the users
	 */
	public Msg getAllUsers() {
		String query = "SELECT * FROM cems1.users";
		ArrayList<User> users = new ArrayList<User>();
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("userID");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String email = resultSet.getString("email");
				String role = resultSet.getString("role");
				Integer status = resultSet.getInt("status");
				String phone = resultSet.getString("phone");
				User user = new User(id, username, password, firstName, lastName, email, UserType.getType(role),
						status);
				user.setPhone(phone);
				users.add(user);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Msg msg = new Msg("getAllUsers", MsgType.SERVER_RESPONSE, users);
		return msg;
	}
	// Abdullah start 952
	// Abdullah start 620
	/**
	 * @Functionality:Locks an activated exam in the database, setting its active status to "inActive".
	 * This prevents any further submissions or modifications to the exam.
	 * 
	 * @param activationCode The activation code of the exam to be locked.
	 * @return A message indicating the status of the operation and a list of student IDs affected by the lock.
	 */
	public Msg lockActivatedExam(String activationCode) {
		String updateQuery = "UPDATE activated_exams SET activeStatus = ? WHERE activationCode = ?";
		List<String> studentIDs = new ArrayList<>();
		try {
			PreparedStatement statement = conn.prepareStatement(updateQuery);
			statement.setString(1, "inActive");
			statement.setString(2, activationCode);

			int rowsAffected = statement.executeUpdate();
			if (rowsAffected == 0) {
				throw new Exception("Update failed");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String selectQuery = "SELECT studentID FROM exams_of_students WHERE examActivationCode = ?  AND status = 'solving'";
		try {
			PreparedStatement statement = conn.prepareStatement(selectQuery);
			statement.setString(1, activationCode);
			ResultSet resultSet = statement.executeQuery();
			// Iterate over the result set and retrieve the student IDs
			while (resultSet.next()) {
				String studentID = resultSet.getString("studentID");
				studentIDs.add(studentID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new Msg("lockActivatedExam", MsgType.SERVER_NOTIFICATION, studentIDs);

	}

	/**
	 *
	 * @Functionality: Updates the time request for an exam in the database.
	 * @param timeReqToExam The ActivatedExam object representing the exam with the
	 *                      updated time request information.
	 * @return The number of rows affected by the update operation.
	 */
	public int updateTimeinReqInDB(ActivatedExam timeReqToExam) {
		String sql = "UPDATE activated_exams SET timeReq = ?, timeRequestStatus = ?,reason = ? WHERE activationCode = ?";
		int rowsAffected = 0;
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, timeReqToExam.getTimeRequested());
			statement.setString(2, "pending");
			statement.setString(3, timeReqToExam.getReasonTimeReq());
			statement.setString(4, timeReqToExam.getActivationCode());
			rowsAffected = statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowsAffected;
	}

	/**
	 *
	 * @Functionality: Retrieves the exams with time requests.
	 * @return A server response message object containing the list of ActivatedExam
	 *         objects representing the exams with time requests.
	 */
	public Msg getTimeReqExams() {
		String query = "SELECT * FROM activated_exams WHERE activeStatus='active' AND timeReq IS NOT NULL";
		PreparedStatement statement;
		ArrayList<ActivatedExam> timeReqExams = new ArrayList<>();
		try {
			statement = conn.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String activationCode = resultSet.getString("activationCode");
				String examID = resultSet.getString("examID");
				String timeRequestStatus = resultSet.getString("timeRequestStatus");
				int actualDuration = resultSet.getInt("actualDuration");
				String activeStatus = resultSet.getString("activeStatus");
				String activatedLecturerID = resultSet.getString("activatedLecturerID");
				int studentsTaken = resultSet.getInt("studentsTaken");
				int studentsSubmitted = resultSet.getInt("studentsSubmitted");
				int timeReq = resultSet.getInt("timeReq");
				ActivatedExam ex = new ActivatedExam(activationCode, examID, timeRequestStatus, actualDuration,
						activeStatus, activatedLecturerID, studentsTaken, studentsSubmitted);
				ArrayList<String> lst = getExamCourseNameAndExamType(examID);
				ex.setCourseName(lst.get(0));
				ex.setExamType(lst.get(1));
				ex.setTimeRequested(timeReq);
				ex.setReasonTimeReq(resultSet.getString("reason"));
			
				timeReqExams.add(ex);
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("getTimeReqExams", MsgType.SERVER_RESPONSE, timeReqExams);
	}

	/**
	 *
	 * @Functionality: Approves the time request for an exam and update
	 *                 timeRequestStatus= approving .
	 * @param activationCode The activation code of the exam for which the time
	 *                       request is being approved.
	 * @return A server notification message object containing the list of student
	 *         IDs currently solving the exam.
	 */

	public Msg approveTimeReqForExam(String activationCode) {
		String updateQuery = "UPDATE activated_exams SET timeRequestStatus = ? WHERE activationCode = ?";
		List<String> studentIDs = new ArrayList<>();
		try {
			PreparedStatement statement = conn.prepareStatement(updateQuery);
			statement.setString(1, "approved");
			statement.setString(2, activationCode);
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected > 0) {
			} else {
				System.out.println("No rows were updated.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String selectQuery = "SELECT studentID FROM exams_of_students WHERE examActivationCode = ? AND status='solving'";
		try {
			PreparedStatement statement = conn.prepareStatement(selectQuery);
			statement.setString(1, activationCode);
			ResultSet resultSet = statement.executeQuery();
			// Iterate over the result set and retrieve the student IDs
			while (resultSet.next()) {
				String studentID = resultSet.getString("studentID");
				studentIDs.add(studentID);
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("approveTimeReqForExam", MsgType.SERVER_NOTIFICATION, studentIDs);
	}

	/**
	 *
	 * @Functionality: Declines the time request for an exam and update it in DB.
	 * @param activationCode The activation code of the exam for which the time
	 *                       request is being declined.
	 * @return A server response message object indicating that the exam time
	 *         request has been declined.
	 */

	public Msg declineTimeReqForExam(String activationCode) {
		String updateQuery = "UPDATE activated_exams SET timeRequestStatus = ? WHERE activationCode = ?";
		List<String> studentIDs = new ArrayList<>();
		try {
			PreparedStatement statement = conn.prepareStatement(updateQuery);
			statement.setString(1, "declined");
			statement.setString(2, activationCode);
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Update successful!");
			} else {
				System.out.println("No rows were updated.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Msg("declineTimeReqForExam", MsgType.SERVER_RESPONSE, "ExamDecliendAddingTime");

	}

	// Abdullah start 1261
	/**
	 * @author Abdullah Checks the activation code of an exam if exist.
	 *
	 * @param ActivationCode The activation code of the exam to check.
	 * @return boolean of the CheckIfCodeExist true false
	 */
	public boolean CheckIfCodeExist(String ActivationCode) {
		String query = "SELECT * FROM " + dbName + ".activated_exams WHERE activationCode = ?";
		boolean flag = false;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, ActivationCode);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					flag = true;
				}
			} catch (Exception e) {
				System.out.println("something Wrong happened by finding active exam");
			}
		} catch (SQLException e) {
			System.out.println("Sql exception active exam");

		}
		return flag;
	}

	/**
	 *
	 * @Functionality: Updates the grade information for a student's exam.
	 * @param studentExam The StudentExam object containing the updated grade
	 *                    information.
	 * @return A server response message object with no data.
	 */
	public Msg gradeApproved(StudentExam studentExam) {
		String query = "UPDATE cems1.exams_of_students SET grade =?, gradeChanged=? , lecturerNotes=?, reasonOfGradeChange=?, availableForViewing=? WHERE studentID=? AND examActivationCode=?";
		PreparedStatement statement;
		int rowsUpdated = 0;
		try {
			statement = conn.prepareStatement(query);
			statement.setString(1, studentExam.getGrade().toString());

			String change = studentExam.isGradeChanged() ? "true" : "false";
			statement.setString(2, change);

			statement.setString(3, studentExam.getLecturerNotes());
			statement.setString(4, studentExam.getReasonOfGradeChange());

			String available = studentExam.isAvailableForVieweing() ? "true" : "false";
			statement.setString(5, available);

			statement.setString(6, studentExam.getStudentId());
			statement.setString(7, studentExam.getActivationCode());

			rowsUpdated = statement.executeUpdate();
			if (rowsUpdated == 0) {
				throw new SQLException("Question update failed, no rows affected.");
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("gradeApproved", MsgType.SERVER_RESPONSE, null);
	}

//Abdullah
	/**
	 * @Functionality: Analyzes the distribution of grades for exams with a specific
	 *                 activation code.
	 * @param activationCode The activation code of the exams for which the grade
	 *                       distribution is to be analyzed.
	 * @return A server response message object containing a list with the counts of
	 *         grades in each range.
	 */
	public Msg getGradeAnalyzer(String activationCode) {
		String sql = "SELECT grade FROM exams_of_students WHERE availableForViewing = 'true' AND examActivationCode = ?";
		int count0To55 = 0;
		int count56To65 = 0;
		int count66To75 = 0;
		int count76To85 = 0;
		int count86To95 = 0;
		int count95To100 = 0;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, activationCode);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int grade = resultSet.getInt("grade");
					// Increment the respective count variable based on the grade range
					if (grade >= 0 && grade <= 55) {
						count0To55++;
					} else if (grade >= 56 && grade <= 65) {
						count56To65++;
					} else if (grade >= 66 && grade <= 75) {
						count66To75++;
					} else if (grade >= 76 && grade <= 85) {
						count76To85++;
					} else if (grade >= 86 && grade <= 95) {
						count86To95++;
					} else if (grade >= 96 && grade <= 100) {
						count95To100++;
					}
				}
			} catch (Exception e) {
				System.out.println("somthing Wrong happen by finding active exam");
			}
		} catch (SQLException e) {
			System.out.println("Sql exception active exam");

		}

		List<Number> countList = new ArrayList<>();
		countList.add(count0To55);
		countList.add(count56To65);
		countList.add(count66To75);
		countList.add(count76To85);
		countList.add(count86To95);
		countList.add(count95To100);

		return new Msg("getGradeAnalyzer", MsgType.SERVER_RESPONSE, countList);

	}

	/**
	 *
	 * @Functionality: Calculates the average and median grades for exams with a
	 *                 specific activation code.
	 * @param activationCode The activation code of the exams for which the average
	 *                       and median grades are to be calculated.
	 * @return A server response message object containing a list with the average
	 *         grade and median grade.
	 */
	public Msg returnAvgAndMedian(String activationCode) {
		String sql = "SELECT grade FROM exams_of_students WHERE availableForViewing = 'true' AND examActivationCode = ?";
		float sum = 0;
		float avg = 0;
		List<Number> AvgMedian = new ArrayList<>();
		List<Integer> gradeList = new ArrayList<>();

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, activationCode);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int grade = resultSet.getInt("grade");
					gradeList.add(grade);
					sum = sum + grade;
					// Increment the respective count variable based on the grade range
				}
				avg = sum / gradeList.size();
				Collections.sort(gradeList);
				int median;
				int middle = gradeList.size() / 2;
				median = gradeList.get(middle);
				AvgMedian.add(avg);
				AvgMedian.add(median);
			} catch (Exception e) {
				System.out.println("somthing Wrong happen by finding active exam");
			}
		} catch (SQLException e) {
			System.out.println("Sql exception active exam");

		}

		return new Msg("returnAvgAndMedian", MsgType.SERVER_RESPONSE, AvgMedian);
	}

	/**
	 *
	 * @Functionality: Retrieves the activation codes of exams activated by a
	 *                 lecturer and another lecturer.
	 * @param examID The ID of the exam for which the activated exams are to be
	 *               retrieved.
	 * @return A server response message object containing an ArrayList with the
	 *         activation codes of the activated exams.
	 */
	public Msg getExamsActivatedByLecAndAnotherLec(String examID) {

		String activationCodesQuery = "SELECT * FROM activated_exams WHERE examID = ? AND activeStatus = 'inActive'";
		ArrayList<String> activationCodes = new ArrayList<>();
		// ArrayList<String> examsID =
		// (ArrayList<String>)getExamsIDOfLecturer(lecID).getData();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(activationCodesQuery);
			// for (String examID : examsID) {
			// Set the exam ID parameter
			preparedStatement.setString(1, examID);

			// Execute the query
			ResultSet resultSet = preparedStatement.executeQuery();

			// Process the results
			while (resultSet.next()) {
				String activationCode = resultSet.getString("activationCode");
				activationCodes.add(activationCode);
			
			}
			// }

		} catch (Exception e) {
			System.out.println("somthing Wrong happen by finding active exam");
		}
		return new Msg("getExamsActivatedByLecAndAnotherLec", MsgType.SERVER_RESPONSE, activationCodes);
	}

	/**
	 *
	 * @Functionality: Retrieves the exam IDs of a lecturer.
	 * @param lecID The ID of the lecturer for which the exam IDs are to be
	 *              retrieved.
	 * @return A server response message object containing an ArrayList with the
	 *         exam IDs of the lecturer.
	 */
	public Msg getExamsIDOfLecturer(String lecID) {
		String sql = "SELECT * FROM exams WHERE authorID = ?";
		ArrayList<String> examsID = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, lecID);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					String activationCode = resultSet.getString("examID");
					examsID.add(activationCode);
				
				}
			} catch (Exception e) {
				System.out.println("somthing Wrong happen by finding active exam");
			}
		} catch (SQLException e) {
			System.out.println("Sql exception active exam");
		}
		return new Msg("getExamsIDOfLecturer", MsgType.SERVER_RESPONSE, examsID);

	}

	/**
	 *
	 * @Functionality: Retrieves the course name and profession name of an exam
	 *                 based on its ID.
	 * @param ExamID The ID of the exam for which the course name and profession
	 *               name are to be retrieved.
	 * @return A server response message object containing an ArrayList with the
	 *         course name and profession name of the exam.
	 */
	public Msg getExamCourseNameAndProfessionName(String ExamID) {
		String sqlQuery = "SELECT c.courseName, p.professionName FROM courses c, exams e,professions p WHERE e.examID = ? AND e.courseID = c.courseID AND e.professionID = p.professionID";
		PreparedStatement statement;
		ArrayList<String> lst = new ArrayList<>();
		try {
			statement = conn.prepareStatement(sqlQuery);
			statement.setString(1, ExamID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String CourseName = resultSet.getString("c.courseName");// getcoursesname
				String professionName = resultSet.getString("p.professionName");
				lst.add(CourseName);
				lst.add(professionName);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Msg("getExamCourseNameAndProfessionName", MsgType.SERVER_RESPONSE, lst);

	}

	/**
	 *
	 * @Functionality: Retrieves the lecturer names and corresponding lecturer IDs.
	 * @return A server response message object containing a HashMap with lecturer
	 *         names as keys and corresponding lecturer IDs as values.
	 */
	public Msg getLecNameAndLecID() {
		String sqlQuery = "SELECT * FROM users WHERE role = 'Lecturer'";
		PreparedStatement statement;
		HashMap<String, String> nameIDMap = new HashMap<>();
		try {
			statement = conn.prepareStatement(sqlQuery);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String fullName = name + " " + lastName;
				String ID = resultSet.getString("userID");
				nameIDMap.put(fullName, ID);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return new Msg("getLecNameAndLecID", MsgType.SERVER_RESPONSE, nameIDMap);

	}

	/**
	 *
	 * @Functionality: Retrieves the course names and corresponding course IDs.
	 * @return A server response message object containing a HashMap with course
	 *         names as keys and corresponding course IDs as values.
	 */
	public Msg getCourseNameAndCourseID() {
		String sqlQuery = "SELECT * FROM courses";
		PreparedStatement statement;
		HashMap<String, String> nameIDMap = new HashMap<>();
		try {
			statement = conn.prepareStatement(sqlQuery);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString("courseName");
				String ID = resultSet.getString("courseID");
				nameIDMap.put(name, ID);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Msg("getCourseNameAndCourseID", MsgType.SERVER_RESPONSE, nameIDMap);

	}

	/**
	 * @Functionality: Retrieves the activated exams by course ID.
	 * @param courseID The course ID used to filter the activated exams.
	 * @return A server response message object containing a HashSet of activation
	 *         codes for the exams activated in the specified course.
	 */
	public Msg getExamsActivatedByCourse(String courseID) {

		String activationCodesQuery = "SELECT * FROM activated_exams WHERE activeStatus = 'inActive'";
		HashSet<String> hashSet = new HashSet<>();
		
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(activationCodesQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			// Process the results
			while (resultSet.next()) {
				String activationCode = resultSet.getString("activationCode");
				String examID = resultSet.getString("examID");
			
				String tempCourseID = examID.substring(2, 4);
				
				if (tempCourseID.equals(courseID)) {
					hashSet.add(activationCode);
				
				}
			}
		} catch (Exception e) {
			System.out.println("somthing Wrong happen by finding active exam");
		}
		return new Msg("getExamsActivatedByCourse", MsgType.SERVER_RESPONSE, hashSet);
	}

	/**
	 * @Functionality: Retrieves the exam ID by the activation code.
	 *
	 * @param activationCode The activation code used to retrieve the exam ID.
	 * @return A server response message object containing the exam ID.
	 */
	public Msg getExamIDByActivationCode(String activationCode) {

		String activationCodesQuery = "SELECT * FROM activated_exams WHERE activeStatus = 'inActive' AND activationCode= ?";
		String examID = null;
		HashSet<String> hashSet = new HashSet<>();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(activationCodesQuery);
			preparedStatement.setString(1, activationCode);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				examID = resultSet.getString("examID");
		
			}
		} catch (Exception e) {
			System.out.println("somthing Wrong happen by finding active exam");
		}
		return new Msg("getExamIDByActivationCode", MsgType.SERVER_RESPONSE, examID);
	}

	/**
	 * @Functionality: Retrieves the full name of a student based on their ID.
	 *
	 * @param: - ID (String): The unique ID of the student for which the name is to
	 *           be retrieved.
	 *
	 * @return: Returns a server response message object containing the full name of
	 *          the student.
	 */
	public Msg getStudentNameByStudentID(String ID) {
		String sqlQuery = "SELECT * FROM users WHERE userID = ?";
	
		String fullName = null;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
			preparedStatement.setString(1, ID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String name = resultSet.getString("firstName");
				String lastname = resultSet.getString("lastName");
				fullName = name + " " + lastname;
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Msg("getStudentNameByStudentID", MsgType.SERVER_RESPONSE, fullName);

	}

	/**
	 * Functionality: This method retrieves the unique IDs of students from the
	 * "users" table in the database where the role is set to "Student". It stores
	 * the IDs in a HashSet and returns a response message object containing the
	 * IDs.
	 *
	 * Input: None
	 *
	 * Output: Returns a server response message object containing the IDs of the
	 * students.
	 * 
	 * @return A Msg object representing the server response.
	 */
	public Msg getStudentsID() {
		String sqlQuery = "SELECT * FROM users WHERE role = 'Student'";
		PreparedStatement statement = null;
		HashSet<String> Ids = new HashSet<String>();
		String ID = null;
		try {
			statement = conn.prepareStatement(sqlQuery);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				ID = resultSet.getString("userID");
				Ids.add(ID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Msg("getStudentsID", MsgType.SERVER_RESPONSE, Ids);

	}

}
