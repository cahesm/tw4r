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
public class UnitCancelOption {
    
        
    private int idCancelOption;
    private int idUnit;
    private int days;
    private float devolution;
                
    private String description = "";
    
    /**
     * @return int
     */
    public int getIdCancelOption()
    {
        return idCancelOption;
    }
    
    /**
     * @param idCancelOption int
     */
    public void setIdCancelOption( int value )
    {
        this.idCancelOption = value;
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
    
    /**
     * @return int
     */
    public int getDays()
    {
        return days;
    }
    
    /**
     * @param days int
     */
    public void setDays( int value )
    {
        this.days = value;
    }
    
    
    /**
     * @return float
     */
    public float getDevolution()
    {
        return devolution;
    }
    
    /**
     * @param retention float
     */
    public void setDevolution( float value )
    {
        this.devolution = value;
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
    public boolean equals(Object obj) 
    {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        UnitCancelOption d = (UnitCancelOption) obj;
        return idCancelOption == d.idCancelOption;
    }

}
