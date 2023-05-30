package com.javatpoint.model;

import java.sql.Timestamp;
import javax.validation.constraints.Size;

public class User {

  public final static int STATUS_NOT_VALIDATED = 0;
  public final static int STATUS_APPROVED = 1;    
  public final static int STATUS_REPROVED = 2;    
    
  @Size(min = 2, max = 16)  
  private String username;
  
  private String cod;
  private String password;
  private String confirmPassword;
  private String name;  
  private String email;
  private String verificationToken;
  private String comment = "";
  
  private int idUser;
  private int validationStatus = 0;
  private String phone;
  
  private Timestamp creation;
  
  private boolean admin = false;
  private boolean owner = true;
  private boolean active = false;
  
  private Address address = new Address();
  
  private Media validationDoc;

  
  /**
   * @return String
   */
  public String getCod()
  {
      return cod;
  }
  
  /**
   * @param cod String
   */
  public void setCod( String value )
  {
      this.cod = value;
  }
  

  
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {    
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  
  /**
   * @return String
   */
  public String getConfirmPassword()
  {
      return confirmPassword;
  }
  
  /**
   * @param confirmPassword String
   */
  public void setConfirmPassword( String value )
  {
      this.confirmPassword = value;
  }
  

  /**
   * @return String
   */
  public String getName()
  {
      return name;
  }
  
  /**
   * @param name String
   */
  public void setName( String value )
  {
      this.name = value;
  }
  
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  
  
  /**
   * @return String
   */
  public String getPhone()
  {
      return phone;
  }
  
  /**
   * @param phone String
   */
  public void setPhone( String value )
  {
      this.phone = value;
  }
  
  /**
   * @return String
   */
  public String getVerificationToken()
  {
      return verificationToken;
  }
  
  /**
   * @param verificationToken String
   */
  public void setVerificationToken( String value )
  {
      this.verificationToken = value;
  }
  
  
  /**
   * @return String
   */
  public String getComment()
  {
      return comment;
  }
  
  /**
   * @param comment String
   */
  public void setComment( String value )
  {
      this.comment = value;
  }
  

  
  /**
   * @return int
   */
  public int getIdUser()
  {
      return idUser;
  }
  
  /**
   * @param idUser int
   */
  public void setIdUser( int value )
  {
      this.idUser = value;
  }
     
  
  /**
   * @return int
   */
  public int getValidationStatus()
  {
      return validationStatus;
  }
  
  /**
   * @param validationStatus int
   */
  public void setValidationStatus( int value )
  {
      this.validationStatus = value;
  }
  

  
  /**
   * @return Date
   */
  public Timestamp getCreation()
  {
      return creation;
  }
  
  /**
   * @param creation Date
   */
  public void setCreation( Timestamp value )
  {
      this.creation = value;
  }
  

  
  /**
   * @return boolean
   */
  public boolean isOwner()
  {
      return owner;
  }
  
  /**
   * @param owner boolean
   */
  public void setOwner( boolean value )
  {
      this.owner = value;
  }
  
  
  /**
   * @return boolean
   */
  public boolean isAdmin()
  {
      return admin;
  }
  
  /**
   * @param admin boolean
   */
  public void setAdmin( boolean value )
  {
      this.admin = value;
  }
  

  /**
   * @return boolean
   */
  public boolean isActive()
  {
      return active;
  }
  
  /**
   * @param active boolean
   */
  public void setActive( boolean value )
  {
      this.active = value;
  }
  

  
  /**
   * @return Address
   */
  public Address getAddress()
  {
      return address;
  }
  
  /**
   * @param address Address
   */
  public void setAddress( Address value )
  {
      this.address = value;
  }
  
  
  /**
   * @return Media
   */
  public Media getValidationDoc()
  {
      return validationDoc;
  }
  
  /**
   * @param validationDoc Media
   */
  public void setValidationDoc( Media value )
  {
      this.validationDoc = value;
  }
  

  
}
