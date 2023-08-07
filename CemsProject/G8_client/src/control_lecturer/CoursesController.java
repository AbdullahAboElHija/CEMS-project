package control_lecturer;

import java.util.ArrayList;
import java.util.Iterator;

import entity.Course;
import entity.Lecturer;
import entity.LoggedInUser;
import entity.Profession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class CoursesController {
	
	public static void fillCourseComboByProfession(ComboBox<Course> courseCombo,
			ObservableList<Course> courses , Profession prof) {

		Lecturer lec;
		try {
			lec = (Lecturer) LoggedInUser.getInstance().getUser();
			ArrayList<Course> crses = new ArrayList<>(lec.getLecturerCourses());
			ArrayList<Course> tempCourses = new ArrayList<>();
			Iterator<Course> iterator = crses.iterator();
			while (iterator.hasNext()) {
				Course c = iterator.next();
				
	            if (c.getProfessionID().equals(prof.getProfessionID())) {
	            	tempCourses.add(c);
	            }
	        }
			courses = FXCollections.observableArrayList(tempCourses);
			courseCombo.setItems(courses);
			courseCombo.setConverter(new StringConverter<Course>() {
				@Override
				public String toString(Course c) {
					return c.getCourseName();
				}

				@Override
				public Course fromString(String string) {
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
