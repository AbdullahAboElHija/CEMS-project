package entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ActivatedExam implements Serializable{
	private String activationCode;
	private Exam exam; //ExamID + authorID
	private String examID;

	private String activatedLecturerID;
	private String timeRequestStatus;
	private int actualDuration;
	private String activeStatus;
	private int studentsTaken;
	private int studentsSubmitted;
	private String courseName;
	private String ExamType;
	private int timeRequested;
	private String reasonTimeReq;







	public ActivatedExam(String activationCode, Exam exam) {
		super();
		this.activationCode = activationCode;
		this.exam = exam;
		this.examID = exam.getExamID();
		this.actualDuration = exam.getDuration();
	}
	
	
	public ActivatedExam(String activationCode, String examID, String timeRequestStatus, int actualDuration,
			String activeStatus, String activatedLecturerID, int studentsTaken, int studentsSubmitted) {
		super();
		this.activationCode = activationCode;
		this.examID = examID;
		this.timeRequestStatus = timeRequestStatus;
		this.actualDuration = actualDuration;
		this.activeStatus = activeStatus;
		this.activatedLecturerID = activatedLecturerID;
		this.studentsTaken = studentsTaken;
		this.studentsSubmitted = studentsSubmitted;
	}
	
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam) {
		this.exam = exam;
	}


	public String getExamID() {
		return examID;
	}


	public void setExamID(String examID) {
		this.examID = examID;
	}


	public String getActivatedLecturerID() {
		return activatedLecturerID;
	}


	public void setActivatedLecturerID(String activatedLecturerID) {
		this.activatedLecturerID = activatedLecturerID;
	}


	public String getTimeRequestStatus() {
		return timeRequestStatus;
	}


	public void setTimeRequestStatus(String timeRequestStatus) {
		this.timeRequestStatus = timeRequestStatus;
	}


	public int getActualDuration() {
		return actualDuration;
	}


	public void setActualDuration(int actualDuration) {
		this.actualDuration = actualDuration;
	}


	public String getActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}


	public int getStudentsTaken() {
		return studentsTaken;
	}


	public void setStudentsTaken(int studentsTaken) {
		this.studentsTaken = studentsTaken;
	}


	public int getStudentsSubmitted() {
		return studentsSubmitted;
	}


	public void setStudentsSubmitted(int studentsSubmitted) {
		this.studentsSubmitted = studentsSubmitted;
	}
	
	public String getExamType() {
		return ExamType;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public void setExamType(String examType) {
		ExamType = examType;
	}

	public int getTimeRequested() {
		return timeRequested;
	}


	public void setTimeRequested(int timeRequested) {
		this.timeRequested = timeRequested;
	}
	
	public String getReasonTimeReq() {
		return reasonTimeReq;
	}


	public void setReasonTimeReq(String reasonTimeReq) {
		this.reasonTimeReq = reasonTimeReq;
	}


}
