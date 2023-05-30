package com.javatpoint.model;

public class ManagerUsersSearch extends Search
{
    private String cod = "";   
    private int option = 0;
    
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
                             
}
