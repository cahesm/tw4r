package com.javatpoint.dao;

import com.javatpoint.model.Disponibility;
import com.javatpoint.model.Hotel;
import com.javatpoint.model.ManagerReservationsSearch;
import com.javatpoint.model.Media;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.javatpoint.model.Reservation;
import com.javatpoint.model.Unit;
import com.javatpoint.service.DisponibilityService;
import com.javatpoint.service.HotelService;
import com.javatpoint.service.MediaService;
import com.javatpoint.service.UnitService;
import java.util.Date;
import org.apache.commons.lang.RandomStringUtils;

public class ReservationDaoImpl implements ReservationDao {
  
  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  DisponibilityService disponibilityService;
  
  @Autowired
  UnitService unitService;
  
  @Autowired
  HotelService hotelService;
  
   @Autowired
  MediaService mediaService;
    
  public void register(Reservation reservation) {

    String sql = "insert into reservations ( idDisponibility,idUser, status, startDate, endDate, comment, cod, creation ) values(?,?,?,?,?,?,?,?)";

    jdbcTemplate.update(sql, new Object[] { reservation.getIdDisponibility(), reservation.getIdUser(), reservation.getStatus(), reservation.getStartDate(),reservation.getEndDate(), reservation.getComment(), "R" + RandomStringUtils.randomAlphanumeric(8).toUpperCase(), new Date() });
    
    
  }
    
  public void update(Reservation reservation) {

    String sql = "update reservations set status = ?, startDate = ?, endDate = ?, comment = ? where idReservation = ? ";

    jdbcTemplate.update(sql, new Object[] { reservation.getStatus(),reservation.getStartDate(),reservation.getEndDate(), reservation.getComment(), reservation.getIdReservation() });
        
  }
    
  public Reservation getReservation(int idReservation) {

    String sql = "select * from reservations where idReservation = ?" ; 
        
    List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationMapper(),new Object[] { idReservation });
        
    return reservations.size() > 0 ? reservations.get(0) : null;
  }
  
  public Reservation getReservation(String cod) {

    String sql = "select * from reservations where cod = ?" ; 
        
    List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationMapper(),new Object[] { cod });
        
    return reservations.size() > 0 ? reservations.get(0) : null;
  }
  
        
  public List<Reservation> getReservationsByDisponibility(int idDisponibility) {

    String sql = "select * from reservations where idDisponibility = ?";
        
    List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationMapper(),new Object[] { idDisponibility });
        
    return reservations;
  }
  
  public List<Reservation> getReservationsByUser(int idUser) {

    String sql = "select * from reservations where idUser = ?";
        
    List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationMapper(),new Object[] { idUser });
        
    return reservations;
  }
  
  public List<Reservation> getReservationsByUnit(int idUnit) {

    String sql = "select * from reservations r inner join disponibilities d on r.idDisponibility = d.idDisponibility where d.idUnit = ?";
        
    List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationMapper(),new Object[] { idUnit });
        
    return reservations;
  }
  
  public List<Reservation> getReservationsByFilter( ManagerReservationsSearch filter ) {

    String sql = "";
      
    String f = "";
      
      f +=  !filter.getCod().isEmpty() ? f.isEmpty() ? " r.cod = '" + filter.getCod() + "'" : " and r.cod = '" + filter.getCod() + "'" : ""; 
      
      if ( filter.getOption() >= 0 )
      {
          f +=  f.isEmpty() ? " r.status = " + filter.getOption()  : " and r.status = " + filter.getOption(); 
      }
      
      if ( filter.getIdUnit() > 0 )
      {
          f +=  f.isEmpty() ? " d.idUnit = " + filter.getIdUnit() : " and d.idUnit = " + filter.getIdUnit() ; 
      }
      
      
      sql = " from reservations r inner join disponibilities d on r.idDisponibility = d.idDisponibility " + (f.isEmpty() ? "" : " where " + f);
                                    
      String countSQL = "select count(*) " + sql;
      
      
      int count = jdbcTemplate.queryForObject( countSQL, Integer.class);      
      int pages = (int)((count + filter.getPageSize() - 1) / filter.getPageSize());
      int page = filter.getPage();
      
      if ( page > pages || page < 1 )
      {
          page = 1;
      }
      
      filter.setPages( pages );
      filter.setPage( page );
      filter.setTotal( count );
      
      int offset = (page - 1) * filter.getPageSize();
      
      String fullSQL = "select * " + sql + " limit " + offset + "," + filter.getPageSize() ;
        
    List<Reservation> reservations = jdbcTemplate.query( fullSQL, new ReservationMapper());
                    
    return reservations;
  }
  
  public List<Reservation> getActiveReservationsByUnit(int idUnit) {

    String sql = "select * from reservations r inner join disponibilities d on r.idDisponibility = d.idDisponibility where d.idUnit = ? and r.status not in (4,5) and now() < r.startDate";
        
    List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationMapper(),new Object[] { idUnit });
        
    return reservations;
  }
  
  public List<Reservation> getReservationsByStatus(int status) {

    String sql = "select * from reservations r inner join disponibilities d on r.idDisponibility = d.idDisponibility where r.status = ? order by d.idUnit";
        
    List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationMapper(),new Object[] { status });
        
    return reservations;
  }
  
  public List<Reservation> getDelayedReservations() {

    String sql = "select * from reservations r where now() >= startDate and status < 3 ";
        
    List<Reservation> reservations = jdbcTemplate.query(sql, new ReservationMapper() );
        
    return reservations;
  }
  
  public boolean hasActiveReservationForDisponibility(int idDisponibility) {

    String sql = "select count(*) from reservations where idDisponibility = ? and status not in (4,5)  ";
        
        
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class ,new Object[] { idDisponibility });
        
    return count != null && count > 0;
  }
  
  class ReservationMapper implements RowMapper<Reservation> {

  public Reservation mapRow(ResultSet rs, int arg1) throws SQLException {
    Reservation reservation = new Reservation();
            
    
    reservation.setIdReservation(rs.getInt("idReservation"));
    reservation.setIdUser(rs.getInt("idUser"));
    reservation.setIdDisponibility(rs.getInt("idDisponibility"));
    reservation.setStatus( rs.getInt("status"));
       
    reservation.setStartDate( rs.getDate("startDate"));
    reservation.setEndDate( rs.getDate("endDate"));
    reservation.setComment( rs.getString("comment"));
    reservation.setCod( rs.getString("cod"));
    reservation.setCreation( rs.getTimestamp("creation"));
    
    Disponibility disponibility = disponibilityService.getDisponibility(reservation.getIdDisponibility());
    reservation.setDisponibility( disponibility );
    
    Unit unit = unitService.getUnitWithoutDependencies( disponibility.getIdUnit() );
    reservation.setUnit( unit );
    
    Hotel hotel = hotelService.getHotel( unit.getIdHotel() );
    reservation.setHotel( hotel );
    
    reservation.setPayments( mediaService.getMedias( Media.TYPE_RESERVATION_PAYMENT, reservation.getIdReservation() ));
    
    List<Media> vouchers = mediaService.getMedias( Media.TYPE_RESERVATION_VOUCHER, reservation.getIdReservation() );
    
    reservation.setVoucher( vouchers != null && !vouchers.isEmpty() ? vouchers.get(0) : null );
    
    List<Media> docs = mediaService.getMedias( Media.TYPE_RESERVATION_DOC, reservation.getIdReservation() );
    
    reservation.setDoc( docs != null && !docs.isEmpty() ? docs.get(0) : null );
        
    return reservation;
  }}

}


  
  
