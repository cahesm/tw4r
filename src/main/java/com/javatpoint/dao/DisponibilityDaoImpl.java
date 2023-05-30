package com.javatpoint.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.javatpoint.model.Disponibility;
import com.javatpoint.model.Unit;
import com.javatpoint.model.UnitsSearch;
import com.javatpoint.service.AddressService;
import com.javatpoint.service.ReservationService;
import com.javatpoint.service.UnitService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DisponibilityDaoImpl implements DisponibilityDao {
  
  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  ReservationService reservationService;
  
  @Autowired
  AddressService addressService;
  
  @Autowired
  UnitService unitService;
    
  public void register(Disponibility disponibility) {

    String sql = "insert into disponibilities ( idUnit, status, nights, price, startDate, endDate ) values(?,?,?,?,?,?)";

    jdbcTemplate.update(sql, new Object[] { disponibility.getIdUnit(),disponibility.getStatus(), disponibility.getNights(), disponibility.getPrice(), disponibility.getStartDate(),disponibility.getEndDate() });
    
    
  }
    
  public void update(Disponibility disponibility) {

    String sql = "update disponibilities set status = ?, nights = ?, price = ?, startDate = ?, endDate = ? where idDisponibility = ? ";

    jdbcTemplate.update(sql, new Object[] { disponibility.getStatus(), disponibility.getNights(),disponibility.getPrice(),disponibility.getStartDate(),disponibility.getEndDate(), disponibility.getIdDisponibility() });
        
  }
    
  public Disponibility getDisponibility(int idDisponibility) {

    String sql = "select * from disponibilities where idDisponibility = " + idDisponibility; 
        
    List<Disponibility> disponibilities = jdbcTemplate.query(sql, new DisponibilityMapper());
        
    return disponibilities.size() > 0 ? disponibilities.get(0) : null;
  }
  
  public void delete( int id ) {
      
       String sql = "";
      
      if ( reservationService.getReservationsByDisponibility( id ).size() > 0 )
      {
          sql = " update disponibilities set deleted  = 1 where idDisponibility = ? ";
      }
      else
      {      
          sql = "delete from disponibilities where idDisponibility = ? ";
      }
      
      jdbcTemplate.update(sql, new Object[] { id });
  }
  
  public void deleteAll( int idUnit ) {
      
      String sql = "delete from disponibilities where idUnit = ? ";

      jdbcTemplate.update(sql, new Object[] { idUnit });
  }
  
  
  public List<Disponibility> getDisponibilitiesByUnit(int idUnit) {

    String sql = "select * from disponibilities where deleted <> 1 and idUnit = " + idUnit + " and now() < endDate"; 
        
    List<Disponibility> disponibilities = jdbcTemplate.query(sql, new DisponibilityMapper());
        
    return disponibilities;
  }
  
  public List<Disponibility> getNewestDisponibilities() {

    String sql = "select * from disponibilities d inner join units u on d.idUnit = u.idUnit where d.deleted <> 1 and u.validationStatus = 1 and now() < endDate order by u.creation desc limit 30"; 
        
    List<Disponibility> disponibilities = jdbcTemplate.query(sql, new DisponibilityMapper( true ));
        
    return disponibilities;
  }
  
  public List<Disponibility> getDisponibilitiesByFilter(UnitsSearch filter) {
     
      String sql = "";
      
      String f = " d.deleted <> 1 and u.validationStatus = 1 and now() < endDate ";
      
      f +=  filter.getRoom() > 0 ? f.isEmpty() ? " u.room >= " + filter.getRoom() : " and u.room >= " + filter.getRoom() : ""; 
      f +=  filter.getBedRoom() > 0 ? f.isEmpty() ? " u.bedroom >= " + filter.getBedRoom() : " and u.bedroom >= " + filter.getBedRoom() : ""; 
      f +=  filter.getBathRoom() > 0 ? f.isEmpty() ? " u.bathroom >= " + filter.getBathRoom() : " and u.bathroom >= " + filter.getBathRoom() : ""; 
      f +=  filter.getPerson() > 0 ? f.isEmpty() ? " u.person >= " + filter.getPerson() : " and u.person >= " + filter.getPerson() : ""; 
                  
      if ( filter.getStartDate() != null )
      {
          DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
          String date = dateFormat.format( filter.getStartDate() );
          
          f += " d.startDate <= '" + date + "' and d.endDate >= '" + date + "')"; 
      }
                   
      if ( filter.getIdUnit() > 0 )
      {
          sql = "from disponibilities d inner join units u on d.idUnit = u.idUnit where u.idUnit = " + filter.getIdUnit(); 
      }
      else if ( filter.getIdHotel() > 0 )
      {      
        sql = "from disponibilities d inner join units u on d.idUnit = u.idUnit where u.idHotel = " + filter.getIdHotel(); 
      }
      else if ( filter.getIdCountry() > 0 )
      {      
        sql = "from disponibilities d inner join units u on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where a.idCountry = " + filter.getIdCountry(); 
      }
      else if ( filter.getIdState() > 0 )
      {      
        sql = "from disponibilities d inner join units u on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where a.idState = " + filter.getIdState(); 
      }
      else if ( filter.getIdCity() > 0 )
      {      
        sql = "from disponibilities d inner join units u on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where a.idCity = " + filter.getIdCity(); 
      }
      else if ( !filter.getRegion().isEmpty() )
      {      
        sql = "from disponibilities d inner join units u on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where a.region = '" + filter.getRegion()+"'"; 
      }
      else if ( !filter.getSearchTerm().isEmpty() )
      {
         sql = "from disponibilities d inner join units u on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel " + 
               " inner join addresses a on h.idAddress = a.idAddress " + 
               " inner join location l1 on a.idCountry = l1.location_id " + 
               " inner join location l2 on a.idState = l2.location_id " + 
               " inner join location l3 on a.idCity = l3.location_id " +
               " where h.name like '%"+filter.getSearchTerm()+"%' " + 
               " or l1.name like '%"+filter.getSearchTerm()+"%' " + 
               " or l2.name like '%"+filter.getSearchTerm()+"%' " + 
               " or l3.name like '%"+filter.getSearchTerm()+"%' " ; 
      }
      
      if ( sql != null && !sql.isEmpty() )
      {
          sql += f.isEmpty() ? "" : " and " + f;
      }      
      else
      {
          sql = "from disponibilities d inner join units u on d.idUnit = u.idUnit " + (f.isEmpty() ? "" : " where " + f);
      }
      
      String countSQL = "select count(*) " + sql;
      
      
      int count = jdbcTemplate.queryForObject( countSQL, Integer.class);      
      int pages = (int)(count / filter.getPageSize()) + 1;
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
        
    List<Disponibility> disponibilities = jdbcTemplate.query( fullSQL, new DisponibilityMapper( true ));
        
    return disponibilities;
  }
  
//  public List<SearchResultItem> search( String name ) {
//
//    List<SearchResultItem> items = new ArrayList<SearchResultItem>();
//    
//    String sql = "select a.idAddress, a.idCountry, l.name, count(*) as regs from units u inner join disponibilities d on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idCountry = l.location_id  where l.location_type = 0 and l.name like  '%"+name+"%' group by a.idAddress, a.idCountry, l.name ";
//    items.addAll( jdbcTemplate.query(sql, new DisponibilityDaoImpl.SearchMapper( "País")) );
//    
//    sql = "select a.idAddress, a.idState, l.name, count(*) as regs from units u inner join disponibilities d on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idState = l.location_id  where l.location_type = 1 and l.name like '%"+name+"%' group by a.idAddress, a.idState, l.name ";
//    items.addAll( jdbcTemplate.query(sql, new DisponibilityDaoImpl.SearchMapper( "Estado")) );
//    
//    sql = "select a.idAddress, a.idCity, l.name, count(*) as regs from units u inner join disponibilities d on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idCity = l.location_id  where l.location_type = 2 and l.name like '%"+name+"%' group by a.idAddress, a.idCity, l.name ";
//    items.addAll( jdbcTemplate.query(sql, new DisponibilityDaoImpl.SearchMapper( "Cidade")) );
//   
//    sql = "select h.idHotel,h.name,a.idAddress, count(*) as regs from units u inner join disponibilities d on d.idUnit = u.idUnit inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress   where h.name like '%"+name+"%' group by h.idHotel";
//    
//    items.addAll( jdbcTemplate.query(sql, new DisponibilityDaoImpl.SearchMapper( "Hotel")) );
//        
//    return items;
//  }
  
  
class DisponibilityMapper implements RowMapper<Disponibility> {
    
    boolean loadUnit = false;
    
    public DisponibilityMapper()
    {}
    
    public DisponibilityMapper( boolean loadUnit )
    {
        this.loadUnit = loadUnit;
    }

  public Disponibility mapRow(ResultSet rs, int arg1) throws SQLException {
    Disponibility disponibility = new Disponibility();
            
    disponibility.setIdDisponibility(rs.getInt("idDisponibility"));
    disponibility.setIdUnit( rs.getInt("idUnit"));
    //disponibility.setStatus(rs.getInt("status"));
    disponibility.setNights(rs.getInt("nights"));
    disponibility.setPrice( rs.getDouble("price"));
    disponibility.setStartDate( rs.getDate("startDate"));
    disponibility.setEndDate( rs.getDate("endDate"));
    disponibility.setDeleted( rs.getInt("deleted") == 1);
    
    disponibility.setFinalPrice( disponibility.getPrice() + disponibility.getPrice() * 0.1 );
    
    boolean hasReservation = reservationService.hasActiveReservationForDisponibility(disponibility.getIdDisponibility());
    
    disponibility.setStatus( hasReservation ? Disponibility.STATUS_RESERVED : Disponibility.STATUS_AVAILABLE );
    
    if ( loadUnit )
    {
        Unit unit = unitService.getUnitWithoutDependencies( disponibility.getIdUnit(),true, false, false, false, false, false, false, false );
        
        disponibility.setUnit( unit );
    }
                
    return disponibility;
  }}

   
}


  
  
