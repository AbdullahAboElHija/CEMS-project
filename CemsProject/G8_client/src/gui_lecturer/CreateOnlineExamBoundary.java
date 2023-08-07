package gui_lecturer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control_common.LoginController;
import control_lecturer.CreateExamController;
import entity.LoggedInUser;
import entity.Question;
import gui.GuiCommon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class CreateOnlineExamBoundary implements Initializable {
	
    @FXML
    private Label userName;
	
    @FXML
    private Button logoutBtn;
    
    @FXML
    private AnchorPane window;

	@FXML
	private TableView<Question> questionsInExamTable;

	@FXML
	private TableColumn<Question, String> questionIDinExam;

	@FXML
	private TableColumn<Question, String> AuthorNameinExam;

	@FXML
	private TableColumn<Question, String> questionTxtinExam;

	@FXML
	private TableColumn<Question, String> correctAnswerinExam;

	@FXML
	private TableColumn<Question, String> questionPoints;

	@FXML
	private TableView<Question> questionsBankTable;
	
	@FXML
	private TableColumn<Question, String> questionID;

	@FXML
	private TableColumn<Question, String> AuthorName;

	@FXML
	private TableColumn<Question, String> questionTxt;

	@FXML
	private TableColumn<Question, Integer> correctAnswer;
	
    @FXML
    private Label ErrMsg;
    
    @FXML
    private Label sumPointsLabel;
    
    @FXML
    private Button backBtn;
    
    @FXML
    private Button previewBtn;
    
	private ObservableList<Question> questionsInExam = FXCollections.observableArrayList();
	private int sum;
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    
    /**
     * Initializes the boundary class.
     *
     * @param location  The URL location of the FXML file.
     * @param resources The ResourceBundle for the FXML file.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//automatic window closing
		Platform.runLater(() -> {
			Stage stage = (Stage) window.getScene().getWindow();
	    		stage.setOnCloseRequest(event -> {
	    			event.consume(); // Consume the event to prevent automatic window closing
	    			LoginController.disconnect();
	    			stage.close();
	    		});
    	});
		
 		try {
 			userName.setText("Hi "+LoggedInUser.getInstance().getUser().getFirstName()+" "+LoggedInUser.getInstance().getUser().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		CreateExamController.LoadQuestionsByProfession();
		questionID.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		AuthorName.setCellValueFactory(new PropertyValueFactory<>("authorName"));
		questionTxt.setCellValueFactory(new PropertyValueFactory<>("question"));
		correctAnswer.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
		ObservableList<Question> questions = FXCollections.observableArrayList(CreateExamController.questions);
		questionsBankTable.setItems(questions);		questionPoints.setCellFactory(TextFieldTableCell.forTableColumn());
		questionIDinExam.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		AuthorNameinExam.setCellValueFactory(new PropertyValueFactory<>("authorName"));
		questionTxtinExam.setCellValueFactory(new PropertyValueFactory<>("question"));
		correctAnswerinExam.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
		questionPoints.setCellValueFactory(new PropertyValueFactory<>("questionPoints"));
		dragAndDropSetUp();
		sumPointsLabel.setText("0");
		
		 if(LoggedInUser.getHeadDep()!=null) {
    		 logoutBtn.setVisible(false);
    	 }
	}
	
	/**
     * Set up drag and drop functionality for questions in the exam table.
     */
	private void dragAndDropSetUp() {
		questionsInExamTable.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Question draggedPerson = questionsInExamTable.getItems().remove(draggedIndex);

                    int dropIndex ; 

                    if (row.isEmpty()) {
                        dropIndex = questionsInExamTable.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    questionsInExamTable.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    questionsInExamTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row ;
        });
	}
	
	/**
     * Add the selected question to the exam.
     *
     * @param event The ActionEvent triggered by clicking the "Add Question" button.
     */
	@FXML
	void addQuestionToExam(ActionEvent event) {
		ErrMsg.setText("");
		Question selectedQuestion = questionsBankTable.getSelectionModel().getSelectedItem();
		if(selectedQuestion==null) {
			ErrMsg.setText("Please Select Question First");
		}
		else if (!questionsInExam.contains(selectedQuestion)) {
			selectedQuestion.setQuestionPoints("0");
			questionsInExam.add(selectedQuestion);
			questionsInExamTable.setItems(questionsInExam);
		}
		else {
			ErrMsg.setText("Error");
		}
		
		
	}
	
	/**
     * Remove the selected question from the exam.
     *
     * @param event The ActionEvent triggered by clicking the "Remove Question" button.
     */
    @FXML
    void removeQuestionFromExam(ActionEvent event) {
		Question selectedQuestion = questionsInExamTable.getSelectionModel().getSelectedItem();
		questionsInExam.remove(selectedQuestion);
    }

    /**
     * Create the online exam with the selected questions and their respective points.
     *
     * @param event The ActionEvent triggered by clicking the "Create Exam" button.
     */
    @FXML
    void createExam(ActionEvent event) {
    	if(sumOfpoints()==100) {
    		int i = 1;
    		for(Question q:questionsInExam){
    			q.setSerialNumber(i);
    			i = i+1;
    		}
    		CreateExamController.fillExamDetails();
        	CreateExamController.createOnlineExam(questionsInExam);
        	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/lecturerMenu.fxml", "LecturerMenu", event,true);
        	GuiCommon.popUp("Online Exam successfully created");
    	}
    	//System.out.println(sumOfpoints());
    	ErrMsg.setText("Sum of question scores must be 100");
    }
    
    /**
     * Handle the cell edit event for the question points column.
     *
     * @param event The CellEditEvent triggered by editing the question points.
     */
    @FXML
    void onQuestionPointsEditCommit(TableColumn.CellEditEvent<Question, String> event) {
        // Get the edited value
    	ErrMsg.setText("");
        String newValue = event.getNewValue();
        // Get the question object associated with the edited cell
        Question question = event.getRowValue();
        // Update the corresponding property of the question object
		try {
			
			int points = Integer.parseInt(newValue);
			if(((sumOfpoints()+points)>100) || points>100) {
				ErrMsg.setText("Your sum of points is more than 100.");
				question.setQuestionPoints("0");
				questionsInExamTable.refresh();
			}else {
				question.setQuestionPoints(newValue);
				sumPointsLabel.setText(String.format("%d",sumOfpoints()));
				}
		} catch (NumberFormatException e) {
		    ErrMsg.setText("The parameter is not a valid integer.");
			question.setQuestionPoints("0");
			questionsInExamTable.refresh();
		}
        
    }
    
    /**
     * Calculate the sum of points for all questions in the exam.
     *
     * @return The sum of points.
     */
    private int sumOfpoints() {
        int sum = 0;
        if(!questionsInExam.isEmpty()) {
	        for(Question q:questionsInExam) {
				try {
				    int intValue = Integer.parseInt(q.getQuestionPoints());
				    sum = sum + intValue;
				} catch (NumberFormatException e) {
				}
	        }
        }
		return sum;
    }
    
    /**
     * Navigate back to the previous screen.
     *
     * @param event The ActionEvent triggered by clicking the "Back" button.
     */
    @FXML
    void clickBack(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/ExamsBank.fxml", "Exams Bank", event,true);
    }
    
    /**
     * Log out the user and navigate to the login screen.
     *
     * @param event The ActionEvent triggered by clicking the "Logout" button.
     */
    @FXML
    void clickLogout(ActionEvent event) {
    	LoginController.logOut();
    	GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event,true);
    }
    
    @FXML
    void clickPreview(ActionEvent event) {
    	CreateExamController.questions= new ArrayList<>(questionsInExam);
    	GuiCommon.getInstance().displayNextScreen("/gui_lecturer/PreviewOnlineExam.fxml", "Preview Online Exam", null,false);
    }
}
