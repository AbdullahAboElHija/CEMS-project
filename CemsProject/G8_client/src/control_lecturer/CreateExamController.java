package control_lecturer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import client.CEMSClient;
import client.ClientUI;
import common.MyFile;
import entity.Exam;
import entity.ManualExam;
import entity.OnlineExam;
import entity.Question;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.Msg;
import logic.MsgType;

public class CreateExamController {
	public static Exam exam;
	private static Exam chosenExam;
	public static ArrayList<Question> questions;
	public static  Exam examForPrev=null;
	
	
	public static void setChosenExam(Exam e) {
		chosenExam=e;
	}
	
	public static Exam getChosenExam() {
		return chosenExam;
	}
	
	public static void CreateExamID(Exam ex) {
		Msg CreateIDExam = new Msg("CreateExamID",MsgType.FROM_CLIENT,ex);
		ClientUI.chat.accept(CreateIDExam);
		exam = (Exam) CEMSClient.responseFromServer.getData();
	}
	
	//ManualExam
	
	public static void UploadManualExam(String LocalfilePath) {
		ManualExam mExam;
		try {
			//String LocalfilePath = pathFile.getText();
			if(!LocalfilePath.endsWith(".docx")) {
				System.out.println("You Must Upload Word Document !");
				return;
			}
			System.out.println("uploaded");
			File newFile = new File(LocalfilePath);
			String name = newFile.getName();
			MyFile msg = new MyFile(name);
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);
			bis.read(msg.getMybytearray(), 0, mybytearray.length);
			mExam = new ManualExam(msg,exam);
			Msg msg1 = new Msg("LecUploadFile", MsgType.FROM_CLIENT, mExam);
			ClientUI.chat.accept(msg1);
			//System.out.println(CEMSClient.responseFromServer.getMsg());
		} catch (Exception e) {
			System.out.println("Error send (Files)msg) to Server");
		}
	}
	
	
	public static void fillExamDetails() {
		Msg fillExamDetailsMsg = new Msg("fillExamDetailsCreatingStep",MsgType.FROM_CLIENT,exam);
		ClientUI.chat.accept(fillExamDetailsMsg);
		
	}
	
	//OnlineExam
	public static void LoadQuestionsByProfession() {
		Msg a = new Msg("LoadQuestionsByProfession",MsgType.FROM_CLIENT,exam.getProfessionID());
		ClientUI.chat.accept(a);
		
		try {
			if( CEMSClient.responseFromServer.getData().equals("Empty")) {
				throw new Exception("Recieved unexpected response msg from server (Question bank controller class)");
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("CAUGHT AN EXCEPTION");
		}
		questions = (ArrayList<Question>) CEMSClient.responseFromServer.getData();
	}
	
	/*
	 * functionality : finishing the creating process of the exam 
	 * by adding exam details in the exams table
	 * and add the questions of the exam to questions_in_exam table
	 * **/
	public static void createOnlineExam(ObservableList<Question> questionsInExam) {
		ArrayList<Question> questions = new ArrayList<>(questionsInExam);
		for (Question question : questions) {
			question.setUsedCounter(question.getUsedCounter()+1);
		}

		OnlineExam oExam = new OnlineExam(questions,exam);
		Msg createNewOnlineExam= new Msg("createNewOnlineExam",MsgType.FROM_CLIENT,oExam);
		ClientUI.chat.accept(createNewOnlineExam);
	
		
	}
	
	

	
}
