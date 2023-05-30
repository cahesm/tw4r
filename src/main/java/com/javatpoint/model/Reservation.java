package com.javatpoint.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Reservation {

    public final static int STATUS_WAITING_APPROVAL = 0;
    public final static int STATUS_WAITING_PAYMENT = 1;  
    public final static int STATUS_WAITING_VOUCHER = 2;  
    public final static int STATUS_COMPLETED = 3;  
    public final static int STATUS_CANCELED = 4;  
    public final static int STATUS_REPROVED = 5;  
  
    private int idReservation;
    private int idUser;
    private int idDisponibility;

    private int status = STATUS_WAITING_APPROVAL;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;
       
    private Timestamp creation;
    
    private Disponibility disponibility;
    private Hotel hotel;
    private Unit unit;
    
    private List<Media> payments;
    private Media voucher;
    private Media doc;
  
    private String comment = "";
    private String cod = "";
  
    /**
     * @return int
     */
    public int getIdReservation()
    {
        return idReservation;
    }
    
    /**
     * @param idReservation int
     */
    public void setIdReservation( int value )
    {
        this.idReservation = value;
    }
        
    /**
     * @return int
     */
    public int getIdUser()
    {
        return idUser;
    }
    
    /**
     * @param idUser int
     */
    public void setIdUser( int value )
    {
        this.idUser = value;
    }
    
    
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
     * @return Timestamp
     */
    public Timestamp getCreation()
    {
        return creation;
    }
    
    /**
     * @param creation Timestamp
     */
    public void setCreation( Timestamp value )
    {
        this.creation = value;
    }
    
    
    
    /**
     * @return Disponibility
     */
    public Disponibility getDisponibility()
    {
        return disponibility;
    }
    
    /**
     * @param disponibility Disponibility
     */
    public void setDisponibility( Disponibility value )
    {
        this.disponibility = value;
    }
    
    /**
     * @return Hotel
     */
    public Hotel getHotel()
    {
        return hotel;
    }
    
    /**
     * @param hotel Hotel
     */
    public void setHotel( Hotel value )
    {
        this.hotel = value;
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
     * @return String
     */
    public String getComment()
    {
        return comment;
    }
    
    
    /**
     * @return String
     */
    public String getCod()
    {
        return cod;
    }
    
    /**
     * @param token String
     */
    public void setCod( String value )
    {
        this.cod = value;
    }
    

    
    /**
     * @param comment String
     */
    public void setComment( String value )
    {
        this.comment = value;
    }
    
    
    /**
     * @return List<Media>
     */
    public List<Media> getPayments()
    {
        return payments;
    }
    
    /**
     * @param payments List<Media>
     */
    public void setPayments( List<Media> value )
    {
        this.payments = value;
    }
    
    
    /**
     * @return Media
     */
    public Media getVoucher()
    {
        return voucher;
    }
    
    /**
     * @param voucher Media
     */
    public void setVoucher( Media value )
    {
        this.voucher = value;
    }
        
    /**
     * @return Media
     */
    public Media getDoc()
    {
        return doc;
    }
    
    /**
     * @param doc Media
     */
    public void setDoc( Media value )
    {
        this.doc = value;
    }
    
   
}
