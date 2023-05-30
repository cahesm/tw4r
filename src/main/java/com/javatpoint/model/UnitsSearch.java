package com.javatpoint.model;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

public class UnitsSearch extends Search
{
    
    private int idUnit;
    private int idHotel;
    private int idCountry;
    private int idState;
    private int idCity;
    
    private int room;
    private int bedRoom;
    private int bathRoom;
    private int person;
    
    private String region = "";
    private String searchTerm = "";
            
    private boolean vacant = false;
  
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;
    
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
    public int getIdCountry()
    {
        return idCountry;
    }
    
    /**
     * @param idCountry int
     */
    public void setIdCountry( int value )
    {
        this.idCountry = value;
    }
    
    /**
     * @return int
     */
    public int getIdState()
    {
        return idState;
    }
    
    /**
     * @param idState int
     */
    public void setIdState( int value )
    {
        this.idState = value;
    }
    
    /**
     * @return int
     */
    public int getIdCity()
    {
        return idCity;
    }
    
    /**
     * @param idCity int
     */
    public void setIdCity( int value )
    {
        this.idCity = value;
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
    public int getBathRoom()
    {
        return bathRoom;
    }
    
    /**
     * @param bathRoom int
     */
    public void setBathRoom( int value )
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
     * @return String
     */
    public String getRegion()
    {
        return region;
    }
    
    /**
     * @param region String
     */
    public void setRegion( String value )
    {
        this.region = value;
    }
      
    /**
     * @return String
     */
    public String getSearchTerm()
    {
        return searchTerm;
    }
    
    /**
     * @param searchTerm String
     */
    public void setSearchTerm( String value )
    {
        this.searchTerm = value;
    }
       
    /**
     * @return boolean
     */
    public boolean isVacant()
    {
        return vacant;
    }
    
    /**
     * @param vacant boolean
     */
    public void setVacant( boolean value )
    {
        this.vacant = value;
    }
    
    
    /**
     * @return Date
     */
    public Date getStartDate()
    {
        return startDate;
    }
    
    /**
     * @param startDate Date
     */
    public void setStartDate( Date value )
    {
        this.startDate = value;
    }
    
    /**
     * @return Date
     */
    public Date getEndDate()
    {
        return endDate;
    }
    
    /**
     * @param endDate Date
     */
    public void setEndDate( Date value )
    {
        this.endDate = value;
    }           
               

}
