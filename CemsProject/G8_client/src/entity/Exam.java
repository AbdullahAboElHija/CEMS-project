package entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Exam implements Serializable{
	@Override
	public String toString() {
		return "Exam [ExamID=" + ExamID + ", examType=" + examType + ", examQuestions=" + examQuestions
				+ ", activationCode=" + activationCode + "]";
	}


	private String ExamID,authorID,professionID,courseID,examType,lecturerNotes,studentNotes,activatedCounter;
	private Course course;
	private Integer duration;
	private ArrayList<Question> examQuestions= new ArrayList<Question>();
	
	private String activationCode;
	
	public Exam(String examID, String authorID, String examType, String lecturerNotes, String studentNotes,
			String activatedCounter, Course course, Integer duration) {
		super();
		ExamID = examID;
		this.authorID = authorID;
		this.examType = examType;
		this.lecturerNotes = lecturerNotes;
		this.studentNotes = studentNotes;
		this.activatedCounter = activatedCounter;
		this.course = course;
		this.duration = duration;
	}
	
	
	public Exam(String examID, int duration, String authorID, String courseID, String examType, String lecturerNotes,
			String studentNotes, String professionID,String activatedCounter) {
		super();
		ExamID = examID;
		this.duration = duration;
		this.authorID = authorID;
		this.courseID = courseID;
		this.examType = examType;
		this.lecturerNotes = lecturerNotes;
		this.studentNotes = studentNotes;
		this.professionID = professionID;
		this.activatedCounter = activatedCounter;
	}
	public Exam() {
		
	}


	public String getProfessionID() {
		if(professionID!=null)
			return professionID;
		else
			return course.getProfessionID();
	}

	public void setProfessionID(String professionID) {
		this.professionID = professionID;
	}
	public String getExamID() {
		return ExamID;
	}
	public void setExamID(String examID) {
		ExamID = examID;
	}
	public String getAuthorID() {
		return authorID;
	}
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	public String getCourseID() {
		if(courseID!=null)
			return courseID;
		else
			return course.getCourseID();
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getLecturerNotes() {
		return lecturerNotes;
	}
	public void setLecturerNotes(String lecturerNotes) {
		this.lecturerNotes = lecturerNotes;
	}
	public String getStudentNotes() {
		return studentNotes;
	}
	public void setStudentNotes(String studentNotes) {
		this.studentNotes = studentNotes;
	}
	public String getActivatedCounter() {
		return activatedCounter;
	}
	public void setActivatedCounter(String activatedCounter) {
		this.activatedCounter = activatedCounter;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getCourseName() {
		return course.getCourseName();
	}
	public String getProfessionName() {
		return course.getProfession().getProfessionName();
	}
	
	public ArrayList<Question> getExamQuestions(){
		return examQuestions;
	}
	public void setExamQuestions(ArrayList<Question> questions){
		examQuestions= questions;
	}


	public void setActivationCode(String codeString) {
		activationCode=codeString;
		
	}


	public String getActivationCode() {
		
		return activationCode;
	}


	public void setCourse(Course course2) {
		course = course2;
		
	}

	
}
