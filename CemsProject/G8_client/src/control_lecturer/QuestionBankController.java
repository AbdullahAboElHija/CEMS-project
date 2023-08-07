package control_lecturer;

import java.util.ArrayList;
import java.util.Iterator;

import client.CEMSClient;
import client.ClientUI;
import entity.Course;
import entity.Lecturer;
import entity.LoggedInUser;
import entity.Profession;
import entity.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import logic.Msg;
import logic.MsgType;

public class QuestionBankController {

		private static Question chosenQuestion;
		
		public static void setChosenQuestion(Question q) {
			chosenQuestion=q;
		}
		
		public static Question getChosenQuestion() {
			return chosenQuestion;
		}
		
		/**
		 * fills up param questionsByLec with all questions written by current logged in lecturer
		 * @param questionsByLec
		 */
		public static ArrayList<Question> loadQuestionsByLec() {
			Lecturer lec=null;
			try {
				 lec= (Lecturer)LoggedInUser.getInstance().getUser();		
			} catch (Exception e) {
				e.printStackTrace();
			}
			Msg a = new Msg("getLecturerQuestions",MsgType.FROM_CLIENT,lec);
			ClientUI.chat.accept(a);
			try {
				if(! CEMSClient.responseFromServer.getMsg().equals("getLecturerQuestions")) {
					throw new Exception("Recieved unexpected response msg from server (Question bank controller class)");
				}
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("CAUGHT AN EXCEPTION");
			}
			return (ArrayList<Question>) CEMSClient.responseFromServer.getData();
		}
		
		

		
		
}
