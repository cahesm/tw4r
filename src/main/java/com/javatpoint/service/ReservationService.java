package com.javatpoint.service;

import com.javatpoint.model.ManagerReservationsSearch;
import com.javatpoint.model.Reservation;
import java.util.List;

public interface ReservationService {

  void register(Reservation reservation);
  void update(Reservation reservation);
  
  
  Reservation getReservation( int idReservation );  
  Reservation getReservation( String cod );  
  List<Reservation> getReservationsByDisponibility( int idDisponibility );
  List<Reservation> getReservationsByUser( int idUser );
  List<Reservation> getReservationsByUnit( int idUnit );
  List<Reservation> getReservationsByFilter( ManagerReservationsSearch filter );
  List<Reservation> getActiveReservationsByUnit( int idUnit );
  List<Reservation> getReservationsByStatus( int status );
  List<Reservation> getDelayedReservations();
  boolean hasActiveReservationForDisponibility( int idDisponibility );
    
}
