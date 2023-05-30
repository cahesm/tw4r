/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.model;

/**
 *
 * @author usuario
 */
public class Option {
    
    public static final int OPTION_CANCEL = 1;
    
    private int idOption;
    private int type;
        
    private String name;
    private String description;
    /**
     * @return int
     */
    public int getIdOption()
    {
        return idOption;
    }
    
    /**
     * @param idOption int
     */
    public void setIdOption( int value )
    {
        this.idOption = value;
    }
    
    /**
     * @return int
     */
    public int getType()
    {
        return type;
    }
    
    /**
     * @param type int
     */
    public void setType( int value )
    {
        this.type = value;
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
    
 
    @Override
    public int hashCode() {
        return new Long(idOption).hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof Option)) {
            return false;
        }
        return this.idOption == ((Option)obj).getIdOption();              
    }
    
    

    @Override
    public String toString() {
        return name;
    }
    
    

       
}
