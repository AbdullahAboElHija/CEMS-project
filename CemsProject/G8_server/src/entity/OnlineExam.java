package entity;

import java.io.Serializable;
import java.util.ArrayList;


public class OnlineExam implements Serializable{
	private ArrayList<Question> questionsInExam;
	private Exam exam;
	
	
	
	public OnlineExam(ArrayList<Question> questionsInExam, Exam exam) {
		super();
		this.questionsInExam = questionsInExam;
		this.exam = exam;
	}
	public ArrayList<Question> getQuestionsInExam() {
		return questionsInExam;
	}
	public void setQuestionsInExam(ArrayList<Question> questionsInExam) {
		this.questionsInExam = questionsInExam;
	}
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam) {
		this.exam = exam;
	}
	
}
