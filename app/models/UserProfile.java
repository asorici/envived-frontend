package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

/**
 * A representation of a user. 
 * @author Bugs Bunny
 */

@Entity
public class UserProfile{
	
	//for registration
 @Id
 	 String Id;
 	 String email;
	 String first_name;
	 String last_name;
	 String password1;
	 String password2;

	 String password;
	
  
  public UserProfile(String email, String first_name, String last_name, String password1, String password2) {
 
    this.email = email;
    this.first_name = first_name;
    this.last_name = last_name;
    this.password1 = password1;
    this.password2 = password2;
   
    
  }
  

  /**
   * @return : email
   */
  public String getEmail() {
    return email;
  }
  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getFirst_name() {
		return first_name;
	}
	
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	
	public String getLast_name() {
		return last_name;
	}
	
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
  /**
   * @return : password1
   */
  public String getPassword1() {
    return password1;
  }
  /**
   * @param password1 : password1 to set
   */
  public void setPassword1(String password1) {
    this.password1 = password1;
  }

  /**
   * @return : password2
   */
  public String getPassword2() {
    return password2;
  }
  /**
   * @param password2 : password1 to set
   */
  public void setPassword2(String password2) {
    this.password2 = password2;
  }
  
	 
	 public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

}
