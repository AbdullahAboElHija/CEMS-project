package gui_student;

import java.util.ArrayList;

import client.CEMSClient;
import client.ClientUI;
import entity.Exam;
import entity.LoggedInUser;
import entity.Question;
import entity.StudentExam;
import entity.User;
import logic.Msg;
import logic.MsgType;

public class PerformExamController {
	
	private static Exam performExam;
	
	/**
	 * Retrieves the currently performed exam.
	 *
	 * @return The Exam object representing the currently performed exam.
	 */
	public static Exam getPerformExam() {
		return performExam;
	}

	/**
	 * Sets the currently performed exam.
	 *
	 * @param exam The Exam object representing the currently performed exam.
	 */
	public static void setPerformExam(Exam exam) {
		performExam = exam;
	}
	
	/**
	 * Grades the student's exam based on the provided answers.
	 *
	 * @param exam The StudentExam object representing the student's exam.
	 * 
	 *
	 * This method calculates the grade for the student's exam by comparing the student's answers with the correct answers
	 * for each question. It updates the correctAns field for each question and sets the grade for the student's exam.
	 */
	public static void gradeExam(StudentExam exam) {
		ArrayList<Question> questions = exam.getExamQuestions();
		Integer grade=0;
		for (Question q: questions) {
			
			if(q.getStudentAnswer().equals(q.getCorrectAnswer())) {
			
				q.setCorrectAns(true);
				grade+= Integer.parseInt(q.getQuestionPoints());
			}
		}
		exam.setGrade(grade);	
	
	}
	
	/**
	 * Checks if the student has already performed the exam with the given activation code.
	 *
	 * @param codeString The activation code for the exam.
	 * @return True if the student has already performed the exam, false otherwise.
	*/
	public static boolean hasStudentPerformedExam(String codeString) {
		
		StudentExam studentExam = new StudentExam();
		studentExam.setActivationCode(codeString);
		
		User user = null;
		try {
			user = LoggedInUser.getInstance().getUser();
			studentExam.setStudentId(user.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Msg msg= new Msg("hasStudentPerformed",MsgType.FROM_CLIENT,studentExam);
		ClientUI.chat.accept(msg);
		Msg response = CEMSClient.responseFromServer;
		try {
			if(!response.getMsg().equals("hasStudentPerformed")) {
				throw new Exception("Unexpected message type");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String hasPerformed = (String)response.getData();
		if(hasPerformed.equals("false")) return false;
		return true;
	}
	/**
	 * Retrieves the details of a manual exam based on the provided activation code and exam ID.
	 *
	 * @param codeString The activation code for the exam.
	 * @param examID The ID of the exam.
	 * 
	 */
	public static void getManualDetails(String codeString, String examID) {
		Msg msg1 = new Msg("getManualExamDetails",MsgType.FROM_CLIENT,examID);
		ClientUI.chat.accept(msg1);
		
		Msg response = CEMSClient.responseFromServer;
		try {
			if(!response.getMsg().equals("getManualExamDetails")) {
				throw new Exception("Unexpected message type");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		setPerformExam((Exam)response.getData());
		getPerformExam().setActivationCode(codeString);
	}
	
	/**
	 * Retrieves the details of an online exam based on the provided activation code and exam ID.
	 *
	 * @param codeString The activation code for the exam.
	 * @param examID The ID of the exam.
	 * 
	 */
	public static void getOnlineDetails(String codeString, String examID) {
		Msg msg1 = new Msg("getOnlineExamDetails",MsgType.FROM_CLIENT,examID);
		ClientUI.chat.accept(msg1);
		
		Msg response = CEMSClient.responseFromServer;
		try {
			if(!response.getMsg().equals("getOnlineExamDetails")) {
				throw new Exception("Unexpected message type");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		setPerformExam((Exam)response.getData());
		getPerformExam().setActivationCode(codeString);
	}
}
