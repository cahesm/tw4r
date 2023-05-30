package com.javatpoint.dao;

import com.javatpoint.model.Address;
import com.javatpoint.model.Hotel;
import com.javatpoint.model.HotelsSearch;
import com.javatpoint.model.Media;
import com.javatpoint.model.SearchResultItem;
import com.javatpoint.service.AddressService;
import com.javatpoint.service.MediaService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;



public class HotelDaoImpl implements HotelDao {
  
  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  AddressService addressService;
  
  @Autowired
  MediaService mediaService;
  
  
    
  public void register( final Hotel hotel) {

    if ( hotel.getAddress() != null )
    {
        final int addressId = addressService.register( hotel.getAddress() );
                        
        final String sql = "insert into hotels (name,site,idAddress) values(?,?,?)";

        //jdbcTemplate.update(sql, new Object[] { hotel.getName(),addressId });
        
        //final String sql = "insert into units (idUser,idHotel,description,validationStatus,room,bedroom,bathroom,creation) values(?,?,?,?,?,?,?,?)";

    
    KeyHolder keyHolder = new GeneratedKeyHolder();
    	jdbcTemplate.update(
    	    new PreparedStatementCreator() {
    	        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	            PreparedStatement pst =
    	                con.prepareStatement(sql, new String[] {"name","site","idAddress"});
    	            pst.setString( 1, hotel.getName());
    	            pst.setString( 2, hotel.getSite());
    	            pst.setInt(3, addressId);    	                	              	            
    	            return pst;
    	        }
    	    },
    	    keyHolder); 
    
        int idHotel = keyHolder.getKey().intValue();
        
        
        if ( !hotel.getMedias().isEmpty() )
        {
            for (Media media : hotel.getMedias())
            {
                media.setIdSource( idHotel );
                media.setType( Media.TYPE_HOTEL_IMAGE );
                mediaService.register( media );
            }
        }
        
    }  
      
      
    
  
    
    
  }
  
//  private void register(Address address) {
//
//        String sql = "insert into addresses (city,address,continent,country,region,state,neighborhood,number) values(?,?,?,?,?,?,?)";
//    	KeyHolder keyHolder = new GeneratedKeyHolder();
//    	jdbcTemplate.update(
//    	    new PreparedStatementCreator() {
//    	        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//    	            PreparedStatement pst =
//    	                con.prepareStatement(sql, new String[] {"id","address","continent","country","region","state",neighborhood,number});
//    	            pst.setString(1, name);
//    	            return pst;
//    	        }
//    	    },
//    	    keyHolder);  
//      
//      
//      
//    String sql = "insert into addresses (city,address,continent,country,region,state,neighborhood,number) values(?,?,?,?,?,?,?)";
//
//    jdbcTemplate.update(sql, new Object[] { address.getCity(), address.getAddress(), address.getContinent(), address.getCountry(),
//        address.getRegion(), address.getState(), address.getNeighborhood(), address.getNumber() });
//  }
  
  public void update( Hotel hotel ) {

    String sql = "update hotels set name = ?, site = ? where idHotel = ? ";

    jdbcTemplate.update(sql, new Object[] { hotel.getName(), hotel.getSite(), hotel.getIdHotel() });
    
    if ( hotel.getAddress() != null )
    {
        addressService.update( hotel.getAddress() );
    }        

      updateMedias( hotel );
  }
  
  private void updateMedias( Hotel hotel )
  {
      List<Media> medias = mediaService.getMedias( Media.TYPE_HOTEL_IMAGE, hotel.getIdHotel());
    
        if ( hotel.getMedias() != null && !hotel.getMedias().isEmpty() )
        {
            for ( Media m : medias )
            {
                if ( !hotel.getMedias().contains( m ) )
                {
                    mediaService.delete( m.getIdMedia() );                
                }
            }

            for ( Media m : hotel.getMedias() )
            {
                if ( m.getIdMedia() > 0 )
                {
                    mediaService.update( m );
                }
                else
                {
                    m.setType( Media.TYPE_HOTEL_IMAGE );
                    m.setIdSource( hotel.getIdHotel());
                    mediaService.register( m );
                }
            }        
        }
        else
        {
            mediaService.deleteAll( Media.TYPE_HOTEL_IMAGE, hotel.getIdHotel() );
        }
  }
  
