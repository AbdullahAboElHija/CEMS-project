package gui_headDep;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import client.CEMSClient;
import client.ClientUI;
import common.ReportController;
import control_common.LoginController;
import entity.LoggedInUser;
import entity.StudentExam;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import logic.Msg;
import logic.MsgType;

public class GetExamReportBoundary implements Initializable {

	@FXML
	private Label ErrMsg;

	@FXML
	private AnchorPane window;

	@FXML
	private Label courseNameLabel;
	@FXML
	private Label courseNameStaticLabel;
	@FXML
	private Label professionLabel;
	@FXML
	private Label profNameStaticLabel;

	@FXML
	private Label avgLabel;

	@FXML
	private Label medianLabel;
	
    @FXML
    private Label numOfSubmits;

	private List<String> gradeRanges = Arrays.asList("0-55", "56-65", "66-75", "76-85", "86-95", "95-100");

	@FXML
	private BarChart<String, Number> barChart;

	@FXML
	private CategoryAxis amount;

	@FXML
	private NumberAxis grades;
	@FXML
	private Button logoutBtn;

	@FXML
	private Label userName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (ReportController.headFromStudent) {
			makeReportToStudent();
		} else {
			String examActivationCode = ReportController.ActivationCodeUsinginHDReportBoundary;
			String examIDCode = ReportController.examIDCodeUsinginHDReportBoundary;
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.getData().clear();
			barChart.getData().clear();
			Msg GradeAnalyzerMsg = new Msg("getGradeAnalyzer", MsgType.FROM_CLIENT, examActivationCode);
			ClientUI.chat.accept(GradeAnalyzerMsg);
			Msg response = CEMSClient.responseFromServer;
			if (response.getMsg().equals("getGradeAnalyzer")) {
				ArrayList<Number> countList = (ArrayList<Number>) response.getData();
				if (sumOfArray(countList) != 0) {
					Msg getAvgMedian = new Msg("returnAvgAndMedian", MsgType.FROM_CLIENT, examActivationCode);
					ClientUI.chat.accept(getAvgMedian);
					response = CEMSClient.responseFromServer;
					if (response.getMsg().equals("returnAvgAndMedian")) {
						ArrayList<Number> AvgMedian = (ArrayList<Number>) response.getData();
						avgLabel.setText(AvgMedian.get(0).toString());
						medianLabel.setText(AvgMedian.get(1).toString());
						series.setName("num of Students");
						Integer sum = 0;
						for (int i = 0; i < 6; i++) {
							series.getData().add(new XYChart.Data(gradeRanges.get(i), countList.get(i)));
							sum = sum + (Integer)countList.get(i);
						}
						numOfSubmits.setText(sum.toString());
						barChart.getData().add(series);
						ErrMsg.setText("");
					}
				} else {
					ErrMsg.setText("There is no grades of students in this exam!");
					numOfSubmits.setText("NULL");
					avgLabel.setText("NULL");
					medianLabel.setText("NULL");
				}
				Msg getExamCourseNameAndProfessionNameMsg = new Msg("getExamCourseNameAndProfessionName",
						MsgType.FROM_CLIENT, examIDCode);
				ClientUI.chat.accept(getExamCourseNameAndProfessionNameMsg);
				response = CEMSClient.responseFromServer;
				if (response.getMsg().equals("getExamCourseNameAndProfessionName")) {
					ArrayList<String> cnamePname = (ArrayList<String>) response.getData();
					if (cnamePname.size() != 0) {
						courseNameLabel.setText(cnamePname.get(0));
						professionLabel.setText(cnamePname.get(1));
					}
				}
				ReportController.ActivationCodeUsinginHDReportBoundary = null;
				ReportController.examIDCodeUsinginHDReportBoundary = null;

			}

		}

		Platform.runLater(() -> {
			Stage stage = (Stage) window.getScene().getWindow();
			stage.setOnCloseRequest(event -> {
				event.consume(); // Consume the event to prevent automatic window closing
				LoginController.setWithReq(true);
				LoginController.disconnect();
				stage.close();
			});
		});
		
