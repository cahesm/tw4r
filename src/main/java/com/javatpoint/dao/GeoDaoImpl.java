package com.javatpoint.dao;

import com.javatpoint.model.GeoName;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;



public class GeoDaoImpl implements GeoDao {
  
  @Autowired
  JdbcTemplate jdbcTemplate;
          
  public GeoName getGeoName( int id ) {

    String sql = "select * from location where location_id = ?"; 
        
    List<GeoName> geoNames = jdbcTemplate.query(sql, new GeoMapper(),new Object[] { id });
        
    return geoNames.size() > 0 ? geoNames.get(0) : null;
  }
  
    
  public List<GeoName> getCountries() {

    String sql = "select * from location where location_type = 0 order by name"; 
        
    List<GeoName> countries = jdbcTemplate.query(sql, new GeoMapper());
        
    return countries;
  }
  
  public List<GeoName> getStates( int idParent ) {

    String sql = "select * from location where location_type = 1 and parent_id = ? order by name"; 
        
    List<GeoName> states = jdbcTemplate.query(sql, new GeoMapper(), new Object[] { idParent });
        
    return states;
  }
  
  public List<GeoName> getCities( int idParent ) {

    String sql = "select * from location where location_type = 2 and parent_id = ? order by name"; 
        
    List<GeoName> cities = jdbcTemplate.query(sql, new GeoMapper(), new Object[] { idParent });
        
    return cities;
  }
  
  
  
  class GeoMapper implements RowMapper<GeoName> {
      
  public GeoName mapRow(ResultSet rs, int arg1) throws SQLException {
    GeoName geo = new GeoName();
            
    geo.setId(rs.getInt("location_id"));    
    geo.setName(rs.getString("name"));
    
    
    return geo;
  }}

  
}


  
  
