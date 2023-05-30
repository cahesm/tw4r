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
public class Location implements Serializable {
    
    public int idLocation;
    public String name;
    
    public Location()
    {        
    }
    
    public Location(int id, String name)
    {
        this.idLocation = id;
        this.name = name;
    }
            
    /**
     * @return int
     */
    public int getIdLocation()
    {
        return idLocation;
    }
    
    /**
     * @param idLocation int
     */
    public void setIdLocation( int value )
    {
        this.idLocation = value;
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
