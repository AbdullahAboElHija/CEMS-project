package entity;

import java.io.Serializable;

public class User implements Serializable {

	private String id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private int isLogged = 0;
	private UserType userType;
	private Integer status;
	private String phone;

	/* constructor */
	public User(String id,String username, String password, String firstName, String lastName, String email, UserType userType,Integer status) {
		super();
		this.id = id;
		this.username=username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userType = userType;
		this.status=status;
	}

	/* constructor */
	public User(String username, String password) {
		this.username=username;
		this.password = password;
	}

	public User(String id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/* constructor */
	public User(String id, UserType userType) {
		this.id = id;
		this.userType = userType;
	}

	/* get status field */
	public Integer getStatus() {
		return status;
	}
	
	/* get Id field */
	public String getId() {
		return id;
	}

	/* set Id field */
	public void setId(String id) {
		this.id = id;
	}
	
	/* get username field */
	public String getUsername() {
		return username;
	}
	
	/* get Password field */
	public String getPassword() {
		return password;
	}

	/* set Password field */
	public void setPassword(String password) {
		this.password = password;
	}

	/* get FirstName field */
	public String getFirstName() {
		return firstName;
	}

	/* set FirstName field */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/* get LastName field */
	public String getLastName() {
		return lastName;
	}

	/* get LastName field */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/* get Email field */
	public String getEmail() {
		return email;
	}

	/* set Email field */
	public void setEmail(String email) {
		this.email = email;
	}

	/* get Logged status {0,1} */
	public int isLogged() {
		return isLogged;
	}

	/* set Logged status {0,1} */
	public void setLogged(int isLogged) {
		this.isLogged = isLogged;
	}

	/* toString to print message in server log */
	@Override
	public String toString() {
		return "User: " + userType + " isLogged=" + isLogged;
	}

	/* get UserType field */
	public UserType getUserType() {
		return userType;
	}

	/* set UserType field */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	
	public void setPhone(String ph) {
		phone = ph;
	}
	
	public String getPhone() {
		return phone;
	}

}
