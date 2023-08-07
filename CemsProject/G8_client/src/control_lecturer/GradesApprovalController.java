package control_lecturer;

import entity.Question;
import entity.StudentExam;

/**
 * The controller class for managing grades approval.
 */
public class GradesApprovalController {
	
	/**
     * The currently chosen student exam.
     */
	private static StudentExam chosenStudentExam;
	
	 /**
     * Retrieves the currently chosen student exam.
     *
     * @return The currently chosen student exam.
     */
	public static StudentExam getChosenStudentExam() {
		return chosenStudentExam;
	}

	/**
     * Sets the currently chosen student exam.
     *
     * @param chosenStudentExam The student exam to be set as the currently chosen one.
     */
	public static void setChosenStudentExam(StudentExam chosenStudentExam) {
		GradesApprovalController.chosenStudentExam = chosenStudentExam;
	}
	
	
}
