package com.javatpoint.dao;

import com.javatpoint.model.Attribute;
import com.javatpoint.model.Disponibility;
import com.javatpoint.model.Hotel;
import com.javatpoint.model.ManagerUnitsSearch;
import com.javatpoint.model.Media;
import com.javatpoint.model.SearchResultItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.javatpoint.model.Unit;
import com.javatpoint.model.UnitAttribute;
import com.javatpoint.model.UnitCancelOption;
import com.javatpoint.model.UnitsSearch;
import com.javatpoint.model.User;
import com.javatpoint.service.AddressService;
import com.javatpoint.service.AttributeService;
import com.javatpoint.service.DisponibilityService;
import com.javatpoint.service.HotelService;
import com.javatpoint.service.MediaService;
import com.javatpoint.service.OptionService;
import com.javatpoint.service.ReservationService;
import com.javatpoint.service.UnitAttributeService;
import com.javatpoint.service.UnitCancelOptionService;
import com.javatpoint.service.UserService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


public class UnitDaoImpl implements UnitDao {
  
  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  AddressService addressService;
  
  @Autowired
  HotelService hotelService;
  
  @Autowired
  UserService userService;
  
  @Autowired
  DisponibilityService disponibilityService;
  
  @Autowired
  UnitCancelOptionService unitCancelOptionService;
  
  @Autowired
  MediaService mediaService;
  
  @Autowired
  AttributeService attributeService;
  
  @Autowired
  ReservationService reservationService;
  
  @Autowired
  UnitAttributeService unitAttributeService;
  
  @Autowired
  OptionService optionService;

  public void register( final Unit unit) {

    final String sql = "insert into units (cod,idUser,idHotel,idCancelOption,description,validationStatus,room,bedroom,bathroom,person,creation, cancelDays, cancelInfo) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    
    KeyHolder keyHolder = new GeneratedKeyHolder();
    	jdbcTemplate.update(
    	    new PreparedStatementCreator() {
    	        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	            PreparedStatement pst =
    	                con.prepareStatement(sql, new String[] {"cod","idUser","idHotel","idCancelOption","description","validationStatus","room","bedroom","bathroom","person","creation","cancelDays","cancelInfo"});
    	            pst.setString(1, "U" + RandomStringUtils.randomAlphanumeric(8).toUpperCase() );
    	            pst.setInt(2, unit.getIdUser());
    	            pst.setInt(3, unit.getIdHotel());    	            
    	            pst.setInt(4, unit.getIdCancelOption());    	            
    	            pst.setString(5, unit.getDescription());
    	            pst.setInt(6, unit.getValidationStatus());
    	            pst.setInt(7, unit.getRoom());
    	            pst.setInt(8, unit.getBedRoom());
    	            pst.setDouble(9, unit.getBathRoom());
                    pst.setInt(10, unit.getPerson());
    	            pst.setTimestamp(11, new Timestamp(System.currentTimeMillis()));    	            
                    pst.setInt(12, unit.getCancelDays());
                    pst.setString(13, unit.getCancelInfo());
    	            return pst;
    	        }
    	    },
    	    keyHolder); 
    
    int idUnit = keyHolder.getKey().intValue();
    //jdbcTemplate.update(sql, new Object[] { unit.getIdUser(), unit.getIdHotel(), unit.getDescription(), unit.getValidationStatus(), new Date() });
    
    if ( !unit.getDisponibilities().isEmpty() )
    {
        for (Disponibility disponibility : unit.getDisponibilities())
        {
            disponibility.setIdUnit( idUnit );
            disponibilityService.register(disponibility);
        }
    }
    
    if ( unit.getCancelOptions() != null && !unit.getCancelOptions().isEmpty() )
    {
        for (UnitCancelOption cancelOption : unit.getCancelOptions())
        {
            cancelOption.setIdUnit( idUnit );
            unitCancelOptionService.register( cancelOption );
        }
    }
    
    if ( !unit.getMedias().isEmpty() )
    {
        for (Media media : unit.getMedias())
        {
            media.setIdSource( idUnit );
            media.setType( Media.TYPE_UNIT_IMAGE );
            mediaService.register( media );
        }
    }
    
