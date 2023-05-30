package com.javatpoint.model;

public class HotelsSearch extends Search
{
    private String term = "";   
    private int idHotel;
    private int idCountry;
    private int idState;
    private int idCity;
           
    
    
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
     * @return String
     */
    public String getTerm()
    {
        return term;
    }
    
    /**
     * @param term String
     */
    public void setTerm( String value )
    {
        this.term = value;
    }
    

              
}
