/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.model;

import java.io.Serializable;

/**
 *
 * @author usuario
 */
public class GeoName implements Serializable {
    
    public int id;
    public String name;
    
    public GeoName()
    {        
    }
    
    public GeoName(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    
    /**
     * @return int
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * @param id int
     */
    public void setId( int value )
    {
        this.id = value;
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
    

}
