package entity;

public class LoggedInUser {
	
	private static LoggedInUser instance =  new LoggedInUser();
	private static User user=null;
	private static User headDep=null;
	
	/*private constructor bc singleton*/
	private LoggedInUser() {
	}

	/**
	 * 
	 * @return the singleton instance of the logged in user
	 * if the instance is null, will create a new one, by the singleton design pattern
	 */
	public static LoggedInUser getInstance() {
		return instance;
	}

	
	/**
	 * @return the logged in user if the singleton object is not null
	 * @throws Exception if no user is logged in
	 */
	public User getUser() throws Exception {
		if (user != null) {
			return user;
		}
		else{
			throw new Exception("No user is logged in, use getInstance(User user) to login a user");
		}
	}
	
	public static User getHeadDep(){
		return headDep;
	}
	/**
	 * @return assigns user if no user is logged in 
	 * @throws Exception if a user already logged in
	 */
	public void setUser(User user1) throws Exception {
		if(user1.getUserType()==UserType.HeadDep) {
			headDep=user1;
			return;
		}
		if (user == null) {
			user=user1;
		}
		else {
			throw new Exception("There's a user logged in, use getInstance.logOut() to log them out");
		}
		
	}
	
	
	/**
	 * in order to log out the user, the single instance becomes null
	 */
	public void  logOut() {
		//System.out.println("logout user");
		user = null;
	}
	
	
	public void  logOutHeadDep() {
		//System.out.println("logout headDep");
		headDep = null;
	}
}
