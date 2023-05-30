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
public class UnitAttribute {
    
    
    private int idUnit;
    private int idAttribute;
        
    private String comment;
    
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
    
       
}
