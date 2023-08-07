package gui_lecturer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import client.CEMSClient;
import client.ClientUI;
import common.ReportController;
import control_common.LoginController;
import entity.LoggedInUser;
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
	private Label professionLabel;

	@FXML
	private Label avgLabel;

	@FXML
	private Label medianLabel;

	private List<String> gradeRanges = Arrays.asList("0-55", "56-65", "66-75", "76-85", "86-95", "95-100");

	@FXML
	private BarChart<String, Number> barChart;

	@FXML
	private CategoryAxis amount;

	@FXML
	private NumberAxis grades;

    @FXML
    private Label numOfSubmits;
    
	@FXML
	private Button logoutBtn;


	@FXML
	private Label userName;

	private int counter = 0;

	
	/**
     * Initializes the controller. This method is automatically called
     * after the FXML file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String examActivationCode = ReportController.ActivationCodeUsinginLecReportBoundary;
		String examIDCode = ReportController.examIDCodeUsinginLecReportBoundary;
		ReportController.examIDCodeUsinginLecReportBoundary = null;
		ReportController.ActivationCodeUsinginLecReportBoundary = null;
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.getData().clear();
		barChart.getData().clear();

		// String examActivationCode = "1110";
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
				//System.out.println(cnamePname);
				if (cnamePname.size() != 0) {
					courseNameLabel.setText(cnamePname.get(0));
					professionLabel.setText(cnamePname.get(1));
				}
			}

		}

		//handles closing window with X button
		Platform.runLater(() -> {
			Stage stage = (Stage) window.getScene().getWindow();
			stage.setOnCloseRequest(event -> {
				event.consume(); // Consume the event to prevent automatic window closing
				LoginController.disconnect();
				stage.close();
			});
		});

		try {
			userName.setText("Hi " + LoggedInUser.getInstance().getUser().getFirstName() + " "
					+ LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setDisable(true);

    	 }
	}





	/**
     * Calculates the sum of numbers in the given countList.
     *
     * @param countList The list of numbers.
     * @return The sum of numbers in the list.
     */

	public int sumOfArray(ArrayList<Number> countList) {
		int sum = 0;
		for (Number number : countList) {
			sum += number.doubleValue();
		}
		return sum;

	}

	/**
     * Handles the action of the back button.
     *
     * @param event The ActionEvent triggered by clicking the button.
     */
	@FXML
	void back(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ChooseReport.fxml", "Choose Report", event, true);

	}

	@FXML
	void clickLogout(ActionEvent event) {
		LoginController.logOut();
		GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
	}

}
