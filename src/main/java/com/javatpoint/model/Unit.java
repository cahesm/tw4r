package com.javatpoint.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Unit {

   public final static int STATUS_NOT_VALIDATED = 0;
   public final static int STATUS_VALIDATED = 1;  
  
  private String token; 
  private String cod; 
   
  private int idUnit;
  private int idUser;
  private int idHotel;
  private int idCancelOption;
  
  private int room = 0;
  private int bedRoom = 0;
  private int person = 0;
  private double bathRoom = 0;
  
  private int validationStatus = 0;
  private int cancelDays = 0;
  
  private String description = "";
  private String cancelInfo = "";
  
  private boolean deleted = false;
 
  private User user;
  private Hotel hotel;
  private Option cancelOption;
     
  private Timestamp creation;
  
  private List<Disponibility> disponibilities;
  private List<UnitCancelOption> cancelOptions;
  private List<Media> medias;
  private List<Media> documents;
  private List<Attribute> attributes = new ArrayList();
  private List<Reservation> reservations = new ArrayList();
  
  private String[] atts; 
  
    
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
  

  /**
   * @return String
   */
  public String getToken()
  {
      return token;
  }
  
  /**
   * @param token String
   */
  public void setToken( String value )
  {
      this.token = value;
  }
  

  
  /**
   * @return int
   */
  public int getIdUnit()
  {
      return idUnit;
  }
  
  /**
   * @param idUnit int
   */
  public void setIdUnit( int value )
  {
      this.idUnit = value;
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
  public int getIdHotel()
  {
      return idHotel;
  }
  
  /**
   * @param idHotel int
   */
  public void setIdHotel( int value )
  {
      this.idHotel = value;
  }
  
  
  /**
   * @return int
   */
  public int getIdCancelOption()
  {
      return idCancelOption;
  }
  
  /**
   * @param idCancelOption int
   */
  public void setIdCancelOption( int value )
  {
      this.idCancelOption = value;
  }

  /**
   * @return int
   */
  public int getCancelDays()
  {
      return cancelDays;
  }
  
  /**
   * @param cancelDays int
   */
  public void setCancelDays( int value )
  {
      this.cancelDays = value;
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
   * @return int
   */
  public int getRoom()
  {
      return room;
  }
  
  /**
   * @param room int
   */
  public void setRoom( int value )
  {
      this.room = value;
  }
  
  /**
   * @return int
   */
  public int getBedRoom()
  {
      return bedRoom;
  }
  
  /**
   * @param bedRoom int
   */
  public void setBedRoom( int value )
  {
      this.bedRoom = value;
  }
  
  /**
   * @return int
   */
  public double getBathRoom()
  {
      return bathRoom;
  }
  
  /**
   * @param bathRoom int
   */
  public void setBathRoom( double value )
  {
      this.bathRoom = value;
  }
   
  
  /**
   * @return int
   */
  public int getPerson()
  {
      return person;
  }
  
  /**
   * @param person int
   */
  public void setPerson( int value )
  {
      this.person = value;
  }
   
  /**
   * @return Timestamp
   */
  public Timestamp getCreation()
  {
      return creation;
  }
  
  /**
   * @param creation Timestamp
   */
  public void setCreation( Timestamp value )
  {
      this.creation = value;
  }
  
  
  /**
   * @return String
   */
  public String getDescription()
  {
      return description;
  }
  
  /**
   * @param description String
   */
  public void setDescription( String value )
  {
      this.description = value;
  }
  
  
  /**
   * @return String
   */
  public String getCancelInfo()
  {
      return cancelInfo;
  }
  
  /**
   * @param cancelInfo String
   */
  public void setCancelInfo( String value )
  {
      this.cancelInfo = value;
  }
  

  
  /**
   * @return boolean
   */
  public boolean isDeleted()
  {
      return deleted;
  }
  
  /**
   * @param deleted boolean
   */
  public void setDeleted( boolean value )
  {
      this.deleted = value;
  }
   
  /**
   * @return List<Disponibility>
   */
  public List<Disponibility> getDisponibilities()
  {
      return disponibilities;
  }
  
  /**
   * @param value List<Disponibility>
   */
  public void setDisponibilities( List<Disponibility> value )
  {
      this.disponibilities = value;
  }
  
  
  /**
   * @return List<UnitCancelOption>
   */
  public List<UnitCancelOption> getCancelOptions()
  {
      return cancelOptions;
  }
  
  /**
   * @param cancelOptions List<UnitCancelOption>
   */
  public void setCancelOptions( List<UnitCancelOption> value )
  {
      this.cancelOptions = value;
  }
  

  
  /**
   * @return List<Media>
   */
  public List<Media> getMedias()
  {
      return medias;
  }
  
  /**
   * @param medias List<Media>
   */
  public void setMedias( List<Media> value )
  {
      this.medias = value;
  }
  
  
  /**
   * @return List<Media>
   */
  public List<Media> getDocuments()
  {
      return documents;
  }
  
  /**
   * @param documents List<Media>
   */
  public void setDocuments( List<Media> value )
  {
      this.documents = value;
  }
   
  /**
   * @return List<Attribute>
   */
  public List<Attribute> getAttributes()
  {
      return attributes;
  }
  
  /**
   * @param attributes List<Attribute>
   */
  public void setAttributes( List<Attribute> value )
  {
      this.attributes = value;
  }
  

 
  /**
   * @return Hotel
   */
  public Hotel getHotel()
  {
      return hotel;
  }
  
  /**
   * @param hotel Hotel
   */
  public void setHotel( Hotel value )
  {
      this.hotel = value;
  }
  
  
  /**
   * @return String[]
   */
  public String[] getAtts()
  {
      return atts;
  }
  
  /**
   * @param atts String[]
   */
  public void setAtts( String[] value )
  {
      this.atts = value;
  }
  

  
  /**
   * @return List<Reservation>
   */
  public List<Reservation> getReservations()
  {
      return reservations;
  }
  
  /**
   * @param reservations List<Reservation>
   */
  public void setReservations( List<Reservation> value )
  {
      this.reservations = value;
  }
  
  
  /**
   * @return User
   */
  public User getUser()
  {
      return user;
  }
  
  /**
   * @param user User
   */
  public void setUser( User value )
  {
      this.user = value;
  }
  
  
//  /**
//   * @return Option
//   */
//  public Option getCancelOption()
//  {
//      return cancelOption;
//  }
//  
//  /**
//   * @param cancelOption Option
//   */
//  public void setCancelOption( Option value )
//  {
//      this.cancelOption = value;
//  }
//  


  
}