		try {
			userName.setText("Hi " + LoggedInUser.getHeadDep().getFirstName() + " "
					+ LoggedInUser.getHeadDep().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	/**
	 * Generates a report for all student exams.
	 *
	 * This function retrieves all exam copies of a specific student from the server and generates a report based on the grades.
	 * It sends a message to the server to get all the exam copies of the student identified by the StudentID.
	 */

	public void makeReportToStudent() {
		String StudentID = ReportController.StudentIDinHDView;
		Msg msg = new Msg("getAllExamCopiesOfStudent", MsgType.FROM_CLIENT, StudentID);
		ClientUI.chat.accept(msg);
		Msg response = CEMSClient.responseFromServer;
		ArrayList<Integer> grades = new ArrayList<>();
		if (response.getMsg().equals("getAllExamCopiesOfStudent")) {
			ArrayList<StudentExam> allCopies = (ArrayList<StudentExam>) response.getData();
			int count0To55 = 0;
			int count56To65 = 0;
			int count66To75 = 0;
			int count76To85 = 0;
			int count86To95 = 0;
			int count95To100 = 0;
			for (StudentExam e : allCopies) {
				if (e.isAvailableForVieweing()) {
					int grade = e.getGrade();
					grades.add(grade);
					// Increment the respective count variable based on the grade range
					if (grade >= 0 && grade <= 55) {
						count0To55++;
					} else if (grade >= 56 && grade <= 65) {
						count56To65++;
					} else if (grade >= 66 && grade <= 75) {
						count66To75++;
					} else if (grade >= 76 && grade <= 85) {
						count76To85++;
					} else if (grade >= 86 && grade <= 95) {
						count86To95++;
					} else if (grade >= 96 && grade <= 100) {
						count95To100++;
					}
				}
			}
			if (grades.size() != 0) {
				List<Number> countList = new ArrayList<>();
				countList.add(count0To55);
				countList.add(count56To65);
				countList.add(count66To75);
				countList.add(count76To85);
				countList.add(count86To95);
				countList.add(count95To100);
				Float avg = avg(grades);
				Integer median = median(grades);
				avgLabel.setText(avg.toString());
				medianLabel.setText(median.toString());
				// get Student Name
				Msg getStudentNameMsg = new Msg("getStudentNameByStudentID", MsgType.FROM_CLIENT, StudentID);
				ClientUI.chat.accept(getStudentNameMsg);
				response = CEMSClient.responseFromServer;
				String StudentName = null;
				if (response.getMsg().equals("getStudentNameByStudentID")) {
					//System.out.println((String) response.getData());
					StudentName = (String) response.getData();
				}
				Integer submits= grades.size();
				numOfSubmits.setText(submits.toString());
				// end Set Student Name
				profNameStaticLabel.setText("Student name: ");
				courseNameStaticLabel.setText("Student ID: ");
				courseNameLabel.setText(StudentID);
				professionLabel.setText(StudentName);
				//
				XYChart.Series<String, Number> series = new XYChart.Series<>();
				series.getData().clear();
				barChart.getData().clear();
				series.setName(StudentName + " Exams Distributions");
				for (int i = 0; i < 6; i++) {
					series.getData().add(new XYChart.Data(gradeRanges.get(i), countList.get(i)));
				}
				barChart.getData().add(series);
				ErrMsg.setText("");
			}

		}

		ReportController.StudentIDinHDView = null;

	}

	public int sumOfArray(ArrayList<Number> countList) {
		int sum = 0;
		for (Number number : countList) {
			sum += number.doubleValue();
		}
		return sum;

	}

	public float avg(ArrayList<Integer> grades) {
		int sum = 0;
		for (Integer number : grades) {
			sum += number;
		}
		return sum / grades.size();

	}

	public int median(ArrayList<Integer> grades) {
		Collections.sort(grades);
		int mid = grades.size() / 2;
		int median = (int) grades.get(mid);
		return median;
	}

	
	
	/**
	 * Navigates back to the previous screen.
	 *
	 * This function is triggered when the user clicks the "Back" button.
	 * It checks the value of the `headFromLec`, `headFromCourse`, and `headFromStudent` variables in the ReportController.
	 * Based on the value of these variables, it navigates to the corresponding previous screen.
	 *
	 * @param event The action event generated by the "Back" button click.
	 */
	@FXML
	void back(ActionEvent event) {
		if (ReportController.headFromLec) {
			GuiCommon.getInstance().displayNextScreen("/gui_headDep/ChooseReportExam.fxml", "Choose Report", event,
					true);
			ReportController.headFromLec = false;
		} else if (ReportController.headFromCourse) {
			GuiCommon.getInstance().displayNextScreen("/gui_headDep/ChooseCourseReport.fxml", "ChooseCourseReport",
					event, true);
			ReportController.headFromCourse = false;
		} else if (ReportController.headFromStudent) {
			GuiCommon.getInstance().displayNextScreen("/gui_headDep/ChooseStudentReport.fxml", "ChooseStudentReport",
					event, true);
			ReportController.headFromStudent = false;
		}

	}
	
	@FXML
    void clickLogout(ActionEvent event) {
    	if( LoginController.logOutHeadDep()) {
			GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
		}
	}

}
