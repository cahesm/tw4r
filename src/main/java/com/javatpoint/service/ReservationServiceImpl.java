package com.javatpoint.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.javatpoint.dao.ReservationDao;
import com.javatpoint.model.ManagerReservationsSearch;
import com.javatpoint.model.Reservation;

public class ReservationServiceImpl implements ReservationService {

  @Autowired
  public ReservationDao reservationDao;

  public void register(Reservation reservation) {
    reservationDao.register(reservation);
  }
  
  public void update(Reservation reservation) {
    reservationDao.update(reservation);
  }
  
        
  public Reservation getReservation(int id) {
    return reservationDao.getReservation(id);
  }
  
  public Reservation getReservation(String cod) {
    return reservationDao.getReservation(cod);
  }
  
  public List<Reservation> getReservationsByDisponibility( int idDisponibility ) {
    return reservationDao.getReservationsByDisponibility( idDisponibility );
  }
  
  public List<Reservation> getReservationsByUser( int idUser ) {
    return reservationDao.getReservationsByUser( idUser );
  }
  
  public List<Reservation> getReservationsByUnit( int idUnit ) {
    return reservationDao.getReservationsByUnit( idUnit );
  }
    
  public List<Reservation> getActiveReservationsByUnit( int idUnit ) {
    return reservationDao.getActiveReservationsByUnit( idUnit );
  }
  
  public List<Reservation> getReservationsByFilter( ManagerReservationsSearch filter ) {
    return reservationDao.getReservationsByFilter( filter );
  }
  
  public List<Reservation> getReservationsByStatus( int status ) {
    return reservationDao.getReservationsByStatus( status );
  }
  
  public List<Reservation> getDelayedReservations() {
    return reservationDao.getDelayedReservations();
  }
  
  public boolean hasActiveReservationForDisponibility( int idDisponibility ) {
    return reservationDao.hasActiveReservationForDisponibility( idDisponibility );
  }
  
  
}