    if ( !unit.getAttributes().isEmpty() )
    {
        for (Attribute attribute : unit.getAttributes())
        {
            UnitAttribute unitAttribute = new UnitAttribute();
            unitAttribute.setIdAttribute( attribute.getIdAttribute() );
            unitAttribute.setIdUnit( idUnit );
            
            unitAttributeService.register( unitAttribute );
        }
    }
  }
    
  public void update(Unit unit) {

    String sql = "update units set idHotel = ?, idCancelOption = ?, description = ?, validationStatus = ?, room = ?, bedRoom = ?, bathRoom = ?, person = ?, cancelDays = ?, cancelInfo = ? where idUnit = ? ";

    jdbcTemplate.update(sql, new Object[] { unit.getIdHotel(), unit.getIdCancelOption(), unit.getDescription(), unit.getValidationStatus(), unit.getRoom(), unit.getBedRoom(), unit.getBathRoom(),unit.getPerson(), unit.getCancelDays(), unit.getCancelInfo(), unit.getIdUnit() });
      
    updateDisponibilities( unit );
    updateCancelOptions( unit );
    updateMedias( unit );
    updateAttributes( unit );
    
  }
  
  private void updateDisponibilities( Unit unit )
  {
      List<Disponibility> disponibilities = disponibilityService.getDisponibilitiesByUnit( unit.getIdUnit());
    
        if ( unit.getDisponibilities() != null && !unit.getDisponibilities().isEmpty() )
        {
            for ( Disponibility d : disponibilities )
            {
                if ( !unit.getDisponibilities().contains( d ) )
                {
                    disponibilityService.delete( d.getIdDisponibility() );                
                }
            }

            for ( Disponibility d : unit.getDisponibilities() )
            {
                if ( d.getIdDisponibility() > 0 )
                {
                    disponibilityService.update( d );
                }
                else
                {
                    d.setIdUnit( unit.getIdUnit() );
                    disponibilityService.register( d );
                }
            }        
        }
        else
        {
            disponibilityService.deleteAll( unit.getIdUnit() );
        }
  }
  
  private void updateCancelOptions( Unit unit )
  {
      List<UnitCancelOption> cancelOptions = unitCancelOptionService.getUnitCancelOptions( unit.getIdUnit() );
    
        if ( unit.getCancelOptions() != null && !unit.getCancelOptions().isEmpty() )
        {
            for ( UnitCancelOption c : cancelOptions )
            {
                if ( !unit.getCancelOptions().contains( c ) )
                {
                    unitCancelOptionService.delete( c.getIdCancelOption() );                
                }
            }

            for ( UnitCancelOption c : unit.getCancelOptions() )
            {
                if ( c.getIdCancelOption() > 0 )
                {
                    unitCancelOptionService.update( c );
                }
                else
                {
                    c.setIdUnit( unit.getIdUnit() );
                    unitCancelOptionService.register( c );
                }
            }        
        }
        else
        {
            unitCancelOptionService.deleteAll( unit.getIdUnit() );
        }
  }
   
  private void updateMedias( Unit unit )
  {
      List<Media> medias = mediaService.getMedias( Media.TYPE_UNIT_IMAGE, unit.getIdUnit());
    
        if ( unit.getMedias() != null && !unit.getMedias().isEmpty() )
        {
            for ( Media m : medias )
            {
                if ( !unit.getMedias().contains( m ) )
                {
                    mediaService.delete( m.getIdMedia() );                
                }
            }

            for ( Media m : unit.getMedias() )
            {
                if ( m.getIdMedia() > 0 )
                {
                    mediaService.update( m );
                }
                else
                {
                    m.setType( Media.TYPE_UNIT_IMAGE );
                    m.setIdSource( unit.getIdUnit() );
                    mediaService.register( m );
                }
            }        
        }
        else
        {
            mediaService.deleteAll( Media.TYPE_UNIT_IMAGE, unit.getIdUnit() );
        }
  }
  
    private void updateAttributes( Unit unit )
    {
        unitAttributeService.delete( unit.getIdUnit());

        if ( !unit.getAttributes().isEmpty() )
        {
            for (Attribute attribute : unit.getAttributes())
            {
                UnitAttribute unitAttribute = new UnitAttribute();
                unitAttribute.setIdAttribute( attribute.getIdAttribute() );
                unitAttribute.setIdUnit( unit.getIdUnit() );

                unitAttributeService.register( unitAttribute );
            }
        }

    }
         
  public void delete( int id ) 
  {
      
      String sql = "";
      
      if ( reservationService.getActiveReservationsByUnit( id ).isEmpty() )
      {
      
        if ( reservationService.getReservationsByUnit( id ).size() > 0 )
        {
            sql = " update units set deleted  = 1 where idUnit = ? ";

            jdbcTemplate.update(sql, new Object[] { id });
        }
        else
        {

          sql = "delete from disponibilities where idUnit = ? ";

          jdbcTemplate.update(sql, new Object[] { id });

          sql = "delete from medias where idSource = ? and type = ? ";

          jdbcTemplate.update(sql, new Object[] { id, Media.TYPE_UNIT_IMAGE });

          sql = "delete from units where idUnit = ? ";

          jdbcTemplate.update(sql, new Object[] { id });
        }
      }
      
  }
  
  public Unit getUnit(int idUnit) {

    String sql = "select * from units where idUnit = " + idUnit; 
        
    List<Unit> units = jdbcTemplate.query(sql, new UnitMapper());
        
    return units.size() > 0 ? units.get(0) : null;
  }
  
  public Unit getUnit(String cod) {

    String sql = "select * from units where cod = '" + cod + "'"; 
        
    List<Unit> units = jdbcTemplate.query(sql, new UnitMapper());
        
    return units.size() > 0 ? units.get(0) : null;
  }
  
  public Unit getUnitWithoutDependencies( int idUnit ) {

    String sql = "select * from units where idUnit = " + idUnit; 
        
    List<Unit> units = jdbcTemplate.query(sql, new UnitMapper( false, false, false, false, false, false, false, false ));
        
    return units.size() > 0 ? units.get(0) : null;
  }
  
  public Unit getUnitWithoutDependencies( int idUnit, boolean loadHotel, boolean loadUser, boolean loadDisponibilities, boolean loadMedias, boolean loadDocuments, boolean loadReservations, boolean loadAttributes, boolean loadCancelOption ) {

    String sql = "select * from units where idUnit = " + idUnit; 
            
    List<Unit> units = jdbcTemplate.query(sql, new UnitMapper( loadHotel, loadUser, loadDisponibilities, loadMedias, loadDocuments, loadReservations, loadAttributes, loadCancelOption ));
        
    return units.size() > 0 ? units.get(0) : null;
  }
  
  public List<Unit> getUnitsByUser(int idUser) {

    String sql = "select * from units where deleted <> 1 and idUser = " + idUser; 
        
    List<Unit> units = jdbcTemplate.query(sql, new UnitMapper());
        
    return units;
  }
  
  public List<Unit> getUnitsByHotel(int idHotel) {

    String sql = "select * from units where idHotel = " + idHotel; 
        
    List<Unit> units = jdbcTemplate.query(sql, new UnitMapper());
        
    return units;
  }
  
  public List<Unit> getUnitsByFilter(UnitsSearch filter) {
     
      String sql = "";
      
      String f = " u.deleted <> 1 ";
      
      f +=  filter.getRoom() > 0 ? f.isEmpty() ? " u.room = " + filter.getRoom() : " and u.room = " + filter.getRoom() : ""; 
      f +=  filter.getBedRoom() > 0 ? f.isEmpty() ? " u.bedroom = " + filter.getBedRoom() : " and u.bedroom = " + filter.getBedRoom() : ""; 
      f +=  filter.getBathRoom() > 0 ? f.isEmpty() ? " u.bathroom = " + filter.getBathRoom() : " and u.bathroom = " + filter.getBathRoom() : ""; 
      
      if ( filter.getStartDate() != null )
      {
          DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
          String date = dateFormat.format( filter.getStartDate() );
          
          f += " exists ( select 1 from disponibilities d where d.idUnit = u.idUnit and d.startDate <= '" + date + "' and d.endDate >= '" + date + "')"; 
      }
                   
      if ( filter.getIdUnit() > 0 )
      {
          sql = "from units u where u.idUnit = " + filter.getIdUnit(); 
      }
      else if ( filter.getIdHotel() > 0 )
      {      
        sql = "from units u where u.idHotel = " + filter.getIdHotel(); 
      }
      else if ( filter.getIdCountry() > 0 )
      {      
        sql = "from units u inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where a.idCountry = " + filter.getIdCountry(); 
      }
      else if ( filter.getIdState() > 0 )
      {      
        sql = "from units u inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where a.idState = " + filter.getIdState(); 
      }
      else if ( filter.getIdCity() > 0 )
      {      
        sql = "from units u inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where a.idCity = " + filter.getIdCity(); 
      }
      else if ( !filter.getRegion().isEmpty() )
      {      
        sql = "from units u inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where a.region = '" + filter.getRegion()+"'"; 
      }
      
      if ( sql != null && !sql.isEmpty() )
      {
          sql += f.isEmpty() ? "" : " and " + f;
      }      
      else
      {
          sql = "from units u " + (f.isEmpty() ? "" : " where " + f);
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
        
    List<Unit> units = jdbcTemplate.query( fullSQL, new UnitMapper());
        
    return units;
  }
  
  public List<Unit> getUnitsByFilter( ManagerUnitsSearch filter ) {
     
      String sql = "";
      
      String f = " u.deleted <> 1 ";
      
      f +=  !filter.getCod().isEmpty() ? f.isEmpty() ? " u.cod = '" + filter.getCod() + "'" : " and u.cod = '" + filter.getCod() + "'" : ""; 
      
      if ( filter.getOption() >= 0 )
      {
          f +=  f.isEmpty() ? " u.validationStatus = " + filter.getOption() : " and u.validationStatus = " + filter.getOption() ; 
      }
      
      if ( sql != null && !sql.isEmpty() )
      {
          sql += f.isEmpty() ? "" : " and " + f;
      }      
      else
      {
          sql = "from units u " + (f.isEmpty() ? "" : " where " + f);
      }
      
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
        
    List<Unit> units = jdbcTemplate.query( fullSQL, new UnitMapper());
        
    return units;
  }
  
  public List<Unit> getNotValidatedUnits()
  {
      String sql = "select * from units where deleted <> 1 and validationStatus = ?"; 
        
        List<Unit> units = jdbcTemplate.query(sql, new UnitMapper(), new Object[] { Unit.STATUS_NOT_VALIDATED });

        return units;
  }
  
  public List<Unit> getUnits()
  {
      String sql = "select * from units"; 
        
        List<Unit> units = jdbcTemplate.query(sql, new UnitMapper());

        return units;
  }
  
  public List<SearchResultItem> search( String name ) 
  {
   
    List<SearchResultItem> items = new ArrayList<SearchResultItem>();
    
    String sql = "select a.idCountry, l.name, count(*) as regs from units u inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idCountry = l.location_id  where u.validationStatus = 1 and l.location_type = 0 and l.name like  '%"+name+"%' group by a.idCountry, l.name ";
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "País")) );
    
    sql = "select a.idCountry, l.name, 0 as regs from hotels h inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idCountry = l.location_id where l.location_type = 0  and l.name like  '%"+name+"%' and not exists ( select 1 from units u inner join hotels h2 on u.idHotel = h2.idHotel inner join addresses a2 on h2.idAddress = a2.idAddress inner join location l2 on a2.idCountry = l2.location_id  where u.validationStatus = 1 and l2.location_type = 0 and l2.name = l.name )  group by a.idCountry, l.name ";
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "País")) );
            
    sql = "select a.idState, l.name, count(*) as regs from units u inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idState = l.location_id  where u.validationStatus = 1 and l.location_type = 1 and l.name like '%"+name+"%' group by  a.idState, l.name ";    
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "Estado")) );
    
    sql = "select a.idState, l.name, 0 as regs from hotels h inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idState = l.location_id  where l.location_type = 1 and l.name like '%"+name+"%' and not exists ( select 1 from units u inner join hotels h2 on u.idHotel = h2.idHotel inner join addresses a2 on h2.idAddress = a2.idAddress inner join location l2 on a2.idCountry = l2.location_id  where u.validationStatus = 1 and l2.location_type = 0 and l2.name = l.name ) group by  a.idState, l.name ";    
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "Estado")) );
    
    sql = "select a.idCity, l.name, count(*) as regs from units u inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idCity = l.location_id  where u.validationStatus = 1 and l.location_type = 2 and l.name like '%"+name+"%' group by  a.idCity, l.name ";
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "Cidade")) );
    
    sql = "select a.idCity, l.name, 0 as regs from hotels h inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idCity = l.location_id  where l.location_type = 2 and l.name like '%"+name+"%' and not exists ( select 1 from units u inner join hotels h2 on u.idHotel = h2.idHotel inner join addresses a2 on h2.idAddress = a2.idAddress inner join location l2 on a2.idCity = l2.location_id  where u.validationStatus = 1 and l2.location_type = 2 and l2.name = l.name ) group by  a.idCity, l.name ";
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "Cidade")) );
    
    sql = "select a.idCity, a.region, count(*) as regs from units u inner join hotels h on u.idHotel = h.idHotel inner join addresses a on h.idAddress = a.idAddress where u.validationStatus = 1 and a.region like '%"+name+"%' group by  a.idCity, a.region ";
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "Região")) );
    
    sql = "select a.idCity, a.region, 0 as regs from hotels h inner join addresses a on h.idAddress = a.idAddress where a.region like '%"+name+"%' and not exists( select 1 from units u inner join hotels h2 on u.idHotel = h2.idHotel inner join addresses a2 on h2.idAddress = a2.idAddress where u.validationStatus = 1 and a2.region = a.region ) group by  a.idCity, a.region ";
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "Região")) );
   
    sql = "select h.idHotel,h.name, count(*) as regs from units u inner join hotels h on u.idHotel = h.idHotel where u.validationStatus = 1 and h.name like '%"+name+"%' group by h.idHotel";    
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "Hotel")) );
    
    sql = "select h.idHotel,h.name, 0 as regs from hotels h where h.name like '%"+name+"%' and not exists ( select 1 from units u inner join hotels h2 on u.idHotel = h2.idHotel where u.validationStatus = 1 and h2.name = h.name ) group by h.idHotel";    
    items.addAll( jdbcTemplate.query(sql, new UnitDaoImpl.SearchMapper( "Hotel")) );
        
    return items;
  }
  
  
  
  class UnitMapper implements RowMapper<Unit> {
      
      //boolean loadDependencies = true;
      
      boolean loadHotel = true; 
      boolean loadUser = true; 
      boolean loadCancelOption = true; 
      boolean loadMedias = true; 
      boolean loadDisponibilities = true; 
      boolean loadDocuments = true; 
      boolean loadReservations = true;
      boolean loadAttributes = true;
      
      public UnitMapper()
      {
          //this( true, true, true );
      }
      
//      public UnitMapper( boolean loadDependencies )
//      {
//          this.loadDependencies = loadDependencies;
//      }
      public UnitMapper( boolean loadHotel, boolean loadUser, boolean loadDisponibilities, boolean loadMedias, boolean loadDocuments, boolean loadReservations, boolean loadAttributes, boolean loadCancelOption )
      {
        this.loadHotel = loadHotel; 
        this.loadUser = loadUser; 
        this.loadCancelOption = loadCancelOption; 
        this.loadDisponibilities = loadDisponibilities; 
        this.loadMedias = loadMedias; 
        this.loadDocuments = loadDocuments; 
        this.loadReservations = loadReservations;
        this.loadAttributes = loadAttributes;
      }

  public Unit mapRow(ResultSet rs, int arg1) throws SQLException {
    Unit unit = new Unit();
            
    unit.setIdUnit( rs.getInt("idUnit"));
    unit.setIdUser( rs.getInt("idUser"));
    unit.setIdHotel( rs.getInt("idHotel"));
    unit.setIdCancelOption( rs.getInt("idCancelOption"));
    unit.setCancelDays( rs.getInt("cancelDays"));
    unit.setDescription( rs.getString("description"));
    unit.setCancelInfo( rs.getString("cancelInfo"));
    unit.setValidationStatus( rs.getInt("validationStatus"));    
    unit.setRoom( rs.getInt("room"));    
    unit.setBedRoom( rs.getInt("bedroom"));    
    unit.setBathRoom( rs.getDouble( "bathroom")); 
    unit.setPerson( rs.getInt("person"));  
    unit.setCreation( rs.getTimestamp("creation"));
    unit.setCod( rs.getString("cod"));
    unit.setDeleted( rs.getInt("deleted") == 1 );
    
//    if ( loadDependencies )
//    {
        if ( loadHotel )
        {
            Hotel hotel = hotelService.getHotel( unit.getIdHotel() );
            unit.setHotel( hotel != null ? hotel : unit.getHotel());
        }
        
        if ( loadUser )
        {
            User user = userService.getUser( unit.getIdUser() );
            unit.setUser( user != null ? user : unit.getUser());
        }
        
        if ( loadCancelOption )
        {
            //Option option = optionService.getOption( unit.getIdCancelOption() );
            //unit.setCancelOption( option != null ? option : unit.getCancelOption() );
            unit.setCancelOptions( unitCancelOptionService.getUnitCancelOptions( unit.getIdUnit() ));
        }
        
        if ( loadDisponibilities )
        {
            unit.setDisponibilities( disponibilityService.getDisponibilitiesByUnit( unit.getIdUnit() ));
        }
        
        if ( loadMedias )
        {
            unit.setMedias( mediaService.getMedias( Media.TYPE_UNIT_IMAGE, unit.getIdUnit() ));
        }
        
        if ( loadDocuments )
        {
            unit.setDocuments( mediaService.getMedias( Media.TYPE_UNIT_DOCUMENT, unit.getIdUnit() ));
        }
        
        if ( loadReservations )
        {
            unit.setReservations( reservationService.getActiveReservationsByUnit( unit.getIdUnit() ));
        }
                
        if ( loadAttributes )
        {
                    
            List<UnitAttribute> unitAttributes = unitAttributeService.getUnitAttributes( unit.getIdUnit());

            ArrayList<Attribute> attributes = new ArrayList<Attribute>();

            if ( unitAttributes != null )
            {
                for ( UnitAttribute unitAttribute : unitAttributes )
                {
                    Attribute attribute = attributeService.getAttribute( unitAttribute.getIdAttribute());
                    attributes.add( attribute );
                }
            }

            unit.setAttributes( attributes );
        }
    

    return unit;
  }}

  class SearchMapper implements RowMapper<SearchResultItem> {
      
      private String term = "";
      
  public SearchMapper( String term )
  {
      this.term = term;
  }
      
  public SearchResultItem mapRow(ResultSet rs, int arg1) throws SQLException {
    
      SearchResultItem item = new SearchResultItem();
      item.setTerm( term );
      
      
      
        if ( term.equalsIgnoreCase( "Hotel" ) )
        {
              item.setId( rs.getInt("idHotel") );
              item.setName( rs.getString("name")  );
              item.setOccurrences( rs.getInt("regs")  );
        }
        else if ( term.equalsIgnoreCase( "País" ) ) 
        {                       
              item.setId( rs.getInt("idCountry") );
              item.setName( rs.getString("name")  );
              item.setOccurrences( rs.getInt("regs")  );
        }
        else if ( term.equalsIgnoreCase( "Estado" ) )       
        {  
              item.setId( rs.getInt("idState") );
              item.setName( rs.getString("name")  );
              item.setOccurrences( rs.getInt("regs")  );
        }
        else if ( term.equalsIgnoreCase( "Cidade" ) )       
        {               
              item.setId( rs.getInt("idCity") );
              item.setName( rs.getString("name")  );
              item.setOccurrences( rs.getInt("regs")  );
        }
        else if ( term.equalsIgnoreCase( "Região" ) )       
        {               
              item.setId( 0 );
              item.setName( rs.getString("region")  );
              item.setOccurrences( rs.getInt("regs")  );
        }
                  
      
      
//      String name = rs.getString("name") ;
//      
//      item.setId( rs.getInt("idHotel") );
//      
//      int idAddress = rs.getInt("idAddress");
//    
//      Address address = addressService.getAddress(idAddress);
//      
//      item.setName ( name + " - " + address.getCity() + " - " + address.getState() + " - " + address.getCountry() + " - " + address.getRegion() );
//            
      return item;
  }}
  
}


  
  
