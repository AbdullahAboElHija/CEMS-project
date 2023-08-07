package entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Question implements Serializable{
	private String questionID;
	private Profession profession;
	private String authorID;
	private User author;
	private String question;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private Integer correctAnswer;
	private String questionInstructions;
	private int usedCounter;

	private String questionPoints;
	private Integer serialNumber;
	private Integer studentAnswer=-1;
	private boolean correctAns=false;

	public boolean isCorrectAns() {
		return correctAns;
	}

	public void setCorrectAns(boolean correctAns) {
		this.correctAns = correctAns;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		return Objects.equals(questionID, other.questionID);
	}

	//Used Online Exam Creation 
	public Question(String questionID, Profession profession, String authorID, String question, String answer1,
			String answer2, String answer3, String answer4, Integer correctAnswer, String questionInstructions,
			Integer usedCounter) {
		super();
		this.questionID = questionID;
		this.profession = profession;
		this.authorID = authorID;
		this.question = question;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
		this.questionInstructions = questionInstructions;
		this.usedCounter = usedCounter;
	}
	
	public Question(String questionID, String professionID,String professionName, User author, String question, String answer1,
			String answer2, String answer3, String answer4, Integer correctAnswer, String questionInstructions,Integer usedCounter) {
		super();
		this.questionID = questionID;
		this.profession = new Profession(professionID,professionName);
		this.author = author;
		this.question = question;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
		this.questionInstructions = questionInstructions;
		this.usedCounter=usedCounter;
	}
	

	
	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Question(String questionID, String professionID,String professionName, String authorID, String question, String answer1,
			String answer2, String answer3, String answer4, Integer correctAnswer, String questionInstructions,Integer usedCounter) {
		super();
		this.questionID = questionID;
		this.profession = new Profession(professionID,professionName);
		this.authorID = authorID;
		this.question = question;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctAnswer = correctAnswer;
		this.questionInstructions = questionInstructions;
		this.usedCounter=usedCounter;
	}
	
	public Question(String questionID,  String professionID,String professionName, String authorID, String question,String questionInstructions) {
		super();
		this.questionID = questionID;
		this.profession = new Profession(professionID,professionName);
		this.authorID = authorID;
		this.question = question;
		this.questionInstructions = questionInstructions;
	}
	
	public Question() {
	}



	public String getQuestionID() {
		return questionID;
	}


	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}

	public String getAuthorName() {
		return author.getFirstName()+" "+author.getLastName();
	}

	public String getProfessionID() {
		return profession.getProfessionID();
	}

	public Profession getProfession() {
		return profession;
	}
	public String getProfessionName() {
		return profession.getProfessionName();
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public String getAuthorID() {
		return authorID;
	}

	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}

	public String getQuestion() {
		return question;
	}

	public String getQuestionText() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public Integer getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(Integer correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getQuestionInstructions() {
		return questionInstructions;
	}

	public void setQuestionInstructions(String questionInstructions) {
		this.questionInstructions = questionInstructions;
	}

	public int getUsedCounter() {
		return usedCounter;
	}

	public void setUsedCounter(int usedCounter) {
		this.usedCounter = usedCounter;
	}

	public String getQuestionPoints() {
		return questionPoints;
	}

	public void setQuestionPoints(String questionPoints) {
		this.questionPoints = questionPoints;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	@Override
	public String toString() {
		return "Question [questionID=" + questionID + ", question=" + question + ", answer1=" + answer1 + ", answer2="
				+ answer2 + ", answer3=" + answer3 + ", answer4=" + answer4 + ", correctAnswer=" + correctAnswer
				+ ", questionPoints=" + questionPoints + ", studentAnswer=" + studentAnswer + ", correctAns="
				+ correctAns + "]";
	}
	
	public Integer getStudentAnswer() {
		return studentAnswer;
	}
	
	public void setStudentAnswer(Integer chosenAns) {
		studentAnswer=chosenAns;
	}
	




	
	
}
