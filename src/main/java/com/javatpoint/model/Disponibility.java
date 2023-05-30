/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.model;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author usuario
 */
public class Disponibility {
    
    public final static int STATUS_AVAILABLE = 0;
    public final static int STATUS_RESERVED = 1;  
        
    private int idDisponibility;
    private int idUnit;
    private int status;
    private int nights;
    
    private double price;
    private double finalPrice;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;
   
    private Unit unit;
    
    private String token = "";
    
    private boolean deleted = false;
    
    /**
     * @return int
     */
    public int getIdDisponibility()
    {
        return idDisponibility;
    }
    
    /**
     * @param idDisponibility int
     */
    public void setIdDisponibility( int value )
    {
        this.idDisponibility = value;
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
    public int getStatus()
    {
        return status;
    }
    
    /**
     * @param status int
     */
    public void setStatus( int value )
    {
        this.status = value;
    }
    
    
    /**
     * @return int
     */
    public int getNights()
    {
        return nights;
    }
    
    /**
     * @param nights int
     */
    public void setNights( int value )
    {
        this.nights = value;
    }
    
    /**
     * @return double
     */
    public double getPrice()
    {
        return price;
    }
    
    /**
     * @param price double
     */
    public void setPrice( double value )
    {
        this.price = value;
    }
    
    
    /**
     * @return double
     */
    public double getFinalPrice()
    {
        return finalPrice;
    }
    
    /**
     * @param finalPrice double
     */
    public void setFinalPrice( double value )
    {
        this.finalPrice = value;
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
    

             
    @Override
    public int hashCode() 
    {
        return idDisponibility;
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

        Disponibility d = (Disponibility) obj;
        return idDisponibility == d.idDisponibility;
    }
    
    
    /**
     * @return Unit
     */
    public Unit getUnit()
    {
        return unit;
    }
    
    /**
     * @param unit Unit
     */
    public void setUnit( Unit value )
    {
        this.unit = value;
    }
        
    /**
     * @return boolean
     */
    public boolean isDeleted()
    {
        return deleted;
    }
    
    /**
     * @param deleted boolean
     */
    public void setDeleted( boolean value )
    {
        this.deleted = value;
    }
              
}
