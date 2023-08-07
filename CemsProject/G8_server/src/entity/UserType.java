package entity;

import java.io.Serializable;

public enum UserType   implements Serializable{
	HeadDep, Lecturer, Student,Invalid;
	public static UserType getType(String str) {
		UserType type=Invalid;
		switch(str) {
		case "Lecturer" :
			type=Lecturer;
			break;
		case "Student":
			type=Student;
			break;
		case "HeadDep":
			type=HeadDep;
			break;
		}
		return type;
	}
}
