package entity;

import java.io.Serializable;

import common.MyFile;

public class ManualExam implements Serializable {

	//private Student
	private String studentID;
	private String examCode;
	private String pathToDownload;
	MyFile examFile;
	private Exam exam;
	
	public ManualExam(MyFile examFile, Exam exam) {
		super();
		this.examFile = examFile;
		this.exam = exam;
	}



	public Exam getExam() {
		return exam;
	}



	public void setExam(Exam exam) {
		this.exam = exam;
	}



	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}



	public ManualExam(String studentID, String examCode, MyFile examFile) {
		this.studentID = studentID;
		this.examCode = examCode;
		this.examFile = examFile;
	}



	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public MyFile getExamFile() {
		return examFile;
	}

	public void setExamFile(MyFile examFile) {
		this.examFile = examFile;
	}

	public String getExamCode() {
		return examCode;
	}
	
	public String getPathToDownload() {
		return pathToDownload;
	}

	public void setPathToDownload(String pathToDownload) {
		this.pathToDownload = pathToDownload;
	}


	public void setExamCode(String examCode,String pathToDownload) {
		this.examCode = examCode;

		}

	public ManualExam(String examCode, String pathToDownload) {
		super();
		this.examCode = examCode;
		this.pathToDownload = pathToDownload;
	}
}