//  private void update( Address address ) {
//
//    String sql = "update addresses set city = ?, address = ?,continent = ?, country = ?, region = ?, state = ?, neighborhood = ?, number = ? where idAddress = ? ";
//
//    jdbcTemplate.update(sql, new Object[] { address.getCity(), address.getAddress(),
//        address.getContinent(), address.getCountry(), address.getRegion(), address.getState(), address.getNeighborhood(),address.getNumber(), address.getIdAddress() });
//        
//  }

  public void delete( int id ) {
      
      String sql = "delete from hotels where idHotel = ? ";

      jdbcTemplate.update(sql, new Object[] { id });
  }
  
  
  public Hotel getHotel(String name) {

    String sql = "select * from hotels where name='" + name + "'"; 
        
    List<Hotel> hotels = jdbcTemplate.query(sql, new HotelMapper());
        
    return hotels.size() > 0 ? hotels.get(0) : null;
  }
  
  public Hotel getHotel(int id) {

    String sql = "select * from hotels where idHotel=" + id ; 
        
    List<Hotel> hotels = jdbcTemplate.query(sql, new HotelMapper());
        
    return hotels.size() > 0 ? hotels.get(0) : null;
  }
  
        
  public List<Hotel> getHotels() {

    String sql = "select * from hotels"; 
        
    List<Hotel> hotels = jdbcTemplate.query(sql, new HotelMapper());
        
    return hotels;
  }
  
  public List<Hotel> getHotelsByFilter( HotelsSearch filter ) {

    String sql = "";
      
      String f = "";
      
      if ( filter.getIdCountry() > 0 )
      {      
        sql = "from hotels h inner join addresses a on h.idAddress = a.idAddress where a.idCountry = " + filter.getIdCountry(); 
      }
      else if ( filter.getIdState() > 0 )
      {      
        sql = "from hotels h inner join addresses a on h.idAddress = a.idAddress where a.idState = " + filter.getIdState(); 
      }
      else if ( filter.getIdCity() > 0 )
      {      
        sql = "from hotels h inner join addresses a on h.idAddress = a.idAddress where a.idCity = " + filter.getIdCity(); 
      }
      else if ( !filter.getTerm().isEmpty() )
      {      
        sql = "from hotels h inner join addresses a on h.idAddress = a.idAddress " + 
                " inner join location l1 on a.idCountry = l1.location_id " + 
                " inner join location l2 on a.idState = l2.location_id " + 
                " inner join location l3 on a.idCity = l3.location_id " +
                " where h.name like '%" + filter.getTerm() + "%'" + 
                " or l1.name like '%"+filter.getTerm()+"%' " + 
                " or l2.name like '%"+filter.getTerm()+"%' " + 
                " or l3.name like '%"+filter.getTerm()+"%' " ;                                         
      }
      
      if ( sql != null && !sql.isEmpty() )
      {
          sql += f.isEmpty() ? "" : " and " + f;
      }      
      else
      {
          sql = " from hotels h " + (f.isEmpty() ? "" : " where " + f);
      }
      
      String countSQL = "select count(*) " + sql;
      String orderBy = " order by h.name ";
      
      
      int count = jdbcTemplate.queryForObject( countSQL, Integer.class);  
      int pages = (int)((count + filter.getPageSize() - 1) / filter.getPageSize());
      //int pages = (int)(count / filter.getPageSize()) + 1;
      int page = filter.getPage();
      
      if ( page > pages || page < 1 )
      {
          page = 1;
      }
      
      filter.setPages( pages );
      filter.setPage( page );
      filter.setTotal( count );
      
      int offset = (page - 1) * filter.getPageSize();
      
      String fullSQL = "select * " + sql + orderBy + " limit " + offset + "," + filter.getPageSize();
        
    List<Hotel> hotels = jdbcTemplate.query( fullSQL, new HotelMapper());
        
    return hotels;
  }
  
  public List<SearchResultItem> search( String name ) {

    List<SearchResultItem> items = new ArrayList<SearchResultItem>();
      
    String sql = "select * from hotels h inner join addresses a on h.idAddress = a.idAddress " +
                " inner join location l1 on a.idCountry = l1.location_id " + 
                " inner join location l2 on a.idState = l2.location_id " + 
                " inner join location l3 on a.idCity = l3.location_id " ;
        
    String where = " where l1.name like '%"+name+"%'";
    
    items.addAll( jdbcTemplate.query(sql + where, new SearchMapper( "País", false)) );
    
    where = " where l2.name like '%"+name+"%'";
    
    items.addAll( jdbcTemplate.query(sql + where, new SearchMapper( "Estado", false)) );
            
    where = " where l3.name like '%"+name+"%'";
    
    items.addAll( jdbcTemplate.query(sql + where, new SearchMapper( "Cidade", false)) );
    
    where = " where h.name like '%"+name+"%'";
    
    items.addAll( jdbcTemplate.query(sql + where, new SearchMapper( "Hotel", false)) );
        
    return items;
  }
  
  public List<SearchResultItem> searchGrouped( String name ) {

    List<SearchResultItem> items = new ArrayList<SearchResultItem>();

    
    String sql = "select a.idCountry, l.name, count(*) as regs from hotels h inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idCountry = l.location_id  where l.location_type = 0 and l.name like  '%"+name+"%' group by a.idCountry, l.name ";
    items.addAll( jdbcTemplate.query(sql, new HotelDaoImpl.SearchMapper( "País", true)) );
    
    sql = "select a.idState, l.name, count(*) as regs from  hotels h inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idState = l.location_id  where l.location_type = 1 and l.name like '%"+name+"%' group by a.idState, l.name ";
    items.addAll( jdbcTemplate.query(sql, new HotelDaoImpl.SearchMapper( "Estado", true)) );
    
    sql = "select a.idCity, l.name, count(*) as regs from  hotels h  inner join addresses a on h.idAddress = a.idAddress inner join location l on a.idCity = l.location_id  where l.location_type = 2 and l.name like '%"+name+"%' group by a.idCity, l.name ";
    items.addAll( jdbcTemplate.query(sql, new HotelDaoImpl.SearchMapper( "Cidade", true)) );
    
    sql = "select h.name, count(*) as regs from hotels h  inner join addresses a on h.idAddress = a.idAddress   where h.name like '%"+name+"%' group by h.name ";

    items.addAll( jdbcTemplate.query(sql, new HotelDaoImpl.SearchMapper( "Hotel", true)) );
        
    return items;
  }
  
  public Address getAddress( Hotel hotel ) 
  {          
    return addressService.getAddress( hotel.getAddress().getIdAddress() );    
  }
  
  class HotelMapper implements RowMapper<Hotel> {
      
  public Hotel mapRow(ResultSet rs, int arg1) throws SQLException {
    Hotel hotel = new Hotel();
            
    hotel.setIdHotel(rs.getInt("idHotel"));    
    hotel.setName(rs.getString("name"));
    hotel.setSite(rs.getString("site"));
    
    int idAddress = rs.getInt("idAddress");
    
    Address address = addressService.getAddress(idAddress);
    
    hotel.setAddress(address != null ? address : hotel.getAddress());
    
    hotel.setMedias( mediaService.getMedias( Media.TYPE_HOTEL_IMAGE, hotel.getIdHotel() ));
    
    return hotel;
  }}

  class SearchMapper implements RowMapper<SearchResultItem> {
      
      private String term = "";
      private boolean grouped = false;
      
  public SearchMapper( String term, boolean grouped )
  {
      this.term = term;
      this.grouped = grouped;
  }
      
  public SearchResultItem mapRow(ResultSet rs, int arg1) throws SQLException {
    
      SearchResultItem item = new SearchResultItem();
      item.setTerm( term );
                              
      if ( grouped )
      {
          if ( term.equalsIgnoreCase( "Hotel" ) )
            {                  
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
      }
      else
      {
            int idAddress = rs.getInt("idAddress");
    
            Address address = addressService.getAddress(idAddress);
            
            String name = rs.getString("name") ;
      
            item.setId( rs.getInt("idHotel") );
        
            item.setName (  name + " - " + address.getCity()+ " - " + address.getState()+ " - " + address.getCountry());
      }
      
                  
     
            
      return item;
  }}
  
}


  
  
