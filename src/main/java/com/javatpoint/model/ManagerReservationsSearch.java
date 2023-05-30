package com.javatpoint.model;

public class ManagerReservationsSearch extends Search
{
    private String cod = "";   
    private String token = "";   
    private int option = 0;
    private int idUnit = 0;
    
    
    
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
     * @return int
     */
    public int getOption()
    {
        return option;
    }
    
    /**
     * @param option int
     */
    public void setOption( int value )
    {
        this.option = value;
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
    

}
