package entity;

import java.util.ArrayList;

public class Lecturer extends User {
	private ArrayList<Profession> lecturerProfessions=new ArrayList<>();
	private ArrayList<Course> lecturerCourses=new ArrayList<>();

	public void setLecturerCourses(ArrayList<Course> lecturerCourses) {
		this.lecturerCourses = lecturerCourses;
	}
	/* constructor */	
	public Lecturer(String id, String username, String password, String firstName, String lastName, String email,
			UserType userType, Integer status) {
		super(id, username, password, firstName, lastName, email, userType, status);
	}
	public Lecturer(User user) {
		this(user.getId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getUserType(),user.getStatus());
	}
	
	/*get ArrayList of Profession belong to teacher*/
	public ArrayList<Profession> getProfessions() {
		return lecturerProfessions;
	}
	/*get ArrayList of Profession belong to teacher*/
	public void setProfessions(ArrayList<Profession> professions) {
		this.lecturerProfessions = professions;
	}
	public ArrayList<Course> getLecturerCourses() {
		return lecturerCourses;
	}
}
