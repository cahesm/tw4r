package com.javatpoint.dao;

import com.javatpoint.model.Address;
import com.javatpoint.model.GeoName;
import com.javatpoint.service.GeoService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;



public class AddressDaoImpl implements AddressDao {

  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  GeoService geoService;
    
  public int register(final Address address) {

    final String sql = "insert into addresses (idContinent,idCountry,idState,idCity,address,region,neighborhood,number) values(?,?,?,?,?,?,?,?)";
    	
    KeyHolder keyHolder = new GeneratedKeyHolder();
    	jdbcTemplate.update(
    	    new PreparedStatementCreator() {
    	        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	            PreparedStatement pst =
    	                con.prepareStatement(sql, new String[] {"idContinent","idCountry","idState","idCity","address","region","neighborhood","number"});
    	            pst.setInt(1, address.getIdContinent());    	           
    	            pst.setInt(2, address.getIdCountry());
    	            pst.setInt(3, address.getIdState());    	            
    	            pst.setInt(4, address.getIdCity());
    	            pst.setString(5, address.getAddress());
    	            pst.setString(6, address.getRegion());
    	            pst.setString(7, address.getNeighborhood());
    	            pst.setString(8, address.getNumber());
    	            return pst;
    	        }
    	    },
    	    keyHolder);    
      
      return keyHolder.getKey().intValue();
    //String sql = "insert into addresses (idContinent,continent,idCountry,country,idState,state,idCity,city,address,region,neighborhood,number) values(?,?,?,?,?,?,?)";

    //jdbcTemplate.update(sql, new Object[] { address.getIdContinent(),address.getContinent(), address.getIdCountry(),address.getCountry(), address.getIdState(),address.getState(), address.getIdCity(), address.getCity(), address.getAddress(),
        //address.getRegion(), address.getNeighborhood(), address.getNumber() });
  }
  
    
  public void update( Address address ) {

    String sql = "update addresses set idContinent= ?, idCountry = ?,  idState = ?, idCity = ?, address = ?, region = ?, neighborhood = ?, number = ? where idAddress = ? ";

    jdbcTemplate.update(sql, new Object[] { address.getIdContinent(), address.getIdCountry(),address.getIdState(), address.getIdCity(), address.getAddress(),
        address.getRegion(),  address.getNeighborhood(),address.getNumber(), address.getIdAddress() });
        
  }

  
  
  public Address getAddress( int id ) {

    String sql = "select * from addresses where idAddress = " + id; 
        
    List<Address> addresses = jdbcTemplate.query(sql, new AddressMapper());
        
    return addresses.size() > 0 ? addresses.get(0) : null;
  }
  
  class AddressMapper implements RowMapper<Address> {

  public Address mapRow(ResultSet rs, int arg1) throws SQLException {
    
    Address address = new Address();

    address.setIdAddress(rs.getInt("idAddress"));
    address.setIdContinent(rs.getInt("idContinent"));
    address.setIdCountry(rs.getInt("idCountry"));
    address.setIdState(rs.getInt("idState"));
    address.setIdCity(rs.getInt("idCity"));
    
    address.setNumber( rs.getString("number"));    
    address.setAddress(rs.getString("address"));    
    address.setRegion(rs.getString("region"));    
    address.setNeighborhood(rs.getString("neighborhood"));
    
    GeoName city = geoService.getGeoName( address.getIdCity() );
    GeoName state = geoService.getGeoName( address.getIdState() );
    GeoName country = geoService.getGeoName( address.getIdCountry() );
    
    address.setCity( city.getName() );
    address.setState( state.getName() );
    address.setCountry( country.getName() );
        
    return address;
  }
}
    
}


  
  