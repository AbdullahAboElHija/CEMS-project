package entity;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentExam implements Serializable {
	@Override
	public String toString() {
		return "StudentExam [exam=" + exam + ", studentId=" + studentId + ", lecturerNotes=" + lecturerNotes
				+ ", reasonOfGradeChange=" + reasonOfGradeChange + ", gradeChanged=" + gradeChanged + ", submitted="
				+ submitted + ", availableForViewing=" + availableForViewing + ", grade=" + grade + ", status=" + status
				+ ", solvingTime=" + solvingTime + ", activeCode=" + activeCode + "]";
	}

	private Exam exam;
	
	//private String examId, activationCode,
	private String studentId;
	//private ArrayList<Question> examQuestions;
	private String lecturerNotes;
	private String reasonOfGradeChange;
	private boolean gradeChanged= false;
	private boolean submitted =false;
	private boolean availableForViewing= false;
	private Integer grade=0;
	private String status;
	private Integer solvingTime;//solving time in minutes
	private String activeCode;
	
	
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam) {
		this.exam = exam;
	}
		public String getExamId() {
		return exam.getExamID();
	}
	public void setExamId(String examId) {
		exam.setExamID(examId);
	}
	
	public String getActivationCode() {
		if(exam!=null)
			if( exam.getActivationCode() !=null)
				return exam.getActivationCode();
			return activeCode;
	}
	public void setActivationCode(String activationCode) {
		if(exam!=null)
			exam.setActivationCode(activationCode);
		activeCode=activationCode;
		
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public ArrayList<Question> getExamQuestions() {
		return exam.getExamQuestions();
	}
	public void setExamQuestions(ArrayList<Question> questionsInExam) {
		exam.setExamQuestions(questionsInExam);
	}
	public String getLecturerNotes() {
		return lecturerNotes;
	}
	public void setLecturerNotes(String lecturerNotes) {
		this.lecturerNotes = lecturerNotes;
	}
	public String getReasonOfGradeChange() {
		return reasonOfGradeChange;
	}
	public void setReasonOfGradeChange(String reasonOfGradeChange) {
		this.reasonOfGradeChange = reasonOfGradeChange;
	}
	public boolean isGradeChanged() {
		return gradeChanged;
	}
	public void setGradeChanged(boolean gradeChanged) {
		this.gradeChanged = gradeChanged;
	}
	public boolean isSubmitted() {
		return submitted;
	}
	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}
	public boolean isAvailableForVieweing() {
		return availableForViewing;
	}
	public void setAvailableForVieweing(boolean availableForVieweing) {
		this.availableForViewing = availableForVieweing;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getSolvingTime() {
		return solvingTime;
	}
	public void setSolvingTime(Integer solvingTime) {
		this.solvingTime = solvingTime;
	}
	
	public String getApprovalStatus() {
		return availableForViewing ? "approved" :"pending";
	}
	
	public String getCourseName() {
		return exam.getCourseName();
	}
	
	public String getGradeChange() {
		if(gradeChanged) {
			return "Yes";
		}
		else
			return "No";
	}
		
}
