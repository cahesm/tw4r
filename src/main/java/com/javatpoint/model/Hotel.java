package com.javatpoint.model;

import java.util.List;

public class Hotel 
{

  private String token;  
    
  private int idHotel;
  
  private String name;
  
  private String site;
  
  private Address address = new Address();
  
  private List<Media> medias;
  
  
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
  
  
  /**
   * @return String
   */
  public String getSite()
  {
      return site;
  }
  
  /**
   * @param site String
   */
  public void setSite( String value )
  {
      this.site = value;
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
  


    @Override
    public String toString() {
        return name + " - " + address.getCity() + " - " + address.getState() + " - " + address.getCountry() + " - " + address.getRegion();
    }
  
  
  
}
