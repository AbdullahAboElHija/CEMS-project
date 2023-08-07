package entity;

import java.io.Serializable;

public class Course implements Serializable {
	private String courseID;
	private String courseName;
	private String professionID;
	private Profession prof;
	
	public Course(String courseID, String courseName, Profession prof) {
		super();
		this.courseID = courseID;
		this.courseName = courseName;
		this.prof = prof;
	}
	public Course(String courseID, String courseName, String professionID) {
		super();
		this.courseID = courseID;
		this.courseName = courseName;
		this.professionID = professionID;
	}
	public String getCourseID() {
		return courseID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getProfessionID() {
		if(professionID!=null)
			return professionID;
		else
			return prof.getProfessionID();
	}
	public void setProfessionID(String professionID) {
		this.professionID = professionID;
	}
	public Profession getProfession() {
		return prof;
	}
	
}
