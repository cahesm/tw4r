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
public class Attribute {
    
    
    private int idAttribute;
    private int idCategory;
    
    private String name;
    
    
    public String getIdAsString() 
    {
        return new Long(idAttribute).toString();
    }
    
    /**
     * @return int
     */
    public int getIdAttribute()
    {
        return idAttribute;
    }
    
    /**
     * @param idAttribute int
     */
    public void setIdAttribute( int value )
    {
        this.idAttribute = value;
    }
    
    /**
     * @return int
     */
    public int getIdCategory()
    {
        return idCategory;
    }
    
    /**
     * @param idCategory int
     */
    public void setIdCategory( int value )
    {
        this.idCategory = value;
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

    @Override
    public int hashCode() {
        return new Long(idAttribute).hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof Attribute)) {
            return false;
        }
        return this.idAttribute == ((Attribute)obj).getIdAttribute();              
    }
    
    

    @Override
    public String toString() {
        return name;
    }
    
    

       
}
